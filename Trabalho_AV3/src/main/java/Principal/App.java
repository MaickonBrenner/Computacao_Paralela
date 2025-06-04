package Principal;

public class App {
	private static String livro = "Amostras/DonQuixote-388208.txt";
    private static String palavra = "Sancho";
	public static void main(String[] args) {
		
		Controlador co = new Controlador();
		co.controle();
	}
}
