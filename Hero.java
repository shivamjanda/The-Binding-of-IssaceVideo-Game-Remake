import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;
import java.util.ArrayList;
import java.awt.Font;
import java.awt.Color;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.util.Properties;
import java.awt.GraphicsEnvironment;
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
public class Hero extends Actor
{
    GreenfootSound shootSFX = new GreenfootSound("Tear.mp3");

    int moveRight = 5, moveLeft = 5, moveUp = 5, moveDown = 5;

    private int wait = 0;
    private final int waitTime = 10;
    private boolean placeBomb = false;
    String v = "null";
    boolean movingRight = false, movingLeft = false, movingUp = false, movingDown = false;

    //hero stats
    double health = 6;
    double maxHealth = 6;
    double damage = -3.5;
    double range = 500;
    double shootSpeed = 600; //can shoot every 600 miliseconds
    int numBomb = 5;
    boolean playMusic = false;
    boolean pickingUpItem = false; // is true for 2 sec after hero picks up powerup
    GreenfootImage image1 = Images.imgs.get("hero1.png");
    GreenfootImage image2 = Images.imgs.get("hero2.png");
    GreenfootImage image3 = Images.imgs.get("hero3.png");
    GreenfootImage image4 = Images.imgs.get("hero4.png");
    GreenfootImage image5 = Images.imgs.get("hero5.png");
    GreenfootImage image6 = Images.imgs.get("hero6.png");
    GreenfootImage image7 = Images.imgs.get("hero7.png");
    GreenfootImage image8 = Images.imgs.get("hero8.png");
    GreenfootImage image9 = Images.imgs.get("hero9.png");

    private SimpleTimer timer = new SimpleTimer();
    private SimpleTimer animateTimer = new SimpleTimer();
    private SimpleTimer invincibleTime = new SimpleTimer();
    private SimpleTimer animateInvinT = new SimpleTimer();

    public void removeEnemies(){ // hacks (actually so that you can test everything, Mr Chan)
        if (!getWorld().getObjects(Enemy.class).isEmpty() && Greenfoot.isKeyDown("k")){
            getWorld().removeObjects(getWorld().getObjects(Enemy.class));
        }
    }

