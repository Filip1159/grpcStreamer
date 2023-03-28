package org.example.ui;

import javax.swing.*;
import java.awt.*;

public class ServerFrame extends JFrame {
    private final JLabel clientCameraScreen;
    private final JLabel myCameraScreen;

    public ServerFrame() {
        super("Server");
        setLayout(null);

        clientCameraScreen = new JLabel();
        clientCameraScreen.setBounds(0, 0, 640, 480);
        add(clientCameraScreen);

        myCameraScreen = new JLabel();
        myCameraScreen.setBounds(480, 360, 160, 120);
        add(myCameraScreen);

        setSize(new Dimension(660, 520));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void updateClientCamera(byte[] imageData) {
        var icon = new ImageIcon(imageData);
        clientCameraScreen.setIcon(icon);
    }

    public void updateMyCameraView(byte[] imageData) {
        var icon = new ImageIcon(imageData);
        myCameraScreen.setIcon(icon);
    }
}
