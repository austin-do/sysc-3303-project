/**
 * 
 */
package main;

/**
 * @author Kevin
 *
 */
public class TestClassElevator implements Runnable{

	Scheduler s;
	
	public TestClassElevator(MainScheduler s) {
		this.s = new SchedulerElevator(s);
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		for(int i = 10; i < 20; i++) {
			
			int response = (int)s.get();
			System.out.println("Elevator Recieved : "+response);
			
			s.put(i);
			System.out.println("Elevator Sent : "+i);
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}

}
