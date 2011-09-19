package com.breskeby.gradle.releaseit.svn;

import java.io.File

import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction
import org.tmatesoft.svn.core.SVNDepth
import org.tmatesoft.svn.core.SVNException
import org.tmatesoft.svn.core.auth.BasicAuthenticationManager
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl
import org.tmatesoft.svn.core.wc.SVNClientManager
import org.tmatesoft.svn.core.wc.SVNCommitClient

public class SvnCommit extends SvnTask{

	@InputDirectory File localWorkspace;
	@Input String branch = "trunk";
	@Input String message;

	public File getLocalWorkspace() {
		return localWorkspace;
	}

	public void setLocalWorkspace(Object relativePathFromProjectRoot){
		this.localWorkspace = getProject().file(relativePathFromProjectRoot);
	}

	@TaskAction
	public void commit() throws SVNException{
		SVNRepositoryFactoryImpl.setup();
		ISVNAuthenticationManager authManager = new BasicAuthenticationManager( userName , userPassword );
		SVNClientManager clientManager = SVNClientManager.newInstance(null, authManager);

		File[] filesToCommit = [new File(localWorkspace.getAbsolutePath(), branch)];
		SVNCommitClient committer = clientManager.getCommitClient();
		committer.doCommit(filesToCommit, false, message, null, null, false, false, SVNDepth.INFINITY);
	}
}
