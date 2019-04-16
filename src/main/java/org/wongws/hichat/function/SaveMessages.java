package org.wongws.hichat.function;

import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wongws.hichat.entity.HcMessage;
import org.wongws.hichat.repository.HcMessageRepository;

public class SaveMessages implements Runnable {
	private final Logger logger = LoggerFactory.getLogger(SaveMessages.class);
	private String sendId;
	private String reciverId;
	private String content;
	private HcMessageRepository hcMessageRepository;

	public SaveMessages(String sendId, String reciverId, String content, HcMessageRepository hcMessageRepository) {
		this.sendId = sendId;
		this.reciverId = reciverId;
		this.content = content;
		this.hcMessageRepository = hcMessageRepository;
	}

	@Override
	public void run() {
		try {
			HcMessage hcMessage = new HcMessage();
			hcMessage.setContent(content);
			hcMessage.setSend_id(sendId);
			hcMessage.setReciver_id(reciverId);
			hcMessageRepository.save(hcMessage);
		} catch (Exception e) {
			logger.warn("save HcMessage failed:" + e.getMessage());
		}

	}

}
