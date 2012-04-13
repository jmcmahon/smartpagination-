package org.powerstone.smartpagination.hibernate;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.powerstone.smartpagination.common.PageInfo;

public class HbmPageInfo extends PageInfo<DetachedCriteria, Order> {
	String countDistinctProjections;

	public String getCountDistinctProjections() {
		return countDistinctProjections;
	}

	public void setCountDistinctProjections(String countDistinctProjections) {
		this.countDistinctProjections = countDistinctProjections;
	}

	@Override
	public void addOrderByAsc(String orderBy) {
		super.getOrderByList().add(Order.asc(orderBy));
	}

	@Override
	public void addOrderByDesc(String orderBy) {
		super.getOrderByList().add(Order.desc(orderBy));
	}

}
