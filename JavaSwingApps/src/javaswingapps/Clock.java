/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaswingapps;

import java.util.GregorianCalendar;
import javax.swing.Timer;
import java.awt.event.*;

/**
 *
 * @author Paolo
 */
public class Clock {
        double t1, t2;
        double waste = 0.0;
        Timer t;
        String current_time;
        
        Clock() {
            Timer timer = new Timer(1000, new MyTimerActionListener());

            timer.start();
            try {
              Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            timer.stop();
        }

        
        class MyTimerActionListener implements ActionListener {
            public void actionPerformed(ActionEvent e) {

                System.out.println("asdf");

            }
        }

    }