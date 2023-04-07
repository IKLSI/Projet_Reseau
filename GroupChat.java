/*
 * @author Arthur Lecomte
 * Last Update : 07/04/23
 */
import java.net.*;
import java.io.*;
import java.util.*;

public class GroupChat
{
	private static final String TERMINATE = "!Quit";
	static String name;
	static volatile boolean finished = false;
	public static void main(String[] args)
	{
		if (args.length != 2)
			System.out.println("Deux arguments requis: <Adresse-IP-Multicast> <Port>");
		else
		{
			try
			{
				InetAddress group = InetAddress.getByName(args[0]); //récupération addresse IP
				int port = Integer.parseInt(args[1]);				//récupération du Port
				Scanner sc = new Scanner(System.in);
				System.out.print("Entrez un pseudo: ");
				name = sc.nextLine();								//Initialisation du pseudo
				MulticastSocket socket = new MulticastSocket(port);
				socket.joinGroup(group);
				Thread t = new Thread(new ReadThread(socket,group,port));
			  
				// Cree un thread pour lire les msg
				t.start(); 
				  
				
				System.out.println("Connected...\nEnvoyez un message");
				System.out.println("!Help ");
				// Envoi sur le groupe
				while(true)
				{
					String message;
					message = sc.nextLine();
					if(message.equalsIgnoreCase(GroupChat.TERMINATE)) //si le msg = !Quit arret de la connexion
					{
						finished = true;
						socket.leaveGroup(group);
						socket.close();
						break;
					}
					message = name + ": " + message;
					byte[] buffer = message.getBytes();
					DatagramPacket datagram = new
					DatagramPacket(buffer,buffer.length,group,port);
					socket.send(datagram);		//Envoi du message
				}
			}
			catch(SocketException se)
			{
				System.out.println("Erreur de création de socket");
				se.printStackTrace();
			}
			catch(IOException ie)
			{
				System.out.println("Impossible de Lire/Ecrire sur le socket");
				ie.printStackTrace();
			}
		}
	}
}
class ReadThread implements Runnable
{
	private MulticastSocket socket;
	private InetAddress group;
	private int port;
	private static final int MAX_LEN = 250; // Longueur du message de 250 caractères max
	ReadThread(MulticastSocket socket,InetAddress group,int port)
	{
		this.socket = socket;
		this.group = group;
		this.port = port;
	}
	  
	@Override
	public void run()
	{
		
		while(!GroupChat.finished) //Reception du message
		{
				byte[] buffer = new byte[ReadThread.MAX_LEN];
				DatagramPacket datagram = new
				DatagramPacket(buffer,buffer.length,group,port);
				String message, titreMusique;
			try
			{
				socket.receive(datagram);
				message = new
				String(buffer,0,datagram.getLength(),"UTF-8");
				if(!message.startsWith(GroupChat.name))
					System.out.println(message);
				if (message.startsWith(GroupChat.name + ": !Play"))
				{
					// On decompose la chaine de caracteres en 2 parties : "Play" et le titre de la musique
					String[] parts = message.split(" ");
					titreMusique = parts[2];
					GestionSon.LireMusique(titreMusique);
					
				}
				if (message.startsWith(GroupChat.name + ": !Stop"))
					GestionSon.arreterMusique();
				
				if (message.startsWith(GroupChat.name + ": !Help")) //mise en place du !Help
					System.out.println( "!Play pour lancer une musique \n!Stop pour arrêter\n"+
										"!ListeMusique pour afficher la liste des musiques\n"+
										"!Pause pour arreter la musique\n" 					 +
										"!Quit pour fermer la connexion"					 );

				if (message.startsWith(GroupChat.name + ": !ListeMusique"))
					GestionSon.ListerMusiques();
				if (message.startsWith(GroupChat.name + ": !Pause"))
					GestionSon.pauseMusique();
				
			}
			catch(IOException e)
			{
				System.out.println("Socket closed!");
			}
		}
	}
}