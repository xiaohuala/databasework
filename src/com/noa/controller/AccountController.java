package com.noa.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import com.noa.exception.CustomException;
import com.noa.po.Employee;
import com.noa.po.EmployeeCustom;
import com.noa.service.EmployeeService;
import com.noa.service.SysService;

@Controller
public class AccountController {

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private SysService sysService;

	EmployeeCustom activeEmp;

	@RequestMapping("/login")
	public String login(Model model, HttpServletRequest request) throws Exception {

		String exceptionClassName = (String) request.getAttribute("shiroLoginFailure");
		if (exceptionClassName != null) {
			if (UnknownAccountException.class.getName().equals(exceptionClassName)) {
				// ���ջ��׸��쳣������

				model.addAttribute("message", "�˺Ų�����");
			} else if (IncorrectCredentialsException.class.getName().equals(exceptionClassName)) {
				model.addAttribute("message", "�û���/�������");
				// throw new CustomException("�û���/�������");
			} else {
				model.addAttribute("message", "δ֪����");
				// throw new Exception();// �������쳣����������δ֪����
			}
		}

		// ����/loginʱ, �Զ�������֤,��֤�ɹ���ֱ����ת��ȥ
		// ���η���ʱexceptionClassName== nullֱ�ӵ�����
		// ��֤ʧ��ʱexceptionClassName!= null , ������Ϣ���ֻص�����
		return "misc/login";
	}

	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	public String showSignupPage(Model model) throws Exception {
		return "misc/signup";
	}

	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public String checkUsername(Model model, Employee employee) throws Exception {

		try {
			employeeService.checkUsername(employee);
			model.addAttribute("employee", employee);

			model.addAttribute("positionList", sysService.getAbleToRegPos());

			return "misc/signup_detail";

		} catch (CustomException e) {
			model.addAttribute("message", e.getMessage());
		}

		return "misc/signup";
	}

	@RequestMapping(value = "/signup2", method = RequestMethod.POST)
	public String confirmPosition(Model model, Employee employee, MultipartFile employee_pic) throws Exception {

		// �ϴ�ͼƬ
		String originalFilename = employee_pic.getOriginalFilename();
		if (employee_pic != null && originalFilename != null && originalFilename.trim() != "") {
			employee.setPic(sysService.uploadPic("employee", employee_pic));
		}

		model.addAttribute("employee", employee);
		model.addAttribute("departmentList", sysService.getAbleToRegDep(employee.getPositionId()));

		return "misc/signup_dep";
	}

	@RequestMapping(value = "/formal_signup", method = RequestMethod.POST)
	public String register(Model model, Employee employee) throws Exception {
		try {

			employeeService.register(employee);

			UsernamePasswordToken token = new UsernamePasswordToken(employee.getUsername(), employee.getPassword());
			SecurityUtils.getSubject().login(token);
			return "redirect:home";

		} catch (CustomException e) {
			model.addAttribute("message", e.getMessage());
		}
		// ע��ʧ��
		return "misc/signup_detail";

	}

	@RequestMapping("/logout")
	public String logout(HttpSession session) throws Exception {
		activeEmp = (EmployeeCustom) session.getAttribute("activeEmp");
		employeeService.logout(activeEmp);
		session.removeAttribute("activeEmp");
		session.invalidate();
		return "redirect:/login";
	}

	@RequestMapping("/refuse")
	public String refuse() throws Exception {
		return "misc/refuse";
	}

}
