package cn.xiyou.works.student.web.servlet;


import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;

import cn.itcast.commons.CommonUtils;
import cn.xiyou.works.student.service.UploadService;
import cn.xiyou.works.work.Work;
import net.sf.json.JSONArray;
/**
 * 作品上传servlet
 * @author lenovo
 *
 *1、设置字符编码，响应格式等
 *2、检查表单属性
 *3、临时文件定义，判断上传大小
 *4、设置上传路径及表单内容
 *5、进行文件上传及表单保存
 *
 *
 *图片在前
 *作品在后
 */
public class UploadServlet extends HttpServlet {  
	
	
	private UploadService us= new UploadService();//表单提交业务逻辑
	
	private String uploadPath;//文件保存的路径
	private String fileName;//文件名字
	private Work form = new Work();//保存表单数据
	private Map<String,String> path = new HashMap<String,String>();//保存文件存储路径的map
	private Map<String,Object> map = new HashMap<String,Object>();//保存前端页面表单数据的map
	private String pictureName;
	private Map<String,String> workName = new HashMap<String,String>();;
	/**
	 *  作品上传方法
	 */
    public void doGet(HttpServletRequest request, HttpServletResponse response)  
            throws ServletException, IOException { 
    	/**
    	 * 1、设置字符编码，响应格式等
    	 */
        request.setCharacterEncoding("UTF-8");  //设置字符编码
        response.setContentType("text/html;charset=UTF-8");//设置响应格式  
        PrintWriter out = response.getWriter();  //获取响应输出流
        
        /**
         * 2、检查表单属性
         */
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);//获取表单属性进行判断  
        if(!isMultipart){  
            throw new RuntimeException("请检查您的表单的enctype属性，确定是multipart/form-data");  
        }  
        /**
         * 3、临时文件定义，判断上传大小
         */
        DiskFileItemFactory dfif = new DiskFileItemFactory();  
        ServletFileUpload parser = new ServletFileUpload(dfif);  
        
        parser.setFileSizeMax(100*1024*1024);//设置单个文件上传的大小  
        parser.setSizeMax(1000*1024*1024);//多文件上传时总大小限制  
          
        //在 fileupload 包中， HTTP 请求中的复杂表单元素都被看做一个 FileItem对象
        List<FileItem> items = null;  
        try {  
            items = parser.parseRequest(request);//分离出具体的文本表单和上传文件  
        }catch(FileUploadBase.FileSizeLimitExceededException e) {  
            out.write("上传文件超出了100M");  
            return;  
        }catch(FileUploadBase.SizeLimitExceededException e){  
            out.write("总文件超出了1000M");  
            return;  
        }catch (FileUploadException e) {  
            e.printStackTrace();  
            throw new RuntimeException("解析上传内容失败，请重新试一下");  
        }  
        /**
         * 4、设置上传路径及表单内容
         */
        
        PathAndForm(items);
        
        /**
         * 5、进行文件上传及表单保存 
         */
        if(items!=null){  
            for(FileItem item:items){  
                if(item.isFormField()){  //判断是普通表单
                }else{  //为上传文件
                    processUploadField(item);  
                }  
            }  
        }  
        //表单保存
        us.saveForm(form);
        //给客户端的响应   
          
