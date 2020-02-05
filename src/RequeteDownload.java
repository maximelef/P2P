
public class RequeteDownload implements Requete {

	String fichier; 
	
	public RequeteDownload(String fichier) {
		this.fichier = fichier;
	}
	
	@Override
	public String repondre() {
		Connexion c = new Connexion (12355);
		c.run();
		return "OK "+this.fichier+" 1235";
		// TODO Auto-generated method stub
		
	}
	

}
