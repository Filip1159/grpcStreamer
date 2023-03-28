package org.example;

import io.grpc.stub.StreamObserver;
import org.example.grpc.StreamingFrameResponse;

public class ClientStreamObserver implements StreamObserver<StreamingFrameResponse> {
    private final Client client;

    public ClientStreamObserver(Client client) {
        this.client = client;
    }

    @Override
    public void onNext(StreamingFrameResponse response) {
        client.updateServerCamera(response.getContent().toByteArray());
    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println(throwable.getMessage());
    }

    @Override
    public void onCompleted() {
        System.out.println("onCompleted");
    }
}
