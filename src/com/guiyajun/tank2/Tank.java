/**
* @Title: Tank.java
* @Package com.guiyajun.tank2
* @Description: TODO(用一句话描述该文件做什么)
* @author Administrator
* @date 2020年4月7日
* @version V1.0
*/
package com.guiyajun.tank2;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**  
 * @ProjectName:  [TankWar2] 
 * @Package:      [com.guiyajun.tank2.Tank.java]  
 * @ClassName:    [Tank]   
 * @Description:  [坦克的父类，定义坦克的共有属性]   
 * @Author:       [Guiyajun]   
 * @CreateDate:   [2020年4月7日 下午3:53:48]   
 * @UpdateUser:   [Guiyajun]   
 * @UpdateDate:   [2020年4月7日 下午3:53:48]   
 * @UpdateRemark: [说明本次修改内容]  
 * @Version:      [v1.0]
 */
public class Tank {
    /** 客户端实例，管理坦克元素 */
    public TankWarClient tankWarClient = null;
    /** 设置坦克的高度,单位是像素 */
    public final static int TANK_HEIGHT = 32;
    /** 设置坦克的宽度,单位是像素 */
    public final static int TANK_WIDTH = 32;
    /** 坦克的移动速度 */
    public final int TANK_MOVE_STEP = 4;
    /** 坦克的x坐标 */
    public int x = 0;
    /** 坦克的y坐标 */
    public int y = 0;
    /** 坦克的颜色 */
    public Color colorOfTank = Color.DARK_GRAY;
    /** 坦克的编号 */
    public int id = 100;
    /** 坦克的初始移动方向 */
    protected Direction directionOfTank = Direction.STOP;
    /** 炮筒的初始方向 */
    protected Direction directionOfBarrel = Direction.UP;
    /** 相对于本方坦克的友好度 */
    protected boolean friendly = true;
    /** 坦克的存活状态 */
    private boolean aliveOfTank = true;
    /** 坦克上一次X坐标，用于坦克越界后复位 */
    protected int xOld;
    /** 坦克上一次Y坐标，用于坦克越界后复位 */
    protected int yOld;
    /** 坦克的血量 */
    private int bloodOfTank = 3;
    
    /**
    * @Description: 创建一个新的实例 Tank.
    * @param x  x坐标
    * @param y  y坐标
    * @param friendly 相对于主坦克的友好性
     */
    public Tank(int x, int y, boolean friendly) {
        this.x = x;
        this.y = y;
        this.xOld = x;
        this.yOld = y;
        this.friendly = friendly;
    }
    
    /**
     * 
    * @Description: 创建一个新的实例 Tank.
    * @param x
    * @param y
    * @param friendly
    * @param tankWarClient
     */
    public Tank(int x, int y, boolean friendly, TankWarClient tankWarClient) {
        this(x, y, friendly);
        this.tankWarClient = tankWarClient;
    }
    
    /**
     * 
    * @Description: 创建一个新的实例 Tank.
    * @param x
    * @param y
    * @param colorOfTank
    * @param friendly
    * @param tankWarClient
     */
    public Tank(int x, int y, Color colorOfTank, boolean friendly, TankWarClient tankWarClient) {
        this(x, y, friendly);
        this.colorOfTank = colorOfTank;
        this.tankWarClient = tankWarClient;
    }
    
    /**
     * 
    * @Title: tankMove
    * @Description: TODO(坦克的移动方法)
    * @param     参数 
    * @return void    返回类型
    * @throws
     */
    public void tankMove() {
        this.xOld = x;
        this.yOld = y;
        
        switch (directionOfTank) {
            case UP:
                y -= TANK_MOVE_STEP;
                break;
            case DOWN:
                y += TANK_MOVE_STEP;
                break;
            case LEFT:
                x -= TANK_MOVE_STEP;
                break;
            case RIGHT:
                x += TANK_MOVE_STEP;
                break;
            case LEFT_UP:
                x -= Math.round(TANK_MOVE_STEP * (Math.sqrt(2)/2));
                y -= Math.round(TANK_MOVE_STEP * (Math.sqrt(2)/2));
                break;
            case RIGHT_UP:
                x += Math.round(TANK_MOVE_STEP * (Math.sqrt(2)/2));
                y -= Math.round(TANK_MOVE_STEP * (Math.sqrt(2)/2));
                break;
            case LEFT_DOWN:
                x -= Math.round(TANK_MOVE_STEP * (Math.sqrt(2)/2));
                y += Math.round(TANK_MOVE_STEP * (Math.sqrt(2)/2));
                break;
            case RIGHT_DOWN:
                x += Math.round(TANK_MOVE_STEP * (Math.sqrt(2)/2));
                y += Math.round(TANK_MOVE_STEP * (Math.sqrt(2)/2));
                break;
            case STOP:
                break;
            default:
                break;
        }
        
        collisionDetection();
    }
    
