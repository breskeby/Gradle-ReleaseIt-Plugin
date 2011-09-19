package com.breskeby.gradle.releaseit.svn;

import java.io.File;

import org.gradle.api.Project;
import org.gradle.api.Task
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.Test;

public class SvnCheckoutTest {

	@Test public void testSetPropertyOverride(){
		ProjectBuilder builder = ProjectBuilder.builder();
		Project project = builder.build();
		SvnCheckout checkout = project.task("SvnCheckoutTest", type:SvnCheckout)
		checkout.setLocalWorkspace("test");
		assert checkout.getLocalWorkspace() == project.file("test");
	}
}
