package com.breskeby.gradle.releaseit.svn;

import java.io.File;

import org.junit.Test;

public class SvnCheckoutTest {

	
	@Test public void testSetPropertyOverride(){
		SvnCheckout checkout = new SvnCheckout();
		checkout.setLocalWorkspace(new File("test"));
	}
}
