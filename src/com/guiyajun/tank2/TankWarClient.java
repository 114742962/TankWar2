package com.guiyajun.tank2;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JRadioButton;

/**
* @Title: TankWarClient.java
* @Package 
* @Description: TODO(用一句话描述该文件做什么)
* @author Administrator
* @date 2020年4月1日
* @version V1.0
*/

/**
 *     * @ProjectName:  [TankWar2]   * @Package:      [.TankWarClient.java]  
 *  * @ClassName:    [TankWarClient]     * @Description:  [坦克大战的客户端]   
 *  * @Author:       [Guiyajun]     * @CreateDate:   [2020年4月1日 上午10:47:30]   
 *  * @UpdateUser:   [Guiyajun]     * @UpdateDate:   [2020年4月1日 上午10:47:30]   
 *  * @UpdateRemark: [说明本次修改内容]    * @Version:      [v1.0]
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
    /** 装玩家坦克的容器 */
    public ArrayList<HeroTank> HeroTanksList = new ArrayList<>();
    /** 装机器人坦克的容器 */
    public ArrayList<VITank> viTanksList = new ArrayList<>();
    /** 装爆炸效果的容器 */
    public ArrayList<Explode> explodesList = new ArrayList<Explode>();
    /** 装炮弹的容器 */
    public ArrayList<Missile> missilesList = new ArrayList<>();
    /** 主坦克 */
    public HeroTank myTank = null;
    /** 在界面左边实例化一个钢化墙壁 */
    public Wall wallLeft = new Wall(180, 150, 20, 350, this);
    /** 在界面右边实例化一个钢化墙壁 */
    public Wall wallRight = new Wall(600, 150, 20, 350, this);
    /** 我方机器人坦克炮弹的颜色 */
    public Color colorOfFriendlyMissile = Color.BLACK;
    /** 敌方机器人坦克炮弹的颜色 */
    public Color colorOfEnermyMissile = Color.RED;
    /** 英雄坦克的颜色 */
    public Color colorOfHeroTank = Color.GREEN;
    /** 我方机器人坦克的颜色 */
    public Color colorOfFriendlyVITank = Color.GREEN;
    /** 敌方机器人坦克的颜色 */
    public Color colorOfEnermyVITank = Color.LIGHT_GRAY;
    /** 渐变初始颜色 */
    public Color startColor;
    /** 渐变结束颜色 */
    public Color endColor;
    /** 定义一个虚拟屏幕，目的是双缓冲，先把图片画到虚拟屏幕上 */
    Image backScreen = null;
    /** 单机模式 */
    public boolean standAloneMode = true;
    /** friendlyVITanksNumber */
    protected int friendlyVITanksNumber = 10;
    /** enermyVITanksNumber */
    protected int enermyVITanksNumber = 10;
    /** 网络客户端 */
    protected NetClient netClient = null;
    /** 网络服务端 */
    protected NetServer netServer = null;
    /** 网络服务端线程 */
    protected ServerThread serverThread = null;
    /** TRUE为网络版，false为单机版 */
    protected boolean mode = false;

    public static void main(String[] args) {
        TankWarClient twc = new TankWarClient();
        twc.launchFrame();
    }

    /**
     * 
     * @Title: launchFrame @Description: TODO(加载主界面) @param 参数 @return void
     *         返回类型 @throws
     */
    public void launchFrame() {
        this.setTitle("坦克大战2");
        this.setBounds(100, 100, GAME_WIDTH, GAME_HEIGHT);
        this.setLayout(null);
        this.setResizable(false);
        this.setBackground(Color.WHITE);
        this.setVisible(true);

        ConnectJDialog connectJDialog = new ConnectJDialog();
        connectJDialog.setVisible(true);

        // 在界面左边实例化一个钢化墙壁
        wallLeft = new Wall(180, 150, 20, 350, this);
        // 在界面右边实例化一个钢化墙壁
        wallRight = new Wall(600, 150, 20, 350, this);
        // 判断是否为单机版
        if (false == mode) {
            // 如果是单机版则创造主坦克
            myTank = new HeroTank(600, 500, true, this);
            
            // 如果是单机版则创造出友方机器人坦克，数量由自己指定
            for (int i = 0; i < friendlyVITanksNumber; i++) {
                viTanksList.add(new VITank(700, 80 + i * 50, colorOfFriendlyVITank, true, this));
            }
            // 如果是单机版则创造出敌方机器人坦克，数量由自己指定
            for (int i = 0; i < enermyVITanksNumber; i++) {
                viTanksList.add(new VITank(100, 80 + i * 50, colorOfEnermyVITank, false, this));
            }
        }

        // 加入右上角关闭客户端按钮监听
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Message message = null;
                
                if (myTank != null) {
                    message = new TankExitMessage(myTank);
                    myTank.setAliveOfTank(false);
                }
                
                setVisible(false);
                
                if (netClient != null && message != null) {
                    netClient.sendMessage(message);
                }
                
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                } finally {
                    System.exit(-1);
                }
            }
        });

        // 加入键盘监听事件，实现坦克移动
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                myTank.KeyPress(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                myTank.keyReleased(e);
            }
        });

        // 启动画面刷新线程池
        ThreadPoolService.getInstance().execute(new Refresh());
    }

    @Override
    /**
     * 画出界面上的元素
     */
    public void paint(Graphics g) {
        // 画出主坦克
        if (myTank != null && true == myTank.getAliveOfTank()) {
            // 判断主坦克是否第一次被画
            if (myTank.drawFirstTime != false) {
                initColor();
                myTank.startColor = this.startColor;
                myTank.endColor = this.endColor;
            }
            myTank.draw(g);
        }

        // 画出所有活着的英雄坦克
        for (int i = 0; i < HeroTanksList.size(); i++) {
            HeroTank heroTank = HeroTanksList.get(i);
            if (heroTank.getAliveOfTank()) {
                if (heroTank.drawFirstTime != false) {
                    initColor();
                    heroTank.startColor = startColor;
                    heroTank.endColor = endColor;
                }
                // 画出英雄坦克
                heroTank.draw(g);
            } else {
                HeroTanksList.remove(i);
            }
        }

        // 画出所有活着的机器人坦克
        for (int i = 0; i < viTanksList.size(); i++) {
            Tank tank = viTanksList.get(i);
            if (tank.getAliveOfTank()) {
                tank.draw(g);
            } else {
                viTanksList.remove(tank);
            }
        }

        // 画出所有活着的坦克炮弹
        for (int i = 0; i < missilesList.size(); i++) {
            Missile friendMissile = missilesList.get(i);
            if (friendMissile.getAliveOfMissile()) {
                friendMissile.draw(g);
            }
        }

        // 画出所有活着的爆炸效果
        for (int i = 0; i < explodesList.size(); i++) {
            Explode explode = explodesList.get(i);
            if (explode.getAliveOfExplode() != false) {
                explode.draw(g);
            }
        }

        // 判断墙体是否第一次被画
        if (wallLeft.drawFirstTime != false) {
            initColor();
            wallLeft.startColor = this.startColor;
            wallRight.startColor = this.startColor;
            wallLeft.endColor = this.endColor;
            wallRight.endColor = this.endColor;
        }
        // 画出左墙
        wallLeft.draw(g);
        // 画出右墙
        wallRight.draw(g);

    }

    @Override
    public void update(Graphics g) {
        // 在一个虚拟屏幕上画图，画完后再显示到真实屏幕上，利用双缓冲解决界面闪烁问题
        if (null == backScreen) {
            backScreen = this.createImage(GAME_WIDTH, GAME_HEIGHT);
        }
        // 获取虚拟屏幕的画笔
        Graphics gOfBackScreen = backScreen.getGraphics();
        // 获取画笔的初始颜色
        Color c = gOfBackScreen.getColor();
        // 设置画笔颜色用来画出积分区
        gOfBackScreen.setColor(Color.cyan);
        // 画出记分区
        gOfBackScreen.fillRect(0, 0, GAME_WIDTH, SCORE_AREA_HEIGHT);
        // 设置画笔的颜色为粉色
        gOfBackScreen.setColor(Color.PINK);
        // 画出游戏区
        gOfBackScreen.fillRect(0, SCORE_AREA_HEIGHT, GAME_WIDTH, GAME_HEIGHT - SCORE_AREA_HEIGHT);
        // 设置画笔的颜色为黑色用来记分
        gOfBackScreen.setColor(Color.BLACK);
        // 记分区画出我方坦克血量值
        if (myTank != null) {
            gOfBackScreen.drawString("BloodOfMyTank: " + myTank.getBloodOfTank(), 20, 50);
        }
        // 记分区画出敌方坦克数量
        gOfBackScreen.drawString("EnemyTanks: " + viTanksList.size(), 150, 50);
        // 记分区画出屏幕中出现的我方炮弹数量
        gOfBackScreen.drawString("Missiles: " + missilesList.size(), 260, 50);
        // 记分区画出爆炸事件数量
        gOfBackScreen.drawString("Explode: " + explodesList.size(), 340, 50);
        // 记分区画出屏幕中出现的敌方炮弹数量
        gOfBackScreen.drawString("MissilesOfEnemy: " + missilesList.size(), 420, 50);
        // 使用虚拟屏画笔画出屏幕上的元素
        paint(gOfBackScreen);
        // 还原虚拟屏画笔的初始颜色
        gOfBackScreen.setColor(c);
        // 使用真实屏幕画笔将虚拟屏画出
        g.drawImage(backScreen, 0, 0, null);
    }

    private void initColor() {
        int red = (int) (Math.random() * 255);
        int green = (int) (Math.random() * 255);
        int blue = (int) (Math.random() * 255);
        startColor = new Color(red, green, blue);
        red = (int) (Math.random() * 255);
        green = (int) (Math.random() * 255);
        blue = (int) (Math.random() * 255);
        endColor = new Color(red, green, blue);
    }

    private class Refresh implements Runnable {

        @Override
        public void run() {
            while (true) {
                repaint();
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class ConnectJDialog extends JDialog {
        /**
         * @Fields field:field:{todo}(用一句话描述这个变量表示什么)
         */
        private static final long serialVersionUID = 5558568220654573792L;
        ButtonGroup bg = new ButtonGroup();
        JButton button = new JButton("确定");
        JRadioButton radioButtonServer = new JRadioButton("server");
        JRadioButton radioButtonClient = new JRadioButton("client", true);
        TextField serverIP = new TextField(PropertiesManager.getPerproty("serverIP"), 15);
        TextField serverTCPPort = new TextField(PropertiesManager.getPerproty("serverTCPPort"), 5);
        TextField serverUDPPort = new TextField(PropertiesManager.getPerproty("serverUDPPort"), 5);
        TextField clientUDPPort = new TextField(PropertiesManager.getPerproty("clientUDPPort"), 5);

        ConnectJDialog() {
            super(TankWarClient.this, "Setting");
            this.setSize(190, 230);
            this.setLocation(400, 300);
            this.setResizable(false);
            this.setModal(true);
            this.setLayout(new FlowLayout());
            bg.add(radioButtonServer);
            bg.add(radioButtonClient);
            this.add(radioButtonServer);
            this.add(radioButtonClient);
            this.add(new Label("ServerIP:"));
            this.add(serverIP);
            this.add(new Label("serverTCPPort:"));
            this.add(serverTCPPort);
            this.add(new Label("serverUDPPort:"));
            this.add(serverUDPPort);
            this.add(new Label("clientUDPPort:"));
            this.add(clientUDPPort);
            this.add(button);

            this.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    
                    setVisible(false);
                }
            });

            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    mode = true;
                    setVisible(false);

                    // 创建一个服务端实例
                    netServer = new NetServer();
                    String ip = serverIP.getText();
                    int TCPServer = Integer.parseInt(serverTCPPort.getText().trim());
                    int UDPServer = Integer.parseInt(serverUDPPort.getText().trim());
                    int UDPClient = Integer.parseInt(clientUDPPort.getText().trim());
                    NetServer.serverTCPPort = TCPServer;
                    NetServer.serverUDPPort = UDPServer;
                    netClient = new NetClient(TankWarClient.this);
                    NetClient.serverIP = ip;
                    NetClient.clientUDPPort = UDPClient;
                    if (radioButtonServer.isSelected() && serverThread == null) {
                        serverThread = new ServerThread();
                        ThreadPoolService.getInstance().execute(serverThread);
                    }
                    // 客户端连接服务端
                    netClient.connect();
                    Message message = new TankNewMessage(myTank);
                    netClient.sendMessage(message);
                }
            });
        }
    }

    private class ServerThread implements Runnable {

        public void run() {
            if (netServer != null) {
                netServer.start();
            }
        }
    }
}
