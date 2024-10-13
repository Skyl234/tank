package OtherClass;

public class BoomEffect {//爆炸效果类
    //坐标
     public int x;
    public int y;

    public BoomEffect(int x, int y) {
        this.x = x;
        this.y = y;
    }

    //生命周期（进行到的状态）
    public int life=9;
    public boolean alive=true;
    //减少生命值
    public void lifeDown(){
        if(life>0){
            life--;
        }else {
            alive=false;
        }
    }
}
