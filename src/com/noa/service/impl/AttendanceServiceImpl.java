package com.noa.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.noa.mapper.AttendanceMapper;
import com.noa.mapper.EmployeeMapper;
import com.noa.po.EmployeeCustom;
import com.noa.service.AttendanceService;

@Service
public class AttendanceServiceImpl implements AttendanceService{
	
	@Autowired
	private AttendanceMapper attendanceMapper;
	
	@Autowired
	private EmployeeMapper employeeMapper;
	
	//����
	public void on(EmployeeCustom employeeCustom) throws Exception {
		attendanceMapper.on(employeeCustom.getId());
		
	}
	

	//����
	public void off(EmployeeCustom employeeCustom) throws Exception {

		Date onTime = attendanceMapper.getOnTime(employeeCustom.getId());
		Date offTime = new Date();
		//����8Сʱǰ
		long period = offTime.getTime() - onTime.getTime() -8*60*60*1000;
		//ʱ�䲻��7����Сʱ��ٵ�����(2) , ����9Сʱ��Ӱ�(1),����0
		//-8*60*60*1000
		int state =(period>60*60*1000)?1:(period<-30*60*1000)?2:0;
		
		attendanceMapper.off(employeeCustom.getId(), state);
		employeeCustom.setWorkingState(0);
		employeeMapper.changeWorkingState(employeeCustom);
		
	}
	//���µĳ���״��
	public int[] countMonthState(EmployeeCustom employeeCustom) throws Exception {
		
		List<Integer> resultList = attendanceMapper.countMonthState(employeeCustom.getId());
		
		int maxday = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);
		int today = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		
		
		//[0]���� [1]�Ӱ� [2]�ٵ����� ([3]����)[3]ȱϯ[4]ʣ������
		int[] states = new int[5];
		int attend = 0;
		for (Integer integer : resultList) {
			if (integer!=null) {
				
			states[integer]++;
			attend++;
			}
		}
		states[3] = today - attend;
		states[4] = maxday - today;
		
		
		return states;
	}
	
	
	public Date getOnTime(Integer empId) throws Exception {
		return attendanceMapper.getOnTime(empId);
	}
	
}
