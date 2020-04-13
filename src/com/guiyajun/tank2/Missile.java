/**
* @Title: Missile.java
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
 * @Package:      [com.guiyajun.tank2.Missile.java]  
 * @ClassName:    [Missile]   
 * @Description:  [定义炮弹的属性与方法]   
 * @Author:       [Guiyajun]   
 * @CreateDate:   [2020年4月7日 下午5:13:21]   
 * @UpdateUser:   [Guiyajun]   
 * @UpdateDate:   [2020年4月7日 下午5:13:21]   
 * @UpdateRemark: [说明本次修改内容]  
 * @Version:      [v1.0]
 */
public class Missile {
    /** 客户端实例变量用于管理元素  */
    public TankWarClient tankWarClient = null;
    /** 炮弹高度 */
    public static final int MISSILE_HEIGHT = 8;
    /** 炮弹宽度 */
    public static final int MISSILE_WIDTH = 8;
    /** 炮弹移动速度 */
    public static final int MISSILE_MOVE_STEP = 8;
    /** 炮弹的友好度 */
    private boolean friendly = true;
    /** 炮弹的X坐标 */
    private int x = 0;
    /** 炮弹的y坐标 */
    private int y = 0;
    /** 炮弹的默认颜色*/
    private Color colorOfMissile = Color.BLACK;
    /** 炮弹的存活状态*/
    private boolean aliveOfMissile = true;
    /** 炮弹的初始方向，类型为Tank类中定义的枚举类型Direction*/
    Direction directionOfMissile = Direction.STOP;
    
    /**
     * 
    * @Description: 创建一个新的实例 Missile.
    * @param x
    * @param y
    * @param directionOfMissile
    * @param tankWarClient
     */
    public Missile(int x, int y, Direction directionOfMissile, boolean friendly, 
            TankWarClient tankWarClient) {
        this.x = x;
        this.y = y;
        this.directionOfMissile = directionOfMissile;
        this.friendly = friendly;
        this.tankWarClient = tankWarClient;
    }
    
    /**
     * 
    * @Description: 创建一个新的实例 Missile.
    * @param x  x坐标
    * @param y  y坐标
    * @param dirOfBarrel 炮筒方向
    * @param tankWarClient 客户端实例
    * @param color 坦克颜色
     */
    Missile(int x, int y, Direction dirOfBarrel, boolean friendly, 
            TankWarClient tankWarClient, Color color) {
        this(x, y, dirOfBarrel, friendly, tankWarClient);
        this.colorOfMissile = color;
    }
    
    /**
     * 
    * @Title: draw
    * @Description: TODO(画出炮弹)
    * @param @param g    参数 
    * @return void    返回类型
    * @throws
     */
    public void draw(Graphics g) {
        if (false == this.getAliveOfMissile()) {
            return;
        }
        Color c = g.getColor();
        g.setColor(colorOfMissile);
        g.fillOval(x, y, MISSILE_WIDTH, MISSILE_HEIGHT);
        g.setColor(c);
        
        MissileMove();
    }

    /**
     * 
    * @Title: TankMove
    * @Description: 炮弹的移动方法
    * @param     参数 
    * @return void    返回类型
    * @throws
     */
    public void MissileMove() {
        switch (directionOfMissile) {
            case UP:
                y -= MISSILE_MOVE_STEP;
                break;
            case DOWN:
                y += MISSILE_MOVE_STEP;
                break;
            case LEFT:
                x -= MISSILE_MOVE_STEP;
                break;
            case RIGHT:
                x += MISSILE_MOVE_STEP;
                break;
            case LEFT_UP:
                x -= Math.round(MISSILE_MOVE_STEP * (Math.sqrt(2)/2));
                y -= Math.round(MISSILE_MOVE_STEP * (Math.sqrt(2)/2));
                break;
            case RIGHT_UP:
                x += Math.round(MISSILE_MOVE_STEP * (Math.sqrt(2)/2));
                y -= Math.round(MISSILE_MOVE_STEP * (Math.sqrt(2)/2));
                break;
            case LEFT_DOWN:
                x -= Math.round(MISSILE_MOVE_STEP * (Math.sqrt(2)/2));
                y += Math.round(MISSILE_MOVE_STEP * (Math.sqrt(2)/2));
                break;
            case RIGHT_DOWN:
                x += Math.round(MISSILE_MOVE_STEP * (Math.sqrt(2)/2));
                y += Math.round(MISSILE_MOVE_STEP * (Math.sqrt(2)/2));
                break;
            case STOP:
                break;
            default:
                break;
        }
        
        collisionDetection();
    }
    
