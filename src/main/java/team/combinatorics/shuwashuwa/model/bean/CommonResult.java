package team.combinatorics.shuwashuwa.model.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("统一返回结构")
public class CommonResult<T> {

    /** 状态码 */
    @ApiModelProperty("返回状态码")
    private int code;

    /** 信息 */
    @ApiModelProperty("返回的信息")
    private String message;

    /** 数据 */
    @ApiModelProperty("返回的数据")
    private T data;

}
