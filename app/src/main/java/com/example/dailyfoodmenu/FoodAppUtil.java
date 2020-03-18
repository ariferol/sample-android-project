package com.example.dailyfoodmenu;

import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public final class FoodAppUtil {

    public static Date getDateFromDatePicker(DatePicker datePicker){
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year =  datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return calendar.getTime();
    }

    public static String convertDateToStr(Date pDate){
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        return sdf.format(pDate);
    }

    public static String convertDateToDetailStr(Date pDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        SimpleDateFormat simpleDayFormat = new SimpleDateFormat("EEEE", new Locale("tr"));
        String result = sdf.format(pDate) + " " + simpleDayFormat.format(pDate);
        return result;
    }

    public static int getYear(Date pDate) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy");
        String year = df.format(pDate);
        return Integer.parseInt(year);
    }

    public static int getMonth(Date pDate) {
        SimpleDateFormat df = new SimpleDateFormat("MM");
        String month = df.format(pDate);
        return Integer.parseInt(month);
    }

    public static int getDay(Date pDate) {
        SimpleDateFormat df = new SimpleDateFormat("dd");
        String day = df.format(pDate);
        return Integer.parseInt(day);
    }
}
