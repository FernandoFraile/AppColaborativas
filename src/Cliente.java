import java.io.*;
import java.net.*;
import java.util.Random;
import java.util.Date;
import java.util.ArrayList;

public class Cliente{
    public static void main(String[] args){
        MulticastSocket sock = null;
        byte[] buffer = new byte[10];
        ArrayList<Integer> secuencia=new ArrayList<Integer>(); //Variable para almacenar la secuencia de cada cliente
        Integer aux ;
        int min_val=1;
        int max_val=90; //Valores mínimmo y máximo que puede haber en el bingo
        Date semilla= new Date();
        Random rand = new Random(semilla.getTime());
        int flag=0;
        try{

            //Se genera la secuencia que va a tener el cliente para el bingo
            System.out.println("Secuencia: ");
            //Se añaden los numeros a la secuencia,


            for(int i=0;i<15;i++){
                aux= min_val + rand.nextInt((max_val - min_val) + 1);
                while(secuencia.contains(aux)){ //De este modo se evita que se repitan los numeros en la secuencia
                    aux= min_val + rand.nextInt((max_val - min_val) + 1);
                }
                secuencia.add(aux);

            }
            for(Integer elem : secuencia){
                System.out.println(elem.toString());
            }

            InetAddress group = InetAddress.getByName("225.0.0.100");
            sock = new MulticastSocket(6789);
            sock.joinGroup(group);
            DatagramPacket mIn = new DatagramPacket(buffer,buffer.length);
            byte [] mCantar = new String("bingo").getBytes();
            DatagramPacket mOut = new DatagramPacket(mCantar,mCantar.length,group,6789);

            while(!secuencia.isEmpty() && flag==0){
                sock.receive(mIn);
                //Se ve si el mensaje recibido es la palabra bingo o un numero. Si no es un numero, sale del bucle
                if(mIn.getLength()>2){
                    flag=1;
                }
                else{
                    System.out.println("Mensaje recibido: " + (int) mIn.getData()[0]);
                    if(secuencia.contains((int) mIn.getData()[0])){
                        secuencia.remove((Integer) (int) mIn.getData()[0]);
                        System.out.println("Numero de la secuencia eliminado: "+ (int) mIn.getData()[0]);
                    }
                }


            }

            //Cuando acabe el bucle es que ha acabado el carton, por lo que puede cantar.
            //Si el flag es 0, el cliente es el ganador, si no es otro, por lo que no envia el mensaje de ganador
            if(flag==0){
                System.out.println("El cliente va a cantar bingo");
                sock.send(mOut);
            }





        }catch (SocketException e){
            System.out.println("Socket: " + e.getMessage());
        }catch (IOException e){
            System.out.println("IO: " + e.getMessage());
        }finally {
            if(sock!= null) sock.close();
        }
    }


}