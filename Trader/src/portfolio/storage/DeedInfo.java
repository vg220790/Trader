package portfolio.storage;

import java.io.PrintStream;
import java.util.Scanner;

public class DeedInfo {

	SecurityInfo security;
	int numOfSec;
	
	public DeedInfo(Scanner scanner) {
		security = new SecurityInfo(scanner);
		numOfSec = Integer.parseInt(scanner.next());
	}

	public DeedInfo() {}

	public SecurityInfo getSecurity() {
		return security;
	}

	public void setSecurity(SecurityInfo security) {
		this.security = security;
	}

	public int getNumOfSec() {
		return numOfSec;
	}

	public void setNumOfSec(int numOfSec) {
		this.numOfSec = numOfSec;
	}

	public void writeInto(PrintStream out) {
		security.writeInto(out);
		out.print(numOfSec);
	}
}
