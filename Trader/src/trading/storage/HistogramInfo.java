package trading.storage;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class HistogramInfo {
	private List<CommandInfo> lCommands;

	public HistogramInfo() {
		lCommands = new ArrayList<CommandInfo>();
	}

	public HistogramInfo(Scanner scanner) {
		lCommands = new ArrayList<CommandInfo>();
		while (scanner.hasNext()) {
			lCommands.add(new CommandInfo(scanner));
			scanner.nextLine();
		}
	}

	public List<CommandInfo> getlCommands() {
		return lCommands;
	}

	public void store(PrintStream out) {
		for (CommandInfo ci : getlCommands()) {
			ci.writeInto(out);
			out.println();
		}
	}
}
