package org.example;

import com.google.protobuf.ByteString;
import io.grpc.ManagedChannelBuilder;
import org.example.grpc.StreamingServiceGrpc;
import org.example.ui.ClientFrame;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import static org.opencv.imgproc.Imgproc.INTER_CUBIC;

public class Client {
    private final ClientFrame clientFrame;

    public Client() {
        var clientStreamObserver = new ClientStreamObserver(this);
        var channel = ManagedChannelBuilder.forAddress("192.168.0.103", 9000)
                .usePlaintext()
                .build();
        var asyncStub = StreamingServiceGrpc.newStub(channel);
        var requestObserver = asyncStub.streamToServer(clientStreamObserver);

        clientFrame = new ClientFrame();
        var capture = new VideoCapture(0);
        var image = new Mat();
        Mat smallImage = new Mat();

        while (true) {
            capture.read(image);
            var buf = new MatOfByte();
            Imgcodecs.imencode(".jpg", image, buf);
            var smallBuf = new MatOfByte();
            Imgproc.resize(image, smallImage, new Size(160, 120), 0, 0, INTER_CUBIC);
            Imgcodecs.imencode(".jpg", smallImage, smallBuf);
            var myCameraData = buf.toArray();
            clientFrame.updateMyCameraView(smallBuf.toArray());
            var request = org.example.grpc.StreamingFrame.newBuilder()
                    .setContent(ByteString.copyFrom(myCameraData))
                    .build();
            requestObserver.onNext(request);
            // requestObserver.onCompleted();
        }
    }

    public void updateServerCamera(byte[] imageData) {
        clientFrame.updateServerCamera(imageData);
    }

    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        new Client();
    }
}
