package team.combinatorics.shuwashuwa.model.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author KiNAmi
 * 一个用于表示一张图片的类，包括图片id和图片位置，和数据库中储存的相对应
 * todo 此对象在业务中不再有用
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ServicePic {
    private Integer id;
    private String picLocation;
}