<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>자랑하기 게시판</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
</head>
<script>
	function readBoard(no) {
		location.href='/board/proud/readboard?no=' + no;
	}
</script>
<body>

	<jsp:include page="../../template.jsp"></jsp:include>
	<div class= "container">
		<c:choose>
			<c:when test="${proudlistBoard != null }">
				<table class="table table-hover">
					<thead>
						<tr>
							<th>글번호</th>
							<th>글제목</th>
							<th>작성자</th>
							<th>작성일</th>
							<th>조회수</th>
							<th>좋아요</th>
						</tr>
					</thead>						
					<tbody>
						<c:forEach var ="board" items="${proudlistBoard }">
						<tr onclick="readBoard(${board.no});">
							<td>
     						<img id='thumnail' src='../../resources/${board.image }'  width="120px" height="120px;"/>
     						</td>
							<td>${board.title }</td>
							<td>${board.writer }</td>
							<td>${board.postdate }</td>
							<td>${board.readcount }</td>
							<td>${board.likecount }</td>
						</tr>
						</c:forEach>
					</tbody>
				</table>
			</c:when>
		</c:choose>

		<div style="float: right">
			<button type="button" class= "btn btn-success" onclick = "location.href='/board/proud/register';">글 등록</button>
		</div>
		
		<div style="margin-top : 15px; clear:right;">
			<ul class="pagination">
            <c:if test="${param.pageNo > 1 }">
               <li><a href="/board/proud/listAll?pageNo=1&searchBy=${param.searchBy}&searchWord=${param.searchWord}">&lt;&lt;</a></li>
            </c:if>
            <c:if test="${param.pageNo > 1 }">
               <li>
               <a href="/board/proud/listAll?pageNo=${param.pageNo - 1}&searchBy=${param.searchBy}&searchWord=${param.searchWord}">&lt;</a>
            </c:if>

            <c:forEach var="i" begin="${pagingInfo.startPageNoOfBlock }"
               end="${pagingInfo.endPageNoOfBlock }" step="1">
               <li><a href="/board/proud/listAll?pageNo=${i }&searchBy=${param.searchBy}&searchWord=${param.searchWord}">${i }</a></li>
            </c:forEach>
            <c:if test="${param.pageNo < pagingInfo.totalPage }">
               <li><a href="/board/proud/listAll?pageNo=${param.pageNo + 1 }&searchBy=${param.searchBy}&searchWord=${param.searchWord}">&gt;</a></li>
            </c:if>
            <c:if test="${param.pageNo < pagingInfo.totalPage }">
               <li><a href="/board/proud/listAll?pageNo=${pagingInfo.totalPage }&searchBy=${param.searchBy}&searchWord=${param.searchWord}">&gt;&gt;</a></li>
            </c:if>
         </ul>
		</div>
		
	</div>

</body>
</html>