package team.combinatorics.shuwashuwa.dao.co;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SelectApplicationCO {
    /**
     * 申请表创建的开始时间
     */
    Timestamp beginTime;
    /**
     * 申请表创建的结束时间
     */
    Timestamp endTime;
    /**
     * UserID
     */
    Integer userID;
    /**
     * 申请表状态
     */
    Integer status;
    /**
     * 管理员id
     */
    Integer adminID;

}
