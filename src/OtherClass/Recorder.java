package OtherClass;

import tankSpecies.enemy1;
import tankSpecies.tank;

import java.io.*;
import java.util.Vector;

//记录玩家相关信息
public class Recorder {
    //定义记录的相关变量
    private static int bittenEnemyTankNum=0;
    //定义IO对象
    private static FileWriter fileWriter=null;
    private static BufferedWriter bufferedWriter=null;

    private static BufferedReader bufferedReader=null;

    private static FileReader fileReader=null;

    private static String  savePath="C:\\Users\\sky龙\\tank\\src\\enemyTanks.dat";
    private static String recordFilePath="C:\\Users\\sky龙\\tank\\src\\record.txt";

    private static Vector<enemy1> enemyTanks=null;

    public static void setEnemyTanks(Vector<enemy1> enemyTanks) {
        Recorder.enemyTanks = enemyTanks;
    }
    public static Vector<enemy1> readRecord(){
        try {
            bufferedReader=new BufferedReader(new FileReader(recordFilePath));
            bittenEnemyTankNum=Integer.parseInt(bufferedReader.readLine());

            ObjectInputStream ois=new ObjectInputStream(new FileInputStream(savePath));
            Vector<enemy1> enemyTanks=(Vector<enemy1>) ois.readObject();
            ois.close();
            bufferedReader.close();
            return enemyTanks;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }


    //当游戏退出时将击毁数,敌人坦克的坐标及方向保存到指定文件中
    public static void saveRecord(){
        try {
            bufferedWriter=new BufferedWriter(new FileWriter(recordFilePath));
            bufferedWriter.write(bittenEnemyTankNum+"");
            bufferedWriter.newLine();
            //遍历当前所有敌人坦克的vector集合
//            for (int i = 0; i < enemyTanks.size(); i++) {
//                enemy1 enemyTank=enemyTanks.get(i);
//                if(enemyTank.isAlive){
//                    //保存相关信息
//                    String info=enemyTank.getX()+" "+enemyTank.getY()+" "+enemyTank.getDirection()+"  "+enemyTank.getSpeed();
//                    //写入
//                    bufferedWriter.write(info);
//                    bufferedWriter.newLine();
//                }
//            }


            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(savePath));
            oos.writeObject(enemyTanks);
            oos.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            bufferedWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static int getBittenEnemyTankNum() {
        return bittenEnemyTankNum;
    }

    public static void setBittenEnemyTankNum(int bittenEnemyTankNum) {
        Recorder.bittenEnemyTankNum = bittenEnemyTankNum;
    }

    public static void addNum(){
        Recorder.bittenEnemyTankNum++;
    }
}
