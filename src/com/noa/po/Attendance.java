package com.noa.po;

import java.util.Date;

public class Attendance {
	private Integer id; // Ψһ��ʶ

	private Integer employeeId;// Ա��id

	private Date date; // ��������

	private Date onTime; // ����ʱ��

	private Date offTime; // ����ʱ��

	private Integer state; // ״̬ : 0���� 1�Ӱ� 2�ٵ�����

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getOnTime() {
		return onTime;
	}

	public void setOnTime(Date onTime) {
		this.onTime = onTime;
	}

	public Date getOffTime() {
		return offTime;
	}

	public void setOffTime(Date offTime) {
		this.offTime = offTime;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

}