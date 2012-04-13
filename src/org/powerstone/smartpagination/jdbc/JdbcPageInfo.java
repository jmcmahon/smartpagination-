package org.powerstone.smartpagination.jdbc;

import java.util.HashMap;
import java.util.Map;

import org.powerstone.smartpagination.common.PageInfo;
import org.springframework.jdbc.core.RowMapper;

/**
 * T Map:SQL->select xx from tt where p1 = :p1 and p2 like
 * :p2|example->model(p1,p2) T String:p1 asc , p2 desc ...
 */
@SuppressWarnings("unchecked")
public class JdbcPageInfo extends PageInfo<Map, String> {
	public static final String JDBC_PAGE_INFO_SQL = "JDBC_PAGE_INFO_SQL";
	public static final String JDBC_PAGE_INFO_EXAMPLE_MODEL = "JDBC_PAGE_INFO_EXAMPLE_MODEL";
	public static final String JDBC_PAGE_INFO_ROW_MAPPER = "JDBC_PAGE_INFO_ROW_MAPPER";

	public JdbcPageInfo() {
		super();
		super.expression = new HashMap();
	}

	public String getSql() {
		return (String) this.getExpression().get(JDBC_PAGE_INFO_SQL);
	}

	public void putSql(String sql) {
		this.getExpression().put(JDBC_PAGE_INFO_SQL, sql);
	}

	public Object getExampleModel() {
		return this.getExpression().get(JDBC_PAGE_INFO_EXAMPLE_MODEL);
	}

	public void putExampleModel(Object obj) {
		this.getExpression().put(JDBC_PAGE_INFO_EXAMPLE_MODEL, obj);
	}

	@Override
	public void addOrderByAsc(String orderBy) {
		super.getOrderByList().add(orderBy + " asc");
	}

	@Override
	public void addOrderByDesc(String orderBy) {
		super.getOrderByList().add(orderBy + " desc");
	}

	public RowMapper getRowMapper() {
		return (RowMapper) this.getExpression().get(JDBC_PAGE_INFO_ROW_MAPPER);
	}

	public void putRowMapper(RowMapper rm) {
		this.getExpression().put(JDBC_PAGE_INFO_ROW_MAPPER, rm);
	}

}
