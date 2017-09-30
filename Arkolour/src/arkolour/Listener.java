/*
 * Arkolour
 * Classe Listener per gestione eventi da tastiera
 */
package arkolour;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
/**
 *
 * Jacopo "Faust" Buttiglieri
 */
public class Listener implements KeyListener {
    
    public int keypressed;
    Listener(){
    }
    
    public void keyPressed(KeyEvent event){
        if(Global.livello.menu!=null)
            Global.livello.menu.events(event, true);
        if(Global.livello!=null)
            Global.livello.events(event, true);
        
        Global.sbarra.events(event,true);
    }
    
    public void keyReleased(KeyEvent event){  
        if(Global.livello.menu!=null)
            Global.livello.menu.events(event, false);
        
            Global.sbarra.events(event,false);
    }
    
    public void keyTyped(KeyEvent event){
        Global.sbarra.events(event,false);
    }
    
}
