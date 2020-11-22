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
public class LogInSuccessDto {
    private String token;
    private boolean isFirstLogin;
}
