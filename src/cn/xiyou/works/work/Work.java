package cn.xiyou.works.work;

import java.util.Date;


public class Work {
	private int sum;
	private String wid;//作品id
	private String address;//作品所存储的路径
	private Date time;//作品上传时间
	private String picture;//作品封面路径
	private String type;//作品类型
	private int status;//作品状态，1审核通过，2未审核
	private int praise;//作品点赞数
	private int stamp;//作品被踩数量
	private String introduce;//作品简介
	private String sname;//学生姓名
	private String sclass;//学生班级
	private String number;//学号
	private String wname;//作品名称
	private String pid;//作品类型id
	private String sid;//学生id;
//	private String pictureName;//文件夹中保存的图片的名称包括后缀
//	private String workName;//文件夹作品名称包括后缀
	
	public String getSname() {
		return sname;
	}
	public int getSum() {
		return sum;
	}
	public void setSum(int sum) {
		this.sum = sum;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public void setSname(String sname) {
		this.sname = sname;
	}
	public String getSclass() {
		return sclass;
	}
	public void setSclass(String sclass) {
		this.sclass = sclass;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getWid() {
		return wid;
	}
	public void setWid(String wid) {
		this.wid = wid;
	}
	public String getWname() {
		return wname;
	}
	public void setWname(String wname) {
		this.wname = wname;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date date) {
		this.time = date;
	}
	//	public String getTime() {
//		return time;
//	}
//	public void setTime(String time) {
//		this.time = time;
//	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getPraise() {
		return praise;
	}
	public void setPraise(int praise) {
		this.praise = praise;
	}
	public int getStamp() {
		return stamp;
	}
	public void setStamp(int stamp) {
		this.stamp = stamp;
	}
	public String getIntroduce() {
		return introduce;
	}
	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "Work [sum=" + sum + ", wid=" + wid + ", address=" + address
				+ ", time=" + time + ", picture=" + picture + ", type=" + type
				+ ", status=" + status + ", praise=" + praise + ", stamp="
				+ stamp + ", introduce=" + introduce + ", sname=" + sname
				+ ", sclass=" + sclass + ", number=" + number + ", wname="
				+ wname + ", pid=" + pid + ", sid=" + sid + "]";
	}

	
}
