/**
* @Title: EnermyTank.java
* @Package com.guiyajun.tank2
* @Description: TODO(用一句话描述该文件做什么)
* @author Administrator
* @date 2020年4月9日
* @version V1.0
*/
package com.guiyajun.tank2;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

/**  
 * @ProjectName:  [TankWar2] 
 * @Package:      [com.guiyajun.tank2.EnermyTank.java]  
 * @ClassName:    [EnermyTank]   
 * @Description:  [机器人坦克]   
 * @Author:       [Guiyajun]   
 * @CreateDate:   [2020年4月9日 上午11:10:52]   
 * @UpdateUser:   [Guiyajun]   
 * @UpdateDate:   [2020年4月9日 上午11:10:52]   
 * @UpdateRemark: [说明本次修改内容]  
 * @Version:      [v1.0]
 */
public class VITank extends Tank {
    /** 申明一个随机数生成器，用于生成随机步数和随机发射炮弹 */
    public static Random random = new Random();
    /** turnDirectionFrequency控制机器人坦克下次变向前的要走的随机步数范围，值越大变向的频率越低 */
    public int turnDirectionFrequency = 20;
    /** step变量随机生成机器人坦克要走的步数，最低为3步 */
    public int step = random.nextInt(turnDirectionFrequency) + 3;
    /** fireFrequency控制机器人坦克子弹发射的倒计数最大范围，值越大，炮弹发射的越少 */
    public int fireFrequency = 50;
    /** 随机生成坦克发射炮弹的倒计数，假如fireFrequency=50，范围是0-50 */
    public int fireCountdown = random.nextInt(fireFrequency);

    /**
    * @Description: 创建一个新的实例 EnermyTank.
    * @param x
    * @param y
    * @param friendly
    */
    public VITank(int x, int y, boolean friendly) {
        super(x, y, friendly);
    }

    /**
    * @Description: 创建一个新的实例 EnermyTank.
    * @param x
    * @param y
    * @param friendly
    * @param tankWarClient
    */
    public VITank(int x, int y, Color colorOfTank, boolean friendly, TankWarClient tankWarClient) {
        super(x, y, colorOfTank, friendly, tankWarClient);
    }
    
    @Override
    public void draw(Graphics g) {
        if (false == this.getAliveOfTank()) {
            return;
        }
        
        //获取初始颜色并画出坦克
        Color c = g.getColor();
        g.setColor(colorOfTank);
        g.fillOval(x, y, TANK_WIDTH, TANK_HEIGHT);
        
        /*
                    如果步数为0就获取新的移动方向，并获取炮管方向，生成新的步数，否则步数减去1
        */
        if (step == 0) {
            getDirection();
            getBarrelDirection();
            step = random.nextInt(turnDirectionFrequency) + turnDirectionFrequency;
        } else {
            step --;
        }
        
        /*
                   如果倒计数为0就开火，并生成新的倒计数，否则倒计数减1
        */
        if(fireCountdown == 0 && friendly == false) {
            tankWarClient.missilesList.add(fire(tankWarClient.colorOfEnermyMissile, this.friendly, directionOfBarrel));
            fireCountdown= random.nextInt(fireFrequency) + fireFrequency;
        } else if (fireCountdown == 0 && friendly == true) {
            tankWarClient.missilesList.add(fire(tankWarClient.colorOfFriendlyMissile, this.friendly, directionOfBarrel));
            fireCountdown= random.nextInt(fireFrequency) + fireFrequency;
        } else {
            fireCountdown --;
        }
        
        /*
                    移动，碰撞检测，画出坦克的炮筒
        */
        tankMove();
        drawBarrel(g);
        drawBlood(g, this.getBloodOfTank());
        
        // 还原颜色
        g.setColor(c);
    }
    
    @Override
    void getDirection() { 
        Direction[] dirOfVITank = Direction.values();
        int indexOfDirection = random.nextInt(dirOfVITank.length);
        directionOfTank = dirOfVITank[indexOfDirection];
    }
}
