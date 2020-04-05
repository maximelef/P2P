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
import java.net.UnknownHostException;
import java.util.Arrays;

public class ServeurP2P extends Thread{

	private ServerSocket serveur; 
	private int portServeur;
	private int portManager;

	
	public ServeurP2P (int portServeur, int portManager) throws IOException {
		this.serveur= new ServerSocket(portServeur);
		this.portManager = portManager;
		this.portServeur = portServeur;
	}
	
	public void run () {
		
		try {
			// Initialisation 
			initialisation();
			// Le serveur doit toujours être en attente d'un client 
			while (true) {
				Socket socketClient= this.serveur.accept();
				System.out.println("Réception d'un client...");
				// Variable qui sert à savoir si la connexion doit être fermé
				String termine = "OPEN";

				while (termine.compareTo("CLOSED") != 0) {
					// Variable qui nous sert à stocker les informations
					byte[] chaine = new byte[4096];
					String texte = "";
					// Variable qui nous servent à communiquer
					BufferedReader entreeTexte = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
					PrintStream sortieTexte = new PrintStream(socketClient.getOutputStream());
					DataInputStream sortieSocket = new DataInputStream(socketClient.getInputStream());
					DataOutputStream entreeSocket = new DataOutputStream(socketClient.getOutputStream());
					// Requete 
					Requete requete = null;
					// Réception de la première chaine qui va nous dire ce que le client souhaite
					texte = entreeTexte.readLine();
					System.out.println(texte);
					// On découpe les params
					String param[]= texte.split(" ");
					// En fonction du premier parametre, 
					// on fait des traitements différents 
					switch (param[0]) {
					case "UPD":
						sortieTexte.println("OK");
						requete = new RequeteUpload(param[1], sortieSocket, this.portManager, this.portServeur);
						break;
					case "DWD":
						requete = new RequeteDownload(param[1], param[2], entreeSocket, this.portManager);
						break;
					case "SIZ":
						requete = new RequeteSize(param[1]);
					break;
					default :
						System.out.println("Commande inconnue");
						// On renvoie une exception si la commande est inconnue
				}
				termine = requete.repondre();
				 // on envoie la chaine au client
				sortieTexte.println(termine);
				// On récupère la réponse 
				termine = "CLOSED";
				}
				socketClient.close();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
						
		}

	private void initialisation() {
		// TODO Auto-generated method stub
		try {
			Socket s = new Socket("localhost", this.portManager);
			PrintStream sortieTexte = new PrintStream(s.getOutputStream());
			BufferedReader entreeTexte = new BufferedReader(new InputStreamReader(s.getInputStream()));

			sortieTexte.println("ADD "+this.portServeur);
			System.out.println(entreeTexte.readLine());
			sortieTexte.println("CLOSED");

			s.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
