package org.powerstone.smartpagination.tag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.powerstone.smartpagination.common.PageModel;

public class PagingBarTest extends TestCase {
	String jsResult = "<script langage=javascript>\n"
			+ "function go2page(page, pageSize, totalpages){\n"
			+ "    if(isNaN(pageSize) | pageSize < 1 | pageSize > 100){\n"
			+ "        alert(\"请输入有效的每页记录数！\");\n"
			+ "        p_pageSize.focus(); return false;\n"
			+ "    }else if(isNaN(page) | page < 1 | page > totalpages){\n"
			+ "        alert(\"请输入有效页码！\");\n"
			+ "        p_toPageNo.focus(); return false;\n" + "    }else{\n"
			+ "        return true;\n    }\n" + "}\n" + "</script>\n";

	@Override
	@Before
	protected void setUp() throws Exception {
		super.setUp();
	}

	@Test
	public void testGenerateMap2Render() {
		PagingBar bar = new PagingBar();
		PageContext mockPageContext = Mockito.mock(PageContext.class);
		HttpServletRequest mockHttpServletRequest = Mockito
				.mock(HttpServletRequest.class);
		Mockito.when(mockPageContext.getRequest()).thenReturn(mockHttpServletRequest);
		Mockito.when(mockHttpServletRequest.getContextPath()).thenReturn("/app1");
		bar.setPageContext(mockPageContext);
		bar.setNearPageRange("4");
		bar.setUrl("/module/url11111.do");

		PageModel pm = new PageModel();
		pm.setCurrPageNoOnRequest("10");
		pm.setPageSize(10);
		pm.setTotalRecordsNumber(1000);
		pm.setPageTo("11");
		pm.setOrderBy("order1");
		pm.setOrderDirection("asc");
		String html = bar.render(pm);
		System.out.println(html);
		// Assert.assertTrue(html.startsWith(jsResult));
		// Assert.assertTrue("" + html.indexOf(jsResult), html.indexOf(jsResult)
		// == 0);
		String formAction = "action=\"/app1/module/url11111.do?p_orderBy=order1&p_orderDir=asc\"";
		Assert.assertTrue(html.indexOf(formAction) > 0);
		Assert.assertTrue(html.indexOf("&p_toPageNo=7\">7</a>") > 0);
		Assert.assertTrue(html.indexOf("&p_toPageNo=8\">8</a>") > 0);
		Assert.assertTrue(html.indexOf("&p_toPageNo=9\">9") > 0);
		Assert.assertTrue(html.indexOf("&p_toPageNo=10\">10</a>") > 0);
		Assert.assertTrue(html.indexOf("<span class=\"pageBarCurrPage\">11</span>") > 0);
		Assert.assertTrue(html.indexOf("&p_toPageNo=12\">12</a>") > 0);
		Assert.assertTrue(html.indexOf("&p_toPageNo=13\">13</a>") > 0);
		Assert.assertTrue(html.indexOf("&p_toPageNo=14\">14</a>") > 0);
		Assert.assertTrue(html.indexOf("&p_toPageNo=15\">15</a>") > 0);
		Assert.assertTrue(html.indexOf("总记录数:1,000") > 0);
		Assert.assertTrue(html.indexOf("总页数:100") > 0);
	}
}