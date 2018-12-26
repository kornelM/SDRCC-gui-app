package com.myapp.guiapp;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

@Getter
@Component
public class UdpServer {

    private DatagramSocket socket;
    private byte[] buf = new byte[65535];

    @Value("${gui.video.server.port}")
    private Integer serverPort;

    @Value("${gui.video.client.inetAddress}")
    private String inetAddress;

    @PostConstruct
    public void setup() throws SocketException {
        socket = new DatagramSocket(serverPort);
    }

    ByteArrayInputStream receiveFrame() {
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        try {
            socket.receive(packet);
            InetAddress address = packet.getAddress();
            int port = packet.getPort();
            packet = new DatagramPacket(buf, buf.length, address, port);
        } catch (IOException e) {
            //TODO add logger
            e.printStackTrace();
        }

        return new ByteArrayInputStream(packet.getData());
    }
}