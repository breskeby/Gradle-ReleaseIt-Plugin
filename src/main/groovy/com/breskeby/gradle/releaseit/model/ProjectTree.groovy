package com.breskeby.gradle.releaseit.model;

class ProjectTree {
	String name

	public ProjectTree(String name) {
		this.name = name;
	}
	
	String svnRootUrl
	def localPath
}
