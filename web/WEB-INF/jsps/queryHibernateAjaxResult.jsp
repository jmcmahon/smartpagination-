<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsps/taglibs.jsp"%>
<table width="50%" border="1" cellpadding="1" cellspacing="0"
	align="center">
	<tr>
		<td><paging:pagehead url="/sample/queryHibernateAjax.htm"
			orderBy="userName">userName</paging:pagehead></td>
		<td><paging:pagehead url="/sample/queryHibernateAjax.htm"
			orderBy="realName">realName</paging:pagehead></td>
		<td><paging:pagehead url="/sample/queryHibernateAjax.htm"
			orderBy="email">email</paging:pagehead></td>
		<td><paging:pagehead url="/sample/queryHibernateAjax.htm"
			orderBy="birth">birth</paging:pagehead></td>
		<td><paging:pagehead url="/sample/queryHibernateAjax.htm"
			orderBy="sex">sex</paging:pagehead></td>
	</tr>

	<c:if test="${userList !=null}">
		<c:forEach var="cItem" items="${userList}" varStatus="status">
			<tr>
				<td>${cItem.userName}</td>
				<td>${cItem.realName}</td>
				<td>${cItem.email}</td>
				<td align="left"><fmt:formatDate
					pattern="yyyy-MM-dd hh:MM:ss,S" value="${cItem.birth}" /></td>
				<td>${cItem.sex}</td>
			</tr>
		</c:forEach>
		<tr>
			<td colspan="5"><paging:pagebar
				url="/sample/queryHibernateAjax.htm" /></td>
		</tr>
	</c:if>
</table>