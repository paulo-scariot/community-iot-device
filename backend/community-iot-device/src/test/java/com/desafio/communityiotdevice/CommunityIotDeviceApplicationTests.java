package com.desafio.communityiotdevice;

import com.desafio.communityiotdevice.config.schedule.ScheduledTasks;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class CommunityIotDeviceApplicationTests {

	@MockBean
	private ScheduledTasks scheduledTasks;

	@Test
	void contextLoads() {
	}

}
