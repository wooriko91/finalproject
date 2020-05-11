<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

<h1>${login.userName}님의 MYPAGE</h1> 	

	<!-- 비밀번호 변경, 생일, 휴대폰번호, 프로필 이미지, 주소 확인과 수정-->
	<a href="<c:url value='/mypage/myinfoChange' />">내 정보</a> <br>
	<!-- 온/오프 강의 관련 정보 -->
	<a href="<c:url value='/mypage/leclist?type=on' />">수강 목록</a> <br>
	<!-- 현재 포인트랑 쿠폰/결제 +/- 내역 -->
	<a href="<c:url value='/mypage/pointInfo' />">위코드 머니</a> <br>
	<!-- 쓴 게시물, 댓글, 리뷰 확인  -->
	<a href="<c:url value='/mypage/recentAct' />">최근활동</a> <br>
	<br><br>

	<a href="<c:url value='/' />">HOME</a> <br>





</body>
</html>