package org.powerstone.smartpagination.sample;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.powerstone.smartpagination.common.BasePagingController;
import org.powerstone.smartpagination.common.BaseQueryFormPagingController;
import org.powerstone.smartpagination.common.PageInfo;
import org.powerstone.smartpagination.common.PageQuery;
import org.powerstone.smartpagination.common.PageResult;
import org.powerstone.smartpagination.hibernate.BaseHbmPagingController;
import org.powerstone.smartpagination.hibernate.BaseHibernateDao;
import org.powerstone.smartpagination.hibernate.BaseHibernateQueryFormPagingController;
import org.powerstone.smartpagination.hibernate.HbmPageInfo;
import org.powerstone.smartpagination.hibernate.UserModelHibernateQuery;
import org.powerstone.smartpagination.ibatis.BaseIbatisDao;
import org.powerstone.smartpagination.ibatis.IbatisPageInfo;
import org.powerstone.smartpagination.ibatis.IbatisPagerable;
import org.powerstone.smartpagination.ibatis.UserModelIbatisQuery;
import org.powerstone.smartpagination.jdbc.BaseJdbcDao;
import org.powerstone.smartpagination.jdbc.JdbcPageInfo;
import org.powerstone.smartpagination.jdbc.UserModelJdbcQuery;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@SuppressWarnings( { "unchecked", "deprecation" })
@Controller
@RequestMapping("/sample")
public class SamplePagingController {
	Logger logger = Logger.getLogger(SamplePagingController.class);
	@Resource
	private BaseHibernateDao baseHibernateDao;
	@Resource
	private BaseJdbcDao baseJdbcDao;
	@Resource
	private BaseIbatisDao baseIbatisDao;

	@RequestMapping("/listHibernate")
	public ModelAndView listHibernate(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		BaseHbmPagingController ctrl = new BaseHbmPagingController(baseHibernateDao) {
			@Override
			protected PageInfo<DetachedCriteria, Order> makePageInfo(
					HttpServletRequest request) {
				HbmPageInfo pi = new HbmPageInfo();
				pi.setCountDistinctProjections("id");
				pi.setExpression(DetachedCriteria.forClass(UserModel.class));
				// pi.addOrderByAsc("id");
				return pi;
			}
		};
		ctrl.handleRequest(request, response);
		return new ModelAndView("userList", "userList", BaseHbmPagingController
				.getPageData(request));
	}

	@RequestMapping("/queryHibernate")
	public ModelAndView queryHibernate(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		BaseHibernateQueryFormPagingController ctrl = new BaseHibernateQueryFormPagingController() {
			@Override
			protected PageResult findByPageInfo(PageInfo<DetachedCriteria, Order> pi) {
				return baseHibernateDao.findByPage((HbmPageInfo) pi);
			}

			@Override
			protected PageQuery<DetachedCriteria, Order> makePageQuery() {
				return new UserModelHibernateQuery();
			}
		};

		ctrl.setCommandClass(UserModel.class);
		ctrl.setCommandName("userModel");
		ctrl.setFormView("userModelQuery");
		ctrl.setSuccessView("redirect:/query.htm");

		ctrl.setPagingDataName("userList");

		return ctrl.handleRequest(request, response);
	}

	@RequestMapping(value = "/queryHibernateAjax", method = RequestMethod.GET)
	public ModelAndView queryHibernateAjaxForm(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return new ModelAndView("queryHibernateAjaxForm");
	}

	@RequestMapping(value = "/queryHibernateAjax", method = RequestMethod.POST)
	public ModelAndView queryHibernateAjax(
			final UserModelHibernateQuery userModelHibernateQuery,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.debug(userModelHibernateQuery);
		BaseHbmPagingController ctrl = new BaseHbmPagingController(baseHibernateDao) {
			@Override
			protected PageInfo<DetachedCriteria, Order> makePageInfo(
					HttpServletRequest request) {
				return userModelHibernateQuery.generatePageInfo();
			}
		};
		ctrl.handleRequest(request, response);
		logger.debug(BaseHbmPagingController.getPageData(request));
		request.setAttribute("userList", BaseHbmPagingController.getPageData(request));
		return new ModelAndView("queryHibernateAjaxResult");
	}

