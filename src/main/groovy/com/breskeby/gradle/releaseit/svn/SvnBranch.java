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

	String source;
	String target;
	String message = "Create branch";
	
	public void setMessage(String message){
		this.message = message;
	}
	
	public String getMessage(){
		return message;
	}
	
	
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	@TaskAction
	public void branch(){
		try {
			SVNRepositoryFactoryImpl.setup();
			ISVNAuthenticationManager authManager = new BasicAuthenticationManager( userName , userPassword );
			SVNClientManager clientManager = SVNClientManager.newInstance(null, authManager);
		    SVNCopyClient client = clientManager.getCopyClient();
		    SVNURL srcURL = SVNURL.parseURIEncoded(rootUrl + "/" + source); 
		    SVNURL dstURL = SVNURL.parseURIEncoded(rootUrl + "/" + target);
		    
		    SVNCopySource copySource = new SVNCopySource(SVNRevision.HEAD, SVNRevision.HEAD, srcURL); 
		    client.doCopy(new SVNCopySource[] {copySource}, dstURL, 
		    		        false, false, true, message, null); 
		} catch (SVNException e) {
			throw(new BuildException(e.getMessage(), e));
		}
	}
}
