package server_socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author QuyTuTlu
 */
public class Server_Socket {

    //Socket[] socket = new Socket[100];
    int SoLuong = 0;
    List<Socket> listSock=new ArrayList<>();
    class LangNgheDuLieu extends Thread {
        Socket sk;
        public LangNgheDuLieu(Socket socket) {
            this.sk = socket;
        }

        @Override
        public void run() {
            while (true) {
                BufferedReader br;
                try {
                    br = new BufferedReader(new InputStreamReader(sk.getInputStream()));
                    String request = br.readLine();
                    if(request.equals("exit")){
                        sk.close();
                        for(int i=0;i<SoLuong;i++){
                            if(listSock.get(i)==sk){
                                listSock.get(i).close();
                                listSock.remove(i);
                                SoLuong--;
                                return;
                            }
                        }
                    }
                    for(int i=0;i<SoLuong;i++){
                        PrintStream ps = new PrintStream(listSock.get(i).getOutputStream());
                        if(listSock.get(i)!=sk){
                            ps.println(request);
                        }else{
                            ps.println("qt_"+request);
                        }
                    }
                } catch (IOException ex) {
                    Logger.getLogger(Server_Socket.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }
    }
    public void Server() {
        try {
            ServerSocket server = new ServerSocket(1994);
            //System.out.println("Server is ready123...");
            while (true) {
                System.out.println("Dang cho ket noi....");
                //socket[SoLuong] = server.accept();
                Socket sk=server.accept();
                listSock.add(sk);
                //new LangNgheDuLieu(socket[SoLuong]).start();
                new LangNgheDuLieu(sk).start();
                SoLuong++;
                System.out.println(SoLuong+" ket noi thanh cong");
            }

        } catch (IOException ex) {
            Logger.getLogger(Server_Socket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        //System.out.println("Nguyen quy tu");
        Server_Socket ss = new Server_Socket();
        ss.Server();
    }
}
