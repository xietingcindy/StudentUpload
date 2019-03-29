package cn.xiyou.works.student.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;
import cn.xiyou.works.pager.Expression;
import cn.xiyou.works.pager.PageConstants;
import cn.xiyou.works.work.Work;

public class StudentDao {
	QueryRunner qr = new TxQueryRunner();
	
	/**
	 * 查找全部审核通过的作品
	 * @return
	 * @throws SQLException 
	 */
	public List<Work> findAll(int pc,int ps) throws SQLException{
		List<Expression> exprList = new ArrayList<Expression>();
		exprList.add(new Expression("status","=","1"));
//		int ps = PageConstants.WORK_PAGE_SIZE;  //ps,每页个数
		List<Work> list = findByCriteria(exprList,pc,ps);
		return list;
	}
	/**
	 * 分页查询
	 */
	/**
	 * 按书名模糊查询
	 * @param bname
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	
	public List<Work> findByName(String wname,int pc,int ps) throws SQLException{
		/**
		 * pc当前页数
		 * wname书名
		 */
		List<Expression> exprList = new ArrayList<Expression>();
		exprList.add(new Expression("status","=","1"));
		exprList.add(new Expression("wname","like","%"+wname+"%"));
//		int ps = PageConstants.WORK_PAGE_SIZE;
		return findByCriteria(exprList,pc,ps);
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
	/**
	 * 通过id查找作品
	 * @param wid
	 * @return
	 * @throws SQLException
	 */
	public Work findByid(String wid) throws SQLException{
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
	 * 按作品所属类型进行查找
	 * @param type
	 * @return
	 * @throws SQLException 
	 */
	public List<Work> findByType(String type,int pc,int ps) throws SQLException{
		List<Expression> exprList = new ArrayList<Expression>();
		//通过t_type表查询所查找类型所对应的pid
		String pid = findPid(type);
		exprList.add(new Expression("status","=","1"));
		exprList.add(new Expression("pid","=",pid));
//		int ps = PageConstants.WORK_PAGE_SIZE;
		return findByCriteria(exprList,pc,ps);
	}
	/**
	 * 通过具体类型查找其对应的pid
	 * @param type
	 * @return
	 * @throws SQLException
	 */
	private String findPid(String type) throws SQLException{
		String sql = "select pid from t_type where pname=?";
		String i= (String) qr.query(sql, new ScalarHandler(),type);
		
		return i;
	}
	private String findSid(String student) throws SQLException{
		String sql = "select sid from t_student where sname=?";
		return (String) qr.query(sql, new ScalarHandler(),student);
	}
	/**
	 * 点赞功能
	 * @param wid
	 * @throws SQLException 
	 */
	public void praise(String wid) throws SQLException{
		//查询出wid所对应作品的当前点赞量
		String sql1 = "select praise from t_works where wid=?";
		Number n = (Number) qr.query(sql1, new ScalarHandler(),wid);
		//点赞量加一
		int m = n.intValue()+1;
//		Number m = n.intValue()+1;
		//更新当前点赞量
		String sql2 = "update t_works set praise=? where wid=?";
		qr.update(sql2,m,wid);
	}
	/**
	 * 踩的功能
	 * 当前踩数加一
	 * @param wid
	 * @throws SQLException
	 */
	public void stamp(String wid) throws SQLException{
			//查询出wid所对应作品的当前点赞量
			String sql1 = "select stamp from t_works where wid=?";
			Number n = (Number) qr.query(sql1, new ScalarHandler(),wid);
			//点赞量加一
			int m = n.intValue()+1;
			//更新当前点赞量
			String sql2 = "update t_works set stamp=? where wid=?";
			qr.update(sql2,m,wid);
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
	public List<Work> findByCompose(String work, String student,
			String type, int pc,int ps) throws SQLException {
		List<Expression> exprList = new ArrayList<Expression>();
		String pid = findPid(type);
		String sid = findSid(student);
		exprList.add(new Expression("status","=","1"));
//		exprList.add(new Expression("wname","like","%"+work+"%"));
		exprList.add(new Expression("pid","=",pid));
		exprList.add(new Expression("sid","=",sid));
//		int ps = PageConstants.WORK_PAGE_SIZE;
		return findByCriteria(exprList,pc,ps);
	}
	public Long findSum() throws SQLException{
		String sql = "select count(*) from t_works";
		Long c = (Long) qr.query(sql, new ScalarHandler());
		return c;
		
	}
	
}
