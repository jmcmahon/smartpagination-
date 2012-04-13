package org.powerstone.smartpagination.common;

public interface PageQuery<T_Criterial, T_OrderBy> {
	public abstract PageInfo<T_Criterial, T_OrderBy> generatePageInfo();
}
