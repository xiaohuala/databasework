package com.noa.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.multipart.MultipartFile;

import com.noa.mapper.SysMapper;
import com.noa.po.EmployeeCustom;
import com.noa.po.SysDepartment;
import com.noa.po.SysPosition;
import com.noa.service.SysService;

@Service
public class SysServiceImpl implements SysService {

	@Autowired
	private SysMapper sysMapper;

	public List<SysDepartment> getAllDepartment() throws Exception {

		return sysMapper.getAllDepartment();
	}

	public List<SysPosition> getAllPosition() throws Exception {

		return sysMapper.getAllPosition();
	}

	public List<String> getPermissionByEmpId(Integer empId) throws Exception {

		return sysMapper.getPermissionByEmpId(empId);
	}

	public List<Integer> getSubDepByMain(Integer mainDep) throws Exception {
		List<SysDepartment> allDep = getAllDepartment();
		List<Integer> subDepList = new ArrayList<Integer>();
		for (SysDepartment dep : allDep) {
			int depId = dep.getDepartmentId();
			if ((depId / 10) * 10 == mainDep) {
				// ��mainDep�µĲ��� (����main)
				subDepList.add(depId);
			}
		}
		// subDepList = {main,sub1,sub2,...}
		return subDepList;

	}

	public List<SysDepartment> getDepsByDepIds(List<Integer> depIds) throws Exception {
		return sysMapper.getDepByDepIds(depIds);
	}

	// ��ȡ�ɲ�������Ķ���
	public List<SysDepartment> getAbleToPostDeps() throws Exception {
		// ��ȡȨ��
		Subject subject = SecurityUtils.getSubject();
		EmployeeCustom activeEmp = (EmployeeCustom) subject.getPrincipal();
		int activeEmpDep = activeEmp.getDepartmentId();
		List<Integer> depIds = new ArrayList<Integer>();

		// ����Ȩ�����,�ɲ�������Ķ���
		if (subject.isPermitted("mission:create:all")) { // �з���ȫ��ĵ�Ȩ��
			depIds.add(0);
		}
		if (subject.isPermitted("mission:create:main")) {
			depIds.addAll(getSubDepByMain((activeEmpDep / 10) * 10));
		} else if (subject.isPermitted("mission:create:sub")) {// ֻ�з����Լ����Ӳ��ŵ�Ȩ��
			if (activeEmpDep % 10 != 0) {
				depIds.add(activeEmpDep);
			}
		}
		if (depIds.isEmpty()) {
			return null;
		}
		return getDepsByDepIds(depIds);

	}

	// ��ȡ�ɷ�������Ķ���
	public List<SysDepartment> getAbleToAnnounceDeps() throws Exception {
		// ��ȡȨ��
		Subject subject = SecurityUtils.getSubject();
		EmployeeCustom activeEmp = (EmployeeCustom) subject.getPrincipal();
		int activeEmpDep = activeEmp.getDepartmentId();
		List<Integer> depIds = new ArrayList<Integer>();

		// ����Ȩ�����,�ɷ�������Ķ���
		if (subject.isPermitted("announce:create:all")) { // �з���ȫ��ĵ�Ȩ��
			depIds.add(0);
		}
		if (subject.isPermitted("announce:create:main")) {
			depIds.addAll(getSubDepByMain((activeEmpDep / 10) * 10));
		} else if (subject.isPermitted("announce:create:sub")) {// ֻ�з����Լ����Ӳ��ŵ�Ȩ��
			if (activeEmpDep % 10 != 0) {
				depIds.add(activeEmpDep);
			}
		}
		if (depIds.isEmpty()) {
			return null;
		}
		return getDepsByDepIds(depIds);
	}

	// ��ȡ��ע��ĸ�λ
	public List<SysPosition> getAbleToRegPos() throws Exception {
		List<SysPosition> allPositions = getAllPosition();
		List<SysPosition> resultList = new ArrayList<SysPosition>();
		// �ص�����ע���
		for (SysPosition posistion : allPositions) {
			if (posistion.getAvailable() != 0) {
				resultList.add(posistion);
			}
		}
		return resultList;
	}

	// ��ȡ��ע��Ĳ���
	public List<SysDepartment> getAbleToRegDep(Integer posId) throws Exception {
		List<SysDepartment> allDepartments = getAllDepartment();
		List<SysDepartment> resultList = new ArrayList<SysDepartment>();

		// �ص�����ע���
		for (SysDepartment department : allDepartments) {

			if (department.getAvailable() == 1) { // ��ע��Ĳ���
				if (department.getDepartmentId() % 10 != 0 && posId <= 1) { // �޻��鳤
																			// ֻ��ע���Ӳ���
					resultList.add(department);
				} else if (department.getDepartmentId() % 10 == 0 && posId > 1) { // �ܼ����,ֻ��ע�����
					resultList.add(department);
				}
			}
		}

		return resultList;
	}

	// �ϴ�ͼƬ
	public String uploadPic(String dir, MultipartFile pic) throws Exception {//

		// �ϴ�ͼƬ
		// ԭʼ����
		String originalFilename = pic.getOriginalFilename();

		// �洢ͼƬ������·��
		//String pic_path = "D:\\upload\\" ;
		//String pic_path = System.getProperty("user.dir") + "\\upload\\" + dir + "\\";
		String pic_path = ContextLoader.getCurrentWebApplicationContext().getServletContext().getRealPath("/upload/"+dir)+"/";
		// �µ�ͼƬ���� : ����� + ��չ��
		//String newfileNStringame = UUID.randomUUID() + originalFilename.substring(originalFilename.lastIndexOf("."));
		String newfileNStringame = originalFilename;

		// ��ͼƬ
		File newfile = new File(pic_path + newfileNStringame);
		String filePath=pic_path + newfileNStringame;
		// ���ڴ��е�����д�����
		pic.transferTo(newfile);

		return filePath;

	}

}