    SimpleTimer test = new SimpleTimer();
    /**
     * Act - do whatever the Hero wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() {
        changeSize();
        if (!isDead){
            display(changeImage(fill())); //health
        }
        headOnCollision();
        sideCollision();
        shoot();
        checkDeath();
        bomb();
        removeEnemies();
        removeObstacles();
        if(pickingUpItem){
            animateItemPickUp();
        }
        else {
            if(!isDead){ //cant move when dead
                movement();
            }
        }
        //powerups
        EightInchNails();
        TheInnerEye();
        PuplaDuplex();
        ForeverAlone();
        Leo();
        BloodClot();
        Polyphemus();
        TwentyTwenty();
        Snack();
        RedHeart();
        CatONineTails();
        Binky();
        Capricorn();
        SoyMilk();
        MomsEye();
        //music
        if(playMusic){
            BackgroundMusic.playMusic();
            playMusic = false;
            createProperties();
        }
        checkChangeLevel();
        animateInvin();
    }

    //used to update properties of hero
    public void createProperties(){
        Properties prop = new Properties();
        OutputStream output = null;
        try {
            output = new FileOutputStream("Hero.properties");
            // set the properties value
            prop.setProperty("Music", Boolean.toString(playMusic));
            prop.setProperty("Health", Double.toString(health));
            prop.setProperty("MaxHealth", Double.toString(maxHealth));
            prop.setProperty("Damage", Double.toString(damage));
            prop.setProperty("ShootSpeed", Double.toString(shootSpeed));
            prop.setProperty("Range", Double.toString(range));
            prop.setProperty("EightInchNails", Boolean.toString(hasPickedUp_EightInchNails));
            prop.setProperty("20/20", Boolean.toString(hasPickedUp_TwentyTwenty));
            prop.setProperty("Snack", Boolean.toString(hasPickedUp_Snack));
            prop.setProperty("SoyMilk", Boolean.toString(hasPickedUp_SoyMilk));
            prop.setProperty("Leo", Boolean.toString(hasPickedUp_Leo));
            prop.setProperty("PuplaDuplex", Boolean.toString(hasPickedUp_PuplaDuplex));     
            prop.setProperty("Polyphemus", Boolean.toString(hasPickedUp_Polyphemus));
            prop.setProperty("TheInnerEye", Boolean.toString(hasPickedUp_TheInnerEye));
            prop.setProperty("CatONineTails", Boolean.toString(hasPickedUp_CatONineTails));
            prop.setProperty("Binky", Boolean.toString(hasPickedUp_Binky));
            prop.setProperty("BloodClot", Boolean.toString(hasPickedUp_BloodClot));
            prop.setProperty("ForeverAlone", Boolean.toString(hasPickedUp_ForeverAlone));
            prop.setProperty("Capricorn", Boolean.toString(hasPickedUp_Capricorn));
            prop.setProperty("MomsEye", Boolean.toString(hasPickedUp_MomsEye));
            prop.setProperty("Bombs", Integer.toString(numBomb));
            prop.setProperty("HasBeenHit", Boolean.toString(hasBeenHit));
            prop.setProperty("UpdateHearts", Boolean.toString(updateHearts));
            // save properties to project root folder
            prop.store(output, null);
        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //gets properties from properties file
    public void getProperties(){
        Properties prop = new Properties();
        InputStream input = null;

        try {
            input = new FileInputStream("Hero.properties");
            // load a properties file
            prop.load(input);
            // get the property value and print it out
            playMusic = Boolean.parseBoolean(prop.getProperty("Music"));
            health = Double.parseDouble(prop.getProperty("Health"));
            maxHealth = Double.parseDouble(prop.getProperty("MaxHealth"));
            damage = Double.parseDouble(prop.getProperty("Damage"));
            shootSpeed = Double.parseDouble(prop.getProperty("ShootSpeed"));
            range = Double.parseDouble(prop.getProperty("Range"));
            hasPickedUp_EightInchNails = Boolean.parseBoolean(prop.getProperty("EightInchNails"));
            hasPickedUp_TwentyTwenty = Boolean.parseBoolean(prop.getProperty("20/20"));
            hasPickedUp_Snack = Boolean.parseBoolean(prop.getProperty("Snack"));
            hasPickedUp_SoyMilk = Boolean.parseBoolean(prop.getProperty("SoyMilk"));
            hasPickedUp_PuplaDuplex = Boolean.parseBoolean(prop.getProperty("PuplaDuplex"));
            hasPickedUp_Leo = Boolean.parseBoolean(prop.getProperty("Leo")); 
            hasPickedUp_Polyphemus = Boolean.parseBoolean(prop.getProperty("Polyphemus"));
            hasPickedUp_TheInnerEye = Boolean.parseBoolean(prop.getProperty("TheInnerEye"));
            hasPickedUp_CatONineTails = Boolean.parseBoolean(prop.getProperty("CatONineTails"));
            hasPickedUp_Binky = Boolean.parseBoolean(prop.getProperty("Binky"));
            hasPickedUp_Capricorn = Boolean.parseBoolean(prop.getProperty("Capricorn"));
            hasPickedUp_ForeverAlone = Boolean.parseBoolean(prop.getProperty("ForeverAlone"));
            hasPickedUp_BloodClot = Boolean.parseBoolean(prop.getProperty("BloodClot"));
            hasPickedUp_MomsEye = Boolean.parseBoolean(prop.getProperty("MomsEye"));
            numBomb = Integer.parseInt(prop.getProperty("Bombs"));
            hasBeenHit = Boolean.parseBoolean(prop.getProperty("HasBeenHit"));
            updateHearts = Boolean.parseBoolean(prop.getProperty("UpdateHearts"));
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //plays the item pick up animation
    public void animateItemPickUp(){
        setImage(Images.imgs.get("heroItem.png"));
    }

    //changes size if picks up binky otherwise sets pics to all the same size
    public void changeSize(){
        if (hasPickedUp_Binky){
            image1.scale(36, 42);
            image2.scale(36, 42);
            image3.scale(36, 42);
            image4.scale(36, 42);
            image5.scale(36, 42);
            image6.scale(36, 42);
            image7.scale(36, 42);
            image8.scale(36, 42);
            image9.scale(36, 42);
            Images.imgs.get("heroHurt.png").scale(36, 42);
            Images.imgs.get("invisHero.png").scale(36, 42);
            Images.imgs.get("heroItem.png").scale(36, 42);
        }
        else{
            image1.scale(47, 55);
            image2.scale(47, 55);
            image3.scale(47, 55);
            image4.scale(47, 55);
            image5.scale(47, 55);
            image6.scale(47, 55);
            image7.scale(47, 55);
            image8.scale(47, 55);
            image9.scale(47, 55);
            Images.imgs.get("heroHurt.png").scale(47, 55);
            Images.imgs.get("invisHero.png").scale(47, 55);
            Images.imgs.get("heroItem.png").scale(47, 55);
        }
    }
    //basic movement
    public void movement() {
        //had to make these exceptions since otherwise the hero would get stuck at the walls.
        //basically if it comes into contact with a wall from a specific direction (let's say to the right), the movement to the right is set to 0
        //but you can still move left
        if (moveRight == 0) {
            moveLeft = 5;
        } else if (moveLeft == 0) {
            moveRight = 5;
        } else {
            moveLeft = moveRight;
        }

        //same here but vertically
        if (moveDown == 0) {
            moveUp = 5;
        } else if (moveUp == 0) {
            moveDown = 5;
        } else {
            moveDown = moveUp;
        }

        //basic movement
        if (Greenfoot.isKeyDown("w")) {
            setLocation(getX(), getY() - moveUp);
            animateUp();
            movingUp = true;
            movingDown = false;
            movingLeft = false;
            movingRight = false;
        } else if (Greenfoot.isKeyDown("s")) {
            setLocation(getX(), getY() + moveDown);
            animateDown();
            movingUp = false;
            movingDown = true;
            movingLeft = false;
            movingRight = false;
        } else if (Greenfoot.isKeyDown("d")) {
            setLocation(getX() + moveRight, getY());
            movingRight = true;
            movingLeft = false; 
            movingUp = false;
            movingDown = false;
            animateRight();
        } else if (Greenfoot.isKeyDown("a")) {
            setLocation(getX() - moveLeft, getY());
            movingLeft = true;
            movingRight = false;
            movingUp = false;
            movingDown = false;
            animateLeft();
        } else {
            setImage(image1);
            movingLeft = false;
            movingRight = false;
            movingUp = false;
            movingDown = false;
        }
    }

    //returns the direction the hero is moving, used in bullet curve
    public boolean movingDirection(int i) {
        if (i == 1) {
            return movingRight;
        } else if (i == 2){
            return movingLeft;
        } else if (i == 3) {
            return movingUp;
        } else if (i == 4) {
            return movingDown;
        } else {
            return false;
        }
    }

    boolean hasBeenHit = false;
    boolean isInvin = false; //is invincible
    boolean isDead = false; //so that only one gameover is added
    //check if hero is dead and set invincibility periods everytime hero is hit
    //also adds gameover screen to world
    public void checkDeath() {
        Actor d = getOneIntersectingObject(Enemy.class);
        Actor f = getOneIntersectingObject(EnemyBullet.class);
        Actor g = getOneIntersectingObject(Boss.class);
        hasBeenHit = false;
        createProperties(); 
        if (health == 0 && !isDead){ 
            createProperties();
            isDead = true; //so that only one gameover is added
            isInvin = true;
            BackgroundMusic.stopMusic();
            BossMusic.stopMusic();
            getWorld().addObject(new GameOver(), getWorld().getWidth()/2, getWorld().getHeight()/2);
        }
        if ((d!=null || f!=null || g!=null)){ //if hit
            if (!isInvin) { 
                health -= 1;  //reduce health
                hasBeenHit = true;
                invincibleTime.mark(); //start counting for how long hero will be invinciblek
                isInvin = true;
                createProperties(); //udpates properties
                getProperties();
            }
            getWorld().removeObject(f);
        }
        if(invincibleTime.millisElapsed() > 1000){
            isInvin = false; //hero isnt invincible after 1 second of being hit
        }
    }  

    //Animates the invincibilty thing, when hero is hit
    public void animateInvin () {
        if (isInvin) {
            if (animateInvinT.millisElapsed() > 50) {
                setImage(Images.imgs.get("invisHero.png"));
                animateInvinT.mark();
            } else {
                setImage(Images.imgs.get("heroHurt.png"));
            }
        }
    }

    //checks if the hero is colliding with specific parts of the wall and sets the speed of the hero accordingly (speed at 0 if colliding)
    public void sideCollision() {
        Obstacle wCheck = (Obstacle)getOneObjectAtOffset(getImage().getWidth()/2, 0, Obstacle.class);
        Obstacle wCheck2 = (Obstacle)getOneObjectAtOffset(getImage().getWidth()/2, -getImage().getHeight()/2, Obstacle.class);
        Obstacle wCheck3 = (Obstacle)getOneObjectAtOffset(getImage().getWidth()/2, getImage().getHeight()/4, Obstacle.class);
        Obstacle wCheck4 = (Obstacle)getOneObjectAtOffset(getImage().getWidth()/2, -getImage().getHeight()/4, Obstacle.class);

        Obstacle wCheck5 = (Obstacle)getOneObjectAtOffset(-getImage().getWidth()/2, 0, Obstacle.class);
        Obstacle wCheck6 = (Obstacle)getOneObjectAtOffset(-getImage().getWidth()/2, -getImage().getHeight()/2, Obstacle.class);
        Obstacle wCheck7 = (Obstacle)getOneObjectAtOffset(-getImage().getWidth()/2, getImage().getHeight()/4, Obstacle.class);
        Obstacle wCheck8 = (Obstacle)getOneObjectAtOffset(-getImage().getWidth()/2, -getImage().getHeight()/4, Obstacle.class);

        if (wCheck != null || wCheck2 != null || wCheck3 != null || wCheck4 != null) {
            moveRight = 0;
        } else {
            moveRight = 5;
        }

        if (wCheck5 != null || wCheck6 != null || wCheck7 != null || wCheck8 != null) {
            moveLeft = 0;
        } else {
            moveLeft = 5;
        }

    }

    //same here but with top down collisions
    public void headOnCollision() {
        Obstacle wCheck3 = (Obstacle)getOneObjectAtOffset(0, getImage().getHeight()/2, Obstacle.class);
        Obstacle wCheck4 = (Obstacle)getOneObjectAtOffset(0, -getImage().getHeight()/2, Obstacle.class);
        if (wCheck3 != null) {
            moveDown = 0;
        } else {
            moveDown = 5;
        }

        if (wCheck4 != null) {
            moveUp = 0;
        } else {
            moveUp = 5;
        }
    }

    //various animations. Checks which key is pressed and what picture should be switched to
    private void animateDown() {
        if (animateTimer.millisElapsed() > 25){
            if (getImage() == image2) {
                setImage(image3);
                animateTimer.mark();
            } else {
                setImage(image2);
                animateTimer.mark();
            }
        }
    }

    private void animateLeft() {
        if (animateTimer.millisElapsed() > 25){
            if (getImage() == image4) {
                setImage(image5);
                animateTimer.mark();
            } else {
                setImage(image4);
                animateTimer.mark();
            }
        }
    }

    private void animateRight() {
        if (animateTimer.millisElapsed() > 25){
            if (getImage() == image6) {
                setImage(image7);
                animateTimer.mark();
            } else {
                setImage(image6);
                animateTimer.mark();
            }
        }
    }

    private void animateUp() {
        if (animateTimer.millisElapsed() > 25){
            if (getImage() == image8) {
                setImage(image9);
                animateTimer.mark();
            } else {
                setImage(image8);
                animateTimer.mark();
            }
        }
    }

    SimpleTimer bombTimer = new SimpleTimer();
    //adds bombs to the world
    public void bomb(){
        BombCounter b = ((BombCounter) getWorld().getObjects(BombCounter.class).get(0));
        if(Greenfoot.isKeyDown("e") && bombTimer.millisElapsed() >= 1000 && b.bombs > 0){ 
            getWorld().addObject(new Bomb(),getX(), getY());
            World myWorld = getWorld();
            Room r = (Room)myWorld;
            BombCounter counter = r.getCounter();
            numBomb--;
            bombTimer.mark();
            createProperties();
        }
    }

    //removes obstacles if has leo powerup
    public void removeObstacles(){
        if (hasPickedUp_Leo){
            Actor a = getOneIntersectingObject(Pedestal.class);
            Actor b = getOneIntersectingObject(rock.class);
            if (a!=null){
                getWorld().removeObject(a);
            }
            if (b!=null){
                getWorld().removeObject(b);
            }
        }
    }

    //Directional shooting with arrow keys spawns an instance of 'hero bullet' and rotates it according to which arrow key is pressed. Actual shooting happens in that class
    public void shoot() {
        //4 instances because of various powerups needs
        HeroBullet h = new HeroBullet(); 
        HeroBullet h2 = new HeroBullet();
        HeroBullet h3 = new HeroBullet();
        HeroBullet h4 = new HeroBullet();
        if(hasPickedUp_EightInchNails){ //if hero has picked up eightinchnails powerup, change the bullet's picture
            h.setImage(Images.imgs.get("Nail.png"));
            h2.setImage(Images.imgs.get("Nail.png"));
            h3.setImage(Images.imgs.get("Nail.png"));
            h4.setImage(Images.imgs.get("Nail.png"));
        }
        if (hasPickedUp_PuplaDuplex){ // if the hero has picked up PuplaDuplex powerup, change the bullets picture
            h.setImage(Images.imgs.get("widen.png"));
            h2.setImage(Images.imgs.get("widen.png"));
            h3.setImage(Images.imgs.get("widen.png"));
            h4.setImage(Images.imgs.get("widen.png"));
        }
        if (hasPickedUp_Polyphemus){ // if the hero has picked up Polyphemus powerup, change the bullets picture
            h.getImage().scale(50,50);
        }
        if (timer.millisElapsed() > shootSpeed){
            if(Greenfoot.isKeyDown("right")){
                shootSFX.play();
                v = "right";
                if (hasPickedUp_TwentyTwenty){ //if has 20/20, add 2 bullets
                    getWorld().addObject(h, getX(), getY() + 10);
                    getWorld().addObject(h2, getX(), getY() - 10);
                }
                if(hasPickedUp_MomsEye){ //if has moms eye, add one bullet behind hero
                    h4.setRotation(180);
                    getWorld().addObject(h4, getX(), getY());
                }
                if (hasPickedUp_TheInnerEye){//if has TheInnerEye, add 3 bullets
                    getWorld().addObject(h, getX(), getY() + 10);
                    getWorld().addObject(h2, getX(), getY() - 10);
                    getWorld().addObject(h3, getX(), getY());
                }
                else {
                    getWorld().addObject(h, getX(), getY());
                }
                timer.mark();
            }
            else if (Greenfoot.isKeyDown("left")){
                shootSFX.play();
                v = "left";
                if (hasPickedUp_TwentyTwenty){//same as above                    
                    getWorld().addObject(h, getX(), getY() + 10);
                    getWorld().addObject(h2, getX(), getY() - 10);
                    h2.setRotation(180);
                }
                if(hasPickedUp_MomsEye){//same as above    
                    getWorld().addObject(h4, getX(), getY());
                }
                if (hasPickedUp_TheInnerEye){//same as above    
                    getWorld().addObject(h, getX(), getY() + 10);
                    getWorld().addObject(h2, getX(), getY() - 10);
                    getWorld().addObject(h3, getX(), getY());
                    h2.setRotation(180);
                    h3.setRotation(180);
                }

                else {
                    getWorld().addObject(h, getX(), getY());
                }
                h.setRotation(180);
                timer.mark();
            }
            else if(Greenfoot.isKeyDown("up")){
                shootSFX.play();
                v = "up";
                if (hasPickedUp_TwentyTwenty){//same as above    
                    getWorld().addObject(h, getX() + 10, getY());
                    getWorld().addObject(h2, getX() - 10, getY());
                    h2.setRotation(270);
                }
                if(hasPickedUp_MomsEye){//same as above    
                    h4.setRotation(90);
                    getWorld().addObject(h4, getX(), getY());
                }
                if (hasPickedUp_TheInnerEye){//same as above    
                    getWorld().addObject(h, getX() + 10, getY());
                    getWorld().addObject(h2, getX() - 10, getY());
                    getWorld().addObject(h3, getX(), getY());
                    h2.setRotation(270);
                    h3.setRotation(270);
                }
                else {
                    getWorld().addObject(h, getX(), getY());
                }
                h.setRotation(270);
                timer.mark();
            }
            else if(Greenfoot.isKeyDown("down")){
                shootSFX.play();
                v = "down";
                if (hasPickedUp_TwentyTwenty){//same as above    
                    getWorld().addObject(h, getX() + 10, getY());
                    getWorld().addObject(h2, getX() - 10, getY());
                    h2.setRotation(90);
                }
                if(hasPickedUp_MomsEye){//same as above    
                    h4.setRotation(270);
                    getWorld().addObject(h4, getX(), getY());
                }
                if (hasPickedUp_TheInnerEye){//same as above    
                    getWorld().addObject(h, getX() + 10, getY());
                    getWorld().addObject(h2, getX() - 10, getY());
                    getWorld().addObject(h3, getX(), getY());
                    h2.setRotation(90);
                    h3.setRotation(90);
                }
                else {
                    getWorld().addObject(h, getX(), getY());
                }
                h.setRotation(90);
                timer.mark();
            }
        }
    }

    /**
     * Following methods are used for powerups
     */
    // checks if hero comes in contact with the healthPowerUp
    boolean hasPickedUp_RedHeart = false;
    public void RedHeart(){
        if (health < maxHealth){ //only picks up when health isnt full
            if (isTouching(RedHeart.class)) {    
                health += 1; //adds 1 health
                hasPickedUp_RedHeart = true;
                getWorld().removeObject(getOneIntersectingObject(RedHeart.class));
                createProperties();
            }
        }
    }  
    //makes the text when items are picked up
    public void createText(Text t, int y){
        int x = getWorld().getWidth()/2 - t.width;
        getWorld().addObject(t, x ,y);
        textTime.mark();
    }
    //makes the background when items are picked up
    public void createBackground(){
        Background b = new Background();
        getWorld().addObject(b, getWorld().getWidth()/2, 80);
    }