     /*   Map<String,String> map = new HashMap<String, String>();
		map.put("result", "OK");
		//返回结果
		response.setContentType("text/text;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		response.getWriter().print(JSONArray.fromObject(map));
		  */
//        out.write("OK");
        response.sendRedirect("upload.html");
    }
    
    
    /**
     * 
     * 设置上传路径及表单内容
     * @param item
     */
    private void PathAndForm(List<FileItem> items) {  
        //将前端页面的数据保存在
    	if(items!=null){  
            for(FileItem item:items)
            {  
                if(item.isFormField()){  //判断是普通表单
                	processUploadForm(item);
                	
                }else{  //为上传文件
                	processUploadPath(item);//设置文件存储路径         
                }  
            }  
        }
    	//将前端页面已有的值存在work中，封装在bean中便于保存在数据库中
    	form = CommonUtils.toBean(map, Work.class);
    	//将其他数据补齐
    	
    	form.setWid(getID());
//    	form.setWid(CommonUtils.uuid());//设置作品唯一Id
    	form.setTime(new Date());
    	form.setStatus(0);//设置当前作品状态为未激活
    	form.setPraise(0);//设置当前作品点赞量为0
    	form.setStamp(0);//设置当前作品踩量为0
    	System.out.println("form中保存的地址值为："+path.get(fileName));
    	form.setAddress(path.get(fileName));//设置作品保存地址
    	if(workName.get("picture")==null||workName.get("picture").equals("")){//如果未上传照片，则设置默认封面
    		form.setPicture("/picture/surf.jpg");
    	}else
    		form.setPicture(workName.get("picture"));//设置封面保存地址
    	
          
      } 
    private String getID(){
    	int i=0;
    	for(int j=0;j<100;j++){
    		i+=(int)((Math.random()*9+1)*100000);
    	}
    	String id = String.valueOf(i);
    	return id;
    }
    /**
     * 设置文件存储路径
     */
    private void processUploadPath(FileItem item){
    	//文件名称
       fileName = item.getFieldName();  
       if(item.getName()==null||item.getName().equals("")){
    	  path.put(fileName, null);
    	  return ;
       }
          /**
         * 设置上传路径 
         */
        
        //用户选择上传文件时  
        if(item.getName()!=null&&!item.getName().equals("")){ 
           //根据表单中的name获取学号以学号为子文件夹名及文件名称
           
        	String childDirectory = "files/"+(String) map.get("number");
//        	String childDirectory = "E:\\files\\"+(String) map.get("number");
        	
            //设置文件存储路径 从根路径开始
            uploadPath = getServletContext().getRealPath("/"+childDirectory);  
//        	uploadPath = childDirectory; 
        	System.out.println("上传路径设置为："+uploadPath);
            File storeDirectory = new File(uploadPath); //创建新文件路径为计算所得路径 
            if(!storeDirectory.exists()){  //检查文件是否已经存在
                storeDirectory.mkdirs();  
            } 
            uploadPath=uploadPath+"/"+item.getName();
            path.put(fileName, uploadPath);//将名及路径保存在map中
            uploadPath=childDirectory+"/"+item.getName();
            workName.put(fileName, uploadPath);
        }
    }
    /**
     * 设置表单
     * @param item
     */
    private void processUploadForm(FileItem item){
    	/**
    	 * 1、获取当前传过来的字段及字段值
    	 * 2、将值保存在map中
    	 * 3、使用CommonUtils.toBean(),将值封装在Work中便于存入数据库
    	 * 4、手动设置其他值
    	 */
    	String fieldName = item.getFieldName();//字段名  
        String fieldValue;//对应的字段值  
        try {  
            fieldValue = item.getString("UTF-8");  //获取了传过来的字段的值
            map.put(fieldName, fieldValue);//将值保存在map里
        } catch (UnsupportedEncodingException e) {  
            throw new RuntimeException("不支持UTF-8编码");  
         }  
    }
    
    /**
     * 5上传文件
     */
    private void processUploadField(FileItem item) { 
    	/**
    	 * 上传文件包括两种：作品文件和封面图片
    	 * 1、获取当前上传的类型：work/picture(表单的name)
    	 * 2、获取当前的文件名(上传时的名字)
    	 * 3、获取当前的路径(设置的路径
    	 * 4、上传
    	 */
        try {  
        		//1、获取上传类型
        		String type = item.getFieldName();//上传的类别，作品或封面
        		String name = item.getName();//作品名字
        		String wpath = path.get(type);//获取当前文件的存储路径
        		item.write(new File(wpath));//保存文件并删除临时文件  
        } catch (Exception e) {  
            throw new RuntimeException("上传失败,请重试");  
        }  
          
    }  
    public void doPost(HttpServletRequest request, HttpServletResponse response)  
            throws ServletException, IOException {  
        doGet(request, response);  
    }  
  
}




/*    *//**
     * 处理上传文件请求
     * 设置上传文件名及存储路径
     * @param item
     *//*
    private void processUploadField(FileItem item) {  
        try {  
        	//文件名
            String fileName = item.getName();  
              
            *//**
             * 4、设置上传路径 
             *//*
            
            //用户选择上传文件时  
            if(fileName!=null&&!fileName.equals("")){ 
            	//设置文件名为随机字符串和本身的文件名
                fileName = UUID.randomUUID().toString()+"_"+FilenameUtils.getName(fileName);  
                  
                //扩展名  取原文件扩展名
                String extension = FilenameUtils.getExtension(fileName);  
                //MIME类型  
                String contentType = item.getContentType();  
                
                //按照文件名的hashCode计算存储目录  
                String childDirectory = makeChildDirectory(getServletContext().getRealPath("/WEB-INF/files/"),fileName);  
                //设置文件存储路径 从根路径开始
                uploadPath = getServletContext().getRealPath("/WEB-INF/files/"+childDirectory);  
                File storeDirectory = new File(uploadPath); //创建新文件路径为计算所得路径 
                if(!storeDirectory.exists()){  //检查文件是否已经存在
                    storeDirectory.mkdirs();  
                }  
                System.out.println(fileName+"..."+uploadPath);  
                *//**
                 * 5上传
                 *//*
                item.write(new File(uploadPath+File.separator+fileName));//保存文件并删除临时文件  
                  
            }  
        } catch (Exception e) {  
            throw new RuntimeException("上传失败,请重试");  
        }  
          
    }  
    //计算存放的子目录  
    private String makeChildDirectory(String realPath, String fileName) {  
        int hashCode = fileName.hashCode();  
        int dir1 = hashCode&0xf;// 取1~4位  
        int dir2 = (hashCode&0xf0)>>4;//取5~8位  
          
        String directory = ""+dir1+File.separator+dir2;  
        File file = new File(realPath,directory);  
        if(!file.exists())  
            file.mkdirs();  
          
        return directory;  
    }  
    *//**
     * 处理普通表单数据
     * @param request
     * @param item
     *//*
    private void processFormField(HttpServletRequest request,FileItem item) {  
     	Map<String,Object> map =new HashMap<String,Object>(); 
     	
    	String fieldName = item.getFieldName();//字段名  
        String fieldValue;  
        try {  
            fieldValue = item.getString("UTF-8");  
        } catch (UnsupportedEncodingException e) {  
            throw new RuntimeException("不支持UTF-8编码");  
         }  
         System.out.println(fieldName+"="+fieldValue);  
   }  
  
    public void doPost(HttpServletRequest request, HttpServletResponse response)  
            throws ServletException, IOException {  
        doGet(request, response);  
    }  
  
}*/