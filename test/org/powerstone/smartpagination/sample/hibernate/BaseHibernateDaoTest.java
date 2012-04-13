package org.powerstone.smartpagination.sample.hibernate;

import java.util.Date;

import junit.framework.Assert;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.junit.Before;
import org.junit.Test;
import org.powerstone.smartpagination.common.PageResult;
import org.powerstone.smartpagination.hibernate.BaseHibernateDao;
import org.powerstone.smartpagination.hibernate.HbmPageInfo;
import org.powerstone.smartpagination.hibernate.UserModelHibernateQuery;
import org.powerstone.smartpagination.sample.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

@ContextConfiguration(locations = { "classpath:spring-common.xml" })
public class BaseHibernateDaoTest extends AbstractTransactionalJUnit4SpringContextTests {
	@Autowired
	private BaseHibernateDao baseHibernateDao;

	private UserModel user;

	@Before
	public void onSetUpInTransaction() throws Exception {
		logger.debug(super.applicationContext.getBean("baseHibernateDao"));
		for (int i = 0; i < 17; i++) {
			user = new UserModel();
			user.setBirth(new Date());
			user.setEmail(i + "liyingquan@gmail.com");
			user.setRealName(i + "liyingquan");
			user.setSex("m");
			user.setUserName(i + "admin");
			baseHibernateDao.saveOrUpdate(user);
		}
		baseHibernateDao.flush();
	}

	@Test
	public void testFindByPage() {
		HbmPageInfo pi = new HbmPageInfo();
		pi.setCountDistinctProjections("id");
		pi.setExpression(DetachedCriteria.forClass(UserModel.class));
		pi.addOrderByAsc("email");
		pi.setPageNo(2);
		pi.setPageSize(10);
		PageResult pageResult = baseHibernateDao.findByPage(pi);
		Assert.assertEquals(7, pageResult.getPageData().size());
		Assert.assertEquals(2, pageResult.getPageAmount());
		Assert.assertEquals(17, pageResult.getTotalRecordsNumber());
	}

	@Test
	public void testFindByPage_WithPageQuery() {
		UserModelHibernateQuery umq = new UserModelHibernateQuery();
		umq.setEmail("liyingquan@gmail.com");
		umq.setRealName("liyingquan");
		umq.setSex("m");
		umq.setUserName("admin");
		umq.setUserNameLike(true);
		HbmPageInfo pi = (HbmPageInfo) umq.generatePageInfo();
		pi.setPageNo(2);
		pi.setPageSize(10);
		PageResult pageResult = baseHibernateDao.findByPage(pi);
		Assert.assertEquals(7, pageResult.getPageData().size());
		Assert.assertEquals(2, pageResult.getPageAmount());
		Assert.assertEquals(17, pageResult.getTotalRecordsNumber());
	}

	@Test
	public void testFindByPage_NoResult() {
		HbmPageInfo pi = new HbmPageInfo();
		pi.setCountDistinctProjections("id");
		pi.setExpression(DetachedCriteria.forClass(UserModel.class).add(
				Restrictions.eq("email", "xxxxcx")));
		pi.addOrderByAsc("id");
		pi.setPageNo(2);
		pi.setPageSize(10);
		PageResult pageResult = baseHibernateDao.findByPage(pi);
		Assert.assertEquals(0, pageResult.getPageData().size());
		Assert.assertEquals(0, pageResult.getPageAmount());
		Assert.assertEquals(0, pageResult.getTotalRecordsNumber());
	}

	@Test
	public void testCountRecordsNumber() {
		Assert.assertEquals(17, baseHibernateDao.countRecordsNumber(DetachedCriteria
				.forClass(UserModel.class), "id"));
	}

	@Test
	public void testDelete() {
		baseHibernateDao.delete(UserModel.class, user.getId());
		Assert.assertEquals(16, baseHibernateDao.countRecordsNumber(DetachedCriteria
				.forClass(UserModel.class), "id"));
	}

	@Test
	public void testFindByCriteria() {
		Assert.assertEquals(17, baseHibernateDao.findByCriteria(
				DetachedCriteria.forClass(UserModel.class)).size());
	}

	@Test
	public void testSaveOrUpdate() {
		Assert.assertNotNull(user.getId());
	}
}