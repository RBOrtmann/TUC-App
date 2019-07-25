package com.example.tucapp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.BitSet;

public class ControllerThread extends Thread {
    private InetSocketAddress address;
    private BitSet bs;

    ControllerThread(InetSocketAddress address){
        this.address = address;
    }

    public void run(){
        try {
            DatagramSocket ds = new DatagramSocket(address);
            DatagramPacket dp = new DatagramPacket(bs.toByteArray(), bs.length(), address);
            while(ds.isConnected()){ // should this just be while(true)?
                if (!ds.isConnected()) {
                    synchronized(this) {
                        while (!ds.isConnected()) {
                            ds.connect(address);
                        }
                    }
                }

                dp.setData(bs.toByteArray());
                ds.send(dp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setBs(BitSet bs){
        this.bs = bs;
    }
}
