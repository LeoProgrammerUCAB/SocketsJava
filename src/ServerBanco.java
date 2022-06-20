import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerBanco implements Runnable {

    private static Banco banco;

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
        ServerSocket ss = null;
        while (ss == null) {
            System.out.println("Ingrese el tipo de banco a utilizar: ");
            int tipo = Integer.parseInt(System.console().readLine());
            switch (tipo) {
                case 1:
                    ss = new ServerSocket(3333);
                    banco = new Banco(Ingredientes.tabaco);
                    break;
                case 2:
                    ss = new ServerSocket(3334);
                    banco = new Banco(Ingredientes.papel);
                    break;
                case 3:
                    ss = new ServerSocket(3335);
                    banco = new Banco(Ingredientes.fosforos);
                    break;
                default:
                    System.out.println("Escoge un ingrediente valido");
            }
        }

        String str = "";
        while (!str.equals("stop")) {
            if (ss != null) {
                Socket s = ss.accept();
                DataInputStream din = new DataInputStream(s.getInputStream());
                DataOutputStream dout = new DataOutputStream(s.getOutputStream());
                ServerBanco server = new ServerBanco(din, dout, s);
                Thread hilo = new Thread(server);
                hilo.start();
            } else {
                break;
            }
        }
        ss.close();
    }

    @Override
    public void run() {
        banco.procesarPeticiones(din, dout);
        try {
            din.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
