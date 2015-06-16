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
  int r1x1 = 100;  // red rectangle
  int r1y1 = 120;
  int r1x2 = 200;
  int r1y2= 250;
  int r2x1 = 180;  // blue rectangle
  int r2y1 = 220;
  int r2x2 = 300;
  int r2y2= 350;
  int enemy_x = 90;
  int enemy_y = 40;
  int enemy_width = 200;
  int enemy_height = 200;
  int target_diam = 10;
  int target_radius = target_diam / 2;
  
  Color color1 = Color.red;
  Color color2 = Color.blue;
 
  Color targetColor = Color.green;
  Color targetHit = Color.red;
  Color bulletHole = Color.blue;
  List<target> targets = new ArrayList<target>();
  

  TargetingSystem()
  {
    
    setTitle("Select2");
    setSize(400,450); // see "dimension" below for smaller size
    setBackground(Color.white);
    setForeground(Color.black);
    // set up plot area
    theDesktop = new JDesktopPane();
    getContentPane().add(theDesktop);
    // Create internal frame
    JInternalFrame frame = new JInternalFrame(
        "Plot area", true, true, true, true);
    // attach panel to internal frame
    Container container = frame.getContentPane();
    panel = new MyJPanel();
    container.add(panel, BorderLayout.CENTER);
    // set size of internal frame to size of its contents
    frame.pack(); // uses class Dimension
    theDesktop.add(frame);
    panel.addMouseListener (new mousePressHandler());
    frame.setVisible(true);
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
                               randInt(enemy_y, enemy_y+enemy_height), target_diam));
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
          refreshTargets();
      }
      if(inrect(x,y,r1x1,r1y1,r1x2,r1y2)) color1 = Color.green;
      else                                color1 = Color.red;
      if(inrect(x,y,r2x1,r2y1,r2x2,r2y2)) color2 = Color.green;
      else                                color2 = Color.blue;
      requestFocus();
      System.out.println("button "+b+" at x="+x+" y="+y); // debug print
      // panel.repaint();
      repaint();
    }
  }

  public boolean inrect(int x, int y, int x1, int y1, int x2, int y2)
  {
    if(x<x1 || x>x2) return false;
    if(y<y1 || y>y2) return false;
    return true;
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
    
    //Ellipse2D.Double circle3 = new Ellipse2D.Double(t.get_x(), t.get_y(), t.get_diam(), t.get_diam());
    //Ellipse2D.Double circle4 = new Ellipse2D.Double(t.get_x(), t.get_y(), t.get_diam(), t.get_diam());
    
    //draw targets (circles and rings)
    g.setColor(targetColor);
    for(target t : targets) {
        Ellipse2D.Double circle = new Ellipse2D.Double(t.get_x()-target_radius, t.get_y()-target_radius, t.get_diam(), t.get_diam());
        g2d.fill(circle); 
    }
   }
    
    
    // return dimensions for sizing
    public Dimension getPreferredSize()
    {
      return new Dimension(380, 380);
    }
  } // end MyJPanel

  public static void main(String args[])
  {
    new TargetingSystem();
  }
  
  private class bulletHole {
      //graphics for bullethole
  }
  
  class target {
      //graphics for targets
      int center_x, center_y, center_diam;
      int num_rings;
      
      
      target(int cx, int cy, int cd) {
        
        center_x = cx;
        center_y = cy;
        center_diam = cd;
      }
      
      public int get_x() {
          return center_x;
      }
      
      public int get_y() {
          return center_y;
      }
      
      public int get_diam() {
          return center_diam;
      }
      
  }
  
  private class enemy {
      //graphics for enemies
  }
  
  
  private void refreshTargets() {
    targets.clear();
    for(int i=0; i<3; i++) {
        targets.add(new target(randInt(enemy_x, enemy_x+enemy_width),
                               randInt(enemy_y, enemy_y+enemy_height), target_diam));
    }
  
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

}


