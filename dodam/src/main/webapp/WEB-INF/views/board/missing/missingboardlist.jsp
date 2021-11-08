<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<title>실종 반려 동물 : 찾습니다</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
<!-- Favicon -->
<link rel="icon" href="../../resources/assets/missing/assets/img/brand/favicon.png" type="image/png">
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Jua&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
</head>
<script>
	let categoryVal = "${param.category}";
	if (categoryVal == "") {
		categoryVal = "missing";
	}
	
	$(function(){
		// 검색해서 넘어온 경우 검색한 내용이 검색파트에 그대로 적용되어 있도록 함.
		if ("${param.location}" != "") {
			$("#location").val("${param.location}");
		}
		if ("${param.animal}" != "") {
			$("#animal").val("${param.animal}");
		}
		if ("${param.category}" == "" || "${param.category}" == "missing") {
			$("#missingCtg").css("background-color", "#3C6E9F");
			$("#missingCtg").css("color", "white");
		} else if ("${param.category}" == "found") {
			$("#foundCtg").css("background-color", "#3C6E9F");
			$("#foundCtg").css("color", "white");
		}
		if ("${param.searchWord}" != "") {
			$("#searchWord").val("${param.searchWord}");
		}
		if ("${param.itemsPerPage}" == "") {
			$("#itemsPerPage").val(20);
		} else {
			$("#itemsPerPage").val("${param.itemsPerPage}");
		}
		
		
		
		for(let i="${pagingInfo.startPageNoOfBlock}"; i <= "${pagingInfo.endPageNoOfBlock}"; i++) {
			console.log(i);
			if ("${param.pageNo}" == "") {
				$("#li1").children("a").css("color", "#ff7f00");
				$("#li1").children("a").css("font-weight", "bold");
				break;
			} else if (i == "${param.pageNo}") {
				$("#li"+i).children("a").css("color", "#ff7f00");
				$("#li"+i).children("a").css("font-weight", "bold");
				break;
			}
		}
		
		$("#itemsPerPage").change(function() {
			callAjax();
		});
		
		$("#location").change(function() {
			callAjax();
		});
		
		$("#animal").change(function() {
			callAjax();
		});
	});
	
	function callAjax() {
		let pageNo = 1;
		let itemsPerPage = $("#itemsPerPage").val();
		let searchWord = $("#searchWord").val();
		let location = $("#location").val();
		let animal = $("#animal").val();
		
		console.log(categoryVal);
		
		let url = '/board/missing/search';
		
		$.ajax({
			url : url, // ajax와 통신 할 곳
			data : {searchWord : searchWord, location : location, animal : animal, category : categoryVal, itemsPerPage : itemsPerPage}, // 서블릿에 보낼 데이터
			dataType : "json", // 수신될 데이터의 타입
			type : "POST", // 통신 방식
			success : function(data) { // 통신 성공시 수행될 콜백 함수
				console.log(data);
				parseResult(data);
				parsePaging(data.pagingInfo, pageNo);
				
				$("#li1").children("a").css("color", "#ff7f00");
				$("#li1").children("a").css("font-weight", "bold");
			}
		});
	}
	
	function parsePaging(pagingInfo, pageNo) {
		let location = $("#location").val();
		let animal = $("#animal").val();
		let itemsPerPage = $("#itemsPerPage").val();
		console.log(pagingInfo);
		let output = '';
		if ("${param.pageNo}" > 1 || "${param.pageNo}" != "" || pageNo != 1) {
			output += '<li><a href="/board/missing/list?&pageNo=1&searchWord=${param.searchWord }&location=' + location
				+ '&animal=' + animal + '&category=${param.category }&itemsPerPage=' + itemsPerPage + '">&lt;&lt;</a></li>';
			output += '<li><a href="/board/missing/list?&pageNo=${param.pageNo - 1 }&searchWord=${param.searchWord }&location=' + location
				+ '&animal=' + animal + '&category=${param.category }&itemsPerPage=' + itemsPerPage + '">&lt;</a></li>';
		}
		for (let i=pagingInfo.startPageNoOfBlock; i<= pagingInfo.endPageNoOfBlock; i++) {
			output += '<li id="li' + i + '"><a href="/board/missing/list?&pageNo=' + i + '&searchWord=${param.searchWord }&location=' + location
				+ '&animal=' + animal + '&category=${param.category }&itemsPerPage=' + itemsPerPage + '">' + i + '</a></li>';
		}
		if ("${param.pageNo}" == "" || "${param.pageNo}" < pagingInfo.totalPage ) {
			output += '<li><a href="/board/missing/list?&pageNo=${param.pageNo + 1 }&searchWord=${param.searchWord }&location=' + location
				+ '&animal=' + animal + '&category=${param.category }&itemsPerPage=' + itemsPerPage + '">&gt;</a></li>';
			output += '<li><a href="/board/missing/list?&pageNo=' + pagingInfo.totalPage + '&searchWord=${param.searchWord }&location=' + location
				+ '&animal=' + animal + '&category=${param.category }&itemsPerPage=' + itemsPerPage + '">&gt;&gt;</a></li>';
		}
		console.log(output);
		$(".pagination").html(output);
		
	}
	
	function parseResult(data) {
		let output = '';
		let MissingBoard = data.listMissingBoard;
		if (MissingBoard != '') {
			output += '<div style="margin-left: 10px;"><span id="numOflist">' + data.numOflist + '</span>개의 검색결과</div>';
			for (let i in MissingBoard) {
				output += '<a href="/board/missing/detail?no=' + MissingBoard[i].no + '&userid=${loginSession.userid}" class="detailAnchor">';
				output +='<div class="col-sm-3">';
				output += '<div class="img_container" style="padding: 20px 10px;">';
				if (MissingBoard[i].img != '') {
					if (MissingBoard[i].dpchknum == null) {
						output += '<img src="../../resources/uploads/kmj/missing' + MissingBoard[i].img + '" width="100%"/>';
					} else {
						output += '<img src="' + MissingBoard[i].img + '" width="100%" onerror="deleteBoard(${MissingBoard.no });"/>';  
					}
				} else {
					output += '<img src="../../resources/images/kmj/missing/noimage.png" width="100%"/>';
				}
				output += '</div><div class="contents_container"><table><tr><td><strong>' + MissingBoard[i].title + '</strong></td></tr><tr>';
				output += '<td>' + MissingBoard[i].name + ' / ' + MissingBoard[i].breed + ' / ' + MissingBoard[i].gender + ' / ' + MissingBoard[i].age 
				+ '</td></tr><tr>';
				output += '<td>' + MissingBoard[i].location + '</td></tr><tr>';
				output += '<td>' + MissingBoard[i].missingdateWithoutTime + '</td></tr></table></div></div></a>';
			}
		} else {
			output = '<div style="margin-left: 30px; font-size: 20px;"> 검색 결과가 존재하지 않습니다. </div>';
		}
		
		$(".container_list").html(output);
	}
	
	function deleteBoard(no) {
		let url = '/board/missing/remove';
		
		$.ajax({
			url : url, // ajax와 통신 할 곳
			data : {no : no}, // 서블릿에 보낼 데이터
			dataType : "text", // 수신될 데이터의 타입
			type : "POST", // 통신 방식
			success : function(data) { // 통신 성공시 수행될 콜백 함수
				console.log(data);
				if(data == "success") {
					history.go(0);
				} else {
					alert("이미지를 불러오는 데에 문제가 발생했습니다! 다시 시도해주세요.");
				}
			}
		});
	}
	
	function setCategory(obj) {
		let itemsPerPage = $("#itemsPerPage").val();
		let searchWord = $("#searchWord").val();
		let location = $("#location").val();
		let animal = $("#animal").val();
		categoryVal = $(obj).attr("id");
		categoryVal = categoryVal.substring(0, categoryVal.indexOf("C"));
		
		if (categoryVal == "missing") {
			$("#missingCtg").css("background-color", "#3C6E9F");
			$("#missingCtg").css("color", "white");
			$("#foundCtg").css("background-color", "#d5d5d5");
			$("#foundCtg").css("color", "black");
		} else if (categoryVal == "found") {
			$("#missingCtg").css("background-color", "#d5d5d5");
			$("#missingCtg").css("color", "black");
			$("#foundCtg").css("background-color", "#3C6E9F");
			$("#foundCtg").css("color", "white");
		}
		
		window.location.href = '/board/missing/list?pageNo=1&searchWord=${param.searchWord}&location=' + location + '&animal=' + animal 
								+ '&category=' + categoryVal + '&itemsPerPage=' + itemsPerPage;
	}
	
	function search() {
		let itemsPerPage = $("#itemsPerPage").val();
		let searchWord = $("#searchWord").val();
		let location = $("#location").val();
		let animal = $("#animal").val();
		
		window.location.href = '/board/missing/list?pageNo=1&searchWord=' + searchWord + '&location=' + location + '&animal=' + animal
								+ '&category=' + categoryVal + "&itemsPerPage=" + itemsPerPage;
		
	}
