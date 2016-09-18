package cscie55.hw2;

/**
 * Floor Object which represents a floor in a building
 *
 * @author Gerald Trotman
 * @version 10/02/2015
 */


public class Floor {

	/** A variable that is set to the number of floors in a building  **/ 
	private final int floorNumber;
	
	/** Single instance of a building  **/
	private final Building building;

	/** Instantiates the number of passengers waiting for the elevator on a floor.  **/
	public int waitingPassengers = 0;


	/** 
	 * A constructor that sets the relationship to the building
 	 * @param building
	 * @param floorNumber
 	 */
	public Floor(Building building, int floorNumber) {
		
		this.building = building;
		this.floorNumber = floorNumber;
	}

	/** This gets the number of passengers on the floor waiting for the elevator **/
	public int passengersWaiting() {
		return waitingPassengers;
	}

	/** This sets the number of passengers on a floor waiting for the elevator **/
	public void waitForElevator() {
		waitingPassengers++;
	}
}		
