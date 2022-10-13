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
        byte[] mBytes = null;
        Date semilla= new Date();
        Random rand = new Random(semilla.getTime());
        int bola; //Variable para enviar el número que sale
        int flag=0;
        String bolaS;
        int i=0; //Para bucles

        try{
            //Creamos el byte que se va a enviar con el numero aleatorio que se ha generado
            
            bola = min_val + rand.nextInt((max_val - min_val) + 1);
            //Se almacenan los datos como enteros, para que sea más cómodo realizar la comprobación de si están.
            //Posteriormente se hará el cambio a string par enviarlo y añadir el 0 a la izquierda si es necesario
            numeros.add(bola);
            if(bola<10){
                bolaS="0"+ String.valueOf(bola);
            }
            else {
                bolaS = String.valueOf(bola);
            }

            mBytes = bolaS.getBytes();
            DatagramPacket mOut = new DatagramPacket(mBytes,mBytes.length,group,6789);
            System.out.println("Bola extraida: " + new String(mOut.getData()));


            sock.send(mOut);
            Thread.sleep(1000);


            //Empieza a sacar las 90 bolas (89 porque ya se indroduce una para hacer la comprobacion del array de numeros)
            while(i<89 && Servidor.DetenerHilo==0){

                flag=0;
                while(flag==0){
                    bola = min_val + rand.nextInt((max_val - min_val) + 1);
                    if(!numeros.contains(bola)){
                        numeros.add(bola);

                        flag=1;
                    }

                }
                if(bola<10){ //Se le añade un cero a la izquierda si es un numero menor que 10
                    bolaS="0"+ String.valueOf(bola);
                }
                else{
                    bolaS = String.valueOf(bola);
                }
                mBytes = bolaS.getBytes();
                mOut.setData(mBytes);

                System.out.println("Bola extraida: " + new String(mOut.getData()));

                sock.send(mOut);
                i++;
                Thread.sleep(1000);

            }
            Servidor.DetenerHilo=1;



        }catch (IOException e){
            System.out.println("IO: " + e.getMessage());
        }catch (InterruptedException e) {
            System.out.println("InterruptedExceptioon: " + e.getMessage());
        }finally {
            if(!sock.isClosed()){ //Se comprueba si la conexion ha sido cerrada por el otro hilo

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


