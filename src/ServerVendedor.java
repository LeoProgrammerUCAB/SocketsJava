import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerVendedor implements Runnable {

    private static vendedor vendedor = new vendedor();

    // Declare din and dout attributes
    private DataInputStream din;
    private DataOutputStream dout;
    private Socket socket;

    public ServerVendedor(DataInputStream din, DataOutputStream dout, Socket socket) {
        this.din = din;
        this.dout = dout;
        this.socket = socket;
    }

    @Override
    public void run() {
        vendedor.procesarPeticiones(din, dout);
        try {
            din.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) throws Exception {
        // Socket Para recibir las peticiones del fumador como servidor
        // Fs = Fumador Socket
        ServerSocket ss = new ServerSocket(4444);

        String str = "";

        while (!str.equals("stop")) {
            Socket Fs = ss.accept();
            DataInputStream Fdin = new DataInputStream(Fs.getInputStream());
            DataOutputStream Fdout = new DataOutputStream(Fs.getOutputStream());
            ServerVendedor server = new ServerVendedor(Fdin, Fdout, Fs);
            Thread hilo = new Thread(server);
            hilo.start();
        }
        ss.close();
    }

}
