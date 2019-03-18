package com.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	
	private Map<String, MyAction> map = new HashMap<String, MyAction>();

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		
		ServletContext context = getServletContext();
		String pathname = config.getInitParameter("config");
		
		if(pathname == null) {
			throw new ServletException("<init-param> error");
		}
		
		// 경로 : /WEB-INF/config.properties
		//System.out.println(pathname); 
		pathname = context.getRealPath(pathname);
		// 경로 : C:\study\work\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\study4\WEB-INF\config.properties
		//System.out.println(pathname);
		
		// config.properties 파일의 내용을 읽어 Properties 객체에 저장
		FileInputStream fis = null;
		Properties p = new Properties();
		
		try {
			fis = new FileInputStream(pathname);
			p.load(fis);
		} catch (Exception e) {
			//e.printStackTrace();
			throw new ServletException(pathname + " file not found");
		} finally {
			if(fis != null) {
				try {
					fis.close();
				} catch (Exception e2) {
					
				}
			}
		}
		
		// properties 파일에 저장된 클래스 이름의 객체를 생성하여 map에 저장
		try {
			Iterator<Object> it = p.keySet().iterator();
			
			String className;
			
			while(it.hasNext()) {
				String key = (String)it.next();
				className = p.getProperty(key).trim();
				
				Class<?> cls = Class.forName(className);
				MyAction action = (MyAction)cls.newInstance();
				
				map.put(key, action);
			}
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		process(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		process(req, resp);
	}
	
	protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		
		MyAction action = null;
		
		try {
			String uri = req.getRequestURI();
			
			// uri에서 ContextPath 제거
			if(uri.indexOf(req.getContextPath()) == 0) {
				uri = uri.substring(req.getContextPath().length());
			}
			
			// uri에서 마지막 부분의 /*.do 제거
			if(uri.lastIndexOf("/") >= 0) {
				uri = uri.substring(0, uri.lastIndexOf("/"));
			}
			
			action = map.get(uri);
			action.execute(req, resp);
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	@Override
	public void destroy() {
		
	}
}
