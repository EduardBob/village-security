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
     * @param dateFormat
     * @return formatted <code>String</code> with <code>Date</code>.
     */
    public static String getCurrentDateString(String dateFormat) {
        DateFormat formatter = new SimpleDateFormat(dateFormat);
        formatter.setLenient(false);
        Date date = new Date();

        return formatter.format(date);
    }

    /**
     * Format input date String to formatted <code>Date</code>
     *
     * @param dateToFormat
     * @param format       of date
     * @return formatted <code>Date</code>
     */
    public static Date getFormattedDateFromString(String dateToFormat, String format) {
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            date = sdf.parse(dateToFormat);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * Convert date to date <code>String</code>
     *
     * @param date
     * @param format of date
     * @return dateString
     */
    public static String getDateStringFromDate(Date date, String format) {
        return (new SimpleDateFormat(format)).format(date);
    }

    /**
     * Format date with specified Date format with current device time zone in UTC
     *
     * @param dateString
     * @param format     of date
     * @return dateString local time
     */
    public static String formatDateWithTimeZone(String dateString, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);

        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));


        formatter.setLenient(false);
        Date date = null;
        try {
            date = formatter.parse(dateString);
        } catch (Exception e) {
            return "";
        }

        TimeZone tz = TimeZone.getDefault();
        SimpleDateFormat destFormat = new SimpleDateFormat(format);
        destFormat.setTimeZone(tz);

        String result = destFormat.format(date);

        //return formatter.format(date);
        return result;
    }

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
     * @param dateString
     * @param format
     * @return Date Object
     */
    public static Date getDateFromString(String dateString, String format) {
        DateFormat formatter = new SimpleDateFormat(format);
        formatter.setLenient(false);

        Date date = null;
        try {
            date = formatter.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
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

    public static String formatDate2(String dateString, String inFormat, String outFormat) {

        DateFormat inFormatter = new SimpleDateFormat(inFormat);
        inFormatter.setLenient(false);
        inFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        DateFormat outFormatter = new SimpleDateFormat(outFormat);
        outFormatter.setLenient(false);
        outFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));

        Date date = null;
        try {
            date = inFormatter.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }


        return outFormatter.format(date);
    }

    /**
     * Check date if is today will return outFormat else will return outFormatNotToday in UTC device time zone
     *
     * @param dateString
     * @param inFormat
     * @param outFormat
     * @param outFormatNotToday
     * @return String format date
     */
    public static String formatDateCheckIfTodayWithDefaultTimeZone(String dateString, String inFormat, String outFormat, String outFormatNotToday) {
        TimeZone tz = TimeZone.getDefault();
        SimpleDateFormat inFormatter = new SimpleDateFormat(inFormat);
        inFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));

        Date date = null;
        try {
            date = inFormatter.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(tz);

//        Long now = calendar.getTimeInMillis();
//        Long difference = now - date.getTime();
//        calendar.setTimeInMillis(difference);
//        android.text.format.DateUtils.isToday(date.getTime());
//        int day = calendar.getAcc(Calendar.DAY_OF_YEAR);  //TODO better solution used . look bellow ("android.text.format.DateUtils.isToday")

        SimpleDateFormat outFormatter;

        if (!android.text.format.DateUtils.isToday(date.getTime())) {
            outFormatter = new SimpleDateFormat(outFormatNotToday);
        } else {
            outFormatter = new SimpleDateFormat(outFormat);
        }
        outFormatter.setTimeZone(tz);

        return outFormatter.format(date);
    }

    public static String formateDate(String dateString, String inFormat, String thisMonth, String thisYear, String yearAgo) {
        TimeZone tz = TimeZone.getDefault();
        SimpleDateFormat inFormatter = new SimpleDateFormat(inFormat);
        inFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));

        Date date = null;
        try {
            date = inFormatter.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getDefault());

        Long oldDate = date.getTime();

        Calendar oldCalendar = Calendar.getInstance();
        oldCalendar.setTimeInMillis(oldDate);

        int day = calendar.get(Calendar.DATE) - oldCalendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH) - oldCalendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR) - oldCalendar.get(Calendar.YEAR);

        SimpleDateFormat outFormatter;

        if (year == 0) {
            //this year
            if (month == 0) {
                outFormatter = new SimpleDateFormat(thisMonth);
                outFormatter.setTimeZone(tz);
                return outFormatter.format(date);

            } else {
                outFormatter = new SimpleDateFormat(thisYear);
                outFormatter.setTimeZone(tz);
                return outFormatter.format(date);
            }
        } else {
            outFormatter = new SimpleDateFormat(yearAgo);
            outFormatter.setTimeZone(tz);
            return outFormatter.format(date);
        }

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

    public static boolean check2Date(String selectedDateTime, String selectedFormat, String dateFromText, String dateFromFormat) {
        SimpleDateFormat  format = new SimpleDateFormat(selectedFormat);
        Date dateTo = null;
        try {
            dateTo = format.parse(selectedDateTime);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        format = new SimpleDateFormat(dateFromFormat);
        Date dateFrom = null;
        try {
            dateFrom = format.parse(dateFromText);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return dateTo.before(dateFrom);
    }

    public static String getDateForm2DatesFromEvent(String selectedDateTime, String selectedFormat, String dateFromText, String dateFromFormat){
        TimeZone tz = TimeZone.getTimeZone("UTC");
        SimpleDateFormat  format = new SimpleDateFormat(selectedFormat);
        Date dateTo = null;
        try {
            dateTo = format.parse(selectedDateTime);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        format = new SimpleDateFormat(dateFromFormat);
        Date dateFrom = null;
        try {
            dateFrom = format.parse(dateFromText);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Long dateToLong = dateTo.getTime();

        Calendar dateToCalendar = Calendar.getInstance();
        dateToCalendar.setTimeInMillis(dateToLong);

        Long dateFromLong = dateFrom.getTime();

        Calendar dateFromCalendar = Calendar.getInstance();
        dateFromCalendar.setTimeInMillis(dateFromLong);

        int day = dateToCalendar.get(Calendar.DATE) - dateFromCalendar.get(Calendar.DATE);
        int month = dateToCalendar.get(Calendar.MONTH) - dateFromCalendar.get(Calendar.MONTH);
        int year = dateToCalendar.get(Calendar.YEAR) - dateFromCalendar.get(Calendar.YEAR);

        SimpleDateFormat outFormatter;

        StringBuilder builder = new StringBuilder();

        if (year == 0) {
            //this year
            if (month == 0) {
                //this month
                if (day == 0) {
                    outFormatter = new SimpleDateFormat(Date_FORMAT_19);
                    outFormatter.setTimeZone(tz);
                    builder.append(outFormatter.format(dateFrom));
                    builder.append(" - ");

                    outFormatter = new SimpleDateFormat(DATE_FORMAT_14);
                    outFormatter.setTimeZone(tz);
                    builder.append(outFormatter.format(dateTo));

                    return builder.toString();
                } else {
                    outFormatter = new SimpleDateFormat(Date_FORMAT_19);
                    outFormatter.setTimeZone(tz);
                    builder.append(outFormatter.format(dateFrom));

                    builder.append(" - ");

                    outFormatter = new SimpleDateFormat(Date_FORMAT_19);
                    outFormatter.setTimeZone(tz);
                    builder.append(outFormatter.format(dateTo));

                    return builder.toString();
                }
            } else {
                outFormatter = new SimpleDateFormat(Date_FORMAT_19);
                outFormatter.setTimeZone(tz);
                builder.append(outFormatter.format(dateFrom));

                builder.append(" - ");

                outFormatter = new SimpleDateFormat(Date_FORMAT_19);
                outFormatter.setTimeZone(tz);
                builder.append(outFormatter.format(dateTo));

                return builder.toString();
            }
        } else {
            outFormatter = new SimpleDateFormat(Date_FORMAT_19);
            outFormatter.setTimeZone(tz);
            builder.append(outFormatter.format(dateFrom));

            builder.append(" - ");

            outFormatter = new SimpleDateFormat(Date_FORMAT_19);
            outFormatter.setTimeZone(tz);
            builder.append(outFormatter.format(dateTo));

            return builder.toString();
        }
    }

    public static String getDateForm2DatesFromEventList(String selectedDateTime, String selectedFormat, String dateFromText, String dateFromFormat){
        TimeZone tz = TimeZone.getTimeZone("UTC");

        SimpleDateFormat  format = new SimpleDateFormat(selectedFormat);
        Date dateTo = null;
        try {
            dateTo = format.parse(selectedDateTime);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        format = new SimpleDateFormat(dateFromFormat);
        Date dateFrom = null;
        try {
            dateFrom = format.parse(dateFromText);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Long dateToLong = dateTo.getTime();

        Calendar dateToCalendar = Calendar.getInstance();
        dateToCalendar.setTimeInMillis(dateToLong);
        Long dateFromLong = dateFrom.getTime();

        Calendar dateFromCalendar = Calendar.getInstance();
        dateFromCalendar.setTimeInMillis(dateFromLong);

        int day = dateToCalendar.get(Calendar.DATE) - dateFromCalendar.get(Calendar.DATE);
        int month = dateToCalendar.get(Calendar.MONTH) - dateFromCalendar.get(Calendar.MONTH);
        int year = dateToCalendar.get(Calendar.YEAR) - dateFromCalendar.get(Calendar.YEAR);

        SimpleDateFormat outFormatter;

        StringBuilder builder = new StringBuilder();

        if (year == 0) {
            //this year
            if (month == 0) {
                //this month
                if (day == 0) {
                    outFormatter = new SimpleDateFormat(DATE_FORMAT_20);
                    outFormatter.setTimeZone(tz);

                    return outFormatter.format(dateFrom);
                } else {
                    outFormatter = new SimpleDateFormat(DATE_FORMAT_21);
                    outFormatter.setTimeZone(tz);
                    builder.append(outFormatter.format(dateFrom));

                    builder.append(" - ");

                    outFormatter = new SimpleDateFormat(DATE_FORMAT_20);
                    outFormatter.setTimeZone(tz);
                    builder.append(outFormatter.format(dateTo));

                    return builder.toString();
                }
            } else {
                outFormatter = new SimpleDateFormat(DATE_FORMAT_15);
                outFormatter.setTimeZone(tz);
                builder.append(outFormatter.format(dateFrom));

                builder.append(" - ");

                outFormatter = new SimpleDateFormat(DATE_FORMAT_20);
                outFormatter.setTimeZone(tz);
                builder.append(outFormatter.format(dateTo));

                return builder.toString();
            }
        } else {
            outFormatter = new SimpleDateFormat(DATE_FORMAT_20);
            outFormatter.setTimeZone(tz);
            builder.append(outFormatter.format(dateFrom));

            builder.append(" - ");

            outFormatter = new SimpleDateFormat(DATE_FORMAT_20);
            outFormatter.setTimeZone(tz);
            builder.append(outFormatter.format(dateTo));

            return builder.toString();
        }
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

    public static boolean checkIfTokenNotExpired(long expiresDate) {
        long currentDate = System.currentTimeMillis();

        if(currentDate>expiresDate){
            return false;
        }

        long diff = TimeUnit.MILLISECONDS.toMinutes(expiresDate-currentDate);

        if(diff < 10){
            Log.v(DateUtils.class.getSimpleName(), String.valueOf(diff) + "false");
            return false;
        }
        Log.v(DateUtils.class.getSimpleName(), String.valueOf(diff) + "true");
        return true;
    }

    //
}
