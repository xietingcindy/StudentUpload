package cn.xiyou.works.student.service;

import java.sql.SQLException;

import cn.itcast.commons.CommonUtils;
import cn.xiyou.works.student.dao.UploadDao;
import cn.xiyou.works.work.Work;

public class UploadService {
	private UploadDao ud = new UploadDao();
	/**
	 * 保存表单内容
	 * @param form
	 */
	public void saveForm(Work form) {
		/**
		 * 1、保存表单内容
		 * 2、通过表单里的学号查sid
		 * 3、通过表单里作品类型查询pid
		 * 4、按wid查作品并设置sid,pid
		 */
		try {
			//1、保存表单内容
			System.out.println("service中的地址值为:"+form.getAddress());
			ud.saveForm(form);
			//2、通过表单里的学号查sid
			String sid = ud.findSidByNumber(form.getNumber());
			if(sid==null){//未查到，则手动创建并插入
				sid = CommonUtils.uuid();
				ud.addStudent(sid,form.getSname(),form.getSclass(),form.getNumber());
			}
			//3、通过表单里作品类型查询pid
			String pid = ud.findPidByType(form.getType());
			if(pid==null){//未查到，则手动创建并插入
				pid = CommonUtils.uuid();
				ud.addType(pid,form.getType());
			}
			//4、按wid查作品并设置sid,pid
			ud.setPidAndSidByWid(form.getWid(), sid, pid);
			
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

}
