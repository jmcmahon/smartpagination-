package org.powerstone.smartpagination.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

@SuppressWarnings("deprecation")
public abstract class BaseQueryFormPagingController<T_Criterial, T_OrderBy> extends
		SimpleFormController {
	/**
	 * 由具体子类改写，ModelAndView名称
	 */
	protected String pagingViewName;

	/**
	 * 由具体子类改写，存放在Response中的查询结果的名称
	 */
	protected String pagingDataName;

	public BaseQueryFormPagingController() {
		super();
		super.setSessionForm(true);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		PageQuery<T_Criterial, T_OrderBy> bq = (PageQuery<T_Criterial, T_OrderBy>) command;

		String commandName = this.getFormSessionAttributeName(request);
		request.setAttribute(commandName, command);
		queryList(request, response, bq);
		return this.showForm(request, response, errors);
	}

	/**
	 * 由具体子类实现，根据Criteria进行数据查询
	 */
	protected abstract PageResult findByPageInfo(PageInfo<T_Criterial, T_OrderBy> pi);

	/**
	 * 由具体子类实现，实例化一个具体的Criteria
	 */
	protected abstract PageQuery<T_Criterial, T_OrderBy> makePageQuery();

	@Override
	@SuppressWarnings("unchecked")
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		try {
			Object command = this.getCommand(request);
			queryList(request, null, (PageQuery<T_Criterial, T_OrderBy>) command);
			return command;
		} catch (Exception e) {
			logger.debug(e.getMessage(), e);
		}

		PageQuery<T_Criterial, T_OrderBy> pageQuery = makePageQuery();
		queryList(request, null, pageQuery);
		return pageQuery;
	}

	private ModelAndView queryList(HttpServletRequest request,
			HttpServletResponse response,
			final PageQuery<T_Criterial, T_OrderBy> pageQuery) throws Exception {

		BasePagingController<T_Criterial, T_OrderBy> ctrl = new BasePagingController<T_Criterial, T_OrderBy>() {
			@Override
			public PageResult findByPage(PageInfo<T_Criterial, T_OrderBy> pageInfo) {
				return findByPageInfo(pageInfo);
			}

			@Override
			protected PageInfo<T_Criterial, T_OrderBy> makePageInfo(
					HttpServletRequest request) {
				return pageQuery.generatePageInfo();
			}
		};

		ctrl.handleRequest(request, response);
		request.setAttribute(pagingDataName, BasePagingController.getPageData(request));

		return new ModelAndView(pagingViewName);
	}

	public String getPagingViewName() {
		return pagingViewName;
	}

	public void setPagingViewName(String pagingViewName) {
		this.pagingViewName = pagingViewName;
	}

	public String getPagingDataName() {
		return pagingDataName;
	}

	public void setPagingDataName(String pagingDataName) {
		this.pagingDataName = pagingDataName;
	}
}
