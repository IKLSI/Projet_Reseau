// Import des librairies

import java.net.*;
import java.io.*;
import java.util.*;

class ChatServerThread extends Thread 
{
	// Déclaration des attributs

	private Socket socket = null;
	private String channel = "General";
	private static Map<String, List<Socket>> channels = new HashMap<>();

	// Constructeur

	public ChatServerThread(Socket socket)
	{
		super("ChatServerThread");
		this.socket = socket;
	}

	// Méthode run

	public void run() 
	{
		try 
		{
			// Déclaration des variables
			
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			InetAddress clientAddress = socket.getInetAddress();
			String inputLine, outputLine, titreMusique;

			out.println("Bienvenue, connexion reussie !");

			// Affichage de l'adresse IP du client et informations sur le client

			System.out.println("Client connecte : " + clientAddress.getHostAddress());
			String clientName = in.readLine();
			System.out.println("Pseudo client : " + clientName);

			// Création du canal General

			if (!channels.containsKey(channel))
				channels.put(channel, new ArrayList<>());
			
			// Ajout du client au canal
			channels.get(channel).add(socket);

			// Boucle de lecture des messages

			while ((inputLine = in.readLine()) != null) 
			{
				// Affichage du message du client dans la console serveur
				System.out.println("Client "+ clientName + " : " + inputLine);

				// Le client peut quitter le serveur en utilisant la commande "exit" ou "quit"
				if (inputLine.equals("exit") || inputLine.equals("quit"))
					break;

				// Permet de créer un nouveau canal de discussion
				if (inputLine.startsWith("join "))
				{
					String newChannel = inputLine.substring(5);
					if (!channels.containsKey(newChannel))
						channels.put(newChannel, new ArrayList<>());

					channels.get(channel).remove(socket);
					channels.get(newChannel).add(socket);
					channel = newChannel;
					out.println("Vous avez rejoint le canal " + newChannel);
				}
				else 
				{
					outputLine = clientName + " : " + inputLine;

					// Envoi du message à tous les clients présent dans le canal
					for (Socket s : channels.get(channel))
					{
						/* Problème ici : le serveur envoie le message à tous les clients, y compris à lui-même et par conséquent décale les sorties de la console
						Il faudrait comparer les clients avec le socket courant pour ne pas envoyer le message à lui-même cependant cela ne fonctionne pas */
						PrintWriter writer = new PrintWriter(s.getOutputStream(), true);
						writer.println(outputLine);
					}
				}
			}
			
			// Fermeture des flux et de la socket
			out.close();
			in.close();
			socket.close();

		} catch (IOException e) { e.printStackTrace(); }
	}
}

public class ChatServer 
{
	// Déclaration du main
	public static void main(String[] args) throws IOException 
	{
		// Déclaration des variables

		ServerSocket serverSocket = null;
		boolean listening = true;
		ArrayList<PrintWriter> clients = new ArrayList<>();

		// Création du socket serveur sur le port 9000 et affichage d'un message dans le cas échéant

		try 
		{
			serverSocket = new ServerSocket(9000);
		} 

		// Gestion des erreurs

		catch (IOException e) 
		{
			System.err.println("Impossible d'ecouter sur le port: 9000.");
			System.exit(-1);
		}

		System.out.println("Serveur lance sur le port 9000, en attente de connexion");
	
		// Boucle d'écoute des clients
		
		while (listening) 
		{
			Socket socket = serverSocket.accept();
			System.out.println("Nouvelle connexion entrante");
	
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
	
			clients.add(out);
	
			Thread clientThread = new Thread(() -> {
				try 
				{
					// Déclaration des variables

					BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					InetAddress clientAddress = socket.getInetAddress();
					String inputLine, outputLine, clientName;
	
					out.println("Bienvenue, connexion reussie !");
					System.out.println("Client connecte : " + clientAddress.getHostAddress());
					clientName = in.readLine();
					System.out.println("Pseudo client : " + clientName);
					
					// Boucle de lecture des messages

					while ((inputLine = in.readLine()) != null) 
					{
						System.out.println("Client " + clientName + " : " + inputLine);
						outputLine = clientName + " : " + inputLine;
	
						for (PrintWriter client : clients)
							client.println(outputLine);

						// Le client peut quitter le serveur en utilisant la commande "exit" ou "quit"
	
						if (inputLine.equals("exit") || inputLine.equals("quit"))
							break;

						// Le client peut lister les musiques disponibles sur le serveur en utilisant la commande "ListeMusique"

						if (inputLine.equals("ListeMusique"))
						{
							String [] listeMusique = GestionSon.ListerMusiques();
							String ligne = "";
							for (int i = 0; i < listeMusique.length; i++)
								ligne += listeMusique[i] + " ";
							
							out.println(ligne);
						}

						// Le client peut jouer une musique en utilisant la commande "Play" suivi du nom de la musique
	
						if (inputLine.startsWith("Play")) 
						{
							String[] parts = inputLine.split(" ");
							String titreMusique = parts[1];
	
							GestionSon.LireMusique(titreMusique);
						}

						// Le client peut arrêter la musique en cours de lecture en utilisant la commande "Stop"
	
						if (inputLine.equals("Stop"))
							GestionSon.arreterMusique();

						/* En cours de développement mais devrait fonctionner : 
						Le client peut mettre en pause la musique en cours de lecture en utilisant la commande "Pause" */
	
						if (inputLine.equals("Pause"))
							GestionSon.pauseMusique();
					}
	
					// Fermeture des flux et de la socket
					out.close();
					in.close();
					socket.close();
	
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					clients.remove(out);
				}
			});
	
			clientThread.start();
		}
	
		serverSocket.close();
	}
}
	
