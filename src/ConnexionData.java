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
	private String path;
	
	public ConnexionData( String nom, int port, String path) {
		this.port = port;
		this.nom =nom;
		this.path = path;
	}

	public void run () {
		try {
			// On met en place des sockets pour communiquer 
			// Création d'un socket serveur générique sur le port 40000
			ServerSocket socketServeur= new ServerSocket(this.port);
			Socket socket= socketServeur.accept();
			byte[] chaine = new byte[4096];
			if ( this.path.compareTo("Upload/") == 0) {
				DataInputStream sortieSocket = new DataInputStream(socket.getInputStream());
				// On crée le fichier 
				File fileTemp = new File (this.path+this.nom);
				fileTemp.createNewFile();
				FileOutputStream file = new FileOutputStream(this.path+this.nom);
				
				// On intègre le fichier petit à petit
				while( sortieSocket.available() > 0 ) {
					sortieSocket.read(chaine);
					file.write(chaine);
					System.out.println("Réception:"+chaine);
				}
				file.close();
				sortieSocket.close();
			} else {
				DataOutputStream entreeSocket = new DataOutputStream(socket.getOutputStream());
				FileInputStream file = new FileInputStream("Test/SuperTest");
				// On parcours tout le fichier
				while (file.read(chaine) != -1) {
					entreeSocket.write(chaine);
					System.out.println("Réception:"+chaine);
				}
				file.close();
				entreeSocket.close();
			}
			System.out.println("Reception terminée.");	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
						
		}

}
