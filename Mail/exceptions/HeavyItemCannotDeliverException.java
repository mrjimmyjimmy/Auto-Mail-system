package exceptions;

public class HeavyItemCannotDeliverException extends Throwable {
    public HeavyItemCannotDeliverException(){
        super("Have item that is heavier than 2000 grams, But there are only week robot avaiable, Please adjust robot types");
    }
}
