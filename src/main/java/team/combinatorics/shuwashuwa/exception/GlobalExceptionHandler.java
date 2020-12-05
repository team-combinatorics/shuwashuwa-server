package team.combinatorics.shuwashuwa.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import team.combinatorics.shuwashuwa.model.pojo.CommonResult;

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
        return new CommonResult<>(40000, "Unknown Error", e.getMessage());
    }


}