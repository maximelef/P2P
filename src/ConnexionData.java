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
	private int bloc;
	private String nom;
	private String path;
	
	public ConnexionData( String nom, int port, String path, int bloc) {
		this.port = port;
		this.nom =nom;
		this.path = path;
		this.bloc = bloc;
	}

	public void run () {
		try {
			// On met en place des sockets pour communiquer 
			// Création d'un socket serveur générique sur le port 40000
			ServerSocket socketServeur= new ServerSocket(this.port);
			Socket socket= socketServeur.accept();
			byte[] chaine = new byte[4096];
			if ( this.path.compareTo("Upload/") == 0) {
				System.out.println("Upload");
				DataInputStream sortieSocket = new DataInputStream(socket.getInputStream());
				// On crée le dossier qui va nous servir à stocket le fichier splitté
				File newFolder = new File ("RACINE/"+this.nom);
				newFolder.mkdir();
				int compteur =0;
				// On intègre le fichier petit à petit
				while( sortieSocket.available() > 0 ) {
					compteur ++;
					System.out.println("Traitement du bloc n°"+compteur);
					sortieSocket.read(chaine);
					// Pour chaque bloc on crée un fichier
					// On crée le fichier 
					File fileTemp = new File ("RACINE/"+this.nom+"/"+this.nom+"."+compteur);
					fileTemp.createNewFile();
					FileOutputStream file = new FileOutputStream("RACINE/"+this.nom+"/"+this.nom+"."+compteur);
					file.write(chaine);
					System.out.println("Réception:"+chaine);
					file.close();
				}
				// Enfin on créé un fichier avec la taille
				File fileTemp = new File ("RACINE/"+this.nom+"/taille");
				fileTemp.createNewFile();
				FileOutputStream file = new FileOutputStream("RACINE/"+this.nom+"/taille");
				// On le transforme en String puis en Bytes polur écrire
				file.write(String.valueOf(compteur).getBytes());
				sortieSocket.close();
			} else {
				System.out.println("Download");
				DataOutputStream entreeSocket = new DataOutputStream(socket.getOutputStream());
				System.out.println(this.path+this.nom);
				FileInputStream file = new FileInputStream("RACINE/"+this.nom+"/"+this.nom+"."+this.bloc);
				// On parcours tout le fichier
				while (file.read(chaine) != -1) {
					System.out.println("Envoi:"+chaine);
					entreeSocket.write(chaine);
				}
				file.close();
				entreeSocket.close();
			}
			socket.close();
			socketServeur.close();
			System.out.println("Terminée.");	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
						
		}

}
