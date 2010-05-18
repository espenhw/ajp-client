package org.grumblesmurf.ajp;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.net.URL;

import java.util.List;
import java.util.Map;

public class AjpCommandLineClient
{
    public static void main(String[] args) throws Exception {
        String host = args[0];
        int port = Integer.parseInt(args[1]);

        AjpClient ac = AjpClient.newInstance(host, port);
        if (args.length == 2) {
            System.out.printf("CPing %s:%s: %s%n", host, port, ac.cping() ? "OK" : "NOK");
        } else {
            URL url = new URL(args[2]);
            AjpResponse resp;
            if (args.length == 3)
                resp = ac.get(url);
            else
                resp = ac.post(url, read(args[3]));
            
            System.out.printf("%s %s%n", resp.getResponseCode(), resp.getResponseMessage());
            for (Map.Entry<String, List<String>> e : resp.getHeaderFields().entrySet()) {
                System.out.printf("%s: %s%n", e.getKey(), e.getValue());
            }
            System.out.println();
            System.out.print(new String(resp.getContent(), "ISO-8859-1"));
        }
        ac.close();
    }

    private static byte[] read(String filename) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        InputStream in = new FileInputStream(filename);
        byte[] buffer = new byte[2048];
        int read;
        while ((read = in.read(buffer)) != -1) {
            bos.write(buffer, 0, read);
        }
        in.close();
        
        return bos.toByteArray();
    }
}
