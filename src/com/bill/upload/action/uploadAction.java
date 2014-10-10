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
		System.out.println("execute��ִ��...");
		//�����ļ�Ҫ�����·��
		String uploadPath=ServletActionContext.getServletContext().getRealPath("/upload");
		
		try {
			System.out.println("123"+myFileFileName);
			//�����ļ�д����
			InputStream is=new FileInputStream(this.getMyFile());
			//����Ŀ���ļ�
			File toFile=new File(uploadPath, myFileFileName);
			//���������
			OutputStream os=new FileOutputStream(toFile);
			//���û���
			byte[] buffer=new byte[1024];
			int length=0;
			//��ȡfile�ļ���toFile
			while ((length=is.read(buffer))!=-1) {
				
				os.write(buffer,0,length);
			}
			System.out.println("�ļ�����"+myFileFileName);
			System.out.println("�ļ����ͣ�"+myFileContentType);
			is.close();
			os.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		insert(myFileFileName, myFileContentType,uploadPath);
		System.out.println("д��ɹ�");
		return "success";
	}
	
	//�����ݿ�д��
	public void insert(String filename,String filetype,String filepath){
		Connection conn=null;
		Statement stmt=null;
		DBUtils db=new DBUtils();
		conn=db.getConnection();
		try {
			stmt=conn.createStatement();
			System.out.println("�ļ�����"+filename);
			System.out.println("�ļ�����"+filetype);
			System.out.println("�ļ�����"+filepath);
			stmt.execute("insert into files(fileName,fileContentType,filePath) values('"+filename+"','"+filetype+"','"+filepath+"')");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		db.close(stmt, conn);
	}
	//���������ļ�
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
