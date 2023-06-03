package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;
import entity.Player;
import tile.TileManager;

public class GamePanel extends JPanel implements Runnable{
    //SCREEN SETTINGS
    final int originalTileSize = 64; //16 x 16 standard tile size (character)
    final int scale = 1; //ex 16 x 16 will become 48 x 48

    public int tileSize = originalTileSize*scale;
    public final int maxScreenCol = 16; //how many tiles wide is screen?
    public final int maxScreenRow = 12; //how many tiles long is screen
    public final int screenWidth = tileSize*maxScreenCol;  //768 pixels   48*16
    public final int screenHeight = tileSize*maxScreenRow; //576 pixels

    
    static final long serialVersionUID = 15;

    //WORLD SETTINGS
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public final int worldWidth = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxWorldRow;
    

    //setdefault player position
    int playerX = 100;
    int playerY = 100;
    int playerSpeed = 4;

    //FPS
    int fps = 60; 

    Thread gameThread; //like a clock
    KeyHandler keyH = new KeyHandler();
    public CollisionChecker cChecker = new CollisionChecker(this);
    public Player player = new Player(this, keyH);
    TileManager tileM = new TileManager(this);


    public GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);

        this.addKeyListener(keyH);
        this.setFocusable(true);
        
    }

    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {

        double drawInterval = 1000000000/fps;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while(gameThread != null){
            currentTime = System.nanoTime(); 
            delta += (currentTime-lastTime)/drawInterval;
            timer += currentTime - lastTime;
            lastTime = currentTime;

            if(delta >= 1){
                //1 UPDATE: update info such as character positions
                update();
                //2 DRAW: draw the screen w/ updated info
                repaint();

                delta--;
                drawCount++;
            }

            if(timer > 1000000000){
                //System.out.println("FPS: "+ drawCount);
                drawCount = 0;
                timer = 0;
            }
            
        }
    }
    public void update(){
        player.update();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;

        tileM.draw(g2);
        player.draw(g2);

        g2.dispose();
    }

}