<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<!-- 파비콘 적용 -->
<link rel="shortcut icon" href="<c:url value='/img/favicon/wcy-favicon.ico'/>">


<link rel="stylesheet" href="<c:url value='/css/commons.css'/>">


<title>WE CODE YOU | 모든 프로그래머를 위한 아카데미</title>
</head>
<body>

<jsp:include page="../include/header-sub.jsp" />


	<!-- 기본적으로 아이디 찾기 창 -->
<main class="wcy-main-content">
	<c:if test="${target eq 'email' }">
		가입 시 작성한 이름과 전화번호를 이용하여 이메일을 찾을 수 있습니다.
		<form action="<c:url value='/member/findEmail' />" method="post">
			이름 : <input type="text" name="userName" />
			전화번호: <input type="text" name="userTel" />
			<button>버-튼</button>
		</form>
		
	</c:if>
</main>
<!-- 이메일 찾기 결과 -->
<c:if test="${msg != null }">
		${msg}
</c:if>

	<!-- 클릭하면 비밀번호 찾기창 -->
<main class="wcy-main-content">
	<c:if test="${target eq 'pw' }">
	이메일을 입력하여 임시 비밀번호를 발송해 드립니다.
		<form action="<c:url value='/member/findPw' />" method="post">
		이메일: <input type="text" name="userEmail" />
		<button>임시 비밀번호 발송</button>
		
		</form>
	</c:if>
</main>


<jsp:include page="../include/footer.jsp" />   

</body>
</html>