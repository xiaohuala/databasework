package com.noa.exception;

//�Զ�����쳣��
@SuppressWarnings("serial")
public class CustomException extends Exception {
	// �쳣��Ϣ
	public String messages;

	public CustomException(String messages) {
		super(messages);
		this.messages = messages;
	}

	public String getMessages() {
		return messages;
	}

	public void setMessages(String messages) {
		this.messages = messages;
	}
}
