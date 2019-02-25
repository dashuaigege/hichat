package org.wongws.hichat.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

//@JsonInclude(JsonInclude.Include.ALWAYS)//不包含有null值的字段,即字段值为null的转换为json字符串时会被省略
public class WiselyResponse {
	private String responseMessage;

	public WiselyResponse(String responseMessage) {
		this.responseMessage = responseMessage;
	}
	public String getResponseMessage()
	{
		return responseMessage;
	}
}
