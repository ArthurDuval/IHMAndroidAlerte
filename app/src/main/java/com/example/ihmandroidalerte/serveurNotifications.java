package com.example.ihmandroidalerte;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Arrays;

public class serveurNotifications {
    final private int size = 1024;
    final private byte[] buffer = new byte[size];
    private DatagramSocket socket;
    private DatagramPacket packet;
    serveurNotifications() {
        /*
        telnet localhost 5554
        auth ~/.emulator_console_auth_token -> auth aNMzuIDk5qUkCRQ2
        redir add udp:12345:12346
         */
        try {
            this.socket = new DatagramSocket(12346);
            this.packet = new DatagramPacket(this.buffer, this.size);
        } catch (SocketException e) {
            e.printStackTrace();
        }
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
            this.socket.setSoTimeout(10000);
            this.socket.receive(packet);
        } catch (SocketTimeoutException ignored) { }
        catch (IOException e) {
            e.printStackTrace();
        } finally {
            this.socket.close();
        }
        return new String(serveurNotifications.retrecirBuffer(this.buffer, this.size));
    }
}