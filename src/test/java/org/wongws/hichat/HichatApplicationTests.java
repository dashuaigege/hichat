package org.wongws.hichat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.wongws.hichat.util.ImageProducerUtil;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HichatApplicationTests {

	@Test
	public void contextLoads() {
		try {
			ImageProducerUtil.create((long) 100, "wongws");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

