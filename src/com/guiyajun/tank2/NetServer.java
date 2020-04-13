/**
* @Title: NetServer.java
* @Package com.guiyajun.tank2
* @Description: TODO(用一句话描述该文件做什么)
* @author Administrator
* @date 2020年4月13日
* @version V1.0
*/
package com.guiyajun.tank2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**  
 * @ProjectName:  [TankWar2] 
 * @Package:      [com.guiyajun.tank2.NetServer.java]  
 * @ClassName:    [NetServer]   
 * @Description:  [网络服务端]   
 * @Author:       [Guiyajun]   
 * @CreateDate:   [2020年4月13日 上午11:36:04]   
 * @UpdateUser:   [Guiyajun]   
 * @UpdateDate:   [2020年4月13日 上午11:36:04]   
 * @UpdateRemark: [说明本次修改内容]  
 * @Version:      [v1.0]
 */
public class NetServer {
    /** 从配置文件中读取服务端TCP端口号 */
    public static int serverTCPPort;
    /** UDP端口号 */
    public static int serverUDPPort;
    /** 当start()被调用时改变，用于是否开始接受TCP客户端的连接 */
    boolean start = false;
    /** 创建一个netClient集合 */
    private List<Client> clients = new ArrayList<>();
    
    /**
     * 
    * @Description: 创建一个新的实例 NetServer.
     */
    NetServer() {}
    
    public void start() {
        start = true;
        ServerSocket ss = null;
        DataInputStream dis = null;
        DataOutputStream dos = null;
        
        try {
            ss = new ServerSocket(serverTCPPort);
System.out.println("The TCPServer is start at port:" + serverTCPPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        while (start) {
            try {
                Socket socket = ss.accept();
System.out.println("A client is connect!" + socket.getInetAddress() + ":" + socket.getPort());
                dis = new DataInputStream(socket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * @ProjectName:  [TankWar_NET] 
     * @Package:      [com.guiyajun.tank.NetServer.java]  
     * @ClassName:    [Client]   
     * @Description:  [客户端在服务端内部的包装类，记录每个连接到服务端的客户端信息，用于UDP回传]   
     * @Author:       [桂亚君]   
     * @CreateDate:   [2019年10月27日 上午10:40:31]   
     * @UpdateUser:   [桂亚君]   
     * @UpdateDate:   [2019年10月27日 上午10:40:31]   
     * @UpdateRemark: [说明本次修改内容]  
     * @Version:      [v1.0]
     */
    private class Client {
        public String ip;
        public int clientUDPPort;
        
        Client(String ip, int clientUDPPort) {
            this.ip = ip;
            this.clientUDPPort = clientUDPPort;
        }
    }
}
