package org.example;

import com.google.protobuf.ByteString;
import io.grpc.stub.StreamObserver;
import org.example.grpc.StreamingFrame;
import org.example.grpc.StreamingFrameResponse;
import org.example.grpc.StreamingServiceGrpc;

public class StreamingServiceImpl extends StreamingServiceGrpc.StreamingServiceImplBase {
    private final Server server;

    public StreamingServiceImpl(Server server) {
        this.server = server;
    }

    @Override
    public StreamObserver<StreamingFrame> streamToServer(StreamObserver<StreamingFrameResponse> responseObserver) {
        return new StreamObserver<>() {
            @Override
            public void onNext(StreamingFrame request) {
                System.out.println("onNext");
                server.updateClientCamera(request.getContent().toByteArray());
                responseObserver.onNext(StreamingFrameResponse.newBuilder()
                        .setContent(ByteString.copyFrom(server.getMyCameraData()))
                        .build());
            }

            @Override
            public void onError(Throwable t) {
                System.out.println(t.getMessage());
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
    }
}
