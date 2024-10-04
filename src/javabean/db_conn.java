package javabean;
/********************
*@IDE	eclipse
*@jdk	1.8.0_161
*********************/
import java.sql.*;

public class db_conn {
	public Connection conn = null;
	public ResultSet res = null;
	public Statement st = null;
	
	//���ݿ��ʼ������
	public  db_conn() {
		
        String URL="jdbc:mysql://localhost:3307/fly_ticket?useSSL=false&useUnicode=true&characterEncoding=UTF8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";     //���ݿ���3306
		String USER="root";    //���ݿ��û���
		String PWD="123456";    //���ݿ�����
	try{
		Class.forName("com.mysql.cj.jdbc.Driver");
	}catch(ClassNotFoundException e){
		System.out.println("��������ʧ��");
		System.out.println(e);
	}
	try{
			
			conn = DriverManager.getConnection(URL,USER,PWD);
//			System.out.println("success");
			st=conn.createStatement();
		}catch(SQLException e){
			System.out.println("�������ݿ�ʧ��");
			System.out.println(e);
		}
	}
	
	//�����ݿ����������
	public int executeInsert(String sql){
		int num=0;
		try{
			num=st.executeUpdate(sql);
		}
		catch(SQLException e){
			System.out.println("������ݳ�����Ϣ:"+e.getMessage());
		}
		return num;
	}
	
	//�����ݿ��в�ѯ����
	public ResultSet executeQuery(String sql){
		res=null;
		try{
			res=st.executeQuery(sql);
		}
		catch(SQLException e){
			System.out.print("��ѯ������Ϣ:"+e.getMessage());
		}
		return res;
	}
	
	//��������
	public int Update(String sql){
		int num=0;
		try{
			num=st.executeUpdate(sql);
		}catch(SQLException ex){
			System.out.print("ִ���޸��д���"+ex.getMessage());
		}
		return num;
	}
	
	//ɾ������
	public int executeDelete(String sql){
		int num=0;
		try{
			num=st.executeUpdate(sql);
		}
		catch(SQLException e){
			System.out.print("ִ��ɾ���д���:"+e.getMessage());
		}
		return num;
	}
	
	//�ر����ݿ�����
	public void closeDB(){
		try{
			st.close();
			conn.close();
		}
		catch(Exception e){
			System.out.print("ִ�йر�Connection�����д���:"+e.getMessage());
		}
	}
}
