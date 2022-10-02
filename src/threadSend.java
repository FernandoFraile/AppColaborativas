import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class threadSend extends Thread {

    //Variables
    private InetAddress group;
    private MulticastSocket sock ;

    //Constructor
    public threadSend(String name,InetAddress group, MulticastSocket Sock) {
        super(name);
        this.group=group;
        this.sock=Sock;
    }
    //Ejecucion
    @Override
    public void run(){

        ArrayList<Integer> numeros=new ArrayList<Integer>();
        //Variable para almacenar los números que pueden salir e ir eliminando,
        // con el fin de que no se repitan en la secuecuencia.
        int min_val=1;
        int max_val=90; //Valores mínimmo y máximo que puede haber en el bingo
        byte[] mBytes = new byte[1];
        Date semilla= new Date();
        Random rand = new Random(semilla.getTime());
        int bola; //Variable para enviar el número que sale
        int flag=0;

        try{
            //Creamos el byte que se va a enviar con el numero aleatorio que se ha generado
            
            bola = min_val + rand.nextInt((max_val - min_val) + 1);
            mBytes[0] = (byte) bola;
            //DatagramPacket mOut = new DatagramPacket(mBytes,mensaje.length(),group,sock.getLocalPort());
            DatagramPacket mOut = new DatagramPacket(mBytes,mBytes.length,group,6789);



            //Empieza a sacar las 90 bolas (89 porque ya se indroduce una para hacer la comprobacion del array de numeros)
            for(int i=0;i<90;i++){
                Thread.sleep(10);

                flag=0;
                while(flag==0){
                    bola = min_val + rand.nextInt((max_val - min_val) + 1);
                    if(!numeros.contains(bola)){
                        numeros.add(bola);

                        flag=1;
                    }

                }
                mBytes[0]= (byte) bola;
                mOut.setData(mBytes);

                System.out.println( (int) mOut.getData()[0]);
                sock.send(mOut);


            }
            System.out.println("Acavb");


        }catch (IOException e){
            System.out.println("IO: " + e.getMessage());
        }catch (InterruptedException e) {
            System.out.println("InterruptedExceptioon: " + e.getMessage());
        }finally {
            if(!sock.isClosed()){
                System.out.println("threadSend cierra el socket");

                sock.close();
            }
        }


    }
}


