

public class Elevator {

	
	private int currentFloor = 1;
	private double velocity = 0;
	private int direction = 1; // 1 is up, -1 is down
	
	//Temporary variables
	private boolean thereIsWork = true;//From scheduler
	private int destination = 5;//From scheduler
	private int time = 4000;//Calculate base on current floor, destination and velocity
	private int doorOpenTime = 2000;//Assumption
	private int doorCloseTime = 2000;//Assumption
	
	private int taskQueue[][] = new int [22][4];
	
	private int currentTask = 0;
	
	
	
	public synchronized void getWork(){
		
		//If there is work, send objects(elevator information) to scheduler with message of confirmation
		//And received objects(data) from scheduler if assigned
		if (thereIsWork){
			
			//Send information to scheduler?
			
			
			//Receive information from scheduler?
			
			
			//Calculate the moving velocity, direction, time?
			
			
			//Add data to task queue
			int test1[] = {
							currentFloor,
							direction,
							time,
							destination
							};		
			
			for (int i = 0; i < 22; i++){		
				taskQueue[i]= null;
			}			
			
			taskQueue[0] = test1;
			
			int test2[] = {
							currentFloor+4,
							direction,
							time+3000,
							destination+6
							};
			
			taskQueue[1] = test2;
			
						
			//Start travel
			travel(taskQueue);
			
		}
		
		else{
			
			//Wait until there is a task assigned
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		
		
	}
	
	
	public synchronized void travel(int[][] tq){
		
		System.out.println("I'm Moving from " + tq[currentTask][0] + " floor!");
		
		try {
			Thread.sleep(tq[currentTask][2]);
		} catch (InterruptedException e) {}
		
		System.out.println(tq[currentTask][3] + " floor Reached!");
		
		//Send message to scheduler saying the elevator arrived destination?
		
		
		//Open door and close door
		toggleDoor(tq);
		
	}
	
	
	public synchronized void toggleDoor(int[][] tq){
		
		System.out.println("Door Opening");
		
		try {
			Thread.sleep(doorOpenTime);
		} catch (InterruptedException e) {}
		
		
		System.out.println("Door Closing");
		
		try {
			Thread.sleep(doorCloseTime);
		} catch (InterruptedException e) {}
		
		//Check queue. Another task? Next object in queue?
		if (tq[++currentTask] == null) {
			System.out.println("Elevator Wait for work");
			thereIsWork = false;
			getWork();
		}
		
		else{
			travel(tq);
		}
		
	}
	
	
	
	public static void main (String arg[]){
		System.out.println("Hello");
		Elevator e = new Elevator();
		e.getWork();
	}
	
}
