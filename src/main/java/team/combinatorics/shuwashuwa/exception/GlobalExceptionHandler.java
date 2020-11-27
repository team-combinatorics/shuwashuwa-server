package team.combinatorics.shuwashuwa.exception;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import team.combinatorics.shuwashuwa.model.bean.CommonResult;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = GlobalException.class)
    public CommonResult<String> globalExceptionHandler(GlobalException se) {
        se.printStackTrace();
        return new CommonResult<>(se.getErrCode(),"Error",se.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    public CommonResult<String> toHandlerException(Exception e) {
        e.printStackTrace();
        if(e instanceof DuplicateKeyException){
            return new CommonResult<>(40001, "Error", "用户名重复");
        }
        return new CommonResult<>(40001, "Error", "?");
    }


}