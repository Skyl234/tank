package tankSpecies;

public class tank {
    private int x;//横坐标
    private int y;//纵坐标
    private int direction=0;//方向默认为0（朝上）
    private int speed=1;//速度默认为1
    public boolean isAlive=true;//存活

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getSpeed() {
        return speed;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public tank(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public tank(){}

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
    public void moveUp(){

        y-= speed;
    }
    public void moveDown(){

        y+=speed;
    }
    public void moveRight(){

        x+=speed;
    }
    public void moveLeft(){

        x-=speed;
    }
}
