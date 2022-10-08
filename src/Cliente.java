import java.io.*;
import java.net.*;
import java.util.Random;
import java.util.Date;
import java.util.ArrayList;

public class Cliente{
    public static void main(String[] args){
        MulticastSocket sock = null;
        byte[] buffer = new byte[10];
        ArrayList<String> secuencia=new ArrayList<>(); //Variable para almacenar la secuencia de cada cliente
        Integer aux ;String num; //Para crear la secuencia
        int min_val=1;
        int max_val=90; //Valores mínimmo y máximo que puede haber en el bingo
        Date semilla= new Date();
        Random rand = new Random(semilla.getTime());
        int flag=0;
        try{

            //Se genera la secuencia que va a tener el cliente para el bingo
            //Se añaden los numeros a la secuencia,


            for(int i=0;i<15;i++){
                aux= min_val + rand.nextInt((max_val - min_val) + 1);
                if(aux<10){
                    num="0"+ String.valueOf(aux);
                }
                else {
                    num= String.valueOf(aux);
                }
                while(secuencia.contains(num)){ //De este modo se evita que se repitan los numeros en la secuencia
                    aux= min_val + rand.nextInt((max_val - min_val) + 1);
                    if(aux<10){
                        num="0"+ String.valueOf(aux);
                    }
                    else {
                        num= String.valueOf(aux);
                    }
                }
                secuencia.add(num);

            }
            System.out.print("Carton: ");

            for(String elem : secuencia){
                System.out.print(elem+" ");
            }
            System.out.print("\n");

            InetAddress group = InetAddress.getByName("225.0.0.100");
            sock = new MulticastSocket(6789);
            sock.joinGroup(group);
            DatagramPacket mIn = new DatagramPacket(buffer,buffer.length);
            byte [] mCantar = ("bingo").getBytes();
            DatagramPacket mOut = new DatagramPacket(mCantar,mCantar.length,group,6789);

            while(!secuencia.isEmpty() && flag==0){
                sock.receive(mIn);
                //Se ve si el mensaje recibido es la palabra bingo o un numero. Si no es un numero, sale del bucle
                if(mIn.getLength()>2){
                    System.out.println("Otro participante ha ganado el bingo...");
                    flag=1;
                }
                else{
                    System.out.println("Ha salido el : " + new String(mIn.getData()).trim());
                    if(secuencia.contains(new String(mIn.getData()).trim())){
                        System.out.println("Coindicendia de un numero");
                        secuencia.remove(new String(mIn.getData()).trim());
                        System.out.print("Carton: ");
                        for(String elem : secuencia){
                            System.out.print(elem+" ");
                        }
                        System.out.print("\n");
                        secuencia.remove(new String(mIn.getData()).trim());

                    }

                }



            }

            //Cuando acabe el bucle es que ha acabado el carton, por lo que puede cantar.
            //Si el flag es 0, el cliente es el ganador, si no es otro, por lo que no envia el mensaje de ganador
            if(flag==0){
                System.out.println("Ganador del bingo!!!!!");
                sock.send(mOut);
            }





        }catch (SocketException e){
            System.out.println("Socket: " + e.getMessage());
        }catch (IOException e){
            System.out.println("IO: " + e.getMessage());
        }finally {
            if(sock!= null){
                System.out.println("El cliente cierra el socket");
                sock.close();
            }

        }
    }


}