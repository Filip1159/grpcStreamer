package org.example;

import io.grpc.ServerBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Server {
    private final int port;
    private final io.grpc.Server server;

    public Server(int port) {
        this(ServerBuilder.forPort(port), port);
    }

    public Server(ServerBuilder serverBuilder, int port) {
        this.port = port;
        StreamingServiceImpl laptopService = new StreamingServiceImpl();
        server = serverBuilder.addService(laptopService)
                .build();
    }

    public void start() throws IOException {
        server.start();
        System.out.println("server started on port " + port);

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.err.println("shut down gRPC server because JVM shuts down");
                try {
                    Server.this.stop();
                } catch (InterruptedException e) {
                    e.printStackTrace(System.err);
                }
                System.err.println("server shut down");
            }
        });
    }

    public void stop() throws InterruptedException {
        if (server != null) {
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        }
    }

    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        Server server = new Server(9000);
        server.start();
        server.blockUntilShutdown();
    }
}
