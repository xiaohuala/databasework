package com.noa.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.noa.mapper.AnnouncementMapper;
import com.noa.po.Announcement;
import com.noa.po.AnnouncementCustom;
import com.noa.po.EmployeeCustom;
import com.noa.service.AnnouncementService;
import com.noa.service.SysService;

@Service
public class AnnouncementServiceImpl implements AnnouncementService {

	@Autowired
	private AnnouncementMapper announcementMapper;

	@Autowired
	private SysService sysService;

	// չʾ���и��ҵĹ���
	@Transactional(propagation = Propagation.REQUIRED)
	public List<AnnouncementCustom> showAllAnnouncement(EmployeeCustom employeeCustom) throws Exception {

		Integer departmentId = employeeCustom.getDepartmentId();
		List<Integer> depIds = null;

		if (departmentId % 10 == 0) { // ���������Ӳ���,��ȡ�ô����µ������Ӳ���
			depIds = sysService.getSubDepByMain(departmentId);
			depIds.add(0); // ����ȫ��
			return announcementMapper.showAnnouncementList(depIds);
		}

		// ���������鲿��
		depIds = new ArrayList<Integer>();
		depIds.add(0); // ȫ��
		depIds.add((departmentId / 10) * 10); // main
		depIds.add(departmentId); // sub
		return announcementMapper.showAnnouncementList(depIds);

	}

	// ɸѡ��ʾ�Ĺ���
	public List<AnnouncementCustom> filterAnnouncement(Integer department_id) throws Exception {
		return announcementMapper.filterAnnouncement(department_id);
	}

	// չʾ������ϸ
	public Announcement announcementDetail(Integer announcement_id) throws Exception {

		return announcementMapper.announcementDetail(announcement_id);
	}

	// ��������
	public void announce(Announcement announcement, EmployeeCustom employeeCustom) throws Exception {
		announcement.setAutherId(employeeCustom.getId());
		announcementMapper.announce(announcement);
	}

	// ɾ������
	public void deleteAnnouncement(Integer announcementId) throws Exception {

		announcementMapper.deleteAnnouncement(announcementId);
	}

}
