/**
 * 
 */
package main;

import java.util.ArrayList;

/**
 * @author Kevin
 *
 */
public class MainScheduler {
	
	ArrayList<Object> elevatorMessageQueue;
	ArrayList<Object> floorMessageQueue;

	public MainScheduler() {
		elevatorMessageQueue = new ArrayList<>();
		floorMessageQueue = new ArrayList<>();
	}
	
	public synchronized Object floorGet() {
		while(floorMessageQueue.size() == 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		notifyAll();
		return floorMessageQueue.remove(0);
	}

	public synchronized boolean floorPut(Object o) {
		notifyAll();
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return elevatorMessageQueue.add(o);
	}

	public synchronized Object elevatorGet() {
		while(elevatorMessageQueue.size() == 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		notifyAll();
		return elevatorMessageQueue.remove(0);
	}

	public synchronized boolean elevatorPut(Object o) {
		notifyAll();
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return floorMessageQueue.add(o);
	}

}
