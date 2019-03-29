package cn.xiyou.works.teacher.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.itcast.jdbc.TxQueryRunner;
import cn.xiyou.works.pager.Expression;
import cn.xiyou.works.pager.PageConstants;
import cn.xiyou.works.work.Work;

public class TeacherDao {
	QueryRunner qr = new TxQueryRunner();
	
	/**
	 * 通过wid查找作品，查看作品详情
	 * @param wid
	 * @return
	 * @throws SQLException
	 */
	public Work findById(String wid) throws SQLException{
		String sql = "select wid,wname,sid,address,picture,pid,praise,stamp,introduce from t_works where wid=?";
		Work work = qr.query(sql, new BeanHandler<Work>(Work.class),wid);
		sql = "select pname from t_type where pid =?";
		String type = (String) qr.query(sql, new ScalarHandler(),work.getPid());//通过pid找到对应类型名称
		sql = "select sclass from t_student where sid =?";
		String sclass = (String) qr.query(sql, new ScalarHandler(),work.getSid());
		sql = "select sname from t_student where sid =?";
		String sname = (String) qr.query(sql, new ScalarHandler(),work.getSid());
		work.setSclass(sclass);
		work.setSname(sname);
		work.setType(type);
		return work;
	}
	/**
	 * 登录,通过用户名密码进行查找
	 * @param loginname
	 * @param password
	 * @return
	 * @throws SQLException
	 */
	public int login(String loginname,String password) throws SQLException{
		String sql = "select count(*) from t_teacher where loginname=? and password =?";
		Number n = (Number) qr.query(sql, new ScalarHandler(),loginname,password);
		return n.intValue();
	}
	/**
	 * 通过审核功能
	 * 通过id查找作品，修改器状态值，1为审核通过，2为未通过
	 * @param wid
	 * @throws SQLException 
	 */
	public void approved(String wid) throws SQLException{
		String sql = "update t_works set status=1 where wid=?";
		qr.update(sql,wid);
	}
	/**
	 * 删除作品
	 * @param wid
	 * @throws SQLException
	 */
	public void delete(String wid) throws SQLException{
		String sql = "delete from t_works where wid=?";
		qr.update(sql,wid);
	}
	/**
	 * 查询所有作品
	 * @return
	 * @throws SQLException
	 */
	public List<Work> selectAll(int pc,int ps) throws SQLException{
		/*String sql = "select wid,wname,sid,address,picture,pid,praise,stamp,introduce from t_works";
		return qr.query(sql, new BeanListHandler<Work>(Work.class));*/
		
		List<Expression> exprList = new ArrayList<Expression>();
//		exprList.add(new Expression("status","=","1"));
//		int ps = PageConstants.WORK_PAGE_SIZE;  //ps,每页个数
		List<Work> list = findByCriteria(exprList,pc,ps);
		return list;

	}
	/**
	 * 根据作品状态进行查找，未审核/已审核
	 * @param status
	 * @return
	 * @throws SQLException
	 */
	public List<Work> selectPassOrUnPass(int status,int pc,int ps) throws SQLException{
		/*String sql = "select wid,wname,sid,address,picture,pid,praise,stamp,introduce from t_works where status=?";
		return qr.query(sql, new BeanListHandler<Work>(Work.class),status);*/
		
		
		List<Expression> exprList = new ArrayList<Expression>();
		exprList.add(new Expression("status","=",status+""));
//		int ps = PageConstants.WORK_PAGE_SIZE;  //ps,每页个数
		List<Work> list = findByCriteria(exprList,pc,ps);
		return list;

	}
	/**
	 * 按照点赞量进行查找
	 * @return
	 * @throws SQLException
	 */
	public List<Work> findByPraise(int ps) throws SQLException {
		
		List<Expression> exprList = new ArrayList<Expression>();
		exprList.add(new Expression("status","=","1"));
//		int ps = PageConstants.POPULARITY_PAGE_SIZE;
		return findByCriteria(exprList,1,ps);

	}
	/**
	 * 通用查询方法
	 * @param exprList
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	private List<Work> findByCriteria(List<Expression> exprList,int pc,int ps) throws SQLException{
		/**
		 * 1.得到ps  pageSize 当前页总数
		 * 2.得到tr	totalRocode 总数
		 * 3.得到beanList 
		 * 4.创建PageBean,返回
		 */
		//1.得到ps
		   //当前页总数分为普通作品展示页数和人气作品页数,所以由对应查找传递过来
		//2.得到tr
		//通过exprList生成where子句
		StringBuilder whereSql = new StringBuilder(" where 1=1");
		List<Object> params = new ArrayList<Object>();//sql语句中的问号值
		
		for(Expression expr : exprList){
		/**
		 * 添加条件
		 * 1.以and开头
		 * 2.条件的名称
		 * 3.条件的运算符，
		 * 4.如果条件不是is null 再追加问号，并向params中添加一些问号对应的值	
		 */
		whereSql.append(" and ").append(expr.getName()).append(" ")
			.append(expr.getOperator()).append(" ");
		
		if(!expr.getOperator().equals("is null")){
			whereSql.append("?");
			params.add(expr.getValue());
		}
		}
		//查询数据库得到tr
		String sql = "select count(*) from t_works"+whereSql;
		Number n = (Number) qr.query(sql, new ScalarHandler(),params.toArray());
		int tr = n.intValue();//得到了总记录数
		//3.得到BeanList
//		sql = "select * from t_works"+whereSql+" limit ?,?";
		sql = "select wid,wname,sid,address,picture,pid,praise,stamp,introduce from t_works"+whereSql+" order by praise desc limit ?,?";
		params.add((pc-1)*ps);//第一个问号当前页首航记录的下标
		if(tr<pc*ps){
			params.add(tr);
		}else{
			params.add(ps);//一共查询ps调记录
		}
		
		
		List<Work> beanList = qr.query(sql,new BeanListHandler<Work>(Work.class),params.toArray());
		
		/*
		
		//创建PageBean，设置参数
		PageBean<Work> pb = new PageBean<Work>();
		//其中pagebean没有url，这个任务由servlet完成
		pb.setBeanList(beanList);
		pb.setPc(pc);       
		pb.setPs(ps);
		pb.setTr(tr);*/
		return beanList;
	}
}
