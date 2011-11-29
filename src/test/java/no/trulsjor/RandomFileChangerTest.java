package no.trulsjor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import no.trulsjor.randomfilechanger.FileEntry;
import no.trulsjor.randomfilechanger.RandomFileChanger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RandomFileChangerTest {

	private static final String SIMPLE_DIR = "testfiledirectory";
	private static final String SEPARATOR = System.getProperty("file.separator");

	@Test
	public void shouldReadNumberOfFilesInDirectory() throws Exception {
		RandomFileChanger fileChanger = new RandomFileChanger(new File(SIMPLE_DIR));
		assertEquals(8, fileChanger.findTotalFiles());
	}

	@Test
	public void shouldFindREADME() throws Exception {
		RandomFileChanger fileChanger = new RandomFileChanger(new File(SIMPLE_DIR));
		assertNotNull(fileChanger.findFile(SIMPLE_DIR + SEPARATOR + "README"));
	}

	@Test
	public void shouldIgnoredotGit() throws Exception {
		RandomFileChanger fileChanger = new RandomFileChanger(new File(SIMPLE_DIR));
		assertNull(fileChanger.findFile(SIMPLE_DIR + SEPARATOR + ".git"));
	}

	@Test
	public void shouldIgnoreGifFile() throws Exception {
		RandomFileChanger fileChanger = new RandomFileChanger(new File(SIMPLE_DIR));
		assertNull(fileChanger.findFile(SIMPLE_DIR + SEPARATOR + ".gif"));
	}
	
	@Test
	public void shouldIgnoredotSettings() throws Exception {
		RandomFileChanger fileChanger = new RandomFileChanger(new File(SIMPLE_DIR));
		assertNull(fileChanger.findFile(SIMPLE_DIR + SEPARATOR + ".settings"));
	}

	@Test
	public void shouldIgnoreTargetDirectory() throws Exception {
		RandomFileChanger fileChanger = new RandomFileChanger(new File(SIMPLE_DIR));
		assertEquals(0, fileChanger.findFilesInSubDir("target").size());
	}

	@Test
	public void shouldFindSubDirectory() throws Exception {
		RandomFileChanger fileChanger = new RandomFileChanger(new File(SIMPLE_DIR));
		assertEquals(3, fileChanger.findFilesInSubDir("subdir").size());
	}

	@Test
	public void shouldPick2RandomFiles() throws Exception {
		RandomFileChanger fileChanger = new RandomFileChanger(new File(SIMPLE_DIR));
		List<FileEntry> randomFiles = fileChanger.getRandomFiles(2);
		assertEquals(2, randomFiles.size());
	}

	@Test
	public void shouldNotPickAnyRandomFilesIfNumberIsLargerThanTotalFiles() throws Exception {
		RandomFileChanger fileChanger = new RandomFileChanger(new File(SIMPLE_DIR));
		int numberofFiles = fileChanger.findTotalFiles();
		List<FileEntry> randomFiles = fileChanger.getRandomFiles(numberofFiles + 1);
		assertEquals(0, randomFiles.size());
	}

	@Test
	public void shouldAllFilesIfNumberExcactlyTotalFiles() throws Exception {
		RandomFileChanger fileChanger = new RandomFileChanger(new File(SIMPLE_DIR));
		int numberofFiles = fileChanger.findTotalFiles();
		List<FileEntry> randomFiles = fileChanger.getRandomFiles(numberofFiles);
		assertEquals(numberofFiles, randomFiles.size());
	}

	@Test
	public void shouldChangeFiles() throws Exception {
		RandomFileChanger fileChanger = new RandomFileChanger(new File(SIMPLE_DIR));
		List<FileEntry> randomFiles = fileChanger.changeRandomFiles(3, "Hello automation");
		for (FileEntry fileEntry : randomFiles) {
			assertTrue(fileEntry.isTouched());
		}
	}

	
	@Before
	public void createTestFileDirectory() throws Exception{
		File root = new File(SIMPLE_DIR);
		root.mkdir();
		new File(root, "README").createNewFile();
		new File(root, ".git").createNewFile();
		File dir1 = new File(root, "dir1");
		dir1.mkdir();
		new File(dir1, "file1").createNewFile();
		new File(dir1, "file2").createNewFile();
		new File(dir1, "file3").createNewFile();
		new File(dir1, "file4").createNewFile();
		new File(dir1, "image.gif").createNewFile();
		
		File subdir = new File(dir1, "subdir");
		subdir.mkdir();
		new File(subdir, "file5").createNewFile();
		new File(subdir, "file6").createNewFile();
		new File(subdir, "file7").createNewFile();
		File target = new File(dir1, "target");
		target.mkdir();
		new File(target, "file8.class").createNewFile();
	}
	
	@After
	public void deleteTestFileDirectory() throws Exception{
		File root = new File(SIMPLE_DIR);
		assertTrue(deleteDirectory(root));	
	}
	private boolean deleteDirectory(File path) {
		if (path.exists()) {
	        File[] files = path.listFiles();
	        for (int i = 0; i < files.length; i++) {
	            if (files[i].isDirectory()) {
	                deleteDirectory(files[i]);
	            } else {
	                files[i].delete();
	            }
	        }
	    }
	    return (path.delete());
		
	}

	
}
