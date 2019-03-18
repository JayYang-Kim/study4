<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
	String cp = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<form name="myForm" method="post" enctype="multipart/form-data" action="<%=cp%>/upload/write_ok.ok">
		<p>
			<span>작성자 : </span>
			<input type="text" name="name"/>
		</p>
		<p>
			<span>제목 : </span>
			<input type="text" name="subject"/>
		</p>
		<p>
			<span>파일1 : </span>
			<input type="file" name="upload1"/>
		</p>
		<p>
			<span>파일2 : </span>
			<input type="file" name="upload2"/>
		</p>
		<p>
			<button type="submit">등록하기</button>
		</p>
	</form>
</body>
</html>