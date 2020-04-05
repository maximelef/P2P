import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class ManagerP2P {
	
	// Socket recevant les infos des srveurs et des clients 
	private ServerSocket serveur;
	// Liste des serveurs disponibles
	private ArrayList <Integer> listeServeur;
	// LIste gérant les blocs des fichiers 
	private HashMap <String, ArrayList<String> > liste;

	public ManagerP2P (int port) throws UnknownHostException, IOException {
		// Initialisation 
		this.serveur = new ServerSocket(port);
		this.listeServeur = new ArrayList <Integer>();
		liste = new HashMap  <String, ArrayList<String> > ();
	}
	
	public void traitement() {
		try {
			
			// Le serveur doit toujours être en attente d'un client 
			while (true) {
				// Variable qui sert à savoir si la connexion doit être fermé
				String texte = "";
				String termine = "OPEN";
				System.out.println("Attente d'un client");
				Socket socketClient= this.serveur.accept();
				System.out.println("Réception d'un client...");
				// Tant qu'on ne reçoit pas CLOSED, on continue l'échange
				while (termine.compareTo("CLOSED") != 0) {
					// Variable qui nous sert à stocker les informations
					byte[] chaine = new byte[4096];
					// Variable qui nous servent à communiquer
					BufferedReader entreeTexte = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
					PrintStream sortieTexte = new PrintStream(socketClient.getOutputStream());
					// Réception de la première chaine qui va nous dire ce que le client souhaite
					termine = entreeTexte.readLine();
					System.out.println("Reception: " + termine);
					// On découpe les params
					String param[]= termine.split(" ");
					// En fonction du premier parametre, 
					// on fait des traitements différents 
					switch (param[0]) {
					// Permet d'ajouter un serveur à la liste, si le client demande un upload
					case "ADD":
						if (verificationParametres(1, param)) {
							try {
								this.listeServeur.add(Integer.parseInt(param[1]));
								System.out.println("Un nouveau serveur a été ajouté.");
								texte = "OK";
							}catch (Exception e) {
								sortieTexte.println("Le paramètre passé n'est pas un nombre");
							}
						} else {
							texte = "Nombre de paramètres incorrect.";
						}
						break;
					// Permet de renvoyer l'addresse d'un serveur
					case "GET":
						// Un client veut savoir si un ou plrs serveurs 
						// On crée un nombre alétoire pour la répartition alétoire 
						Random r = new Random ();
						int numServeur = r.nextInt(this.listeServeur.size());
						texte = String.valueOf(this.listeServeur.get(numServeur));
						break;
					case "UPD":
						if (verificationParametres(2 , param)) {
							if (this.liste.get(param[1]) == null) {
								this.liste.put(param[1], new ArrayList<String>());
							}
							this.liste.get(param[1]).add(param[2]);
							System.out.println("Un bloc a été ajouté par un serveur.");
							texte = "OK";
						} else {
							texte = "Nombre de paramètres incorrect.";
						}
						break;
					// Renvoie la liste de serveur et les blocs 
					case "DWD":
						if (verificationParametres(1 , param)) {
							// On regarde les serveurs contenant le fichier
							ArrayList<String> listeBloc = this.liste.get(param[1]);
							// La dernière ligne contient la taille
							String [] derniereLigne = listeBloc.get(listeBloc.size() -1).split(",");
							int taille = Integer.parseInt(derniereLigne[1]);
							System.out.println("La taille ud fichier est de "+derniereLigne[1]);
							// Variable retournée
							texte = "DWD "+param[1]+" ";
							// On prépare la ligne à renvoyer au client
							for ( int i = 1 ; i <= taille ; i++) {
								// Pour répartir au mieu on change l'ordre de la liste 
								ArrayList<String> listeTemp = listeBloc; 
								if ( i%2 == 0) {
									Collections.reverse(listeTemp);
								}
								for (String ligne : listeTemp) {
									String tab [] = ligne.split(",");
									if (tab[1].compareTo(String.valueOf(i)) == 0) {
										texte = texte +ligne +";";
									}
								}
							}
						} else {
							texte = "Nombre de paramètres incorrect.";
						}
						System.out.println("Texte:"+texte);
						break;
					case "CLOSED":
						System.out.println("Le client souhaite fermer la connexion.");
					break;
					// Commande inconnue 
					default :
						texte = "Commande inconnue";
				}
				
				sortieTexte.println(texte); // on envoie la chaine au client
				}
			socketClient.close();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public boolean verificationParametres(int nbre, String[]tab) {
		// on ajoute 1 car il y a la commande 
		if (tab.length == nbre +1) {
			return true;
		}
		return false;
	}
}
