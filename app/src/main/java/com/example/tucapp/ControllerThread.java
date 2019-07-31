/*
 * File: ControllerThread.java
 * Author: Brendan Ortmann
 * Owner: Ring-Co LLC
 * For: TUC Companion App
 * Date: July 2019
 *
 * Desc: A thread for continuously sending information to the TUC while the app is connected to it. Retrieves data from ControllerActivity.
 */

package com.example.tucapp;

import android.util.Log;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.concurrent.BlockingQueue;

public class ControllerThread extends Thread {
    private InetSocketAddress address;
    private BlockingQueue<ByteBuffer> queue;

    ControllerThread(BlockingQueue<ByteBuffer> bq){
        // Host and port will probably need to be changed later
        this.queue = bq;
    }

    // Custom implementation of run() method from Thread interface
    @Override
    public void run(){
        ByteBuffer bb = ByteBuffer.allocateDirect(15);
        try {
            // Initialize the ByteBuffer, Socket, and Packet needed for transmitting data
            address = new InetSocketAddress(InetAddress.getByName("192.168.82.246"), 25565);
            DatagramSocket ds = new DatagramSocket();
            ds.connect(address);
            DatagramPacket dp = new DatagramPacket(bb.array(), bb.array().length, address);

            reconnect(ds);

            while(ds.isConnected()){ // should this just be while(true)?
                // If no longer connected, wait until the connection is reestablished
                reconnect(ds);

                // Send the data
                Log.d("ControllerThread", "Attempting take...");
                bb = queue.take(); // take() automatically waits until data is available to retrieve
                Log.d("ControllerThread", "Take confirmed.");
                dp.setData(bb.array());
                ds.send(dp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Reconnection method from Oracle (I think) retooled for my purposes
    private void reconnect(DatagramSocket ds) throws SocketException {
        if (!ds.isConnected()) {
            synchronized (this) {
                while (!ds.isConnected()) {
                    queue.clear(); // Clears the queue so no addt'l instructions are stored while disconnected
                    ds.connect(address); // Attempts to connect on the same address
                    Log.d("ControllerThread", "Reconnecting...");
                }
            }
        }
    }
}
