

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {

    public static void main(String[] args) throws Exception {
        System.out.println("Welcome to Server!");
        int port = 4444;
        ServerSocket servers = new ServerSocket(port);

        MultiClientThread mclient = new MultiClientThread(port, servers);
        Thread thread = new Thread(mclient);
        thread.start();
    }
}

class MultiClientThread implements Runnable {

    ServerSocket servers;
    Socket clientSocket;
    int port;

    public MultiClientThread(int port, ServerSocket servers) {
        this.port = port;
        this.servers = servers;
    }

    @Override
    public void run() {
        while (true) {
            try {
                System.out.println("Wait for client...");
                clientSocket = servers.accept();

                RecieveFromClientThread recieve = new RecieveFromClientThread(clientSocket);
                Thread thread = new Thread(recieve);
                thread.start();
            } catch (IOException ex) {
                Logger.getLogger(MultiClientThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}

class RecieveFromClientThread implements Runnable {

    Socket clientSocket = null;
    BufferedReader brBufferedReader = null;

    public RecieveFromClientThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            brBufferedReader = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));

            String input;
            System.out.println("Wait for message...");
            
            SendToClientMesg send = new SendToClientMesg(clientSocket);
          //  send.sendFileList(); // Выдет список файлов на сервере
            while (true) {
                while ((input = brBufferedReader.readLine()) != null) {
                       if (input.toLowerCase().equals("exit")) {
                       // break;
                    }else
                    if (input.toLowerCase().equals("privet")) {
                        //SendToClientMesg send = new SendToClientMesg(clientSocket);
                        send.send("lalka huli ti ot menya hochesh");
                    }else{
                        OutputStream os = clientSocket.getOutputStream();
                        send.sendFIle(os,input, clientSocket);
                    }
                      System.out.println(input);
                }
                this.clientSocket.close();
                System.exit(0);
            }
        } catch (IOException ex) {
            Logger.getLogger(RecieveFromClientThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(RecieveFromClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void delay() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

class SendToClientMesg {

    PrintWriter pwPrintWriter;
    Socket clientSock = null;

    public SendToClientMesg(Socket clientSock) {
        this.clientSock = clientSock;
    }

    public void send(String msgToClientString) {
        try {
            pwPrintWriter = new PrintWriter(new OutputStreamWriter(this.clientSock.getOutputStream()));//get outputstream			
            pwPrintWriter.println(msgToClientString);//send message to client with PrintWriter
            pwPrintWriter.flush();//flush the PrintWriter
            System.out.println("Please enter something to send back to client..");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void sendFileList() {
        try {
            String msgToClientString = "";
            
            File[] fList;
            File F = new File("C:\\Disk");

            fList = F.listFiles();

            for (int i = 0; i < fList.length; i++) {
                if (fList[i].isFile()) {
                    msgToClientString += " "+fList[i].getName();
                }
            }
            pwPrintWriter = new PrintWriter(new OutputStreamWriter(this.clientSock.getOutputStream()));//get outputstream			
            pwPrintWriter.println(msgToClientString);//send message to client with PrintWriter
            pwPrintWriter.flush();//flush the PrintWriter
            System.out.println("Please enter something to send back to client..");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
    public void sendFIle(OutputStream os,String fileName,Socket cs) throws Exception {
                BufferedOutputStream put=new BufferedOutputStream(cs.getOutputStream());
                File f=new File("C:\\Disk\\" + fileName);
                 if(f.isFile())
                 { 
                     FileInputStream fis=new FileInputStream(f);


                     byte []buf=new byte[1024];
                     int read;
                     while((read=fis.read(buf,0,1024))!=-1)
                     {
                         put.write(buf,0,read);
                         put.flush();
                     }
                     fis.close();
                     System.out.println("File transfered");
                 }               
    }
}
