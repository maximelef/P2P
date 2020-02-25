
public class RequeteDownload implements Requete {

	private String fichier; 
	private int port;
	
	public RequeteDownload(String fichier, String port) {
		this.fichier = fichier;
		this.port = Integer.parseInt(port);
	}
	
	@Override
	public String repondre() {
		ConnexionData c = new ConnexionData(this.fichier, this.port, "Download/");
		c.start();
		return "OK "+this.fichier+" "+this.port;
		
	}
	

}
