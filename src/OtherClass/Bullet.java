package OtherClass;

import java.io.Serializable;

//子弹类
public class Bullet implements Runnable, Serializable {
    public int x;//子弹横坐标
    public int y;//子弹纵坐标
    public int direction=0;//子弹方向
    int speed=4;//子弹速度
    public boolean isAlive=true;//子弹是否存活
    public Bullet(int x, int y, int direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    @Override
    public void run() {
        while (true){
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            switch (direction){
                case 0://上
                    y-=speed;
                    break;
                case 1://右
                    x+=speed;
                    break;
                case 2://下
                    y+=speed;
                    break;
                case 3://坐
                    x-=speed;
                    break;
            }
            //子弹碰到边界就销毁
            if(!(x>=0&&x<=1280&&y>=0&&y<=960&&isAlive)){
                isAlive=false;
                break;
            }

        }
    }
}
