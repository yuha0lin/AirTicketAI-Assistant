package servlet;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import javabean.db_conn;
import javabean.flight;

public class search_fly extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		String url=null;
		if(session.getAttribute("url")!=null) {
			url=session.getAttribute("url").toString();
		}else {
			url="default/index.jsp";
		}
		resp.sendRedirect(url);
	}

	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		String url = "default/index.jsp";
		
		String departure = req.getParameter("departure");
		String destination = req.getParameter("destination");
		String departureYear = req.getParameter("departureYear");
		String departureMonth = req.getParameter("departureMonth");
		String departureDay = req.getParameter("departureDay");
		String sql = null;
		
		if (departure.isEmpty() && destination.isEmpty() && departureYear.isEmpty() && departureMonth.isEmpty() && departureDay.isEmpty()) {
			// 如果所有字段都为空，重定向到原页面
			resp.sendRedirect(url);
		} else {
			sql = "SELECT * FROM flight WHERE 1=1";
			
			if (!departure.isEmpty()) {
				sql += " AND f_s_p='" + departure + "'";
			}
			if (!destination.isEmpty()) {
				sql += " AND f_a_p='" + destination + "'";
			}
			if (!departureYear.isEmpty() && !departureMonth.isEmpty() && !departureDay.isEmpty()) {
				// 验证日期格式
				try {
					int year = Integer.parseInt(departureYear);
					int month = Integer.parseInt(departureMonth);
					int day = Integer.parseInt(departureDay);
					if (year >= 1900 && year <= 9999 && month >= 1 && month <= 12 && day >= 1 && day <= 31) {
						String departureDate = String.format("%04d-%02d-%02d", year, month, day);
						sql += " AND DATE(f_Date)='" + departureDate + "'";
					} else {
						req.setAttribute("errorMsg", "日期格式不正确，请输入有效的年、月、日");
					}
				} catch (NumberFormatException e) {
					req.setAttribute("errorMsg", "日期格式不正确，请输入数字");
				}
			}
			
			db_conn conn = new db_conn();
			ArrayList<flight> flightlist = new ArrayList<flight>();
			
			ResultSet res = conn.executeQuery(sql);
			try {
				while (res.next()) {
					flight flight_info = new flight();
					flight_info.setF_n(res.getString(1));
					flight_info.setF_s_p(res.getString(2));
					flight_info.setF_a_p(res.getString(3));
					flight_info.setF_s_a(res.getString(4));
					flight_info.setF_a_a(res.getString(5));
					flight_info.setF_s_t(res.getString(6));
					flight_info.setF_a_t(res.getString(7));
					flight_info.setF_p(res.getString(8));
					flightlist.add(flight_info);           
				}
				req.setAttribute("flightlist", flightlist);
			} catch (SQLException e) {
				System.out.println("错误信息：" + e);
			} finally {
				conn.closeDB();
			}
			req.getRequestDispatcher("default/search.jsp").forward(req, resp);
		}
	}
}
