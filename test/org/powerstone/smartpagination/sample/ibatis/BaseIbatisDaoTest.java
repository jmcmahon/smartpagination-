package org.powerstone.smartpagination.sample.ibatis;

import java.util.ArrayList;
import java.util.Date;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.powerstone.smartpagination.common.PageResult;
import org.powerstone.smartpagination.hibernate.BaseHibernateDao;
import org.powerstone.smartpagination.ibatis.BaseIbatisDao;
import org.powerstone.smartpagination.ibatis.IbatisPageInfo;
import org.powerstone.smartpagination.ibatis.UserModelIbatisQuery;
import org.powerstone.smartpagination.sample.UserModel;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

@ContextConfiguration(locations = { "classpath:spring-common.xml" })
public class BaseIbatisDaoTest extends AbstractTransactionalJUnit4SpringContextTests {
	Logger log = Logger.getLogger(getClass());
	@Resource
	private BaseHibernateDao baseHibernateDao;
	@Resource
	private BaseIbatisDao baseIbatisDao;

	private UserModel user;

	@Before
	public void onSetUpInTransaction() throws Exception {
		logger.debug(super.applicationContext.getBean("baseHibernateDao"));
		for (int i = 0; i < 256; i++) {
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

	@SuppressWarnings("unchecked")
	@Test
	public void testFindByPage() {
		UserModelIbatisQuery query = new UserModelIbatisQuery();

		query.setEmail("liyingquan@gmail.com");
		query.setUserName("admin");
		query.setUserNameLike(true);

		IbatisPageInfo pi = (IbatisPageInfo) query.generatePageInfo();
		pi.addOrderByAsc("email");
		pi.addOrderByDesc("user_Name");
		pi.setPageNo(1);
		pi.setPageSize(10);

		PageResult pageResult = baseIbatisDao.findByPage(pi);
		Assert.assertEquals("第1页", 10, pageResult.getPageData().size());
		Assert.assertEquals("页数", 26, pageResult.getPageAmount());
		Assert.assertEquals("总记录数", 256, pageResult.getTotalRecordsNumber());
		Assert.assertEquals("first result", "0liyingquan@gmail.com",
				((UserModel) pageResult.getPageData().get(0)).getEmail());

		pi.setOrderByList(new ArrayList());
		pi.addOrderByDesc("email");// OrderBy email desc
		pageResult = baseIbatisDao.findByPage(pi);
		Assert.assertEquals("第1页", 10, pageResult.getPageData().size());
		Assert.assertEquals("页数", 26, pageResult.getPageAmount());
		Assert.assertEquals("总记录数", 256, pageResult.getTotalRecordsNumber());
		Assert.assertEquals("first user", "9liyingquan@gmail.com",
				((UserModel) pageResult.getPageData().get(0)).getEmail());

		pi.setPageNo(5);
		pi.setPageSize(10);
		pageResult = baseIbatisDao.findByPage(pi);
		Assert.assertEquals("第5页", 10, pageResult.getPageData().size());
		Assert.assertEquals("页数", 26, pageResult.getPageAmount());
		Assert.assertEquals("总记录数", 256, pageResult.getTotalRecordsNumber());
	}

	@Test
	public void testFindByPage_NoOrderBy() {
		UserModelIbatisQuery query = new UserModelIbatisQuery();
		IbatisPageInfo pi = (IbatisPageInfo) query.generatePageInfo();
		pi.setPageNo(2);
		pi.setPageSize(10);

		PageResult pageResult = baseIbatisDao.findByPage(pi);
		Assert.assertEquals("第2页", 10, pageResult.getPageData().size());
		Assert.assertEquals("页数", 26, pageResult.getPageAmount());
		Assert.assertEquals("总记录数", 256, pageResult.getTotalRecordsNumber());
	}

	@Test
	public void testFindByPageQuery_LessThan1Page() {
		UserModelIbatisQuery query = new UserModelIbatisQuery();
		query.setUserName("liyingquan@gmail.com");
		query.setUserName("admin");
		query.setUserNameLike(true);

		IbatisPageInfo pi = (IbatisPageInfo) query.generatePageInfo();
		pi.addOrderByAsc("email");
		pi.setPageNo(100);
		pi.setPageSize(50);

		PageResult pageResult = baseIbatisDao.findByPage(pi);
		Assert.assertEquals("第100页", 6, pageResult.getPageData().size());
		Assert.assertEquals("页数", 6, pageResult.getPageAmount());
		Assert.assertEquals("总记录数", 256, pageResult.getTotalRecordsNumber());
	}
}