
public class RequeteDownload implements Requete {

	private String fichier; 
	private int port;
	
	public RequeteDownload(String fichier, int port) {
		this.fichier = fichier;
		this.port = port;
	}
	
	@Override
	public String repondre() {
		Connexion c = new Connexion (12355);
		c.start();
		return "OK "+this.fichier+" "+this.port;
		// TODO Auto-generated method stub
		
	}
	

}
