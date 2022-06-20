import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.*;

//El banco hace funciones de servidor
public class Banco {

    private int noTiene; // 1, 2 o 3
    private int tabaco = 0;
    private int papel = 0;
    private int fosforos = 0;

    public Banco(int noTiene) {
        if (noTiene == 1 || noTiene == 2 || noTiene == 3) {
            this.noTiene = noTiene;
        } else {
            this.noTiene = 1;
        }
    }

    private String seleccionarIngrediente() {
        String ingrediente = "";
        if (this.tabaco > 0 || this.papel > 0 || this.fosforos > 0) {
            while (ingrediente == "") {
                int random = (int) (Math.random() * 3);
                switch (random) {
                    case 0:
                        if (tabaco > 0 && noTiene != 1) {
                            ingrediente = "tabaco";
                            this.tabaco--;
                            System.out.println("Banco: " + this.tabaco + " tabaco");
                        }
                        break;
                    case 1:
                        if (papel > 0 && noTiene != 2) {
                            ingrediente = "papel";
                            this.papel--;
                            System.out.println("Banco: " + this.papel + " papel");
                        }
                        break;
                    case 2:
                        if (fosforos > 0 && noTiene != 3) {
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
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        try {
            // Delay 2 seconds
            Thread.sleep(2000);
            String str = "";
            str = din.readUTF();
            switch (str) {
                case "BI": // Fumador Buscando ingredientes
                    dout.writeUTF(this.seleccionarIngrediente() + ":" + dtf.format(now));
                    break;
                case "ST": // Vendedor Sumando Tabaco
                    this.tabaco++;
                    dout.writeUTF("ST:" + dtf.format(now));
                    System.out.println("Banco: Sumando tabaco...");
                    break;
                case "SP":// Vendedor Sumando Papel
                    this.papel++;
                    dout.writeUTF("SP:" + dtf.format(now));
                    System.out.println("Banco: Sumando papel...");
                    break;
                case "SF": // Vendedor Sumando Fosforos
                    this.fosforos++;
                    dout.writeUTF("SF:" + dtf.format(now));
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
