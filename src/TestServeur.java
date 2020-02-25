import java.io.IOException;
import java.net.Socket;

public class TestServeur {

	public static void main(String[] args) throws Exception {
		// Déclaration et initialisation des connexions 
		Serveur serveur = new Serveur (12345);
	
		serveur.lancerServeur();
		// On ferme les connexions 
		serveur.fermerConnexion();		
	}

}
