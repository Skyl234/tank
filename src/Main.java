import OtherClass.BoomEffect;
import OtherClass.Bullet;
import OtherClass.Recorder;
import tankSpecies.cfj;
import tankSpecies.enemy1;
import tankSpecies.tank;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.Vector;

//坦克大战v1.0
//@author Mr.Long

@SuppressWarnings({"all"})

public class Main extends JFrame{//一个窗口，先实现游戏起始页面

    private MyPanel mp=null;

    static Scanner scanner=new Scanner(System.in);
    public static void main(String[] args) {

        new Main();
        
    }
     public Main() {
        //1开启新游戏，2继续上局游戏
         String option=scanner.next();
         mp = new MyPanel(option);
         new Thread(mp).start();
         this.add(mp);//将面板加入窗口
         this.addKeyListener(mp);//接入事件监听器
         this.setSize(1680,960);//窗口大小
         this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//退出窗口时真正退出程序
         this.setVisible(true);//设置为可显示
         this.addWindowListener(new WindowAdapter() {//关闭游戏后保存退出
             @Override
             public void windowClosing(WindowEvent e) {
                 System.out.println("游戏关闭");
                 Recorder.saveRecord();
                 System.exit(0);
             }
         });
     }
}
//添加键盘监听器
class MyPanel extends JPanel implements KeyListener,Runnable {//实现面板


    tankSpecies.cfj cfj=null;
    int enemyTankNumber=8;
    //存放敌人tank
    Vector<enemy1> enemyTanks=new Vector<>();//存放敌人tank的集合，使用Vector保证线程安全
    //定义三张爆炸过程图
    Image image1=null;
    Image image2=null;
    Image image3=null;

    //存放爆炸效果,当一个tank被击中时，就会往其中添加一个对象
    Vector<BoomEffect> boomEffects=new Vector<>();

    Vector<enemy1> oldDat=new Vector<>();
    public  MyPanel(String option){
        cfj=new cfj(200,200);
        if(option.equals("1")){//新游戏
            System.out.println("开始新游戏");
            for (int i = 0; i < enemyTankNumber; i++) {
                enemy1 enemy1 = new enemy1(100 * (i + 1), 0);
                //构造器中设置全部敌人
                enemy1.setEnemyTanks(enemyTanks);
                //启动敌方tank线程
                new Thread(enemy1).start();
                //创建当前敌人tank的bullet类
                Bullet bullet = new Bullet(enemy1.getX() + 20, enemy1.getY() + 60, enemy1.getDirection());
                //加入enemy的Vector
                enemy1.vector.add(bullet);
                //启动敌方bullet对象
                new Thread(bullet).start();
                enemyTanks.add(enemy1);//敌方tank分散开来，注意下标
            }
        }else{//继续上局游戏
            System.out.println("继续上局游戏");
            this.oldDat=Recorder.readRecord();
            for (int i = 0; i < enemyTankNumber; i++) {
                enemy1 enemy1 = new enemy1(oldDat.get(i).getX(), oldDat.get(i).getY());
                //构造器中设置全部敌人
                enemy1.setEnemyTanks(enemyTanks);
                //启动敌方tank线程
                new Thread(enemy1).start();
                //创建当前敌人tank的bullet类
                Bullet bullet = new Bullet(enemy1.getX() + 20, enemy1.getY() + 60, enemy1.getDirection());

                //加入enemy的Vector
                enemy1.vector.clear();
                enemy1.vector.add(bullet);
                //启动敌方bullet对象

                new Thread(bullet).start();
                enemyTanks.add(enemy1);//敌方tank分散开来，注意下标
            }

        }


        //构造器中初始化图片对象
        image1=Toolkit.getDefaultToolkit().getImage(MyPanel.class.getResource("/1.png"));
        image2=Toolkit.getDefaultToolkit().getImage(MyPanel.class.getResource("/2.png"));
        image3=Toolkit.getDefaultToolkit().getImage(MyPanel.class.getResource("/3.png"));
        //记录敌方tank
        Recorder.setEnemyTanks(enemyTanks);

    }
    //显示我方击毁tank数量
    public void showInfo(Graphics g){
        g.setColor(Color.BLACK);
        Font font = new Font("宋体", Font.BOLD, 25);
        g.setFont(font);
        g.drawString("击毁敌方坦克数量",1300,50);
        drawTank(1300,80,g,0,0);
        g.setColor(Color.BLACK);
        g.drawString(Recorder.getBittenEnemyTankNum()+"",1360,120);

    }


    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("y年M月d日");
    String datetime=dateTimeFormatter.format(LocalDateTime.now());
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.fillRect(0,0,1280,960);
        showInfo(g);
        g.setColor(Color.blue);
        g.drawString(datetime,0,10);
        if(cfj!=null&&cfj.isAlive)
        drawTank(cfj.getX(),cfj.getY(),g,cfj.getDirection(),0);//画出我方tank

