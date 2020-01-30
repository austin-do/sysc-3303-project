import java.util.Date;
import java.util.PriorityQueue;
import java.util.Random;

public class Elevator {

	// ======== Victor's work ===================

	private boolean poweredOn;
	private ElevatorState state;
	private Doors doors;
	private PriorityQueue<Task> taskQueue;

	public Elevator() {
		state = new ElevatorState();
		doors = new Doors();
		taskQueue = new PriorityQueue<Elevator.Task>();
	}

	public boolean isPoweredOn() {
		return poweredOn;
	}

	public void togglePowerdOn() {
		poweredOn = !poweredOn;
	}

	public synchronized void tick() {
		while (poweredOn) {

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}
	}

	// ========= Zeen's work ================

	public synchronized void travel(int[][] tq) {

		System.out.println("I'm Moving from " + tq[currentTask][0] + " floor!");

		try {
			Thread.sleep(tq[currentTask][2]);
		} catch (InterruptedException e) {
		}

		System.out.println(tq[currentTask][3] + " floor Reached!");

		// Send message to scheduler saying the elevator arrived destination?

		// Open door and close door
		toggleDoor(tq);

	}

	public synchronized void toggleDoor(int[][] tq) {

		System.out.println("Door Opening");

		try {
			Thread.sleep(doorOpenTime);
		} catch (InterruptedException e) {
		}

		System.out.println("Door Closing");

		try {
			Thread.sleep(doorCloseTime);
		} catch (InterruptedException e) {
		}

		// Check queue. Another task? Next object in queue?
		if (tq[++currentTask] == null) {
			System.out.println("Elevator Wait for work");
			thereIsWork = false;
			getWork();
		}

		else {
			travel(tq);
		}

	}

	public static void main(String arg[]) {
		System.out.println("Hello");
		Elevator e = new Elevator();
		e.getWork();
	}

	// ======== Victor's work ===================

	// only need to give Scheduler the ElevatorState to check if the elevator
	// can stop at a floor. This protects data in Elevator
	public class ElevatorState {
		private final float ACCELERATION = 0.68f;
		private final float MAX_VELOCITY = 4.31f;
		private final float FLOOR_HEIGHT = 3.23f; // This should be a function call to Floor but whatever for now

		private Motion motion;
		private int currentFloor;
		private float velocity;
		private int direction; // 1 is up, -1 is down
		private float metresTravelled; // per floor, NOT total

		private ElevatorState() {
			motion = new Motion();
			currentFloor = 1;
			velocity = 0;
			direction = 1;
			metresTravelled = 0;
		}

		public boolean isMoving() {
			return velocity > 0;
		}

		private void accelerate() {
			if (!isMoving())
				return;
			doMovement(velocity = Math.min(velocity + ACCELERATION, MAX_VELOCITY));
		}

		private void decelerate() {
			if (!isMoving())
				return;
			doMovement(velocity = Math.max(velocity - ACCELERATION, 0));
		}

		private void doMovement(float velocity) {
			metresTravelled += velocity;

			if (metresTravelled >= FLOOR_HEIGHT) {
				currentFloor += direction;
				metresTravelled = 0;
			}
		}

		private float secondsToStop() {
			return velocity <= ACCELERATION ? 0 : velocity / ACCELERATION;
		}

		public boolean canStopAtFloor(int floor) {
			if (taskQueue.isEmpty()) return true;
			if ((state.direction == -1 && floor > state.currentFloor)
					|| (state.direction == 1 && floor < state.currentFloor)) {
				return false;
			}
			if (!state.isMoving() || state.currentFloor == floor)
				return true;

			float distanceToFloor = Math.abs(floor - state.currentFloor) * FLOOR_HEIGHT;
			float secondsToFloor = distanceToFloor / state.velocity;
			return secondsToFloor >= state.secondsToStop();
		}

		public void assignTask(Task task) {
			taskQueue.add(task);
		}

		private boolean isAlive() {
			return motion.running;
		}

		private void wakeup() {
			Thread motionThread = new Thread(motion, "ElevatorMotion");
			motionThread.run();
		}

		private class Motion implements Runnable {
			private boolean running;

			@Override
			public void run() {
				while (!taskQueue.isEmpty()) {
					Task currentTask = taskQueue.peek();
					
					int nextFloor = (currentTask.pickedUp ? currentTask.endFloor : currentTask.startFloor);
					float distanceToFloor = Math.abs(
							nextFloor - state.currentFloor)
							* FLOOR_HEIGHT;
					float secondsToFloor = distanceToFloor / state.velocity;
					
					if (secondsToFloor - 1 < state.secondsToStop()) {
						decelerate();
						if (state.currentFloor == nextFloor) {
							velocity = 0;
							metresTravelled = 0;
							
							doors.openDoors();
							//TODO allow people to press button just in time and open doors again
							doors.closeDoors();
							if (!currentTask.pickedUp) {
								currentTask.pickedUp = true;
							} else {
								taskQueue.poll();
							}
						}
					} else {
						accelerate();
					}
				}
				velocity = 0;
				metresTravelled = 0;
				running = false;
			}
		}
	}

	private class Doors {
		private final float DOOR_MOVE_TIME = 6.74f;

		private boolean doorsOpen;
		private int movingDirection; // 1 for opening, -1 for closing

		private void openDoors() {
			if (doorsOpen)
				return;
			movingDirection = 1;
			try {
				Thread.sleep((long) (DOOR_MOVE_TIME * 1000));
			} catch (InterruptedException e) {
			}
			doorsOpen = true;
		}

		private void closeDoors() {
			if (!doorsOpen)
				return;
			movingDirection = -1;
			try {
				Thread.sleep((long) (DOOR_MOVE_TIME * 1000));
			} catch (InterruptedException e) {
			}
			doorsOpen = false;
		}
	}

	// This is a model of the Scheduler class, not the real thing
	public class Scheduler {
		private final Task test1 = new Task(new Date(), state.currentFloor, state.currentFloor + 2);
		private final Task test2 = new Task(new Date(), state.currentFloor + 4, state.currentFloor + 6);

		private ElevatorState[] elevatorStates = new ElevatorState[1];

		public void onReadTaskFromFile() {
			Task task = new Random().nextBoolean() ? test1 : test2;
			if (elevatorStates[0].canStopAtFloor(task.startFloor)) {
				elevatorStates[0].assignTask(task);
			} else {
				// check other elevators. if none can do it, wait
			}
		}
	}

	// This is a model of the Task class, not the real thing
	public class Task implements Comparable<Task> {
		private Date timeOfRequest;
		private int startFloor;
		private int endFloor;
		private boolean pickedUp;

		public Task(Date timeOfRequest, int startFloor, int endFloor) {
			this.timeOfRequest = timeOfRequest;
			this.startFloor = startFloor;
			this.endFloor = endFloor;
		}

		@Override
		public int compareTo(Task o) {
			return this.startFloor - o.startFloor;
		}
	}

}