    /**
     * @Title: drawBlood
     * @Description: 根据坦克的血量及方向画出坦克的血量值，血量值永远在炮筒后方显示
     * @param @param g   画笔
     * @param @param bloodOfTank    血量值 
     * @return void    返回类型
     * @throws
     */
    public void drawBlood(Graphics g, int bloodOfTank) {
        // 根据炮筒方向画出血量值
        getBarrelDirection();
        
        Color c =  g.getColor();
        g.setColor(Color.RED);
        
        switch(directionOfBarrel) {   
        case UP:
            g.drawString(" " + bloodOfTank, x + 10, y + 27);
            break;
        case DOWN:
            g.drawString(" " + bloodOfTank, x + 10, y + 12);
            break;
        case LEFT:
            g.drawString(" " + bloodOfTank, x + 18, y + 20);
            break;
        case RIGHT:
            g.drawString(" " + bloodOfTank, x + 5, y + 20);
            break;
        case LEFT_UP:
            g.drawString(" " + bloodOfTank, x + 15, y + 25);
            break;
        case RIGHT_UP:
            g.drawString(" " + bloodOfTank, x + 5, y + 25);
            break;
        case LEFT_DOWN:
            g.drawString(" " + bloodOfTank, x + 15, y + 15);
            break;
        case RIGHT_DOWN:
            g.drawString(" " + bloodOfTank, x + 5, y + 18);
            break;
        default:
            break;
        }
        
        g.setColor(c);
    }
    
    /**
    * @Title: drawBarrel
    * @Description: 根据坦克的方向画出炮筒
    * @param @param g    画笔 
    * @return void    返回类型
    * @throws
     */
    public void drawBarrel(Graphics g) {  
        getBarrelDirection();
        
        Color c = g.getColor();
        g.setColor(Color.BLACK);
        
        switch(directionOfBarrel) {   
            case UP:
                g.drawLine(x + TANK_WIDTH/2, y + TANK_HEIGHT/2, x + TANK_WIDTH/2, y - TANK_HEIGHT * 1 / 4);
                break;
            case DOWN:
                g.drawLine(x + TANK_WIDTH/2, y + TANK_HEIGHT/2, x + TANK_WIDTH/2, y + TANK_HEIGHT * 5 / 4);
                break;
            case LEFT:
                g.drawLine(x + TANK_WIDTH/2, y + TANK_HEIGHT/2, x - TANK_WIDTH * 1 / 4, y + TANK_HEIGHT/2);
                break;
            case RIGHT:
                g.drawLine(x + TANK_WIDTH/2, y + TANK_HEIGHT/2, x + TANK_WIDTH * 5 / 4, y + TANK_HEIGHT/2);
                break;
            case LEFT_UP:
                g.drawLine(x + TANK_WIDTH/2, y + TANK_HEIGHT/2, x, y);
                break;
            case RIGHT_UP:
                g.drawLine(x + TANK_WIDTH/2, y + TANK_HEIGHT/2,x + TANK_WIDTH, y);
                break;
            case LEFT_DOWN:
                g.drawLine(x + TANK_WIDTH/2, y + TANK_HEIGHT/2, x, y + TANK_HEIGHT);
                break;
            case RIGHT_DOWN:
                g.drawLine(x + TANK_WIDTH/2, y + TANK_HEIGHT/2, x + TANK_WIDTH, y + TANK_HEIGHT);
                break;
            default:
                break;
        }
        
        g.setColor(c);
    }
    
    /**
    * @Title: getDirection
    * @Description: 获取坦克的移动方向，这里未实现，每个子类需要复写自己的方向获取方法
    * @param      
    * @return void    返回类型
    * @throws
     */
    void getDirection() {}
    
    /**
    * @Title: getBarrelDirection
    * @Description: 根据坦克的移动方向改变炮筒的方向
    * @param     
    * @return void    返回类型
    * @throws
     */
    void getBarrelDirection() {
        if (directionOfTank != Direction.STOP) {
            directionOfBarrel = directionOfTank;
        }
    }
    
