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
    @ApiModelProperty("维修单状态")
    private Integer status;
}
