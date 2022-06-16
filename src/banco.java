import java.net.*;
import java.io.*;

//El banco hace funciones de servidor
public class Banco {

    private int tabaco = 10;
    private int papel = 10;
    private int fosforos = 10;

    private String seleccionarIngrediente() {
        String ingrediente = "";
        if (this.tabaco > 0 || this.papel > 0 || this.fosforos > 0) {
            while (ingrediente == "") {
                int random = (int) (Math.random() * 3);
                switch (random) {
                    case 0:
                        if (tabaco > 0) {
                            ingrediente = "tabaco";
                            this.tabaco--;
                            System.out.println("Banco: " + this.tabaco + " tabaco");
                        }
                        break;
                    case 1:
                        if (papel > 0) {
                            ingrediente = "papel";
                            this.papel--;
                            System.out.println("Banco: " + this.papel + " papel");
                        }
                        break;
                    case 2:
                        if (fosforos > 0) {
                            ingrediente = "fosforos";
                            this.fosforos--;
                            System.out.println("Banco: " + this.fosforos + " fosforos");
                        }
                        break;
                }
            }
            return ingrediente;
        } else {
            return "vacio";
        }

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