    /**
    * @Title: hitTank
    * @Description: 判断炮弹是否击中坦克，根据判断修改炮弹和坦克的生存状态
    * @param @param tank 传入一个tank对象
    * @param @return
    * @return boolean    返回类型
    * @throws
     */
    boolean hitTank(Tank tank) {
        boolean hit = false;
        if (this.friendly != tank.friendly && this.aliveOfMissile && this.getRectangleOfMissile()
            .intersects(tank.getRectangleOfTank()) && tank.getAliveOfTank()) {
            hit = true;
            this.setAliveOfMissile(false);
            
            // 主坦克被击中一次掉一点血
            if(tank instanceof HeroTank) {
                tank.reduceBloodOfTank(1);
            } else {
                tank.reduceBloodOfTank(1);
            }
            
            // 如果坦克没血了将坦克设置为被摧毁状态
            if (tank.getBloodOfTank() <= 0) {
                tank.setAliveOfTank(false);
            }
            
            // 坦克被击中时产生爆炸效果并将爆炸效果加入到客户端实例的爆炸集合中
            tankWarClient.explodesList.add(new Explode(tank.x, tank.y, tankWarClient));
        }
        
        return hit;
    }
    
    /**
    * @Title: collisionDetection
    * @Description: 子弹碰撞检测
    * @param     参数 
    * @return void    返回类型
    * @throws
     */
    public void collisionDetection() {
        if (tankWarClient != null) {
            // 子弹与围墙碰撞检测
            if (x < 0 || y < TankWarClient.SCORE_AREA_HEIGHT || x > TankWarClient.GAME_WIDTH 
                || y > TankWarClient.GAME_HEIGHT) {
            
                aliveOfMissile = false;
                tankWarClient.missilesList.remove(this);
            }

            // 子弹与内墙碰撞检测
            if (this.getRectangleOfMissile().intersects(tankWarClient.wallLeft.getRectangleOfWall()) 
                || this.getRectangleOfMissile().intersects(tankWarClient.wallRight.getRectangleOfWall())) {
                aliveOfMissile = false;
                tankWarClient.missilesList.remove(this);
            }
            
            // 子弹与坦克碰撞检测
            if (this.getAliveOfMissile() != false) {
                //  子弹与机器人坦克的碰撞
                for (int j = 0; j < tankWarClient.viTanksList.size(); j++) {
                    Tank tank = tankWarClient.viTanksList.get(j);
                    
                    if (this.friendly != tank.friendly && this.getRectangleOfMissile()
                            .intersects(tank.getRectangleOfTank())) {
                        tankWarClient.missilesList.remove(this);
                        hitTank(tank);
                    }
                }
                
                // 子弹与主坦克的碰撞
                if (this.friendly != tankWarClient.myTank.friendly && this.getRectangleOfMissile()
                    .intersects(tankWarClient.myTank.getRectangleOfTank())) {
                    hitTank(tankWarClient.myTank);
                }
            }
        }
    }
    
    /**
     * 
    * @Title: getX
    * @Description: 获取炮弹的x坐标
    * @param @return    参数 
    * @return int    返回类型
    * @throws
     */
    public int getX() {
        return x;
    }
    
    /**
     * 
    * @Title: setX
    * @Description: 设置炮弹的x坐标
    * @param @return    参数 
    * @return void    返回类型
    * @throws
     */
    public void setX(int x) {
        this.x = x;
    }
    
    /**
     * 
    * @Title: setY
    * @Description: 获取炮弹的y坐标
    * @param @return    参数 
    * @return int    返回类型
    * @throws
     */
    public int getY() {
        return y;
    }
    
    /**
     * 
    * @Title: setY
    * @Description: 设置炮弹的y坐标
    * @param @return    参数 
    * @return void    返回类型
    * @throws
     */
    public void setY(int y) {
        this.y = y;
    }
    
    /**
     * 
    * @Title: getColorOfMissile
    * @Description: 获取炮弹的颜色
    * @param @return    参数 
    * @return Color    返回类型
    * @throws
     */
    public Color getColorOfMissile() {
        return colorOfMissile;
    }

    /**
     * 
    * @Title: setColorOfMissile
    * @Description: 设置炮弹的颜色
    * @param @param colorOfMissile    参数 
    * @return void    返回类型
    * @throws
     */
    public void setColorOfMissile(Color colorOfMissile) {
        this.colorOfMissile = colorOfMissile;
    }
    
    /**
    * @Title: getAliveOfMissile
    * @Description: 获取炮弹的生存状态
    * @param @return   
    * @return boolean    返回类型
    * @throws
     */
    public boolean getAliveOfMissile() {
        return aliveOfMissile;
    }
    
    /**
    * @Title: setAliveOfMissile
    * @Description: 设置炮弹的生存状态
    * @param @param live    布尔型，true代表正常，false代表摧毁 
    * @return void    返回类型
    * @throws
     */
    public void setAliveOfMissile(boolean aliveOfMissile) {
        this.aliveOfMissile = aliveOfMissile;
    }
    
    public Rectangle getRectangleOfMissile() {
        return new Rectangle(x, y, MISSILE_HEIGHT, MISSILE_WIDTH);
    }
    
    public boolean isFriendly() {
        return friendly;
    }

    public void setFriendly(boolean friendly) {
        this.friendly = friendly;
    }

}
