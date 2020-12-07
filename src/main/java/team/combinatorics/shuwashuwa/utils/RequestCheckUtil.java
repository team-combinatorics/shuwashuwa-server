package team.combinatorics.shuwashuwa.utils;

import java.lang.reflect.Field;

public class RequestCheckUtil {

    public static boolean fieldAllNull(Object obj) {
        for (Field field:obj.getClass().getDeclaredFields()){
            if(field.getName().equals("this$0"))
                continue;
            if(obj.getClass().toString().equals("class team.combinatorics.shuwashuwa.model.dto.AdminDTO")
                    && field.getName().equals("userid"))
                continue;
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
}
