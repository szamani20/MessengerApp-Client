import javax.swing.*;
import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Main extends JFrame {
    static Socket client;
    static DataInputStream dis;
    static DataOutputStream dos;
    static JTextArea textArea;

    public Main() throws IOException {
        super("Client");

        try {
            client = new Socket("localhost", 1234);
        } catch (Exception e) {
            System.out.println("Unable to connect ot the server");
        }
        dos = new DataOutputStream(client.getOutputStream());
        dis = new DataInputStream(client.getInputStream());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(new Dimension(300, 300));
        JTextField textField = new JTextField(10);
        textArea = new JTextArea();
        textArea.setEditable(false);

        add(textField, BorderLayout.NORTH);
        add(new JScrollPane(textArea), BorderLayout.CENTER);

        setVisible(true);

        textField.addActionListener(e -> {
            try {
                dos.writeUTF(textField.getText());
            } catch (IOException e1) {
                System.out.println("Connection end, unable to send message");
                textArea.append("Connection end, unable to send message\n");
            } finally {
                textField.setText("");
            }
        });

    }

    public static void main(String[] args) throws IOException {
        try {
            new Main();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String messageIn;
        try {
            do {
                messageIn = dis.readUTF();
                if (!messageIn.equals(""))
                    textArea.append("Server: " + messageIn + "\n");
            } while (!messageIn.equals("End"));

            client.close();
        } catch (Exception e) {
        }
    }
}
