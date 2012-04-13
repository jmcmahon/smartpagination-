<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsps/taglibs.jsp"%>
<html>
<head>
<title>SmartPagination——高级查询AJAX</title>
<link type="text/css"
	href="${ctxPath}/css/smoothness/jquery-ui-1.8.9.custom.css"
	rel="Stylesheet" />
<script type="text/javascript" src="${ctxPath}/js/jquery-1.5.min.js"></script>
<script type="text/javascript"
	src="${ctxPath}/js/jquery-ui-1.8.9.custom.min.js"></script>
<script type="text/javascript" src="${ctxPath}/js/jquery.form.js"></script>
<script>
    $(function() {
        $("#query").click(function(){
            $("#criterial").ajaxSubmit({
                success:showResponse,
                type:"POST",
                url:"${ctxPath}/sample/queryHibernateAjax.htm"
                });
        });

        function showResponse(result){
            $("#result").html(result);
            $("#resultWrapper").css('display','block');
        }

        $("#reset").click(function(){
        	$("#resultWrapper").css('display','none');
        	$("#criterial").reset();
        }); 
    });
</script>
</head>
<body>
    <div class="container">
        <h1>AJAX查询</h1>
        <div class="span-12 last">
	        <form id="criterial">
			  <fieldset><legend>查询条件</legend>
				<p>UserName:<input name="userName" maxlength="50" />like 
				            <input type="checkbox" name="userNameLike" />
				            Email:<input name="email" maxlength="50" />(LIKE)</p>
                <p>RealName:<input name="realName" maxlength="50" />(LIKE)
                   Sex:<select name="sex">
						<option value="m">男</option>
						<option value="f">女</option>
				       </select></p>
                <p><input id="query" type="button" value="查询" />
                <input id="reset" type="button" value="Reset" /></p>
			  </fieldset>
			</form>
        </div>
    </div>
    <div id="resultWrapper" style="display: none;">
        <h3>查询结果</h3>
	    <div id="result" class="span-8 last"></div>
	</div>
</body>
</html>