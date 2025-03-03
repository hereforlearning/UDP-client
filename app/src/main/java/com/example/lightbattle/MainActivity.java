package com.example.lightbattle;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import android.widget.TextView;
import android.os.Handler;
import android.os.Message;

public class MainActivity extends AppCompatActivity {
    private TextView udpresult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btSendMessage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int nbrepet = Integer.parseInt(((TextView) findViewById(R.id.EdNbRepete)).getText().toString());
                String data = ((TextView) findViewById(R.id.EdText)).getText().toString();
                int port = Integer.valueOf(((TextView) findViewById(R.id.EdPort)).getText().toString());
                String address = ((TextView) findViewById(R.id.EdIpServeur)).getText().toString();
                SendData(nbrepet,data,port,address);
            }
        });

        findViewById(R.id.bshutdown).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int nbrepet = Integer.parseInt(((TextView) findViewById(R.id.EdNbRepete)).getText().toString());
                String data = ((TextView) findViewById(R.id.EdText)).getText().toString();
                int port = Integer.valueOf(((TextView) findViewById(R.id.EdPort)).getText().toString());
                String address = ((TextView) findViewById(R.id.EdIpServeur)).getText().toString();
                SendData(1,"akjsdfuewirmzxcvhbreith32434",51234,"192.168.0.108");
            }
        });


        findViewById(R.id.babordshutdown).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int nbrepet = Integer.parseInt(((TextView) findViewById(R.id.EdNbRepete)).getText().toString());
                String data = ((TextView) findViewById(R.id.EdText)).getText().toString();
                int port = Integer.valueOf(((TextView) findViewById(R.id.EdPort)).getText().toString());
                String address = ((TextView) findViewById(R.id.EdIpServeur)).getText().toString();
                SendData(1,"aafsdfaw342fq23qfea32thgeyu567iii",51234,"192.168.0.108");
            }
        });

        udpresult=(TextView) findViewById(R.id.tvudpresult);
        ReceiveData(51235);


    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch(msg.what){
                case 0:
                    String data = (String) msg.obj;
                    udpresult.setText(data);
                    break;
            }
        };
    };



    private DatagramSocket UDPSocket;
    private InetAddress address;
    private int port;

    /// Initialise une socket avec les parametres recupere dans l'interface graphique pour l'envoi des données
    public void Initreseau(InetAddress address) {
        try {
            this.UDPSocket = new DatagramSocket();
            this.address = address;
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    /// Envoi les données dans la socket defini par la methode InitReseau
    public void SendInstruction(final byte[] data, final int port) {
        new Thread() {
            @Override
            public void run() {
                try {
                    DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
                    UDPSocket.send(packet);
                    DatagramPacket packetreponse = null;
                    UDPSocket.receive(packetreponse);
                    DisplayData(packetreponse);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }

    /// Envoi X fois la data
    public void SendData(final int nbRepet, final String Sdata , final int port, final String address) {
        new Thread() {
            @Override
            public void run() {
                try {
                    Initreseau(InetAddress.getByName(address));
                    for (int i = 0; i < nbRepet; i++) {
                        byte[] data = Sdata.getBytes();
                        SendInstruction(data,port);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    /// scan le port mis en parametre
    public void ReceiveData(final int portNum) {
        new Thread() {
            @Override
            public void run() {
                try {

                    final int taille = 1024;
                    final byte[] buffer = new byte[taille];
                    DatagramSocket socketReceive = new DatagramSocket(portNum);
                    while (true) {
                        DatagramPacket data = new DatagramPacket(buffer, buffer.length);
                        socketReceive.receive(data);
                        DisplayData(data);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }

    // Modifie l affichage en fonction de la tram recu
    public void DisplayData(DatagramPacket data) {
        Message msg = new Message();
        String msgdata = new String(data.getData(), data.getOffset(), data.getLength());
        msg.obj = msgdata;//可以是基本类型，可以是对象，可以是List、map等；
        msg.what=0;
        mHandler.sendMessage(msg);
        System.out.println(data);
    }

}
