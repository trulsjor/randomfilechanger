package no.trulsjor.randomfilechanger;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class FileDB {

    private final File directory;

    private Map<Integer, FileEntry> db = new HashMap<Integer, FileEntry>();

    private List<String> invalidDirectories = Arrays.asList(".git", "target");
    private List<String> invalidFileNames = Arrays.asList(".class", ".gif", ".jpg", ".jpeg", ".png", ".bat", ".sh", ".wsdl");
    private static final String SEPARATOR = System.getProperty("file.separator");

    public FileDB(File directory) {
	this.directory = directory;
    }

    public void buildDB() {
	addFilesToDirectory(directory);
    }

    private void addFilesToDirectory(File directory) {
	for (File file : directory.listFiles()) {
	    if (isValidDirectory(file)) {
		addFilesToDirectory(file);
	    } else if (isValidFile(file)) {
		db.put(db.size(), new FileEntry(db.size(), file.getAbsoluteFile()));
	    }
	}
    }

    private boolean isValidFile(File file) {
	return file.isFile() && !invalidFileNames.contains(file.getName()) && !file.getName().startsWith(".");
    }

    private boolean isValidDirectory(File file) {
	return file.isDirectory() && !invalidDirectories.contains(file.getName()) && !file.getName().startsWith(".");
    }

    public int findTotalFiles() {
	return db.size();
    }

    public FileEntry findEntry(File file) {
	for (FileEntry entry : db.values()) {
	    if (entry.hasFile(file)) {
		return entry;
	    }
	}
	return null;
    }

    public List<FileEntry> findFilesInSubDir(String subdir) {
	List<FileEntry> subsetFromDir = new ArrayList<FileEntry>();
	for (FileEntry entry : db.values()) {
	    if (entry.containsText(SEPARATOR + subdir + SEPARATOR)) {
		subsetFromDir.add(entry);
	    }
	}
	return subsetFromDir;
    }

    public List<FileEntry> getRandomFiles(final int number) {
	List<FileEntry> randomFiles = new ArrayList<FileEntry>();
	Random randomGenerator = new Random();
	while (number <= db.size() && randomFiles.size() < number) {
	    Integer index = randomGenerator.nextInt(db.size());
	    FileEntry entry = db.get(index);
	    if (!randomFiles.contains(entry)) {
		randomFiles.add(entry);
	    }
	}
	return randomFiles;
    }

}