</script>
<style>
	.contents_container {
		height : 100px;
	}
	
	.wrap {
		margin-top: 80px;
		margin-bottom: 50px;
	}
	
	.detailAnchor {
		text-decoration: none;
		color : black;
	}
	
	.col-sm-3 {
		padding: 10px;
		height : 380px;
	}
	
	.col-sm-3:hover {
		background-color: lightblue;
	}
	
	.img_container {
		height : 74%;
	}
	
	.above_search {
		float: right;
		margin-right: 25px;
	}
	
	.container_list {
		clear: both;
		margin-top: 60px;
	}
	
	input:focus {
		outline:2px solid #3C6E9F;
		border-radius: 4px;
	}
	
	.categoryBtn {
		width: 140px;
		height: 40px;
		cursor: pointer;
		display: table-cell;
		text-align: center;
		border: 1px solid black;
		margin-top: 10px;
		padding-top: 10px;
		border-radius: 4px 4px 0 0;
		border: none;
		background-color: #d5d5d5;
	}
	
	.above_category {
		margin: 40px 0 25px 0;
		border-bottom: 5px solid #3C6E9F;
	}
	
	#noResult {
		text-align: center;
		margin : 50px;
	}
</style>
<body>
	<jsp:include page="../../template.jsp"></jsp:include>
	<div class="container wrap">
		<h1 style="font-family: 'Jua', sans-serif; font-size: 43px;">신고 목록</h1>
		<div class="above_search">
			<div style="float: right;">
				<button type="button" class="btn btn-primary" style="margin-bottom: 3px;" onclick="location.href='/board/missing/write'">글등록</button>
			</div>
			<div style="clear: right;">
				<select id="location">
					<option value="">--- 지역 ---</option>
				    <option value="서울특별시">서울특별시</option>
				    <option value="경기도">경기도</option>
				    <option value="인천광역시">인천광역시</option>
				    <option value="강원도">강원도</option>
				    <option value="충청남도">충청남도</option>
					<option value="대전광역시">대전광역시</option>
				    <option value="충청북도">충청북도</option>
				    <option value="세종특별자치시">세종시</option>
				    <option value="부산광역시">부산광역시</option>
					<option value="울산광역시">울산광역시</option>
				    <option value="대구광역시">대구광역시</option>
				    <option value="경상북도">경상북도</option>
				    <option value="경상남도">경상남도</option>
					<option value="전라남도">전라남도</option>
				    <option value="광주광역시">광주광역시</option>
				    <option value="전라북도">전라북도</option>
					<option value="제주특별자치도">제주도</option>
				</select>
				<select id="animal">
					<option value="">-- 동물 --</option>
					<option value="dog">강아지</option>
					<option value="cat">고양이</option>
					<option value="other">다른 동물</option>
				</select>
				<input type="text" id="searchWord" />
				<div class="btn btn-default" id="searchBtn" onclick="search();">검색</div>
			</div>
		</div>
		<div class="above_category" style="clear: right">
			<span class="categoryBtn" id="missingCtg" onclick="setCategory(this);">찾습니다</span>
			<span class="categoryBtn" id="foundCtg" onclick="setCategory(this);">찾았어요</span>
		</div>
		<div style="float: right;">
			<select id="itemsPerPage">
					<option value="16">16개씩 보기</option>
					<option value="20">20개씩 보기</option>
					<option value="24">24개씩 보기</option>
					<option value="28">28개씩 보기</option>
			</select>
		</div>
		<div class="container_list">
			<c:choose>
				<c:when test="${listMissingBoard eq '[]' }">
					<div style="margin-left: 30px; font-size: 20px;"> 검색 결과가 존재하지 않습니다. </div>
				</c:when>
				<c:otherwise>
					<div style="margin-left: 10px;"><span id="numOflist">${numOflist }</span>개의 검색결과</div>
					<c:forEach var="MissingBoard" items="${listMissingBoard }">
					<a href="/board/missing/detail?no=${MissingBoard.no}" class="detailAnchor">
						<div class="col-sm-3">
							<div class="img_container" style="padding: 20px 10px;">
								<c:choose>
									<c:when test="${MissingBoard.img != ''}">
										<c:choose>
											<c:when test="${MissingBoard.dpchknum eq null }">
												<img src="../../resources/uploads/kmj/missing${MissingBoard.img }" height="230px"/>
											</c:when>
											<c:otherwise>
												<c:choose>
													<c:when test="${MissingBoard.img == ''}">
														<img src="../../resources/images/kmj/missing/noimage.png" width="100%"/>
													</c:when>
													<c:otherwise>
														<img src="${MissingBoard.img }" width="100%" onerror="deleteBoard(${MissingBoard.no });" />
													</c:otherwise>
												</c:choose>
											</c:otherwise>
										</c:choose>
									</c:when>
									<c:otherwise>
										<img src="../../resources/images/kmj/missing/noimage.png" height="230px"/>
									</c:otherwise>
								</c:choose>
							</div>
							<div class="contents_container">
								<table>
									<tr>
										<td><strong>${MissingBoard.title }</strong></td>
									</tr>
									<tr>
										<td>${MissingBoard.name} / ${MissingBoard.breed } / ${MissingBoard.gender } / ${MissingBoard.age }</td>
									</tr>
									<tr>
										<td>${MissingBoard.location }</td>
									</tr>
									<tr>
										<td>실종일 : ${MissingBoard.missingdateWithoutTime }</td>
									</tr>
								</table>
							</div>
						</div>
					</a>
				</c:forEach>
			</c:otherwise>
			</c:choose>
		</div>
	</div>
		
	<div style="text-align: center;">
		<ul class="pagination">
			<c:if test="${param.pageNo > 1 }">
				<li><a href="/board/missing/list?&pageNo=1&searchWord=${param.searchWord }&location=${param.location }
				&animal=${param.animal }&category=${param.category }&itemsPerPage=${param.itemsPerPage}">&lt;&lt;</a></li>
				<li><a href="/board/missing/list?&pageNo=${param.pageNo - 1 }&searchWord=${param.searchWord }&location=${param.location }
				&animal=${param.animal }&category=${param.category }&itemsPerPage=${param.itemsPerPage}">&lt;</a></li>
			</c:if>
			<c:forEach var="i" begin="${pagingInfo.startPageNoOfBlock }"
				end="${pagingInfo.endPageNoOfBlock }" step="1">
				<li id="li${i }"><a href="/board/missing/list?&pageNo=${i }&searchWord=${param.searchWord }&location=${param.location }
				&animal=${param.animal }&category=${param.category }&itemsPerPage=${param.itemsPerPage}">${i }</a></li>
			</c:forEach>
			<c:if
				test="${param.pageNo == null or param.pageNo < pagingInfo.totalPage }">
				<li><a href="/board/missing/list?&pageNo=${param.pageNo + 1 }&searchWord=${param.searchWord }&location=${param.location }
				&animal=${param.animal }&category=${param.category }&itemsPerPage=${param.itemsPerPage}">&gt;</a></li>
				<li><a href="/board/missing/list?&pageNo=${pagingInfo.totalPage }&searchWord=${param.searchWord }&location=${param.location }
				&animal=${param.animal }&category=${param.category }&itemsPerPage=${param.itemsPerPage}">&gt;&gt;</a></li>
			</c:if>
		</ul>
		<br/><br/><br/>
	</div>
	<jsp:include page="../../footer.jsp"></jsp:include>
</body>
</html>