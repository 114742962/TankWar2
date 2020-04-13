/**
* @Title: HeroTank.java
* @Package com.guiyajun.tank2
* @Description: TODO(用一句话描述该文件做什么)
* @author Administrator
* @date 2020年4月8日
* @version V1.0
*/
package com.guiyajun.tank2;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

/**  
 * @ProjectName:  [TankWar2] 
 * @Package:      [com.guiyajun.tank2.HeroTank.java]  
 * @ClassName:    [HeroTank]   
 * @Description:  [主坦克类，坦克的子类]   
 * @Author:       [Guiyajun]   
 * @CreateDate:   [2020年4月8日 上午9:13:35]   
 * @UpdateUser:   [Guiyajun]   
 * @UpdateDate:   [2020年4月8日 上午9:13:35]   
 * @UpdateRemark: [说明本次修改内容]  
 * @Version:      [v1.0]
 */
public class HeroTank extends Tank {
    /** 主坦克炮弹的颜色 */
    public Color colorOfFriendlyMissile = Color.BLACK;
    /** 敌方主坦克炮弹的颜色 */
    public Color colorOfEnermyMissile = Color.RED;
    /** 是否第一次被画 */
    public boolean drawFirstTime = true;
    /** 渐变初始颜色 */
    public Color startColor;
    /** 渐变结束颜色 */
    public Color endColor;
    /** 记录左方向按键的状态，true表示按键被按压中，false表示按键被释放了 */
    private boolean beLeft = false;
    /** 记录右方向按键的状态，true表示按键被按压中，false表示按键被释放了 */
    private boolean beRight = false;
    /** 记录上方向按键的状态，true表示按键被按压中，false表示按键被释放了 */
    private boolean beUp = false; 
    /** 记录下方向按键的状态，true表示按键被按压中，false表示按键被释放了 */
    private boolean beDown = false;
    
    /**
    * @Description: 创建一个新的实例 HeroTank.
    * @param x
    * @param y
    */
    public HeroTank(int x, int y, boolean friendly, TankWarClient tankWarClient) {
        super(x, y, friendly, tankWarClient);
    }
    
    @Override
    /**
         * 重写父类方法，画出主坦克的方法
     */
    public void draw(Graphics g) {
        if(!this.getAliveOfTank()) {
            return;
        }
        
        if (drawFirstTime != false) {
            drawFirstTime = false;
        }

        // 定义坦克的格式
        Color c = g.getColor();
        Graphics2D graphics2D = (Graphics2D)g;
        GradientPaint gradientPaint = new GradientPaint(10, 10, startColor, 32, 
            32, endColor, true);
        graphics2D.setPaint(gradientPaint);
        graphics2D.fillOval(x, y, TANK_WIDTH, TANK_HEIGHT);
        
        tankMove();
        drawBarrel(g);
        drawBlood(g, this.getBloodOfTank());
        
        g.setColor(c);
    }
    
    /**
     * 
    * @Title: keyPressed
    * @Description: 检测到相应键按下时，修改对应键的状态
    * @param @param e    传入按键被按下时的事件
    * @return void    返回类型
    * @throws
     */
    public void KeyPress(KeyEvent e) {
        int keyCode = e.getKeyCode();
        
        switch (keyCode) {
            case KeyEvent.VK_UP:
                beUp = true;
                break;
            case KeyEvent.VK_LEFT:
                beLeft = true;
                break;
            case KeyEvent.VK_RIGHT:
                beRight = true;
                break;                    
            case KeyEvent.VK_DOWN:
                beDown = true;
                break;
            default:
                break;
        }
        
        getDirection();
    }
    
    /**
    * @Title: keyReleased
    * @Description: 检测到相应键释放时，修改对应键的状态或者响应按键的功能
    * @param @param e    传入按键被释放时的事件 
    * @return void    返回类型
    * @throws
     */
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch(keyCode) {
            // F2键我方主坦克复位恢复最初始状态
            case KeyEvent.VK_F2:
                myTankReset();
                break;
             // F键发射炮弹    
            case KeyEvent.VK_F:
                tankWarClient.missilesList.add(fire(colorOfFriendlyMissile, this.friendly, 
                    directionOfBarrel));
                break;
            case KeyEvent.VK_UP:
                beUp = false;
                break;
            case KeyEvent.VK_LEFT:
                beLeft = false;
                break;
            case KeyEvent.VK_RIGHT:
                beRight = false;
                break;                    
            case KeyEvent.VK_DOWN:
                beDown = false;
                break;
            // 超级发射一次往八个方向各发射一枚炮弹
            case KeyEvent.VK_A:
                superFire(colorOfFriendlyMissile);
                break;
            default:
                break;
        }
        
        getDirection();
    }

        /**
        * @Title: superFire
        * @Description: 超级发射方法，一次往八个方向各发射一枚炮弹
        * @param @param c    炮弹的颜色 
        * @return void    返回类型
        * @throws
        */
        public void superFire(Color c) {
         if (false == getAliveOfTank()) {
             return;
         }
         
         // 将枚举变量Direction中的值转化为Direction数组
         Direction[] dirs = Direction.values();
         
         // 每个方向发射一枚炮弹
         for(int i=0; i<dirs.length - 1; i++) {
             tankWarClient.missilesList.add(fire(colorOfFriendlyMissile, this.friendly, dirs[i]));
         }
     }
        
    @Override
    /**
     * 
    * @Title: getDirection
    * @Description: TODO(获取坦克的方向)
    * @param     参数 
    * @return void    返回类型
    * @throws
     */
    public void getDirection() {
        if(beUp && !beDown && !beLeft && !beRight) {directionOfTank = Direction.UP;}
        else if (!beUp && beDown && !beLeft && !beRight) {directionOfTank = Direction.DOWN;}
        else if (!beUp && !beDown && beLeft && !beRight) {directionOfTank = Direction.LEFT;}
        else if (!beUp && !beDown && !beLeft && beRight) {directionOfTank = Direction.RIGHT;}
        else if (beUp && !beDown && beLeft && !beRight) {directionOfTank = Direction.LEFT_UP;}
        else if (beUp && !beDown && !beLeft && beRight) {directionOfTank = Direction.RIGHT_UP;}
        else if (!beUp && beDown && beLeft && !beRight) {directionOfTank = Direction.LEFT_DOWN;}
        else if (!beUp && beDown && !beLeft && beRight) {directionOfTank = Direction.RIGHT_DOWN;}
        else {directionOfTank = Direction.STOP;}
    }
    
    /**
    * @Title: myTankReset
    * @Description: 初始化主坦克
    * @param      
    * @return void    返回类型
    * @throws
     */
    public void myTankReset() {
        tankWarClient.myTank = new HeroTank(700, 400, true, tankWarClient);
    }

    /**
    * @Title: getBloodOfTank
    * @Description: TODO(这里用一句话描述这个方法的作用)
    * @param @return    参数 
    * @return String    返回类型
    * @throws
    */
}
