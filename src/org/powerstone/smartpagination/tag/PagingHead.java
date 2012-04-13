package org.powerstone.smartpagination.tag;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;
import org.powerstone.smartpagination.common.BasePagingController;
import org.powerstone.smartpagination.common.PageModel;

public class PagingHead extends TagSupport {
	private static final long serialVersionUID = -887745989108474554L;

	private static Logger log = Logger.getLogger(PagingHead.class);

	private String url;

	private String orderBy;

	String html;

	@Override
	public int doStartTag() throws JspException {
		PageModel pm = (PageModel) pageContext.getRequest().getAttribute(
				BasePagingController.DEFAULT_PAGE_MODEL_NAME);
		if (pm == null) {
			log.warn("There is no PageModel in request:"
					+ ((HttpServletRequest) pageContext.getRequest()).getRequestURI());
			pm = new PageModel();
		}
		// write out
		try {
			String contextPath = ((HttpServletRequest) pageContext.getRequest())
					.getContextPath();
			String fullUrl = genUrl(pm, contextPath);
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("paging_fullUrl", fullUrl);
			if (orderBy.equals(pm.getOrderBy())) {
				map.put("paging_orderDir", ""
						+ PageModel.ORDER_ASC.equals(pm.getOrderDirection()));
			}
			html = FreemarkerUtil.rendPageHead(map);
			pageContext.getOut().println(html.substring(0, html.indexOf(" </td>")));
		} catch (IOException ioe) {
			throw new JspException(ioe);
		}
		return TagSupport.EVAL_BODY_INCLUDE;
	}

	@Override
	public int doEndTag() throws JspException {
		PageModel pm = (PageModel) pageContext.getRequest().getAttribute(
				BasePagingController.DEFAULT_PAGE_MODEL_NAME);
		if (pm == null) {
			log.warn("There is no PageModel in request:"
					+ ((HttpServletRequest) pageContext.getRequest()).getRequestURI());
			pm = new PageModel();
		}
		// write out
		try {
			pageContext.getOut().println(html.substring(html.indexOf(" </td>")));
		} catch (IOException ioe) {
			throw new JspException(ioe);
		}

		return EVAL_PAGE;
	}

	protected String genUrl(PageModel pm, String contextPath) {
		if (url.indexOf("?") < 0) {
			url += "?";
		}
		String currDir = PageModel.ORDER_ASC;
		if (orderBy.equals(pm.getOrderBy())) {
			currDir = (PageModel.ORDER_ASC.equals(pm.getOrderDirection())) ? PageModel.ORDER_DESC
					: PageModel.ORDER_ASC;
		}
		String fullUrl = contextPath + url + BasePagingController.PAGE_SIZE_PARAM + "="
				+ pm.getPageSize() + "&" + BasePagingController.TO_PAGE_NO_PARAM + "="
				+ pm.computeDestinationPageNo() + "&"
				+ BasePagingController.ORDER_BY_PARAM + "=" + orderBy + "&"
				+ BasePagingController.ORDER_DIR_PARAM + "=" + currDir;
		log.debug(fullUrl);
		return fullUrl;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}