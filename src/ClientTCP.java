import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
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
		
		// Scanner sur System.in
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("Tapez vos phrases ou FIN pour arrêter :");
					
		while(!chaine.equalsIgnoreCase("FIN")) {
			// lecture clavier
			chaine = scanner.nextLine();
			sortieSocket.println(chaine); // on envoie la chaine au serveur
			
			// lecture d'une chaine envoyée à travers la connexion socket
			chaine = entreeSocket.readLine();
			System.out.println("Chaine reçue : "+chaine);
		}
	}
	
	public void lancerDownload(String fichier) throws IOException {
		// On récupère la sortie
		DataInputStream sortieSocket = new DataInputStream(socket.getInputStream());
		FileOutputStream file = new FileOutputStream(fichier);
		byte[] chaine = new byte[4096];
		// On intègre le fichier petit à petit
		while( sortieSocket.available() > 0 ) {
			sortieSocket.read(chaine);
			file.write(chaine);
			System.out.println(chaine);
		}
		System.out.println("fini");
		file.close();
		sortieSocket.close();
	}
	
	
	public void fermerConnexion () throws IOException {
		this.socket.close();
	}
}
