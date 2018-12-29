package com.myapp.guiapp.udp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

@Component
public class UdpServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(UdpServer.class);

    ByteArrayInputStream catchPacket(byte[] buf,
                                     DatagramSocket socket,
                                     InetAddress inetAddress,
                                     Integer serverPort) {
        DatagramPacket packet = new DatagramPacket(buf, buf.length, inetAddress, serverPort);
        try {
            socket.receive(packet);
            InetAddress address = packet.getAddress();
            int port = packet.getPort();
            packet = new DatagramPacket(buf, buf.length, address, port);
        } catch (IOException e) {
            LOGGER.error("Error during receiving udp packet!", e);
        }

        return new ByteArrayInputStream(packet.getData());
    }
}