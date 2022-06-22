import java.net.*;
import java.sql.Date;
import java.text.FieldPosition;
import java.time.format.DateTimeFormatter;
import java.io.*;

//El vendedor hace funciones de cliente
public class vendedor {

    public void writelogvendedor(String actor, String accion, Integer cant, Date fecha) throws IOException{
        
        File file = new File ("/Users/Usuario/Desktop/LogsVendedor.txt");
        FileWriter writer = new FileWriter(file, true);
        writer.write("el "+ actor+accion+",cantidad "+cant+" Fecha del sistema: "+fecha+"\n");
        writer.close();

    }

    private void seleccionar2Bancos() throws UnknownHostException, IOException, Exception {
        int banco1 = 0;
        int banco2 = 0;
        // Seleccionar dos numeros diferentes del 1 al 3
        while (banco1 == banco2) {
            banco1 = (int) (Math.random() * 3) + 1;
            banco2 = (int) (Math.random() * 3) + 1;
        }
        // Switch from 1 to 3
        switch (banco1) {
            case 1:
                this.depositarIngredientes(new Socket("localhost", 3333));
                break;
            case 2:
                this.depositarIngredientes(new Socket("localhost", 3334));
                break;
            case 3:
                this.depositarIngredientes(new Socket("localhost", 3335));
                break;
        }
        switch (banco2) {
            case 1:
                this.depositarIngredientes(new Socket("localhost", 3333));
                break;
            case 2:
                this.depositarIngredientes(new Socket("localhost", 3334));
                break;
            case 3:
                this.depositarIngredientes(new Socket("localhost", 3335));
                break;
        }
    }

    public void depositarIngredientes(Socket socket) throws Exception {
        DateTimeFormatter dtf5 = DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm");
        DataInputStream din = new DataInputStream(socket.getInputStream());
        DataOutputStream dout = new DataOutputStream(socket.getOutputStream());

        String ingrediente = "SI";
        // Print depositando
        System.out.println("Vendedor: Depositando ingrediente: " + ingrediente);
        //imprime el log 
        //writelog("Vendedor Depositó: ",ingrediente, 1, dtf5.format(LocalDateTime.now()));
        dout.writeUTF(ingrediente);
        dout.flush();
        dout.close();
        socket.close();
    }

    public void procesarPeticiones(DataInputStream din, DataOutputStream dout) {
        try {
            String str = "";
            str = din.readUTF();
            switch (str) {
                case "SI": // Fumador Solicitando ingredientes
                    this.seleccionar2Bancos();
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
