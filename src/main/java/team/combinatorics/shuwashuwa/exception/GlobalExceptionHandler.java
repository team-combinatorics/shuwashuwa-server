package team.combinatorics.shuwashuwa.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import team.combinatorics.shuwashuwa.model.pojo.CommonResult;

import java.util.*;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = KnownException.class)
    public CommonResult<String> knownExceptionHandler(KnownException se) {
        return new CommonResult<>(se.getErrCode(), "Error", se.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    public CommonResult<Map<String, Object>> defaultExceptionHandler(Exception e) {
        // return new CommonResult<>(40000, "Unknown Error", e.getMessage());
        // 开发阶段，直接返回这个好了
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("errMsg", e.getMessage());
        returnMap.put("errStackTrace", e.getStackTrace());
        System.out.println(e.getMessage());


        return new CommonResult<>(40000, "Unknown Error", returnMap);
    }


}