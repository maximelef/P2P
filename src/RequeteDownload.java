
public class RequeteDownload implements Requete {

	private String fichier; 
	private int port;
	private int bloc;

	
	public RequeteDownload(String fichier, String port, String bloc) {
		this.fichier = fichier;
		this.port = Integer.parseInt(port);
		this.bloc = Integer.parseInt(bloc);
	}
	
	@Override
	public String repondre() {
		ConnexionData c = new ConnexionData(this.fichier, this.port, "Download/", this.bloc);
		c.start();
		return "OK "+this.fichier+" "+this.port;
		
	}
	

}
