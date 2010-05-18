package org.grumblesmurf.ajp;

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
            AjpResponse resp = ac.get(new URL(args[2]));
            System.out.printf("%s %s%n", resp.getResponseCode(), resp.getResponseMessage());
            for (Map.Entry<String, List<String>> e : resp.getHeaderFields().entrySet()) {
                System.out.printf("%s: %s%n", e.getKey(), e.getValue());
            }
            System.out.println();
            System.out.print(new String(resp.getContent(), "ISO-8859-1"));
        }
        ac.close();
    }
}
