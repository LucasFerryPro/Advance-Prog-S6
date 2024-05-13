import java.io.*;
import java.net.*;

public class SommeServer {
    public static void main(String[] args) {
        
        BufferedReader br = null;
        PrintStream ps = null;
        String line = null;
        ServerSocket serverSocket = null;
        int port = -1;

        if (args.length != 1) {
            System.out.println("usage: SommeServer port");
            System.exit(1);
        }

        //Creation du server
        try {
            port = Integer.parseInt(args[0]);
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("probleme creation socket serveur : " + e.getMessage());
            System.exit(1);
        }

        try {
            while (true) {
                Socket clientSocket = serverSocket.accept(); // attente connexion client
                br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); //flux lecture
                ps = new PrintStream(clientSocket.getOutputStream()); //flux ecriture

                int i = 0;
                int nbInt = 0;
                int somme = 0;

                //traitement des requetes
                while((line = br.readLine()) != null && !line.isEmpty()){
                    if(i==0) System.out.println("DEBUG");

                    System.out.println("line : "+line + " i = " + i + " nbInt = " + nbInt);
                    if (i == 0){
                        nbInt = Integer.parseInt(line);
                        System.out.println("nbInt = "+line);
                        i++;
                    } 
                    else{
                        if(i <= nbInt){
                            System.out.println("nombre:"+line);
                            somme += Integer.parseInt(line);
                            i++;
                            if(i > nbInt){
                                System.out.println("\nRESULTAT\nLa somme est : " + somme);
                                ps.println("La somme est : " + somme);
                                i = 0;
                                somme = 0;
                                nbInt = 0;
                            }
                        }
                    }
                }

                //Fermer les flux et la socket du client
                br.close();
                ps.close();
                clientSocket.close();
            }
        } catch (IOException e) {
            System.out.println("Requete invalide : " + e.getMessage());
        }
    }
}
