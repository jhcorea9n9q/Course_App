package com.example.web;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

public class UserForm implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@NotEmpty
	private String userId;
	
	@NotEmpty
	private String passwd;
	
	private String passwdCheck;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public String getPasswdCheck() {
		return passwdCheck;
	}
	public void setPasswdCheck(String passwdCheck) {
		this.passwdCheck = passwdCheck;
	}

}
