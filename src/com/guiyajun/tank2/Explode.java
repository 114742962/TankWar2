/**
* @Title: Explode.java
* @Package com.guiyajun.tank2
* @Description: TODO(用一句话描述该文件做什么)
* @author Administrator
* @date 2020年4月9日
* @version V1.0
*/
package com.guiyajun.tank2;

import java.awt.Color;
import java.awt.Graphics;

/**  
 * @ProjectName:  [TankWar2] 
 * @Package:      [com.guiyajun.tank2.Explode.java]  
 * @ClassName:    [Explode]   
 * @Description:  [爆炸效果]   
 * @Author:       [Guiyajun]   
 * @CreateDate:   [2020年4月9日 上午11:15:52]   
 * @UpdateUser:   [Guiyajun]   
 * @UpdateDate:   [2020年4月9日 上午11:15:52]   
 * @UpdateRemark: [说明本次修改内容]  
 * @Version:      [v1.0]
 */
public class Explode {
    
    /** 客户端实例变量用于管理元素 */
    public TankWarClient twc = null;
    /** 定义爆炸效果直径数组  */
    private int[] diameter = {6, 14, 22, 30, 38, 38, 22};
    /** 定义爆炸直径数组的初始下标值  */
    private int step = 0;
    /** 爆炸效果的x坐标  */
    private int x;
    /** 爆炸效果的y坐标  */
    private int y;
    /** 爆炸效果是否存活  */
    private boolean aliveOfExplode = true;
    
    /**
          * 创建一个新的实例 Explode.
    * @param x  x坐标
    * @param y  y坐标
    * @param twc    客户端实例
     */
    public Explode(int x, int y, TankWarClient twc) {
        this.x = x;
        this.y = y;
        this.twc = twc;
    }
    
    /**
    * @Title: draw
    * @Description: (爆炸效果的绘制方法)
    * @param @param g    参数，可以理解为绘画用的笔 
    * @return void    返回类型
    * @throws
     */
    public void draw(Graphics g) {
        if (false == aliveOfExplode) {
            return;
        }
        if (step == diameter.length) {
            aliveOfExplode = false;
            step = 0;
            return;
        }
        
        // 根据爆炸直径以及被击中坦克的中心点画出爆炸效果
        Color c = g.getColor();
        g.setColor(Color.RED);
        g.fillOval(x + Tank.TANK_WIDTH/2 - diameter[step]/2, y + Tank.TANK_WIDTH/2 - diameter[step]/2,
            diameter[step], diameter[step]);
        g.setColor(c);
        
        /*
                  每画一次，下标递增1，为了取下一步的爆炸直径
        */
        step ++;
    }
    
    /**
    * @Title: isAliveOfExplode
    * @Description: (获取爆炸效果的存活状态)
    * @param @return    参数 
    * @return boolean    返回类型
    * @throws
     */
    public boolean getAliveOfExplode() {
        return aliveOfExplode;
    }
    
    /**
    * @Title: setAliveOfExplode
    * @Description: (设置爆炸效果的存活状态)
    * @param @param aliveOfExplode    参数 
    * @return void    返回类型
    * @throws
     */
    public void setAliveOfExplode(boolean aliveOfExplode) {
        this.aliveOfExplode = aliveOfExplode;
    }
}

