import java.net.*;
import java.io.*;

//El banco hace funciones de servidor
public class banco {

    public String seleccionarIngrediente() {
        String ingrediente = "";
        int random = (int) (Math.random() * 3);
        switch (random) {
            case 0:
                ingrediente = "tabaco";
                break;
            case 1:
                ingrediente = "papel";
                break;
            case 2:
                ingrediente = "fosforos";
                break;
        }
        return ingrediente;
    }

    public void darIngrediente(DataInputStream din, DataOutputStream dout) {
        try {
            String str = "";
            str = din.readUTF();
            switch (str) {
                case "BI":
                    dout.writeUTF(this.seleccionarIngrediente());
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
        ServerSocket ss = new ServerSocket(3333);
        // Socket s = ss.accept();
        // DataInputStream din = new DataInputStream(s.getInputStream());
        // DataOutputStream dout = new DataOutputStream(s.getOutputStream());

        String str = "";
        banco banco = new banco();
        while (!str.equals("stop")) {
            Socket s = ss.accept();
            DataInputStream din = new DataInputStream(s.getInputStream());
            DataOutputStream dout = new DataOutputStream(s.getOutputStream());
            banco.darIngrediente(din, dout);
            din.close();
            s.close();
        }
        // din.close();
        // s.close();
        ss.close();
    }
}
