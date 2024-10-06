package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import javabean.db_conn;//�����������ݿ���

public class check_login_reg extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		resp.setContentType("text/html;charset=utf-8");
		PrintWriter out = resp.getWriter();
		out.println("�ó��򲻽���ֱ�ӷ��ʣ��벻Ҫ���ԷǷ�����");
		resp.setHeader("refresh", "2;url=index/login_reg.jsp");
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		String log_name = req.getParameter("log_name");
		String tel=req.getParameter("reg_name");
		String id=req.getParameter("reg_p_id");
		String name=req.getParameter("reg_p_name");
		String sex=req.getParameter("reg_p_sex");
		String work=req.getParameter("reg_p_work");
		String log_pwd = req.getParameter("log_pwd");
		String reg_name = req.getParameter("reg_name");
		String reg_pwd1 = req.getParameter("reg_pwd1");
		String reg_pwd2 = req.getParameter("reg_pwd2");
		
		if(log_name!=null&&log_pwd!=null&&reg_name==null&&reg_pwd1==null&&reg_pwd2==null) {
			//���õ�¼���������¼
			go_login(log_name, log_pwd, req, resp);
			
		}
		else if(log_name==null&&log_pwd==null&&reg_name!=null&&reg_pwd1!=null&&reg_pwd2!=null&&reg_pwd1.equals(reg_pwd2)) {
			//����ע�᷽������ע��
			go_reg(id,name,tel,sex,work,reg_pwd1, req, resp);
			
		}
		else {
			
			resp.setContentType("text/html;charset=utf-8");
			PrintWriter out = resp.getWriter();
			out.println("�벻Ҫ���ԷǷ�����");
			resp.setHeader("refresh", "2;url=index/login_reg.jsp");
		}
	}
	
	protected void go_login(String name, String pwd, HttpServletRequest req, HttpServletResponse resp) {
		//System.out.println("go_login��������");
		db_conn conn = new db_conn();//�������ݿ����Ӷ���
		//ȡ��cookie����֤�Ƿ���url�������url������ת��url�����û������ת��index.jsp
		//Ŀ������ת���û������ԭҳ�棬����û������
		HttpSession session = req.getSession(); 
		/*����cookie��ת��ʹ��session�����¼��ҳ����ת�����⣬cookieò���޷�����������*/
		String sql = "select * from p_inf where p_tel = '"+name+"'";
		ResultSet res = conn.executeQuery(sql);
		try {
			
			if(res.next()) {
				String p_pwd = res.getString(6);
				
				if(pwd.equals(p_pwd)) {
					session.setAttribute("user_id", name);
					
					if(session.getAttribute("url")!=null) {
						String url = session.getAttribute("url").toString();
						try{
							url="/air_ticket_book/index.jsp";
							resp.sendRedirect(url);
							}
						catch (IOException e) {
							System.out.println("������Ϣ���£�"+e);
						}
					}else {
						try {
							resp.sendRedirect("index.jsp");
						}
						catch (IOException e) {
							System.out.println("������Ϣ���£�"+e);
						}
						
					}
				}else {
					//�û������������
					try {
						resp.setContentType("text/html;charset=utf-8");
						PrintWriter out = resp.getWriter();
						out.println("������������µ�¼");
						resp.setHeader("refresh", "2;url=index/login_reg.jsp");
					}catch (IOException e) {
						System.out.println("������Ϣ���£�"+e);
					}
					
				}				
			}else {
				//�û��˺Ŵ���
				try {
					resp.setContentType("text/html;charset=utf-8");
					PrintWriter out = resp.getWriter();
					out.println("�˺Ŵ��������µ�¼");
					resp.setHeader("refresh", "2;url=index/login_reg.jsp");
				}catch (IOException e) {
					System.out.println("������Ϣ���£�"+e);
				}
			}
			conn.closeDB();
		}
		catch (SQLException e) {
			System.out.println("������Ϣ���£�"+e);
		}
	}
	protected void go_reg(String id,String name, String tel,String sex,String work,String pwd1,HttpServletRequest req, HttpServletResponse resp) {
		//System.out.println("ע�᷽����������");
		resp.setContentType("text/html;charset=utf-8");
		
		db_conn conn = new db_conn();//�������ݿ����Ӷ���
		String sql = "select * from p_inf where p_tel = '"+tel+"'";
		ResultSet res = conn.executeQuery(sql);
		try {
			PrintWriter out = resp.getWriter();
			try {
				if(res.next()) {			
					out.println("���ֻ�������ע���˺�");
					resp.setHeader("refresh", "2;url=index/login_reg.jsp");			
				}else {
					sql = "insert into p_inf (p_id,p_name,p_tel,p_sex,p_work,p_pwd) values('"+id+"','"+name+"','"+tel+"','"+sex+"','"+work+"','"+pwd1+"')";
					int in_res = conn.executeInsert(sql);
					//System.out.println(in_res);
					if(in_res==1) {
						out.println("ע��ɹ���2��֮����ת����¼ҳ��");
						resp.setHeader("refresh", "2;url=index/login_reg.jsp");	
					}else {
						out.println("������������ϵ������Ա�����޲�bug");
						resp.setHeader("refresh", "2;url=index/login_reg.jsp");
					}
				}
			}catch (Exception e) {
				System.out.print("������Ϣ���£�"+e);
			}
		}catch (IOException e) {
			System.out.println("������"+e);
		}
		
		conn.closeDB();
	}
}
