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
import java.io.*;
import javax.swing.*;

public class TargetingSystem extends JFrame
{
  MyJPanel panel;
  private JDesktopPane theDesktop;
  int x, y, b;
  int win_x = 1000;
  int win_y = 1050;
  int r1x1 = 100;  // red rectangle
  int r1y1 = 120;
  int r1x2 = 200;
  int r1y2= 250;
  int r2x1 = 180;  // blue rectangle
  int r2y1 = 220;
  int r2x2 = 300;
  int r2y2= 350;
  int enemy_x = 250;
  int enemy_y = 50;
  int enemy_width = win_x - (2 * enemy_x);
  int enemy_height = 2 * enemy_x;
  int bullet_diam = 20;
  int target_diam = 40;
  int target_radius = target_diam / 2;
  int max_distance = target_diam;
  
  Color color1 = Color.red;
  Color color2 = Color.blue;
 
  Color targetColor = Color.green;
  Color targetHitColor = Color.red;
  Color bulletHole = Color.blue;
  List<target> targets = new ArrayList<>();
  List<bulletHole> bullets = new ArrayList<>();
  

  TargetingSystem()
  {
    
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
    setVisible(true);
    
    //initialize targets
    for(int i=0; i<3; i++) {
        targets.add(new target(randInt(enemy_x, enemy_x+enemy_width),
                               randInt(enemy_y, enemy_y+enemy_height), target_diam, targetColor));
    }


    
  }

  class mousePressHandler extends MouseAdapter
  {
    public void mousePressed (MouseEvent e)
    {
      x = e.getX();
      y = e.getY();
      b = e.getButton();
      
      //just for example purposes
      if(b == e.BUTTON3) {
          refreshAll();
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
      //System.out.println("button "+b+" at x="+x+" y="+y); // debug print
      // panel.repaint();
      repaint();
    }
  }

  private boolean successful_hit(int shot_x, int shot_y, int t_x, int t_y)
  {
      //is shot coordinate inside hitbox?
      /*if(shot_x < t_x || shot_x > (t_x + target_diam))
          return false;
      if(shot_y < t_y || shot_y > (t_y + target_diam))
          return false;
   
      return true;*/
      
      int targ_x = t_x + target_radius;
      int targ_y = t_y + target_radius;
      double distance = Math.sqrt(Math.pow(shot_x - targ_x, 2) + Math.pow(shot_y - targ_y, 2));
      System.out.println("distance = "+distance); // debug print

      if(distance <= .5 * (target_diam + target_radius))
          return true;
      return false;
      //double distance = 0;
  }
  
  
    
  
  private void refreshAll() {
    bullets.clear();
    targets.clear();
    for(int i=0; i<3; i++) {
        //creates new UNHIT targets
        targets.add(new target(randInt(enemy_x, enemy_x+enemy_width),
                               randInt(enemy_y, enemy_y+enemy_height), target_diam, targetColor));
    }
  
  }
  
  
  private int game_over() {
      //different types of game over
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





  class MyJPanel extends JPanel
  {
    MyJPanel(){} // default, just to have "paint" dependent

   public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D)g;
    
    //enemy area
    g.setColor(Color.black);
    g2d.drawRect(enemy_x, enemy_y, enemy_width, enemy_height);
    
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
    
    if(game_over() !=  0) {
        g.setColor(Color.black);
        g.drawString("Game Over", 20, 50);
    }
   }
    
  } // end MyJPanel

  public static void main(String args[])
  {
    new TargetingSystem();
  }
  
  
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
  
  
  //**//
  private class enemy {
      //graphics for enemies
  }//end class enemy

}