	@RequestMapping("/listJdbc")
	public ModelAndView listJdbc(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		BasePagingController<Map, String> ctrl = new BasePagingController<Map, String>() {
			@Override
			public PageResult findByPage(PageInfo pageInfo) {
				return baseJdbcDao.findByPage((JdbcPageInfo) pageInfo);
			}

			@Override
			protected PageInfo makePageInfo(HttpServletRequest request) {
				JdbcPageInfo pi = new JdbcPageInfo();
				pi.putSql("select * from user_model");
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
				return pi;
			}
		};
		ctrl.handleRequest(request, response);
		return new ModelAndView("listJdbc", "userList", BasePagingController
				.getPageData(request));
	}

	@RequestMapping("/queryJdbc")
	public ModelAndView queryJdbc(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		BaseQueryFormPagingController<Map, String> ctrl = new BaseQueryFormPagingController<Map, String>() {
			@Override
			protected PageResult findByPageInfo(PageInfo<Map, String> pi) {
				return baseJdbcDao.findByPage((JdbcPageInfo) pi);
			}

			@Override
			protected PageQuery<Map, String> makePageQuery() {
				return new UserModelJdbcQuery();
			}
		};

		ctrl.setCommandClass(UserModelJdbcQuery.class);
		ctrl.setCommandName("userModel");
		ctrl.setFormView("queryJdbc");
		ctrl.setSuccessView("redirect:/queryJdbc.htm");
		ctrl.setPagingDataName("userList");
		ctrl.setPagingViewName("queryJdbc");
		return ctrl.handleRequest(request, response);
	}

	@RequestMapping("/listIbatis")
	public ModelAndView listIbatis(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		BasePagingController ctrl = new BasePagingController<IbatisPagerable, String>() {
			@Override
			public PageResult findByPage(PageInfo<IbatisPagerable, String> pageInfo) {
				return baseIbatisDao.findByPage((IbatisPageInfo) pageInfo);
			}

			@Override
			protected PageInfo<IbatisPagerable, String> makePageInfo(
					HttpServletRequest request) {
				return new UserModelIbatisQuery().generatePageInfo();
			}
		};
		ctrl.handleRequest(request, response);
		return new ModelAndView("listIbatis", "userList", BaseHbmPagingController
				.getPageData(request));
	}

	@RequestMapping("/queryIbatis")
	public ModelAndView queryIbatis(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		BaseQueryFormPagingController ctrl = new BaseQueryFormPagingController<IbatisPagerable, String>() {
			@Override
			protected PageResult findByPageInfo(PageInfo<IbatisPagerable, String> pi) {
				return baseIbatisDao.findByPage((IbatisPageInfo) pi);
			}

			@Override
			protected PageQuery<IbatisPagerable, String> makePageQuery() {
				return new UserModelIbatisQuery();
			}
		};

		ctrl.setCommandClass(UserModelIbatisQuery.class);
		ctrl.setCommandName("userModel");
		ctrl.setFormView("queryIbatis");
		ctrl.setSuccessView("redirect:/queryIbatis.htm");
		ctrl.setPagingDataName("userList");
		return ctrl.handleRequest(request, response);
	}

	@RequestMapping("/initData")
	public ModelAndView initData(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		List data = baseHibernateDao.findByCriteria(DetachedCriteria
				.forClass(UserModel.class));
		for (Object o : data) {
			baseHibernateDao.delete(UserModel.class, ((UserModel) o).getId());
		}

		UserModel user = null;
		for (int i = 0; i < 256; i++) {
			user = new UserModel();
			user.setBirth(new Date());
			user.setEmail("liyingquan@gmail.com" + i);
			user.setRealName("liyingquan" + i);
			user.setSex("m");
			user.setUserName("admin" + i);
			baseHibernateDao.saveOrUpdate(user);
		}
		// return listHibernate(request, response);
		return new ModelAndView("redirect:/");
	}

	public void setBaseHibernateDao(BaseHibernateDao baseHibernateDao) {
		this.baseHibernateDao = baseHibernateDao;
	}
}