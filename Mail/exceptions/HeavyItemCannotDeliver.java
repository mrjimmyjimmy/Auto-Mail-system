package exceptions;

public class HeavyItemCannotDeliver extends Throwable {
    public HeavyItemCannotDeliver(){
        super("Have heavy item, need add Big or Standard robots.");
    }
}
