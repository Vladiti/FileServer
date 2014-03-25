

import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Klient {

    public static void main(String[] args) throws Exception {
        try {
            Socket sock = new Socket("localhost", 4444);
            

            RecieveThread recieveThread = new RecieveThread(sock);
            Thread thread2 = new Thread(recieveThread);

            SendThread sendThread = new SendThread(sock,thread2);
            Thread thread = new Thread(sendThread);
            thread.start();


        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}

class SendThread implements Runnable {
    Socket sock = null;
    PrintWriter print = null;
    BufferedReader brinput = null;
    Thread t;
    public SendThread(Socket sock,Thread t) {
        this.sock = sock;
        this.t = t;
    }
    @Override
    public void run() {
        try {
            if (sock.isConnected()) {
                System.out.println("Client connected to " + sock.getInetAddress() + " on port " + sock.getPort());
                this.print = new PrintWriter(sock.getOutputStream(), true);
                while (true) {
                    System.out.println("Type your message to send to server..type 'exit' to exit");
                    brinput = new BufferedReader(new InputStreamReader(System.in));
                    String msgtoServerString = null;
                    msgtoServerString = brinput.readLine();
                    this.print.println(msgtoServerString);
                    this.print.flush();

                    if (msgtoServerString.equals("exit")) {
                        break;
                    } else {
                        BufferedInputStream get;
                        try {
                            get = new BufferedInputStream(sock.getInputStream());
                            int u;
                            String f = "in.txt";
                            File f1 = new File(f);
                            String str = "C:\\Disk2\\";
                            try (FileOutputStream fs = new FileOutputStream(new File(str, f1.toString()))) {
                                byte jj[] = new byte[1024];
                                while ((u = get.read(jj, 0, 1024)) != -1) {
                                    fs.write(jj, 0, u);
                                }
                                System.out.print("asdasd");
                            }
                        } catch (IOException ex) {
                            Logger.getLogger(RecieveThread.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                sock.close();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}

class RecieveThread implements Runnable {

    Socket sock = null;
    BufferedReader recieve = null;

    public RecieveThread(Socket sock) {
        this.sock = sock;
    }//end constructor

    @Override
    public void run() {
        try {
            recieve = new BufferedReader(new InputStreamReader(this.sock.getInputStream()));//get inputstream
            String msgRecieved = null;
            while ((msgRecieved = recieve.readLine()) != null) {
                System.out.println("From Server: " + msgRecieved);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}

