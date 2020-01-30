import java.time.*;

public class InputInfo {
	//CLASS VARIABLES
	private final LocalTime requestTime;
	private final String direction;
	private final int startFloor;
	private final int destinationFloor;

	//CONSTRUCTORS
	InputInfo(String requestTime, String startFloor, String direction, String destinationFloor) {
		this.requestTime = LocalTime.parse(requestTime);
		this.startFloor = Integer.parseInt(startFloor);
		this.direction = direction;
		this.destinationFloor = Integer.parseInt(destinationFloor);
	}
	
	//GETTERS
	public LocalTime getRequestTime() {
		return this.requestTime;
	}
	
	//an input of 0 denotes a downward direction while 1 denotes an upward one.
	public int getDirection() {
		switch (direction) {
		case "Up":
			return 1;
		case "Down":
			return 0;
		default:
			return -1;
		}
	}
	
	public int getStartFloor() {
		return this.startFloor;
	}
	
	public int getDestinationFloor() {
		return this.destinationFloor;
	}
}
