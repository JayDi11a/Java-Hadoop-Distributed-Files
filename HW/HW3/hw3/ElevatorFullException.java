package cscie55.hw3;


/**
 * This class extends the built in Exception class.The ElevatorFullException object is 
 * triggered when a full elevator is met with the scenario of trying to take on more
 * passengers.
 * 
 * @author Gerald Trotman
 * @version 10/14/2015
 */ 

public class ElevatorFullException extends Exception {
	public ElevatorFullException() {
	}

	public ElevatorFullException(String message){
		super(message);
	}
}
