package team.combinatorics.shuwashuwa.model.so;

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
public class VolunteerApplicationAbstract {
    private Integer id;
    private Timestamp createTime;
    private Integer userId;
    private String userName;
    private String department;
    private String grade;
    private Integer status;
}
