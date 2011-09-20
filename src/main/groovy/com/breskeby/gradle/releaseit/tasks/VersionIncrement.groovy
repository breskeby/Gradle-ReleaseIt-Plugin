package com.breskeby.gradle.releaseit.tasks;

import java.io.File;

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputFile;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;

public class VersionIncrement extends DefaultTask{

	@InputFile File propertyFile
	@Input List propertyNames = []
	
	@Input String incrementPattern

	public void setPropertyName(String propertyName){
		propertyNames << propertyName
	}
	
	public File getPropertyFile() {
		return propertyFile;
	}
	
	public void setPropertyFile(Object relativePathFromProjectRoot){
		this.propertyFile = getProject().file(relativePathFromProjectRoot);
	}
	
	@TaskAction void update(){
		Properties props = new Properties()

		//read old value
		propertyFile.withInputStream { stream ->
			props.load(stream)
		}

		//set new value
		propertyNames.each{propName ->
			def newValue = applyPattern(props[propName], incrementPattern);
			props.setProperty(propName, newValue)
		}
		propertyFile.withOutputStream{ stream ->
			props.store(stream, null)
		}
	}

	 String applyPattern(String value, String pattern){
		def versionMatcher = value =~ /\d/
		def applyPatternMatcher = pattern =~ /\d/

		//as matcher[] throws ArrayOutofbounds we use a workaround here
		List oldVersionList = []

		versionMatcher.eachWithIndex{fragm, idx ->
			oldVersionList[idx] = Integer.parseInt(fragm)
		}
		def applyPatternSize = applyPatternMatcher.size()
		def newVersion = new StringBuilder()

		applyPatternMatcher.eachWithIndex{ mat, idx->
			int orgval = oldVersionList[idx] == null ? 0 : oldVersionList[idx]

			def merged = orgval + Integer.parseInt(mat)
			newVersion << merged
			if(applyPatternSize>idx+1){
				newVersion << "."
			}
		}

		//add eventually existing appendix in pattern
		def patternAppendixStartIndex = pattern.indexOf("-");
		if(patternAppendixStartIndex!=-1){
			newVersion.append(pattern.substring(patternAppendixStartIndex))
		}
		newVersion.toString()
	}
}
