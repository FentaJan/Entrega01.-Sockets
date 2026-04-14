package paquete;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {

    public static void main(String[] args) {

        if (args.length < 3) {
            System.out.println("Uso: java Cliente <host> <puerto> <palabraClave>");
            return;
        }

        String host = args[0];
        int puerto = Integer.parseInt(args[1]);
        String palabraClave = args[2];

        Socket socket = null;
        DataInputStream entrada = null;
        DataOutputStream salida = null;
        Scanner scanner = null;

        try {
            // Conectar al servido
            System.out.print("Conectando al servidor " + host + ":" + puerto + "... ");
            socket = new Socket(host, puerto);
            System.out.println("OK");

            // Crear flujos de entrada y salida
            entrada = new DataInputStream(socket.getInputStream());
            salida = new DataOutputStream(socket.getOutputStream());
            scanner = new Scanner(System.in);

            // Recibir palabra clave del servidor y enviar la propia
            String palabraClaveServidor = entrada.readUTF();
            salida.writeUTF(palabraClave);
            System.out.println("Palabra clave del cliente: " + palabraClave);
            System.out.println("Palabra clave del servidor: " + palabraClaveServidor);
            System.out.println("--- Conversación iniciada ---");
            System.out.println("(El cliente envía el primer mensaje)\n");

            // Bucle de conversació
            boolean continuar = true;
            while (continuar) {

                // Escribir y enviar mensaje (el cliente sempre envía primer)
                System.out.print("Cliente> ");
                String mensajeEnviar = scanner.nextLine();

                System.out.print("Enviando mensaje... ");
                salida.writeUTF(mensajeEnviar);
                System.out.println("OK");

                // Comprobar si el cliente usó alguna palabra clave
                if (mensajeEnviar.equalsIgnoreCase(palabraClave)
                        || mensajeEnviar.equalsIgnoreCase(palabraClaveServidor)) {
                    System.out.println("Has finalizado la conversación.");
                    continuar = false;
                    break;
                }

                // Recibir mensaje del servidor
                System.out.print("Recibiendo mensaje del servidor... ");
                String mensajeRecibido = entrada.readUTF();
                System.out.println("OK");
                System.out.println("Servidor: " + mensajeRecibido);

                // Comprobar si el servidor usó alguna palabra clave
                if (mensajeRecibido.equalsIgnoreCase(palabraClave)
                        || mensajeRecibido.equalsIgnoreCase(palabraClaveServidor)) {
                    System.out.println("El servidor ha finalizado la conversación.");
                    continuar = false;
                }
            }

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            // Cerrar todos los recursos
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
            } catch (IOException e) {
                System.out.println("Error al cerrar recursos: " + e.getMessage());
            }
        }
    }
}