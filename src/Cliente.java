import java.io.*;
import java.net.*;

public class Cliente{
    public static void main(String[] args){
        MulticastSocket sock = null;
        byte[] buffer = new byte[100];
        int[] secuencia = new int[15]; //Variable para almacenar la secuencia de cada cliente
        int min_val=1;
        int max_val=90;
        try{
            InetAddress group = InetAddress.getByName("225.0.0.100");
            sock = new MulticastSocket(6789);
            sock.joinGroup(group);

                DatagramPacket mIn = new DatagramPacket(buffer,buffer.length);
                sock.receive(mIn);
                System.out.println("Mensaje recibido" + new String(mIn.getData()));


        }catch (SocketException e){
            System.out.println("Socket: " + e.getMessage());
        }catch (IOException e){
            System.out.println("IO: " + e.getMessage());
        }finally {
            if(sock!= null) sock.close();
        }
    }


}