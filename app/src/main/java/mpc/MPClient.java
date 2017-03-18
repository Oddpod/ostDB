package mpc;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.minlog.Log;
import com.example.odd.ostrino.Ost;

import java.io.IOException;

/**
 * Created by Odd on 07.03.2017.
 */

public class MPClient {
    public Client client;
    public Ost ost;
    private NetworkListener nl;
    private String serverIp = "2001:0:9d38:90d7:105c:6a9d:7e0e:1817";

    public MPClient() {
        Log.set(Log.LEVEL_DEBUG);
        client = new Client();
        register();

        nl = new NetworkListener();
        nl.init(client);
        client.addListener(nl);

        System.out.println("starting client");
        client.start();

        try{
            client.connect(5000, serverIp, 54555);
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
    }

    public void sendOst(Ost ost){
        nl.setOst(ost);
    }
}
