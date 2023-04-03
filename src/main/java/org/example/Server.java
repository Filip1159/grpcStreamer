package org.example;

import io.grpc.ServerBuilder;
import org.example.ui.ServerFrame;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import java.io.IOException;

import static org.opencv.imgproc.Imgproc.INTER_CUBIC;

public class Server {
    private final ServerFrame serverFrame;
    private byte[] myCameraData;

    public Server() throws IOException {
        serverFrame = new ServerFrame();
        var server = ServerBuilder.forPort(9000)
                .addService(new StreamingServiceImpl(this))
                .build();

        server.start();

        VideoCapture capture = new VideoCapture(0);
        Mat image = new Mat();

        while (true) {
            System.out.println("while");
            capture.read(image);
            var smallerImageCopy = new MatOfByte();
            Imgproc.resize(image, smallerImageCopy, new Size(160, 120), 0, 0, INTER_CUBIC);
            var buf = new MatOfByte();
            Imgcodecs.imencode(".jpg", image, buf);
            myCameraData = buf.toArray();
            serverFrame.updateMyCameraView(smallerImageCopy.toArray());
        }
    }

    public static void main(String[] args) throws IOException {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        new Server();
    }

    public void updateClientCamera(byte[] imageData) {
        serverFrame.updateClientCamera(imageData);
    }

    public byte[] getMyCameraData() {
        return myCameraData;
    }
}
