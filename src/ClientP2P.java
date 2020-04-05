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
import java.util.Scanner;

public class ClientP2P {

private int portManager;
	
	public ClientP2P (int port) throws UnknownHostException, IOException {
		// Socket client, le port est celui du manager
		this.portManager = port;
	}
	
	public void lancerConnexion() throws IOException {

		String tailleDownload = "";
		String chaine = "";
		String chaineRetour ="";
		// On utilise le scanner pour récupérer les infos de l'utilisateur
		Scanner scanner = new Scanner(System.in);
	
		
		while(!chaine.equalsIgnoreCase("FIN")) {
			System.out.println("user(to stop enter FIN)#:");
			// lecture clavier
			chaine = scanner.nextLine();
			// Connexion au socket 
			Socket socket = new Socket ("localhost", this.portManager);
			// Construction d'un BufferedReader pour lire du texte envoyé à travers la connexion socket
			BufferedReader entreeSocket = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			// Construction d'un PrintStream pour envoyer du texte à travers la connexion socket
			PrintStream sortieSocket = new PrintStream(socket.getOutputStream());
			
			String[] cmd = chaine.split(" ");
			String retour, tab[];
			switch (cmd[0]) {
			case "DWD":
				// On récupère le serveur sur lequel on peut télécharger
				sortieSocket.println(chaine);
				retour = entreeSocket.readLine();
				System.out.println("Chaine retournée par le manager :"+retour);
				tab= retour.split(" ");
				if (this.verificationParametres(2,tab)) {
					// On recupere 
					this.lancerDownload(tab[1],tab[2], this.getTailleTotale(tab[1], socket));
				} else {
					System.out.println("Nombre de paremètres incorrects.");
				}
				break;
			case "UPD":
				// On récupère le serveur sur lequel on peut upload
				sortieSocket.println("GET");
				retour = entreeSocket.readLine();
				System.out.println("Reception du manager:" + retour);
				tab = chaine.split(" ");
				if (this.verificationParametres(1,tab)) {
					this.lancerUpload(tab[1], retour);
				} else {
					System.out.println("Nombre de paremètres incorrects.");
				}
				
				break;
			case "LIST":
				sortieSocket.println(chaine);
				break;
			default:
				System.out.println("La commande que vous avez entrée est inconnue.");
				break;
			}
			sortieSocket.println("CLOSED");
			socket.close();
		}
	}
	
	// retourne la taille d'un fichier
	public int getTailleTotale (String fichier, Socket socket) {
		try {
			// On récupère les données pour se connecter au serveur
			// Construction d'un PrintStream pour envoyer du texte à travers la connexion socket
			PrintStream sortieSocket = new PrintStream(socket.getOutputStream());
			// Construction d'un BufferedReader pour lire du texte envoyé à travers la connexion socket
			BufferedReader entreeSocket = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			//requete
			String requete = "GET";
			// On envoie la requete au serveur
			sortieSocket.println(requete);
			// On recupère sa réponse 
			String retour = entreeSocket.readLine();
			String tab [] = retour.split(" ");
			// On construit le socket pour se connecter au serveur 
			Socket socketC = new Socket ("localhost", Integer.parseInt(tab[0]));
			// On reconstruit les entrées et sorties
			sortieSocket = new PrintStream(socketC.getOutputStream());
			entreeSocket = new BufferedReader(new InputStreamReader(socketC.getInputStream()));
			//requete
			requete = "SIZ "+fichier;
			// On envoie la requete au serveur
			sortieSocket.println(requete);
			
			// On récupère sa réponse
			return 	Integer.parseInt(entreeSocket.readLine());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}
	
	public void lancerDownload(String fichier, String listeBloc, int tailleTotale) throws IOException {
		// Pour chaque bloc on va aller récupérer sur les serveurs donnés par le manager
		// On découpe la chaine renvoyée
		String tableauBloc [] = listeBloc.split(";");
		for ( int numBloc = 1; numBloc <= tailleTotale; numBloc++) {
			// Les données sont de la forme PORT,NUMBLOC
			String [] ligneBloc = tableauBloc[numBloc-1].split(",");
			int numeroPort = Integer.parseInt(ligneBloc[0]);
			int numeroBloc = Integer.parseInt(ligneBloc[1]);
			// On crée le socket pour se connecter au serveur
			Socket socketC = new Socket ("localhost",numeroPort);
			// Construction d'un PrintStream pour envoyer du texte à travers la connexion socket
			PrintStream sortieSocket = new PrintStream(socketC.getOutputStream());
			// Construction d'un BufferedReader pour lire du texte envoyé à travers la connexion socket
			BufferedReader entreeSocket = new BufferedReader(new InputStreamReader(socketC.getInputStream()));
			// on envoie la chaine au serveur
			sortieSocket.println("DWD "+fichier+" "+numBloc);

			// On accepte les différents serveurs qui se connectent à nous
			System.out.println("On se connecte à un serveur");
			
			// On récupère la sortie
			DataInputStream sortieS = new DataInputStream(socketC.getInputStream());
			// On crée le fichier
			File fileTemp = new File ("Downloaded/"+fichier);
			fileTemp.createNewFile();
			FileOutputStream file = new FileOutputStream("Downloaded/"+fichier, true);
			byte[] chaine = new byte[4096];

			// On intègre le fichier petit à petit
			while( sortieS.read(chaine)  != -1) {
				file.write(chaine);
				System.out.println("Réception:"+chaine);
			}
			
			sortieSocket.println("CLOSED");
			file.close();
			socketC.close();
		}	
			System.out.println("Terminé.");
			
		}
	
	public boolean verificationParametres(int nbre, String[]tab) {
		// on ajoute 1 car il y a la commande 
		if (tab.length == nbre +1) {
			return true;
		}
		return false;
	}
	
	
	
	public void lancerUpload(String fichier, String port) throws IOException {
		// Création d'un socket pour le serveur
		Socket socketC = new Socket ("localhost", Integer.parseInt(port));
		// On récupère la sortie
		PrintStream sortieSocket = new PrintStream(socketC.getOutputStream());
		// Construction d'un BufferedReader pour lire du texte envoyé à travers la connexion socket
		BufferedReader entreeSocket = new BufferedReader(new InputStreamReader(socketC.getInputStream()));
		
		// On envoie au serveur le fait qu'on va uploader
		sortieSocket.println("UPD "+fichier);
		if (entreeSocket.readLine().compareTo("OK") == 0) {
			DataOutputStream entreeData = new DataOutputStream(socketC.getOutputStream());
			FileInputStream file = new FileInputStream("Downloaded/"+fichier);
			byte[] fileContent = new byte[4096];
			while (file.read(fileContent) != -1) {
				entreeData.write(fileContent);
				System.out.println("Données:"+fileContent);
			}
			System.out.println("Terminé.");
			file.close();
		}
		sortieSocket.println("CLOSED");
		// On ferme le socket
		socketC.close();
	}
	
	
	
}
