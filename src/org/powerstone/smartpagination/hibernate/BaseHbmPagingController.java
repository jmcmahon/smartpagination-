package org.powerstone.smartpagination.hibernate;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.powerstone.smartpagination.common.BasePagingController;
import org.powerstone.smartpagination.common.PageInfo;
import org.powerstone.smartpagination.common.PageResult;

abstract public class BaseHbmPagingController extends
		BasePagingController<DetachedCriteria, Order> {
	private BaseHibernateDao baseHibernateDao;

	@Override
	public PageResult findByPage(PageInfo<DetachedCriteria, Order> pageInfo) {
		log.debug(pageInfo);
		HbmPageInfo pi = (HbmPageInfo) pageInfo;
		PageResult pr = baseHibernateDao.findByPage(pi);
		return pr;
	}

	public BaseHbmPagingController() {
		super();
	}

	public BaseHbmPagingController(BaseHibernateDao baseHibernateDao) {
		super();
		this.baseHibernateDao = baseHibernateDao;
	}
}