package com.breskeby.gradle.releaseit.svn;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class SvnCheckoutIntegrationTest {

	@Rule public TemporaryFolder folder = new TemporaryFolder();


	private File projectFolder;

	private SvnCheckout cut;
	
	@Before public void createLocalProjectDirectory(){
		projectFolder = folder.newFolder("workspace");
		cut = new SvnCheckout();
		cut.setLocalWorkspace(projectFolder);
	
		cut.setUserName("harry");
		cut.setUserPassword("harryssecret");
	}

	@Test public void checkoutProjectToLocalDirectory(){		
		cut.checkout();
		assertTrue(projectFolder.list().length != 0);
	}
	
	@Test public void testNetworkSettings() throws MalformedURLException{
		new URL("http://10.37.129.3/svn/test/proj1").getHost();
	}
}
