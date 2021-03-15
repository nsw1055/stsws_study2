<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="../includes/header.jsp"%>

<h1 class="listHome h3 mb-4 text-gray-800">List Page</h1>


<select name="slt" class="slt form-select" aria-label="Default select example">
  <option value=''>---</option>
  <option value="t" ${pageDTO.type == "t" ? "selected" : "" }>제목</option>
  <option value="c" ${pageDTO.type == "c" ? "selected" : "" }>내용</option>
  <option value="w" ${pageDTO.type == "w" ? "selected" : "" }>작성자</option>
  <option value="tc" ${pageDTO.type == "tc" ? "selected" : "" }>제목/내용</option>
  <option value="tcw" ${pageDTO.type == "tcw" ? "selected" : "" }>제목/내용/작성자</option>
</select>
<form
	class="d-none d-sm-inline-block form-inline mr-auto ml-md-3 my-2 my-md-0 mw-100 navbar-search">
	<div class="input-group">
		<input name="searchword" value = "<c:out value=''/>" type="text" class="form-control bg-light border-1 small"
			placeholder="Search for..." aria-label="Search"
			aria-describedby="basic-addon2">
		<div class="input-group-append">
			<button class="searchBtn btn btn-primary" type="button">
				<i class="fas fa-search fa-sm"></i>
			</button>
		</div>
	</div>
</form>
<div style="margin: 10px">
	<table class="table table-striped">
		<thead>
			<tr>
				<th scope="col">#</th>
				<th scope="col">제목</th>
				<th scope="col">작성자</th>
				<th scope="col">날짜</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${list }" var="board">
				<tr class='listA' data-value='<c:out value="${board.bno }"/>'>
					<td><c:out value="${board.bno }" /></td>
					<td><c:out value="${board.title }" /></td>
					<td><c:out value="${board.writer }" /></td>
					<td><c:out value="${board.regDate }" /></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>
<ul class="pagination">
	<c:if test="${pageMaker.prev}">
		<li class="page-item"><a class="page-link"
			href="${pageMaker.start - 1}" tabindex="-1">Previous</a></li>
	</c:if>

	<c:forEach begin="${pageMaker.start }" end="${pageMaker.end }"
		var="num">

		<li class="page-item ${num == pageMaker.pageDTO.page? "active":"" }"><a
			class="page-link" href="${num}">${num }</a></li>

	</c:forEach>

	<c:if test="${pageMaker.next}">
		<li class="page-item"><a class="page-link"
			href="${pageMaker.end + 1 }">Next</a></li>
	</c:if>
</ul>



<form class="actionForm" action="/board/list" method="get">
	<input type="hidden" name="page" value="${pageDTO.page}"> 
	<input type="hidden" name="perSheet" value="${pageDTO.perSheet}">
	<input type="hidden" name="type" value="${pageDTO.type}">
	<input type="hidden" name="keyword" value="${pageDTO.keyword}">
</form>

<script>
const dq = document.querySelector.bind(document)
const dqA = document.querySelectorAll.bind(document)
const actionForm = document.querySelector(".actionForm")

dq(".listHome").addEventListener("click", function(e){
	const target = e.target
	console.log(target)
	target.setAttribute("href", "/board/list")
})

dq(".searchBtn").addEventListener("click", function (e) {
        const stype = dq(".slt")
        const idx = stype.selectedIndex
        const type = stype[idx].value
        actionForm.querySelector("input[name='page']").value = 1
        actionForm.querySelector("input[name='type']").value = type
        console.log(type)
        actionForm.querySelector("input[name='keyword']").value = dq("input[name='searchword']").value
        if(actionForm.querySelector("input[name='keyword']").value == "" ||  type == ""){
        	return
        }
        actionForm.submit();

    });

dq(".pagination").addEventListener("click", e=>{
		e.preventDefault()
		console.log(e.target)
		const pageNum= e.target.getAttribute("href")
		if(pageNum == null){
			return
		}
		console.log(pageNum)
		
		dq(".actionForm input[name='page']").value=pageNum
		if(actionForm.querySelector("input[name='keyword']").value == "" ||  actionForm.querySelector("input[name='type']").value == ""){
	    	actionForm.querySelector("input[name='keyword']").remove()
	    	actionForm.querySelector("input[name='type']").remove()
	    	
	    }
		actionForm.submit()
	},false)

dqA(".listA").forEach(a => {
    a.addEventListener("click", function (e) {
    const bno = e.currentTarget.getAttribute("data-value");
    console.log(bno)
    
    actionForm.setAttribute("action", "/board/read")
    actionForm.innerHTML += "<input type='hidden' name='bno' value='"+bno+"'>";
    if(actionForm.querySelector("input[name='keyword']").value == "" ||  actionForm.querySelector("input[name='type']").value == ""){
    	actionForm.querySelector("input[name='keyword']").remove()
    	actionForm.querySelector("input[name='type']").remove()
    	
    }
    actionForm.submit();
},false)
})

</script>
<%@include file="../includes/footer.jsp"%>