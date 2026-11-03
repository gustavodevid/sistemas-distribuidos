import java.io.*;

public class Message implements Serializable {
    public int messageType;
    public int requestId;
    public String remoteReference;
    public int operationId;
    public byte[] arguments;

    public Message(int messageType, int requestId, String remoteReference, int operationId, byte[] arguments) {
        this.messageType = messageType;
        this.requestId = requestId;
        this.remoteReference = remoteReference;
        this.operationId = operationId;
        this.arguments = arguments;
    }

    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(this);
        return baos.toByteArray();
    }

    public static Message fromBytes(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bais);
        return (Message) ois.readObject();
    }
}