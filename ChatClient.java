// Déclaration des packages

import java.net.*;
import java.io.*;
import iut.algo.*;

// Déclaration de la classe

public class ChatClient 
{
	public static void main(String[] args) throws IOException 
	{
		// Déclaration des variables

		Socket chatSocket = null;
		PrintWriter out = null;
		BufferedReader in = null;
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		String userInput, pseudo, serverName = "172.26.7.112";

		// Connexion au serveur

		try 
		{
			chatSocket = new Socket(serverName, 9000);
			out = new PrintWriter(chatSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(chatSocket.getInputStream()));
		} 
		
		// Gestion des erreurs

		catch (UnknownHostException e) 
		{
			System.err.println("Aucune connexion au serveur");
			System.exit(1);
		} 
		
		catch (IOException e) 
		{
			System.err.println("Impossible d'obtenir les flux d'entree / sortie");
			System.exit(1);
		}

		// Lecture des messages

		System.out.print("Saisissez un pseudonyme : ");
		pseudo = Clavier.lireString();
		out.println(pseudo);
		System.out.println("Pseudo saisi : " + pseudo);
		System.out.print(pseudo + " :");

		// Traitement des messages

		while ((userInput = stdIn.readLine()) != null) 
		{
			out.println(userInput);
			System.out.println(in.readLine());

			if (userInput.equals("exit") || userInput.equals("quit")) 
				break;
		}

		// Fermeture des flux

		out.close();
		in.close();
		stdIn.close();
		chatSocket.close();
	}
}