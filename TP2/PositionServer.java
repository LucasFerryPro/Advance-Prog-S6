import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PositionServer {

    static List<Position> positions = new ArrayList<Position>();

    BufferedReader br = null; // pour lire du texte sur la socket
    PrintStream ps = null; // pour envoyer du texte sur la socket
    ServerSocket serverSocket = null;
    Socket clientSocket = null;
    int idClient;

    public PositionServer(int port) throws IOException{
        idClient = 0;
        serverSocket = new ServerSocket(port); // création socket serveur
    }

    public void checkArgs(String argsReq, int numReq){

        String regex;
        Pattern pattern;
        Matcher matcher;

        switch (numReq) {
            case 1:
                regex = "\\d+,\\d+,\\d+";
                pattern = Pattern.compile(regex);
                matcher = pattern.matcher(argsReq);
                if(!matcher.matches())
                    throw new IllegalArgumentException("Invalid args for request 1");
                break;
            
            case 3:
                regex = "\\d+,\\d+,\\d+,(\\d+(\\.\\d+)?)";
                pattern = Pattern.compile(regex);
                matcher = pattern.matcher(argsReq);
                if(!matcher.matches())
                    throw new IllegalArgumentException("Invalid args for request 3");
                break;
        
            default:
                break;
        }
    }

    public String requete1() throws IOException{
        String res = "";

        try {
            String argsReq = br.readLine();

            checkArgs(argsReq, 1);

            String[] coord = argsReq.split(",");

            Position position =  new Position(Integer.parseInt(coord[0]), Integer.parseInt(coord[1]), Integer.parseInt(coord[2]));

            positions.add(position);

            res = "OK";
        } catch (IllegalArgumentException e) {
            System.out.println("ERR_REQ: " + e.getMessage());
            res = "ERR_REQ: " + e.getMessage();
        }
        
        return res;
    }

    public String requete2(){
        double res = 0;

        for (int i = 0; i < positions.size()-1; i++) {
            res += positions.get(i).distanceTo(positions.get(i+1));
        }

        return Double.toString(res);
    }

    public String requete3() throws IOException{
        String res = "FALSE";

        try {
            String argsReq = br.readLine();

            checkArgs(argsReq, 3);

            String[] coord = argsReq.split(",");

            Position tempPosition =  new Position(Integer.parseInt(coord[1]), Integer.parseInt(coord[2]), Integer.parseInt(coord[3]));

            for (Position position : positions) {
                if(position.equals(tempPosition, Double.parseDouble(coord[0]))){
                    res = "TRUE";   
                }          
            }
        } catch (IllegalArgumentException e) {
            System.out.println("ERR_REQ: " + e.getMessage());
            res = "ERR_REQ: " + e.getMessage();
        }

        return res;
    }

    public String computeResult(int numReq) throws IOException, Exception{
        String res = "";

        switch (numReq) {
            case 1:
                res = requete1();
                break;
            case 2:
                res = requete2();
                break;
            case 3:
                res = requete3();
                break;
        
            default:
                throw new Exception("Error on compute result");
        }

        return res;
    }

    public void execute() throws IOException{

        String line = null; // la ligne reçue/envoyée

        while(true){
            clientSocket = serverSocket.accept(); // attente connexion client et création socket dédiée

            try {
                br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); // création flux lecture lignes de texte
                ps = new PrintStream(clientSocket.getOutputStream()); // création flux écriture lignes de texte

                idClient++;
                ps.println(idClient);

                int numReq = -1;

                while ((line = br.readLine()) != null && !line.isEmpty()) {
                    numReq = Integer.parseInt(line);
                    String res = computeResult(numReq);
                    System.out.println(res);
                    ps.println(res);
                }  

            } catch (IOException ioe) {
                System.out.println(ioe.getMessage());
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }
}
