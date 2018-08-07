package trading.logic;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
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
import portfolio.logic.Deed;
import portfolio.logic.Security;
import trading.storage.Type;

public class Platform {

	private Histogram histogram;
	private ExchangeManager em;
	private BankManager bm;
	private portfolio.logic.Account portfolioAccount;

	public Platform(ExchangeManager em, BankManager bm, String secret, int id, portfolio.logic.Account portfolioAccount, Histogram histogram)
			throws MalformedURLException, RemoteException, NotBoundException {
		this.histogram = histogram;
		this.em = em;
		this.bm = bm;
		this.portfolioAccount = portfolioAccount;
	}

	public List<Command> getLastCommands(int numOfCommands) {
		return histogram.getLastXCommands(numOfCommands);
	}

	public int BuySecurities(String secret, int accountId, String stockName, int amount, int bidPrice)
			throws RemoteException, WrongSecretException, NoSuchAccountException, NotEnoughStockException,
			StockNotTradedException, InternalExchangeErrorException, DoesNotHaveThisAssetException,
			InternalServerErrorException, NotEnoughAssetException {
			Command c;
			int commandId = em.placeBid(secret, accountId, stockName, amount, bidPrice);
			if (commandId > 0) {
				c = new Command(stockName, amount, commandId, true, Type.BID);
				histogram.add(c);
				portfolioAccount.addDeed(new Deed(new Security(stockName), amount));	
			return commandId;
			}
		return (-1);
	}

	public int SellSecurities(String secret, int accountId, String stockName, int amount, int askPrice)
			throws RemoteException, WrongSecretException, NoSuchAccountException, StockNotTradedException,
			InternalExchangeErrorException, InternalServerErrorException, NotEnoughStockException,
			DoesNotHaveThisStockException {
		Command c;
		int commandId = em.placeAsk(secret, accountId, stockName, amount, askPrice);
		if (commandId > 0) {
			c = new Command(stockName, amount, commandId, false, Type.ASK);
			histogram.add(c);
			portfolioAccount.removeDeed(new Deed(new Security(stockName), amount));
		}
		return commandId;
	}


	public boolean enoughSecurities(String secret, int accountId, int amountSecuirites, String assetName)
			throws RemoteException, WrongSecretException, DoesNotHaveThisAssetException, InternalServerErrorException {
		if (bm.getQuantityOfAsset(secret, accountId, assetName) <= amountSecuirites)
			return true;
		else
			return false;
	}

	public ExchangeManager getExchange() {
		return em;
	}
}