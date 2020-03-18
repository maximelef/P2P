import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;

public class RequeteSize implements Requete {

	
	private String fichier;
	


	public RequeteSize(String fichier) {
		this.fichier = fichier;
	}



	@Override
	public String repondre() {
		try {
			int  val = 0;
			FileInputStream flux = new FileInputStream("RACINE/"+this.fichier+"/taille");
			InputStreamReader lecture=new InputStreamReader(flux);
			BufferedReader buff=new BufferedReader(lecture);
			return buff.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "OK 0";
	}

}
