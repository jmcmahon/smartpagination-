package org.powerstone.smartpagination.ibatis;

import org.powerstone.smartpagination.common.PageInfo;
import org.powerstone.smartpagination.common.PageQuery;
import org.powerstone.smartpagination.sample.UserModel;

public class UserModelIbatisQuery extends UserModel implements
		PageQuery<IbatisPagerable, String>, IbatisPagerable {
	private boolean userNameLike;

	private String orderByStr;

	private int offset;

	private int limit;

	public boolean isUserNameLike() {
		return userNameLike;
	}

	public void setUserNameLike(boolean userNameLike) {
		this.userNameLike = userNameLike;
	}

	public String getOrderByStr() {
		return orderByStr;
	}

	public void setOrderByStr(String orderByStr) {
		this.orderByStr = orderByStr;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public PageInfo<IbatisPagerable, String> generatePageInfo() {
		IbatisPageInfo pi = new IbatisPageInfo();
		pi.setCountQueryName("countUser");
		pi.setPageQueryName("hsql_findUsers");
		UserModelIbatisQuery exampleModel = new UserModelIbatisQuery();
		exampleModel.setBirth(this.getBirth());
		if (getEmail() != null && getEmail().trim().length() > 0) {
			exampleModel.setEmail("%" + getEmail().toLowerCase() + "%");
		}
		exampleModel.setId(getId());
		if (getRealName() != null && getRealName().trim().length() > 0) {
			exampleModel.setRealName("%" + getRealName().toLowerCase() + "%");
		}
		if (getSex() != null && getSex().trim().length() > 0) {
			exampleModel.setSex(getSex());
		}
		exampleModel.setUserNameLike(isUserNameLike());
		if (isUserNameLike()) {
			exampleModel.setUserName("%" + getUserName().toLowerCase() + "%");
		} else {
			exampleModel.setUserName(getUserName());
		}

		pi.setExpression(exampleModel);
		return pi;
	}

}