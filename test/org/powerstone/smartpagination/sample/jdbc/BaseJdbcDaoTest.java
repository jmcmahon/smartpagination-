package org.powerstone.smartpagination.sample.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.powerstone.smartpagination.common.PageResult;
import org.powerstone.smartpagination.hibernate.BaseHibernateDao;
import org.powerstone.smartpagination.jdbc.BaseJdbcDao;
import org.powerstone.smartpagination.jdbc.JdbcPageInfo;
import org.powerstone.smartpagination.jdbc.UserModelJdbcQuery;
import org.powerstone.smartpagination.sample.UserModel;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

@ContextConfiguration(locations = { "classpath:spring-common.xml" })
public class BaseJdbcDaoTest extends AbstractTransactionalJUnit4SpringContextTests {
	Logger log = Logger.getLogger(getClass());
	@Resource
	private BaseHibernateDao baseHibernateDao;
	@Resource
	private BaseJdbcDao baseJdbcDao;

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

	@SuppressWarnings("unchecked")
	@Test
	public void testFindByPage() {
		JdbcPageInfo pi = new JdbcPageInfo();
		pi.putSql("select * from user_model where lower(user_name) like :userName "
				+ "and lower(email) like :email");
		pi.putRowMapper(new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				UserModel u = new UserModel();
				u.setBirth(rs.getDate("birth"));
				u.setEmail(rs.getString("email"));
				u.setId(rs.getLong("id"));
				u.setRealName(rs.getString("real_Name"));
				u.setSex(rs.getString("sex"));
				u.setUserName(rs.getString("user_Name"));
				return u;
			}
		});

		UserModel exampleModel = new UserModel();
		exampleModel.setEmail("%liyingquan@gmail.com%");
		exampleModel.setUserName("%admin%".toLowerCase());
		pi.putExampleModel(exampleModel);
		pi.addOrderByAsc("email");
		pi.addOrderByDesc("user_Name");
		pi.setPageNo(1);
		pi.setPageSize(10);

		PageResult pageResult = baseJdbcDao.findByPage(pi);
		Assert.assertEquals("第1页", 10, pageResult.getPageData().size());
		Assert.assertEquals("页数", 2, pageResult.getPageAmount());
		Assert.assertEquals("总记录数", 17, pageResult.getTotalRecordsNumber());
		Assert.assertEquals("first result", "0liyingquan@gmail.com",
				((UserModel) pageResult.getPageData().get(0)).getEmail());

		pi.setOrderByList(new ArrayList());
		pi.addOrderByDesc("email");// OrderBy EXCEPTIONCODE desc
		pageResult = baseJdbcDao.findByPage(pi);
		Assert.assertEquals("第1页", 10, pageResult.getPageData().size());
		Assert.assertEquals("页数", 2, pageResult.getPageAmount());
		Assert.assertEquals("总记录数", 17, pageResult.getTotalRecordsNumber());
		Assert.assertEquals("first user", "9liyingquan@gmail.com",
				((UserModel) pageResult.getPageData().get(0)).getEmail());

		pi.setPageNo(2);
		pi.setPageSize(10);
		pageResult = baseJdbcDao.findByPage(pi);
		Assert.assertEquals("第2页", 7, pageResult.getPageData().size());
		Assert.assertEquals("页数", 2, pageResult.getPageAmount());
		Assert.assertEquals("总记录数", 17, pageResult.getTotalRecordsNumber());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testFindByPage_BlankExample() {
		JdbcPageInfo pi = new JdbcPageInfo();
		pi.putSql("select * from User_Model");
		pi.putRowMapper(new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				UserModel u = new UserModel();
				u.setBirth(rs.getDate("birth"));
				u.setEmail(rs.getString("email"));
				u.setId(rs.getLong("id"));
				u.setRealName(rs.getString("real_Name"));
				u.setSex(rs.getString("sex"));
				u.setUserName(rs.getString("user_Name"));
				return u;
			}
		});
		pi.putExampleModel(new UserModel());
		pi.addOrderByAsc("email");
		pi.addOrderByDesc("user_Name");
		pi.setPageNo(1);
		pi.setPageSize(10);

		PageResult pageResult = baseJdbcDao.findByPage(pi);
		Assert.assertEquals("第1页", 10, pageResult.getPageData().size());
		Assert.assertEquals("页数", 2, pageResult.getPageAmount());
		Assert.assertEquals("总记录数", 17, pageResult.getTotalRecordsNumber());
		Assert.assertEquals("first user", "0liyingquan@gmail.com",
				((UserModel) pageResult.getPageData().get(0)).getEmail());

		pi.setOrderByList(new ArrayList());
		pi.addOrderByDesc("email");// OrderBy EXCEPTIONCODE desc
		pageResult = baseJdbcDao.findByPage(pi);
		Assert.assertEquals("第1页", 10, pageResult.getPageData().size());
		Assert.assertEquals("页数", 2, pageResult.getPageAmount());
		Assert.assertEquals("总记录数", 17, pageResult.getTotalRecordsNumber());
		Assert.assertEquals("first user", "9liyingquan@gmail.com",
				((UserModel) pageResult.getPageData().get(0)).getEmail());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testFindByPageQuery() {
		UserModelJdbcQuery pq = new UserModelJdbcQuery();

		pq.setUserName("liyingquan@gmail.com");
		pq.setUserName("admin");

		pq.setUserNameLike(true);
		JdbcPageInfo pi = (JdbcPageInfo) pq.generatePageInfo();
		pi.addOrderByAsc("email");
		pi.addOrderByDesc("user_Name");
		pi.setPageNo(1);
		pi.setPageSize(10);

		PageResult pageResult = baseJdbcDao.findByPage(pi);
		Assert.assertEquals("第1页", 10, pageResult.getPageData().size());
		Assert.assertEquals("页数", 2, pageResult.getPageAmount());
		Assert.assertEquals("总记录数", 17, pageResult.getTotalRecordsNumber());
		Assert.assertEquals("first user", "0liyingquan@gmail.com",
				((UserModel) pageResult.getPageData().get(0)).getEmail());

		pi.setOrderByList(new ArrayList());
		pi.addOrderByDesc("email");// OrderBy email desc
		pageResult = baseJdbcDao.findByPage(pi);
		Assert.assertEquals("第1页", 10, pageResult.getPageData().size());
		Assert.assertEquals("页数", 2, pageResult.getPageAmount());
		Assert.assertEquals("总记录数", 17, pageResult.getTotalRecordsNumber());
		Assert.assertEquals("first exception code", "9liyingquan@gmail.com",
				((UserModel) pageResult.getPageData().get(0)).getEmail());
	}

	@Test
	public void testFindByPageQuery_LessThan1Page() {
		UserModelJdbcQuery pq = new UserModelJdbcQuery();

		pq.setUserName("liyingquan@gmail.com");
		pq.setUserName("admin");
		pq.setUserNameLike(true);

		JdbcPageInfo pi = (JdbcPageInfo) pq.generatePageInfo();
		pi.addOrderByAsc("email");
		pi.setPageNo(1);
		pi.setPageSize(50);

		PageResult pageResult = baseJdbcDao.findByPage(pi);
		Assert.assertEquals("第1页", 17, pageResult.getPageData().size());
		Assert.assertEquals("页数", 1, pageResult.getPageAmount());
		Assert.assertEquals("总记录数", 17, pageResult.getTotalRecordsNumber());
	}
}