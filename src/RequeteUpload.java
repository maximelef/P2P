import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class RequeteUpload implements Requete {
	
	private String nom;
	private DataInputStream data;


	public RequeteUpload(String nom, DataInputStream data) {
		this.nom = nom;
		this.data = data;
	}

	@Override
	public String repondre() {
		try {
			byte[] chaine = new byte[4096];
			File newFolder = new File ("RACINE/"+ this.nom);
			newFolder.mkdir();
			int compteur =0;
			// On intègre le fichier petit à petit
			while( data.available() > 0 ) {
				compteur ++;
				System.out.println("Traitement du bloc n°"+compteur);
				data.read(chaine);
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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "OK "+this.nom;
	}

}
