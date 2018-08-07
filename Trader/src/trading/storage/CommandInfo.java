package trading.storage;

import java.io.PrintStream;
import java.util.Scanner;
import trading.storage.Type;

public class CommandInfo {
	
	private String stockName;
	private int amount;
	private boolean complete;
	private int id;
	private Type type;

	public CommandInfo(String stockName, int amount, boolean complete, int id, Type type) {
		setStockName(stockName);
		setAmount(amount);
		setComplete(complete);
		setId(id);
		setType(type);
	}

	public CommandInfo(Scanner scanner) {
		setStockName(scanner.next());
		setAmount(Integer.parseInt(scanner.next()));
		setComplete(Boolean.parseBoolean(scanner.next()));
		setId(Integer.parseInt(scanner.next()));
		int t = Integer.parseInt(scanner.next());
		if(t == 0)
			type = Type.BID;
		else
			type = Type.ASK;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public boolean isBid() {
		if(type == Type.BID)
			return true;
		return false;
	}

	public String getStockName() {
		return stockName;
	}

	public void setStockName(String stockName) {
		this.stockName = stockName;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public boolean isComplete() {
		return complete;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void writeInto(PrintStream out) {
		out.print(stockName + " ");
		out.print(amount + " ");
		out.print(complete + " ");
		out.print(id + " ");
		out.print(type.ordinal());
	}

}
