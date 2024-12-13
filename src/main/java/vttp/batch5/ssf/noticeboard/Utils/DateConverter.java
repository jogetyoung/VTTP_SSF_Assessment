package vttp.batch5.ssf.noticeboard.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter {

    public static Date convertDate(String dateString) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try{
            Date date = sdf.parse(dateString);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static long datetoEpoch(Date date) {
        //get time in milliseconds since 1 January, 1970
        return date.getTime();

    }
}
