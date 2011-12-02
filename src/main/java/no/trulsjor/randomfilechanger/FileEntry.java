package no.trulsjor.randomfilechanger;

import static no.trulsjor.randomfilechanger.SystemConstants.NEWLINE;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Scanner;

import org.apache.commons.lang.WordUtils;

public class FileEntry {

    private int index;
    private File fileName;
    private boolean touched;
    private String speaker;
    private String subject;

    public FileEntry(int index, File file) {
	this.index = index;
	this.fileName = file;
	this.touched = false;
	this.speaker = findSpeaker();

    }

    private String findSpeaker() {
	String speaker = "UNKNOWN";
	String fileNameAsString = fileName.getName();
	
	if (fileNameAsString.contains("-")){
	    String speakerName =  fileName.getName().substring(0,fileNameAsString.indexOf("-"));
	    speaker = WordUtils.capitalize(speakerName.replaceAll("_", " "));
	}
	return speaker;
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

    @Override
    public String toString() {
	return "FileEntry [index=" + index + ", speaker=" + speaker + ", fileName=" + fileName + "]";
    }

    public String name() {
	try {
	    return fileName.getCanonicalPath();
	} catch (IOException e) {
	    throw new RuntimeException(e);
	}
    }

}
