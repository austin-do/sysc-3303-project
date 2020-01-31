/**
 * 
 */
package main;

/**
 * @author Kevin
 *
 */
public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		MainScheduler s = new MainScheduler();

		TestClassFloor tcf = new TestClassFloor(s);
		TestClassElevator tce = new TestClassElevator(s);
		
		new Thread(tcf).start();
		new Thread(tce).start();
		

	}

}
