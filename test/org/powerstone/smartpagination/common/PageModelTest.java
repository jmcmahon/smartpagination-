package org.powerstone.smartpagination.common;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PageModelTest {
	private PageModel pm = null;

	@Before
	public void setUp() throws Exception {
		pm = new PageModel();
		pm.setTotalRecordsNumber(55);
		pm.setPageSize(10);
	}

	@After
	public void tearDown() throws Exception {
		pm = null;
	}

	@Test
	public void testDefaultComputeRecordsBeginNo() {
		assertEquals("default RecordsBeginNo", pm.computeRecordsBeginNo(), 0);
	}

	@Test
	public void testToPageComputeRecordsBeginNo() {
		pm.setPageTo("4");
		assertEquals("default RecordsBeginNo", pm.computeRecordsBeginNo(), 30);
		pm.setPageTo("6");
		assertEquals("default RecordsBeginNo", pm.computeRecordsBeginNo(), 50);
	}

	@Test
	public void testChangePageComputeRecordsBeginNo() {
		pm.setTotalRecordsNumber(55);
		pm.setPageSize(10);

		pm.setBeEnd(true);
		assertEquals("Change1", pm.computeRecordsBeginNo(), 50);
		pm.clear();

		pm.setBeFirst(true);
		assertEquals("Change2", pm.computeRecordsBeginNo(), 0);
		pm.clear();

		pm.setBeLast(true);
		assertEquals("Change3", pm.computeRecordsBeginNo(), 0);
		pm.clear();

		pm.setBeNext(true);
		assertEquals("Change4", pm.computeRecordsBeginNo(), 0);
		pm.clear();

		pm.setCurrPageNoOnRequest("3");
		pm.setBeLast(true);
		assertEquals("Change5", pm.computeRecordsBeginNo(), 10);
		pm.clear();

		pm.setBeNext(true);
		assertEquals("Change6", pm.computeRecordsBeginNo(), 30);
		pm.clear();

	}

	@Test
	public void testToPageSize20ComputeRecordsBeginNo() {
		pm.setPageTo("3");
		pm.setTotalRecordsNumber(88);
		pm.setPageSize(20);
		assertEquals("compute1", pm.computeRecordsBeginNo(), 40);

		pm.setPageSize(25);
		assertEquals("compute2", pm.computeRecordsBeginNo(), 50);
		pm.setPageTo("4");
		assertEquals("compute3", pm.computeRecordsBeginNo(), 75);
		pm.setPageTo("5");
		assertEquals("compute4", pm.computeRecordsBeginNo(), 75);
	}

	@Test
	public void testDefaultComputeDestinationPageNo() {
		assertEquals("default NewPageNo", pm.computeDestinationPageNo(), 1);
		pm.setPageTo("3");
		pm.setTotalRecordsNumber(88);
		pm.setPageSize(20);
		assertEquals("compute2", pm.computeDestinationPageNo(), 3);
		pm.setPageTo("4");
		assertEquals("compute3", pm.computeDestinationPageNo(), 4);
		pm.setPageTo("5");
		assertEquals("compute4", pm.computeDestinationPageNo(), 5);
		pm.setPageTo("6");
		assertEquals("compute5", pm.computeDestinationPageNo(), 5);
	}

	@Test
	public void testDefaultComputePageCount() {
		assertEquals("default PageCount", pm.computePageCount(), 6);
		pm.setTotalRecordsNumber(88);
		pm.setPageSize(20);
		assertEquals("88/20", 5, pm.computePageCount());
	}

	@Test
	public void testIsAssignableFrom() {
		assertTrue(PageModel.class.isAssignableFrom(pm.getClass()));
		assertTrue(pm.getClass().isAssignableFrom(PageModel.class));

		final class PageModelChild extends PageModel {
		}
		PageModelChild pmc = new PageModelChild();

		//
		assertTrue(PageModel.class.isAssignableFrom(pmc.getClass()));
		assertFalse(pmc.getClass().isAssignableFrom(PageModel.class));
		assertTrue(pmc.getClass().isAssignableFrom(PageModelChild.class));

		assertFalse(String.class.isAssignableFrom(Object.class));
		assertTrue(Object.class.isAssignableFrom(Object.class));
		assertTrue(Object.class.isAssignableFrom(String.class));
	}
}