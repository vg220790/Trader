package portfolio.storage;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PortfolioInfo {

	private double yield;
	private double value;
	private double balance;
	private List<DeedInfo> DeedsInfo = new ArrayList<DeedInfo>();
	
	public PortfolioInfo(Scanner scanner) {
		this.yield = Double.parseDouble(scanner.nextLine());
		this.value = Double.parseDouble(scanner.nextLine());
		this.balance = Double.parseDouble(scanner.nextLine());
		while (scanner.hasNext()) {
			DeedInfo di = new DeedInfo(scanner);
			scanner.nextLine();
			this.DeedsInfo.add(di);
		}
	}

	public PortfolioInfo() {}

	public void store(PrintStream out) {
		out.println(getYield());
		out.println(getValue());
		out.println(getBalance());
		for(DeedInfo dInfo : getTitleDeedsInfo()) {
			dInfo.writeInto(out);
			out.println();
		}
	}
	public double getYield() {
		return yield;
	}

	public double getValue() {
		return value;
	}

	public double getBalance() {
		return balance;
	}

	public List<DeedInfo> getTitleDeedsInfo() {
		return DeedsInfo;
	}

	public void setTitleDeedsInfo(List<DeedInfo> titleDeedsInfo) {
		DeedsInfo = titleDeedsInfo;
	}

	public void setYield(double yield) {
		this.yield = yield;
	}

	public void setValue(double value) {
		this.value = value;	
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}
	
	
}