    //new font
    public Font font(){
        Font font = new Font("Arial", Font.PLAIN, 24);
        try{
            InputStream is = new FileInputStream("upheavtt.ttf");
            font = Font.createFont(Font.TRUETYPE_FONT, is);
        }
        catch(Exception e){
        }
        return font;
    }
    SimpleTimer textTime = new SimpleTimer();// timer for how long the text stays up after picking up a powerup

    boolean hasPickedUp_EightInchNails = false;
    //increase damage and change tear appearance
    //change properties when powerup is pickedup and display text
    public void EightInchNails(){
        Text itemName = new Text("8 Inch Nails", "name");
        Text description = new Text("Damage Up, Nail Tears", "description");
        if (isTouching(EightInchNails.class)) {    
            damage += -1.5;
            hasPickedUp_EightInchNails = true;          
            createProperties();
            createBackground();
            createText(itemName, 90);
            createText(description, 130);
            pickingUpItem = true;
            textTime.mark();
        }
        if (textTime.millisElapsed() >= 2000 && hasPickedUp_EightInchNails){
            if(!getWorld().getObjects(ItemDescription.class).isEmpty()){
                getWorld().removeObjects(getWorld().getObjects(ItemDescription.class));
                pickingUpItem = false;
                textTime.mark();
            }
        }
    }

