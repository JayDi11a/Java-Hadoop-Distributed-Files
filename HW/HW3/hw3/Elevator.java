package cscie55.hw3;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * This is a class that runs an Elevator Simulation
 * It uses an algorithm, changes direction when the elevator reaches the top
 * or bottom floors.
 * @author Gerald Trotman
 * @version 10/14/2015
 */

public class Elevator {

	public final static int CAPACITY = 10;
	private final Building building;
	
	private int currentFloor = 1;
	private int direction = 1;

	private Map<Integer, ArrayList<Passenger>> occupants = new HashMap<Integer, ArrayList<Passenger>>();

	/**
	 * Constructor
	 * @param building building that the elevator belongs to
	 *
	 */
	public Elevator(Building building) {
		
		this.building = building;

		// each floor has its corresponding list for passengers to be stored
		for (int i = Building.MAIN_FLOOR; i <= Building.FLOORS; i++) {
			occupants.put(i, new ArrayList<Passenger>());
		}
	}

	/** This gets the number of passengers that are currently in the elevator **/
	public Set<Passenger> passengers() {
		return currentCapacity();
	}

	/** This returns what floor the Elevator is currently on **/
	public int currentFloor() {
		return this.currentFloor;
	}	

	/**
	 * Gives the director of the Elevator
	 * @return true for up and false for down
	 *
	 */
	public boolean goingUp() {
		return (this.direction == 1);
	}


	public boolean goingDown() {
		return (this.direction == -1);
	}

	/** 
	 * This moves the elevator and manages passenger collections
	 * 	
	 */
	public void move() {
		
		//this does the actual elevator movement
		currentFloor = currentFloor + this.direction;
		
		//uses big flipping to handle direction change from the top or bottom movement
		if (Building.MAIN_FLOOR == this.currentFloor || this.currentFloor == Building.FLOORS)
			this.direction *= -1;

		//I wasnt sure how to use the absolute path so I chose to use this to get the object directly
		Floor floorObj = this.building.floor(currentFloor);

		//unloads the passengers
		if (this.occupants.get(currentFloor).size() > 0)
			this.elevatorStop(currentFloor, true, this.occupants.get(currentFloor));

		//boards passengers going up
		if (this.goingUp() && floorObj.ascendingPassengers.size() > 0)
			this.elevatorStop(currentFloor, false, floorObj.ascendingPassengers);

		//boards passengers going down
		else if (this.goingDown() && floorObj.decendingPassengers.size() > 0)
			this.elevatorStop(currentFloor, false, floorObj.decendingPassengers);
	}

	/** 
	 * Constructs a set of total passengers in the Elevator 
	 * @return returnSet
	 *  
	 */

	private Set<Passenger> currentCapacity() {
		
		Set<Passenger> returnSet = new HashSet<Passenger>();

		for (int floor : occupants.keySet()) {
			for (Passenger passenger : this.occupants.get(floor)){
				returnSet.add(passenger);
			}
		}

		return returnSet;
	}


	/** 
	 * Performs logic on collections
	 * @param floor where the elevator currently is
	 * @param unloading for whether or not passengers are unloading when elevator stops
	 * @param passengers Collection of passengerss
	 */
	private void elevatorStop(int floor, boolean isUnloading, Collection<Passenger> passengers) {

		if (isUnloading) {
			//unloads the passengers onto floor
			for (Passenger passenger : this.occupants.get(floor)) {
				passenger.arrive();
				this.building.floor(floor).residentPassengers.add(passenger);
			}

			this.occupants.put(floor, new ArrayList<Passenger>());

		} else {
			Collection<Passenger> justBoarded = new ArrayList<Passenger>();

			try{
				for (Passenger passenger : passengers) {
					if(this.currentCapacity().size() == Elevator.CAPACITY)
						throw new ElevatorFullException();

					passenger.boardElevator();
					this.occupants.get(passenger.destinationFloor()).add(passenger);
					justBoarded.add(passenger);
				}
			} catch (ElevatorFullException e) {}

			for(Passenger passenger : justBoarded){
				passengers.remove(passenger);
			}
			justBoarded = null;
		}
	}
}
