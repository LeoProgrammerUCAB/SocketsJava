import java.net.*;
import java.io.*;

//El fumador hace funciones de cliente
public class fumador {

    private int tabaco = 0;
    private int papel = 0;
    private int fosforos = 0;
    private int buscadasConsecutivas = 0;
    private int infinito; // 1, 2 o 3

    public fumador(int infinito) {
        if (infinito == 1 || infinito == 2 || infinito == 3) {
            this.infinito = infinito;
        } else {
            this.infinito = 1;
        }
    }

    // Por ahora todo empieza en 0, mas adelante hay que validar lo que dijo la
    // profe de un insumo infinito

    public void buscarIngredientes(DataInputStream din, DataOutputStream dout) throws IOException {
        try {
            System.out.println("Fumador: Buscando ingredientes...");
            // BI: Buscando ingredientes
            String str = "BI", response = "";
            dout.writeUTF(str);
            dout.flush();
            response = din.readUTF();
            // Split response by :
            String[] str2 = response.split(":");
            switch (str2[0]) {
                case "tabaco":
                    this.tabaco++;
                    break;
                case "papel":
                    this.papel++;
                    break;
                case "fosforos":
                    this.fosforos++;
                    break;
                case "vacio":
                    System.out.println("Fumador: El banco no retornó ingredientes...");
                    break;
            }
            this.buscadasConsecutivas++;
            System.out.println("Fumador: Ingrediente Recibido " + response);
        } catch (Exception e) {
            System.out.println("Fumador: Error al buscar ingredientes...: " + e.toString());
        }
    }

    public int fumar() {
        // Print fumando
        if (this.tabaco > 0 && this.papel > 0 && this.fosforos > 0) {
            System.out.println("Fumador: Fumando...");
            this.buscadasConsecutivas = 0;
            this.fosforos--;
            this.papel--;
            this.tabaco--;
            return 1;
        } else {
            System.out.println("Fumador: No hay ingredientes para fumar...");
            return 0;
        }
    }

    public void solicitarIngredientes() {
        try {
            if (this.buscadasConsecutivas >= 2) {
                System.out.println("Fumador: Solicitando ingredientes al vendedor...");
                Socket s = new Socket("localhost", 4444);
                DataInputStream din = new DataInputStream(s.getInputStream());
                DataOutputStream dout = new DataOutputStream(s.getOutputStream());
                // SI: Solicitando ingredientes (Debe solicitar esto al vendedor)
                String str = "SI";
                dout.writeUTF(str);
                dout.flush();
                // Cerrando
                dout.close();
                s.close();
                this.buscadasConsecutivas = 0;
            }
        } catch (Exception e) {
            System.out.println("Fumador: Error al buscar ingredientes...");
        }
    }

    public static void main(String args[]) throws Exception {
        int parada = 0;
        fumador f = new fumador(1);
        while (parada != 1) {
            Socket s = new Socket("localhost", 3333);
            DataInputStream din = new DataInputStream(s.getInputStream());
            DataOutputStream dout = new DataOutputStream(s.getOutputStream());
            f.buscarIngredientes(din, dout);
            parada = f.fumar();
            f.solicitarIngredientes();
            dout.close();
            s.close();
        }
    }
}