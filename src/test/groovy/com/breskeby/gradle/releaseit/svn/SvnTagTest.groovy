package com.breskeby.gradle.releaseit.svn

import org.tmatesoft.svn.core.SVNURL;


import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

class SvnTagTest extends Specification{

	SvnTag cut;
	
	def setup(){
		Project testProject = ProjectBuilder.builder().build()
		cut = testProject.task("testTagMeTask", type:SvnTag)
	}
	
	def "test getRootURL with on default svn layout"(){
		expect:
			cut.getRootURL(SVNURL.parseURIDecoded(givenSourceURL)).toString() == expectedRootURL
		where:
			givenSourceURL							| expectedRootURL
				"svn://testrepo/trunk"				| "svn://testrepo"		
				"svn://testrepo/branches/branch1"	| "svn://testrepo"		
				"svn://testrepo/tags/tag1"			| "svn://testrepo"		

	}
	
	def "test calculateDestURL with on default svn layout"(){
		setup:
			cut.tagName = "TestTag"
		expect:
			cut.calculateDestURL(SVNURL.parseURIDecoded(givenSourceURL)) == SVNURL.parseURIDecoded(expectedTagURL)
		
		where:
				givenSourceURL						| expectedTagURL
				"svn://testrepo/trunk"				| "svn://testrepo/tags/TestTag"		
				"svn://testrepo/branches/branch1"	| "svn://testrepo//tags/TestTag"		
				"svn://testrepo/tags/tag1"			| "svn://testrepo/tags/TestTag"		
	}
}
