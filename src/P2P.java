import java.io.IOException;
import java.net.UnknownHostException;

public class P2P {

	private ClientP2P client;
	private ServeurP2P serveur;
	
	public P2P () {
		
	}
	
	public void lancerServeurP2P(int portServeur, int portManager) {
		try {
			// Création du serveur
			this.serveur = new ServeurP2P (portServeur, portManager);
			// On lance le thread du serveur 
			this.serveur.start();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void lancerClientP2P(int portClient) {
		try {
			// Création du serveur
			this.client = new ClientP2P (portClient);
			// On lance le thread du serveur 
			this.client.lancerConnexion();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
