package team.combinatorics.shuwashuwa.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GlobalException extends RuntimeException {
    private Integer errCode;

    public GlobalException(ErrorEnum errorEnum) {
        super(errorEnum.getErrMsg());
        this.errCode = errorEnum.getErrCode();
    }
}

