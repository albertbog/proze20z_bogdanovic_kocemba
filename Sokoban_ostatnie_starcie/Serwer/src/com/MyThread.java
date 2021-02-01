package com;

import java.io.*;
import java.net.Socket;

/**
 * Klasa odpowiedzialna za watek w ktorym serwer obsluguje zadania klientow
 */
public class MyThread extends Thread
{
    /**
     * Zmienna przechowująca socket serwera
     */
    private final Socket socket;
    /**
     * Konstruktor, przypisujący polu socket wartość
     * @param socket socket, który zostanie zapisany w zmiennej socket znajdującej się w tym obiekcie
     */
    public MyThread(Socket socket)
    {
        this.socket=socket;
    }

    /**
     * Odczytuje zadanie klienta i wywołuję metode ktora ma za zadnie na nie odpowiedziec
     */
    @Override
    public void run() {
        try {
            while (true) {
                DataInputStream dataInputStream=new DataInputStream(socket.getInputStream());
                String fromClient=dataInputStream.readUTF();
                String[] array=fromClient.split("!");
                System.out.println("From client: " + array[0]);
                if(array[0].equals("Load_textures") || array[0].equals("Load_avatars"))
                {
                    FileInputStream fileInputStream=new FileInputStream(ServerCommand.do_command_string(array));
                    byte[] buffer= new byte[fileInputStream.available()];
                    fileInputStream.read(buffer);

                    ObjectOutputStream objectOutputStream=new ObjectOutputStream(socket.getOutputStream());
                    objectOutputStream.writeObject(buffer);

                    objectOutputStream.close();
                    break;
                }
                else {
                    PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
                    String serverRespond = ServerCommand.do_command_string(array);
                    printWriter.println(serverRespond);
                    printWriter.flush();
                    System.out.println("Server respond: " + serverRespond);
                    printWriter.close();
                    break;
                }
            }
        } catch (IOException exception) {
            System.out.println("Sorry! We could not do command!");
            System.err.println(exception);
        }
    }
}
