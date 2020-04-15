/**
* @Title: TankSuperFireMessage.java
* @Package com.guiyajun.tank
* @Description: TODO(用一句话描述该文件做什么)
* @author Administrator
* @date 2019年10月29日
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
 * @Package:      [com.guiyajun.tank.TankSuperFireMessage.java]  
 * @ClassName:    [TankSuperFireMessage]   
 * @Description:  [一句话描述该类的功能]   
 * @Author:       [桂亚君]   
 * @CreateDate:   [2019年10月29日 下午8:53:12]   
 * @UpdateUser:   [桂亚君]   
 * @UpdateDate:   [2019年10月29日 下午8:53:12]   
 * @UpdateRemark: [说明本次修改内容]  
 * @Version:      [v1.0]
 */
public class TankSuperFireMessage implements Message {
    private int messageType = Message.TANK_SUPER_FIRE_MESSAGE;
    private int serverUDPPort;
    private String serverIP;
    public Tank HeroTank = null;
    public TankWarClient tankWarClient = null;
    
    public TankSuperFireMessage(Tank HeroTank) {
        this.HeroTank = HeroTank;
        this.serverUDPPort = NetServer.serverUDPPort;
        this.serverIP = NetClient.serverIP;
    }
    
    public TankSuperFireMessage(TankWarClient tankWarClient) {
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
            dos.writeInt(HeroTank.id);
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
            datagramSocket.send(datagramPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void parse(DataInputStream dis) {
        try {
            int id = dis.readInt();
            if (HeroTank != null && id == HeroTank.id) {
                return;
            }
            
            for (int i=0; i<tankWarClient.HeroTanksList.size(); i++) {
                HeroTank tank = tankWarClient.HeroTanksList.get(i);
                if (tank.id == id) {
                    if (tank.getAliveOfTank() == false) {
                        return;
                    }
                    
                    // 将枚举变量Direction中的值转化为Direction数组
                    Direction[] dirs = Direction.values();
                    
                    // 每个方向发射一枚炮弹
                    for(int j=0; j<dirs.length - 1; j++) {
                        tankWarClient.missilesList.add(tank.fire(tank.colorOfEnermyMissile, false, dirs[j]));
                    }
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
