import java.io.*;
import java.net.*;

public class Servidor {
    public static void main(String[] args){
        MulticastSocket sock = null;
        String mensaje = "Hola";
        try{
            InetAddress group = InetAddress.getByName("225.0.0.100");
            sock = new MulticastSocket(6789);
            sock.joinGroup(group);
            byte[] mBytes = mensaje.getBytes();
            //DatagramPacket mOut = new DatagramPacket(mBytes,mensaje.length(),group,sock.getLocalPort());
            DatagramPacket mOut = new DatagramPacket(mBytes,mensaje.length(),group,6789);
            sock.send(mOut);
        }
        catch (SocketException e){
            System.out.println("Socket: " + e.getMessage());
        }catch (IOException e){
            System.out.println("IO: " + e.getMessage());
        }finally {
            if(sock!= null) sock.close();
        }
    }


}
