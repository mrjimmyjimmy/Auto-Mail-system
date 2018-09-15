package exceptions;

/**
 * An exception thrown when all robot unable to deliver the item that is heavier than 2000
 */

public class HeavyItemCannotDeliverException extends Throwable {
    public HeavyItemCannotDeliverException(){
        super("Have item that is heavier than 2000 grams, But there are only week robot avaiable, Please adjust robot types");
    }
}
