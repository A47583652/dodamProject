<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>실종 : ${MissingBoard.title }</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
	<script>
		$(function() {
			let imgAr = "${MissingBoard.img}".split(", ");
			console.log(imgAr);
			let output = "";
			
			// or 슬릭으로 해보던지??
			
			let imgCnt = 0;
			for (let i=0; i<imgAr.length; i++) {
				if (imgAr[i] != "") {
					if (imgAr[i].split(":")[0] == "http") {
						output += "<img src='" + imgAr[i] + "' width='30%'/>";
					} else {
						output += "<img src='../resources/uploads/kmj/missing" + imgAr[i] + "' width='30%'/>";
					}
					imgCnt++;
				}
			}
			
			for (let i=0; i<3-imgCnt; i++) {
				output += "<img src='../../resources/images/kmj/missing/noimage.png' width='30%' />";
			}
			
			console.log(output);
			$(".img_container").html(output);
			
			let animal = process_animal();
			let missingdate = "${MissingBoard.missingdate }".split(" ")[0];
			let registerdate = "${MissingBoard.registerdate}".substring(0,"${MissingBoard.registerdate}".lastIndexOf(":"));
			let gender = process_gender();
			
			$("#registerdate").html(registerdate);
			$("#missingdate").html(missingdate);	
			$("#animal_breed").html(animal);
			$("#gender").html(gender);
			
			if ("${MissingBoard.category}" == "missing") {
				$("#category").attr("class", "foundBtn");
				$("#category").html("찾았어요!");
			} else {
				$("#category").attr("class", "missingBtn");
				$("#category").html("찾았어요 취소");
			}
			
			$("#like").click(function() {
				let no = "${MissingBoard.no }";
				
				if (status == "foundBtn") {
					category = "found";
				} else if (status == "missingBtn"){
					category = "missing";
				}
				
				let url = '/missing/changeCategory';
				
				$.ajax({
					url : url, // ajax와 통신 할 곳
					data : {no : no, category : category}, // 서블릿에 보낼 데이터
					dataType : "text", // 수신될 데이터의 타입
					type : "POST", // 통신 방식
					success : function(data) { // 통신 성공시 수행될 콜백 함수
						if (data == "success") {
							if (category == "found") {
								$("#category").attr("class", "missingBtn");
								$("#category").html("찾았어요 취소");
							} else {
								$("#category").attr("class", "foundBtn");
								$("#category").html("찾았어요!");
							}
						}
					}
				});
			});
		});
		
		function process_animal() {
			let animal = "";
			if ("${MissingBoard.animal }" == "dog") {
				animal = "개";
			} else if ("${MissingBoard.animal }" == "cat") {
				animal = "고양이";
			}
			
			return animal;
		}
			
		function process_gender() {
			let gender = "";
			if ("${MissingBoard.gender }" == 'M') {
				gender = "수컷";
			} else if ("${MissingBoard.gender }" == 'F') {
				gender = "암컷";
			} else if ("${MissingBoard.gender }" == 'N') {
				gender = "성별 모름";
			}
		
			return gender;
		}
		
		function deleteBoard() {
			let url = '/missing/remove';
			let no = "${MissingBoard.no }";
			
			$.ajax({
				url : url, // ajax와 통신 할 곳
				data : {no : no}, // 서블릿에 보낼 데이터
				dataType : "text", // 수신될 데이터의 타입
				type : "POST", // 통신 방식
				success : function(data) { // 통신 성공시 수행될 콜백 함수
					console.log(data);
					if(data == "success") {
						alert("게시물을 삭제하였습니다!");
						location.href="/missing/list";
					} else {
						alert("게시물을 삭제하는데에 실패하였습니다!");
					}
				}
			});
		}
		
		function showReply() {
			$("#replyDiv").show(500);
		}
		
		function changeCategory() {
			let status = $("#category").attr("class");
			let category = '';
			let no = "${MissingBoard.no }";
			
			if (status == "foundBtn") {
				category = "found";
			} else if (status == "missingBtn"){
				category = "missing";
			}
			
			let url = '/missing/changeCategory';
			
			$.ajax({
				url : url, // ajax와 통신 할 곳
				data : {no : no, category : category}, // 서블릿에 보낼 데이터
				dataType : "text", // 수신될 데이터의 타입
				type : "POST", // 통신 방식
				success : function(data) { // 통신 성공시 수행될 콜백 함수
					if (data == "success") {
						if (category == "found") {
							$("#category").attr("class", "missingBtn");
							$("#category").html("찾았어요 취소");
						} else {
							$("#category").attr("class", "foundBtn");
							$("#category").html("찾았어요!");
						}
					}
				}
			});
		}
	
	</script>
	<style>
		h1 {
			font-size: 48px;
		}
	
		.img_container {
			display : block;
			margin : 30px 0;
			text-align: center;
		}
		
		.info_index {
			width : 15%;
		}
		
		table {
			font-size: 14px;
			width: 100%;
		}
		
		.table {
			font-size: 16px;
		}
		
		.wrap {
			width: 90%;
			margin: auto;
		}
		
		.add_border {
			border-top: 2px solid #666666;
		}
		
		.img_tr {
			border-top: 1px solid #d9d9d9;
		}
		
		#replyDiv {
			boarder : 1px dotted #e1bee7;
			display : none;
			padding : 5px;
		}
		
		textarea {
			width: 80%;
		}
		
		#like {
			width: 20px;
			margin-bottom: 2px;
			cursor: pointer;
		}
		
		.foundBtn {
			background-color: #ff7f00;
			width: 130px;
			height: 30px;
			border-radius: 4px;
			font-size: 16px;
			text-align: center;
			display: table-cell;
      		vertical-align: middle;
      		float: right;
      		cursor: pointer;
		}
		
		.missingBtn {
			background-color: #d9d9d9;
			width: 130px;
			height: 30px;
			border-radius: 4px;
			font-size: 16px;
			text-align: center;
			display: table-cell;
      		vertical-align: middle;
      		float: right;
      		cursor: pointer;
		}
	</style>
