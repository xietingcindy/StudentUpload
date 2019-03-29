package cn.xiyou.works.student.dao;

import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.itcast.jdbc.TxQueryRunner;

public class DownloadDao {
	private QueryRunner qr = new TxQueryRunner();
	
	/**
	 * 查找作品所在文件位置方法
	 * @param wid
	 * @return
	 * @throws SQLException
	 */
	public String findAddress(String wid) throws SQLException{
		String sql = "select address from t_works where wid=?";
		
		return (String) qr.query(sql, new ScalarHandler(),wid);
	}
	public String findName(String wid) throws SQLException{
		String sql = "select wname form t_works where wid=?";
		return (String) qr.query(sql, new ScalarHandler(),wid);
	}
}
