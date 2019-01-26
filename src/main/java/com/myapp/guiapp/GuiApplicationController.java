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
import org.apache.commons.math3.util.Precision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.DatagramSocket;

@Data
@Component
public class GuiApplicationController {

    private static final int BUFFER_SIZE = 65535;

    @FXML
    public Slider houghLinesPThresholdSlider;
    @FXML
    public Label houghLinesPThresholdLabel;
    @FXML
    public Slider minLineLengthSlider;
    @FXML
    public Label minLineLengthLabel;
    @FXML
    public Slider maxLineGapSlider;
    @FXML
    public Label maxLineGapLabel;
    @FXML
    public Slider rhoSlider;
    @FXML
    public Label rhoLabel;
    @FXML
    private Button startButton;
    @FXML
    private Label thresholdBottomLabel;
    @FXML
    private Label thresholdTopLabel;
    @FXML
    private ImageView histogram;
    @FXML
    private ImageView currentFrame;
    @FXML
    private ImageView linesFrame;
    @FXML
    private Slider thresholdBottomSlider;
    @FXML
    private Slider thresholdTopSlider;

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
    public void updateThresholdBottomValue() {
        thresholdBottomSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            thresholdBottomLabel.setText(String.valueOf(newValue.intValue()));
            videoServerClient.setThresholdBottomValue(String.valueOf(newValue.intValue()));
        });
    }

    @FXML
    public void updateThresholdTopValue() {
        thresholdTopSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            thresholdTopLabel.setText(String.valueOf(newValue.intValue()));
            videoServerClient.setThresholdTopValue(String.valueOf(newValue.intValue()));
        });
    }

    @FXML
    public void updateHoughLinesPThresholdValue() {
        houghLinesPThresholdSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            houghLinesPThresholdLabel.setText(String.valueOf(newValue.intValue()));
            videoServerClient.setHoughLinesPThresholdValue(String.valueOf(newValue.intValue()));
        });
    }

    @FXML
    public void updateMinLineLengthValue() {
        minLineLengthSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            minLineLengthLabel.setText(String.valueOf(newValue.intValue()));
            videoServerClient.setMinLineLengthValue(String.valueOf(newValue.intValue()));
        });
    }

    @FXML
    public void updateMaxLineGapValue() {
        maxLineGapSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            maxLineGapLabel.setText(String.valueOf(newValue.intValue()));
            videoServerClient.setMaxLineGapValue(String.valueOf(newValue.intValue()));
        });
    }

    @FXML
    public void updateRhoValue() {
        rhoSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            rhoLabel.setText(String.valueOf(Precision.round(newValue.doubleValue(), 1)));
            videoServerClient.setRhoValue(String.valueOf(Precision.round(newValue.doubleValue(), 1)));
        });
    }
}