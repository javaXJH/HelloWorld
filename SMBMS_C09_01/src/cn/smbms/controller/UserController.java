package cn.smbms.controller;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.catalina.connector.Response;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import cn.smbms.pojo.Role;
import cn.smbms.pojo.User;
import cn.smbms.service.role.RoleService;
import cn.smbms.service.user.UserService;
import cn.smbms.service.user.UserServiceImpl;
import cn.smbms.tools.Constants;
import cn.smbms.tools.PageSupport;

@Controller
@RequestMapping("/user")
public class UserController{
	//日志输出的对象
	private Logger logger = Logger.getLogger(UserController.class);
	
	@Resource
	private RoleService roleService;
	
	@Resource
	private UserService userService;
	
	@RequestMapping("/login.html")
	public String login(){
		logger.info("UserController welcome SMBMS========");
		return "login";
	}
	@RequestMapping(value="dologin.html",method=RequestMethod.POST)
	public String dologin(@RequestParam String userCode,@RequestParam String userPassword,
			HttpSession session,HttpServletRequest request){
		logger.info("doLogin==========");
		//调用service方法，进行用户匹配
		User user=userService.login(userCode, userPassword);
		if(null!=user){
			//登录成功,页面跳转(frame.jsp)
			//放入Session
			session.setAttribute(Constants.USER_SESSION, user);
			//有这个前缀后，才是在浏览器上重定向跳转，而不是服务器上跳转
			return "redirect:main.html";
		}else{
			//放入request的失败信息
			request.setAttribute("error", "用户名或密码不正确");
			//登录失败，页面跳转(login.jsp)
			return"login";
		}
	}
	
	@RequestMapping(value="main.html")
	public String main(HttpSession session){
		if(session.getAttribute(Constants.USER_SESSION)==null){
			return "redirect:/user/login.html";
		}
		return "frame";
	}
	
	@RequestMapping(value="exlogin.html",method=RequestMethod.GET)
	public String exLogin(@RequestParam String userCode,@RequestParam String userPassword){
		logger.info("exLogin============");
		User user=userService.login(userCode, userPassword);
		if(user==null){
			throw new RuntimeException("用户名或者密码不正确");
		}
		return "redirect:/user/main.html";
	}
	
	@RequestMapping(value="/userlist.html")
	public String getUserList(Model model,
			@RequestParam(value="queryname",required=false)String queryUserName,
			@RequestParam(value="queryUserRole",required=false)String queryUserRole,
			@RequestParam(value="pageIndex",required=false)String pageIndex){
		System.out.println("找乱码======================"+queryUserName);
		//查询用户列表
		int _queryUserRole=0;
		List<User>userList=null;
		//设置页面内容
		int pageSize=Constants.pageSize;
		//当前页码
		int currentPageNo=1;
		if(queryUserName==null){
			queryUserName="";
		}
		if(queryUserRole!=null&&!queryUserRole.equals(" ")){
			_queryUserRole=Integer.parseInt(queryUserRole);
		}
		if(pageIndex!=null){
			try{
				currentPageNo=Integer.valueOf(pageIndex);
			}catch(NumberFormatException e){
				return "redirect:/user/syserror.html";
			}
		}
		//总数量（表）
		//总数量（表）	
    	int totalCount	= userService.getUserCount(queryUserName,_queryUserRole);
    	//总页数
    	PageSupport pages=new PageSupport();
    	pages.setCurrentPageNo(currentPageNo);
    	pages.setPageSize(pageSize);
    	pages.setTotalCount(totalCount);
    	int totalPageCount = pages.getTotalPageCount();
		userList=userService.getUserList(queryUserName, _queryUserRole, currentPageNo, pageSize);
		model.addAttribute("userList",userList);
	
		List<Role>roleList=null;
		roleList=roleService.getRoleList();
		model.addAttribute("roleList", roleList);
		model.addAttribute("queryUserName", queryUserName);
		model.addAttribute("queryUserRole", queryUserRole);
		model.addAttribute("totalPageCount", totalPageCount);
		model.addAttribute("totalCount", totalCount);
		model.addAttribute("currentPageNo", currentPageNo);
		return "userlist";
	}
	
	
}
