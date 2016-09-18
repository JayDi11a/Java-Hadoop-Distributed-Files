package cscie55.hw3;

import java.util.ArrayList;
import java.util.Collection;


/**
 * Floor Object which represents a floor in a building
 *
 * @author Gerald Trotman
 * @version 10/02/2015
 */


public class Floor {


	@SuppressWarnings("unused")
	/** A variable that is set to the number of floors in a building  **/ 
	private final int floorNumber;
	
	/** Single instance of a building  **/
	private final Building building;

	/** Instantiates the number of passengers waiting for the elevator on a floor.  **/
	public int waitingPassengers = 0;




	Collection<Passenger> residentPassengers = new ArrayList<Passenger>();
	Collection<Passenger> ascendingPassengers = new ArrayList<Passenger>();
	Collection<Passenger> decendingPassengers = new ArrayList<Passenger>();


	/** 
	 * A constructor that sets the relationship to the building
 	 * @param building
	 * @param floorNumber
 	 */
	public Floor(Building building, int floorNumber) {
		
		this.building = building;
		this.floorNumber = floorNumber;
	}


	public boolean isResident(Passenger passenger) {
		return (residentPassengers.contains(passenger));
	}


	
	public void enterGroundFloor(Passenger passenger) {
		if (floorNumber == Building.MAIN_FLOOR) {
			residentPassengers.add(passenger);
		} else { 
			throw new RuntimeException("Method can only be called on the ground floor Object");
		}
	}

	/** 
	 * Comes into play when a passenger on the floor wants to wait for the elevator	
	 * @param passenger called when a passenger is waiting to board
	 * @param destinationFloor passengers destination
	 *
	 */
	public void waitForElevator(Passenger passenger, int destinationFloor) {
		//to check that it's actuall a floor
		floorCheck(destinationFloor);
		
		//initiate passenger waiting
		passenger.waitForElevator(destinationFloor);
	
		//passenger's direction
		if (destinationFloor < floorNumber) {
			decendingPassengers.add(passenger);
		
		} else if (destinationFloor > floorNumber) {
			ascendingPassengers.add(passenger);
		
		} else { 
			throw new IllegalArgumentException("Passenger Cannot Be Destined for the Floor their on");

		}
		residentPassengers.remove(passenger);
	}

	/**
	 * Ensures passenger is going an actual floor
	 * @param floor 
	 * @return boolean value
	 */
 	 
	private boolean floorCheck(int floor) {
		if (Building.MAIN_FLOOR <= floor && floor <= Building.FLOORS) {
			return true;
		} else {
			throw new IllegalArgumentException("Floor is Out of Bounds of the Building");
		}
	}

}		
