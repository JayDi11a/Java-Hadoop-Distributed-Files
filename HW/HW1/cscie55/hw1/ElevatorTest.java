package cscie55.hw1;

/**
 * This is the main method
 * @param args
 *
 */

public class ElevatorTest {
	
	public static final int NO_OF_FLOORS = 7;
		
	public static void main( String [] args) {

	Elevator myElevator = new Elevator();

	myElevator.boardPassenger(4);
	myElevator.boardPassenger(7);
	myElevator.boardPassenger(3);

	System.out.print(myElevator.toString());

	for (int i = 0; i < (NO_OF_FLOORS * 6); i++)
		myElevator.move();
	}
}
