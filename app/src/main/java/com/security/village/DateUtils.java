package com.security.village;

/**
 * Created by fruitware on 12/24/15.
 */

import android.app.Application;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class DateUtils {
    public static final String DATE_FORMAT_ISO_8601 = "yyyy-MM-dd'T'HH:mm:ss.SSZ";
    ;
    public static final String DATE_FORMAT_1 = "dd.MM.yyyy HH:mm:ss";
    public static final String DATE_FORMAT_2 = "yyyy-MM-dd'T'HH:mm:ssZ";
    public static final String DATE_FORMAT_3 = "EEE, d MMM, yyyy";
    public static final String DATE_FORMAT_4 = "dd.MM.yyyy HH:mm";
    public static final String DATE_FORMAT_5 = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_6 = "yyyy-MM-dd HH:mm";
    public static final String DATE_FORMAT_7 = "yyyy-MM-dd";
    public static final String DATE_FORMAT_8 = "dd.MM.yyyy";
    public static final String DATE_FORMAT_9 = "EEE d, yyyy";
    public static final String DATE_FORMAT_10 = "EEE d, yyyy HH:mm";
    public static final String DATE_FORMAT_11 = "EEE d, HH:mm";
    public static final String DATE_FORMAT_12 = "MMM d, yyyy";
    public static final String DATE_FORMAT_13 = "MMM EEE d. yyy HH:mm";
    public static final String DATE_FORMAT_14 = "HH:mm";
    public static final String DATE_FORMAT_15 = "d MMM";
    public static final String DATE_FORMAT_16 = "MMM d";
    public static final String DATE_FORMAT_17 = "MMM d, HH:mm";
    public static final String DATE_FORMAT_18 = "HH:mm, MMM d, yyyy";
    public static final String Date_FORMAT_19 = "d MMM yyyy, HH:mm";
    public static final String DATE_FORMAT_20 = "d MMM yyyy";
    public static final String DATE_FORMAT_21 = "d";
    public static final String DATE_FORMAT_22 = "d-MM-yyyy";
    public static final String DATE_FORMAT_23 = "d MMM yyyy";
    public static final String DATE_FORMAT_24 = "d MMM yyyy, HH:mm";




    /**
     * @param dateString
     * @param format
     * @return format date without time zone
     */
    public static String formatDateWithoutTimeZone(String dateString, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

        formatter.setLenient(false);
        Date date = null;
        try {
            date = formatter.parse(dateString);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return formatter.format(date);
    }

    /**
     * Format date with specified Date format
     *
     * @param dateString
     * @param inFormat   format of input date
     * @param outFormat  format of result date
     * @return dateString
     */
    public static String formatDate(String dateString, String inFormat, String outFormat) {
        DateFormat inFormatter = new SimpleDateFormat(inFormat);
        inFormatter.setLenient(false);
        DateFormat outFormatter = new SimpleDateFormat(outFormat);
        outFormatter.setLenient(false);

        Date date = null;
        try {
            date = inFormatter.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }


        return outFormatter.format(date);
    }

    public static boolean checkDateMinToday(String dateString,String formatDate){
        SimpleDateFormat  format = new SimpleDateFormat(formatDate);
        Date date = null;
        try {
            date = format.parse(dateString);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Date currentDate = new Date(System.currentTimeMillis()-100000);

        return date.before(currentDate);
    }

    public static Calendar getCalendarFromDate(String date){
        SimpleDateFormat  format = new SimpleDateFormat(DATE_FORMAT_5);
        Date dateTo = null;
        try {
            dateTo = format.parse(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dateTo.getTime());

        return calendar;
    }

    public static Calendar getCalendarFromDate(String date,String dateFormat){
        SimpleDateFormat  format = new SimpleDateFormat(dateFormat);
        Date dateTo = null;
        try {
            dateTo = format.parse(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dateTo.getTime());

        return calendar;
    }

}
