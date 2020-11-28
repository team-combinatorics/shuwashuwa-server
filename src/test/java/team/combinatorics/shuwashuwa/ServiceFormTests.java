package team.combinatorics.shuwashuwa;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ServiceFormTests {

    @Test
    void generateData()
    {
        for(int i=1;i<=20;i++) {
            System.out.println("formid="+i);
            System.out.println("brand="+i*i);
            System.out.println("computer_model="+i*2);
            System.out.println("has_descrete_graphics="+(i&1));
            System.out.println("laptop_type="+i);
            System.out.println("bought_time="+"19190810");
            System.out.println("is_under_warranty="+0);
            System.out.println("problem_description="+i*3);
            System.out.println("description_editing_advice="+i);
            System.out.println("repairing_result="+i);
            System.out.println("status=、"+i);
            System.out.println("feedback="+i*4);
            System.out.println("activity_id="+i);
            System.out.println("time_slot="+i);
            /*
            private String problem_description;
    private String problem_type;
    private String decription_editing_advice;
    private String repairing_result;
    private int status;
    private String feedback;
    private int activity_id;
             */

        }
    }
}
