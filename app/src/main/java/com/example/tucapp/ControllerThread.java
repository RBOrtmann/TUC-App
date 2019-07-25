package com.example.tucapp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

public class ControllerThread extends Thread {
    private InetSocketAddress address;
    private ByteBuffer bb;

    ControllerThread(InetSocketAddress address){
        this.address = address;
    }

    public void run(){
        try {
            DatagramSocket ds = new DatagramSocket(address);
            DatagramPacket dp = new DatagramPacket(bb.array(), bb.array().length, address);
            while(ds.isConnected()){ // should this just be while(true)?
                if (!ds.isConnected()) {
                    synchronized(this) {
                        while (!ds.isConnected())
                            ds.connect(address);
                    }
                }

                dp.setData(bb.array());
                ds.send(dp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setBb(ByteBuffer bb){
        this.bb = bb;
    }
}
