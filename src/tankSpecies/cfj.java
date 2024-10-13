package tankSpecies;

import OtherClass.Bullet;

import java.util.Vector;

public class cfj extends tank {
    public Bullet bullet=null;//这样每次只能发射一颗子弹
    public Vector<Bullet> bullets=new Vector<>();//创建vector用以射出多颗子弹
    public cfj(int x,int y){
        super(x,y);
        setSpeed(5);
    }
    public void shot(){//射击逻辑

        switch (getDirection()){
            case 0:
                bullet=new Bullet(getX()+20,getY(),0);
                break;
            case 1:
                bullet=new Bullet(getX()+60,getY()+20,1);
                break;
            case 2:
                bullet=new Bullet(getX()+20,getY()+60,2);
                break;
            case 3:
                bullet=new Bullet(getX(),getY()+20,3);
                break;
        }
        //每次创建的子弹放入vector
        //if(bullets.size()<10)//可以控制射出子弹数量
        bullets.add(bullet);
        //启动线程
        new Thread(bullet).start();
    }

}
