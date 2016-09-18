package cscie55.hw2;

/**
 * This is the Building object which is a collection of
 * floors and an elevator. 
 *
 * @author Gerald Trotman
 * @version 10/2/2015
 *
 */



public class Building {

	/** This sets a hard number of floors in a Building **/
	public static final int FLOORS = 7;
	

	/** This sets the bottom floor as the main floor **/
	public static final int MAIN_FLOOR = 1;	


	/** Instantiates single Elevator **/
	private Elevator elevator;

	/** This creates an array of floor objects **/
	private Floor [] floor = new Floor[Building.FLOORS + 1];

	/** This instantiates the floors of the Building and the elevator **/
	public Building() {
		elevator = new Elevator(this);
		for (int i = Building.MAIN_FLOOR; i <= Building.FLOORS; i++) {
			floor[i] = new Floor(this, i);
		}
		this.elevator = new Elevator(this);
	}

	/** This gets the elevator to the corresponding building **/
	public Elevator elevator() {
		return elevator;
	}

	/** This gets the floor number for the corresponding floor **/
	public Floor floor(int floorNumber) {
		return floor[floorNumber];
	}
}
