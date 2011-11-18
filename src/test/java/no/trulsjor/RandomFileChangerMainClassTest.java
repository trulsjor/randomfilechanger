package no.trulsjor;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import no.trulsjor.randomfilechanger.RandomFileChanger;

import org.junit.Test;

public class RandomFileChangerMainClassTest {

	@Test
	public void shouldFailGracefullyWithNull() throws Exception {
		File mainoutput = redirectOutputToFile();
		RandomFileChanger.main(null);
		File disclaimer = redirectOutputToFile();
		RandomFileChanger.printDisclaimer();
		assertFileEquals(readFile(disclaimer), readFile(mainoutput));

		cleanup(disclaimer);
		cleanup(mainoutput);
	}

	@Test
	public void shouldFailGracefullyWithoutArguments() throws Exception {
		File mainoutput = redirectOutputToFile();
		RandomFileChanger.main(new String[] { "" });
		File disclaimer = redirectOutputToFile();
		RandomFileChanger.printDisclaimer();
		assertFileEquals(readFile(disclaimer), readFile(mainoutput));

		cleanup(disclaimer);
		cleanup(mainoutput);
	}

	@Test
	public void shouldFailGracefullyWithInvalidArguments() throws Exception {
		File mainoutput = redirectOutputToFile();
		RandomFileChanger.main(new String[] { "invalidstring" });
		File disclaimer = redirectOutputToFile();
		RandomFileChanger.printDisclaimer();
		assertFileEquals(readFile(disclaimer), readFile(mainoutput));

		cleanup(disclaimer);
		cleanup(mainoutput);

	}

	@Test
	public void shouldFailGracefullyWithInvalidArguments2() throws Exception {
		File mainoutput = redirectOutputToFile();
		RandomFileChanger.main(new String[] { "invalid", "string" });
		File disclaimer = redirectOutputToFile();
		RandomFileChanger.printDisclaimer();
		assertFileEquals(readFile(disclaimer), readFile(mainoutput));

		cleanup(disclaimer);
		cleanup(mainoutput);
	}

	@Test
	public void shouldFailGracefullyWithMoreThan2arguments() throws Exception {
		File mainoutput = redirectOutputToFile();
		RandomFileChanger.main(new String[] { "--test", "1", "d" });
		File disclaimer = redirectOutputToFile();
		RandomFileChanger.printDisclaimer();
		assertFileEquals(readFile(disclaimer), readFile(mainoutput));

		cleanup(disclaimer);
		cleanup(mainoutput);

	}

	@Test
	public void shouldFailGracefullyWithInvalidMode() throws Exception {
		File mainoutput = redirectOutputToFile();
		RandomFileChanger.main(new String[] { "--invalid", "1" });
		File disclaimer = redirectOutputToFile();
		RandomFileChanger.printDisclaimer();
		assertFileEquals(readFile(disclaimer), readFile(mainoutput));

		cleanup(disclaimer);
		cleanup(mainoutput);

	}

	@Test
	public void shouldListCorrectNumberOfFilesInTest() throws Exception {
		File mainoutput = redirectOutputToFile();
		RandomFileChanger.main(new String[] { "--test", "2" });
		List<String> outputLines = readFile(mainoutput);
		assertEquals(2, outputLines.size());
		cleanup(mainoutput);

	}

	@Test
	public void shouldListCorrectNumberOfFilesInChange() throws Exception {
		File mainoutput = redirectOutputToFile();
		RandomFileChanger.main(new String[] { "--change", "0" });
		List<String> outputLines = readFile(mainoutput);
		assertEquals(0, outputLines.size());
		cleanup(mainoutput);
	}

	private static void assertFileEquals(List<String> file1, List<String> file2) {
		assertEquals(file1.size(), file2.size());
		for (int i = 0; i < file1.size(); i++) {
			assertEquals(file1.get(i), file2.get(i));
		}
	}

	private List<String> readFile(File file) throws IOException {
		List<String> buffer = new ArrayList<String>();
		Scanner scanner = new Scanner(file);

		try {
			while (scanner.hasNextLine()) {
				buffer.add(scanner.nextLine());
			}
			return buffer;
		} finally {
			scanner.close();
		}
	}

	private PrintStream systemout;

	public File redirectOutputToFile() throws IOException {
		File tmp = File.createTempFile("out", "tmp");
		systemout = System.out;
		PrintStream printStream = new PrintStream(new FileOutputStream(tmp));
		System.setOut(printStream);
		return tmp;
	}

	public void cleanup(File tmp) {
		tmp.deleteOnExit();
		System.setOut(systemout);
	}

}
