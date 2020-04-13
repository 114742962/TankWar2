/**
* @Title: Direction.java
* @Package com.guiyajun.tank2
* @Description: TODO(用一句话描述该文件做什么)
* @author Administrator
* @date 2020年4月7日
* @version V1.0
*/
package com.guiyajun.tank2;

 /**  
 * @ProjectName:  [TankWar2] 
 * @Package:      [com.guiyajun.tank2.Direction.java]  
 * @ClassName:    [Direction]   
 * @Description:  [定义了游戏中坦克的方向元素]   
 * @Author:       [Guiyajun]   
 * @CreateDate:   [2020年4月7日 下午4:09:45]   
 * @UpdateUser:   [Guiyajun]   
 * @UpdateDate:   [2020年4月7日 下午4:09:45]   
 * @UpdateRemark: [说明本次修改内容]  
 * @Version:      [v1.0]
 */
public enum Direction {
    /** 上 */
    UP,
    /** 下 */
    DOWN,
    /** 左 */
    LEFT,
    /** 右 */
    RIGHT,
    /** 左上 */
    LEFT_UP,
    /** 左下 */
    LEFT_DOWN,
    /** 右上 */
    RIGHT_UP,
    /** 右下 */
    RIGHT_DOWN,
    /** 停止 */
    STOP;
}