    boolean hasPickedUp_TwentyTwenty = false;
    //double shot
    //change properties when powerup is pickedup and display text
    public void TwentyTwenty(){
        Text itemName = new Text("20/20", "name");
        Text description = new Text("Double Shot!", "description");
        if (isTouching(TwentyTwenty.class)) {    
            hasPickedUp_TwentyTwenty = true;
            createBackground();
            createText(itemName, 90);
            createText(description, 130);
            createProperties();
            pickingUpItem = true;
        }
        if (textTime.millisElapsed() >= 2000 && hasPickedUp_TwentyTwenty){
            if(!getWorld().getObjects(ItemDescription.class).isEmpty()){
                getWorld().removeObjects(getWorld().getObjects(ItemDescription.class));
                pickingUpItem = false;
                textTime.mark();
            }
        }
    }

    //increase max health
    boolean hasPickedUp_Snack = false;
    //change properties when powerup is pickedup and display text
    public void Snack(){
        Text itemName = new Text("A Snack", "name");
        Text description = new Text("Health Up", "description");
        if (isTouching(Snack.class)) {   
            maxHealth += 1;
            health += 1;
            hasPickedUp_Snack = true;
            createBackground();
            createText(itemName, 90);
            createText(description, 130);
            createProperties();
            pickingUpItem = true;
        }
        if (textTime.millisElapsed() >= 2000 && hasPickedUp_Snack){
            if(!getWorld().getObjects(ItemDescription.class).isEmpty()){
                getWorld().removeObjects(getWorld().getObjects(ItemDescription.class));
                pickingUpItem = false;
                textTime.mark();
            }
        }
    }

