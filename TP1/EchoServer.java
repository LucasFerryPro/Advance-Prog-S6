import java.io.*;
import java.net.*;

class EchoServer {
    public static void main(String[] args) {

        BufferedReader br = null; // pour lire du texte sur la socket
        PrintStream ps = null; // pour envoyer du texte sur la socket
        String line = null; // la ligne reçue/envoyée
        ServerSocket serverSocket = null;

        int port = -1;

        if (args.length != 1) {
            System.out.println("usage: Server port");
            System.exit(1);
        }

        try {
            port = Integer.parseInt(args[0]); // récupération du port sous forme int
            serverSocket = new ServerSocket(port); // création socket serveur
        } catch (IOException e) {
            System.out.println("probleme creation socket serveur : " + e.getMessage());
            System.exit(1);
        }

        try {
            while (true) {
                Socket clientSocket = serverSocket.accept(); // attente connexion client
                br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); // création flux lecture lignes de texte
                ps = new PrintStream(clientSocket.getOutputStream()); // création flux écriture lignes de texte

                while ((line = br.readLine()) != null && !line.isEmpty()) {
                    System.out.println("Le client me dit : " + line); // affichage debug
                    ps.println(line); // envoi de la ligne précédemment reçue
                }

                // Fermer les flux et la socket du client
                br.close();
                ps.close();
                clientSocket.close();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
