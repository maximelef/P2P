import java.io.File;

public class RequeteCWD implements Requete {

	// Nom de notre repertoire 
	private File repertoire;
	
	public RequeteCWD (String nomRepertoire) {
		this.repertoire = new File(nomRepertoire);
	}
	
	@Override
	public String repondre() {
		// TODO Auto-generated method stub
		return this.repertoire.getAbsolutePath();
	}
	
	
}
