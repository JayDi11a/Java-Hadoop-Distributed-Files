package cscie55.hw3;




public class Passenger {


	private final int ID;
	private final static int UNDEFINED_FLOOR = -1;
	private int currentFloor = UNDEFINED_FLOOR;
	private	int desiredFloor = UNDEFINED_FLOOR;



	public Passenger(int id) {
		currentFloor = Building.MAIN_FLOOR;
		ID = id;
	}


	public int currentFloor() {
		return currentFloor;
	}


	public int destinationFloor() {
		return desiredFloor;
	}


	/**
	 * Passenger requesting to go to a differnt floor
	 * @param newDestinationFloor
	 *
	 */
	public void waitForElevator(int newDestinationFloor) {
		floorCheck(newDestinationFloor);
		this.desiredFloor = newDestinationFloor;
	}


	//Passenger gets on Elevator
	public void boardElevator() {
		currentFloor = UNDEFINED_FLOOR;
	}


	//When passenger gets to desired floor
	public void arrive() {
		currentFloor = desiredFloor;
		desiredFloor = UNDEFINED_FLOOR;
	}


	public String toString() {
		String string = "PASSENGER: current: " + currentFloor + " desired: " + desiredFloor;
		return string;
	}

	private boolean floorCheck(int floor) {
		if( Building.MAIN_FLOOR <= floor && floor <= Building.FLOORS){
			return true;
		} else {
			throw new IllegalArgumentException("Floor is OutofBounds for Building");
		}
	}
}



