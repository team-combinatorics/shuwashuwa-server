package team.combinatorics.shuwashuwa.model.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author kinami
 * @version 0.0.1
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private int userid;
    private String openid;
    private String user_name;
    private String nick_name;
    private String phone_number;
    private String email;
    private String identity;
    private String department;
    private String grade;
    private String student_id;
    private String comment;

}
