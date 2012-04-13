package org.powerstone.smartpagination.ibatis;

public interface IbatisPagerable {
	public String getOrderByStr();

	public void setOrderByStr(String orderByStr);

	public int getOffset();

	public void setOffset(int offset);

	public int getLimit();

	public void setLimit(int limit);
}