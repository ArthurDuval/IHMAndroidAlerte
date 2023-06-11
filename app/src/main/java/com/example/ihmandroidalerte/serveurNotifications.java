package com.example.ihmandroidalerte;
import android.util.Log;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Arrays;

public class serveurNotifications {
    final private int size = 1024;
    final private byte[] buffer = new byte[size];
    final private DatagramSocket socket;
    final private DatagramPacket packet;
    serveurNotifications() {
        // création de la socket
        InetAddress address;
        try {
            address = InetAddress.getByName("10.0.2.15");
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        try {
            // TODO : essayer de créer un script au startup
            /*
            telnet localhost 5554
            auth ~/.emulator_console_auth_token
            redir add udp:12345:12346
             */
            this.socket = new DatagramSocket(12346, address);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        // création du packet
        this.packet = new DatagramPacket(this.buffer, this.size);
    }
    static public byte[] retrecirBuffer(byte[] buffer, int size) {
        int counter = 0;
        for (int i = 0; i < size; i++) {
            if (buffer[i] != 0) {
                counter++;
            }
            else {
                break;
            }
        }
        return Arrays.copyOf(buffer, counter);
    }
    public String receptionPacket() {
        try {
            this.socket.setSoTimeout(10000); // 10 secondes d'attente
            this.socket.receive(packet);
        } catch (SocketTimeoutException e) {
            Log.d("TIMEOUT", "Timeout");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            this.socket.close();
        }
        return new String(serveurNotifications.retrecirBuffer(this.buffer, this.size));
    }
}