import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("usage: PositionServer port");
            System.exit(1);
        }

        try {
            int port = Integer.parseInt(args[0]); // récupération du port sous forme int
            PositionServer server = new PositionServer(port);
            server.execute();
        } catch (NumberFormatException e) {
            System.err.println("invalid port number");
            System.exit(1);
        } catch (IOException ioe) {
            System.err.println("could not listen on port");
            System.exit(1);
        }
    }
}


