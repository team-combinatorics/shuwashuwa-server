package team.combinatorics.shuwashuwa.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ShuwarinException extends RuntimeException {
    private Integer errCode;

    public ShuwarinException(ErrorEnum errorEnum) {
        super(errorEnum.getErrMsg());
        this.errCode = errorEnum.getErrCode();
    }
}