    // increases damage and shoot speed
    boolean hasPickedUp_SoyMilk = false;
    //change properties when powerup is pickedup and display text
    public void SoyMilk(){
        Text itemName = new Text("Soy Milk", "name");
        Text description = new Text("Damage Down, Tears Way Up", "description");
        if (isTouching(SoyMilk.class)) {    
            damage = 1;
            shootSpeed = 100;
            hasPickedUp_SoyMilk = true;          
            createProperties();
            createBackground();
            createText(itemName, 90);
            createText(description, 130);
            pickingUpItem = true;

        }
        if (textTime.millisElapsed() >= 2000 && hasPickedUp_SoyMilk){
            if(!getWorld().getObjects(ItemDescription.class).isEmpty()){
                getWorld().removeObjects(getWorld().getObjects(ItemDescription.class));
                pickingUpItem = false;
                textTime.mark();
            }
        }
    }

    // wider bullets
    boolean hasPickedUp_PuplaDuplex = false;
    //change properties when powerup is pickedup and display text
    public void PuplaDuplex(){
        Text itemName = new Text("PuplaDuplex", "name");
        Text description = new Text("Widen Tear, Spectral Tear", "description");
        if (isTouching(PuplaDuplex.class)) {    
            hasPickedUp_PuplaDuplex = true;          
            createProperties();
            createBackground();
            createText(itemName, 90);
            createText(description, 130);
            pickingUpItem = true;
        }
        if (textTime.millisElapsed() >= 2000 && hasPickedUp_PuplaDuplex){
            if(!getWorld().getObjects(ItemDescription.class).isEmpty()){
                getWorld().removeObjects(getWorld().getObjects(ItemDescription.class));
                pickingUpItem = false;
                textTime.mark();
            }
        }
    }

