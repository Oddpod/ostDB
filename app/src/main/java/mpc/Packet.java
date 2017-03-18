package mpc;

import com.example.odd.ostrino.Ost;

/**
 * Created by Odd on 07.03.2017.
 */

public class Packet {
    public static class Packet0LoginRequest {}
    public static class Packet1LoginAnswer {boolean accepted = false;}
    public static class Packet2Message { String message;}
    public static class Packet3Ost {Ost ost;
    public void setOst(Ost ost){
        this.ost = ost;
    }}
}
