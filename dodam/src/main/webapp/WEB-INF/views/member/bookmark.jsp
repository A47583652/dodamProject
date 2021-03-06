<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="icon" href="../../resources/assets/missing/assets/img/brand/favicon.png" type="image/png">
<title>마이페이지</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
</head>
<script>
function readBoard(no) {
	location.href='/board/missing/detail?no=' + no;
}
</script>
<body>

	<jsp:include page="../template.jsp"></jsp:include>
	<div class="container">
		<br />
		<h2>${loginSession.userid }님의 북마크 기록</h2>

		<br />
		<br />

		<table class="table table-hover">
			<thead>
				<tr>
					<th>카테고리</th>
					<th>제목</th>
					<th>작성자</th>
					<th>내용</th>
					<th>작성일</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="point" items="${bookmark }">
					<tr onclick="readBoard(${point.no});">
						<td>${point.category }</td>
						<td>${point.title }</td>
						<td>${point.writer }</td>
						<td>${point.contents }</td>
						<td>${point.registerdate }</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>

		<br />
		<button onclick="location.href='/member/mypage'">뒤로가기</button>
	</div>
</body>
</html>