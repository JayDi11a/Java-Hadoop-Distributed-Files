package cscie55.hw1;

/**
 * This is a class that runs an Elevator Simulation
 * It uses an algorithm, changes direction when the elevator reaches the top
 * or bottom floors.
 * @author Gerald Trotman
 * @version 09/16/2015
 */

public class Elevator {

	final int NO_OF_FLOORS = 7;
	private int [] destination_requests = new int [NO_OF_FLOORS];
	private int current_floor;
	private int no_of_passengers;
	private boolean ascending;
	
	/**
	 * Constructor was created here
	 */

	public Elevator()
	{
		current_floor = 0;
		no_of_passengers = 0;
	}

	/**
	* This moves the elevator by either incrementing or decrementing
	* the current_floor variable.The variable changes direction with the
	* changeDirection() method when it reaches either the top or bottom
	* floors.
	*/
	public void move()
	{

		if (destination_requests[current_floor] > 0) {
			stop();
			}
		if (ascending) {
			if (current_floor == (NO_OF_FLOORS - 1))
				changeDirection();
			current_floor++;
		} else {
			if (current_floor == 0)
				changeDirection();
			else
				current_floor--;
		}
	}	

	
	/** 
	 * This method gets passengers on the Elevator in relation to the
	 * floor the passengar is on.
	 * @param floor 
	 */ 
	public void boardPassenger (int floor) {
		no_of_passengers++;
		destination_requests[floor - 1]++;
	}

	/**
	 * This method gets passengers off the Elevator.I just happened 
	 * to use the destination_requests[] array to figure out
	 * how many passengers to let off.I haven't figured out how to 
	 * get to spec at the moment.
	 */
	private void departingPassenger() {
		int departing = destination_requests[current_floor];
		for (int i = 0; i < departing; i++) {
			no_of_passengers--;
			destination_requests[current_floor]--;
		}
	}	
	
	/**
	 * This method stops the Elevator to let passengers on or off
	 */
	private void stop() {
		destination_requests[current_floor - 1] = 0;
		departingPassenger();
		System.out.print(this.toString());
	}

	/**
	 * This method does the actual changing of the Elevator direction
	 */
	private void changeDirection() {
		ascending = !ascending;
		move();
	}

	/**
	 * This is the String Object that was asked of to
	 * convert data at a particular memory location to human readable text
	 */
	public String toString() {
		return String.format("Floor %d: " + " %d passengers\n", current_floor + 1, no_of_passengers);
	}
}
