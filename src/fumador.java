import java.net.*;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    public void writelog(String actor, String accion, Integer cant, Date fecha) throws IOException{
        
        File file = new File ("/Users/Usuario/Desktop/LogsFumador.txt");
        FileWriter writer = new FileWriter(file, true);
        writer.write("el "+ actor+accion+",cantidad "+cant+" Fecha del sistema: "+fecha+"\n");
        writer.close();

    }

    // Por ahora todo empieza en 0, mas adelante hay que validar lo que dijo la
    // profe de un insumo infinito

    public void buscarIngredientes(DataInputStream din, DataOutputStream dout) throws IOException {
        DateTimeFormatter dtf5 = DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm");
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
            //imprime el log 
            //writelog("fumador recibió: ", response, cant, dtf5.format(LocalDateTime.now()));
        } catch (Exception e) {
            System.out.println("Fumador: Error al buscar ingredientes...: " + e.toString());
        }
    }

    public int fumar() {
        DateTimeFormatter dtf5 = DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm");
        if ((this.tabaco > 0 || this.infinito == 1) && (this.papel > 0 || this.infinito == 2)
                && (this.fosforos > 0 || this.infinito == 3)) {
            System.out.println("Fumador: Fumando...");
            //imprime el log 
            //writelog("fumador","fumó un cigarrro", 1, dtf5.format(LocalDateTime.now()));
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
        DateTimeFormatter dtf5 = DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm");
        try {
            if (this.buscadasConsecutivas >= 2) {
                System.out.println("Fumador: Solicitando ingredientes al vendedor...");
                //imprime el log 
                //writelog("fumador","solicitó un", 1, dtf5.format(LocalDateTime.now()));
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

    public static Socket encontrarBanco(fumador f) throws Exception {
        if (f.tabaco == 0 && f.infinito != 1) {
            return new Socket("localhost", 3333);
        } else if (f.papel == 0 && f.infinito != 2) {
            return new Socket("localhost", 3334);
        } else if (f.fosforos == 0 && f.infinito != 3) {
            return new Socket("localhost", 3335);
        } else
            throw new Exception("Fumador: No necesita ingredientes...");
    }

    public static void main(String args[]) throws Exception {
        int parada = 0;
        int tipo = 0;
        while (tipo == 0) {
            System.out.println("Escoja el ingrediente que será infinito:");
            System.out.println("1.- Tabaco");
            System.out.println("2.- Papel");
            System.out.println("3.- Fosforos");
            tipo = Integer.parseInt(System.console().readLine());
            if (tipo != 1 && tipo != 2 && tipo != 3) {
                System.out.println("Ingrese un numero valido");
                tipo = 0;
            }
        }
        fumador f = new fumador(tipo);
        while (parada != 1) {
            Socket s = encontrarBanco(f);
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
