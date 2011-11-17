package no.trulsjor.randomfilechanger;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class RandomFileChanger {

    private static final String MESSAGE = "//Hello automation at ";

    private enum Mode {
	ILLEGAL("--ilegal"), TEST("--test"), CHANGE("--change");

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
	case TEST:
	    fileEntries = randomFileChanger.getRandomFiles(Integer.parseInt(args[1]));
	    randomFileChanger.printEntries(fileEntries);
	    break;
	case CHANGE:
	    String message = MESSAGE + new Date(System.currentTimeMillis());
	    fileEntries = randomFileChanger.changeRandomFiles(Integer.parseInt(args[1]), message);
	    randomFileChanger.printEntries(fileEntries);
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
	if (args.length == 2) {
	    try {
		Integer.parseInt(args[1]);
	    } catch (NumberFormatException e) {
		return Mode.ILLEGAL;
	    }

	    String mode = args[0];
	    if (mode.equals(Mode.TEST.modeCommando)) {
		return Mode.TEST;
	    }

	    if (mode.equals(Mode.CHANGE.modeCommando)) {
		return Mode.CHANGE;
	    }
	}
	return Mode.ILLEGAL;

    }

    public static void printDisclaimer() {
	System.out.println("No supported mode: Supported modes are  " + "\n\t\t --test <number of entries> and"
		+ "\n\t\t --change <number of entries>");

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

}