package com.noa.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.noa.exception.CustomException;
import com.noa.mapper.MailMapper;
import com.noa.po.EmployeeCustom;
import com.noa.po.Mail;
import com.noa.po.MailCustom;
import com.noa.service.MailService;

@Service
public class MailServiceImpl implements MailService {
	@Autowired
	private MailMapper mailMapper;

	// չʾ���и��ҵ��ʼ�
	public List<MailCustom> findMailToMe(EmployeeCustom employeeCustom) throws Exception {

		return mailMapper.findMailToMe(employeeCustom.getId());
	}

	// չʾδ�����ʼ�
	public List<MailCustom> findUnreadMailToMe(EmployeeCustom employeeCustom) throws Exception {

		return mailMapper.findUnreadMailToMe(employeeCustom.getId());
	}

	// չʾ�Ѷ����ʼ�
	public List<MailCustom> findReadMailToMe(EmployeeCustom employeeCustom) throws Exception {
		return mailMapper.findReadMailToMe(employeeCustom.getId());
	}

	// չʾ�ҷ��͵��ʼ�
	public List<MailCustom> findMailSendByMe(EmployeeCustom employeeCustom) throws Exception {
		return mailMapper.findMailSendByMe(employeeCustom.getId());
	}

	// �ʼ�����(text,pic)
	public MailCustom showMailDetail(Integer mailId, Integer othersideId) throws Exception {
		mailMapper.updateRead(mailId);
		return mailMapper.showMailDetail(mailId, othersideId);
	}

	public void sendMail(Mail mail) throws Exception {
		if (mail.getServerId() == null || mail.getRecieverId() == null) {
			throw new CustomException("�ռ��˻򷢼���Ϊ��");
		}
		mailMapper.SendMail(mail);
	}
}
