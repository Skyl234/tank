package tankSpecies;

import OtherClass.Bullet;

import java.io.Serializable;
import java.util.Random;
import java.util.Vector;

public class enemy1 extends tank implements Runnable, Serializable {//最普通的敌方tank
    public Vector<Bullet> vector=new Vector<>();
    //新增vector，使得每个敌人能获得其他敌人的位置信息，完成防碰撞功能
    Vector<enemy1> enemyTanks=new Vector<>();

    //panel调用该方法设置


    public void setEnemyTanks(Vector<enemy1> enemyTanks) {
        this.enemyTanks = enemyTanks;
    }

    public boolean isAlive=true;
    public enemy1(int x,int y) {
        super(x, y);
        setSpeed(2);
        setDirection(2);
    }
    //通用矩形重叠判断
    public boolean isMatrixCollide(int[] rec1,int[] rec2){
        return !(rec1[2] <= rec2[0] || rec1[0] >= rec2[2] || -rec1[1] >= -rec2[3] || -rec1[3] <= -rec2[1]);
    }

    //判断当前敌人是否和其他敌人发生碰撞
    public boolean isCollide(){


        //遍历vector里面tank
        for (int i = 0; i < enemyTanks.size(); i++) {
            enemy1 enemyTank= enemyTanks.get(i);
            int d1=this.getDirection();
            int d2=enemyTank.getDirection();

            //排除本身
            if(this!=enemyTank){
                //按照重叠面积判断是否碰撞
                int[] rec2;
                int[] rec1;
                if(d1==0||d1==2){
                    rec1 = new int[]{this.getX(), this.getY() + 60, this.getX() + 40, this.getY()};
                }else{
                    rec1 = new int[]{this.getX(), this.getY() + 40, this.getX() + 60, this.getY()};
                }
                if(d2==0||d2==2){
                    rec2 = new int[]{enemyTank.getX(), enemyTank.getY() + 60, enemyTank.getX() + 40, enemyTank.getY()};
                }else{
                    rec2 = new int[]{enemyTank.getX(), enemyTank.getY() + 40, enemyTank.getX() + 60, enemyTank.getY()};
                }
                if (isMatrixCollide(rec1,rec2)){
                    return true;
                }


            }
        }
        return false;
    }


    /**
     * @param stride 控制一段时间内敌人的移动步幅
     * @param direction 当前敌人方向
     * @param time 隔多久走下一步
     */
    public void randomMove(int stride,int direction,int time){
        if(direction==0){
        while(stride-->0) {
            if(getY()>0&&!isCollide()){
                moveUp();
            }

            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        }else if(direction==1){
            while(stride-->0) {
                if(getX()<1200&&!isCollide()){
                    moveRight();
                }

            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        } else if(direction==2){
            while(stride-->0) {
                if(getY()<870&&!isCollide()){
                    moveDown();
                }

                try {
                    Thread.sleep(time);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }else {
            while (stride-- > 0) {
                if(getX()>0&&!isCollide()){
                    moveLeft();
                }

                try {
                    Thread.sleep(time);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    @Override
    public void run() {

        while (true) {
            if(isAlive&&vector.size()<10) {//解决敌人只能发射一颗子弹的问题
                //tank存在且场上没有敌方子弹了
                Bullet bullet = switch (getDirection()) {
                    case 0 -> new Bullet(getX() + 20, getY(), 0);
                    case 1 -> new Bullet(getX() + 60, getY() + 20, 1);
                    case 2 -> new Bullet(getX() + 20, getY() + 60, 2);
                    case 3 -> new Bullet(getX(), getY() + 20, 3);
                    default -> throw new IllegalStateException("Unexpected value: " + getDirection());
                };
                vector.add(bullet);
                new Thread(bullet).start();
            }

            switch (getDirection()) {
                case 0:
                    randomMove(30,0,50);
                    break;
                case 1:
                    randomMove(30,1,50);
                    break;
                case 2:
                    randomMove(30,2,50);
                    break;
                case 3:
                    randomMove(30,3,50);
                    break;
            }

            setDirection((int) (Math.random() * 4));//随机改变方向

            if (!isAlive) break;
        }
    }
}
