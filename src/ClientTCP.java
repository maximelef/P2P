import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientTCP {
	
	private Socket socket;
	
	public ClientTCP (Socket socket) {
		// Socket client 
		this.socket = socket;
	}
	
	public void lancerConnexion() throws IOException {
		
		// Construction d'un BufferedReader pour lire du texte envoyé à travers la connexion socket
		BufferedReader entreeSocket = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
		// Construction d'un PrintStream pour envoyer du texte à travers la connexion socket
		PrintStream sortieSocket = new PrintStream(socket.getOutputStream());
		
		String chaine = "";
		String chaineRetour ="";
		// Scanner sur System.in
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("Tapez vos phrases ou FIN pour arrêter :");
					
		while(!chaine.equalsIgnoreCase("FIN")) {
			// lecture clavier
			chaine = scanner.nextLine();
			sortieSocket.println(chaine); // on envoie la chaine au serveur
			String[] cmd = chaine.split(" ");
			
			// lecture d'une chaine envoyée à travers la connexion socket
			chaineRetour = entreeSocket.readLine();
			System.out.println("Chaine reçue : "+chaineRetour);
			String[] retour = chaineRetour.split(" ");
			if (cmd[0].compareTo("UPD") == 0 &&  retour[0].compareTo("OK") == 0) {
				System.out.println("Lancement de l'upload");
				this.lancerUpload(cmd[1], Integer.parseInt(cmd[2]));
			}
			if (cmd[0].compareTo("DWD") == 0 &&  retour[0].compareTo("OK") == 0) {
				System.out.println("Lancement du download");
				this.lancerDownload(cmd[1], Integer.parseInt(cmd[2]));
			}
		}
	}
	
	public void lancerDownload(String fichier, int port) throws IOException {
		// On construit le socket 
		Socket socketDownload = new Socket ("localhost", port);
		// On récupère la sortie
		DataInputStream sortieSocket = new DataInputStream(socketDownload.getInputStream());
		// On crée le fichier 
		File fileTemp = new File ("Downloaded/"+fichier);
		fileTemp.createNewFile();
		FileOutputStream file = new FileOutputStream("Downloaded/"+fichier);
		byte[] chaine = new byte[4096];

		// On intègre le fichier petit à petit
		while( sortieSocket.read(chaine)  != -1) {
			file.write(chaine);
			System.out.println("Réception:"+chaine);
		}
		file.close();
		sortieSocket.close();
		System.out.println("Terminé.");

	}
	
	public void lancerUpload(String fichier, int port) throws IOException {
		// On construit le socket 
		Socket socketUpload = new Socket ("localhost", port);
		// On récupère la sortie
		DataOutputStream entreeSocket = new DataOutputStream(socketUpload.getOutputStream());
		FileInputStream file = new FileInputStream("Downloaded/"+fichier);
		byte[] fileContent = new byte[4096];
		while (file.read(fileContent) != -1) {
			entreeSocket.write(fileContent);
			System.out.println("Données:"+fileContent);
		}
		System.out.println("Terminé.");
		file.close();
		entreeSocket.close();
	}
	
	public void fermerConnexion () throws IOException {
		this.socket.close();
	}
}
