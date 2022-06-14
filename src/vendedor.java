import java.net.*;
import java.io.*;

//El vendedor hace funciones de cliente
public class vendedor {

    public String seleccionarIngrediente() {
        String ingrediente = "";
        int random = (int) (Math.random() * 3);
        switch (random) {
            case 0:
                ingrediente = "ST"; // Sumar Tabaco
                break;
            case 1:
                ingrediente = "SP"; // Sumar Papel
                break;
            case 2:
                ingrediente = "SF"; // Sumar Fosforos
                break;
        }
        return ingrediente;
    }

    public void depositarIngredientes() throws Exception {

        Socket s = new Socket("localhost", 3333);
        DataInputStream din = new DataInputStream(s.getInputStream());
        DataOutputStream dout = new DataOutputStream(s.getOutputStream());
        String ingrediente = this.seleccionarIngrediente();
        // Print depositando
        System.out.println("Vendedor: Depositando ingrediente: " + ingrediente);
        dout.writeUTF(ingrediente);
        dout.flush();
        dout.close();
        s.close();
    }

    public void procesarPeticiones(DataInputStream din, DataOutputStream dout) {
        try {
            String str = "";
            str = din.readUTF();
            switch (str) {
                case "SI": // Fumador Solicitando ingredientes
                    this.depositarIngredientes();
                    break;
            }
            dout.flush();
        }
        // Catch end of file exception
        catch (EOFException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Fumador: Error al dar Ingredientes");
        }
    }

    public static void main(String args[]) throws Exception {
        // Socket Para recibir las peticiones del fumador como servidor
        // Fs = Fumador Socket
        ServerSocket ss = new ServerSocket(4444);

        String str = "";
        vendedor vendedor = new vendedor();
        while (!str.equals("stop")) {
            Socket Fs = ss.accept();
            DataInputStream Fdin = new DataInputStream(Fs.getInputStream());
            DataOutputStream Fdout = new DataOutputStream(Fs.getOutputStream());
            vendedor.procesarPeticiones(Fdin, Fdout);
            Fdin.close();
            Fs.close();
        }
        ss.close();
    }
}
