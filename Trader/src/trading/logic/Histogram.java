package trading.logic;

import java.util.ArrayList;
import java.util.List;
import trading.storage.CommandInfo;
import trading.storage.HistogramInfo;

public class Histogram {

	private List<Command> lCommands;

	public Histogram(HistogramInfo h) {
		lCommands = new ArrayList<Command>();
		if(!(h == null)) {
			for(CommandInfo ci : h.getlCommands()) {
				Command c = new Command(ci);
				lCommands.add(c);
			}
		}	
	}

	public void add(Command c) {
		lCommands.add(0, c);
	}

	public void clear() {
		lCommands.clear();
	}

	public List<Command> getLastXCommands(int numOfCommands) {
		List<Command> l = new ArrayList<Command>();
		for (int i = 0; i < numOfCommands; i++) {
			l.add(lCommands.get(i));
		}
		return l;
	}
	
	 public void  Print(){
		for (int i=0;i<lCommands.size();i++){
			lCommands.get(i).print();
		}
	}

	public boolean isEmpty() {
		if (lCommands.size() == 0)
			return true;
		return false;
	}

	public HistogramInfo returnHistogramInfo() {
		HistogramInfo hi = new HistogramInfo();
		for(Command c : this.getLastXCommands(lCommands.size())) {
			hi.getlCommands().add(new CommandInfo(c.getStockName(), c.getAmount(), c.isComplete(), c.getId(), c.getType()));
		}
		return hi;
	}
}