package cn.xiyou.works.student.service;

import java.sql.SQLException;
import java.util.List;

import org.junit.Test;

import cn.xiyou.works.pager.PageBean;
import cn.xiyou.works.student.dao.StudentDao;
import cn.xiyou.works.work.Work;

public class StudentService {
	StudentDao sd = new StudentDao();
	/**
	 * 查找数据库所有条目数
	 */
	public Long findSum(){
		try {
			return sd.findSum();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * 查找所有已通过审核的作品
	 */
	public List<Work> findAll(int pc,int ps){
		try {
			return sd.findAll(pc,ps);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * 通过ID查找作品
	 * @param wid
	 * @return
	 */
	public Work finById(String wid){
		try {
			return sd.findByid(wid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * 按照作品类型进行查找
	 * @param type
	 * @return
	 */
	public List<Work> findByType(String type,int pc,int ps){
		try {
			return sd.findByType(type,pc,ps);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * 点赞
	 * @param wid
	 * @throws SQLException 
	 */
	public void praise(String wid) {
		 try {
			sd.praise(wid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * 踩
	 * @param wid
	 */
	public void stamp(String wid){
		try {
			sd.stamp(wid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * 通过作品名称模糊查询
	 * @param name
	 * @return
	 */
	public List<Work> findByName(String name,int pc,int ps) {
//	@Test
//	public void a(){	
	try {
			List<Work> pb =  sd.findByName(name,pc,ps);
			return pb;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * 按照点赞量查找
	 * @return
	 */
	public List<Work> findByPraise(int ps) {
//	@Test
//	public void a (){
		try {
			List<Work> pb  = sd.findByPraise(ps);
			
			return pb;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	public List<Work> findByCompose(String work, String student, String type, int pc,int ps) {
		try {
			List<Work> pb = sd.findByCompose(work,student,type,pc,ps);
			return pb;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	
		
	}
	
}
