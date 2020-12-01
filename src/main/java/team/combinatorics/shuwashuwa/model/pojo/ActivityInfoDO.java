package team.combinatorics.shuwashuwa.model.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityInfoDO {
    private int activity_id;
    private String position;
    private String starting_time;
}
