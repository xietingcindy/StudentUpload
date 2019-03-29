package cn.xiyou.works.student.web.servlet;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.junit.Test;

import cn.xiyou.works.student.service.DownloadService;


public class DownloadServlet extends HttpServlet {
	
	private DownloadService ds = new DownloadService();
@Override
public void doGet(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
	System.out.println("进入get");
	doPost(req, resp);
	
}
/*	*//**
	 * 实现下载功能
	 *//*
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("进入post");
		*//**
		 * 1、获取作品id
		 * 2、根据id查找作品所在磁盘位置
		 * 3、进行下载
		 *//*
		response.setContentType("text/html");
		//获得作品id
		String wid = request.getParameter("wid");
		//查找对应文件位置
		String address = ds.findAddress(wid);
		System.out.println(address);
		//下载
			//新建文件，目录为要下载的文件的目录
//			File file = new File(getWEBINFPath(address));
		address.replace('/','\\');
		address = request.getRealPath("/")+address;
		File file = new File(address);
			//文件输入流
			FileInputStream is = new FileInputStream(file);
			//获取响应输出流
			ServletOutputStream os = response.getOutputStream();
			BufferedOutputStream bos = new BufferedOutputStream(os);
			//设置下载文件名，命名格式
			String date = new SimpleDateFormat("yyyy-HH-dd").format(new Date());
			String name = file.getName();
			String fileName = date+"."+name.substring(name.lastIndexOf(".")+1);
			//设置响应头
			response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
			
			byte[] len = new byte[1024*2];
			int read = 0;
			while((read=is.read(len)) != -1){
				bos.write(len, 0, read);
				System.out.println("read---"+read);
			}
			bos.flush();
			bos.close();
			is.close();
		}*/
	@Test
	public String getWEBINFPath(String p){
		String path = Thread.currentThread().getContextClassLoader().getResource("").toString();
		path = path.replace('/','\\');
		path = path.replace("file:", "");
		path = path.replace("classes\\", "");
		path = path.substring(1);
		p = p.replace('/', '\\');
		path+=p;
		System.out.println(path);
		return path;
	}
	/**
	 * 实现下载功能
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("进入post");
		/**
		 * 1、获取作品id
		 * 2、根据id查找作品所在磁盘位置
		 * 3、进行下载
		 */
		response.setContentType("text/html");
		//获得作品id
		String wid = request.getParameter("wid");
		System.out.println(wid);
		//查找对应文件位置
		String address = ds.findAddress(wid);
		
		
		String name = ds.findName(wid);
		
		
		
		//下载
			//新建文件，目录为要下载的文件的目录
		address.replace('/','\\');
		address = request.getRealPath("/")+address;
		
		List<String> list = new ArrayList<String>();
		list.add(name);
		list.add(address);
		
		request.setCharacterEncoding("utf-8");
		response.getWriter().print(JSONArray.fromObject(list));
		
		
		
		
		
		
		
		
		/*
		File file = new File(address);
			//文件输入流
			FileInputStream is = new FileInputStream(file);
			String filewname = URLEncoder.encode(file.getName(),"utf-8");
			//获取响应输出流
			ServletOutputStream os = response.getOutputStream();
			BufferedOutputStream bos = new BufferedOutputStream(os);
			//设置下载文件名，命名格式
			String date = new SimpleDateFormat("yyyy-HH-dd").format(new Date());
			String name = file.getName();
			String fileName = date+"."+name.substring(name.lastIndexOf(".")+1);
			//设置响应头
			response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
			
			byte[] len = new byte[1024*2];
			int read = 0;
			while((read=is.read(len)) != -1){
				bos.write(len, 0, read);
				System.out.println("read---"+read);
			}
			bos.flush();
			bos.close();
			is.close();*/
		}

}