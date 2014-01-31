package com.breskeby.gradle.releaseit.svn;

import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.BasicAuthenticationManager;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNCopyClient;
import org.tmatesoft.svn.core.wc.SVNCopySource
import org.tmatesoft.svn.core.wc.SVNInfo;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNWCClient;

//TODO can be abstracted with a SvnCopy Task
class SvnTag extends SvnTask{

	private static String TRUNK = "/TRUNK";
	private static String BRANCHES = "/BRANCHES";
	private static String TAGS = "/TAGS";

	@Input String message = "Tagging with gradle"
	@Input String tagName;
	
	private SVNClientManager clientManager
	
	public SvnTag(){
	}
	
	@TaskAction public void tagit() throws SVNException{
        SVNRepositoryFactoryImpl.setup();
        ISVNAuthenticationManager authManager = new BasicAuthenticationManager( userName , userPassword );
        clientManager = SVNClientManager.newInstance(null, authManager);
        SVNWCClient wcClient = clientManager.getWCClient();
        SVNInfo doInfo = wcClient.doInfo(getProject().file("."), SVNRevision.WORKING);
        SVNURL srcURL = doInfo.getURL();
        SVNRevision revision = doInfo.getRevision();

		SVNURL dstURL = calculateDestURL(srcURL);
		SVNCopyClient copyClient = clientManager.getCopyClient();
		SVNCopySource[] copySources = [new SVNCopySource(revision, revision, srcURL)] as SVNCopySource[]; 
		copyClient.doCopy(copySources, dstURL, false, false, true, message, null);
	}

	SVNURL calculateDestURL(SVNURL sourceURL) {
		String rootURL = getRootURL(sourceURL)
		return SVNURL.parseURIEncoded("$rootURL/tags/$tagName");
	}
	
	SVNURL getRootURL(SVNURL sourceURL) throws SVNException{
		SVNURL rootURL = sourceURL;
		String path = sourceURL.getPath().toUpperCase();
		if(path.contains(TRUNK)){
			rootURL = sourceURL.removePathTail();
		}else if(path.contains(BRANCHES) || path.contains(TAGS)){
			SVNURL branchtagURL = sourceURL.removePathTail();
			if(branchtagURL.getPath()!=null && !branchtagURL.equals("")){
				rootURL = branchtagURL.removePathTail();
			}
		}
		return rootURL;
	}
}
