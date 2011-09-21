package com.breskeby.gradle.releaseit.tasks;

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.BeforeClass
import org.junit.Test
import org.junit.Rule;
import org.junit.Before;

import org.junit.rules.TemporaryFolder
public class VersionIncrementTest {
	static VersionIncrement cut;

	@Rule public static TemporaryFolder tempFolder = new TemporaryFolder();
	static File testPropFile = tempFolder.newFile("test.properties");

	@Before public void setupTask(){
		testPropFile.text = """
			testProp1 = 1.0.0-SNAPSHOT
			testProp2 = 2.0.0-SNAPSHOT
		"""
		Project testProject = ProjectBuilder.builder().build();
		cut = testProject.task("testVersionIncrement", type:VersionIncrement);
	}

	@Test public void testTaskExecutionWithSingleProperty(){
		cut.propertyFile = testPropFile.absoluteFile
		cut.incrementPattern = "0.0.1"
		cut.propertyName = "testProp1"

		cut.update()

		Properties newProps = new Properties()
		cut.propertyFile.withInputStream { stream ->
			newProps.load(stream)
		}
		assert newProps["testProp1"] == "1.0.1"
	}

	@Test public void testTaskExecutionWithMultipleProperties(){
		cut.propertyFile = testPropFile.absoluteFile
		cut.incrementPattern = "0.0.1"
		cut.propertyNames << "testProp1" << "testProp2"
		cut.update()

		Properties newProps = new Properties()
		cut.propertyFile.withInputStream { stream ->
			newProps.load(stream)
		}
		assert newProps["testProp1"] == "1.0.1"
		assert newProps["testProp2"] == "2.0.1"
	}


	@Test public void testWithZeroMajorOnlyApplyPattern(){
		assert "1" == cut.applyPattern("1.0.0", "0");
	}

	@Test public void testWithMajorOnlyApplyPattern(){
		assert "2" == cut.applyPattern("1.0.0", "1");
	}

	@Test public void testWithMinorApplyPattern(){
		assert "1.1" == cut.applyPattern("1.0.0", "0.1");
		assert "1.0" == cut.applyPattern("1.0.0", "0.0");
		assert "2.0" == cut.applyPattern("1.0.0", "1.0");
	}

	@Test public void testWithBufixApplyPattern(){
		assert "1.1.0" == cut.applyPattern("1.0.0", "0.1.0");
		assert "1.0.0" == cut.applyPattern("1.0.0", "0.0.0");
		assert "2.0.0" == cut.applyPattern("1.0.0", "1.0.0");
		assert "2.0.1" == cut.applyPattern("1.0.0", "1.0.1");
		assert "2.1.1" == cut.applyPattern("1.0.0", "1.1.1");
	}

	@Test public void testAppendixOnOriginalVersionIsIgnored(){
		assert "1.1.0" == cut.applyPattern("1.0.0-SNAPSHOT", "0.1.0");
		assert "1.0.0" == cut.applyPattern("1.0.0-BETA", "0.0.0");
		assert "2.0.0" == cut.applyPattern("1.0.0-BETA2", "1.0.0");
		assert "2.0.1" == cut.applyPattern("1.0.0-ALPHA", "1.0.1");
		assert "2.1.1" == cut.applyPattern("1.0.0-BLINGBLING", "1.1.1");
	}

	@Test public void testAppendixOfPatternIsMerged(){
		assert "1.1.0-SNAPSHOT" == cut.applyPattern("1.0.0", "0.1.0-SNAPSHOT");
		assert "1.0.0-SNAPSHOT" == cut.applyPattern("1.0.0-BETA", "0.0.0-SNAPSHOT");
		assert "1.0.0-SNAPSHOT" == cut.applyPattern("1.0.0-SNAPSHOT", "0.0.0-SNAPSHOT");
	}
}
