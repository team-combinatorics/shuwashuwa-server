package team.combinatorics.shuwashuwa.model.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VolunteerApplicationDO {
    private Integer id;
    private Timestamp createTime;
    private Timestamp updatedTime;
    private Integer userId;
    private String comment;
    private Integer status;
}