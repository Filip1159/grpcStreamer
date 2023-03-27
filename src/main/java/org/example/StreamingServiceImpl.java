package org.example;

import com.google.protobuf.ByteString;
import io.grpc.stub.StreamObserver;
import org.example.grpc.StreamingFrame;
import org.example.grpc.StreamingFrameResponse;
import org.example.grpc.StreamingServiceGrpc;


import static java.nio.charset.StandardCharsets.UTF_8;

public class StreamingServiceImpl extends StreamingServiceGrpc.StreamingServiceImplBase {

    @Override
    public StreamObserver<StreamingFrame> streamToServer(StreamObserver<StreamingFrameResponse> responseObserver) {
        return new StreamObserver<StreamingFrame>() {
            @Override
            public void onNext(StreamingFrame request) {
                System.out.println("rec");
                StreamingFrameResponse response = StreamingFrameResponse.newBuilder()
                        .setContent(ByteString.copyFrom("abc", UTF_8))
                        .build();

                responseObserver.onNext(response);
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
