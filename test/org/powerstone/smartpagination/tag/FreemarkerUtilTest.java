package org.powerstone.smartpagination.tag;

import java.util.HashMap;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.junit.Test;

public class FreemarkerUtilTest extends TestCase {
	Logger logger=Logger.getLogger(getClass());
	@Test
	public void testRentPageHead() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("paging_fullUrl", "TTTT");
		String html = FreemarkerUtil.rendPageHead(map);
		logger.debug(html);
		Assert.assertTrue(html.indexOf("TTTT") > 0);
	}

	@Test
	public void testRentPageBar() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("paging_pageSizeParam", "ABCDE");
		String html = FreemarkerUtil.rendPageBar(map);
		logger.debug(html);
		Assert.assertTrue(html.indexOf("ABCDE.focus()") > 0);
	}
}