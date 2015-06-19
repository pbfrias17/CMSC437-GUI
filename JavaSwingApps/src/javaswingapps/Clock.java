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

/**
 *
 * @author Paolo
 */
public class Clock {
        String current_time;
        GregorianCalendar cal = new GregorianCalendar();

        
        Clock() {

        }

        
        void setTime(String time) {
            current_time = time;
        }
        
        String displayTime() {
            return current_time;
            
        }

    }