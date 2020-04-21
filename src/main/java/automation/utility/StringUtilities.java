package automation.utility;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

public class StringUtilities {

    public static String removeAllSpaces(String text){
        return text.replaceAll("\\s","");
    }

    public static String generateUniqueId(){
        String randomString = UUID.randomUUID().toString();
        return randomString.substring(0,randomString.length()-8);
    }

    public static String getFormattedDate(String datePattern) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
        String formattedDate = sdf.format(date);
        return formattedDate;
    }

    //can use to convert from epoch time to formatted time
    public static String getFormattedDate(long timeInMilliseconds, String datePattern){
        Date date = new Date(timeInMilliseconds);

        SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
        String formattedDate = sdf.format(date);
        return formattedDate;
    }
}


