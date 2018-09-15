package exceptions;

/**
 * An exception thrown when all robot unable to fragile item
 */

public class FragileItemCannotDeliverException extends Throwable {
	public FragileItemCannotDeliverException(){
		super("Have Fragile item, But no careful robot to deliver, Please adjust robot types");
	}
}

