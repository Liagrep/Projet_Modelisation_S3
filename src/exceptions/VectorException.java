package exceptions;

@SuppressWarnings("serial")
public class VectorException extends Exception {

	public VectorException(){
		super("Le param�tre n'est pas un vecteur !");
	}
}