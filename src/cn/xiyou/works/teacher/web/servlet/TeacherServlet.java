package cn.xiyou.works.teacher.web.servlet;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import cn.xiyou.works.teacher.service.TeacherService;
import cn.xiyou.works.work.Work;

public class TeacherServlet extends HttpServlet {
	TeacherService ts = new TeacherService();
	
	
	public  void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String method = req.getParameter("method");
		System.out.println("所调用方法为"+method);
		if(method.equals("findById")){
			findById(req, resp);
		}else if(method.equals("select")){
			select(req, resp);
		}else if(method.equals("login")){
			login(req, resp);
		}else if(method.equals("approved")){
			approved(req, resp);
		}else if(method.equals("delete")){
			delete(req, resp);
		}else if(method.equals("findByPraise")){
			findByPraise(req, resp);
		}
	}
	/**
	 * 根据点赞量查询
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	public void findByPraise(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
	/**
		 *1、调用service进行查找
		 *2、数据进行封装并返回
		 */
		String page = req.getParameter("limit");
		int ps = Integer.parseInt(page);
		List<Work> list = ts.findByPraise(ps);
		JSONArray array = JSONArray.fromObject(list);
		resp.setCharacterEncoding("utf-8");
		resp.getWriter().print(array);
	}            
	
	/**
	 * 通过wid查看作品详情
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public void findById(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		//获取当前所要查询作品的id
		String wid = req.getParameter("wid");
		Work work = ts.findById(wid);//调用service层进行业务处理并返回
		resp.setCharacterEncoding("utf-8");
		resp.getWriter().print(JSONArray.fromObject(work));
		
	}
	/**
	 * 查询作品
	 * 老师根据条件进行查询
	 * 未审核/已审核/全部
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
//	@Test
	public void select(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/**
		 * 获取前端json传来的数据
		 * 获取所查询条件，	""里为所查询标签的名字
		 * 未审核、已审核、全部 标签的name都取值为select
		 */
		String condition = req.getParameter("select");
		String p = req.getParameter("page");
		String page = req.getParameter("limit");
		int ps = Integer.parseInt(page);
		int pc = Integer.parseInt(p);
		/**
		 * 调用service完成业务
		 */
		List<Work> list = ts.select(condition,pc,ps);
		/**
		 * 向前端返回json数据
		 */
		
		
		JSONArray array = JSONArray.fromObject(list);
		resp.setContentType("text/text;charset=utf-8");
		resp.setCharacterEncoding("utf-8");
		resp.getWriter().print(array);
		
        
	}
	/**
	 * 登录功能
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public void login(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		//获取表单数据
		String loginname = req.getParameter("loginname");
		String password = req.getParameter("password");
		//调用service完成业务
		int teacher = ts.login(loginname, password);
		//进行判断，并返回
		Map<String, String> result = new HashMap<String,String>();
//		String result = "{'result':";
		if(teacher==1){//登录成功，保存用户信息，并跳转到成功页
			/**
			 * 将用户名进行保存
			 */
			req.getSession().setAttribute("teacher", loginname);
//			result+="'success'}";
			result.put("result", "success");
		}else{//登录失败设置提示信息，并跳转到登录页提示并重新登录
			req.setAttribute("msg", "用户名或密码错误！");
//			result+="'fail'}";
			result.put("result", "fail");

		}
		resp.setContentType("text/text;charset=utf-8");
		resp.setCharacterEncoding("utf-8");
		resp.getWriter().print(JSONArray.fromObject(result));
	}
	/**
	 * 通过审核功能
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
//	@Test
	public void approved(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/**
		 * 1.获取所要通过的作品对应的id
		 * 2.调用service完成业务
		 * 3.刷新页面（跳转至当前页面）
		 */
		//获取id。
		String wid = req.getParameter("wid");
		//调用service
		ts.approved(wid);
		//返回页面
		Map<String,String> map = new HashMap<String,String>();
		map.put("result", "success");
		resp.setContentType("text/text;charset=utf-8");
		resp.setCharacterEncoding("utf-8");
		resp.getWriter().print(JSONArray.fromObject(map));

//		req.getRequestDispatcher("f:/teacher/success.jsp").forward(req, resp);//页面跳转
	}
	/**
	 * 删除作品功能
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
//	@Test
	public void delete(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		//public void s(){
		/**
		 * 获取所删除作品的id
		 * 调用service进行处理
		 * 刷新页面
		 */
		String wid = req.getParameter("wid");//request.getParameter获取表单窜过来的数据，name
		//String wid = "3";
		ts.delete(wid);
		Map<String,String> map = new HashMap<String,String>();
		map.put("result", "success");
		resp.setContentType("text/text;charset=utf-8");
		resp.setCharacterEncoding("utf-8");
		resp.getWriter().print(JSONArray.fromObject(map));

//		req.getRequestDispatcher("f:/teacher/success.jsp").forward(req, resp);//页面跳转
	}

}
