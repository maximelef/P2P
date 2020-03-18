import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Connexion extends Thread{

	// Le port d'ouverture de la connexion
	private int port;
	// La requete qu'on récupère 
	private Requete requete;
	
	public Connexion(int port) {
		this.port = port;
		this.requete = null;
	}
	
	public void lancerConnexion () throws Exception {
		// On met en place des sockets pour communiquer 
		// Création d'un socket serveur générique sur le port 40000
		ServerSocket socketServeur = new ServerSocket(this.port);
		
		// On attend une connexion puis on l'accepte
		Socket socket = socketServeur.accept();
		
		// Construction d'un BufferedReader pour lire du texte envoyé à travers la connexion socket
		BufferedReader entreeSocket = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		// Construction d'un PrintStream pour envoyer du texte à travers la connexion socket
		PrintStream sortieSocket = new PrintStream(socket.getOutputStream());
		
		String chaine = "";
		
		while(chaine != null) {
			// lecture d'une chaine envoyée à travers la connexion socket
			chaine = entreeSocket.readLine();
			
			// si elle est nulle c'est que le client a fermé la connexion
			if (chaine != null) {
				// On récupère la commande de la requete
				String cmdRequete = this.recupererCmd(chaine);
				// Selon la commande on crée l'objet correspondant 
				switch (cmdRequete)
				{
					case "LIST":
						this.requete = new RequeteListe ();
					break;
					case "CWD":
						this.requete = new RequeteCWD();						
					break;
					case "DWD":
						this.requete = new RequeteDownload(this.recupererParam(chaine), this.recupererSecondParam(chaine),  this.recupererTroisiemeParam(chaine));
					break;
					case "UPD":
						//this.requete = new RequeteUpload(this.recupererParam(chaine), this.recupererSecondParam(chaine));
					break;
					case "SIZ":
						this.requete = new RequeteSize(this.recupererParam(chaine));
					break;
					default :
						// On renvoie une exception si la commande est inconnue
						throw new Exception();
				}
				chaine = requete.repondre();
				sortieSocket.println(chaine); // on envoie la chaine au client
			}
			
		}
	}
	
	public String recupererCmd (String chaine) {
		String tableau[] = chaine.split(" ");
		return tableau[0];
	}
	
	public String recupererParam (String chaine) {
		String tableau[] = chaine.split(" ");
		return tableau[1];
	}
	
	public String recupererSecondParam (String chaine) {
		String tableau[] = chaine.split(" ");
		return tableau[2];
	}
	
	public String recupererTroisiemeParam (String chaine) {
		String tableau[] = chaine.split(" ");
		return tableau[3];
	}
	
	public void fermerConnexion() {
		// on ferme nous aussi la connexion
	}
	

	
	
}
