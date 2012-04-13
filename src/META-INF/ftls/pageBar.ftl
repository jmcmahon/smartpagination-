<script langage=javascript>
function go2page(page, pageSize, totalpages){
    if(isNaN(pageSize) | pageSize < 1 | pageSize > 100){
        alert("请输入有效的每页记录数！");
        ${paging_pageSizeParam}.focus(); return false;
    }else if(isNaN(page) | page < 1 | page > totalpages){
        alert("请输入有效页码！");
        ${paging_toPageNoParam}.focus(); return false;
    }else{
        return true;
    }
}
</script>
<form id="inf-paging-form" action="${paging_formActionUrl}" method="GET" 
    onsubmit="return go2page(${paging_toPageNoParam}.value,${paging_pageSizeParam}.value,${paging_totalPages})">
<table class="pageBar">
<tr>
    <td>
        <span class="pageBarEndPage">
            <#if paging_firstUrl?exists><a href="${paging_firstUrl}">首页</a><#else>首页</#if>
        </span>
        <#list paging_nearPageList as nearPageInfo>
            <#if nearPageInfo["nearPageUrl"]?exists>
                <span class="pageBarIndex"><a href="${nearPageInfo["nearPageUrl"]}">${nearPageInfo["nearPageNo"]}</a></span>
            <#else>
                <span class="pageBarCurrPage">${nearPageInfo["nearPageNo"]}</span>
            </#if>
        </#list>
        <span class="pageBarEndPage">
            <#if paging_firstUrl?exists><a href="${paging_tailUrl}">尾页</a><#else>尾页</#if>&nbsp;&nbsp;
        </span>
        <span>总记录数:${paging_totalRecord}&nbsp;&nbsp;每页:<input type="text" 
         id="${paging_pageSizeParam}" name="${paging_pageSizeParam}" maxlength="3" size="3" value="${paging_size}" />
              &nbsp;&nbsp;总页数:${paging_totalPages}&nbsp;&nbsp;</span>
        <span>转到<input type="text" id="${paging_toPageNoParam}" name="${paging_toPageNoParam}" 
         maxlength="5" size="2" value="${paging_toPageNo}" />
        <input type="submit" value="Go" ></span>
     </td>
</tr>
</table>
</form>