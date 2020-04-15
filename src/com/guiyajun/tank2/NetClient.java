/**
* @Title: NetClient.java
* @Package com.guiyajun.tank2
* @Description: TODO(用一句话描述该文件做什么)
* @author Administrator
* @date 2020年4月13日
* @version V1.0
*/
package com.guiyajun.tank2;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.BindException;
import java.net.ConnectException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

/**  
 * @ProjectName:  [TankWar2] 
 * @Package:      [com.guiyajun.tank2.NetClient.java]  
 * @ClassName:    [NetClient]   
 * @Description:  [网络客户端]   
 * @Author:       [Guiyajun]   
 * @CreateDate:   [2020年4月13日 下午1:24:38]   
 * @UpdateUser:   [Guiyajun]   
 * @UpdateDate:   [2020年4月13日 下午1:24:38]   
 * @UpdateRemark: [说明本次修改内容]  
 * @Version:      [v1.0]
 */
public class NetClient {
    public static int serverTCPPort;
    public static int clientUDPPort;
    public static String serverIP;
    public TankWarClient tankWarClient = null;
    private DatagramSocket datagramSocket = null;
    
    NetClient(TankWarClient tankWarClient) {
        this.tankWarClient = tankWarClient;
        NetClient.serverTCPPort = NetServer.serverTCPPort;
    }
    
    public void connect() {
        Socket socket = null;
        DataInputStream dis = null;
        DataOutputStream dos = null;
        
        try {
            socket = new Socket(serverIP, serverTCPPort);
            dos = new DataOutputStream(socket.getOutputStream());
            dos.writeInt(clientUDPPort);
System.out.println("Send my UDPPort to server:" + clientUDPPort);
            dis = new DataInputStream(socket.getInputStream());
            int id = dis.readInt();
            
            if (id <= 110) {
                tankWarClient.myTank = new HeroTank(700, 540 - 50 * (id - 100), true, tankWarClient);
            } else {
                tankWarClient.myTank = new HeroTank(70, 540 - 50 * (id - 100), true, tankWarClient);
            }
            
            tankWarClient.myTank.id = id;
System.out.println("Connect to server and get a id:" + id);            
        } catch (ConnectException e) {
            System.out.println("The TCPServer is not started!");
        } catch (UnknownHostException e) {
            System.out.println("没有找到服务端！");
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        try {
            datagramSocket = new DatagramSocket(clientUDPPort);
        } catch (BindException e) {
            System.out.println("The udpPort:" + clientUDPPort +"is already be used!");
        } catch (SocketException e) {
            e.printStackTrace();
        }
        
        ThreadPoolService.getInstance().execute(new UDPThread());
        
    }
    
    public void sendMessage(Message message) {
        message.send(datagramSocket);
    }
    
    private class UDPThread implements Runnable {
        byte[] buffered = new byte[1024];
        @Override
        public void run() {
            try {
                DatagramPacket datagramPacket = new DatagramPacket(buffered, buffered.length);
                while (datagramSocket != null) {
                    datagramSocket.receive(datagramPacket);
                    parse(datagramPacket);
                } 
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        public void parse(DatagramPacket datagramPacket) {
            ByteArrayInputStream bais = new ByteArrayInputStream(buffered);
            DataInputStream dis = new DataInputStream(bais);
            int messageType = 0;
            
            try {
                messageType = dis.readInt();
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            Message message = null;
            switch (messageType) {
                case Message.TANK_NEW_MESSAGE:
                    message = new TankNewMessage(tankWarClient);
                    message.parse(dis);
                    break;
                case Message.TANK_MOVE_MESSAGE:
                    message = new TankMoveMessage(tankWarClient);
                    message.parse(dis);
                    break;
                case Message.TANK_FIRE_MESSAGE:
                    message = new TankFireMessage(tankWarClient);
                    message.parse(dis);
                    break;                    
                case Message.TANK_BARREL_MESSAGE:
                    message = new TankBarrelMessage(tankWarClient);
                    message.parse(dis);
                    break;                    
                case Message.TANK_SUPER_FIRE_MESSAGE:
                    message = new TankSuperFireMessage(tankWarClient);
                    message.parse(dis);
                    break;                    
                case Message.TANK_EXIT_MESSAGE:
                    message = new TankExitMessage(tankWarClient);
                    message.parse(dis);
                    break;                    
                case Message.TANK_EXIT_RECEIVED_MESSAGE:
                    message = new TankExitRecievedMessage(tankWarClient.myTank);
                    message.parse(dis);
                    break;
                default:
                    break;
            }
        }
    }
}
