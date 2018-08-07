package portfolio.logic;

import portfolio.storage.DeedInfo;
import portfolio.storage.PortfolioInfo;
import portfolio.storage.Type;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class Portfolio {

	private double yield;
	private double value;
	public static double balance;
	private List<Deed> TitleDeeds = new ArrayList<Deed>();

	public Portfolio(double yield, double value, double overallProfit)
			throws MalformedURLException, RemoteException, NotBoundException {
		setBalance(overallProfit);
		setValue(value);
		setYield(yield);
	}

	public Portfolio() {
		setBalance(0);
		setValue(0);
		setYield(0);
	}

	public Portfolio(PortfolioInfo p) {
		if (!(p == null)) {
			setYield(p.getYield());
			setValue(p.getValue());
			setBalance(p.getBalance());
			for (DeedInfo di : p.getTitleDeedsInfo()) {
				TitleDeeds.add(new Deed(di));
			}
		} else
			new Portfolio();
	}

	public void addDeed(Deed deed) {
		for (int i = 0; i < getTitleDeeds().size(); i++) {
			if (getTitleDeeds().get(i).getSecurity().getName().equals(deed.getSecurity().getName())) {
				int index = TitleDeeds.indexOf(getTitleDeeds().get(i));
				TitleDeeds.get(index).setNumOfSec(deed.getNumOfSec() + TitleDeeds.get(index).getNumOfSec());
				return;
			} 
		}
		deed.getSecurity().setType(Type.STOCK);
		TitleDeeds.add(deed);
		return;
	}

	public void removeDeed(Deed deed) {
		for (int i = 0; i < getTitleDeeds().size(); i++) {
			if (getTitleDeeds().get(i).getSecurity().getName().equals(deed.getSecurity().getName())) {
				int index = TitleDeeds.indexOf(getTitleDeeds().get(i));
				TitleDeeds.get(index).setNumOfSec(TitleDeeds.get(index).getNumOfSec() - deed.getNumOfSec());
				return;
			} 
		}
		TitleDeeds.remove(deed);
		return;
	}

	public int getDeedAmount(String name) {
		for (int i = 0; i < TitleDeeds.size(); i++) {
			if (TitleDeeds.get(i).getSecurity().getName().equals(name)) {
				return TitleDeeds.get(i).getNumOfSec();
			}
		}
		return 0;

	}

	public double getYield() {
		return yield;
	}

	public void setYield(double yield) {
		this.yield = yield;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public static void setBalance(double price) {
		Portfolio.balance += price;
	}

	public double getBalance() {
		return balance;
	}

	public List<Deed> getTitleDeeds() {
		return TitleDeeds;
	}

	public void setTitleDeeds(List<Deed> titleDeeds) {
		TitleDeeds = titleDeeds;
	}

	@Override
	public String toString() {
		return "Portfolio details:\nYield:\t" + yield + "\nValue:\t" + value + "\nBalance:" + balance
				+ "\nTitle Deeds:\n" + getTitleDeedsPrint() + "\n";
	}

	private String getTitleDeedsPrint() {
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < TitleDeeds.size(); i++) {
			s.append(getTitleDeeds().get(i).toString());
		}
		return s + "";
	}

	public PortfolioInfo returnPortfolioInfo() {
		PortfolioInfo pi = new PortfolioInfo();
		pi.setYield(getYield());
		pi.setValue(getValue());
		pi.setYield(getBalance());
		for (Deed d : getTitleDeeds()) {
			DeedInfo di = d.returnDeedInfo();
			pi.getTitleDeedsInfo().add(di);
		}
		return pi;
	}
}
