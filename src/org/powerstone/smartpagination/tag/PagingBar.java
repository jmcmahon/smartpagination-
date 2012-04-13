package org.powerstone.smartpagination.tag;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;
import org.powerstone.smartpagination.common.BasePagingController;
import org.powerstone.smartpagination.common.PageModel;

public class PagingBar extends TagSupport {
	private static final long serialVersionUID = 6670041132688723682L;

	protected final Logger log = Logger.getLogger(this.getClass());

	private String url;

	private String nearPageRange = "3";

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setNearPageRange(String nearPageRange) {
		this.nearPageRange = nearPageRange;
	}

	@Override
	public int doStartTag() throws JspException {
		return TagSupport.SKIP_BODY;
	}

	public static int computeRowNo(HttpServletRequest request) {
		PageModel pm = (PageModel) request
				.getAttribute(BasePagingController.DEFAULT_PAGE_MODEL_NAME);
		if (pm == null) {
			return 0;
		} else {
			return pm.computeRecordsBeginNo() + 1;
		}
	}

	@Override
	public int doEndTag() throws JspException {
		// get the paging model from reuest context
		PageModel pm = (PageModel) pageContext.getRequest().getAttribute(
				BasePagingController.DEFAULT_PAGE_MODEL_NAME);
		if (pm == null) {
			log.warn("There is no PageModel in request:"
					+ ((HttpServletRequest) pageContext.getRequest()).getRequestURI());
			pm = new PageModel();
		}
		// write out
		try {
			String html = render(pm);
			pageContext.getOut().println(html);
			log.debug(html);
		} catch (IOException ioe) {
			throw new JspException(ioe);
		}
		return EVAL_PAGE;
	}

	protected String render(PageModel pm) {
		String fullUrl = generateBaseUrl(pm);
		if (!fullUrl.endsWith("?")) {
			fullUrl += "&";
		}
		fullUrl += BasePagingController.PAGE_SIZE_PARAM + "=" + pm.getPageSize();
		int newPageNo = pm.computeDestinationPageNo();
		int totalPages = pm.computePageCount();

		HashMap<String, Object> mapToRend = new HashMap<String, Object>();
		mapToRend.put("paging_formActionUrl", generateBaseUrl(pm));
		mapToRend.put("paging_toPageNoParam", BasePagingController.TO_PAGE_NO_PARAM);
		mapToRend.put("paging_pageSizeParam", BasePagingController.PAGE_SIZE_PARAM);
		mapToRend.put("paging_totalPages", totalPages);

		// link to first page
		if (totalPages > 0) {
			mapToRend.put("paging_firstUrl", fullUrl + "&"
					+ BasePagingController.TO_FIRST_PARAM + "=true");
		}

		// link to near pages
		List<Map<String, String>> nearPageList = new ArrayList<Map<String, String>>();
		for (int i = 0; i < (Integer.parseInt(nearPageRange) * 2 + 1); i++) {
			int index = newPageNo - Integer.parseInt(nearPageRange) + i;
			if (index > 0 && index <= totalPages) {
				HashMap<String, String> nearPageInfo = new HashMap<String, String>();
				nearPageInfo.put("nearPageNo", "" + index);
				if (index != newPageNo) {
					nearPageInfo.put("nearPageUrl", fullUrl + "&"
							+ BasePagingController.TO_PAGE_NO_PARAM + "=" + index);
				}
				nearPageList.add(nearPageInfo);
			}
		}
		mapToRend.put("paging_nearPageList", nearPageList);
		// link to end page
		if (totalPages > 0) {
			mapToRend.put("paging_tailUrl", fullUrl + "&"
					+ BasePagingController.TO_END_PARAM + "=true");
		}
		mapToRend.put("paging_totalRecord", pm.getTotalRecordsNumber());
		mapToRend.put("paging_size", pm.getPageSize());
		mapToRend.put("paging_toPageNo", newPageNo);
		return FreemarkerUtil.rendPageBar(mapToRend);
	}

	// return /app/url?orderBy=xx&orderDir=xxx
	private String generateBaseUrl(PageModel pm) {
		String formUrl = ((HttpServletRequest) pageContext.getRequest()).getContextPath()
				+ url;
		if (formUrl.indexOf("?") < 0) {
			formUrl += "?";
		}
		if (pm.getOrderBy() != null) {
			formUrl += BasePagingController.ORDER_BY_PARAM + "=" + pm.getOrderBy() + "&"
					+ BasePagingController.ORDER_DIR_PARAM + "=" + pm.getOrderDirection();
		}
		return formUrl;
	}
}