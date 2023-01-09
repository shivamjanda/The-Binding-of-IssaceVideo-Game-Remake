import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
/**
 * Computer Science Final Project
 * Teacher: Mr.Chan
 * Akathian Santhakumar, Ahmad Shah and Shivam Janda
 * 
 * January 19, 2017
 * 
 * Credits:
 * Greenfoot.org
 * ^
 * rkr profile on Greenfoot.org is Shivams profile (asked questions about code,
 * had to paste part of our own code when asking the question)
 * https://www.mkyong.com/java/java-properties-file-examples/
 */
public class GameOverMusic  
{
    static GreenfootSound GameOver = new GreenfootSound("Dead.mp3");
    //plays music
    public static void playMusic(){
        GameOver.playLoop();
    }
    //stop music
    public static void stopMusic(){
        GameOver.stop();
    }
}
