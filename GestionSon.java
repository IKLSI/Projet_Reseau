import java.io.*;
import jaco.mp3.player.MP3Player;
import jaco.mp3.player.*;

public class GestionSon
{ 
	private static MP3Player mp3Player;

	
	public static boolean LireMusique(String musique)
	{

		if(MusiqueExiste(musique))
		{
			musique = musique;
		}
		else if (MusiqueExiste(musique + ".mp3"))
		{
			musique = musique + ".mp3";
		}

		try 
			{
				File f = new File("musiques/" + musique);

				mp3Player = new MP3Player(f);
				if (mp3Player.isStopped()) 
					mp3Player.play();

				while (!mp3Player.isStopped()) 
				{
					Thread.sleep(5000);
				}
				return true;
			} 
			catch (Exception e) { System.err.println("La musique n'a pas pu etre lue"); }
	

		return false;
	}

	// vérifie l'existence de la musique donnée en paramètre dans l
	public static boolean MusiqueExiste(String nomMusique)
	{
	
		File dir  = new File("musiques");
		File[] liste = dir.listFiles();

		for(File item : liste)
		{
			if((item.isFile()) && nomMusique.equals(item.getName()))
			{ 
				return true;
			}
		}

		return false;
	}

	// renvoie un tableau contenant le nom de toutes les musiques disponibles 
	public static String[] ListerMusiques()
	{
		File dir  = new File("musiques");
		File[] liste = dir.listFiles();
		String[] listeMusiques = new String[liste.length];

		for(int i = 0; i < liste.length; i++)
		{
			if(liste[i].isFile())
			{
				listeMusiques[i] = liste[i].getName();
				System.out.println(liste[i].getName());
			}
		}

		return listeMusiques;
	}

	// permet de lire une rentrée clavier et de retourner un string contenant ce qui a été lu
	public static String lireString() 
	{
		byte b = -1;
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		String str = new String("");

		while (b == -1)
		{
			try 
			{
				str = bufferedReader.readLine();
				b = 0;
			} 

			catch (IOException iOException) { System.out.println("Erreur : saisissez une chaine"); } 
		} 
		return str;
	}


	// sert à arrêter la musique en cours de lecture 
	public static void arreterMusique()
	{
		mp3Player.stop();
	}

	// sert à mettre sur pause la musique en cours de lecture 
	public static void pauseMusique()
	{
		mp3Player.pause();
	}

	// getStatus() : retourne le status de la musique
	public static boolean getStatus()
	{
		// si la musique est stoppée le programme retourne true sinon il retourne false
		if (mp3Player.isStopped())
			return true;
		else
			return false;
	}
}