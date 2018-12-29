package com.myapp.guiapp.udp;

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
public class UdpServerVideoServer {

    private static final int BUFFER_SIZE = 65535;

    @Value("${gui.video.videoServerApp.port}")
    private Integer serverPort;

    @Value("${gui.video.videoServerApp.inetAddress}")
    private String inetAddress;

    private byte[] buf = new byte[BUFFER_SIZE];
    private DatagramSocket socket;
    private final UdpServer udpServer;
    private final InetAddress addressGui;

    @Autowired
    public UdpServerVideoServer(UdpServer udpServer) throws UnknownHostException {
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