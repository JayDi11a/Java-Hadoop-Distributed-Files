package cscie55.hw2;

/**
 * This is a class that runs an Elevator Simulation
 * It uses an algorithm, changes direction when the elevator reaches the top
 * or bottom floors.
 * @author Gerald Trotman
 * @version 10/02/2015
 */

public class Elevator {

	public final static int CAPACITY = 10;
	private final Building building;
	private final static int MIN_FLOOR = Building.MAIN_FLOOR;
	private final static int MAX_FLOOR = Building.FLOORS;
	
	private int currentFloor = 1;
	private int direction = 1;

	private int [] passengerRequests = new int [Elevator.MAX_FLOOR + 1];


	public Elevator(Building building) {
		
		this.building = building;
	}

	/** This gets the number of passengers that are currently in the elevator **/
	public int passengers() {
		return currentCapacity();
	}

	/** This returns what floor the Elevator is currently on **/
	public int currentFloor() {
		return currentFloor;
	}	


	/** 
	 * This moves the elevator based on passenger request
	 * I chose to use arithmetic bit flipping instead of creating
	 * an entire methods for changing elevator direction.	
	 */
	public void move() {
		
		currentFloor = currentFloor + (1 * this.direction);
		
		
		if (Elevator.MIN_FLOOR == currentFloor || currentFloor == Elevator.MAX_FLOOR) {
			this.direction *= -1;
		}

		if (passengerRequests[currentFloor] > 0) {
			elevatorStop(currentFloor, true);
		}

		if(building.floor(currentFloor).passengersWaiting() > 0) {
			elevatorStop(currentFloor, false);	
		}
	}

	/** 
	 * This method boards passengers onto the Elevator.
	 * I felt it necessary to address how to handle being just at capacity.
	 * @param destinationFloorNumber 
	 * @throws ElevatorFullException
	 *   
	 */ 
	public void boardPassenger(int destinationFloorNumber) throws ElevatorFullException {
		
		if (floorVerification(destinationFloorNumber)) {
			if (currentCapacity() <= (Elevator.CAPACITY - 1)) {
				passengerRequests[destinationFloorNumber]++;
			} else {
				throw new ElevatorFullException();
			}
		} else {
			System.out.println("You're at the wrong floor.");
		}
	}

	/** This gets the current capacity of Elevator **/
	private int currentCapacity() {
		int capacity = 0;
		
		for (int i = Elevator.MIN_FLOOR; i <= Elevator.MAX_FLOOR; i++) {
			capacity += passengerRequests[i];
		}

		return capacity;
	}

	/** This makes sure that we are within the boundaries of building
	 *  @param floor
	 */
	private Boolean floorVerification(int floor) {
		
		if (Elevator.MIN_FLOOR <= floor && floor <= Elevator.MAX_FLOOR) {
			return true;
		 } else {
			return false;
		 }
	}

	/** 
	 * This allows the Elevator to stop on a requested floor
	 * @param floor
	 * @param unload
	 */
	private void elevatorStop(int floor, Boolean unload) {

		if (unload) {
			passengerRequests[floor] = 0;
		} else {
			while (building.floor(floor).passengersWaiting() > 0) {
				try {
					boardPassenger(1);
				} catch (ElevatorFullException e) {
					break;
				}
				
				building.floor(floor).waitingPassengers--;
			}
		}
	}
		
	/**
	 * This is the String Object that was asked of to
	 * convert data at a particular memory location to human readable text
	 */
	public String toString() {
		return String.format("Floor %d: " + " %d passengers\n", currentFloor + 1, currentCapacity());
	}
}
