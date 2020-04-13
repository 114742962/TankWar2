/**
* @Title: NetClient.java
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
import java.net.Socket;
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
        } catch (UnknownHostException e) {
            System.out.println("没有找到服务端！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void sendMessage(Message message) {
        
    }
}
