import java.io.File;

public class RequeteCWD implements Requete {

	// Nom de notre repertoire 
	private File repertoire;
	
	public RequeteCWD () {
		this.repertoire = new File(Constante.repertoire);
	}
	
	@Override
	public String repondre() {
		// TODO Auto-generated method stub
		return this.repertoire.getAbsolutePath();
	}
	
	
}
