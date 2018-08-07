package portfolio.logic;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.NotBoundException;
import auth.api.WrongSecretException;
import bank.api.BankManager;
import bank.api.DoesNotHaveThisAssetException;
import bank.api.InternalServerErrorException;
import exchange.api.ExchangeManager;
import exchange.api.InternalExchangeErrorException;
import exchange.api.NoSuchAccountException;
import portfolio.storage.PortfolioInfo;
import portfolio.storage.PortfolioStorageManager;

public class Account {
	private Integer accountId = 206;
	private String secret = "fY8mmK";
	private Portfolio portfolio;
	private PortfolioStorageManager storage;
	private BankManager bm;
	private ExchangeManager em;
	private double credit;

	public Account(BankManager bm, ExchangeManager em) throws IOException, NotBoundException, WrongSecretException,
			DoesNotHaveThisAssetException, InternalServerErrorException {
		portfolio = new Portfolio();
		storage = new PortfolioStorageManager();
		this.setBm(bm);
		this.setEm(em);
		try {
			loadPortfolio();
		} catch (FileNotFoundException e) {
			throw new FileNotFoundException();
		}
		try {
			setCredit(bm.getQuantityOfAsset(secret, accountId, "NIS") + em.getAmountOfAsset(secret, accountId, "NIS"));
		} catch (NoSuchAccountException e) {
			e.printStackTrace();
		} catch (InternalExchangeErrorException e) {
			e.printStackTrace();
		}
	}

	private void loadPortfolio() throws FileNotFoundException {
		PortfolioInfo p = storage.load();
		portfolio = new Portfolio(p);
	}
	
	public Integer getAccountId() {
		return accountId;
	}

	public void addDeed(Deed deed) {
		getPortfolio().addDeed(deed);
	}

	public void removeDeed(Deed deed) {
		getPortfolio().removeDeed(deed);
	}

	public Portfolio getPortfolio() {
		return portfolio;
	}

	public void save() throws FileNotFoundException {
		storage.store(getPortfolio().returnPortfolioInfo());
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getSecret() {
		return secret;
	}

	public double getCredit() {
		return credit;
	}

	public void setCredit(double credit) {
		this.credit = credit;
	}

	public BankManager getBm() {
		return bm;
	}

	public void setBm(BankManager bm) {
		this.bm = bm;
	}

	public ExchangeManager getEm() {
		return em;
	}

	public void setEm(ExchangeManager em) {
		this.em = em;
	}

	@Override
	public String toString() {
		return "Account Information\n accountId:\t" + accountId + "\nsecret" + secret + "\nPortfolio:" + portfolio + "\nStorage:"
				+ storage + "\nCredit:" + credit;
	}

	
}
