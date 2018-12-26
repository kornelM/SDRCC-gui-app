package com.myapp.guiapp;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
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

    private final UdpServer udpServer;
    private DatagramSocket socket;
    private byte[] buf = new byte[BUFFER_SIZE];

    @Autowired
    public GuiApplicationController(UdpServer udpServer) {
        this.udpServer = udpServer;
    }

    @FXML
    protected void launchWindow() {
        this.currentFrame.setFitWidth(600);
        this.currentFrame.setPreserveRatio(true);
        Thread thread = new Thread((this::receiveImage), "ImageWindowThread");
        thread.start();
    }

    private void receiveImage() {
        BufferedImage bufferedImage = null;
        while (true) {
            try {
                bufferedImage = ImageIO.read(udpServer.receiveFrame());
                if(bufferedImage != null){
                    Image image = SwingFXUtils.toFXImage(bufferedImage, null);
                    updateImageView(currentFrame, image);
                } else {
                    //TODO add logger
                    System.exit(-1);
                }
            } catch (IOException e) {
                //TODO add logger
                e.printStackTrace();
            }
        }
    }

    private void updateImageView(ImageView view, Image image) {
        onFXThread(view.imageProperty(), image);
    }

    private static <T> void onFXThread(final ObjectProperty<T> property, final T value) {
        Platform.runLater(() -> property.set(value));
    }
}

