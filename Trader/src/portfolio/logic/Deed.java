package portfolio.logic;

import portfolio.storage.DeedInfo;

public class Deed {
	private Security security;
	private int numOfSec;
	
	public Security getSecurity() {
		return security;
	}
	
	public void setSecurity(Security security) {
		this.security = security;
	}
	
	public int getNumOfSec() {
		return numOfSec;
	}
	
	public void setNumOfSec(int numOfSec) {
		this.numOfSec = numOfSec;
	}

	public Deed(Security sec , int num){
		setNumOfSec(num);
		setSecurity(sec);
	}

	public Deed(DeedInfo di){
		setSecurity(new Security(di.getSecurity()));
		setNumOfSec(di.getNumOfSec());
	}

	public DeedInfo returnDeedInfo() {
		DeedInfo di = new DeedInfo();
		di.setSecurity(getSecurity().returnSecurityInfo());
		di.setNumOfSec(getNumOfSec());
		return di;
	}

	@Override
	public String toString() {
		return "Deed for security: " + security.toString() + ", Amount: " + numOfSec + "\n";
	}

}
