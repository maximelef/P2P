import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class SplitFile {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		splitFileInBlocks("/home/valentin/Pictures/Table.png");
			  	
			 }
	
	public static void splitFileInBlocks(String filePath) {

		// declaration
		/*
		 *  byte[] fileContentInBytes : tableau d'octets
		 *  blockSize = 0 : taille du bloc actuel
		 *  NBS = 1 : nombre de blocs envoyés
		 *	lastBlockSize = 0 : taille du contenu du dernier bloc envoyé
		 */
		
		byte[] fileContentInBytes;
		int blockSize = 0, NBS = 1;
		int lastBlockSize = 0;
		
		// Chaine de lecture du fichier
		FileInputStream fis;

		// Création du fichier dont le chemin est en paramètres
		File file = new File(filePath);

		try {
			// Déclaration du file input stream
			fis = new FileInputStream(file);
			// Création de la variable file content in byte de la taille max d'un bloc
			fileContentInBytes = new byte[4096];

			/*
			 * on read le fichier sur 4096 bytes max, quand on arrive au
			 * dernier block "blocksize" n'est plus égale à 4096 mais au nombre de byte transmis dans
			 * le bloc de 4096
			 * on récupère le nombre de bytes du dernier bloc dans latblocksize
			 * 
			 * Donc on a des blocs de taille fixe qui sont transmis mais on sait quand
			 * s'arrete le dernier bloc grace à la valeur de blocksize. blocksize inclu le symbole de fin de
			 * ligne/fichier
			 */

			while ((blockSize = fis.read(fileContentInBytes, 0, 4096)) > 0) {

				NBS++ ;
				lastBlockSize = blockSize;
							}
			// Fermeture de la lecture
			fis.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		System.out.println("il y a eu " + NBS + " bloc(s) envoyé(s)" + " la taille du dernier bloc est de " + lastBlockSize + " octets!");
		
		/*
		 * Note à moi : Affiche en ASCII :
		 * System.out.println(Arrays.toString(fileContentInBytes));
		 */
	}
	
	public static void sendBlock(Byte blockToSend) {
		
	}
}

		

	
	
		
	
		

	
	