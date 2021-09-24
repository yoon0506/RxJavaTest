package com.yoon.rxjavatest._Library._Date;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import timber.log.Timber;

/**
 * Created by singleton on 2017. 9. 6..
 */

public class _Date {

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    public static Date CurrentDate() {
        Date mmDate = new Date();
        DateFormat mmDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        TimeZone mmTZ = TimeZone.getTimeZone("Asia/Seoul");
        mmDF.setTimeZone(mmTZ);
        mmDF.setCalendar(Calendar.getInstance());
        return mmDF.getCalendar().getTime();
    }

    public static String CurrentDatePoint() {
        Date mmDate = new Date();
        SimpleDateFormat mmDF = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm");
        String mmCurrentDate = mmDF.format(mmDate);
        return mmCurrentDate;
    }

    public static long CurrentTimestamp() {
        return _Date.CurrentDate().getTime();
    }

    public static long CalTimeStamp(long exTime, long curTime){
        return ((curTime-exTime)/60000);
    }

    // 현재 기준으로 한달 후인지 아닌지 계산
    public static boolean cal31Days(long time) {
        if (time < 1000000000000L) {
            //if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        long now = _Date.CurrentTimestamp();
        if (time > now || time <= 0) {
            return true;
        }


        final long diff = now - time;

        Timber.tag("CHECK_TIME").d(" %s",diff / DAY_MILLIS);

        if (diff / DAY_MILLIS > 31) {
            return true;
        } else {
            return false;
        }
    }

    // 현재 기준으로 한시간 이내인지 아닌지 계산
    public static boolean cal1Hours(long time) {
        if (time < 1000000000000L) {
            //if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        long now = _Date.CurrentTimestamp();
        if (time > now || time <= 0) {
            return true;
        }


        final long diff = now - time;

//        Timber.tag("CHECK_TIME").d("시간 확인 : %s",diff / MINUTE_MILLIS);

        if (diff / MINUTE_MILLIS < 61) {
            return true;
        } else {
            return false;
        }
    }
}
