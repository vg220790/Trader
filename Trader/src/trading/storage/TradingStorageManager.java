package trading.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class TradingStorageManager {

	private File file;
	private String fileName = "trading.txt";

	public TradingStorageManager() throws IOException {
		file = new File(fileName);
		if (!file.exists())
			Files.createFile(Paths.get(fileName));
	}

	public HistogramInfo load() throws FileNotFoundException {
		if (!(file.length() == 0)) {
			Scanner scanner = new Scanner(new FileInputStream(file));
			HistogramInfo hi = new HistogramInfo(scanner);
			scanner.close();
			return hi;
		}
		else
			return null;
	}

	public void store(HistogramInfo hi) throws FileNotFoundException {
		file.delete();
		file = new File(fileName);
		PrintStream out = new PrintStream(file);
		hi.store(out);
	}

}
