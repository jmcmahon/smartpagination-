package org.powerstone.smartpagination.tag;

import java.io.StringWriter;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class FreemarkerUtil {
	private static Template pageHeadTemplate;
	private static Template pageBarTemplate;
	static Configuration cfg;
	static {
		try {
			cfg = new Configuration();
			cfg.setLocalizedLookup(false);
			cfg.setClassForTemplateLoading(FreemarkerUtil.class, "/META-INF/ftls");
			pageHeadTemplate = cfg.getTemplate("pageHead.ftl", "UTF-8");
			pageBarTemplate = cfg.getTemplate("pageBar.ftl", "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public static String rendPageHead(Map map) {
		StringWriter sw = new StringWriter();
		try {
			pageHeadTemplate.process(map, sw);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sw.toString();
	}

	@SuppressWarnings("unchecked")
	public static String rendPageBar(Map map) {
		StringWriter sw = new StringWriter();
		try {
			pageBarTemplate.process(map, sw);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sw.toString();
	}
}