package com.noa.po;

public class Employee {
	private Integer id; // Ա��id

	private String name; // Ա������

	private Integer sex; // Ա���Ա� ,��1Ů0

	private String pic; // ͷ��

	private Integer departmentId; // ����id

	private Integer positionId; // ְλid,��sys_employee_position����.

	private String username;
	private String password;
	private String salt;

	private Integer workingState; // ״̬ : 0(Ĭ��):����,1:����

	private Integer locked; // �Ƿ����, ������ʱ1 ����ʱ0

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name == null ? null : name.trim();
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public Integer getPositionId() {
		return positionId;
	}

	public void setPositionId(Integer positionId) {
		this.positionId = positionId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public Integer getWorkingState() {
		return workingState;
	}

	public void setWorkingState(Integer workingState) {
		this.workingState = workingState;
	}

	public Integer getLocked() {
		return locked;
	}

	public void setLocked(Integer locked) {
		this.locked = locked;
	}
}