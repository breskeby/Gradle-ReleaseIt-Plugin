package com.breskeby.gradle.releaseit.svn;

import java.io.File;

import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.BasicAuthenticationManager;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;

public class SvnCheckout extends SvnTask{

	String branch = "trunk";
	
	@OutputDirectory File localWorkspace;

	public File getLocalWorkspace() {
		return localWorkspace;
	}
	
	public void setLocalWorkspace(Object relativePathFromProjectRoot){
		this.localWorkspace = getProject().file(relativePathFromProjectRoot);
	}
	
	@TaskAction
	public void checkout(){
		try {
			SVNRepositoryFactoryImpl.setup();
			SVNURL url = SVNURL.parseURIDecoded( rootUrl + "/" + branch);
			ISVNAuthenticationManager authManager = new BasicAuthenticationManager( userName , userPassword );
			SVNClientManager clientManager = SVNClientManager.newInstance(null, authManager); 
			SVNUpdateClient client = clientManager.getUpdateClient();
		    client.doCheckout(url, new File(localWorkspace.getAbsolutePath(), branch), SVNRevision.UNDEFINED, SVNRevision.HEAD, SVNDepth.INFINITY, true);
		} catch (SVNException e) {
			e.printStackTrace();
		}
	}
}
