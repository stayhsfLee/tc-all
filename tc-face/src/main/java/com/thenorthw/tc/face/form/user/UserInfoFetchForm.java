package com.thenorthw.tc.face.form.user;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @autuor theNorthW
 * @date 18/09/2017.
 * blog: thenorthw.com
 */
public class UserInfoFetchForm {
	@Pattern(regexp = "[1-9]\\d*")
	@NotNull
	String userId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
