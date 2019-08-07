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

import java.io.ByteArrayOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.BitSet;
import java.util.concurrent.BlockingQueue;

public class ControllerThread extends Thread {
    private InetSocketAddress address;
    private BlockingQueue<ByteBuffer> queue;
    private BlockingQueue<ByteBuffer> bq2;

    ControllerThread(BlockingQueue<ByteBuffer> bq, BlockingQueue<ByteBuffer> bq2){
        // Host and port will probably need to be changed later
        this.queue = bq;
        this.bq2 = bq2;
    }

    // Custom implementation of run() method from Thread interface
    @Override
    public void run(){
        ByteBuffer bb = ByteBuffer.allocateDirect(15);
        ByteArrayOutputStream outputStreamJoystick = new ByteArrayOutputStream();
        ByteArrayOutputStream outputStreamButtons = new ByteArrayOutputStream();

        byte[] joystickPrefix = new byte[]{
                0x43, // Hex value for 'C' in ASCII
                0x0B, // Indicator for 11 bytes of data
                0x00, // Command for 11 bit ID
                0x00, // MSB of CAN ID
                0x01 // LSB of CAN ID
        };

        byte[] buttonsPrefix = new byte[]{
                0x43, // Hex value for 'C' in ASCII
                0x0B, // Indicator for 11 bytes of data
                0x00, // Command for 11 bit ID
                0x00, // MSB of CAN ID
                0x02 // LSB of CAN ID
        };

        try {
            // Initialize the ByteBuffer, Socket, and Packet needed for transmitting data
            address = new InetSocketAddress(InetAddress.getByName("192.168.82.246"), 25565);

            DatagramSocket ds = new DatagramSocket();
            DatagramPacket dp = new DatagramPacket(bb.array(), 15, address);

            while(this.isAlive()){ // should this just be while(true)?

                // If no longer connected, wait until the connection is reestablished
                reconnect(ds);

                // Construct the joystick data
                bb = queue.take(); // take() automatically waits until data is available to retrieve
                outputStreamJoystick.write(joystickPrefix);
                outputStreamJoystick.write(bb.array());
                outputStreamJoystick.write(checkSum(outputStreamJoystick.toByteArray()));
                outputStreamJoystick.write(0x0D); // Hex for ASCII carriage return
                Log.d("outputStreamJoystick", bytesToHex(outputStreamJoystick.toByteArray()));

                // Construct the button data
                bb = bq2.take();
                outputStreamButtons.write(buttonsPrefix);
                outputStreamButtons.write(bb.array());
                outputStreamButtons.write(checkSum(outputStreamButtons.toByteArray()));
                outputStreamButtons.write(0x0D); // Hex for ASCII carriage return
                Log.d("outputStreamButtons", bytesToHex(outputStreamButtons.toByteArray()));

                // Send the data
                dp.setData(outputStreamJoystick.toByteArray());
                ds.send(dp);

                dp.setData(outputStreamButtons.toByteArray());
                ds.send(dp);

                // Reset output streams
                outputStreamJoystick.reset();
                outputStreamButtons.reset();
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

    private byte checkSum(byte[] bytes){
        byte sum = 0;
        for(byte b : bytes){
            sum ^= b;
        }
        return sum;
    }

    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }
}
