package portfolio.iface;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import auth.api.WrongSecretException;
import bank.api.BankManager;
import bank.api.DoesNotHaveThisAssetException;
import bank.api.InternalServerErrorException;
import exchange.api.ExchangeManager;
import exchange.api.InternalExchangeErrorException;
import exchange.api.NoSuchAccountException;
import portfolio.logic.Account;
import portfolio.logic.Portfolio;

public class PortfolioApp {
	private Account account;
	private BankManager bm;
	private ExchangeManager em;

	public PortfolioApp(ExchangeManager em, BankManager bm)
			throws WrongSecretException, DoesNotHaveThisAssetException, InternalServerErrorException {
		try {
			this.em = em;
			this.bm = bm;
			account = new Account(bm, em);
		} catch (FileNotFoundException e) {
			System.out.println("No current account found. Created a new one.");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void gettingPersonalInformation() throws RemoteException, WrongSecretException, InternalServerErrorException,
			NoSuchAccountException, InternalExchangeErrorException {
		try {
			updateAccount();
		} catch (DoesNotHaveThisAssetException e) {
			e.printStackTrace();
		}
		System.out.println("Personal information:");
		System.out.println(account.getPortfolio().toString());
	}

	public void updateAccount() throws RemoteException, WrongSecretException, InternalServerErrorException,
			NoSuchAccountException, InternalExchangeErrorException, DoesNotHaveThisAssetException {
		System.out.println("Getting up-to-date information...");
		Set<String> currentSecurities = getMySecurity();
		double v = calcValue(currentSecurities);
		account.getPortfolio().setValue(v + account.getCredit());
		Portfolio.setBalance(account.getCredit());
		double yield = (v - Portfolio.balance) / Portfolio.balance;
		if (!Double.isNaN(yield))
			account.getPortfolio().setYield(yield);
		else
			account.getPortfolio().setYield(0);
	}

	public void save() throws FileNotFoundException {
		account.save();
	}

	public Account getAccount() {
		return account;
	}

	private double calcValue(Set<String> currentSecurities)
			throws RemoteException, NoSuchAccountException, WrongSecretException, InternalExchangeErrorException {
		double value = 0;
		int amount = 0;
		Object[] cs = currentSecurities.toArray();
		for (int i = 0; i < currentSecurities.size(); i++) {
			String security = (String) cs[i];
			if (!security.equals("NIS")) {
				amount = account.getPortfolio().getDeedAmount(security);
				if (amount > 0)
					value += (priceOfSecurity(account.getSecret(), account.getAccountId(), security) * amount);
			}
		}
		return value;
	}

	private Set<String> getMySecurity() throws RemoteException, WrongSecretException, InternalServerErrorException,
			NoSuchAccountException, InternalExchangeErrorException {
		Integer accountId = account.getAccountId();
		String secret = account.getSecret();
		Set<String> currentAssets = bm.getAssets(secret, accountId);
		currentAssets.addAll(em.getAssets(secret, accountId));
		return currentAssets;
	}

	private double priceOfSecurity(String secret, int accountId, String assetName)
			throws RemoteException, NoSuchAccountException, WrongSecretException, InternalExchangeErrorException {
		int sumPrices = 0, assetAmount = 0;
		Map<Integer, Integer> assets = em.getSupply(assetName);
		for (Entry<Integer, Integer> asset : assets.entrySet()) {
			sumPrices += (asset.getValue() * asset.getKey()); // Sums all the different prices for all the offers
			assetAmount += asset.getValue();
		}
		return (sumPrices / assetAmount);
	}

}
