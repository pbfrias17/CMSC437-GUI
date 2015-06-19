/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaswingapps;

import java.util.*;
import java.text.*;
import java.util.GregorianCalendar;
import javax.swing.Timer;
import java.awt.event.*;
import java.awt.GraphicsEnvironment;

/**
 *
 * @author Paolo
 */
public class Clock {
        String current_time;
        GregorianCalendar cal = new GregorianCalendar();
        Long duration;

        
        Clock() {
/*
            String fonts[] = 
                GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

            for ( int i = 0; i < fonts.length; i++ )
            {
              System.out.println(fonts[i]);
            }*/
        }

            
          

        
        void setTime(long millis) {
            duration = millis;
        }
        
        Long displayTime() {
            return duration;
            
        }

    }