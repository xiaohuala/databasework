package com.noa.po;

import java.util.Date;

public class Announcement {
	private Integer id;// Ψһ��ʶ

	private Integer autherId;// ����id

	private String pic;// �ϴ�����Ƭ

	private Integer targetId;// ���������

	private String title;

	private String text;

	private Date time; // ����ʱ��

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAutherId() {
		return autherId;
	}

	public void setAutherId(Integer autherId) {
		this.autherId = autherId;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic == null ? null : pic.trim();
	}

	public Integer getTargetId() {
		return targetId;
	}

	public void setTargetId(Integer targetId) {
		this.targetId = targetId;
	}

	public String getText() {
		return text;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setText(String text) {
		this.text = text == null ? null : text.trim();
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

}