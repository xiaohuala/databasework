package com.noa.service.impl;

import java.util.List;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.noa.exception.CustomException;
import com.noa.mapper.EmployeeMapper;
import com.noa.po.Employee;
import com.noa.po.EmployeeCustom;
import com.noa.service.AttendanceService;
import com.noa.service.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private EmployeeMapper employeeMapper;

	@Autowired
	private AttendanceService attendanceService;

	// ����Ա����Ϣ�����û�
	public List<EmployeeCustom> findEmployee(Employee employeeInfo) throws Exception {

		List<EmployeeCustom> employeeList = employeeMapper.findEmployee(employeeInfo);
		if (employeeList == null) {
			throw new CustomException("û�з��ϸ�������Ա��!");
		}
		return employeeList;

	}

	public EmployeeCustom findEmployeeById(Integer employeeId) throws Exception {
		return employeeMapper.findEmployeeById(employeeId);
	}

	// ���Ĺ���״̬,��ʱǰ��Ӧ��ֻ�ṩ1(�ڹ�˾)��3(�뿪)��ѡ��
	public EmployeeCustom changeWorkingState(EmployeeCustom employeeCustom, int workingState) throws Exception {
		employeeCustom.setWorkingState(workingState);
		employeeMapper.changeWorkingState(employeeCustom);
		return employeeCustom;
	}

	// �û����Ƿ�ʹ��
	public void checkUsername(Employee employee) throws Exception {
		if (employee.getUsername() == null || employee.getUsername().trim() == "") {
			throw new CustomException("�������û���!");
		}
		if (employee.getPassword() == null || employee.getPassword().trim() == "") {
			throw new CustomException("����������!");
		}
		if (employeeMapper.existedUsername(employee) != 0) {
			throw new CustomException("���˺��ѱ�ע��!");
		}
	}

	// ע��
	@Transactional(propagation = Propagation.REQUIRED)
	public void register(Employee employee) throws Exception {
		// ��Ҫ�ĳ�ǰ����֤
		if (employee.getName() == null || employee.getName().trim() == "" || employee.getSex() == null
				|| employee.getDepartmentId() == null || employee.getPositionId() == null
				|| employee.getUsername() == null || employee.getUsername().trim() == ""
				|| employee.getPassword() == null || employee.getPassword().trim() == "") {
			throw new CustomException("��������Ϣ!");
		}

		// md5
		// ԭʼ����
		String source = employee.getPassword();
		// �� = username + pass
		String salt = "salt";
		// ɢ�д���, Ҫ����shiro.xml�����õ�
		int hashIterations = 1;

		Md5Hash md5Hash = new Md5Hash(source, salt, hashIterations);

		// ��Ϊע���Ҫ������֤,���ܸı�ԭ����ֵ
		Employee newEmployee = new Employee();
		BeanUtils.copyProperties(employee, newEmployee);
		newEmployee.setPassword(md5Hash.toString());
		newEmployee.setSalt(salt);

		employeeMapper.register(newEmployee);
		employeeMapper.registerPosition(newEmployee);
		// ͨ��LAST_INSERT_ID()��ø�ע���employee��id Ȼ��д��sys_employee_position��

	}

	// ��¼, ����Ҫ��״̬��Ϊ���� �Ƿ��ڹ�˾(0(Ĭ��):����,1:��,(2:�뿪))
	@Transactional(propagation = Propagation.REQUIRED)
	public EmployeeCustom login(Employee employee) throws Exception {
		if (employee.getUsername() == null || employee.getUsername().trim() == "") {
			throw new CustomException("�������û���!");
		}
		if (employee.getPassword() == null || employee.getPassword().trim() == "") {
			throw new CustomException("����������!");
		}

		// �˻�������֤
		EmployeeCustom employeeCustom = employeeMapper.login(employee);
		if (employeeCustom == null) {
			throw new CustomException("�˺Ż��������!");
		}
		if (employeeCustom.getLocked() == 1) {
			throw new CustomException("���˺Ų�����!");
		}

		employeeCustom.setWorkingState(1);
		changeWorkingState(employeeCustom, 1);

		// ���컹û��½��
		if (attendanceService.getOnTime(employeeCustom.getId()) == null) {

			attendanceService.on(employeeCustom);
		}

		return employeeCustom;
	}

	// ����,
	@Transactional(propagation = Propagation.REQUIRED)
	public void logout(EmployeeCustom employeeCustom) throws Exception {
		// TODO Auto-generated method stub

		attendanceService.off(employeeCustom);

	}

	// shiro��֤��
	public Employee findAccountByUsername(String username) throws Exception {

		return employeeMapper.findAccountByUsername(username);
	}
}
