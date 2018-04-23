package com.thenorthw.tc.common.enums;

/**
 * Created by theNorthW on 21/07/2017.
 * blog: thenorthw.com
 *
 * @autuor : theNorthW
 */
public enum RoleType {
    ALL_USER(-1,"all_user"),
    NORMAL_USER(18,"normal_user"),
    ROOT(188,"root");



    private String rolename;
    public long roleId;

    RoleType(long role_id,String rolename){
        this.roleId = role_id;
        this.rolename = rolename;
    }

    public String getRolename() {
        return rolename;
    }

	public Long getRoleId() {
		return roleId;
	}
}
