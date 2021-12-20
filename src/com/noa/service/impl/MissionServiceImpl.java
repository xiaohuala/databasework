package com.noa.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.noa.exception.CustomException;
import com.noa.mapper.EmployeeMapper;
import com.noa.mapper.MissionMapper;
import com.noa.po.EmployeeCustom;
import com.noa.po.Mission;
import com.noa.po.MissionCustom;
import com.noa.po.MissionVo;
import com.noa.service.MissionService;
import com.noa.service.SysService;

@Service
public class MissionServiceImpl implements MissionService {

	@Autowired
	private MissionMapper missionMapper;

	@Autowired
	private EmployeeMapper employeeMapper;

	@Autowired
	private SysService sysService;

	// չʾ���и��ҵ�����
	@Transactional(propagation = Propagation.REQUIRED)
	public List<MissionCustom> showAllMission(EmployeeCustom employeeCustom) throws Exception {

		Integer departmentId = employeeCustom.getDepartmentId();
		List<Integer> depIds = null;

		if (departmentId % 10 == 0) { // ���������Ӳ���,��ȡ�ô����µ������Ӳ���
			depIds = sysService.getSubDepByMain(departmentId);
			depIds.add(0); // ����ȫ��
			return missionMapper.showMissionList(depIds);
		}

		// ���������鲿��
		depIds = new ArrayList<Integer>();
		depIds.add(0); // ȫ��
		depIds.add((departmentId / 10) * 10); // main
		depIds.add(departmentId); // sub
		return missionMapper.showMissionList(depIds);

	}

	// ɸѡ��ʾ������
	public List<MissionCustom> filterMission(Integer department_id) throws Exception {
		return missionMapper.filterMission(department_id);
	}

	// չʾ������ϸ
	public MissionVo missionDetail(Integer mission_id) throws Exception {
		MissionCustom missionDetail = missionMapper.missionDetail(mission_id);
		MissionVo missionVo = new MissionVo();

		// String str = "2:#DASDASD:&3:#����:&";

		// ��ÿ�����۰�װ��Map<EmployeeCustom, String>
		String str = missionDetail.getComment();
		if (str != null && str.trim() != "") {

			String[] strs = str.split(":&");
			// Map��������ͬ��key. Integer�͵�id��ͬʱ�ᱻ����( ��ͬһ��Ա�������Ķ���ʱ,�ᱻ����),
			// ->��List<Map>
			List<Map<Integer, String>> idCommentList = new ArrayList<Map<Integer, String>>();

			for (int i = 0; i < strs.length; i++) {
				String[] tmp = strs[i].split(":#");
				Map<Integer, String> idCommentMap = new HashMap<Integer, String>();
				idCommentMap.put(Integer.parseInt(tmp[0]), tmp[1]);
				idCommentList.add(idCommentMap);

			}
			Map<EmployeeCustom, String> employeeCommentMap = new LinkedHashMap<EmployeeCustom, String>();
			for (Map<Integer, String> idComment : idCommentList) {
				for (Integer key : idComment.keySet()) {
					// һ��mapֻ��һ��key, ��forֻ���ظ�һ��
					employeeCommentMap.put(employeeMapper.findEmployeeById(key), idComment.get(key));
				}
			}
			missionVo.setEmployeeCommentMap(employeeCommentMap);
		}

		missionVo.setMissionDetail(missionDetail);

		return missionVo;
	}

	// ��������
	public void postMission(Mission mission, EmployeeCustom employeeCustom) throws Exception {
		mission.setAutherId(employeeCustom.getId());
		missionMapper.postMission(mission);
	}

	// ���½���
	public void updateProgress(Integer missionId, Integer progress) throws Exception {

		missionMapper.updateProgress(progress, missionId);

	}

	// �������
	public void comment(Integer missionId, String oldComment, String newComment, EmployeeCustom employeeCustom)
			throws Exception {

		// String str = "2:#DASDASD:&3:#����:&";

		String commentStr = oldComment + employeeCustom.getId() + ":#" + newComment + ":&";
		Mission mission = new Mission();
		mission.setId(missionId);
		mission.setComment(commentStr);
		missionMapper.comment(mission);

	}

	// ɾ������
	public void deleteComment(Integer missionId, Integer commentEmp, String comment, String allComment)
			throws Exception {
		String commentStr = commentEmp + ":#" + comment + ":&";
		allComment = allComment.replaceFirst(commentStr, "");
		Mission mission = new Mission();
		mission.setId(missionId);
		mission.setComment(allComment);
		missionMapper.comment(mission);
	}

	// ɾ������
	public void deleteMission(Integer missionId) throws Exception {

		missionMapper.deleteMission(missionId);

	}

	// �༭����
	public void editMission(Mission mission, EmployeeCustom employeeCustom) throws Exception {
		if (mission.getAutherId() != employeeCustom.getId()) {
			throw new CustomException("ֻ�������ܱ༭!");
		}
		missionMapper.editMission(mission);
	}

}
