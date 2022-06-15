import java.net.*;
import java.io.*;

//El banco hace funciones de servidor
public class Banco {

    private int tabaco = 0;
    private int papel = 0;
    private int fosforos = 0;

    private String seleccionarIngrediente() {
        String ingrediente = "";
        int random = (int) (Math.random() * 3);
        switch (random) {
            case 0:
                ingrediente = "tabaco";
                this.tabaco--;
                break;
            case 1:
                ingrediente = "papel";
                this.papel--;
                break;
            case 2:
                ingrediente = "fosforos";
                this.fosforos--;
                break;
        }
        return ingrediente;
    }

    public synchronized void procesarPeticiones(DataInputStream din, DataOutputStream dout) {
        try {
            // Delay 2 seconds
            Thread.sleep(2000);
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
            System.out.println("Fumador: Error al dar Ingredientes:" + e.getMessage());
        }
    }
}
