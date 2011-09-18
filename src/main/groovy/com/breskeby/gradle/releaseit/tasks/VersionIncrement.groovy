package com.breskeby.gradle.releaseit.tasks;

import java.io.File;

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputFile;
import org.gradle.api.tasks.TaskAction;

public class VersionIncrement extends DefaultTask{

	@InputFile File propertyFile
	@Input String versionProperty
	@Input String incrementPattern

	@TaskAction void update(){
		Properties props = new Properties()

		//read old value
		propertyFile.withInputStream { stream ->
			props.load(stream)
		}

		//set new value
		def newValue = applyPattern(props[versionProperty], incrementPattern);

		props.setProperty(versionProperty, newValue)
		propertyFile.withOutputStream{ stream ->
			props.store(stream, null)
		}
	}

	private String applyPattern(String value, String pattern){
		def versionMatcher = value =~ /\d/
		def applyPatternMatcher = pattern =~ /\d/

		//as matcher[] throws ArrayOutofbounds we use a workaround here
		List oldVersionList = []

		versionMatcher.eachWithIndex{fragm, idx ->
			oldVersionList[idx] = Integer.parseInt(fragm)
		}
		applyPatternSize = applyPatternMatcher.size()
		def newVersion = new StringBuilder()

		applyPatternMatcher.eachWithIndex{ mat, idx->
			int orgval = oldVersionList[idx] == null ? 0 : oldVersionList[idx]

			def merged = orgval + Integer.parseInt(mat)
			newVersion << merged
			if(applyPatternSize>idx+1){
				newVersion << "."
			}
		}
		newVersion.toString()
	}
}
