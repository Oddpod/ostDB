package mpc;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;
import com.example.odd.ostrino.DBHandler;
import com.example.odd.ostrino.Ost;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Odd on 07.03.2017.
 */

public class NetworkListener extends Listener {
    private Client client;
    private List<Ost> ostList = new ArrayList();
    private List<Ost> receivedOsts = new ArrayList();

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
        if( o instanceof Packet.Packet1LoginAnswer){
            boolean answer = ((Packet.Packet1LoginAnswer) o).accepted;

            if(answer){
                for( Ost ost : ostList) {
                    Packet.Packet3Ost ostPacket = new Packet.Packet3Ost();
                    ostPacket.ost = ost;
                    client.sendTCP(ostPacket);
                }
                Packet.Packet4UpdateRequest updateRequest = new Packet.Packet4UpdateRequest();
                client.sendTCP(updateRequest);
                }
            } else{
                c.close();
            }

        if( o instanceof Packet.Packet2Message){
            String message = ((Packet.Packet2Message)o).message;
            Log.info(message);
        }

        if(o instanceof Packet.Packet3Ost){
            Ost ost = ((Packet.Packet3Ost)o).ost;
            System.out.println(ost);
            receivedOsts.add(ost);
            System.out.println("Received osts: " + receivedOsts);
        }
    }

    public void addOstToList(Ost ost){
        this.ostList.add(ost);
    }

    public List<Ost> getreceivedOsts(){
        return receivedOsts;
    }
}
