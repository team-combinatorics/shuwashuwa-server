package team.combinatorics.shuwashuwa.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "志愿者更新维修事件的传输对象")
public class ServiceEventUpdateByVolunteerDTO {
    @ApiModelProperty("维修事件id")
    private Integer eventID;
    @ApiModelProperty("维修结果说明")
    private String repairingResult;
    @ApiModelProperty("维修事件状态")
    private Integer status;
    // TODO (to leesou and leo_h) 注意这个用于更新的dto添加了两个在维修事件中补充的属性，请修改对应的地方
    @ApiModelProperty("是否有草稿状态的维修单")
    private Boolean draft;
    @ApiModelProperty("该维修事件是否关闭")
    private Boolean closed;
}
