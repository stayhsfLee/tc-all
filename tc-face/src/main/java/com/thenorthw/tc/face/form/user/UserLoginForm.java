package com.thenorthw.tc.face.form.user;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @autuor  theNorthW
 * @date 16/08/2017.
 * blog: thenorthw.com
 */
public class UserLoginForm {
	@NotNull
	@Email
	String loginname;

	@Pattern(regexp = "^[a-fA-F0-9]{32}$")
	@NotNull
	String password;

	@Pattern(regexp = "1")
	@NotNull
	String logintype;

	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getLogintype() {
		return logintype;
	}

	public void setLogintype(String logintype) {
		this.logintype = logintype;
	}
}