        //画出子弹
        for (int i = 0; i <cfj.bullets.size(); i++) {
            Bullet bullet=cfj.bullets.get(i);
            if(bullet!=null&&bullet.isAlive!=false){
                if(bullet.direction==0||bullet.direction==2)
                    g.draw3DRect(bullet.x,bullet.y,1,5,false);
                else g.draw3DRect(bullet.x,bullet.y,5,1,false);
            }else {
                cfj.bullets.remove(bullet);
            }
        }


        //如果boomEffects中有对象，就画出相应的爆炸效果
        for (int i = 0; i <boomEffects.size() ; i++) {
            BoomEffect boomEffect=boomEffects.get(i);
            //根据当前时刻画出相应效果图
            if(boomEffect.life>6){
                g.drawImage(image1,boomEffect.x,boomEffect.y,80,80,this);
            } else if (boomEffect.life > 3) {
                g.drawImage(image2,boomEffect.x,boomEffect.y,80,80,this);
            }else {
                g.drawImage(image3,boomEffect.x,boomEffect.y,80,80,this);
            }
            //到下一个时刻
            boomEffect.lifeDown();
            //爆炸加载完就从vector中删除
            if(boomEffect.life==0){
                boomEffects.remove(boomEffect);
            }
        }

        //画出敌人tank，遍历vector
        for (int i = 0; i < enemyTanks.size(); i++) {
            enemy1 enemy1=  enemyTanks.get(i);
            if(enemy1.isAlive) {//tank存活才能画
                drawTank(enemy1.getX(), enemy1.getY(), g, enemy1.getDirection(), 1);
                //画出tank后还要画出子弹
                for (int i1 = 0; i1 < enemy1.vector.size(); i1++) {
                    //取出每个tank的子弹
                    Bullet bullet = enemy1.vector.get(i1);
                    //绘制
                    if (bullet.isAlive == true) {
                        if (bullet.direction == 0 || bullet.direction == 2)
                            g.draw3DRect(bullet.x, bullet.y, 1, 5, false);
                        else g.draw3DRect(bullet.x, bullet.y, 5, 1, false);
                    } else {
                        //移除子弹
                        enemy1.vector.remove(bullet);
                    }
                }
            }
        }
        
    }
    //封装tank绘画方法

    /**
     *
     * @param x tank左上角横坐标
     * @param y tank左上角纵坐标
     * @param g 调用绘画方法的对象
     * @param direction tank朝向
     * @param type tank类型
     */
    public void drawTank(int x,int y,Graphics g,int direction,int type){
        switch (type){
            case 0://cfj型tank
                g.setColor(Color.green);
                break;
            case 1://敌方tank
                g.setColor(Color.red);
                break;
        }
        //根据方向绘制tank
        switch (direction){//0-3表示
            case 0://上
                g.fill3DRect(x,y,10,60,true);
                g.fill3DRect(x+30,y,10,60,true);
                g.fill3DRect(x+10,y+10,20,40,false);
                g.fillOval(x+10,y+20,20,20);
                g.drawLine(x+20,y+30,x+20,y);
                break;
            case 1://右
                g.fill3DRect(x,y,60,10,true);
                g.fill3DRect(x,y+30,60,10,true);
                g.fill3DRect(x+10,y+10,40,20,false);
                g.fillOval(x+20,y+10,20,20);
                g.drawLine(x+30,y+20,x+60,y+20);
                break;
            case 2://下
                g.fill3DRect(x,y,10,60,true);
                g.fill3DRect(x+30,y,10,60,true);
                g.fill3DRect(x+10,y+10,20,40,false);
                g.fillOval(x+10,y+20,20,20);
                g.drawLine(x+20,y+30,x+20,y+60);
                break;
            case 3://左
                g.fill3DRect(x,y,60,10,true);
                g.fill3DRect(x,y+30,60,10,true);
                g.fill3DRect(x+10,y+10,40,20,false);
                g.fillOval(x+20,y+10,20,20);
                g.drawLine(x+30,y+20,x,y+20);
                break;

        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
    boolean coolDown=true;
    class Cool extends Thread{
        @Override
        public void run() {
            coolDown=false;
            try {
                sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            coolDown=true;
        }

    }
    @Override
    public void keyPressed(KeyEvent e) {//经典wasd


        if(e.getKeyCode()==KeyEvent.VK_W){
        cfj.setDirection(0);
        cfj.moveUp();
        } else if (e.getKeyCode()==KeyEvent.VK_D) {
            cfj.setDirection(1);
            cfj.moveRight();
        } else if (e.getKeyCode()==KeyEvent.VK_S) {
            cfj.setDirection(2);
            cfj.moveDown();
        } else if (e.getKeyCode()==KeyEvent.VK_A) {
            cfj.setDirection(3);
            cfj.moveLeft();
        }

        if(e.getKeyCode()==KeyEvent.VK_J){//我方tank攻击按键
//            if(cfj.bullet==null||cfj.bullet.isAlive==false) {//只有第一次射击才会创建子弹，之后就要判断子弹是否消亡
//                cfj.shot();
//            }
            if(coolDown&&cfj.isAlive) {//每次按j时启动冷却线程，冷却完毕后该冷却线程消失，可以发射下一发并开启下一个冷却线程
                cfj.shot();
                new Cool().start();
            }
        }
        this.repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
    //作为线程，需要不断地重绘
    @Override
    public void run() {
        while (true){
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            //先判断是否击中敌人tank
            if(cfj.bullet!=null&&cfj.bullet.isAlive){
                for (int i = 0; i < enemyTanks.size(); i++) {
                    enemy1 enemy=enemyTanks.get(i);
                    isHit(cfj.bullets,enemy);
                }
            }
            //再判断是否被敌人击中
            isHitByEnemy();
            this.repaint();
        }
    }
    //判断子弹是否击中tank
    /**
     *
     * @param bullets 子弹
     * @param tank 敌我坦克
     * */
    public void isHit(Vector<Bullet> bullets, tank tank){
        switch (tank.getDirection()){
            case 0:
            case 2:
                for (int i = 0; i < bullets.size(); i++) {
                    Bullet bullet=bullets.get(i);
                    if (bullet.x > tank.getX() && bullet.x < tank.getX() + 40 && bullet.y > tank.getY() && bullet.y < tank.getY() + 60) {
                        bullet.isAlive = false;
                        tank.isAlive = false;
                        //创建爆炸效果
                        BoomEffect boomEffect = new BoomEffect(tank.getX(), tank.getY());
                        boomEffects.add(boomEffect);
                        enemyTanks.remove(tank);
                        if(tank instanceof enemy1){
                            Recorder.addNum();
                        }
                    }
                }
                break;
            case 1:
            case 3:
                for (int i = 0; i < bullets.size(); i++) {
                    Bullet bullet = bullets.get(i);
                    if (bullet.x > tank.getX() && bullet.x < tank.getX() + 60 && bullet.y > tank.getY() && bullet.y < tank.getY() + 40) {
                        bullet.isAlive = false;
                        tank.isAlive = false;
                        BoomEffect boomEffect = new BoomEffect(tank.getX(), tank.getY());
                        boomEffects.add(boomEffect);
                        enemyTanks.remove(tank);
                        if(tank instanceof enemy1){
                            Recorder.addNum();
                        }
                    }
                }
                break;
        }
    }

    //判断敌人tank是否击中我方tank
    public void isHitByEnemy(){
        for (int i = 0; i < enemyTanks.size(); i++) {
            enemy1 enemy=enemyTanks.get(i);
            for (int j = 0; j < enemy.vector.size(); j++) {
                Bullet bullet=enemy.vector.get(j);
                if(cfj.isAlive&&bullet.isAlive){
                    Vector<Bullet> bullets = new Vector<>();
                    bullets.add(bullet);
                    isHit(bullets,cfj);
                }
            }
        }
    }
}

