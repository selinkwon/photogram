<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp"%>

<!--인기 게시글-->
<main class="popular">
	<div class="exploreContainer">

		<!--인기게시글 갤러리(GRID배치)-->
		<div class="popular-gallery">
		
		<!-- 게시글을 클릭시 해당 계정으로 이동 -> 게시글로 이동되게 수정 -->
			<c:forEach var="image" items="${images }">
				<div class="p-img-box">
					<a href="/user/${image.user.id }"> <img src="/upload/${image.postImageUrl }" />
					</a>
				</div>
			</c:forEach>
			
		</div>
	</div>
</main>

<%@ include file="../layout/footer.jsp"%>

