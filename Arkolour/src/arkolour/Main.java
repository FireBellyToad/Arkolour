/*
 * Arkolour
 * File con funzione di main()
 */
package arkolour;



/**
 *
 * Jacopo "Faust" Buttiglieri
 */
public class Main {
    
public static boolean done=false;
        
    
    public static void main(String[] args){
       
        Global.livello = new State(0);
        Global.tasti = new Listener();
        Global.finestra = new Render(Global.livello,Global.tasti);
        
        done=Global.loadFiles();
        
        while(!done)
        {

            if(Global.finestra!=null)
                Global.finestra.render(); 
            if(Global.livello!=null){
                Global.livello.logic();
            }
            try{
                Thread.sleep(1000/60);
            }
            catch(InterruptedException e){
            }                
        }
        
        Global.closing();
        System.exit(0);
    }
}
    
  

    
