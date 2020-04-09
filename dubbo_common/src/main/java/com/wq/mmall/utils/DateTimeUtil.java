package com.wq.mmall.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Created by geely
 */
public class DateTimeUtil {

    //joda-time

    //str->Date
    //Date->str
    public static final String STANDARD_FORMAT = "yyyy-MM-dd HH:mm:ss";
   private static final SimpleDateFormat sdf =new SimpleDateFormat(STANDARD_FORMAT);

    public static Date strToDate(String dateTimeStr){
        Date parse = null;
        try {
            parse = sdf.parse(dateTimeStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return parse;

    }

    public static String dateToStr(Date date){
        if (date==null)
            return sdf.format(new Date());
        return sdf.format(date);
    }

}
