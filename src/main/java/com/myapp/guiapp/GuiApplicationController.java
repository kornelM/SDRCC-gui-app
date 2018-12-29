package com.myapp.guiapp;

import com.myapp.guiapp.udp.UdpServerCarControl;
import com.myapp.guiapp.udp.UdpServerVideoServer;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.DatagramSocket;

@Data
@Component
public class GuiApplicationController {

    private static final int BUFFER_SIZE = 65535;

    @FXML
    private Button button;
    @FXML
    private CheckBox grayscale;
    @FXML
    private CheckBox logoCheckBox;
    @FXML
    private ImageView histogram;
    @FXML
    private ImageView currentFrame;
    @FXML
    private ImageView linesFrame;

    private final UdpServerVideoServer udpServerVideoServer;
    private final UdpServerCarControl udpServerCarControl;
    private DatagramSocket socket;
    private byte[] buf = new byte[BUFFER_SIZE];

    @Autowired
    public GuiApplicationController(UdpServerVideoServer udpServerVideoServer,
                                    UdpServerCarControl udpServerCarControl) {
        this.udpServerVideoServer = udpServerVideoServer;
        this.udpServerCarControl = udpServerCarControl;
    }

    @FXML
    protected void launchWindow() {
        this.currentFrame.setFitWidth(300);
        this.currentFrame.setPreserveRatio(true);
        this.linesFrame.setFitWidth(300);
        this.linesFrame.setPreserveRatio(true);

        startImagesThread();
    }

    private void startImagesThread() {

        Thread carControlThread = new Thread((this::getAndUpdateImageCarControl), "Image-Window-Thread");
        Thread videoServerThread = new Thread((this::getAndUpdateImageVideoServer), "Image-Window-Thread");

        carControlThread.start();
        videoServerThread.start();
    }

    private void getAndUpdateImageCarControl() {
        while (true) {
            updateImageView(currentFrame, new Image(udpServerCarControl.receiveFrame()));
        }
    }

    private void getAndUpdateImageVideoServer() {
        while (true) {
            updateImageView(linesFrame, new Image(udpServerVideoServer.receiveFrame()));
        }
    }

    private void updateImageView(ImageView view, Image image) {
        onFXThread(view.imageProperty(), image);
    }

    private static <T> void onFXThread(final ObjectProperty<T> property, final T value) {
        Platform.runLater(() -> property.set(value));
    }
}