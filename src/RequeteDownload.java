import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class RequeteDownload implements Requete {

	private String nom; 
	private int port;
	private int bloc;

	
	public RequeteDownload(String fichier, String port, String bloc) {
		this.nom = fichier;
		this.port = Integer.parseInt(port);
		this.bloc = Integer.parseInt(bloc);
	}
	
	@Override
	public String repondre() {
		try {
			// On créé le nouveau socket pour se connecter 
			ServerSocket serveur = new ServerSocket (this.port);
			// On attend le client 
			Socket socket = serveur.accept();
			// On prend l'entrée pour envoyer les données
			DataOutputStream entreeSocket = new DataOutputStream(socket.getOutputStream());
			byte[] chaine = new byte[4096];
			FileInputStream fileDwd = new FileInputStream("RACINE/"+this.nom+"/"+this.nom+"."+this.bloc);
			// On parcours tout le fichier qui a été découpé en 1 bloc
			while (fileDwd.read(chaine) != -1) {
				System.out.println("Envoi:"+chaine);
				entreeSocket.write(chaine);
			}
			fileDwd.close();
			socket.close();
			serveur.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "OK "+this.nom;
		
	}
	

}
