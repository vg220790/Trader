package portfolio.storage;

import java.io.PrintStream;
import java.util.Scanner;
 
public class SecurityInfo {
	private String name;
	private Type type;
	
	public SecurityInfo(Scanner scanner) {
		setName(scanner.next());
		int t = Integer.parseInt(scanner.next());
		if(t == 0)
			type = Type.BOND;
		else
			type = Type.STOCK;
	}

	public SecurityInfo() {}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public void writeInto(PrintStream out) {
		out.print(getName() + " ");
		out.print(getType().ordinal() + " ");
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
