package org.powerstone.smartpagination.common;

import java.util.ArrayList;
import java.util.List;

abstract public class PageInfo<T_Criterial, T_OrderBy> {
	protected Class<?> entityClass;
	protected T_Criterial expression;
	protected List<T_OrderBy> orderByList = new ArrayList<T_OrderBy>();
	protected int pageSize;// 0相当于取全部
	protected int pageNo;// 如果end参数不为空，忽略pageNo
	protected Boolean end;// end:true->首页;false->尾页

	public PageInfo(Class<?> entityClass, T_Criterial expression,
			List<T_OrderBy> orderByList, int pageNo, int pageSize, Boolean end) {
		this.entityClass = entityClass;
		this.expression = expression;
		this.orderByList = orderByList;
		this.pageNo = pageNo;
		this.pageSize = pageSize;
		this.end = end;
	}

	public PageInfo(Class<?> entityClass, T_Criterial expression,
			List<T_OrderBy> orderBy, int pageNo, int pageSize) {
		this.entityClass = entityClass;
		this.expression = expression;
		this.orderByList = orderBy;
		this.pageNo = pageNo;
		this.pageSize = pageSize;
		this.end = null;
	}

	public PageInfo() {
	}

	public Class<?> getEntityClass() {
		return entityClass;
	}

	public void setEntityClass(Class<?> entityClass) {
		this.entityClass = entityClass;
	}

	public T_Criterial getExpression() {
		return expression;
	}

	public void setExpression(T_Criterial expression) {
		this.expression = expression;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public Boolean getEnd() {
		return end;
	}

	/**
	 * 
	 * @param end
	 *            :true->首页;false->尾页
	 */
	public void setEnd(Boolean end) {
		this.end = end;
	}

	public List<T_OrderBy> getOrderByList() {
		return orderByList;
	}

	public void setOrderByList(List<T_OrderBy> orderBy) {
		this.orderByList = orderBy;
	}

	abstract public void addOrderByAsc(String orderBy);

	abstract public void addOrderByDesc(String orderBy);
}