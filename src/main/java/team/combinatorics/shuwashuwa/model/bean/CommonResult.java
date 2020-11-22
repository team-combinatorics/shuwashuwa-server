package team.combinatorics.shuwashuwa.model.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResult<T> {

    /** 状态码 */
    private int code;

    /** 信息 */
    private String message;

    /** 数据 */
    private T data;

}
