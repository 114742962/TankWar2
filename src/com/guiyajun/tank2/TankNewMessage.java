/**
* @Title: TankMessage.java
* @Package com.guiyajun.tank
* @Description: TODO(用一句话描述该文件做什么)
* @author Administrator
* @date 2019年10月27日
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
 * @ProjectName:  [TankWar_NET] 
 * @Package:      [com.guiyajun.tank.TankMessage.java]  
 * @ClassName:    [TankMessage]   
 * @Description:  [包装坦克的消息]   
 * @Author:       [桂亚君]   
 * @CreateDate:   [2019年10月27日 下午7:18:16]   
 * @UpdateUser:   [桂亚君]   
 * @UpdateDate:   [2019年10月27日 下午7:18:16]   
 * @UpdateRemark: [说明本次修改内容]  
 * @Version:      [v1.0]
 */
public class TankNewMessage implements Message{
    private int messageType = TANK_NEW_MESSAGE;  
    Tank myTank = null;
    TankWarClient tankWarClient = null;
    private int serverUDPPort;
    private String serverIP;
    
    TankNewMessage(Tank myTank) {
        this.myTank = myTank;
        this.serverUDPPort = NetServer.serverUDPPort;
        this.serverIP = NetClient.serverIP;
    }
    
    TankNewMessage(TankWarClient tankWarClient) {
        this.tankWarClient = tankWarClient;
        this.serverUDPPort = NetServer.serverUDPPort;
        this.serverIP = NetClient.serverIP;
    }
    
    /**
    * @Title: send
    * @Description: TODO(这里用一句话描述这个方法的作用)
    * @param     参数 
    * @return void    返回类型
    * @throws
    */
    public void send(DatagramSocket datagramSocket) {
        ByteArrayOutputStream baos = null;
        DataOutputStream dos = null;
        try {
            if (myTank != null) {
                baos = new ByteArrayOutputStream();
                dos = new DataOutputStream(baos);
                dos.writeInt(messageType);
                dos.writeInt(myTank.id);
                dos.writeInt(myTank.x);
                dos.writeInt(myTank.y);
                dos.writeInt(myTank.getBloodOfTank());
                dos.writeInt(myTank.directionOfTank.ordinal());
                dos.writeBoolean(myTank.friendly);
            }
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
        
        if (baos != null) {
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
    }

    /**
    * @Title: parse
    * @Description: 解析UDP服务端转发的消息包
    * @param     参数 
    * @return void    返回类型
    * @throws
    */
    public void parse(DataInputStream dis) {
        
        try {
            int id = dis.readInt();
            if (tankWarClient.myTank != null && id == tankWarClient.myTank.id) {
                return;
            }
            
            int x = dis.readInt();
            int y = dis.readInt();
            int bloodOfTank = dis.readInt();
            Direction dir = Direction.values()[dis.readInt()];
            boolean friendly = dis.readBoolean();
            boolean exists = false;
            for (int i=0; i<tankWarClient.HeroTanksList.size(); i++) {
                Tank tank = tankWarClient.HeroTanksList.get(i);
                if (tank.id == id) {
                    exists = true;
                    break;
                }
            }
            
            if (exists == false) {
                Message message = new TankNewMessage(tankWarClient.myTank);
                tankWarClient.netClient.sendMessage(message);
System.out.println("Got a Tank_new_message from server!");
System.out.println("messageType:" + messageType + "-id:" + id + "-x:" + x + "-y:" + y 
    + "-dir:" + dir + "-friendly:" + friendly);
                
                HeroTank tank = null;
                
                if (id <= 110) {
                    tank = new HeroTank(700, 540 - 50 * (id - 100), true, tankWarClient);
                } else {
                    tank = new HeroTank(70, 540 - 50 * (id - 100), true, tankWarClient);
                }
                
                if (tank != null) {
                    tank.id = id;
                    tank.setBloodOfTank(bloodOfTank);
                    tankWarClient.HeroTanksList.add(tank);
                    System.out.println("add the tank to HeroTanksList!");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
