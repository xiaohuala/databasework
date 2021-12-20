package com.noa.shiro;

import java.util.List;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.noa.po.Employee;
import com.noa.po.EmployeeCustom;
import com.noa.service.EmployeeService;
import com.noa.service.SysService;

public class CustomRealm extends AuthorizingRealm {

	@Autowired
	EmployeeService employeeService;

	@Autowired
	SysService sysService;

	@Override
	public void setName(String name) {
		super.setName("customRealm");
	}

	@Override // ��֤, ��֤ʧ�ܷ���null,
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

		// ȡ�������Ϣ
		String username = (String) token.getPrincipal();
		Employee authenticateInfo = null;
		try {
			authenticateInfo = employeeService.findAccountByUsername(username);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// �����ڸ�username��accountʱ,����null
		if (authenticateInfo == null) {
			return null;
		}

		EmployeeCustom activeEmp = null;
		try {
			activeEmp = employeeService.findEmployeeById(authenticateInfo.getId());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// �����û�ʱ����SimpleAuthenticationInfo

		SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(activeEmp,
				authenticateInfo.getPassword(), ByteSource.Util.bytes(authenticateInfo.getSalt()), this.getName());

		// ��������authenticatorͨ�� ɢ���㷨(xml������) ����(����������)�������password(token��)
		// ����ȷ������ƥ��(�ڶ�������)
		return simpleAuthenticationInfo;
	}

	@Override // ��Ȩ
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

		EmployeeCustom activeEmp = (EmployeeCustom) principals.getPrimaryPrincipal();
		List<String> permissionList = null;
		try {
			permissionList = sysService.getPermissionByEmpId(activeEmp.getId());
		} catch (Exception e) {
			e.printStackTrace();
		}

		SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
		simpleAuthorizationInfo.addStringPermissions(permissionList);
		
		//�������
		if(simpleAuthorizationInfo != null){
			try {
				employeeService.login(employeeService.findEmployeeById(activeEmp.getId()));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return simpleAuthorizationInfo;
	}

}
