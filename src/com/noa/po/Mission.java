package com.noa.po;

import java.util.Date;

public class Mission {
	private Integer id;// Ψһ��ʶ

	private Integer autherId;// ����id

	private String pic;// �ϴ�����Ƭ

	private String title;

	private String text;

	private Integer targetId; // �������

	private Integer progress; // ����

	private String comment; // Ա��id:#�����ı�:&Ա��id:#�����ı�:&

	private Date time; // ����ʱ��
	
	private String wuti;

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

	public Integer getProgress() {
		return progress;
	}

	public void setProgress(Integer progress) {
		this.progress = progress;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	public String getWuti() {
		return wuti;
	}

	public void setWuti(String wuti) {
		this.wuti = wuti;
	}

}