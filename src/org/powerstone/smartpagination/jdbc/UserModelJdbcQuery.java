package org.powerstone.smartpagination.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.powerstone.smartpagination.common.PageInfo;
import org.powerstone.smartpagination.common.PageQuery;
import org.powerstone.smartpagination.sample.UserModel;
import org.springframework.jdbc.core.RowMapper;

@SuppressWarnings("unchecked")
public class UserModelJdbcQuery extends UserModel implements
		PageQuery<Map, String> {
	private boolean userNameLike;

	public boolean isUserNameLike() {
		return userNameLike;
	}

	public void setUserNameLike(boolean userNameLike) {
		this.userNameLike = userNameLike;
	}

	public PageInfo<Map, String> generatePageInfo() {
		JdbcPageInfo pi = new JdbcPageInfo();
		UserModel exampleModel = new UserModel();
		String sql = "select * from user_model where 1=1 ";
		if (super.getUserName() != null
				&& super.getUserName().trim().length() > 0) {
			if (userNameLike) {
				sql += " and lower(user_Name) like :userName";
				exampleModel.setUserName("%" + getUserName().toLowerCase()
						+ "%");
			} else {
				sql += " and user_Name = :userName";
				exampleModel.setUserName(getUserName());
			}
		}
		if (super.getEmail() != null && super.getEmail().trim().length() > 0) {
			sql += " and lower(email) like :email";
			exampleModel.setEmail("%" + getEmail().toLowerCase() + "%");
		}
		if (super.getRealName() != null
				&& super.getRealName().trim().length() > 0) {
			sql += " and lower(real_Name) like :realName";
			exampleModel.setRealName("%" + getRealName().toLowerCase() + "%");
		}
		if (super.getSex() != null && super.getSex().trim().length() > 0) {
			sql += " and sex = :sex";
			exampleModel.setSex(getSex());
		}

		pi.putSql(sql);
		pi.putExampleModel(exampleModel);
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

		return pi;
	}

}