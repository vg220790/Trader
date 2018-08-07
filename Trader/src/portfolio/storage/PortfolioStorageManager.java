package portfolio.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class PortfolioStorageManager  {
	
	private File file;
	private String fileName = "portfolio.txt";

	public PortfolioStorageManager() throws IOException{
		file = new File(fileName);
		if (!file.exists())
			Files.createFile(Paths.get(fileName));		
	}

	public PortfolioInfo load() throws FileNotFoundException {
		if(!(file.length() == 0)) {
			PortfolioInfo pi;
			Scanner scanner = new Scanner(new FileInputStream(file));
			pi = new PortfolioInfo(scanner);
			scanner.close();
			return pi;
		}
		else
			return null;
	}
	
	public void store(PortfolioInfo pi) throws FileNotFoundException {
		file.delete();
		file = new File(fileName);
		PrintStream out = new PrintStream(file);
		pi.store(out);
	}
	
	public boolean ifExists() {
		return file.exists();
	}
	
	
}
