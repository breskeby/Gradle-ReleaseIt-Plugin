package com.breskeby.gradle.releaseit.tasks

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

public class VersionIncrementSpec extends Specification {
	VersionIncrement cut;

	def setup(){
		Project testProject = ProjectBuilder.builder().build()
		cut = testProject.task("testVersionIncrement", type:VersionIncrement)
	}

	def 'test with Major Only Apply Pattern'(){
		expect:
		cut.applyPattern(givenVersion, increment) == expectedVersion

		where:
		givenVersion | increment | expectedVersion
		"1.0.0"			 | "0"			 | "1"
		"1.0.0"			 | "1"			 | "2"
	}

	def "test with Minor Apply Pattern"(){
		expect:
		cut.applyPattern(givenVersion, increment) == expectedVersion

		where:
		givenVersion | increment | expectedVersion
		"1.0.0"			 | "0.1"			 | "1.1"
		"1.0.0"			 | "0.0"			 | "1.0"
		"1.0.0"			 | "1.0"			 | "2.0"
	}

	def "test with Bugfix Apply Pattern"(){
		expect:
		cut.applyPattern(givenVersion, increment) == expectedVersion

		where:
		givenVersion | increment 		 | expectedVersion
		"1.0.0"			 | "0.1.0"			 | "1.1.0"
		"1.0.0"			 | "0.0.0"			 | "1.0.0"
		"1.0.0"			 | "1.0.0"			 | "2.0.0"
		"1.0.0"			 | "1.0.1"			 | "2.0.1"
		"1.0.0"			 | "1.1.1"			 | "2.1.1"
	}

	def "test Appendix On Original Version Is Ignored"(){
		expect:
		cut.applyPattern(givenVersion, increment) == expectedVersion

		where:
		givenVersion 			 | increment 	 | expectedVersion
		"1.0.0-SNAPSHOT"	 | "0.1.0"		 | "1.1.0"
		"1.0.0-BETA"			 | "0.0.0"		 | "1.0.0"
		"1.0.0-BETA2"			 | "1.0.0"		 | "2.0.0"
		"1.0.0-ALPHA"			 | "1.0.1"		 | "2.0.1"
		"1.0.0-BLINGBLING" | "1.1.1"		 | "2.1.1"
	}

	def "test Appendix Of Pattern Is Merged"(){
		expect:
		cut.applyPattern(givenVersion, increment) == expectedVersion

		where:
		givenVersion			| increment 	 			| expectedVersion
		"1.0.0"	 					| "0.1.0-SNAPSHOT"  | "1.1.0-SNAPSHOT"
		"1.0.0-BETA"	 		| "0.0.0-SNAPSHOT"	| "1.0.0-SNAPSHOT"
		"1.0.0-SNAPSHOT"	| "0.0.0-SNAPSHOT"	| "1.0.0-SNAPSHOT"
	}
}