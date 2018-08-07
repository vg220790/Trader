package trading.logic;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.NotBoundException;
import bank.api.BankManager;
import exchange.api.ExchangeManager;
import trading.storage.HistogramInfo;
import trading.storage.TradingStorageManager;

public class Account {
	private Integer accountId = 206;
	private String secret = "fY8mmK";
	private TradingStorageManager storage;
	private BankManager bm;
	private ExchangeManager em;
	private Platform platform;
	private Histogram history;

	public Account(BankManager bm, ExchangeManager em, portfolio.logic.Account portfolioAccount)
			throws NotBoundException, IOException {
		this.setBm(bm);
		this.setEm(em);
		storage = new TradingStorageManager();
		try {
			loadHistory();
		} catch (FileNotFoundException e) {
			throw new FileNotFoundException();
		}
		platform = new Platform(em, bm, secret, accountId, portfolioAccount, getHistory());
	}

	private void loadHistory() throws FileNotFoundException {
		HistogramInfo h = storage.load();
		setHistory(new Histogram(h));
	}

	public Integer getAccountId() {
		return accountId;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public TradingStorageManager getStorage() {
		return storage;
	}

	public void setStorage(TradingStorageManager storage) {
		this.storage = storage;
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

	public Platform getPlatform() {
		return platform;
	}

	public void setPlatform(Platform platform) {
		this.platform = platform;
	}

	public Histogram getHistory() {
		return history;
	}

	public void setHistory(Histogram history) {
		this.history = history;
	}

	public void save() throws FileNotFoundException {
		storage.store(getHistory().returnHistogramInfo());
	}
}
