import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Date;

public class Servidor {

    public static void main(String[] args){
        MulticastSocket sock = null;


        try{

            //Se establece la conexion
            InetAddress group = InetAddress.getByName("225.0.0.100");
            sock = new MulticastSocket(6789);
            sock.joinGroup(group);

            //Se invocan a los hilos send y receive
            Thread hiloEnviar=new threadSend("hiloEnviar",group,sock);
            Thread hiloRecibir=new threadReceive("hiloRecibir",group,sock);

            hiloEnviar.start();
            hiloRecibir.start();


        }
        catch (SocketException e){
            System.out.println("Socket: " + e.getMessage());
        }catch (IOException e){
            System.out.println("IO: " + e.getMessage());
        }
    }


}
