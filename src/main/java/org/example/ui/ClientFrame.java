package org.example.ui;

import javax.swing.*;
import java.awt.*;

public class ClientFrame extends JFrame {
    private final JLabel serverCameraScreen;
    private final JLabel myCameraScreen;

    public ClientFrame() {
        super("Client");
        setLayout(null);

        serverCameraScreen = new JLabel();
        serverCameraScreen.setBounds(0, 0, 640, 480);
        add(serverCameraScreen);

        myCameraScreen = new JLabel();
        myCameraScreen.setBounds(480, 360, 160, 120);
        add(myCameraScreen);

        setSize(new Dimension(660, 520));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void updateServerCamera(byte[] imageData) {
        var icon = new ImageIcon(imageData);
        serverCameraScreen.setIcon(icon);
    }

    public void updateMyCameraView(byte[] imageData) {
        var icon = new ImageIcon(imageData);
        myCameraScreen.setIcon(icon);
    }
}
