package com.bill.upload.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;

import com.bill.upload.utils.DBUtils;
import com.opensymphony.xwork2.ActionContext;
import com.sun.corba.se.spi.orbutil.fsm.Input;

public class uploadAction implements SessionAware{

	private String myFileContentType;
	private String myFileFileName;
	private File myFile;
	public String getMyFileContentType() {
		return myFileContentType;
	}
	public void setMyFileContentType(String myFileContentType) {
		this.myFileContentType = myFileContentType;
	}
	public String getMyFileFileName() {
		return myFileFileName;
	}
	public void setMyFileFileName(String myFileFileName) {
		this.myFileFileName = myFileFileName;
	}
	public File getMyFile() {
		return myFile;
	}
	public void setMyFile(File myFile) {
		this.myFile = myFile;
	}
	public String execute(){
		System.out.println("execute已执行...");
		//设置文件要保存的路径
		String uploadPath=ServletActionContext.getServletContext().getRealPath("/upload");
		
		try {
			System.out.println("123"+myFileFileName);
			//创建文件写入流
			InputStream is=new FileInputStream(this.getMyFile());
			//设置目标文件
			File toFile=new File(uploadPath, myFileFileName);
			//设置输出流
			OutputStream os=new FileOutputStream(toFile);
			//设置缓存
			byte[] buffer=new byte[1024];
			int length=0;
			//读取file文件到toFile
			while ((length=is.read(buffer))!=-1) {
				
				os.write(buffer,0,length);
			}
			System.out.println("文件名："+myFileFileName);
			System.out.println("文件类型："+myFileContentType);
			is.close();
			os.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		insert(myFileFileName, myFileContentType,uploadPath);
		System.out.println("写入成功");
		return "success";
	}
	
	//向数据库写入
	public void insert(String filename,String filetype,String filepath){
		Connection conn=null;
		Statement stmt=null;
		DBUtils db=new DBUtils();
		conn=db.getConnection();
		try {
			stmt=conn.createStatement();
			System.out.println("文件名："+filename);
			System.out.println("文件名："+filetype);
			System.out.println("文件名："+filepath);
			stmt.execute("insert into files(fileName,fileContentType,filePath) values('"+filename+"','"+filetype+"','"+filepath+"')");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		db.close(stmt, conn);
	}
	//读出所有文件
	public String list(){
		Connection conn=null;
		Statement stmt=null;
		ResultSet rs=null;
		DBUtils db=new DBUtils();
		conn=db.getConnection();
		try {
			stmt=conn.createStatement();
			rs=stmt.executeQuery("select fileName,filePath from files");
			while(rs.next()){
				myFileFileName=rs.getString("fileName");
				myFileContentType=rs.getString("filePath");
				System.out.println(myFileFileName);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "list";
	}
}
