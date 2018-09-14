package exceptions;

public class FragileItemCannotDeliver extends Throwable {
	public FragileItemCannotDeliver(){
		super("Have heavy item, need add Big or Standard robots.");
	}
}

