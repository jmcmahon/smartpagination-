package org.powerstone.smartpagination.common;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

abstract public class BasePagingController<T_Criterial, T_OrderBy> {
	protected Logger log = Logger.getLogger(getClass());

	public final static String DEFAULT_PAGE_MODEL_NAME = "org.powerstone.smartpagination.common.PageModel";

	public final static String DEFAULT_PAGE_DATA_NAME = "org.powerstone.smartpagination.common.PageData";

	public final static String QUERY_MAP_IN_SESSION = "p_QueryMapInSession";

	public final static String TO_PAGE_NO_PARAM = "p_toPageNo";

	public final static String TO_FIRST_PARAM = "p_toFirst";

	public final static String TO_END_PARAM = "p_toEnd";

	public final static String TO_NEXT_PARAM = "p_toNext";

	public final static String TO_LAST_PARAM = "p_toLast";

	public final static String CURR_PAGE_PARAM = "p_currPage";

	public final static String PAGE_SIZE_PARAM = "p_pageSize";

	public final static String ORDER_BY_PARAM = "p_orderBy";

	public final static String ORDER_DIR_PARAM = "p_orderDir";

	private int defaultPageSize = 10;

	public void setDefaultPageSize(int thePageSize) {
		this.defaultPageSize = thePageSize;
	}

	/**
	 * 分页控制器入口
	 */
	public final void handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		PageModel pm = new PageModel();
		request.setAttribute(DEFAULT_PAGE_MODEL_NAME, pm);

		pm.setPageTo(request.getParameter(TO_PAGE_NO_PARAM) != null ? (request
				.getParameter(TO_PAGE_NO_PARAM).trim()) : null);
		pm.setBeFirst(request.getParameter(TO_FIRST_PARAM) != null);
		pm.setBeEnd(request.getParameter(TO_END_PARAM) != null);
		pm.setBeNext(request.getParameter(TO_NEXT_PARAM) != null);
		pm.setBeLast(request.getParameter(TO_LAST_PARAM) != null);
		pm.setCurrPageNoOnRequest(request.getParameter(CURR_PAGE_PARAM));
		pm.setOrderBy(request.getParameter(ORDER_BY_PARAM));
		pm.setOrderDirection(request.getParameter(ORDER_DIR_PARAM));

		String pageSizeParam = request.getParameter(PAGE_SIZE_PARAM);
		int pageSize = (pageSizeParam != null) ? new Integer(pageSizeParam)
				: defaultPageSize;
		if (pageSize > 0) {
			pm.setPageSize(pageSize);
		} else {
			pm.setPageSize(defaultPageSize);
		}

		PageInfo<T_Criterial, T_OrderBy> pi = makePageInfo(request);
		if (pm.isBeFirst()) {
			pi.setEnd(true);
		} else if (pm.isBeEnd()) {
			pi.setEnd(false);
		}
		pi.setPageNo(pm.computeNewPageNoForQuery());
		pi.setPageSize(pm.getPageSize());

		if (pm.getOrderBy() != null && pm.getOrderBy().trim().length() > 0) {
			if (pm.isOrderAsc()) {
				pi.addOrderByAsc(pm.getOrderBy());
			} else {
				pi.addOrderByDesc(pm.getOrderBy());
			}
		}

		PageResult pageResult = findByPage(pi);

		pm.setTotalRecordsNumber(pageResult.getTotalRecordsNumber());

		request.setAttribute(DEFAULT_PAGE_DATA_NAME, pageResult.getPageData());

		log.debug("PageModel:" + pm);
	}

	@SuppressWarnings("unchecked")
	public static List getPageData(HttpServletRequest request) {
		return (List) request.getAttribute(DEFAULT_PAGE_DATA_NAME);
	}

	/**
	 * 由子类重写，构造PageInfo(entityClass,expression,orderBy)
	 */
	abstract protected PageInfo<T_Criterial, T_OrderBy> makePageInfo(
			HttpServletRequest request);

	/**
	 * 由子类重写，调用service查询
	 */
	abstract public PageResult findByPage(PageInfo<T_Criterial, T_OrderBy> pageInfo);

}