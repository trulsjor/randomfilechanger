package no.trulsjor;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.imageio.stream.FileCacheImageInputStream;

import no.trulsjor.randomfilechanger.FileEntry;
import no.trulsjor.randomfilechanger.RandomFileChanger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.sun.corba.se.impl.interceptors.PICurrent;

public class LoddtrekkerTest {

	private static final String SIMPLE_DIR = "loddtrekning";
	private static final String SEPARATOR = System.getProperty("file.separator");

	@Test
	public void testname() throws Exception {
		assertTrue(true);
	}

	@Before
	public void createDirectory() throws Exception {
		File root = new File(SIMPLE_DIR);
		root.mkdir();
		File meetings = new File(root, "meetings");
		meetings.mkdir();
		Locale loc = new Locale("no", "no");
		String[] months = new DateFormatSymbols(loc).getMonths();
		for (int i = 0; i < 12; i++) {
			File dir = new File(meetings, i + 1 + "-" + months[i]);
			dir.mkdir();
			for (int j = 1; j < 5; j++) {
				File file = new File(dir, "meeting" + j);
				file.createNewFile();
				BufferedWriter out = new BufferedWriter(new FileWriter(file));
				out.write("Congratulations!");
				out.newLine();
				out.write("" + dir.getName());
				out.newLine();
				out.write("" + file.getName());
				out.close();
			}
		}
	}

	@Test
	public void testdelete() throws Exception {
		deleteDirectory(new File(SIMPLE_DIR));
	}

	@Test
	public void shouldPickOne() throws Exception {
		RandomFileChanger fileChanger = new RandomFileChanger(new File(SIMPLE_DIR));
		List<FileEntry> randomFiles = fileChanger.getRandomFiles(1);
		fileChanger.printEntries(randomFiles);
		assertEquals(1, randomFiles.size());

	}

	@Test
	public void shouldPickUniform() throws Exception {
		RandomFileChanger fileChanger = new RandomFileChanger(new File(SIMPLE_DIR));
		int[] indexes = new int[fileChanger.findTotalFiles()];
		for (int i = 0; i < 1000000; i++) {
			FileEntry entry = fileChanger.getRandomFile();
			indexes[entry.getIndex()]++;
		}
		for (int i = 0; i < indexes.length; i++) {
			System.out.println(i + " : " + indexes[i]);
		}
	}

	public void deleteTestFileDirectory() throws Exception {
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
		return path.delete();
	}

}
