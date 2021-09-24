package com.yoon.rxjavatest._Library._Date;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by singleton on 2017. 9. 5..
 */

public class _DateFormat {

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    public static String ConvertFacebookDateformat(long time) {
        if (time < 1000000000000L) {
            //if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        long now = _Date.CurrentTimestamp();
        if (time > now || time <= 0) {
            return "in the future";
        }


        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "방금 전";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "1분 전";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + "분 전";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "한시간 전";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + "시간 전";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "어제";
        } else {
            return diff / DAY_MILLIS + "일 전";
        }
    }

    private static String getDate(long timeStamp){

        try{
            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date netDate = (new Date(timeStamp));
            return sdf.format(netDate);
        }
        catch(Exception ex){
            return "xx";
        }
    }

    public static long dateToLong(String timeStr){
        long milliseconds = 0;
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
            Date d = transFormat.parse(timeStr);
            milliseconds = d.getTime();
        }catch (Exception e){
            e.printStackTrace();
        }
        return milliseconds;
    }
}
