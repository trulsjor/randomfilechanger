package no.trulsjor.randomfilechanger;

import static no.trulsjor.randomfilechanger.SystemConstants.NEWLINE;
import static no.trulsjor.randomfilechanger.SystemConstants.TAB;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.TreeMap;

public class RandomFileChanger {

    private static final String MESSAGE = "//Hello automation at ";

    private enum Mode {
	ILLEGAL("--illegal"), PICK("--pick"), CHANGE("--change"), LIST("--list");

	private final String modeCommando;

	Mode(String modeCommando) {
	    this.modeCommando = modeCommando;
	}

    }

    private FileDB fileDB;

    public RandomFileChanger(File directory) {
	fileDB = new FileDB(directory);
	fileDB.buildDB();
    }

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
	RandomFileChanger randomFileChanger = new RandomFileChanger(new File("."));
	List<FileEntry> fileEntries;
	switch (validateArgs(args)) {
	case PICK:
	    fileEntries = randomFileChanger.getRandomFiles(Integer.parseInt(args[1]));
	    randomFileChanger.printEntries(fileEntries);
	    break;
	case CHANGE:
	    String message = MESSAGE + new Date(System.currentTimeMillis());
	    fileEntries = randomFileChanger.changeRandomFiles(Integer.parseInt(args[1]), message);
	    randomFileChanger.printEntries(fileEntries);
	    break;
	case LIST:
	    randomFileChanger.prettyPrintSpeakers(randomFileChanger.getAllFiles());
	    break;
	case ILLEGAL:
	default:
	    printDisclaimer();
	}
    }

    private static Mode validateArgs(String[] args) {
	if (args == null) {
	    return Mode.ILLEGAL;
	}
	if (args.length == 1){
	  if (args[0].equals(Mode.LIST.modeCommando)){
	      return Mode.LIST;
	  }
	}
	if (args.length == 2) {
	    try {
		Integer.parseInt(args[1]);
	    } catch (NumberFormatException e) {
		return Mode.ILLEGAL;
	    }

	    String mode = args[0];
	    if (mode.equals(Mode.PICK.modeCommando)) {
		return Mode.PICK;
	    }

	    if (mode.equals(Mode.CHANGE.modeCommando)) {
		return Mode.CHANGE;
	    }
	}
	return Mode.ILLEGAL;

    }

    public static void printDisclaimer() {
	System.out.println("No supported mode: Supported modes are "
		+ NEWLINE + TAB + " --list"
		+ NEWLINE + TAB + " --pick <number of entries> "
		+ NEWLINE + TAB + " --change <number of entries>");

    }

    public int findTotalFiles() {
	return fileDB.findTotalFiles();
    }

    public FileEntry findFile(String name) {
	return fileDB.findEntry(new File(name));

    }

    public List<FileEntry> findFilesInSubDir(String subdir) {
	return fileDB.findFilesInSubDir(subdir);
    }

    public List<FileEntry> getRandomFiles(int number) {
	return fileDB.getRandomFiles(number);
    }

    public FileEntry getRandomFile() {
	return fileDB.getRandomFiles(1).get(0);
    }

    public List<FileEntry> changeRandomFiles(int number, String message) {
	List<FileEntry> randomFiles = getRandomFiles(number);
	for (FileEntry fileEntry : randomFiles) {
	    fileEntry.writeChange(message);
	}
	return randomFiles;
    }

    public void printEntries(Collection<FileEntry> entries) {
	for (FileEntry fileEntry : entries) {
	    System.out.println(fileEntry);
	}
    }

    public void prettyPrintSpeakers(Collection<FileEntry> entries) {
	TreeMap<String, List<FileEntry>> activityMap = new TreeMap<String, List<FileEntry>>();

	for (FileEntry fileEntry : entries) {
	    if (activityMap.containsKey(fileEntry.getSpeaker())) {
		List<FileEntry> activityList = activityMap.get(fileEntry.getSpeaker());
		activityList.add(fileEntry);
		activityMap.put(fileEntry.getSpeaker(), activityList);
	    } else {
		List<FileEntry> activityList = new ArrayList<FileEntry>();
		activityList.add(fileEntry);
		activityMap.put(fileEntry.getSpeaker(), activityList);
	    }
	}

	
	for (String speaker : activityMap.keySet()) {
	    System.out.println(speaker + " (" + activityMap.get(speaker).size() + ")");
	    for (FileEntry fileEntry : activityMap.get(speaker)) {
		System.out.println(TAB + "- " + fileEntry.getSubject() + " (" + fileEntry.getContext() + ")");
	    }
	}
	
	System.out.println(NEWLINE + "Total: " + entries.size() + " entries");

    }

    public void printContents(FileEntry fileEntry) {
	Scanner s = new Scanner(fileEntry.getFileContent());
	while (s.hasNextLine()) {
	    System.out.println(TAB + s.nextLine());
	}

    }

    public List<FileEntry> getAllFiles() {
	return fileDB.getDB();
    }

}