    /**
    * @Title: fire
    * @Description: 坦克发射炮弹方法
    * @param @param color 炮弹的颜色
    * @param @param dirOfBarrel 炮筒的方向
    * @return Missile    返回类型
    * @throws
     */
    public Missile fire(Color colorOfMissile, boolean friendly, Direction dirOfBarrel) {
        if (!getAliveOfTank()) {
            return null;
        }
        
        Missile missileOfMyTank = null;
        
        // 根据炮筒的方向和炮筒尾部的位置打出炮弹
        switch(dirOfBarrel) {   
            case UP:
                missileOfMyTank = new Missile(x + TANK_WIDTH / 2 - Missile.MISSILE_WIDTH / 2, 
                    y - TANK_HEIGHT * 1 / 4 - Missile.MISSILE_HEIGHT / 2, dirOfBarrel, friendly,
                    this.tankWarClient, colorOfMissile);
                break;
            case DOWN:
                missileOfMyTank = new Missile(x + TANK_WIDTH/2 - Missile.MISSILE_WIDTH/2, 
                    y + TANK_HEIGHT * 5 / 4 - Missile.MISSILE_HEIGHT/2, dirOfBarrel, friendly,
                    this.tankWarClient, colorOfMissile);
                break;
            case LEFT:
                missileOfMyTank = new Missile(x - TANK_WIDTH * 1 / 4 - Missile.MISSILE_WIDTH/2, 
                    y + TANK_HEIGHT/2 - Missile.MISSILE_HEIGHT/2, dirOfBarrel, friendly,
                    this.tankWarClient, colorOfMissile);
                break;
            case RIGHT:
                missileOfMyTank = new Missile(x + TANK_WIDTH * 5 / 4 - Missile.MISSILE_WIDTH/2, 
                    y + TANK_HEIGHT/2 - Missile.MISSILE_HEIGHT/2, dirOfBarrel, friendly,
                    this.tankWarClient, colorOfMissile);
                break;
            case LEFT_UP:
                missileOfMyTank = new Missile(x, y, dirOfBarrel, friendly, this.tankWarClient, 
                    colorOfMissile);
                break;
            case RIGHT_UP:
                missileOfMyTank = new Missile(x + TANK_WIDTH, y - Missile.MISSILE_HEIGHT, dirOfBarrel, 
                    friendly, this.tankWarClient, colorOfMissile);
                break;
            case LEFT_DOWN:
                missileOfMyTank = new Missile(x - Missile.MISSILE_WIDTH / 2, y + TANK_HEIGHT - Missile
                    .MISSILE_HEIGHT/2,dirOfBarrel, friendly, this.tankWarClient, colorOfMissile);
                break;
            case RIGHT_DOWN:
                missileOfMyTank = new Missile(x + TANK_WIDTH, y + TANK_HEIGHT, dirOfBarrel, friendly,
                    this.tankWarClient, colorOfMissile);
                break;
            default:
                break;
        }
        
        return missileOfMyTank;
    }
    
