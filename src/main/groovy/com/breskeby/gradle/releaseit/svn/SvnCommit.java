package com.breskeby.gradle.releaseit.svn;

import java.io.File;

import org.gradle.api.tasks.InputDirectory;
import org.gradle.api.tasks.TaskAction;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.auth.BasicAuthenticationManager;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNCommitClient;

public class SvnCommit extends SvnTask{

	@InputDirectory File localWorkspace;
	
	String branch = "trunk";

	private String commitMessage;
	
	public File getLocalWorkspace() {
		return localWorkspace;
	}

	public void setLocalWorkspace(File localWorkspace) {
		this.localWorkspace = localWorkspace;
	}

	@TaskAction
	public void checkout(){
		try {
			SVNRepositoryFactoryImpl.setup();
//			SVNURL url = SVNURL.parseURIDecoded( rootUrl + "/" + branch);
			ISVNAuthenticationManager authManager = new BasicAuthenticationManager( userName , userPassword );
			SVNClientManager clientManager = SVNClientManager.newInstance(null, authManager); 
			
			SVNCommitClient committer = clientManager.getCommitClient(); 
			committer.doCommit(new File[] {new File(localWorkspace.getAbsolutePath(), branch)}, false, commitMessage, null, 
			             null, false, false, SVNDepth.INFINITY);
		} catch (SVNException e) {
			e.printStackTrace();
		}
	}
}
