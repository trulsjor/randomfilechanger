package no.trulsjor.randomfilechanger;

import static no.trulsjor.randomfilechanger.SystemConstants.NEWLINE;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import org.apache.commons.lang.WordUtils;

public class FileEntry {

    private int index;
    private File fileName;
    private boolean touched;
    private String speaker;
    private String subject;
    private String context;

    public FileEntry(int index, File file) {
	this.index = index;
	this.fileName = file;
	this.touched = false;
	this.speaker = "UNKOWN";
	this.subject ="UNKOWN";
	analyzeFileName();
	
	
    }

    private void analyzeFileName() {
	String fileNameAsString = fileName.getName();
	context = fileName.getParentFile().getName();
	if (fileNameAsString.contains("-")) {
	    speaker = WordUtils.capitalize(fileNameAsString.substring(0, fileNameAsString.indexOf("-")).replace("_", " "));
	    subject = WordUtils.capitalize(fileNameAsString.substring(fileNameAsString.indexOf("-")+1, fileNameAsString.length()-4).replace("-", " ").replace("_", " "));
	}
    }
     
    public boolean hasFile(File file) {
	return this.fileName.getAbsolutePath().equals(file.getAbsolutePath());
    }

    public boolean containsText(String text) {
	return this.fileName.getAbsolutePath().contains(text);
    }

    public boolean isTouched() {
	return touched;
    }

    public void writeChange(String message) {
	try {
	    FileWriter writer = new FileWriter(fileName, true);
	    writer.write(NEWLINE + message);
	    writer.close();
	} catch (IOException e) {
	    throw new RuntimeException(e);
	}
	touched = true;
    }

    public String getFileContent() {
	StringBuffer content = new StringBuffer();

	try {
	    Scanner reader = new Scanner(fileName);
	    while (reader.hasNextLine()) {
		content.append(reader.nextLine() + " ");
	    }
	} catch (IOException e) {
	    throw new RuntimeException(e);
	}
	return content.toString();
    }

    public int getIndex() {
	return index;
    }

    public String getSpeaker() {
	return speaker;
    }
    
    public String getSubject() {
	return subject;
    }
    
    public String getContext(){
	return context;
    }

    @Override
    public String toString() {
	return "FileEntry [index=" + index + ", speaker=" + speaker + ", fileName=" + fileName + "]";
    }

    public String name() {

	return fileName.getName();

    }

}
