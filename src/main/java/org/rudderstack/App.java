package org.rudderstack;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class App
{
    private static int port = 9999;

    public static void main( String[] args ) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your username : ");
        String username = scanner.nextLine();
        Socket socket = null;
        long wait = 1000;
        while(socket == null){
            try{
                socket = new Socket("localhost",port);
                Client client = new Client(username,socket);
                client.recieveMessage();
                client.sendMessage();
            }catch (Exception e){
                System.out.println("Can not connect with the chat server");
                try {
                    Thread.sleep(wait);
                    wait = 2*wait;
                } catch (InterruptedException ex) {
                    continue;
                }
            }
        }
    }
}
