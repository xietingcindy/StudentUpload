package cn.xiyou.works.student.web.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import cn.xiyou.works.student.service.StudentService;
import cn.xiyou.works.work.Work;


public class StudentServlet extends HttpServlet {
	StudentService ss = new StudentService();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
	doPost(req, resp);
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		resp.setCharacterEncoding("utf-8");
		String method = req.getParameter("method");
		System.out.println("所调用方法为"+method);
		if(method.equals("findByPraise")){
			findByPraise(req, resp);
		}else if(method.equals("findAll")){
			findAll(req, resp);
		}
		else if(method.equals("findByName")){
			findByName(req, resp);
		}else if(method.equals("praise")){
			praise(req, resp);
		}else if(method.equals("stamp")){
			stamp(req, resp);
		}else if(method.equals("findByCompose")){
			findByCompose(req, resp);
		}else if(method.equals("findById")){
			findById(req, resp);
		}else if(method.equals("findByType")){
			findByType(req, resp);
		}
		else if(method.equals("findSum")){
			findSum(req, resp);
		}
		
	}
	
	/**
	 * 查找数据库中作品数量
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	public void findSum(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Long count = ss.findSum();
		
		Map<String,Long> map = new HashMap<String, Long>();
		map.put("count", count);
		System.out.println(JSONArray.fromObject(map));
		//返回结果
		resp.setContentType("text/text;charset=utf-8");
		resp.setCharacterEncoding("utf-8");
		resp.getWriter().print(JSONArray.fromObject(map));
		  
	}

	/**
	 * 获取当前页码
	 * @param req
	 * @return
	 */
	private int getPc(HttpServletRequest req){
		int pc=1;
		String param = req.getParameter("page");
		if(param!=null && !param.trim().isEmpty()){
			try{
				pc = Integer.parseInt(param);
		
			}catch(RuntimeException e){}
		}
		return pc;
	}
	
	/**
	 * 截取url，页面中分页导航中需要使用它作为超链接的目标
	 * @param req
	 * @return
	 */
	private String getUrl(HttpServletRequest req){
		String url=req.getRequestURI()+"?"+req.getQueryString();
		/**
		 * 如果url中存在pc参数，截取掉，否则不用管
		 */
		int index = url.lastIndexOf("&pc=");
		if(index!=-1){
			url = url.substring(0,index);
		}
		return url;
	}
	/**
	 * 多条件组合查询
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	public void findByCompose(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/**
		 * 1、获取表单内容（作品名称，学生姓名，作品类型）
		 * 2、调用service完成业务
		 * 3、返回数据
		 */
		//分页
//		String url = getUrl(req);//获取url
		int pc = getPc(req);//获取当前页数
		
		String work = req.getParameter("wname");
		String student = req.getParameter("sname");
		String type = req.getParameter("tname");
		String page = req.getParameter("limit");
		int ps = Integer.parseInt(page);
		List<Work> list = ss.findByCompose(work,student,type,pc,ps);
		
		//Long sum = ss.findSum();
		
//		list.setUrl(url);
		//返回结果
		resp.setContentType("text/text;charset=utf-8");
		resp.setCharacterEncoding("utf-8");
		resp.getWriter().print(JSONArray.fromObject(list));
		
	}
	/**
	 * 通过点赞量查找并返回所有作品
	 * @param req
	 * @param resp
	 * @return
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
//		String url = getUrl(req);
		List<Work> list = ss.findByPraise(ps);
//		list.setUrl(url);
//		JSONObject jo = JSONObject.fromObject(list);
//		System.out.println(jo);
		JSONArray array = JSONArray.fromObject(list);
		resp.setContentType("text/text;charset=utf-8");
		resp.setCharacterEncoding("utf-8");
		resp.getWriter().print(array);
	}                                                     
	
	/**
	 * 通过ID查找作品
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	public void findById(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.out.println("进入findById");
		//获取作品id
		String wid = req.getParameter("wid");
		//根据ID进行查找
		Work work = ss.finById(wid);
		System.out.println("退出findById");
		System.out.println(JSONArray.fromObject(work));
//		返回json类数据
		resp.setContentType("text/text;charset=utf-8");
		resp.setCharacterEncoding("utf-8");
		resp.getWriter().print(JSONArray.fromObject(work));
	}
	/**
	 * 按照作品类型进行查找
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public void findByType(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		resp.setContentType("text/html;charset=UTF-8");
		//获取当前页数
		int pc = getPc(req);
		//获取当前Url
//		String url = getUrl(req);
		//获取所要查找的作品类型
		String type = req.getParameter("type");
		String page = req.getParameter("limit");
		int ps = Integer.parseInt(page);
		//调用service完成业务
		List<Work> list = ss.findByType(type,pc,ps);
//		list.setUrl(url);
		System.out.println(JSONArray.fromObject(list));
		resp.setContentType("text/text;charset=utf-8");
		resp.setCharacterEncoding("utf-8");
		resp.getWriter().print(JSONArray.fromObject(list));
	}	
	/**
	 * 点赞
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	public void praise(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		System.out.println("进入点赞方法");
		//获取作品Id
		String wid = req.getParameter("wid");
		System.out.println(wid);
		//进行业务处理
		 ss.praise(wid);
		//返回成功提示
		StringBuilder sb = new StringBuilder("{");
		sb.append("\"success\"").append(":").append("success");
		sb.append("}");
		System.out.println("退出点赞方法");
		resp.setContentType("text/text;charset=utf-8");
		resp.setCharacterEncoding("utf-8");
		resp.getWriter().print(JSONArray.fromObject(sb));
	}
	/**
	 * 踩
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	public void stamp(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		//获取作品Id
		String wid = req.getParameter("wid");
		//进行业务处理
		ss.stamp(wid);
		//返回成功提示
		StringBuilder sb = new StringBuilder("{");
		sb.append("\"success\"").append(":").append("success");
		sb.append("}");
		resp.setContentType("text/text;charset=utf-8");
		resp.setCharacterEncoding("utf-8");
		resp.getWriter().print(JSONArray.fromObject(sb));
	}
	/**
	 * 通过作品名称进行查询
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public void findByName(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		int pc = getPc(req);//获取当前页数
		//获取查询条件
		String name = req.getParameter("wname");
		String page = req.getParameter("limit");
		int ps = Integer.parseInt(page);
		System.out.println("接收到的作品名称为："+name);
		//进行查询处理
		List<Work> list = ss.findByName(name,pc,ps);
		//返回结果
		resp.setContentType("text/text;charset=utf-8");
		resp.setCharacterEncoding("utf-8");
		resp.getWriter().print(JSONArray.fromObject(list));
	}
	/**
	 * 查找所有已审核
	 * @param req作品
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	public void findAll(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		int pc = getPc(req);
		String page = req.getParameter("limit");
		int ps = Integer.parseInt(page);
		
//		findSum(req, resp);
		
		List<Work> list = ss.findAll(pc,ps);
		JSONArray array = JSONArray.fromObject(list);
		resp.setContentType("text/text;charset=utf-8");
		resp.setCharacterEncoding("utf-8");
		resp.getWriter().print(array);
	}
}
