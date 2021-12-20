package com.noa.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.multipart.MultipartFile;
//import org.json.JSONObject;
import com.noa.po.EmployeeCustom;
import com.noa.po.Mission;
import com.noa.po.MissionCustom;
import com.noa.service.MissionService;
import com.noa.service.SysService;
import com.noa.test.Base64Util;
import com.noa.test.FileUtil;
import com.noa.test.HttpUtil;
import com.noa.controller.PlantController;
@Controller
@RequestMapping("/mission")

@SuppressWarnings("all")
public class MissionController {

	@Autowired
	private MissionService missionService;
	
	@Autowired
	private SysService sysService;
	
	EmployeeCustom activeEmp;

	@RequestMapping("")
	public String showMissionPage(Model model, HttpSession session,String view) throws Exception {
		List<MissionCustom> missionList = null;
		activeEmp= (EmployeeCustom) session.getAttribute("activeEmp");
		switch (view) {
		case "all":
			missionList = missionService.showAllMission(activeEmp);
			break;
		case "overall":
			missionList = missionService.filterMission(0);
			break;
		case "main":
			missionList = missionService.filterMission((activeEmp.getDepartmentId()/10)*10);
			break;
		case "sub":
			missionList = missionService.filterMission(activeEmp.getDepartmentId());
			break;
		default:
			missionList = missionService.showAllMission(activeEmp);
			break;
		}

		model.addAttribute("missionList", missionList);
		model.addAttribute("isInbox",true);
		return "mission/mission_inbox";
	}
	
	@RequestMapping(value="/mission_view" ,method=RequestMethod.GET)
	public String showMissionDetail(Model model,Integer mission_id,Integer updateProgress) throws Exception{
		if (updateProgress!=null) {
			missionService.updateProgress(mission_id, updateProgress);
		}
		model.addAttribute("mission", missionService.missionDetail(mission_id));

		return "mission/mission_view";
		
	}
	
	@RequestMapping(value="/mission_view" ,method=RequestMethod.POST)
	public String insertComment(Model model,HttpSession session,Integer mission_id,String oldComment,String newComment) throws Exception{
		
		newComment = newComment.replaceAll("\r\n", "<br>");
		
		
		missionService.comment(mission_id, oldComment, newComment, (EmployeeCustom) session.getAttribute("activeEmp"));
		model.addAttribute("mission", missionService.missionDetail(mission_id));
		
		return "mission/mission_view";
	}
	
	@RequestMapping("/mission_compose")
	public String showComposeForm(Model model)throws Exception{
		
		

		model.addAttribute("departmentList", sysService.getAbleToPostDeps());

		return "mission/mission_compose";
	}

	@RequestMapping("/post_mission.action")//
	@RequiresPermissions(value={"mission:create:all","mission:create:main","mission:create:sub"},logical=Logical.OR)
	public String postMission(HttpSession session,Mission mission,MultipartFile mission_pic) throws Exception{
		
		//图片上传
		String originalFilename = null;
		File file =null;
		String url = "https://aip.baidubce.com/rest/2.0/image-classify/v1/plant";
		if (mission_pic != null) {
			originalFilename = mission_pic.getOriginalFilename();
			mission.setPic(originalFilename);	
		} 
		if (mission_pic!=null && originalFilename!=null && originalFilename.trim()!="") {
			
			String filePath = sysService.uploadPic("mission", mission_pic);
            byte[] imgData = FileUtil.readFileByBytes(filePath);
            String imgStr = Base64Util.encode(imgData);
            String imgParam = URLEncoder.encode(imgStr, "UTF-8");

            String param = "image=" + imgParam;

            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String accessToken = "[24.c0e4d43f6013e4979c5233ce474bfab1.2592000.1641692619.282335-25308532]";

            String result = HttpUtil.post(url, accessToken, param);	
            mission.setText(result);
			//mission.setPic(sysService.uploadPic("mission", mission_pic));			
		}
		
            		
		activeEmp = (EmployeeCustom) session.getAttribute("activeEmp");
		mission.setAutherId(activeEmp.getId());
		//String url = "https://aip.baidubce.com/rest/2.0/image-classify/v1/plant";
		
		/*File file = new File(mission_pic.getOriginalFilename());
		FileUtils.copyInputStreamToFile(mission_pic.getInputStream(), file);
		String imgStr=null;
		try (FileInputStream fis = new FileInputStream(file)) {
		  byte[] buf = new byte[(int) file.length()];
		  fis.read(buf);
		  imgStr=new String(Base64Util.encode(buf));
		  return imgStr;
		} catch (IOException e) {
		} finally {
		  if (file.exists()) {
		    file.delete();
		  }
		}*/
		   //File file = new File(mission_pic.getOriginalFilename());
		   //FileUtils.copyInputStreamToFile(mission_pic.getInputStream(), file);
            	
		    //String pic_path = ContextLoader.getCurrentWebApplicationContext().getServletContext().getRealPath("/upload/"+mission)+"/";
		    //String filepath=pic_path+mission_pic.getOriginalFilename();
		/*	String filePath = "D:\\upload\\test.jpg";
            //byte[] imgData = FileUtil.readFileByBytes(filePath);
			byte[] imgData = mission_pic.getBytes();
            String imgStr = Base64Util.encode(imgData);
            String imgParam = URLEncoder.encode(imgStr, "UTF-8");
            String param = "image=" + imgParam;
            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String accessToken = "[24.c0e4d43f6013e4979c5233ce474bfab1.2592000.1641692619.282335-25308532]";

            String result = HttpUtil.post(url, accessToken, param);*/
           // JSONObject jsonObject = new JSONObject(result);
            //Person person = new Person(jsonObject.get("name").toString(), jsonObject.get("sex").toString(), Integer.valueOf(jsonObject.get("age").toString()));
            //System.out.println(person.toString());
            //System.out.println(result);
            //return result;
            
       // } catch (Exception e) {
         //   e.printStackTrace();
       // }
         // mission.setText(result);
          
		//mission.setText(mission.getText().replaceAll("\r\n", "<br>"));
		missionService.postMission(mission, activeEmp);
		
		return "redirect:/mission?view=all";
	}
	@RequestMapping("/delete_mission.action")
	public String deleteMission(Integer delete_id) throws Exception{
		missionService.deleteMission(delete_id);
		
		return "redirect:/mission?view=all";
	}
	
	@RequestMapping(value="/delete_comment.action",method=RequestMethod.POST)
	public String deleteMission(Integer mission_id,Integer comment_emp,String comment,String all_comment) throws Exception{
		
		missionService.deleteComment(mission_id, comment_emp, comment, all_comment);
		
		return "redirect:/mission/mission_view?mission_id="+mission_id;
	}
	
	
}
