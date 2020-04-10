package com.wq.mmall.service;

import com.wq.mmall.dao.CategoryDao;
import com.wq.mmall.entity.Category;
import com.wq.mmall.exception.MmallException;
import com.wq.mmall.message.Arg;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.apache.dubbo.rpc.RpcException;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * Created by weiqiang
 */

@Service
@Slf4j
@Transactional(rollbackFor = {RpcException.class, MmallException.class})
public class CategoryServiceImpl implements ICategoryService {


    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private RocketMQTemplate rocketMQTemplate;


    @Override
    public List<Category> getChildrenParallelCategory(Integer categoryId) throws MmallException {
        //发送办消息
        this.rocketMQTemplate.sendMessageInTransaction("test_transaction",
                "topic",
                MessageBuilder.withPayload(Arg.builder().mes("aaaaaaaa").build())
                        .setHeader(RocketMQHeaders.TRANSACTION_ID,UUID.randomUUID().toString())
                        .setHeader("share_id",categoryId)
                        .build(),"ok");


        List<Category> categoryList = categoryDao.findByParentId(categoryId);
        if (CollectionUtils.isEmpty(categoryList)) {
            throw new MmallException("未找到当前分类的子分类");
        }
        return categoryList;
    }

    @Override
    public Category selectById(Integer categoryId) throws MmallException {
        Optional<Category> category = categoryDao.findById(categoryId);
        if (category.isPresent())
            return category.get();
        throw new MmallException("该分类不存在");
    }

    /**
     * 递归查询本节点的id及孩子节点的id
     */
    @Override
    public List<Integer> selectCategoryAndChildrenById(Integer categoryId) {
        Set<Category> categorySet = new HashSet<>();
        findChildCategory(categorySet, categoryId);

        List<Integer> categoryIdList = new ArrayList<>();
        if (categoryId != null) {
            for (Category categoryItem : categorySet) {
                categoryIdList.add(categoryItem.getId());
            }
        }
        return categoryIdList;
    }

    @Override
    public String deleteCategory(Integer categoryId) {
        Set<Category> categorySet = new HashSet<>();
        findChildCategory(categorySet, categoryId);
        for (Category ignored : categorySet) {
            categoryDao.deleteById(categoryId);

        }
        return "删除分类成功";
    }

    @Override
    public String updateCategory(Category category) throws MmallException {
        Category category1 = categoryDao.saveAndFlush(category);
        if (null == category1)
            throw new MmallException("删除分类失败");
        return "更新分类成功";
    }

    private void findChildCategory(Set<Category> categorySet, Integer categoryId) {
        Optional<Category> category = categoryDao.findById(categoryId);
        category.ifPresent(categorySet::add);
        List<Category> categoryList = categoryDao.findByParentId(categoryId);
        for (Category categoryItem : categoryList) {
            findChildCategory(categorySet, categoryItem.getId());
        }
    }

}