    // can destroy obstacles when walking on top of it
    boolean hasPickedUp_Leo = false;
    //change properties when powerup is pickedup and display text
    public void Leo(){
        Text itemName = new Text("Leo", "name");
        Text description = new Text("Can Destroy Obstacles", "description");
        if (isTouching(Leo.class)) {    
            hasPickedUp_Leo = true;          
            createProperties();
            createBackground();
            createText(itemName, 90);
            createText(description, 130);
            pickingUpItem = true;
        }
        if (textTime.millisElapsed() >= 2000 && hasPickedUp_Leo){
            if(!getWorld().getObjects(ItemDescription.class).isEmpty()){
                getWorld().removeObjects(getWorld().getObjects(ItemDescription.class));
                pickingUpItem = false;
                textTime.mark();
            }
        }
    }

    // decreases shoot speed and shoots three bullets
    boolean hasPickedUp_TheInnerEye = false;
    //change properties when powerup is pickedup and display text
    public void TheInnerEye(){
        Text itemName = new Text("The Inner Eye", "name");
        Text description = new Text("Tears Down, Three Tears", "description");
        if (isTouching(TheInnerEye.class)) {    
            shootSpeed += 200;
            hasPickedUp_TheInnerEye = true;          
            createProperties();
            createBackground();
            createText(itemName, 90);
            createText(description, 130);
            pickingUpItem = true;
        }
        if (textTime.millisElapsed() >= 2000 && hasPickedUp_TheInnerEye){
            if(!getWorld().getObjects(ItemDescription.class).isEmpty()){
                getWorld().removeObjects(getWorld().getObjects(ItemDescription.class));
                pickingUpItem = false;
                textTime.mark();
            }
        }
    }

