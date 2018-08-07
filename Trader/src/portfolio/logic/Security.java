package portfolio.logic;

import java.util.Scanner;

import portfolio.storage.SecurityInfo;
import portfolio.storage.Type;

public class Security {
	private String name;
	private Type type;
	
	public Security(String name){
		setName(name);
	}
	
	private void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public Security(Scanner scanner) {
		name = scanner.next();
	}

	public Security(SecurityInfo s) {
		setName(s.getName());
		setType(s.getType());
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public SecurityInfo returnSecurityInfo() {
		SecurityInfo si = new SecurityInfo();
		si.setName(getName());
		si.setType(getType());
		return si;
	}

	@Override
	public String toString() {
		return name + ", Type: " + type + " ";
	}

	
}

