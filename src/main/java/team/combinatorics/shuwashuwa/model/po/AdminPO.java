package team.combinatorics.shuwashuwa.model.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminPO {
    private Integer id;
    private Timestamp createTime;
    private Timestamp updatedTime;
    private String userid;
    private String userName;
    private String phoneNumber;
    private String email;
    private String identity;
    private String department;
    private String studentId;
}