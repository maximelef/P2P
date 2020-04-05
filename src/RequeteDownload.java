import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class RequeteDownload implements Requete {

	private String nom; 
	private int port;
	private int bloc;
	private DataOutputStream entreeSocket;
	
	public RequeteDownload(String fichier, String bloc, DataOutputStream entreeSocket, int portManager) {
		this.nom = fichier;
		this.port = portManager;
		this.bloc = Integer.parseInt(bloc);
		this.entreeSocket = entreeSocket;
	}
	
	@Override
	public String repondre() {
		try {
			byte[] chaine = new byte[4096];
			FileInputStream fileDwd = new FileInputStream("RACINE/"+this.nom+"/"+this.nom+"."+this.bloc);
			// On parcourt tout le fichier qui a été découpé en 1 bloc
			while (fileDwd.read(chaine) != -1) {
				System.out.println("Envoi:"+chaine);
				this.entreeSocket.write(chaine);
			}
			this.entreeSocket.close();
			fileDwd.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "OK ";
	}
	

}
