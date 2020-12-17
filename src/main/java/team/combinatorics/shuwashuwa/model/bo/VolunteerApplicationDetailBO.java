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
public class VolunteerApplicationDetailBO {
    private Integer id;
    private Timestamp createTime;
    private Timestamp updatedTime;
    private Integer userId;
    private String reasonForApplication;
    private String replyByAdmin;
    private Integer adminId;
    private String adminName;
    private Integer status;
    private String cardPicLocation;
    private String userName;
    private String phoneNumber;
    private String email;
    private String department;
    private String grade;
    private String identity;
    private String studentId;
}
