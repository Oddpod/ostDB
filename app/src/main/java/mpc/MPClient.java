package mpc;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.minlog.Log;
import com.example.odd.ostrino.Ost;

import java.io.IOException;
import java.util.List;

/**
 * Created by Odd on 07.03.2017.
 */

public class MPClient {
    public Client client;
    public Ost ost;
    private NetworkListener nl;
    private String serverIp = "192.168.1.3";
    private int tcpPort = 54555;
    private int udpPort = 54556;

    public MPClient() {
        client = new Client();
        register();

        nl = new NetworkListener();
        nl.init(client);
        client.addListener(nl);

        System.out.println("starting client");
        client.start();

        try{
            client.connect(5000, serverIp, tcpPort);
        } catch(IOException e){
            e.printStackTrace();
            System.out.println("heyooooooooooo");
            client.stop();
        }
    }

    public void register(){
        Kryo kryo = client.getKryo();
        kryo.register(Packet.Packet0LoginRequest.class);
        kryo.register(Packet.Packet1LoginAnswer.class);
        kryo.register(Packet.Packet2Message.class);
        kryo.register(Packet.Packet3Ost.class);
        kryo.register(Ost.class);
        kryo.register(Packet.Packet4UpdateRequest.class);
    }

    public void sendOst(Ost ost){
        nl.addOstToList(ost);
    }

    public List<Ost> receiveOsts(){
        System.out.println(nl.getreceivedOsts());
        return nl.getreceivedOsts();
    }
}
