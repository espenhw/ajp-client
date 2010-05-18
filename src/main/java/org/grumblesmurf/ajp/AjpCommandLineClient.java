package org.grumblesmurf.ajp;

import java.net.URL;

public class AjpCommandLineClient
{
    public static void main(String[] args) throws Exception {
        String host = args[0];
        int port = Integer.parseInt(args[1]);

        AjpClient ac = AjpClient.newInstance(host, port);
        if (args.length == 2) {
            System.out.printf("CPing %s:%s: %s%n", host, port, ac.cping() ? "OK" : "NOK");
        } else {
            ac.get(new URL(args[2]));
        }
        ac.close();
    }
}
