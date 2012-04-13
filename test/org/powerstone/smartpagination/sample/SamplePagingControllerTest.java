package org.powerstone.smartpagination.sample;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.powerstone.smartpagination.common.BasePagingController;
import org.powerstone.smartpagination.common.PageModel;
import org.powerstone.smartpagination.common.PageResult;
import org.powerstone.smartpagination.hibernate.BaseHibernateDao;
import org.powerstone.smartpagination.hibernate.HbmPageInfo;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class SamplePagingControllerTest extends TestCase {
	protected MockHttpServletRequest request;

	protected MockHttpServletResponse response;

	@SuppressWarnings("unchecked")
	@Test
	public void testList() throws Exception {
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
		SamplePagingController spc = new SamplePagingController();
		BaseHibernateDao dao = Mockito.mock(BaseHibernateDao.class);
		spc.setBaseHibernateDao(dao);

		PageResult pageResult = new PageResult();
		pageResult.setPageAmount(5);
		pageResult.setPageData(new ArrayList());
		pageResult.setTotalRecordsNumber(48);
		Mockito.stub(dao.findByPage(Matchers.isA(HbmPageInfo.class)))
				.toReturn(pageResult);

		request.setMethod("GET");
		request.setRequestURI("/list.htm");

		spc.listHibernate(request, response);

		PageModel pm = (PageModel) request
				.getAttribute(BasePagingController.DEFAULT_PAGE_MODEL_NAME);
		List data = (List) request
				.getAttribute(BasePagingController.DEFAULT_PAGE_DATA_NAME);

		Assert.assertEquals(0, data.size());
		Assert.assertEquals(5, pm.computePageCount());
		Assert.assertEquals(48, pm.getTotalRecordsNumber());
		Assert.assertEquals(10, pm.getPageSize());
		Assert.assertEquals(1, pm.computeDestinationPageNo());
	}
}