package com.breskeby.gradle.releaseit.svn;

import org.gradle.api.DefaultTask;

public abstract class SvnTask extends DefaultTask {

	protected String userName;
	protected String userPassword;
	protected String rootUrl;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public SvnTask() {
		super();
	}

	public String getRootUrl() {
		return rootUrl;
	}

	public void setRootUrl(String rootUrl) {
		this.rootUrl = rootUrl;
	}

}