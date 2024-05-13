import java.io.*;
import java.net.*;

public class PositionClient {
    public static void main(String[] args) {

        BufferedReader br = null; // pour lire du texte sur la socket
        PrintStream ps = null; // pour écrire du texte sur la socket
        String line = null;
        Socket sock = null;
        int port = -1;

        if (args.length != 2) {
            System.out.println("usage: EchoClient ip_server port");
            System.exit(1);
        }

        try {
            port = Integer.parseInt(args[1]); // récupération du port sous forme int
            sock = new Socket(args[0], port); // création socket client et connexion au serveur donné en args[0]
        } catch (IOException e) {
            System.out.println("problème de connexion au serveur : " + e.getMessage());
            System.exit(1);
        }

        try {
            br = new BufferedReader(new InputStreamReader(sock.getInputStream())); // création flux lecture lignes de texte
            ps = new PrintStream(sock.getOutputStream()); // création flux écriture lignes de texte

            System.out.println("ID client : " + br.readLine());

            for (int i = 0; i < 10; i++) {
                ps.println("1");
                ps.println(i+","+i+","+i); // envoi du texte au serveur
                line = br.readLine(); // lecture réponse serveur
                System.out.println("Le serveur me repond : " + line); // affichage de la réponse du serveur
            }

            ps.println("2");
            line = br.readLine(); // lecture réponse serveur
            System.out.println("Le serveur me repond : " + line); // affichage de la réponse du serveur

            ps.println("3");
            ps.println("0.5,6,6,6"); // envoi du texte au serveur
            line = br.readLine(); // lecture réponse serveur
            System.out.println("Le serveur me repond : " + line); // affichage de la réponse du serveur

            ps.println("3");
            ps.println("0.5,6,6,8"); // envoi du texte au serveur
            line = br.readLine(); // lecture réponse serveur
            System.out.println("Le serveur me repond : " + line); // affichage de la réponse du serveur

            br.close();
            ps.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}

