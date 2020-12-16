package team.combinatorics.shuwashuwa.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import team.combinatorics.shuwashuwa.model.pojo.CommonResult;

import java.util.Arrays;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = KnownException.class)
    public CommonResult<String> knownExceptionHandler(KnownException se) {
        se.printStackTrace();
        return new CommonResult<>(se.getErrCode(),"Error",se.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    public CommonResult<String> defaultExceptionHandler(Exception e) {
        e.printStackTrace();
        // 开发阶段，直接返回这个好了
        // return new CommonResult<>(40000, "Unknown Error", e.getMessage());
        // 开发阶段，直接返回这个好了
        return new CommonResult<>(40000, "Unknown Error", Arrays.toString(e.getStackTrace()));
    }


}