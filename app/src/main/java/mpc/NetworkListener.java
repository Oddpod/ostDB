package mpc;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;
import com.example.odd.ostrino.DBHandler;
import com.example.odd.ostrino.Ost;

/**
 * Created by Odd on 07.03.2017.
 */

public class NetworkListener extends Listener {
    private Client client;
    private Ost ost;

    public void init(Client client){
        this.client = client;
    }

    public void connected(Connection arg0){
        Log.info("[CLIENT] You have connected.");
        client.sendTCP(new Packet.Packet0LoginRequest());
    }

    public void disconnected(Connection arg0){
        Log.info("[CLIENT] You have disconnected.");
    }

    public void received(Connection c, Object o){
        if( o instanceof Packet.Packet0LoginRequest){
            boolean answer = ((Packet.Packet1LoginAnswer) o).accepted;

            if(answer){
                Packet.Packet3Ost ostPacket = new Packet.Packet3Ost();
                ostPacket.ost = ost;
                client.sendTCP(ostPacket);
            } else{
                c.close();
            }

        }
        if( o instanceof Packet.Packet2Message){
            String message = ((Packet.Packet2Message)o).message;
            Log.info(message);
        }
    }

    public void setOst(Ost ost){
        this.ost = ost;
    }
}
