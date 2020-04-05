import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class RequeteUpload implements Requete {
	
	private String nom;
	private DataInputStream data;
	private Socket manager; 
	private PrintStream sortieTexte;
	private int port;

	public RequeteUpload(String nom, DataInputStream data, int portManager, int port) {
		this.nom = nom;
		this.data = data;
		this.sortieTexte = sortieTexte;
		this.port = port;
		try {
			this.manager = new Socket ("localhost", portManager);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public String repondre() {
		try {
			byte[] chaine = new byte[4096];
			File newFolder = new File ("RACINE/"+ this.nom);
			newFolder.mkdir();
			int compteur =0;
			// On créé l'objet pour notifier le serveur
			PrintStream sortieSocket = new PrintStream(this.manager.getOutputStream());
			// On intègre le fichier petit à petit
			while( data.read(chaine) != -1 ) {
				compteur ++;
				System.out.println("Traitement du bloc n°"+compteur);
				//data.read(chaine);
				// Pour chaque bloc on crée un fichier
				// On crée le fichier 
				File fileTemp = new File ("RACINE/"+this.nom+"/"+this.nom+"."+compteur);
				fileTemp.createNewFile();
				FileOutputStream file = new FileOutputStream("RACINE/"+this.nom+"/"+this.nom+"."+compteur);
				file.write(chaine);
				System.out.println("Réception:"+chaine);
				// On notifie le manager 
				sortieSocket.println("UPD "+this.nom+" "+this.port+","+compteur);
				file.close();
			}
			// On envoie ua manager qu'ona aterminé 
			sortieSocket.println("CLOSED");
			// Enfin on créé un fichier avec la taille
			File fileTemp = new File ("RACINE/"+this.nom+"/taille");
			fileTemp.createNewFile();
			FileOutputStream file = new FileOutputStream("RACINE/"+this.nom+"/taille");
			// On le transforme en String puis en Bytes polur écrire
			file.write(String.valueOf(compteur).getBytes());
			this.manager.close();
			System.out.println("Fermeture du socket.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "OK "+this.nom;
	}

}
