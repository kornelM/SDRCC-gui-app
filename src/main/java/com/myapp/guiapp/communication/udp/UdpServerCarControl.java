package com.myapp.guiapp.communication.udp;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

@Getter
@Component
public class UdpServerCarControl {

    private static final int BUFFER_SIZE = 65535;

    @Value("${gui.video.carControlApp.port}")
    private Integer serverPort;

    @Value("${gui.video.carControlApp.address}")
    private String inetAddress;

    private DatagramSocket socket;
    private final byte[] buf = new byte[BUFFER_SIZE];
    private final UdpServer udpServer;
    private final InetAddress addressGui;

    @Autowired
    public UdpServerCarControl(UdpServer udpServer) throws UnknownHostException {
        this.udpServer = udpServer;
        this.addressGui = InetAddress.getByName(inetAddress);
    }

    @PostConstruct
    public void setup() throws SocketException {
        this.socket = new DatagramSocket(serverPort);
    }

    public ByteArrayInputStream receiveFrame() {
        return udpServer.catchPacket(buf, socket, addressGui, serverPort);
    }
}