    /**
     * @Title: collisionDetection
     * @Description: 坦克的碰撞检测
     * @param      
     * @return void    返回类型
     * @throws
      */
     public void collisionDetection() {
         if (tankWarClient != null) {
             // 坦克与围墙碰撞检测
             if (x < 0) {
                 stay();
             }
             else if (y < TankWarClient.SCORE_AREA_HEIGHT) {
                 stay();
             }
             else if (x > TankWarClient.GAME_WIDTH - Tank.TANK_WIDTH) {
                 stay();
             }
             else if (y > TankWarClient.GAME_HEIGHT - Tank.TANK_HEIGHT) {
                 stay();
             }
             
             // 坦克与内墙碰撞检测
             if (this.getRectangleOfTank().intersects(tankWarClient.wallLeft.getRectangleOfWall()) 
                 || this.getRectangleOfTank().intersects(tankWarClient.wallRight.getRectangleOfWall())) {
                 this.stay();
             }
             
             // 坦克与坦克的碰撞
             if (this.getAliveOfTank() != false) {
                 // 与机器人坦克的碰撞
                 for (int i=0; i<tankWarClient.viTanksList.size(); i++) {
                     Tank tank = tankWarClient.viTanksList.get(i);
                     
                     if (this.equals(tank) != true && this.getRectangleOfTank().intersects(tank
                         .getRectangleOfTank())) {
                         this.stay();
                     }
                 }
                 
                 // 与主坦克碰撞
                 if (this.equals(tankWarClient.myTank) != true && this.getRectangleOfTank()
                     .intersects(tankWarClient.myTank.getRectangleOfTank())) {
                     this.stay();
                 }
             }
             
             
             for (int i=0; i<tankWarClient.viTanksList.size(); i++) {
                 Tank tank = tankWarClient.viTanksList.get(i);
                     
                 if (this.getAliveOfTank() && tank.getAliveOfTank() && !this.equals(tank) && !this.equals(tankWarClient.myTank)) {
                     if (tankWarClient.myTank.getRectangleOfTank().intersects(this.getRectangleOfTank())
                         || tank.getRectangleOfTank().intersects(this.getRectangleOfTank())) {
                         this.stay();
                     }
                 } else if (this.getAliveOfTank() && tank.getAliveOfTank() && this.equals(tankWarClient.myTank)) {
                     if (tank.getRectangleOfTank().intersects(tankWarClient.myTank.getRectangleOfTank())) {
                         tankWarClient.myTank.stay();
                     }
                 }
             }
             
//             // 我方坦克与玩家坦克的碰撞检测
//             for (int i=0; i<tankWarClient.tanks.size(); i++) {
//                 Tank tank = tankWarClient.tanks.get(i);
//                 
//                 if (this.getAliveOfTank() && tank.getAliveOfTank() && !this.equals(tank) && !this.equals(tankWarClient.myTank)) {
//                     if (tankWarClient.myTank.getRectangleOfTank().intersects(this.getRectangleOfTank())
//                             || tank.getRectangleOfTank().intersects(this.getRectangleOfTank())) {
//                         this.stay();
//                     }
//                 } else if (this.getAliveOfTank() && tank.getAliveOfTank() && this.equals(tankWarClient.myTank)) {
//                     if (tank.getRectangleOfTank().intersects(tankWarClient.myTank.getRectangleOfTank())) {
//                         tankWarClient.myTank.stay();
//                     }
//                 }
//             }
             
//             // 坦克与加血包碰撞检测
//             for(int i=0; i<tankWarClient.bloods.size(); i++) {
//                 for(int j=0; j<tankWarClient.enemyTanks.size(); j++) {
//                     Blood blood = tankWarClient.bloods.get(i);
//                     Tank tank = tankWarClient.enemyTanks.get(j);
//                     if (blood.getAlive() && tank.getAliveOfTank() && blood.getRectOfBlood()
//                         .intersects(tank.getRectangleOfTank())) {
//                         blood.setAlive(false);
//                         tank.setBloodOfTank(4);
//                     } else if (blood.getAlive() && blood.getRectOfBlood().intersects(tankWarClient.myTank
//                         .getRectangleOfTank())) {
//                         blood.setAlive(false);
//                         tankWarClient.myTank.setBloodOfTank(4);
//                     }
//                 }
//             }
         }
     }
    
    /**
    * @Title: stay
    * @Description: 记录坦克上一次移动后的坐标信息，用于坦克越界复位
    * @return void    返回类型
    * @throws
     */
    public void stay() {
        x = xOld;
        y = yOld;
    }
    
    /**
     * 
     * @Title: Draw
     * @Description: 在游戏客户端主界面画出单色坦克的方法，由子类自己实现
     * @param @param g    参数 
     * @return void    返回类型
     * @throws
     */
    public void draw(Graphics g) {
        
    }
    
    public boolean getAliveOfTank() {
        return aliveOfTank;
    }

    public void setAliveOfTank(boolean aliveOfTank) {
        this.aliveOfTank = aliveOfTank;
    }
    
    public int getBloodOfTank() {
        return bloodOfTank;
    }

    public void setBloodOfTank(int bloodOfTank) {
        this.bloodOfTank = bloodOfTank;
    }
    
    public Rectangle getRectangleOfTank() {
        return new Rectangle(x, y, TANK_WIDTH, TANK_HEIGHT);
    }
    
    /**
    * @Title: reduceBloodOfTank
    * @Description: 减少坦克的血量值，可以直接定义减少的血量数
    * @param @param numOfBlood    血量数
    * @return void    返回类型
    * @throws
     */
    public void reduceBloodOfTank(int numOfBlood) {
        this.bloodOfTank -= numOfBlood;
    }
}
