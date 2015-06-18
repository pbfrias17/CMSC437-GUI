/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaswingapps;

import java.util.*;
import java.util.GregorianCalendar;
import javax.swing.Timer;
import java.awt.event.*;

/**
 *
 * @author Paolo
 */
public class Clock {
        long current_time = System.currentTimeMillis();
        GregorianCalendar cal = new GregorianCalendar();
        
        Clock() {
            System.out.println("MADE A CLOCK!");
        }

        
        void setTime(long time) {
            current_time = time;
            System.out.println("time = "+time);

        }
        
        long displayTime() {
            System.out.println("Returning time...");
            return current_time;
            
        }

    }