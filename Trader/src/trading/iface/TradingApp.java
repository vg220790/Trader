package trading.iface;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import auth.api.WrongSecretException;
import bank.api.BankManager;
import bank.api.DoesNotHaveThisAssetException;
import bank.api.InternalServerErrorException;
import bank.api.NotEnoughAssetException;
import exchange.api.DoesNotHaveThisStockException;
import exchange.api.ExchangeManager;
import exchange.api.InternalExchangeErrorException;
import exchange.api.NoSuchAccountException;
import exchange.api.NotEnoughStockException;
import exchange.api.StockNotTradedException;
import portfolio.logic.Portfolio;
import trading.logic.Account;
import trading.logic.Command;

public class TradingApp {
	private ExchangeManager em;
	private BankManager bm;
	private final String currency = "NIS";
	private final int seAcountId = 3373; // Id of the stock exchange itself.
											// Used to bid/ask.
	private Account account;

	public TradingApp(ExchangeManager em, BankManager bm, portfolio.logic.Account portfolioAccount)
			throws MalformedURLException, RemoteException, NotBoundException {
		this.em = em;
		this.bm = bm;
		try {
			account = new Account(bm, em, portfolioAccount);
		} catch (FileNotFoundException e) {
			System.out.println("Command history file not found");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void buyingSecurity(Scanner s) throws Exception {
		showAvailable();
		s.nextLine();
		String stockName = getNameStock(s);
		int amount = getAmount(s);
		s.nextLine();
		int price = getPrice(s);
		s.nextLine();
		if (validationRequest(stockName, price, amount, s)) {
			try {
				checkenoughmoney(price);
				account.getPlatform().BuySecurities(account.getSecret(), account.getAccountId(), stockName, amount,
						price);
			} catch (Exception e) {
				System.out.println("The system can't execute the command.");
				throw e;
			}
			System.out.print("The system executed your comand.\n your command:");
			getLastCommands(1).get(0).print();
			Portfolio.setBalance(price);
		} else {
			System.out.println("No transaction executed.");
		}

	}

	public void sellingSecurity(Scanner s)
			throws RemoteException, WrongSecretException, NoSuchAccountException, NotEnoughStockException,
			StockNotTradedException, DoesNotHaveThisStockException, InternalExchangeErrorException,
			DoesNotHaveThisAssetException, InternalServerErrorException, NotEnoughAssetException {
		s.nextLine();
		String stockName = getNameStock(s);
		int amount = getAmount(s);
		s.nextLine();
		int price = getPrice(s);
		s.nextLine();
		int currentAmount = 0;

		if (validationRequest(stockName, price, amount, s)) {

			// if user approved he wants to sell we check if we have enough
			// stocks in Bank
			currentAmount = bm.getQuantityOfAsset(account.getSecret(), account.getAccountId(), stockName);

			if (currentAmount >= amount) {  // if there are enough stocks in
											// bank we try to sell them
				try {
					bm.transferAssets(account.getSecret(), account.getAccountId(), seAcountId, stockName, amount);
					account.getPlatform().SellSecurities(account.getSecret(), account.getAccountId(), stockName, amount,
							price);
					System.out.println("The system executed your comand.\n your command:");
					getLastCommands(1).get(0).print();
					Portfolio.setBalance(-price);
				} catch (NotEnoughStockException e) {
					System.out.println("The system can't execute the command. Not enough stocks owned.");
					e.printStackTrace();
				}
				 catch (Exception e) {
					System.out.println("The system can't execute the command.");
					e.printStackTrace();
				}
			}
		}else
			System.out.println("Command was aborted.");
	}

	public List<Command> getLastCommands(int numOfCommands) {
		List<Command> lastXCommands = account.getPlatform().getLastCommands(numOfCommands);
		return lastXCommands;
	}

	public void save() throws FileNotFoundException {
		account.save();
	}

	public void showHistogram() {
		System.out.println("History of commands:");
		account.getHistory().Print();
	}

	private boolean checkenoughmoney(int price)
			throws RemoteException, NoSuchAccountException, WrongSecretException, InternalExchangeErrorException,
			DoesNotHaveThisAssetException, InternalServerErrorException, NotEnoughAssetException {
		int currentMoney = em.getAmountOfAsset(account.getSecret(), account.getAccountId(), currency);
		if (currentMoney >= price)
			return true;
		int allMyMoney = currentMoney + bm.getQuantityOfAsset(account.getSecret(), account.getAccountId(), currency);
		if (allMyMoney >= price) {
			int moneyForTransfer = price - currentMoney;
			bm.transferAssets(account.getSecret(), account.getAccountId(), seAcountId, currency, moneyForTransfer);
			return true;
		}
		return false;
	}

	private boolean validationRequest(String stockName, int price, int amount, Scanner s) {
		System.out.println("Security details:" + stockName + "	price:" + price + "	amount:" + amount);
		return checkPress(s);
	}

	private String getNameStock(Scanner s) {
		System.out.println("Enter stock name:");
		return s.nextLine();
	}

	private int getAmount(Scanner s) {
		System.out.println("Enter amount:");
		return s.nextInt();
	}

	private int getPrice(Scanner s) {
		System.out.println("Entere price for stock:");
		return s.nextInt();
	}

	private boolean checkPress(Scanner s) {
		char press;
		do {
			System.out.print("Enter 'y' to confirm or 'n' for disapproval:");
			press = s.next().charAt(0);
			System.out.println("");
			if (press == 'y' || press == 'Y')
				return true;
			else if (press == 'n' || press == 'N')
				return false;
			else
				System.out.println("invalid input.\n press 'n' or 'y'.");

		} while (press != 'y' || press != 'Y' || press != 'n' || press != 'N');
		return false;
	}

	private void showAvailable() throws RemoteException {
		for (String stockName : account.getPlatform().getExchange().getStockNames()) {
			System.out.println("Supply of stock " + stockName);
			System.out.println("Price\tAmount");
			Map<Integer, Integer> supply = account.getPlatform().getExchange().getSupply(stockName);
			for (Map.Entry<Integer, Integer> quote : supply.entrySet()) {
				System.out.println(quote.getKey() + "\t" + quote.getValue());
			}
		}
		System.out.println();
	}
}
