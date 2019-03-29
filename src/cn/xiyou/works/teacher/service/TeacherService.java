package cn.xiyou.works.teacher.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import cn.xiyou.works.teacher.dao.TeacherDao;
import cn.xiyou.works.work.Work;

public class TeacherService {
	TeacherDao td = new TeacherDao();
	
	/**
	 * 通过wid查看作品详情
	 * @param wid
	 * @return
	 */
	public Work findById(String wid){
		try {
			return td.findById(wid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * 查询功能
	 * 老师根据未审核已审核等条件进行查询
	 * @param condition
	 * @return
	 */
	@Test
	public List<Work> select(String condition,int pc,int ps){
		try {
				List<Work> list = new ArrayList<Work>();
				//判断查询条件，查询所有/已审核通过/未审核
				if(condition.equals("selectAll")){
					list = td.selectAll(pc,ps);
				}else if(condition.equals("selectPass")){
					list = td.selectPassOrUnPass(1,pc,ps);
				}else{
					list = td.selectPassOrUnPass(0,pc,ps);
				}
				//返回查询结果
				return list;
				
			} catch (SQLException e) {
				throw new RuntimeException();
			}
	}
	/**
	 * 登录功能
	 * @param loginname
	 * @param password
	 * @return
	 */
	public int login(String loginname,String password){
		try {
			return td.login(loginname, password);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * 通过审核功能
	 * @param wid
	 */
	public void approved(String wid){
		try {
			td.approved(wid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	public void delete(String wid){
		try {
			td.delete(wid);
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
			List<Work> pb  = td.findByPraise(ps);
			
			return pb;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
