package com.example.tucapp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.concurrent.BlockingQueue;

public class ControllerThread extends Thread {
    private InetSocketAddress address;
    private final BlockingQueue<ByteBuffer> queue;

    ControllerThread(InetSocketAddress address, BlockingQueue<ByteBuffer> bq){
        this.address = address;
        this.queue = bq;
    }

    // Custom implementation of run() method from Thread interface
    @Override
    public void run(){
        ByteBuffer bb;
        try {
            // Initialize the ByteBuffer, Socket, and Packet needed for transmitting data
            bb = queue.take();
            DatagramSocket ds = new DatagramSocket(address);
            DatagramPacket dp = new DatagramPacket(bb.array(), bb.array().length, address);

            while(ds.isConnected()){ // should this just be while(true)?
                if (!ds.isConnected()) {
                    synchronized(this) {
                        while (!ds.isConnected())
                            queue.clear(); // Clears the queue so no addt'l instructions are stored while disconnected
                            ds.connect(address); // Attempts to connect on the same address
                    }
                }

                // Send the data
                bb = queue.take();
                dp.setData(bb.array());
                ds.send(dp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
