import java.net.*;
import java.io.*;

public class UDPServer {
    private DatagramSocket socket;
    
    public UDPServer(int port) throws SocketException {
        socket = new DatagramSocket(port);
        System.out.println("Servidor iniciado na porta " + port);
    }

    public DatagramPacket getRequest() throws IOException {
        byte[] buffer = new byte[1024];
        DatagramPacket requestPacket = new DatagramPacket(buffer, buffer.length);
        socket.receive(requestPacket); 
        return requestPacket;
    }

    public void sendReply(byte[] reply, InetAddress clientHost, int clientPort) throws IOException {
        DatagramPacket replyPacket = new DatagramPacket(reply, reply.length, clientHost, clientPort);
        socket.send(replyPacket);
    }

    public static void main(String[] args) {
        try {
            UDPServer server = new UDPServer(9090);
            
            while (true) {
                DatagramPacket requestPacket = server.getRequest();
                Message reqMsg = Message.fromBytes(requestPacket.getData());
                
                System.out.println("Requisição recebida ID: " + reqMsg.requestId + " | Operação: " + reqMsg.operationId);
                
                String requestData = new String(reqMsg.arguments);
                String responseData = "Operação concluída com sucesso: " + requestData.toUpperCase();
                
                Message repMsg = new Message(1, reqMsg.requestId, reqMsg.remoteReference, reqMsg.operationId, responseData.getBytes());
                
                server.sendReply(repMsg.toBytes(), requestPacket.getAddress(), requestPacket.getPort());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}