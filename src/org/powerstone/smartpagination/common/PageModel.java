package org.powerstone.smartpagination.common;

public class PageModel {
	public final static String ORDER_ASC = "asc";

	public final static String ORDER_DESC = "desc";

	private int pageSize = 10;

	private String currPageNoOnRequest;

	private String pageTo = null;

	private int totalRecordsNumber;

	boolean beEnd = false;

	boolean beFirst = false;

	boolean beLast = false;

	boolean beNext = false;

	private String orderBy;

	private String orderDirection = ORDER_DESC;

	/**
	 * 查询前，根据请求参数计算要查的页码
	 * 
	 * 由于这时还不知道总记录数，因此无法计算首尾页号
	 * 
	 * @return
	 */
	public int computeNewPageNoForQuery() {
		Integer pageNum = (currPageNoOnRequest != null ? new Integer(currPageNoOnRequest)
				: new Integer("0"));
		int newCurrPageNo = 0;

		if (pageTo != null) {
			newCurrPageNo = new Integer(pageTo).intValue();
		} else if (!beFirst && !beLast && !beNext && !beEnd) {
			newCurrPageNo = 1;
		} else if (beLast) {
			newCurrPageNo = pageNum.intValue() - 1;
		} else if (beNext) {
			newCurrPageNo = pageNum.intValue() + 1;
		}
		// 由于这时还不知道总记录数，因此无法计算首尾页号，因此不检查beFirst和beEnd

		if (newCurrPageNo < 1) {
			newCurrPageNo = 1;
		}
		return newCurrPageNo;
	}

	/**
	 * 查询后计算当前页码
	 * 
	 * @return
	 */
	public int computeDestinationPageNo() {
		int newCurrPageNo = this.computeNewPageNoForQuery();
		int pageCount = computePageCount();

		if (beFirst) {
			newCurrPageNo = 1;
		} else if (beEnd) {
			newCurrPageNo = pageCount;
		}
		if (newCurrPageNo > pageCount) {
			newCurrPageNo = pageCount;
		}
		return newCurrPageNo;
	}

	// public int computeDestinationPageNo() {
	// int pageCount = computePageCount();
	// Integer pageNum = (currPageNoOnRequest != null ? new
	// Integer(currPageNoOnRequest)
	// : new Integer("0"));
	// int destPageNo = 0;
	//
	// if (pageTo != null) {
	// destPageNo = new Integer(pageTo).intValue();
	// } else if (!beFirst && !beLast && !beNext && !beEnd) {
	// destPageNo = 1;
	// } else if (beLast) {
	// destPageNo = pageNum.intValue() - 1;
	// } else if (beNext) {
	// destPageNo = pageNum.intValue() + 1;
	// } else if (beFirst) {
	// destPageNo = 1;
	// } else if (beEnd) {
	// destPageNo = pageCount;
	// }
	//
	// if (destPageNo > pageCount) {
	// destPageNo = pageCount;
	// }
	// if (destPageNo < 1) {
	// destPageNo = 1;
	// }
	// return destPageNo;
	// }

	public int computePageCount() {
		int pageCount = 0;
		if (totalRecordsNumber % pageSize == 0) {
			pageCount = totalRecordsNumber / pageSize;
		} else {
			pageCount = totalRecordsNumber / pageSize + 1;
		}
		return pageCount;
	}

	public void clear() {
		this.setBeEnd(false);
		this.setBeFirst(false);
		this.setBeNext(false);
		this.setBeLast(false);
	}

	public String getCurrPageNoOnRequest() {
		return currPageNoOnRequest;
	}

	public void setCurrPageNoOnRequest(String currPageNo) {
		this.currPageNoOnRequest = currPageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotalRecordsNumber() {
		return totalRecordsNumber;
	}

	public void setTotalRecordsNumber(int totalNumber) {
		this.totalRecordsNumber = totalNumber;
	}

	public void setBeEnd(boolean beEnd) {
		this.beEnd = beEnd;
	}

	public void setBeFirst(boolean beFirst) {
		this.beFirst = beFirst;
	}

	public void setBeLast(boolean beLast) {
		this.beLast = beLast;
	}

	public void setBeNext(boolean beNext) {
		this.beNext = beNext;
	}

	public void setPageTo(String pageTo) {
		this.pageTo = pageTo;
	}

	public String getPageTo() {
		return pageTo;
	}

	public boolean isBeEnd() {
		return beEnd;
	}

	public boolean isBeFirst() {
		return beFirst;
	}

	public boolean isBeLast() {
		return beLast;
	}

	public boolean isBeNext() {
		return beNext;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public String getOrderDirection() {
		return orderDirection;
	}

	public void setOrderDirection(String orderDirection) {
		this.orderDirection = orderDirection;
	}

	public boolean isOrderAsc() {
		return getOrderDirection().equals(ORDER_ASC);
	}

	public int computeRecordsBeginNo() {
		return (computeDestinationPageNo() - 1) * pageSize;
	}
}
