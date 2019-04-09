package org.wongws.hichat.function;

import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wongws.hichat.util.ImageProducerUtil;

public class CreateUserImg implements Runnable {
	private final Logger logger = LoggerFactory.getLogger(CreateUserImg.class);
	private long id;
	private String username;
	private CountDownLatch latch;

	public CreateUserImg(long id, String username, CountDownLatch latch) {
		this.id = id;
		this.username = username;
		this.latch = latch;
	}

	@Override
	public void run() {
		try {
			ImageProducerUtil.create(id, username);
		} catch (Exception e) {
			logger.warn("create userimg failed:" + e.getMessage());
		} finally {
			if (latch != null)
				latch.countDown();
		}
	}

}
