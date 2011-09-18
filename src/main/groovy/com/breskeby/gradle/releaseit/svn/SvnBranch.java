package com.breskeby.gradle.releaseit.svn;

import org.gradle.api.tasks.TaskAction;
import org.gradle.tooling.BuildException;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.BasicAuthenticationManager;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNCopyClient;
import org.tmatesoft.svn.core.wc.SVNCopySource;
import org.tmatesoft.svn.core.wc.SVNRevision;

public class SvnBranch extends SvnTask{

	String sourceBranch;
	String targetBranch;
	
	public String getSourceBranch() {
		return sourceBranch;
	}

	public void setSourceBranch(String sourceBranch) {
		this.sourceBranch = sourceBranch;
	}

	public String getTargetBranch() {
		return targetBranch;
	}

	public void setTargetBranch(String targetBranch) {
		this.targetBranch = targetBranch;
	}

	@TaskAction
	public void branch(){
		try {
			SVNRepositoryFactoryImpl.setup();
			ISVNAuthenticationManager authManager = new BasicAuthenticationManager( userName , userPassword );
			SVNClientManager clientManager = SVNClientManager.newInstance(null, authManager);
		    SVNCopyClient client = clientManager.getCopyClient();
		    SVNURL srcURL = SVNURL.parseURIEncoded(rootUrl + "/" + sourceBranch); 
		    SVNURL dstURL = SVNURL.parseURIEncoded(rootUrl + "/" + targetBranch);
		    
		    SVNCopySource copySource = new SVNCopySource(SVNRevision.HEAD, SVNRevision.HEAD, srcURL); 
		    client.doCopy(new SVNCopySource[] {copySource}, dstURL, 
		    		        false, false, true, "branching from [ " + sourceBranch + " ] to [ " + targetBranch +" ]", null); 
		} catch (SVNException e) {
			throw(new BuildException(e.getMessage(), e));
		}
	}
}
