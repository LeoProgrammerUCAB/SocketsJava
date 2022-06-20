import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.*;

enum Ingredientes {
    tabaco,
    papel,
    fosforos
}

// El banco hace funciones de servidor
public class Banco {

    private Ingredientes tipo;
    private int ingrediente = 2;

    public Banco(Ingredientes tipo) {
        this.tipo = tipo;
    }

    private String seleccionarIngrediente() {
        if (this.ingrediente > 0) {
            this.ingrediente--;
            // Print how much is left
            System.out.println("Banco: " + this.tipo.toString() + ": " + this.ingrediente + " unidades restantes");
            return this.tipo.toString();
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
                case "SI": // Vendedor Sumando ingredientes
                    this.ingrediente++;
                    dout.writeUTF("IS:" + dtf.format(now));
                    System.out.println("Banco: Sumando " + this.tipo.toString() + " ...");
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
