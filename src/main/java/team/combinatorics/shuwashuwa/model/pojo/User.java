package team.combinatorics.shuwashuwa.model.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Integer userid;
    private String openid;
    private String userName;
    private String nickName;
    private String phoneNumber;
    private String email;
    private String identity;
    private String department;
    private String grade;
    private String studentId;
    private String comment;
    private boolean volunteer;
    private boolean admin;
    private boolean su;
}