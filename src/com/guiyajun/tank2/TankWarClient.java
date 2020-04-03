package com.guiyajun.tank2;
import java.awt.Color;
import java.awt.Frame;

/**
* @Title: TankWarClient.java
* @Package 
* @Description: TODO(用一句话描述该文件做什么)
* @author Administrator
* @date 2020年4月1日
* @version V1.0
*/

/**  
 * @ProjectName:  [TankWar2] 
 * @Package:      [.TankWarClient.java]  
 * @ClassName:    [TankWarClient]   
 * @Description:  [坦克大战的客户端]   
 * @Author:       [Guiyajun]   
 * @CreateDate:   [2020年4月1日 上午10:47:30]   
 * @UpdateUser:   [Guiyajun]   
 * @UpdateDate:   [2020年4月1日 上午10:47:30]   
 * @UpdateRemark: [说明本次修改内容]  
 * @Version:      [v1.0]
*/
public class TankWarClient extends Frame {
    /**
    * @Fields field:field:{todo}(用一句话描述这个变量表示什么)
    */
    private static final long serialVersionUID = 1L;
    /** 游戏客户端的宽度 */
    public static final int GAME_WIDTH = 800;
    /** 游戏客户端的高度 */
    public static final int GAME_HEIGHT = 600;
    /** 记分区的高度 */
    public static final int SCORE_AREA_HEIGHT = 60;
    
    public static void main(String[] args) {
        TankWarClient twc = new TankWarClient();
        twc.launchFrame();
    }
    
    public void launchFrame() {
        this.setTitle("坦克大战2");
        this.setBounds(100, 100, GAME_WIDTH, GAME_HEIGHT);
        this.setVisible(true);
        this.setBackground(Color.blue);
    }
}
