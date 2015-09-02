package client_socket;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
public class Client_Socket {

    public void connect() {
        try {
            //Socket socket = new Socket("localhost", 1994);
            Socket socket = new Socket("192.168.1.102", 1994);
            new FormCoCaRo(socket);
        } catch (IOException ex) {
            Logger.getLogger(Client_Socket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static void main(String[] args) {
        Client_Socket cs = new Client_Socket();
        cs.connect();
        //cs.HienForm();
    }

}
