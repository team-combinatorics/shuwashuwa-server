package team.combinatorics.shuwashuwa.model.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VolunteerApplicationAbstractBO {
    private Integer formId;
    private Timestamp createTime;
    private Integer userId;
    private String userName;
    private String department;
    private String grade;
    private Integer status;
}
