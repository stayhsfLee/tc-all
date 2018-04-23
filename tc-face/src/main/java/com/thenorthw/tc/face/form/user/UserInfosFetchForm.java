package com.thenorthw.tc.face.form.user;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @autuor theNorthW
 * @date 18/09/2017.
 * blog: thenorthw.com
 */
public class UserInfosFetchForm {
	@Pattern(regexp = "^([1-9]\\d*)(,([1-9]\\d*))*")
	@NotNull
	String userIds;


	public String getUserIds() {
		return userIds;
	}

	public void setUserIds(String userIds) {
		this.userIds = userIds;
	}
}
