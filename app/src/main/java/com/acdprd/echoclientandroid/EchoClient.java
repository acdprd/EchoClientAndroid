package com.acdprd.echoclientandroid;

import android.os.AsyncTask;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by maksismad on 01.05.2017.
 */

public class EchoClient {
    private final String SERVER_ADDR = "89.253.231.135";
    private final int SERVER_PORT = 5253;
    private final String END_COMMAND = "end";
    private final String SERVER_CLOSED_COMMAND = ">Server closed";
    private Socket sock = null;
    private DataOutputStream out;
    private DataInputStream in;
    private TextView output;
    private Thread run;
    private boolean running;

    public EchoClient(final TextView output) {
        this.output = output;
        running = true;
        try {
            sock = new Socket(SERVER_ADDR, SERVER_PORT);
            in = new DataInputStream(sock.getInputStream());
            out = new DataOutputStream(sock.getOutputStream());
            //TODO errors when exiting
            run = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (running) {
                        try {
                            String inMsg = in.readUTF();
                            if (inMsg != null) {
                                new Task().execute(inMsg);
                                if (inMsg.equals(SERVER_CLOSED_COMMAND)) {
                                    running = false;
                                }
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            run.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            running = false;
            out.writeUTF(END_COMMAND);
            out.flush();
            out.close();
            in.close();
            sock.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean sendMsg(String message) {
        boolean result = false;
        try {
            out.writeUTF(message);
            out.flush();
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return result;
        }
    }
    
    private class Task extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            return params[0];
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            output.append("\n");
            output.append(s);
        }
    }
}
