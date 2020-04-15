/**
* @Title: TankMoveMesssage.java
* @Package com.guiyajun.tank2
* @Description: TODO(用一句话描述该文件做什么)
* @author Administrator
* @date 2020年4月15日
* @version V1.0
*/
package com.guiyajun.tank2;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

/**  
 * @ProjectName:  [TankWar2] 
 * @Package:      [com.guiyajun.tank2.TankMoveMesssage.java]  
 * @ClassName:    [TankMoveMesssage]   
 * @Description:  [一句话描述该类的功能]   
 * @Author:       [Guiyajun]   
 * @CreateDate:   [2020年4月15日 下午3:34:00]   
 * @UpdateUser:   [Guiyajun]   
 * @UpdateDate:   [2020年4月15日 下午3:34:00]   
 * @UpdateRemark: [说明本次修改内容]  
 * @Version:      [v1.0]
 */
public class TankMoveMessage implements Message {
    private int messageType = Message.TANK_MOVE_MESSAGE;
    private int serverUDPPort;
    private String serverIP;
    public Tank myTank = null;
    public TankWarClient tankWarClient = null;
    
    TankMoveMessage(Tank myTank) {
        this.myTank = myTank;
        this.serverUDPPort = NetServer.serverUDPPort;
        this.serverIP = NetClient.serverIP;
    }
    
    TankMoveMessage(TankWarClient tankWarClient) {
        this.tankWarClient = tankWarClient;
        this.serverUDPPort = NetServer.serverUDPPort;
        this.serverIP = NetClient.serverIP;
    }

    @Override
    public void send(DatagramSocket datagramSocket) {
        ByteArrayOutputStream baos = null;
        DataOutputStream dos = null;
        try {
            baos = new ByteArrayOutputStream();
            dos = new DataOutputStream(baos);
            dos.writeInt(messageType);
            dos.writeInt(myTank.id);
            dos.writeInt(myTank.x);
            dos.writeInt(myTank.y);
            dos.writeInt(myTank.directionOfTank.ordinal());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (dos != null) {
                try {
                    dos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        byte[] buf = baos.toByteArray();
        DatagramPacket datagramPacket = new DatagramPacket(buf, buf.length, 
            new InetSocketAddress(serverIP, serverUDPPort));
        try {
            if (datagramSocket != null) {
                datagramSocket.send(datagramPacket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void parse(DataInputStream dis) {
        try {
            int id = dis.readInt();
            if (myTank != null && id == myTank.id) {
                return;
            }
            int x = dis.readInt();
            int y = dis.readInt();
            Direction direction = Direction.values()[dis.readInt()];
            
            for (int i=0; i<tankWarClient.HeroTanksList.size(); i++) {
                HeroTank tank = tankWarClient.HeroTanksList.get(i);
                if (tank.id == id) {
                    tank.directionOfTank = direction;
                    tank.x = x;
                    tank.y = y;
                    break;
                }
            }
            System.out.println("Got a Tank_move_message from server!");
            System.out.println("messageType:" + messageType + "-id:" + id + "-x:" + x + "-y:" + y 
                + "-direction:" + direction);            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
