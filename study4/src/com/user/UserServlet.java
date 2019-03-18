package com.user;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionListener;
import javax.servlet.http.Part;

// enctype="multipart/form-data"�� ���� 3.0���ʹ� Part �������̽� ��ü�� ����

@WebServlet("/user/*")
@MultipartConfig(
		location = "c:/temp", 
		fileSizeThreshold = 1024 * 1024, 	 // �޸� ��Ʈ���� ũ��
		maxFileSize = 1024 * 1024 * 5, 		 // �ϳ��� ������ �ִ� �뷮 (5�ް�)
		maxRequestSize = 1024 * 1024 * 5 * 5 // form ��ü �뷮
)
public class UserServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		process(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		process(req, resp);	
	}
	
	protected void forward(HttpServletRequest req, HttpServletResponse resp, String path) throws ServletException, IOException {
		RequestDispatcher rd = req.getRequestDispatcher(path);
		rd.forward(req, resp);
	}
	
	protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String uri = req.getRequestURI();
		
		if(uri.indexOf("write.ok") != -1) {
			forward(req, resp, "/WEB-INF/views/user/write.jsp");
		} else if(uri.indexOf("write_ok.ok") != -1) {
			HttpSession session = req.getSession();
			
			String root = session.getServletContext().getRealPath("/");
			String pathname = root + "uploads" + File.separator + "user";
			
			File f = new File(pathname);
			if(!f.exists()) {
				f.mkdirs();
			}
			
			forward(req, resp, "/WEB-INF/views/user/write_ok.jsp");
		}
	}
	
	// ���� �̿��� �Ķ���� �ޱ�
	private String getParameterValue(InputStream is) throws IOException {
		StringBuffer sb = new StringBuffer();
		
		InputStreamReader reader = new InputStreamReader(is, "UTF-8");
		char []cc = new char[512];
		int len = -1;
		
		try {
			while((len = reader.read(cc)) != -1) {
				sb.append(cc, 0, len);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (Exception e2) {
				 
			}
		}
		
		return sb.toString();
	}
	
	private String getOriginalFilename(Part part) {
		for(String s : part.getHeader("content-disposition").split(";")) {
			if(s.trim().startsWith("filename")) {
				return s.substring(s.indexOf("=") + 1).trim().replace("\"","");
			}
		}
		
		return null;
	}
	
}