    // increases damage, decreases shoot speed and increases range
    boolean hasPickedUp_Polyphemus = false;
    //change properties when powerup is pickedup and display text
    public void Polyphemus(){
        Text itemName = new Text("Polyphemus", "name");
        Text description = new Text("Damage Up, Tears Down, Range Up", "description");
        if (isTouching(Polyphemus.class)) {
            damage = (damage - 4) * 2;
            shootSpeed = shootSpeed + 1000;
            range = range + 300;
            hasPickedUp_Polyphemus = true;          
            createProperties();
            createBackground();
            createText(itemName, 90);
            createText(description, 130);
            pickingUpItem = true;
        }
        if (textTime.millisElapsed() >= 2000 && hasPickedUp_Polyphemus){
            if(!getWorld().getObjects(ItemDescription.class).isEmpty()){
                getWorld().removeObjects(getWorld().getObjects(ItemDescription.class));
                pickingUpItem = false;
                textTime.mark();
            }
        }
    }

    //increase damage and shootspeed
    boolean hasPickedUp_CatONineTails = false;
    //change properties when powerup is pickedup and display text
    public void CatONineTails(){
        Text itemName = new Text("Cat O' Nine Tails", "name");
        Text description = new Text("Damage Up, Tears Up", "description");
        if (isTouching(CatONineTails.class)) {
            damage -= 1;
            shootSpeed = shootSpeed*0.77;
            hasPickedUp_CatONineTails = true;          
            createProperties();
            createBackground();
            createText(itemName, 90);
            createText(description, 130);
            pickingUpItem = true;
        }
        if (textTime.millisElapsed() >= 2000 && hasPickedUp_CatONineTails){
            if(!getWorld().getObjects(ItemDescription.class).isEmpty()){
                getWorld().removeObjects(getWorld().getObjects(ItemDescription.class));
                pickingUpItem = false;
                textTime.mark();
            }
        }
    }

    // increases damage and increases range
    boolean hasPickedUp_BloodClot = false;
    //change properties when powerup is pickedup and display text
    public void BloodClot(){
        Text itemName = new Text("BloodClot", "name");
        Text description = new Text("Damage up, Range Up", "description");
        if (isTouching(BloodClot.class))     {
            damage += -1;
            range += 100;
            hasPickedUp_BloodClot = true;          
            createProperties();
            createBackground();
            createText(itemName, 90);
            createText(description, 130);
            getImage().scale(36, 42);
            pickingUpItem = true;

        }
        if (textTime.millisElapsed() >= 2000 && hasPickedUp_BloodClot){
            if(!getWorld().getObjects(ItemDescription.class).isEmpty()){
                getWorld().removeObjects(getWorld().getObjects(ItemDescription.class));
                pickingUpItem = false;
                textTime.mark();
            }
        }
    }

    //decreases hero size
    boolean hasPickedUp_Binky = false;
    //change properties when powerup is pickedup and display text
    public void Binky(){
        Text itemName = new Text("Binky", "name");
        Text description = new Text("Decreased Size, Tears Up", "description");
        if (isTouching(Binky.class)) {
            shootSpeed = shootSpeed - 100;
            hasPickedUp_Binky = true;          
            createProperties();
            createBackground();
            createText(itemName, 90);
            createText(description, 130);
            getImage().scale(36, 42);
            pickingUpItem = true;
        }
        if (textTime.millisElapsed() >= 2000 && hasPickedUp_Binky){
            if(!getWorld().getObjects(ItemDescription.class).isEmpty()){
                getWorld().removeObjects(getWorld().getObjects(ItemDescription.class));
                pickingUpItem = false;
                textTime.mark();
            }
        }
    }

    // gives you a friendly helper
    boolean hasPickedUp_ForeverAlone = false;
    boolean hasAddedFly = false;
    //change properties when powerup is pickedup and display text
    public void ForeverAlone(){
        Text itemName = new Text("ForeverAlone", "name");
        Text description = new Text("Friend", "description");
        if (hasPickedUp_ForeverAlone && !hasAddedFly){
            FlyFriend a = new FlyFriend();
            getWorld().addObject(a, getX(), getY());
            hasAddedFly = true;
        }
        if (isTouching(ForeverAlone.class)) 
        {
            hasPickedUp_ForeverAlone = true; 
            createProperties();
            createBackground();
            createText(itemName, 90);
            createText(description, 130);
            getImage().scale(36, 42);
            pickingUpItem = true;
        }
        if (textTime.millisElapsed() >= 2000 && hasPickedUp_ForeverAlone){
            if(!getWorld().getObjects(ItemDescription.class).isEmpty()){
                getWorld().removeObjects(getWorld().getObjects(ItemDescription.class));
                pickingUpItem = false;
                textTime.mark();
            }
        }
    }

