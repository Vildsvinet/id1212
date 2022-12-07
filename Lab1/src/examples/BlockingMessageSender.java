package examples;
import java.net.Socket;
import java.io.OutputStreamWriter;

public class BlockingMessageSender {
    
    public static void main(String[] args){
        Socket s;
        OutputStreamWriter osw;

        try{
            s = new Socket("localhost",8080);
            osw = new OutputStreamWriter(s.getOutputStream());
            
            osw.write("Yes hello...");
                //Thread.sleep(1000);
            
            osw.close();
        }
        catch(java.net.UnknownHostException e){
            System.out.print(e.getMessage());
        }
        catch(java.io.IOException e){
            System.out.print(e.getMessage());
        }
        
    }   
}
