package trading.logic;

import trading.storage.CommandInfo;
import trading.storage.Type;

public class Command {

	private String stockName;
	private int amount;
	private boolean complete;
	private int id;
	private Type type;

	public Command(String stockName, int amount, int id, boolean complete, Type type) {
		setStockName(stockName);
		setAmount(amount);
		setComplete(complete);
		setId(id);
		setType(type);
	}

	public Command(CommandInfo ci) {
		setStockName(ci.getStockName());
		setAmount(ci.getAmount());
		setComplete(ci.isComplete());
		setId(ci.getId());
		setType(ci.getType());
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	private void setComplete(boolean complete) {
		this.complete = complete;
	}

	public void print() {
		System.out.println("Id:" + id + "\t" + "Stock name: " + stockName + "\t" + "Amount: " + amount);
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

}