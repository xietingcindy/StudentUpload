package cn.xiyou.works.student.dao;

import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.itcast.jdbc.TxQueryRunner;
import cn.xiyou.works.work.Work;

public class UploadDao {
	private QueryRunner qr = new TxQueryRunner();
	
	/**
	 * 通过学号查找学生id
	 * @param number
	 * @return
	 * @throws SQLException
	 */
	public String findSidByNumber(String number) throws SQLException{
		String sql = "select sid from t_student where number=?";
		String sid = (String) qr.query(sql, new ScalarHandler(),number);
		return sid;
	}
			
	/**
	 * 通过作品类型查找作品类型id
	 * @param type
	 * @return
	 * @throws SQLException
	 */
	public String findPidByType(String type) throws SQLException{
		String sql = "select pid from t_type where pname=?";
		String pid = (String) qr.query(sql, new ScalarHandler(),type);
		return pid;
	}
	
	/**
	 * 按wid查作品并设置sid,pid
	 * @param wid
	 * @param sid
	 * @param pid
	 * @throws SQLException
	 */
	public void setPidAndSidByWid(String wid,String sid,String pid) throws SQLException{
		String sql = "update t_works set sid=?, pid=? where wid=?";
		
		Object[] param = {sid,pid,wid};
		qr.update(sql, param);
	}
	/**
	 * 保存表单内容至数据库
	 * @param form
	 * @throws SQLException
	 */
	public void saveForm(Work form) throws SQLException{
		String sql = "insert into t_works(wid,wname,address,time,status,praise,stamp,introduce,picture)" +
				" values(?,?,?,?,?,?,?,?,?)";
		Object params[] = {form.getWid(),form.getWname(),form.getAddress(),form.getTime(),form.getStatus(),form.getPraise(),form.getStamp(),form.getIntroduce(),form.getPicture()};
		qr.update(sql,params);
	}

	/**
	 * 添加学生信息
	 * @param sid
	 * @param sname
	 * @param sclass
	 * @param number
	 * @throws SQLException 
	 */
	public void addStudent(String sid, String sname, String sclass,
			String number) throws SQLException {
		String sql = "insert into t_student(sid,sname,sclass,number) values(?,?,?,?)";
		Object params[] = {sid,sname,sclass,number};
		qr.update(sql,params);
	}

	/**
	 * 添加作品类型
	 * @param pid
	 * @param type
	 * @throws SQLException 
	 */
	public void addType(String pid, String type) throws SQLException {
		String sql = "insert into t_type(pid,pname) values(?,?)";
		qr.update(sql,pid,type);
	}
}
