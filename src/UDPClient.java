import java.net.*;
import java.io.*;

public class UDPClient {
    private DatagramSocket socket;
    
    public UDPClient() throws SocketException {
        socket = new DatagramSocket();
        socket.setSoTimeout(5000); 
    }

    public byte[] doOperation(String host, int port, int operationId, byte[] arguments) throws Exception {
        InetAddress address = InetAddress.getByName(host);
        
        Message reqMsg = new Message(0, 101, "ServerRef", operationId, arguments);
        byte[] reqBytes = reqMsg.toBytes();
        
        DatagramPacket requestPacket = new DatagramPacket(reqBytes, reqBytes.length, address, port);
        socket.send(requestPacket); 
        
        byte[] buffer = new byte[1024];
        DatagramPacket replyPacket = new DatagramPacket(buffer, buffer.length);
        socket.receive(replyPacket);
        
        Message repMsg = Message.fromBytes(replyPacket.getData());
        return repMsg.arguments;
    }

    public static void main(String[] args) {
        try {
            UDPClient client = new UDPClient();
            String host = "localhost"; 
            int port = 9090;
            
            String payload = "Ola mundo sistemas distribuidos";
            System.out.println("Enviando requisição: " + payload);
            
            byte[] response = client.doOperation(host, port, 1, payload.getBytes());
            
            System.out.println("Resposta do Servidor: " + new String(response));
        } catch (SocketTimeoutException e) {
            System.out.println("Timeout: O servidor não respondeu a tempo.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}