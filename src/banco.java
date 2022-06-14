import java.net.*;
import java.io.*;

//El banco hace funciones de servidor
public class banco {

    private int tabaco = 0;
    private int papel = 0;
    private int fosforos = 0;

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

    public void procesarPeticiones(DataInputStream din, DataOutputStream dout) {
        try {
            String str = "";
            str = din.readUTF();
            switch (str) {
                case "BI": // Fumador Buscando ingredientes
                    dout.writeUTF(this.seleccionarIngrediente());
                    break;
                case "ST": // Vendedor Sumando Tabaco
                    this.tabaco++;
                    // Print sumando tabaco
                    System.out.println("Banco: Sumando tabaco...");
                    break;
                // Vendedor Sumando Papel
                case "SP":
                    this.papel++;
                    System.out.println("Banco: Sumando papel...");
                    break;
                case "SF": // Vendedor Sumando Fosforos
                    this.fosforos++;
                    System.out.println("Banco: Sumando fosforos...");
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

        String str = "";
        banco banco = new banco();
        while (!str.equals("stop")) {
            Socket s = ss.accept();
            DataInputStream din = new DataInputStream(s.getInputStream());
            DataOutputStream dout = new DataOutputStream(s.getOutputStream());
            banco.procesarPeticiones(din, dout);
            din.close();
            s.close();
        }
        // din.close();
        // s.close();
        ss.close();
    }
}
