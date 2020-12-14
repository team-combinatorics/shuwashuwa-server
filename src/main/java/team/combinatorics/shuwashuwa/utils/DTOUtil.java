package team.combinatorics.shuwashuwa.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

public class DTOUtil {
    static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static boolean fieldAllNull(Object obj) {
        for (Field field:obj.getClass().getDeclaredFields()){
            if(field.getName().equals("this$0"))
                continue;
            if(obj.getClass().toString().equals("class team.combinatorics.shuwashuwa.model.dto.AdminDTO")
                    && field.getName().equals("userid")) {
                try {
                    field.setAccessible(true);
                    if(field.get(obj) == null)
                        return true;
                    continue;
                }catch (IllegalAccessException e) {
                    System.out.println(field.getName()+" is not accessible");
                }
            }

            try {
                field.setAccessible(true);
                if(field.get(obj) != null)
                    return false;
            }
            catch (IllegalAccessException e){
                System.out.println(field.getName()+" is not accessible");
            }
        }
        return true;
    }

    public static boolean fieldExistNull(Object obj) {
        for (Field field:obj.getClass().getDeclaredFields()){
            if(field.getName().equals("this$0"))
                continue;
            try {
                field.setAccessible(true);
                if(field.get(obj) == null)
                    return true;
            }
            catch (IllegalAccessException e){
                System.out.println(field.getName()+" is not accessible");
            }
        }
        return false;
    }
    public static String stamp2str(Timestamp timestamp) {
        return dateFormat.format(timestamp);
    }

    @Deprecated
    public static Timestamp str2stamp(String format) {
        return Timestamp.valueOf(format);
    }

    public static Object convert(Object source, Class<?> targetClass){
        try {
            Object target = targetClass.getDeclaredConstructor().newInstance();
            Object[] sourceFields = Arrays.stream(source.getClass().getDeclaredFields())
                    .map(Field::getName).toArray();
            Set<Object> targetFields = Arrays.stream(targetClass.getDeclaredFields())
                    .map(Field::getName).collect(Collectors.toSet());
            for (Object fieldName : sourceFields) {
                if (fieldName.equals("this$0"))
                    continue;
                if (targetFields.contains(fieldName)) {
                    String fieldNameCap = fieldName.toString().substring(0, 1).toUpperCase(Locale.ROOT) +
                            fieldName.toString().substring(1);
                    Method getter = source.getClass().getMethod("get" + fieldNameCap);
                    Method setter = targetClass.getMethod("set" + fieldNameCap, targetClass.
                            getDeclaredField(fieldName.toString()).getType());
                    Object value = getter.invoke(source);
                    if (getter.getReturnType() == Timestamp.class)
                        value = stamp2str((Timestamp) value);
                    if (setter.getParameterTypes()[0] == Timestamp.class)
                        value = Timestamp.valueOf((String) value);
                    setter.invoke(target, value);
                }
            }
            return target;
        } catch (Exception e) {
            System.err.println("DTO convert failed");
            System.out.println(source);
            e.printStackTrace();
            return null;
        }
    }
}
