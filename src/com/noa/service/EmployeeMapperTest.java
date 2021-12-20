package com.noa.service;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import com.noa.po.Employee;


	

	@RunWith(SpringJUnit4ClassRunner.class) 
	 //º”‘ÿspring»›∆˜
	@ContextConfiguration(locations="file:src/config/spring/applicationContext-dao.xml")
	@WebAppConfiguration
	@MapperScan("com.*.EmployeeService")
	//@MapperScan("com.noa.**.mapper")
	public class EmployeeMapperTest {

	    @Autowired
	    private EmployeeService studentservice;

	    @Test
	    public void test() throws Exception{
	    	Employee an = new Employee();
	        an.setId(70);
	        studentservice.findEmployeeById(an.getId());
	    }
	}
	