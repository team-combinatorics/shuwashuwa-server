package team.combinatorics.shuwashuwa.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GlobalException extends RuntimeException {
    private Integer errCode;

    public GlobalException(ErrorInfoEnum errorInfoEnum) {
        super(errorInfoEnum.getErrMsg());
        this.errCode = errorInfoEnum.getErrCode();
    }
}

