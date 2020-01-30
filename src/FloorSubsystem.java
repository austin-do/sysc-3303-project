import java.io.*;
import java.util.*;

public class FloorSubsystem implements Runnable{

	static LinkedList<InputInfo> inputQueue = new LinkedList<InputInfo>();
	
	public static void main(String[] args) throws IOException {
		getInputs();
		System.out.println(inputQueue.size());
	}
	
	@Override
	public void run(){
		try {
			getInputs();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
//		for (;;) {
//			scheduler.sendToElevatorSS
//			scheduler.notifyElevatorSS
//		}
	}

	//reads from the input file and calls parseAdd on each line 
	private static void getInputs() throws IOException {
		//is this how u do relative paths in java
		String localDir = System.getProperty("user.dir");
		BufferedReader in = new BufferedReader(new FileReader(localDir + "\\src\\assets\\Inputs.txt"));
		String ln;
		while ((ln = in.readLine()) != null)
				parseAdd(ln);
		in.close();
	}
	
	//splits each string by whitespace and creates an InfoObject to be checked for validity based on resulting inputs
	private static void parseAdd(String ln) {
		String[] inputs = ln.split(" ");
		try {
			yeetThatShitOut(new InputInfo(inputs[0], inputs[1], inputs[2], inputs[3]));
		} catch (Exception e) {
			System.out.println("Invalid Input: " + e);
		}
	}
	
	//checks if the input makes sense *different from input being valid*
	//i.e. if the direction doesn't make sense in conjunction with the floor inputs
	private static boolean yeetThatShitOut(InputInfo i) {
		switch (i.getDirection()) {
		case 0:
			if (i.getStartFloor() < i.getDestinationFloor()) return false;
			break;
		case 1:
			if (i.getStartFloor() > i.getDestinationFloor()) return false;
			break;
		default: return false;
		}
		inputQueue.add(i);
		return true;
	}
	
	//for the scheduler?
	private synchronized boolean awaitingConsumption() {
		return inputQueue.size()>0;
	}
	
	//for the scheduler?
	private synchronized void removeFromInputList() {
		inputQueue.pop();
	}
}

