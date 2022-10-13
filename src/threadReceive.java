import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;


public class threadReceive extends Thread{

    private InetAddress group;
    private MulticastSocket sock ;

    public threadReceive(String name,InetAddress group, MulticastSocket Sock) {
        super(name);
        this.group=group;
        this.sock=Sock;
    }

    @Override
    public void run() {
        byte[] buffer = new byte[10];
        int flag;
        try{
            //Se espera a que llegue un mensaje de algun cliente
            DatagramPacket mIn = new DatagramPacket(buffer,buffer.length);

            sock.receive(mIn);

            flag=mIn.getLength();
            while (flag==2){ //Se comprueba si el mensaje que le llega es un numero o la palabra bingo
                sock.receive(mIn);
                flag=mIn.getLength();

            }
            Servidor.DetenerHilo=1; //Se cambia la variable global para detener el hilo que envia las bolas

            System.out.println("Hay ganador del bingo!! ");


        }catch (IOException e){
            System.out.println("IO: " + e.getMessage());
        }finally {
            if(!sock.isClosed()){ //Se comprueba si la conexion ha sido cerrada por el otro hilo
                if(sock!= null){
                    try{
                        sock.leaveGroup(group);
                        System.out.println("threadReceive cierra el socket");
                        sock.close();
                    }catch (IOException e){
                        System.out.println("IO: " + e.getMessage());
                    }

                }

            }
        }
    }
}
