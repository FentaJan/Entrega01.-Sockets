package paquete;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Servidor {

    public static void main(String[] args) {

        if (args.length < 2) {
            System.out.println("Uso: java Servidor <puerto> <palabraClave>");
            return;
        }

        int puerto = Integer.parseInt(args[0]);
        String palabraClave = args[1];

        ServerSocket serverSocket = null;
        Socket socket = null;
        DataInputStream entrada = null;
        DataOutputStream salida = null;
        Scanner scanner = null;

        try {
            // Iniciar servidor
            System.out.print("Iniciando servidor en puerto " + puerto + "... ");
            serverSocket = new ServerSocket(puerto);
            System.out.println("OK");

            // Esperar conexió del cliente
            System.out.print("Esperando conexión de un cliente... ");
            socket = serverSocket.accept();
            System.out.println("OK");
            System.out.println("Cliente conectado desde: " + socket.getInetAddress());

            // Crear flujos de entrada y salida
            entrada = new DataInputStream(socket.getInputStream());
            salida = new DataOutputStream(socket.getOutputStream());
            scanner = new Scanner(System.in);

            // Enviar palabra clave al cliente y recibir la suya
            salida.writeUTF(palabraClave);
            String palabraClaveCliente = entrada.readUTF();
            System.out.println("Palabra clave del servidor: " + palabraClave);
            System.out.println("Palabra clave del cliente: " + palabraClaveCliente);
            System.out.println("--- Conversación iniciada ---\n");

            // Bucle de conversació
            boolean continuar = true;
            while (continuar) {

                // Rebre mensaje del cliente
                System.out.print("Recibiendo mensaje del cliente... ");
                String mensajeRecibido = entrada.readUTF();
                System.out.println("OK");
                System.out.println("Cliente: " + mensajeRecibido);

                // Comprobar si el cliente usó alguna palabra clave
                if (mensajeRecibido.equalsIgnoreCase(palabraClave)
                        || mensajeRecibido.equalsIgnoreCase(palabraClaveCliente)) {
                    System.out.println("El cliente ha finalizado la conversación.");
                    continuar = false;
                    break;
                }

                // Escribir y enviar mensaje
                System.out.print("Servidor> ");
                String mensajeEnviar = scanner.nextLine();

                System.out.print("Enviando mensaje... ");
                salida.writeUTF(mensajeEnviar);
                System.out.println("OK");

               
                if (mensajeEnviar.equalsIgnoreCase(palabraClave)
                        || mensajeEnviar.equalsIgnoreCase(palabraClaveCliente)) {
                    System.out.println("Has finalizado la conversación.");
                    continuar = false;
                }
            }

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
           
            try {
                if (scanner != null) {
                    System.out.print("Cerrando scanner... ");
                    scanner.close();
                    System.out.println("OK");
                }
                if (entrada != null) {
                    System.out.print("Cerrando flujo de entrada... ");
                    entrada.close();
                    System.out.println("OK");
                }
                if (salida != null) {
                    System.out.print("Cerrando flujo de salida... ");
                    salida.close();
                    System.out.println("OK");
                }
                if (socket != null) {
                    System.out.print("Cerrando socket... ");
                    socket.close();
                    System.out.println("OK");
                }
                if (serverSocket != null) {
                    System.out.print("Cerrando servidor... ");
                    serverSocket.close();
                    System.out.println("OK");
                }
            } catch (IOException e) {
                System.out.println("Error al cerrar recursos: " + e.getMessage());
            }
        }
    }
}