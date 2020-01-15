import java.io.IOException;
import java.net.BindException;

public class Serveur {

	public static void main(String[] args) {
		
		try {
			// On crée notre connexion
			Connexion connexion = new Connexion (12345);
			// On lance notre connexion
			connexion.lancerConnexion();
		} catch (BindException e) {
			System.out.println("Erreur lors de la création du socket, le port est déjà utilisé");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
