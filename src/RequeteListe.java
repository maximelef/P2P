import java.io.File;

public class RequeteListe implements Requete{
	
		// Nom de notre repertoire 
		private File repertoire;
		
		public RequeteListe () {
			this.repertoire = new File(Constante.repertoire);
		}
		
		@Override
		public String repondre() {
			String chaine = "";
			// TODO Auto-generated method stub
			for (String c : this.repertoire.list()) {
				chaine = chaine + c +"; ";
			}
			return chaine;
		}

}
