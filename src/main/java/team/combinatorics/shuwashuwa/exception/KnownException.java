package team.combinatorics.shuwashuwa.exception;

import lombok.Getter;

@Getter
public class KnownException extends RuntimeException {
    private final Integer errCode;

    public KnownException(ErrorInfoEnum errorInfoEnum) {
        super(errorInfoEnum.getErrMsg());
        this.errCode = errorInfoEnum.getErrCode();
    }
}

