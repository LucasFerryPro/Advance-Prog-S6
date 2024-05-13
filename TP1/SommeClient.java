import java.io.*;
import java.net.*;

public class SommeClient {
    public static void main(String[] args) {
        
        BufferedReader br = null;
        PrintStream ps = null;
        String line = null;
        Socket sock = null;
        int port = -1;

        if (args.length != 2) {
            System.out.println("usage: SommeClient ip_server port");
            System.exit(1);
        }

        //Connexion au serveur
        try {
            port = Integer.parseInt(args[1]);
            sock = new Socket(args[0], port);
        } catch (IOException e) {
            System.out.println("problème de connexion au serveur : " + e.getMessage());
            System.exit(1);
        }

        try {

            //ouvrire les flux
            br = new BufferedReader(new InputStreamReader(sock.getInputStream()));  //lire les reponses du server
            ps = new PrintStream(sock.getOutputStream()); //envoyer les requetes au server
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in)); //lire les input du user

            //boucle pour envoyer les requetes au server
            while (true) {
                System.out.print("Entrez les nombres entier a additionner (ex: 1,58,64,2)");
                line = userInput.readLine(); //on récupère la ligne de l'utilisateur

                if (line == null || line.isEmpty()) {  //Sortir du server
                    break;
                }

                boolean invalid = false;

                String[] lineStrings = line.split(",");

                int[] numbers = new int[lineStrings.length];

                for (int i = 0; i < lineStrings.length; i++) {
                    if (lineStrings[i] != null && lineStrings[i].matches("[0-9]+")){
                        numbers[i] = Integer.parseInt(lineStrings[i]);
                    }else{
                        invalid = true;
                        line="";
                        lineStrings = new String[]{};
                        numbers = new int[]{};
                        System.out.println("Mauvaise requete -> non envoye au server");
                        break;
                    }
                }
                
                if(!invalid){
                    ps.println(lineStrings.length);
                    for (int i : numbers) {
                        ps.println(i);
                    }
                    line = br.readLine(); //lire la reponse du server
                    System.out.println("Le serveur repond : " + line); // affichage de la réponse du serveur
                }
            }

            //fermer les flux
            br.close();
            ps.close();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
