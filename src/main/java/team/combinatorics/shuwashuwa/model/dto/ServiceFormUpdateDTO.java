package team.combinatorics.shuwashuwa.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "管理员审核维修单时传输的数据")
public class ServiceFormUpdateDTO {
    @ApiModelProperty("维修单id")
    private Integer formID;
    @ApiModelProperty("修改建议")
    private String advice;
    // TODO (to leesou and leo_h)注意这里的status变量删去了，请注意有没有什么地方引用到了
    // 确认完毕之后请删除此处注释掉的代码
//    @ApiModelProperty("更改后的维修单状态")
//    private Integer status;
}
