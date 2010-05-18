package org.grumblesmurf.ajp;

public class AjpCommandLineClient
{
    public static void main(String[] args) throws Exception {
        String host = args[0];
        int port = Integer.parseInt(args[1]);

        AjpClient ac = AjpClient.newInstance(host, port);
        System.out.printf("CPing %s:%s: %s%n", host, port, ac.cping() ? "OK" : "NOK");
        ac.close();
    }
}
