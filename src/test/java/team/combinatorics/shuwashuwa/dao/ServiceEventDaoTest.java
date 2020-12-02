package team.combinatorics.shuwashuwa.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import team.combinatorics.shuwashuwa.MainApplication;
import team.combinatorics.shuwashuwa.model.dto.ServiceEventUpdateByVolunteerDTO;
import team.combinatorics.shuwashuwa.model.po.ServiceEventPO;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MainApplication.class)
public class ServiceEventDaoTest {

    @Autowired
    private ServiceEventDao serviceEventDao;

    @Test
    public void simpleTest() {
        ServiceEventPO serviceEventPO = ServiceEventPO.builder()
                .activityId(1)
                .timeSlot(1).build();
        serviceEventDao.insertServiceEvent(1, serviceEventPO);
        System.out.println(serviceEventPO.getId());
        serviceEventDao.updateByVolunteer(1,
                ServiceEventUpdateByVolunteerDTO.builder()
                        .eventID(1)
                        .repairingResult("修不了，告辞")
                        .status(2)
                        .build());
        serviceEventDao.updateAppointment(1, 2, 3);
        serviceEventDao.updateFeedback(1, "志愿者太菜了，建议开除");


    }
}
