import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerBanco implements Runnable {

    private static Banco banco1 = new Banco();
    // private static Banco banco2 = new Banco();
    // private static Banco banco3 = new Banco();

    // Declare din and dout attributes
    private DataInputStream din;
    private DataOutputStream dout;
    private Socket socket;

    public ServerBanco(DataInputStream din, DataOutputStream dout, Socket socket) {
        this.din = din;
        this.dout = dout;
        this.socket = socket;
    }

    public static void main(String args[]) throws Exception {
        ServerSocket ss = new ServerSocket(3333);

        String str = "";
        while (!str.equals("stop")) {
            Socket s = ss.accept();
            DataInputStream din = new DataInputStream(s.getInputStream());
            DataOutputStream dout = new DataOutputStream(s.getOutputStream());
            ServerBanco server = new ServerBanco(din, dout, s);
            Thread hilo = new Thread(server);
            hilo.start();
            // System.out.println("Cerrando conexion");
            // din.close();
            // s.close();
        }
        ss.close();
    }

    @Override
    public void run() {
        banco1.procesarPeticiones(din, dout);
        try {
            din.close();
            socket.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
