#基于dubbo的电商平台  
##技术方向
1、采用dubbo实现微服务架构  
2、nacos作为服务注册中心  
3、sentinel实现服务容错  
4、RocketMQ作为消息中间件，并解决分布式事务  
5、Swagger2作为前后端交互文档  
6、jenkins实现持续化集成  
7、SpringCloudGateway作为网关  
8、mysql作为数据库  
9、sharding-sphere实现数据库的分库分表  
10、redis作为缓存数据库，并使用布隆过滤器解决缓存穿透问题


##端口号说明  
	700x user用户模块  
	710x category分类模块  
	720x product产品模块  
	730x shipping收货地址  
	740x cart购物车模块  
	750x order订单模块  
##模块说明
common基本模块 存放工具类、实体类、借口等  
gateway网关
user用户模块  
category分类模块  
shipping收货地址 依赖 user  
product产品模块 依赖 category user  
cart购物车模块 依赖 user product   
order订单模块 product shipping cart user  




