package team.combinatorics.shuwashuwa.model.dto;

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
public class UpdateUserInfoDto {
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
