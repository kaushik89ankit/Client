package org.rudderstack;

import lombok.Data;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

@Data
public class Client {

    private String username;
    private Socket socket;
    private BufferedWriter writer;
    private BufferedReader reader;


    public Client(String username,Socket socket){
        this.socket = socket;
        this.username = username;
        try {
            this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
           close(reader,writer,socket);
        }

    }


    public void sendMessage(){
        try{
            writer.write(username);
            writer.newLine();
            writer.flush();
            Scanner scanner = new Scanner(System.in);
            while(socket.isConnected()){
                String message = scanner.nextLine();
                if(message.equalsIgnoreCase("exit")){
                    System.out.println("You're no longer connected");
                    close(reader,writer,socket);
                }
                writer.write(message);
                writer.newLine();
                writer.flush();
            }

        }catch (IOException e){
            System.out.println("Exception in sending message");
            e.printStackTrace();
            close(reader,writer,socket);
        }
    }

    public void recieveMessage(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (socket.isConnected()){
                    try{
                        String messageRecieved = reader.readLine();
                        if(messageRecieved == null){
                            throw new IOException("Chat server closed");
                        }
                        System.out.println(messageRecieved);
                    }catch (IOException e){
                        close(reader,writer,socket);
                        break;
                    }
                }
            }
        }).start();
    }

    private void close(BufferedReader reader, BufferedWriter writer, Socket socket) {
        try{
            if(reader != null){
                reader.close();
            }

            if(writer != null){
                writer.close();
            }

            if(socket != null){
                socket.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }

        System.out.println("Your chat window is closed");
    }
}