</head>
<body>
	<jsp:include page="../../template.jsp"></jsp:include>
	<div class="container">
		<div class="wrap">
			<table>
				<tr>
					<td colspan="2"><h1 style="color: #ff7f00;"><span id="found_"></span>※ ${MissingBoard.title } ※</h1>
					<!-- <td></td> -->
				</tr>
				<tr>
					<td>
						<div style="margin: 10px 0 20px 0;">${MissingBoard.writer } | 
						<span id="registerdate"></span> |
					 	조회 ${MissingBoard.readcount } | 좋아요 
					 	<img src="../../resources/images/kmj/missing/dislike.png" id="like" /> ${MissingBoard.likecount } </div>
					 	<div id="category" onclick="changeCategory();"></div>
					 </td>
					<!-- <td></td> -->
				</tr>
				<tr class="img_tr">
					<td colspan="2"><div class="img_container"></div></td>
					<!-- <td></td> -->
				</tr>
				<tr>
					<td colspan="2"><h3>실종 동물 정보</h3></td>
					<!-- <td></td> -->
				</tr>
				<tr>
					<td colspan="2">
						<table class="table table-hover">
							<tbody>
								<tr class="add_border">
									<td class="info_index">이름</td>
									<td>${MissingBoard.name }</td>
								</tr>
								<tr>
									<td class="info_index">종</td>
									<td><span id="animal_breed"></span> / ${MissingBoard.breed }</td>
								</tr>
								<tr>
									<td class="info_index">성별</td>
									<td><span id="gender"></span></td>
								</tr>
								<tr>
									<td class="info_index">실종일자</td>
									<td><span id="missingdate"></span></td>
								</tr>
								<tr>
									<td>실종장소</td>
									<td>${MissingBoard.location } 
									<c:if test="${MissingBoard.dlocation != null }">
										${MissingBoard.dlocation }
									</c:if>
									</td>
								</tr>
								<tr>
									<td>특이사항</td>
									<td>${MissingBoard.contents }</td>
								</tr>
							</tbody>
						</table>
					</td>
					<!-- <td></td> -->
				</tr>
			</table>
			<button type="button" class="btn btn-danger" onclick="location.href='/missing/list'">목록</button>
			<button type="button" class="btn btn-danger" onclick="showReply();">댓글달기</button>
			<button type="button" class="btn btn-danger" onclick="location.href='/missing/modify?no=${MissingBoard.no}'">수정</button>
			<button type="button" class="btn btn-danger" onclick="deleteBoard();">삭제</button>
			
			<div id="replyDiv" style="clear: both;">
				<div class="form-group">
					<div class="checkbox">
	  					<label><input type="checkbox" value="" id="isSecret">비밀글로 등록</label>
					</div>
	            	<label for="replyContents">댓글 내용: </label>
	            	<div><textarea rows="6"  id="replyContents"></textarea></div>
	         	</div>
	         	<button type="button" class="btn btn-danger" onclick="addReply();">댓글등록</button>
			</div>
			
			<div id="replyLst"></div>
			
			<div id="replyModify" style="display: none;">
				<div>댓글 수정</div>
				<div class="form-group">
					<div class="checkbox">
	  					<label><input type="checkbox" value="" id="isSecretModify">비밀글로 등록</label>
					</div>
	            	<label for="replyContents">댓글 내용:</label>
	            	<textarea rows="6" id="replyContentsModify"></textarea>
	         	</div>
	         	<button type="button" class="btn btn-danger" onclick="modifyReply();">댓글수정</button>
			</div>
			
			<div id="replyRemove" style="display: none;">
				<div>댓글 삭제</div>
				<div class="form-group">
					정말로 진심으로 진짜로 삭제할까요? (삭제된 댓글은 복구가 불가능 합니다!)
				<button type="button" class="btn btn-info" onclick="closeRemove();">취소</button>
				<button type="button" class="btn btn-warning" onclick="removeReply();">댓글 삭제</button>
				</div>
			</div>
		</div>
	</div>
</body>
</html>