package team.combinatorics.shuwashuwa.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class TimeFormatUtil {
    static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String stamp2str(Timestamp timestamp) {
        return dateFormat.format(timestamp);
    }

    @Deprecated
    public static Timestamp str2stamp(String format) {
        return Timestamp.valueOf(format);
    }
}
