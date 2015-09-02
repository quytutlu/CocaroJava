package client_socket;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author QuyTuTlu
 */
public class FormCoCaRo {
    
    final JButton[][] jb;
    final int[][] a;
    boolean flag;
    boolean KHOA = false;
    
    public FormCoCaRo(Socket socket) throws IOException {
        class LangNgheDuLieu extends Thread {
            
            Socket socket;
            
            public LangNgheDuLieu(Socket socket) {
                this.socket = socket;
            }
            
            @Override
            public void run() {
                while (true) {
                    BufferedReader br;
                    try {
                        br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        //System.out.println("Server: " + br.readLine());
                        String index = br.readLine();
                        String[] indexs = index.split("_");
                        int Dong;
                        int Cot = Integer.parseInt(indexs[1]);
                        if (indexs[0].equals("qt")) {
                            //System.out.println("Chinh no");
                            //DongBang();
                            Dong = Integer.parseInt(indexs[1]);
                            Cot = Integer.parseInt(indexs[2]);
                            if (a[Dong][Cot] != 0) {
                                continue;
                            }
                            KHOA = true;
                            flag = true;
                            a[Dong][Cot] = 2;
                            try {
                                Image img = ImageIO.read(getClass().getResource("x.jpg"));
                                jb[Dong][Cot].setIcon(new ImageIcon(img));
                            } catch (IOException ex) {
                            }
                        } else {
                            //GiaiBang();
                            Dong = Integer.parseInt(indexs[0]);
                            Cot = Integer.parseInt(indexs[1]);
                            if (a[Dong][Cot] != 0) {
                                continue;
                            }
                            KHOA = false;
                            flag = false;
                            a[Dong][Cot] = 1;
                            try {
                                Image img = ImageIO.read(getClass().getResource("o.jpg"));
                                jb[Dong][Cot].setIcon(new ImageIcon(img));
                            } catch (IOException ex) {
                            }
                        }
                        XetThang(socket);
                    } catch (IOException ex) {
                        Logger.getLogger(FormCoCaRo.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        new LangNgheDuLieu(socket).start();
        JFrame f = new JFrame();
        jb = new JButton[40][42];
        a = new int[40][42];
        f.setLayout(new java.awt.GridLayout(36, 40));
        
        for (int i = 0; i < 36; i++) {
            for (int j = 0; j < 40; j++) {
                String temp = "";
                if (i < 10) {
                    temp += "0";
                }
                temp += i;
                temp += "_";
                if (j < 10) {
                    temp += "0";
                }
                temp += j;
                final String t2 = temp;
                final int d = i;
                final int c = j;
                JButton bTemp = new JButton();
                bTemp.setName(temp);
                
                bTemp.setPreferredSize(new Dimension(30, 30));
                bTemp.setLocation(i * 30 + 10, j * 30 + 10);
                bTemp.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (!KHOA) {
                            try {
                                PrintStream ps = new PrintStream(socket.getOutputStream());
                                ps.println(t2);
                                
                            } catch (IOException ex) {
                                Logger.getLogger(FormCoCaRo.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                });
                jb[i][j] = bTemp;
                f.add(jb[i][j]);
            }
        }
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setPreferredSize(new Dimension(400, 300));
        f.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                PrintStream ps;
                try {
                    ps = new PrintStream(socket.getOutputStream());
                    ps.println("exit");
                } catch (IOException ex) {
                    Logger.getLogger(FormCoCaRo.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        });
        //f.pack();
        f.setVisible(true);
    }
    
    public void DongBang() {
        for (int i = 0; i < 36; i++) {
            for (int j = 0; j < 40; j++) {
                jb[i][j].setEnabled(false);
            }
        }
    }
    
    public void GiaiBang() {
        for (int i = 0; i < 36; i++) {
            for (int j = 0; j < 40; j++) {
                jb[i][j].setEnabled(true);
            }
        }
    }
    
    public void XetThang(Socket socket) throws IOException {
        boolean temp = false;
        for (int i = 0; i < 36; i++) {
            for (int j = 0; j < 40; j++) {
                if (XetCacTruongHopThang(i, j)) {
                    temp = true;
                }
            }
        }
        if (temp) {
            if (flag) {
                JOptionPane.showMessageDialog(null, "Chúc mừng bạn đã chiến thắng!");
                PrintStream ps = new PrintStream(socket.getOutputStream());
                ps.println("exit");
            } else {
                JOptionPane.showMessageDialog(null, "Cố lên! Thua keo này ta bày keo khác");
                PrintStream ps = new PrintStream(socket.getOutputStream());
                ps.println("exit");
            }
        } else {
            if (XetHoa()) {
                //MessageBox.Show("Hòa");
                JOptionPane.showMessageDialog(null, "Hòa");
                PrintStream ps = new PrintStream(socket.getOutputStream());
                ps.println("exit");
            }
        }
    }
    
    public boolean XetHoa() {
        for (int i = 0; i < 36; i++) {
            for (int j = 0; j < 40; j++) {
                if (a[i][j] == 0) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public boolean XetCacTruongHopThang(int dong, int cot) {
        int temp = a[dong][cot];
        int SoCoGiongNhau = 0;
        if (temp == 0) {
            return false;
        } else {
            int tam;
            if (temp == 1) {
                tam = 2;
            } else {
                tam = 1;
            }

            // an ngang
            for (int i = 0; i < 5; i++) {
                if (a[dong][cot + i] == temp) {
                    SoCoGiongNhau++;
                }
            }
            if (SoCoGiongNhau == 5) {
                // xet truong hop chan hai dau
                if (cot != 0 && a[dong][cot - 1] == tam
                        && a[dong][cot + 5] == tam) {
                    return false;
                }
                return true;
            }

            // an doc
            SoCoGiongNhau = 0;
            for (int i = 0; i < 5; i++) {
                if (a[dong + i][cot] == temp) {
                    SoCoGiongNhau++;
                }
            }
            if (SoCoGiongNhau == 5) {
                // xet truong hop chan hai dau
                if (dong != 0 && a[dong - 1][cot] == tam && a[dong + 5][cot] == tam) {
                    return false;
                }
                return true;
            }

            // an cheo
            SoCoGiongNhau = 0;
            for (int i = 0; i < 5; i++) {
                if (a[dong + i][cot + i] == temp) {
                    SoCoGiongNhau++;
                }
            }
            if (SoCoGiongNhau == 5) {
                // xet truong hop chan hai dau
                if (dong != 0 && cot != 0 && a[dong - 1][cot - 1] == tam && a[dong + 5][cot + 5] == tam) {
                    return false;
                }
                return true;
            }

            // xet truong hop thang vi an cheo len
            if (dong >= 4) {
                SoCoGiongNhau = 0;
                for (int i = 0; i < 5; i++) {
                    if (a[dong - i][cot + i] == temp) {
                        SoCoGiongNhau++;
                    }
                }
                if (SoCoGiongNhau == 5) {
                    // xet truong hop chan hai dau
                    if (cot != 0 && dong - 5 >= 0 && a[dong + 1][cot - 1] == tam && a[dong - 5][cot + 5] == tam) {
                        return false;
                    }
                    return true;
                }
            }
            
        }
        return false;
    }
}
