package com.breskeby.gradle.releaseit;

public class Version {
	int major
	int minor
	int bugfix
	
	String appendix = ""
	
	String toString(){
		"${major}.${minor}.${bugfix}${bugfix}"
	}
}
