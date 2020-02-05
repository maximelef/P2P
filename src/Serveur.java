import java.io.IOException;
import java.net.BindException;

public class Serveur {
	
	private Connexion connexion;
	
	public Serveur (int port) {
		this.connexion = new Connexion(port);
	}
	
	public void lancerServeur () throws Exception{
		
		try {
			// On lance notre connexion
			connexion.lancerConnexion();
		} catch (BindException e) {
			System.out.println("Erreur lors de la création du socket, le port est déjà utilisé");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void fermerConnexion ()
	{
		
	}
}
