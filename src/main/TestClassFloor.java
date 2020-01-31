/**
 * 
 */
package main;

/**
 * @author Kevin
 *
 */
public class TestClassFloor implements Runnable{

	Scheduler s;
	
	public TestClassFloor(MainScheduler s) {
		this.s = new SchedulerFloors(s);
	}
	
	@Override
	public void run() {
		
		for(int i = 0; i < 10; i++) {
			
			s.put(i);
			System.out.println("Floor Sent : "+i);
			int response = (int)s.get();
			System.out.println("Floor Recieved : "+response);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}

}
