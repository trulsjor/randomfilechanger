package no.trulsjor.randomfilechanger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.DateFormatSymbols;
import java.util.List;
import java.util.Locale;

import no.trulsjor.statistics.RelativeStandardDeviationCalculator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LoddtrekkerTest {

    private static final String SIMPLE_DIR = "loddtrekning";

    @Before
    public void createTestDirectory() throws Exception {
	File root = new File(SIMPLE_DIR);
	root.mkdir();
	File meetings = new File(root, "fagmoter");
	meetings.mkdir();
	Locale loc = new Locale("no", "no");
	String[] months = new DateFormatSymbols(loc).getMonths();
	for (int i = 0; i < 12; i++) {
	    File dir = new File(meetings, i + 1 + "-" + months[i]);
	    dir.mkdir();
	    for (int j = 1; j < 5; j++) {
		File file = new File(dir, "bidrag" + j + ".txt");
		file.createNewFile();
		BufferedWriter out = new BufferedWriter(new FileWriter(file));
		out.write("hvem: x");
		out.newLine();
		out.write("hva: y");
		out.close();
	    }
	}
    }

  
    @Test
    public void shouldPickOne() throws Exception {
	RandomFileChanger fileChanger = new RandomFileChanger(new File(SIMPLE_DIR));
	List<FileEntry> randomFiles = fileChanger.getRandomFiles(1);
	assertEquals(1, randomFiles.size());

    }

    @Test
    public void shouldPickUniform() throws Exception {
	RandomFileChanger fileChanger = new RandomFileChanger(new File(SIMPLE_DIR));
	int[] numbers = new int[fileChanger.findTotalFiles()];
	for (int i = 0; i < 1000000; i++) {
	    FileEntry entry = fileChanger.getRandomFile();
	    numbers[entry.getIndex()]++;
	}

	double relativeStandardDeviation = RelativeStandardDeviationCalculator.calculate(numbers);
	assertTrue(relativeStandardDeviation < 0.01); // less than one percent
    }
   
    
    @Test
    public void shouldListAllReal(){
	RandomFileChanger fileChanger = new RandomFileChanger(new File("loddtrekning_julemote"));
	List<FileEntry> list = fileChanger.getAllFiles();
	assertFalse(list.isEmpty());
	//fileChanger.printSpeakers(list);
    }

    @After
    public void deleteSimpleDir() throws Exception {
	assertTrue(deleteDirectory(new File(SIMPLE_DIR)));
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
