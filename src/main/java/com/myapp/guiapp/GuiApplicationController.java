package com.myapp.guiapp;

import com.myapp.guiapp.communication.udp.UdpServerCarControl;
import com.myapp.guiapp.communication.udp.UdpServerVideoServer;
import com.myapp.guiapp.services.VideoServerClient;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
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
    private Button startButton;
    @FXML
    private Label thresholdLabel;
    @FXML
    private ImageView histogram;
    @FXML
    private ImageView currentFrame;
    @FXML
    private ImageView linesFrame;
    @FXML
    private Slider thresholdSlider;

    private final UdpServerVideoServer udpServerVideoServer;
    private final UdpServerCarControl udpServerCarControl;
    private final VideoServerClient videoServerClient;

    private DatagramSocket socket;
    private byte[] buf = new byte[BUFFER_SIZE];

    @Autowired
    public GuiApplicationController(UdpServerVideoServer udpServerVideoServer,
                                    UdpServerCarControl udpServerCarControl,
                                    VideoServerClient videoServerClient) {
        this.udpServerVideoServer = udpServerVideoServer;
        this.udpServerCarControl = udpServerCarControl;
        this.videoServerClient = videoServerClient;
    }

    @FXML
    protected void launchWindow() {
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

    @FXML
    public void updateThresholdValue() {
        thresholdSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            thresholdLabel.setText(String.valueOf(newValue.intValue()));
            videoServerClient.setThresholdValue(String.valueOf(newValue.intValue()));
        });
    }
}