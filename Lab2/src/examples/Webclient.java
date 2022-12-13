package examples;

import java.net.URL;
import java.net.HttpURLConnection;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Webclient {
    
    public static void main(String[] args){
        try{
            URL u;
            HttpURLConnection h;
            InputStream is;
            //u = new URL("https://www.kth.se");
            u = new URL("http://localhost:8080");
            h = (HttpURLConnection)u.openConnection();
            h.connect();
            //h.getHeaderField(name)
            is = h.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String str;
            while( (str = br.readLine()) != null){
                System.out.println(str);
            }
        }
        catch(java.net.MalformedURLException e){
            System.out.println(e.getMessage());
        }
        catch(java.io.IOException e){
            System.out.println(e.getMessage());
        }
    }
}
