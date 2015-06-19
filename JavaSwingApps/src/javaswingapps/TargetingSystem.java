/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaswingapps;

/**
 *
 * @author Paolo
 */

import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.awt.geom.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.net.URL;

public class TargetingSystem extends JFrame
{
    //splash screen
    static SplashScreen mySplash;                   // instantiated by JVM we use it to get graphics
    static Graphics2D splashGraphics;               // graphics context for overlay of the splash image
    static Rectangle2D.Double splashTextArea;       // area where we draw the text
    static Font font;                               // used to draw our text
    
    MyJPanel panel;
    private JDesktopPane theDesktop;
    int x, y, b;
    Clock gameClock;
    Timer t;
    BufferedImage enemyImage;
    int num_enemies = 9;
    List<Integer> enemies_seen = new ArrayList<Integer>();
    int win_x = 1000;
    int win_y = 1050;
    int clock_x = 250;
    int clock_y = 700;
    int clock_width = win_x - (2 * clock_x);
    int clock_height = 175;
    int enemy_x = 250;
    int enemy_y = 50;
    int enemy_width = clock_width;
    int enemy_height = 2 * enemy_x;
    int bullet_diam = 20;
    int target_diam = 40;
    int target_radius = target_diam / 2;
    int max_distance = target_diam;
    int current_round = 0;
    int initial_round_duration = 15000;
    int round_duration = initial_round_duration;
    long current_duration;

    Color targetColor = Color.green;
    Color targetHitColor = Color.red;
    Color bulletHole = Color.blue;
    List<target> targets = new ArrayList<>();
    List<bulletHole> bullets = new ArrayList<>();

    
        
