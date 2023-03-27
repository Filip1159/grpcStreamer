package org.example;

import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.example.grpc.StreamingFrame;
import org.example.grpc.StreamingFrameResponse;
import org.example.grpc.StreamingServiceGrpc;

import java.util.Scanner;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Client {
    private final ManagedChannel channel;
    private final StreamingServiceGrpc.StreamingServiceStub asyncStub;

    public Client(String host, int port) {
        channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        asyncStub = StreamingServiceGrpc.newStub(channel);
    }

    public void rateLaptop(String[] laptopIDs) throws InterruptedException {
        StreamObserver<StreamingFrame> requestObserver = asyncStub
                .streamToServer(new StreamObserver<StreamingFrameResponse>() {
                    @Override
                    public void onNext(StreamingFrameResponse response) {
                        System.out.println("laptop rated");
                    }

                    @Override
                    public void onError(Throwable t) {
                        System.out.println(t.getMessage());
                    }

                    @Override
                    public void onCompleted() {
                        System.out.println("onCompleted");
                    }
                });

        int n = laptopIDs.length;
        try {
            for (int i = 0; i < n; i++) {
                StreamingFrame request = StreamingFrame.newBuilder()
                        .setContent(ByteString.copyFrom("abcd", UTF_8))
                        .build();
                requestObserver.onNext(request);
            }
        } catch (Exception e) {
            return;
        }

        requestObserver.onCompleted();
    }

    public static void testRateLaptop(Client client) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String answer = scanner.nextLine();
            if (answer.toLowerCase().trim().equals("n")) {
                break;
            }
            client.rateLaptop(new String[]{"", "", ""});
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Client client = new Client("0.0.0.0", 9000);

        testRateLaptop(client);
    }
}
