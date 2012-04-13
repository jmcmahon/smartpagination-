package org.powerstone.smartpagination.ibatis;

import org.powerstone.smartpagination.common.PageInfo;

public class IbatisPageInfo extends PageInfo<IbatisPagerable, String> {
	String countQueryName;
	String pageQueryName;

	@Override
	public void addOrderByAsc(String orderBy) {
		super.getOrderByList().add(orderBy + " asc");
	}

	@Override
	public void addOrderByDesc(String orderBy) {
		super.getOrderByList().add(orderBy + " desc");
	}

	public String getCountQueryName() {
		return countQueryName;
	}

	public void setCountQueryName(String countQueryName) {
		this.countQueryName = countQueryName;
	}

	public String getPageQueryName() {
		return pageQueryName;
	}

	public void setPageQueryName(String pageQueryName) {
		this.pageQueryName = pageQueryName;
	}
}