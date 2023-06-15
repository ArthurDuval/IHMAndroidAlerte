package com.example.ihmandroidalerte;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketTimeoutException;
import java.util.Arrays;

/*
telnet localhost 5554
auth ~/.emulator_console_auth_token -> auth aNMzuIDk5qUkCRQ2
redir add udp:12345:12346
 */

public class serveurNotifications {
    private final int size;
    private final byte[] buffer;
    serveurNotifications(int size) {
        this.size = size;
        this.buffer = new byte[size];
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
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(12346);
            DatagramPacket packet = new DatagramPacket(this.buffer, this.size);
            socket.setSoTimeout(10000);
            socket.receive(packet);
        } catch (SocketTimeoutException ignored) { }
        catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null)
                socket.close();
        }
        return new String(serveurNotifications.retrecirBuffer(this.buffer, this.size));
    }
}