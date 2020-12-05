package team.combinatorics.shuwashuwa.model.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityInfoPO {
    private int activity_id;
    private String position;
    private String starting_time;
}
