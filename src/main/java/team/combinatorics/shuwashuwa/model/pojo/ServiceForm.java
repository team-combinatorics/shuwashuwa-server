package team.combinatorics.shuwashuwa.model.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceForm {
    private int formid;
    private String brand;
    private String computer_model;
    private String cpu_model;
    private int has_discrete_graphics;
    private String graphics_model;
    private String laptop_type;
    private String bought_time;
    private int is_under_warranty;
    private String problem_description;
    private String problem_type;
    private String decription_editing_advice;
    private String repairing_result;
    private int status;
    private String feedback;
    private int activity_id;
    private int time_slot;
}
