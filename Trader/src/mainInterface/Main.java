package mainInterface;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
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
import trading.iface.TradingApp;
import portfolio.iface.PortfolioApp;
	
public class Main {
	static Scanner s = new Scanner(System.in);
	
	public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException, WrongSecretException, NoSuchAccountException, NotEnoughStockException, StockNotTradedException, DoesNotHaveThisStockException, InternalExchangeErrorException, DoesNotHaveThisAssetException, InternalServerErrorException, NotEnoughAssetException {
		
		showMenu();
		Integer code = selectOperation(s);
		ExchangeManager em = (ExchangeManager) Naming.lookup("rmi://13.59.120.241/Exchange");
		BankManager bm = (BankManager) Naming.lookup("rmi://13.59.120.241/Bank");
		PortfolioApp pApp = new PortfolioApp(em, bm);
		TradingApp tApp = new TradingApp(em, bm, pApp.getAccount());
		while (true) {
			switch (code) {
			case 1:
				try {
					tApp.buyingSecurity(s);
				} catch (RemoteException e) {
					System.out.println("Couldn't contact server. please try again later.");
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case 2:
				try {
				tApp.sellingSecurity(s);
				} catch (RemoteException e) {
					System.out.println("Couldn't contact server. please try again later.");
				}
				break;
			case 3:
				System.out.println("Press 1 for statistics , 2 for histogram , 3 for both.");
				int press = s.nextInt();
				if(press == 1)
					pApp.gettingPersonalInformation();
				else if(press == 2 ){
					tApp.showHistogram();
					}
				else if(press == 3){
					pApp.gettingPersonalInformation();
					tApp.showHistogram();
				}else{
					System.out.println("Illegal input. Choose a number between 1-3");
				}
					break;
			case 4:
				try {
					tApp.save();
				} catch (FileNotFoundException e) {
					System.out.println("Could not find histogram file to save to.");
					e.printStackTrace();
				}
				try {
					pApp.save();
				}catch(FileNotFoundException ex) {
					System.out.println("Could not find portfolio file to save to.");
					ex.printStackTrace();
				}
				System.out.println("Data saved. Goodbye");
				System.exit(0);
			default:
				System.out.println("Invalid choice.");
			}
			showMenu();
			code = s.nextInt();
		}
	}

	private static Integer selectOperation(Scanner s) {
		Integer code = s.nextInt();
		s.nextLine();
		while (code < 1 || code > 4) {
			System.out.println("Invalid choice. try again (1-4)");
			code = s.nextInt();
		}
		return code;
	}

	private static void showMenu() {
		System.out.println("Select one option:\n" + "1 - Buying Security\n" + "2 - Selling Security\n"
				+ "3 - Getting personal information\n" + "4 - EXIT");
	}

}
