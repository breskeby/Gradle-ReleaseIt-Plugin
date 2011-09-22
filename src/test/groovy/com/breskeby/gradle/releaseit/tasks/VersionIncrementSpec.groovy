package com.breskeby.gradle.releaseit.tasks

import java.io.File;

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import spock.lang.Specification


class VersionIncrementSpec extends Specification {
	VersionIncrement cut;

	@Rule TemporaryFolder tempFolder = new TemporaryFolder();
	
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
		givenVersion		| increment 	 | expectedVersion
		"1.0.0-SNAPSHOT"	| "0.1.0"		 | "1.1.0"
		"1.0.0-BETA"		| "0.0.0"		 | "1.0.0"
		"1.0.0-BETA2"		| "1.0.0"		 | "2.0.0"
		"1.0.0-ALPHA"		| "1.0.1"		 | "2.0.1"
		"1.0.0-BLINGBLING"  | "1.1.1"		 | "2.1.1"
	}

	def "test Appendix Of Pattern Is Merged"(){
		expect:
		cut.applyPattern(givenVersion, increment) == expectedVersion

		where:
		givenVersion			| increment 	 	| expectedVersion
		"1.0.0"	 				| "0.1.0-SNAPSHOT"  | "1.1.0-SNAPSHOT"
		"1.0.0-BETA"	 		| "0.0.0-SNAPSHOT"	| "1.0.0-SNAPSHOT"
		"1.0.0-SNAPSHOT"		| "0.0.0-SNAPSHOT"	| "1.0.0-SNAPSHOT"
	}

	// not that happy with this spec. needs more spock skilled refactorings i guess
	def "test multiple properties are applied by same pattern"() {
		setup:
			File testPropFile = tempFolder.newFile("test.properties");
			
		
			Properties originProps = new Properties()
			originProps.put("testProp1","1.0.0-SNAPSHOT")
			originProps.put("testProp2","2.0.0-SNAPSHOT")
			testPropFile.withWriter {  writer -> 
				originProps.store(writer, "original props file")	
				
			}
			cut.propertyFile = testPropFile
			cut.incrementPattern = "0.0.1"
			cut.propertyNames << "testProp1" << "testProp2"

			
			
		when:
			cut.update()

			Properties newProps = new Properties()
			testPropFile.withInputStream { stream ->
				newProps.load(stream)
			}
		then:
			newProps["testProp1"] == "1.0.1"
			newProps["testProp2"] == "2.0.1"
	}
}