    //increases damage, range and health
    boolean hasPickedUp_Capricorn = false;
    //change properties when powerup is pickedup and display text
    public void Capricorn(){
        Text itemName = new Text("Capricorn", "name");
        Text description = new Text("Damage, Range, Health Up", "description");
        if (isTouching(Capricorn.class)) {
            damage -= 0.5;
            range += 100;
            maxHealth += 1;
            health += 1;
            hasPickedUp_Capricorn = true; 
            createProperties();
            createBackground();
            createText(itemName, 90);
            createText(description, 130);
            getImage().scale(36, 42);
            pickingUpItem = true;
        }
        if (textTime.millisElapsed() >= 2000 && hasPickedUp_Capricorn){
            if(!getWorld().getObjects(ItemDescription.class).isEmpty()){
                getWorld().removeObjects(getWorld().getObjects(ItemDescription.class));
                pickingUpItem = false;
                textTime.mark();
            }
        }
    }

    //shoots behind player
    boolean hasPickedUp_MomsEye = false;
    //change properties when powerup is pickedup and display text
    public void MomsEye(){
        Text itemName = new Text("Mom's Eye", "name");
        Text description = new Text("Tears At The Back of Your Head", "description");
        if (isTouching(MomsEye.class)) {
            hasPickedUp_MomsEye = true; 
            createProperties();
            createBackground();
            createText(itemName, 90);
            createText(description, 130);
            getImage().scale(36, 42);
            pickingUpItem = true;
        }
        if (textTime.millisElapsed() >= 2000 && hasPickedUp_MomsEye){
            if(!getWorld().getObjects(ItemDescription.class).isEmpty()){
                getWorld().removeObjects(getWorld().getObjects(ItemDescription.class));
                pickingUpItem = false;
                textTime.mark();
            }
        }
    }

    /**
     * Following methods are used for displaying health 
     */
    boolean updateHearts = true;
    //fills arraylist with hearts and displays
    public ArrayList<Actor> fill(){
        ArrayList<Actor> hearts = new ArrayList<Actor>();
        for (int i = 0; i < (int)maxHealth; i ++){
            hearts.add(new Heart());  //fill the arraylist with hearts according to hero's maxhealth
        }
        return hearts;
    }

    //changes the hearts to empty hearts according to current health
    public ArrayList<Actor> changeImage(ArrayList<Actor> hearts){
        if (health < maxHealth){
            for (int i = (int)health; i < hearts.size(); i++){
                hearts.get(i).getImage().clear();
                hearts.get(i).setImage(Images.imgs.get("emptyheart.png"));
            }
        }
        return hearts;
    }

    //displays the hearts
    public ArrayList<Actor> display(ArrayList<Actor> hearts){
        if(updateHearts){ //initializes
            for (int i = 0; i < hearts.size(); i++){
                getWorld().addObject(hearts.get(i), 175 + i*(hearts.get(i).getImage().getWidth() + 5), 40);
            }
            updateHearts = false; //boolean so that it doesnt update every act method
            createProperties();
        }
        else if (hasBeenHit){ //if the hero is hasBeenHit by enemy, updates the images
            getWorld().removeObjects(getWorld().getObjects(Heart.class));
            for (int i = 0; i < hearts.size(); i++){
                getWorld().addObject(hearts.get(i), 175 + i*(hearts.get(i).getImage().getWidth() + 5), 40);
            }
        }
        else if(hasPickedUp_Snack){ //if the hero picked up snack, updates the images
            getWorld().removeObjects(getWorld().getObjects(Heart.class));
            for (int i = 0; i < hearts.size(); i++){
                getWorld().addObject(hearts.get(i), 175 + i*(hearts.get(i).getImage().getWidth() + 5), 40);
            }
        }
        else if(hasPickedUp_RedHeart){ //if the hero picked up a red hearts, updates the images
            getWorld().removeObjects(getWorld().getObjects(Heart.class));
            for (int i = 0; i < hearts.size(); i++){
                getWorld().addObject(hearts.get(i), 175 + i*(hearts.get(i).getImage().getWidth() + 5), 40);
            }
        }
        else if(hasPickedUp_Capricorn){ //if the hero picked up capricorn, updates the images
            getWorld().removeObjects(getWorld().getObjects(Heart.class));
            for (int i = 0; i < hearts.size(); i++){
                getWorld().addObject(hearts.get(i), 175 + i*(hearts.get(i).getImage().getWidth() + 5), 40);
            }
        }
        return hearts;
    }

    //change the level once the hero is touching trapdoor
    public void checkChangeLevel(){
        if (isTouching(TrapDoor.class)){
            getProperties();
            Level.level += 1;
            createProperties();
            if (Level.level >= 6){ //level 5 is end
                BackgroundMusic.stopMusic(); 
                BossMusic.stopMusic();
                Greenfoot.setWorld(new Ending("")); //play the ending
            }
            else{
                Greenfoot.setWorld(new StartRoom());
            }
        }
    }

    /**
     * rest of these methods are to access variables from other classes
     */
    //for use in bullet curve
    public int moveRightValue() {
        return moveRight;
    }

    public String returnV() {
        return v;
    }

}
