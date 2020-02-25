
public class RequeteUpload implements Requete {
	
	private int port;
	private String nom;


	public RequeteUpload(String nom, String port) {
		this.port = Integer.parseInt(port);
		this.nom = nom;
	}

	@Override
	public String repondre() {
		ConnexionData c = new ConnexionData(this.nom, this.port, "Upload/");
		c.start();
		return "OK "+this.nom+" "+this.port;
	}

}
