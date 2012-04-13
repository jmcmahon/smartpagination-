<%@ page language="java" contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsps/taglibs.jsp" %>
<head>
	<title>SmartPagination——简单查询</title>
</head>

<body>
	<table width="100%" height="100%" border="0" align="center" valign="top" cellpadding="0" cellspacing="0">
		
		<tr valign="top"><td align="left">
			<table width="50%" border="1" cellpadding="1" cellspacing="0" align="center">
				<tr>
					<td colspan="5">点表头进行排序</td>
				</tr>
				<tr>
					<td width="15%"></td>
					<td width="20%"></td>
					<td width="20%"></td>
					<td width="35%"></td>
					<td width="10%"></td>
				</tr>
				<tr>
					<td>
						<paging:pageHead url="/sample/listHibernate.htm" orderBy="userName" >userName</paging:pageHead>
					</td>
					<td>
						<paging:pageHead url="/sample/listHibernate.htm" orderBy="realName" >realName</paging:pageHead>
					</td>	
					<td>
						<paging:pageHead url="/sample/listHibernate.htm" orderBy="email" >email</paging:pageHead>
					</td>	
					<td>
						<paging:pageHead url="/sample/listHibernate.htm" orderBy="birth" >birth</paging:pageHead>
					</td>
					<td>
						<paging:pageHead url="/sample/listHibernate.htm" orderBy="sex" >sex</paging:pageHead>
					</td>
				</tr>
				
				<c:if test="${userList !=null}">
				<c:forEach var="cItem" items="${userList}" varStatus="status">
					<tr>
						<td>${cItem.userName}</td>
						<td>${cItem.realName}</td>
						<td>${cItem.email}</td>
						<td align="left"><fmt:formatDate pattern="yyyy-MM-dd hh:MM:ss,S" value="${cItem.birth}"/></td>
						<td >${cItem.sex}</td>		
					</tr>
					<tr><td class="listViewHRS1" colSpan="5"></td></tr>
				</c:forEach>
				<tr>
					<td colspan="5"><paging:pageBar url="/sample/listHibernate.htm" /></td>
				</tr>
				</c:if>
				<tr>
					<td colspan="5"><a href="<c:url value="/sample/queryHibernate.htm"/>">高级查询</a></td>
				</tr>
				<tr>
					<td colspan="5"><a href="<c:url value="/index.jsp"/>">首页</a></td>
				</tr>
			</table>
		</td></tr>
	</table>
</body>