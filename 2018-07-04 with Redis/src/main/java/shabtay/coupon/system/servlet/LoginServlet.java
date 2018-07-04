package shabtay.coupon.system.servlet;

import java.io.IOException;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import shabtay.coupon.system.common.ClientType;
import shabtay.coupon.system.entry.CouponSystem;
import shabtay.coupon.system.exceptions.WrongLoginInputException;
import shabtay.coupon.system.facade.CouponClientFacade;

@Controller
public class LoginServlet {

	@Autowired
	private CouponSystem couponSystem;

	@RequestMapping(value = "/loginservlet", method = RequestMethod.POST)
	public void doGet(HttpServletRequest request, HttpServletResponse response) {

		
		String userName = request.getParameter("username");
		String password = request.getParameter("password");
		String userType = request.getParameter("usertype");

		try {
			CouponClientFacade facade = couponSystem.login(userName, password, ClientType.valueOf(userType));

				switch (userType) {
				case "ADMIN":
					request.getSession().setAttribute("adminFacade", facade);	
					response.sendRedirect("http://localhost:8080/admin/index.html");
//					response.sendRedirect("http://localhost:4200");
					
					break;
				case "COMPANY":
					request.getSession().setAttribute("companyFacade", facade);
					response.sendRedirect("http://localhost:8080/company/index.html");
//					response.sendRedirect("http://localhost:4201");
					break;
				case "CUSTOMER":
					request.getSession().setAttribute("customerFacade", facade); 
					response.sendRedirect("http://localhost:8080/customer/index.html");
//					response.sendRedirect("http://localhost:4202");
					break;
				
			}
		} catch (WrongLoginInputException | InterruptedException | IOException e) {
			try {
				response.sendRedirect("http://localhost:8080/login.html?error=wrongDataEntered");
			} catch (IOException e1) {
				e1.printStackTrace();
			}			
		}
	}
}
