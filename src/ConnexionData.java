import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnexionData  extends Thread{
	
	private int port;
	private String nom;
	
	public ConnexionData( String nom, int port) {
		this.port = port;
		this.nom =nom;
	}

	public void run () {
			
			// On met en place des sockets pour communiquer 
			// Création d'un socket serveur générique sur le port 40000
			ServerSocket socketServeur;
			try {
				socketServeur = new ServerSocket(this.port);
				// On attend une connexion puis on l'accepte
				Socket socket = socketServeur.accept();
				
				DataInputStream sortieSocket = new DataInputStream(socket.getInputStream());
				// On crée le fichier 
				File fileTemp = new File ("Upload/"+this.nom);
				fileTemp.createNewFile();
				FileOutputStream file = new FileOutputStream("Upload/"+this.nom);
				byte[] chaine = new byte[4096];
				// On intègre le fichier petit à petit
				while( sortieSocket.available() > 0 ) {
					sortieSocket.read(chaine);
					file.write(chaine);
					System.out.println(chaine);
				}
				System.out.println("fini");
				System.out.println("Reception terminée.");
				file.close();
				sortieSocket.close();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
						
		}

}
