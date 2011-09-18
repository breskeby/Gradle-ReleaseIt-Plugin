package com.breskeby.gradle.releaseit.svn;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.tmatesoft.svn.core.ISVNLogEntryHandler;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.BasicAuthenticationManager;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNCommitClient;
import org.tmatesoft.svn.core.wc.SVNLogClient;
import org.tmatesoft.svn.core.wc.SVNRevision;

public class SvnBranchIntegrationTest {
	@Rule public TemporaryFolder folder = new TemporaryFolder();

	private static String TEST_BRANCH_NAME = "branches/testBranch";
	private SvnBranch cut;
	private String testRootURL;
	
	@Before public void createLocalProjectDirectory(){
		cut = new SvnBranch();
		testRootURL = "svn://10.37.129.3/svn/test/proj1";		
		
		cut.setUserName("harry");
		cut.setUserPassword("harryssecret");
		cut.setSourceBranch("trunk");
		cut.setTargetBranch(TEST_BRANCH_NAME);
	}

	@Test public void testbranching(){		
		cut.branch();
		assertBranchExist(testRootURL + "/", TEST_BRANCH_NAME);
	}
	
	@After public void deleteBranch() throws SVNException{
		SVNRepositoryFactoryImpl.setup();
		ISVNAuthenticationManager authManager = new BasicAuthenticationManager( "harry", "harryssecret");
		SVNClientManager clientManager = SVNClientManager.newInstance(null, authManager);
	    SVNCommitClient client = clientManager.getCommitClient();
	    String branchUrl = testRootURL + "/" + TEST_BRANCH_NAME;
		client.doDelete(new SVNURL[]{ SVNURL.parseURIEncoded(branchUrl) }, "Delete Test Branch from Test");
	
	}
	
	private void assertBranchExist(String svnRootUrl, String string) {
		SVNRepositoryFactoryImpl.setup();
		ISVNAuthenticationManager authManager = new BasicAuthenticationManager( "harry", "harryssecret");
		SVNClientManager clientManager = SVNClientManager.newInstance(null, authManager);

		SVNLogClient logClient = clientManager.getLogClient();
	    String branchUrl = testRootURL + "/" + TEST_BRANCH_NAME;

		SVNRevision revision = SVNRevision.create(new Date()); 

		try {
			final SVNURL url = SVNURL.parseURIEncoded(branchUrl); 
			logClient.doLog(url, null, SVNRevision.HEAD, revision, revision, 
			                false, false, false, -1, null, 
			                new ISVNLogEntryHandler() { 
								public void handleLogEntry(SVNLogEntry logEntry) throws SVNException { 
										System.out.println("this revision changes " + url); 
								} 
					});
		} catch (SVNException e) {
			// TODO Auto-generated catch block
			new AssertionError();
		} 
	}
}