    ActionListener updateClock = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            //eventually change this to a countdown timer
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
            //gameClock.setTime(sdf.format(date));
            gameClock.setTime(current_duration - System.currentTimeMillis());
            repaint();
        }
    };

    class mousePressHandler extends MouseAdapter {
        public void mousePressed (MouseEvent e) {
            x = e.getX();
            y = e.getY();
            b = e.getButton();

            //just for example purposes
            if(b == e.BUTTON3) {
                refreshAll(true);
            }

            if(b == e.BUTTON1) {
                bullets.add(new bulletHole(x, y, bullet_diam));
                //check if it hit target(s)
                for(target t : targets) {
                    if(successful_hit(x, y, t.get_x(), t.get_y())) {
                        t.setColor(targetHitColor);
                        t.hit();
                    }
                }
            }
            requestFocus();
            repaint();
        }
    }

    


    class MyJPanel extends JPanel {
        MyJPanel(){} // default, just to have "paint" dependent

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D)g;


            g.setColor(Color.black);
            g2d.drawRect(clock_x, clock_y, clock_width, clock_height);
            Font fontClock = new Font("Courier", Font.PLAIN, 130);
            g.setFont(fontClock);
            g.drawString(Long.toString(gameClock.displayTime()), clock_x, clock_y + clock_height);
            
            //enemy area
            //g.setColor(Color.black);
            //g2d.drawRect(enemy_x, enemy_y, enemy_width, enemy_height);
            if(enemyImage != null) {
                g2d.drawImage(enemyImage,
                    enemy_x, enemy_y, enemy_width+enemy_x, enemy_height+enemy_y,
                    0, 0, enemyImage.getWidth(null),
                    enemyImage.getHeight(null), null);             
            } else {
                System.out.println("Enemy image is null");
            }
            
            //draw targets
            for(target t : targets) {
                g.setColor(t.getColor());
                Ellipse2D.Double targ = new Ellipse2D.Double(t.get_x(), t.get_y(), t.get_diam(), t.get_diam());
                g2d.fill(targ);
                //g.drawRect(t.get_x(), t.get_y(), t.get_diam(), t.get_diam());
                Ellipse2D.Double hitCircle = new Ellipse2D.Double(t.get_x() - target_radius / 2, t.get_y() - target_radius / 2, t.get_diam() + target_radius, t.get_diam() + target_radius);
                g2d.draw(hitCircle);
            }

            //draw bullet holes
            for(bulletHole b : bullets) {
                //translate circle so that it
                //displays at correct coordinates
                //double translation = Math.sqrt(2 * (Math.pow(bullet_diam / 2, 2)));
                double translation = bullet_diam / 2;
                g.setColor(bulletHole);
                Ellipse2D.Double hole = new Ellipse2D.Double(b.get_x() - translation, b.get_y() - translation, b.get_diam(), b.get_diam());
                g2d.fill(hole);
            }

            //end of game
            //... or next round
            switch(end_round()) {
                case(1):
                    g.setColor(Color.black);
                    g.setFont(new Font("Narkisim", Font.PLAIN, 150));
                    current_round++;
                    g.drawString("Round "+current_round, 253, 350);
                    refreshAll(false);
                    break;
                
                case(2):
                    g.setColor(Color.black);
                    g.setFont(new Font("Narkisim", Font.PLAIN, 135));
                    g.drawString("You Lose!", 240, 350);
                    
                    g.setColor(Color.black);
                    Font fontPlayAgain = new Font("Mool Boran", Font.PLAIN, 20);
                    g.setFont(fontPlayAgain);
                    g.drawString("Right Click to play again!", 400, 400);
                    //show score
                    //then prompt to replay
                    break;
                    
                default:
                    break;
            }
        }

    } // end MyJPanel
    
    
    //**//
    private class bulletHole {
        //graphics for bullethole
        int center_x, center_y, center_diam;
        Color color;

        bulletHole(int x, int y, int diam) {
            center_x = x;
            center_y = y;
            center_diam = diam;
        }

        int get_x() {
            return center_x;
        }

        int get_y() {
            return center_y;
        }

        int get_diam() {
            return center_diam;
        }
    }//end class bulletHole

    //**//
    private class target {
        //graphics for targets
        int center_x, center_y, center_diam;
        int num_rings;
        Color color;
        boolean is_hit;


        target(int cx, int cy, int cd, Color c) {
            center_x = cx;
            center_y = cy;
            center_diam = cd;
            color = c;
            is_hit = false;
        }

        //must subtract target_radius due to
        //nature of circle drawing
        private int get_x() {
            return center_x;
        }

        private int get_y() {
            return center_y;
        }

        private int get_diam() {
            return center_diam;
        }

        private Color getColor() {
            return color;
        }

        private void setColor(Color c) {
            color = c;
        }

        private boolean is_hit() {
            return is_hit;
        }

        private void hit() {
            is_hit = true;
        }
    }//end class target
    

    
    private static void splash(int sleep_duration)
    {
        // the splash screen object is created by the JVM, if it is displaying a splash image
        
        mySplash = SplashScreen.getSplashScreen();
        // if there are any problems displaying the splash image
        // the call to getSplashScreen will returned null

        if (mySplash != null)
        {
            // get the size of the image now being displayed
            Dimension ssDim = mySplash.getSize();
            int height = ssDim.height;
            int width = ssDim.width;

            // stake out some area for our status information
            splashTextArea = new Rectangle2D.Double(0, height*.11, width, height*.25);
           

            // create the Graphics environment for drawing status info
            splashGraphics = mySplash.createGraphics();
            font = new Font("Courier", Font.PLAIN, 100);
            splashGraphics.setFont(font);

            // initialize the status info
            splashText("R U RDY?");
            try {
                Thread.sleep(sleep_duration);
            } catch(InterruptedException ex) {
                //do nothing
            }
            
            
        }
    }
    /**
     * Display text in status area of Splash.  Note: no validation it will fit.
     * @param str - text to be displayed
     */
    public static void splashText(String str)
    {
        if (mySplash != null && mySplash.isVisible())
        {   // important to check here so no other methods need to know if there
            // really is a Splash being displayed

            // erase the last status text
            splashGraphics.setPaint(Color.LIGHT_GRAY);
            //splashGraphics.fill(splashTextArea);

            // draw the text
            splashGraphics.setPaint(Color.RED);
            splashGraphics.drawString(str, (int)(splashTextArea.getX() + splashTextArea.getWidth() * .30),(int)(splashTextArea.getY() + 15));

            // make sure it's displayed
            mySplash.update();
        }
    }
    

    TargetingSystem() {
        
        //show splash screen
        splash(100);
        
        //clock
        gameClock = new Clock();
        t = new Timer(1, updateClock);
        t.start();
        current_duration = System.currentTimeMillis() + initial_round_duration;
        
        //start application
        setTitle("Targeting System");
        setSize(win_x,win_y);
        setBackground(Color.white);
        setForeground(Color.black);

        theDesktop = new JDesktopPane();
        getContentPane().add(theDesktop);
        Container container = getContentPane();
        panel = new MyJPanel();
        container.add(panel, BorderLayout.CENTER);
        panel.addMouseListener (new mousePressHandler());
        //frame.setVisible(true);
        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
              System.exit(0);
            }
        });
       
        //container.add(enemy_panel, BorderLayout.CENTER);
        setVisible(true);

        //initialize targets
        refreshAll(true);



    }
    
    
    private BufferedImage loadImage(int enemy_num) {
        String imgFileName = "enemyImages/enemy"+enemy_num+".png";
        URL url = TargetingSystem.class.getResource(imgFileName);
        BufferedImage img = null;
        try {
            img =  ImageIO.read(url);
        } catch (Exception e) {
        }
        System.out.println("Successfully loaded "+imgFileName);
        enemies_seen.add(enemy_num);
        return img;
    }
    
    
    void set_current_duration(long dur) {
        current_duration = dur;
        if(dur < 2000)
            current_duration = 2000;
    }
    
    private boolean successful_hit(int shot_x, int shot_y, int t_x, int t_y)
    {
        int targ_x = t_x + target_radius;
        int targ_y = t_y + target_radius;
        double distance = Math.sqrt(Math.pow(shot_x - targ_x, 2) + Math.pow(shot_y - targ_y, 2));

        if(distance <= .5 * (target_diam + target_radius))
            return true;
        return false;
    }




    private void refreshAll(boolean start_over) {
        bullets.clear();
        targets.clear();
        
        //load random enemy image
        //probably the worst way to do this...
        if(enemies_seen.size() < num_enemies) {
            int currentEnemy = randInt(1, num_enemies);
            while(Arrays.asList(enemies_seen).contains(currentEnemy)) {
                currentEnemy = randInt(1, num_enemies);
                System.out.println("I already seen dis monster doe");
            }
            enemyImage = loadImage(currentEnemy);
            //enemies_seen.add(currentEnemy);
            System.out.println(enemies_seen.size()+" < "+num_enemies);            
        } else {
            enemyImage = null;
            System.out.println(enemies_seen.size()+" >= "+num_enemies);
        }
        
        
        //creates new UNHIT targets
        for(int i=0; i<3; i++) {
            targets.add(new target(randInt(enemy_x, enemy_x+enemy_width),
                                   randInt(enemy_y, enemy_y+enemy_height), target_diam, targetColor));
        }
        round_duration -= 1000;
        set_current_duration(System.currentTimeMillis() + round_duration);
        if(start_over) {
            enemies_seen.clear();
            current_round = 0;
            round_duration = initial_round_duration;
            set_current_duration(System.currentTimeMillis() + initial_round_duration);
        }

    }


    private int end_round() {
        //different types of end_round
        //0 = still playing current round
        //1 = new round
        //2 = game over
        if(gameClock.displayTime() <= 0) {
            return 2;
        }
        
        for(target t : targets) {
            if(!t.is_hit()) return 0;
        }
        
        return 1;
    }
    
        //for future's sake, this shouldn't really
    //be inside of the class definition
    public static int randInt(int min, int max) {

        // NOTE: Usually this should be a field rather than a method
        // variable so that it is not re-seeded every call.
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }
    

        
    
    public static void main(String args[]) {
        new TargetingSystem();
    }

    
    
    
}

