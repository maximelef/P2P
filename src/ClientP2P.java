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

private Socket socket;
	
	public ClientP2P (int port) throws UnknownHostException, IOException {
		// Socket client 
		this.socket = new Socket("localhost", port);
	}
	
	public void lancerConnexion() throws IOException {
		
		// Construction d'un BufferedReader pour lire du texte envoyé à travers la connexion socket
		BufferedReader entreeSocket = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
		// Construction d'un PrintStream pour envoyer du texte à travers la connexion socket
		PrintStream sortieSocket = new PrintStream(socket.getOutputStream());
		
		String tailleDownload = "";
		String chaine = "";
		String chaineRetour ="";
		// On utilise le scanner pour récupérer les infos de l'utilisateur
		Scanner scanner = new Scanner(System.in);
		
		while(!chaine.equalsIgnoreCase("FIN")) {
			System.out.println("user(to stop enter FIN)#:");
			// lecture clavier
			chaine = scanner.nextLine();
			String[] cmd = chaine.split(" ");
			switch (cmd[0]) {
			case "DWD":
				if (this.verificationParametres(2,cmd)) {
					this.lancerDownload(cmd[1], Integer.parseInt(cmd[2]), this.getTailleTotale(cmd[1]));
				} else {
					System.out.println("Nombre de paremètres incorrects.");
				}
				break;
			case "UPD":
				// On notifie le serveur qu'on lui envoie le fichier 
				sortieSocket.println(chaine);
				if (this.verificationParametres(1,cmd)) {
					this.lancerUpload(cmd[1]);
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
			// lecture d'une chaine envoyée à travers la connexion socket
			chaineRetour = entreeSocket.readLine();
			System.out.println("Chaine reçue :"+chaineRetour);
		}
	}
	
	// retourne la taille d'un fichier
	public int getTailleTotale (String fichier) {
		
		try {
			// Construction d'un BufferedReader pour lire du texte envoyé à travers la connexion socket
			BufferedReader entreeSocket = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			// Construction d'un PrintStream pour envoyer du texte à travers la connexion socket
			PrintStream sortieSocket = new PrintStream(this.socket.getOutputStream());
			//requete
			String requete = "SIZ "+fichier;
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
	
	public void lancerDownload(String fichier, int port, int tailleTotale) throws IOException {
		for ( int numBloc = 1; numBloc <= tailleTotale; numBloc++) {
			// Construction d'un PrintStream pour envoyer du texte à travers la connexion socket
			PrintStream sortieSocket = new PrintStream(this.socket.getOutputStream());
			// on envoie la chaine au serveur
			sortieSocket.println("DWD "+fichier+" "+port+" "+numBloc);
			//
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//Pour chaque serveur on récupère le socket correspondant 
			Socket socketData = new Socket ("localhost", port);

			// On accepte les différents serveurs qui se connectent à nous
			System.out.println("On se connecte à un serveur");
			
			// On récupère la sortie
			DataInputStream sortieS = new DataInputStream(socketData.getInputStream());
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
			file.close();
			socketData.close();

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
	
	
	
	public void lancerUpload(String fichier) throws IOException {
		// On récupère la sortie
		DataOutputStream entreeSocket = new DataOutputStream(this.socket.getOutputStream());
		FileInputStream file = new FileInputStream("Downloaded/"+fichier);
		byte[] fileContent = new byte[4096];
		while (file.read(fileContent) != -1) {
			entreeSocket.write(fileContent);
			System.out.println("Données:"+fileContent);
		}
		System.out.println("Terminé.");
		file.close();
	}
	
	public void fermerConnexion () throws IOException {
		this.socket.close();
	}
	
}
