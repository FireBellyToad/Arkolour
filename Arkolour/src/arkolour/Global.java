/*
 * Arkolour
 * File di oggetti e variabili globali
 */
package arkolour;

import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.JOptionPane;
import java.awt.Font;
import java.io.*;

/**
 *
 * Jacopo "Faust" Buttiglieri
 */
public class Global {
    
    public static State livello;   //Classe che gestisce i livelli del gioco
    public static Render finestra; //Classe che si occupa del rendering della finestra e del gioco
    public static Listener tasti;    
    public static Bat sbarra; 
    public static Ball palla=new Ball();
    public static Powers potere;
    public static Audio audio=new Audio();
    public static Font font;
    
    public final static int screenEnd=565;
    public final static int screenStart=50;
    public final static int ballRadius=5;
        
    public static boolean pause=false;  
    public static boolean soundsOn=true;
    public static boolean musicOn=true; 
     
    public static int score=0;
    public static int highscore=250000;
    public static int lives=10;
    public static Image bricks[][]=new Image[7][3];
    public static Image powers[]=new Image[9];
    

      /*
     * Procedura per cambio di livello. Viene controllato se non ci sono rimasti
     * blocchi attivi. Se il risultato è positivo, viene cambiato il livello in 
     * base all'ID
     */
    public static void change(int newID,boolean forceChange){        
        if(Global.livello.levelIsFinished(0)||(forceChange)){
            Global.livello = null;  
            
            if(newID!=0){
                Global.livello = new State(newID);
            }
            else{
                Global.livello = new State(0);
                Global.livello.menu=new Menu();
            }
            
            Global.finestra.setLevel(livello);
            Global.potere=null;
            Global.palla.restart();
            Global.sbarra.restart(); 
        }
    }

    
    //Procedura che carica le immagini        
    public static boolean loadFiles(){     
        boolean done=false;
        try{
        for(int i=0;i<9;i++){
            if(i<3){
                Global.bricks[0][i]= Toolkit.getDefaultToolkit().createImage("img/bricks/redBrick"+(i+1)+".gif");
                Global.bricks[1][i]= Toolkit.getDefaultToolkit().createImage("img/bricks/yellowBrick"+(i+1)+".gif");
                Global.bricks[2][i]= Toolkit.getDefaultToolkit().createImage("img/bricks/blueBrick"+(i+1)+".gif");
                Global.bricks[3][i]= Toolkit.getDefaultToolkit().createImage("img/bricks/redBlueBrick"+(i+1)+".gif");
                Global.bricks[4][i]= Toolkit.getDefaultToolkit().createImage("img/bricks/redYellowBrick"+(i+1)+".gif");
                Global.bricks[5][i]= Toolkit.getDefaultToolkit().createImage("img/bricks/blueYellowBrick"+(i+1)+".gif");
                Global.bricks[6][i]= Toolkit.getDefaultToolkit().createImage("img/bricks/allBrick"+(i+1)+".gif");
            }
            Global.powers[i]=Toolkit.getDefaultToolkit().createImage("img/powers/power"+i+".gif");             
        }
        
        Global.audio.loadSounds();

        Render.scorePlate=Toolkit.getDefaultToolkit().createImage("img/backgrounds/scoreback.gif");
        
        font=Font.createFont(Font.TRUETYPE_FONT, new File("font.ttf"));
        font=font.deriveFont(14f);
        
        sbarra=new Bat();
        }
        catch (Exception e){        
            JOptionPane.showMessageDialog(Global.finestra, "Errore durante il caricamento dei file, reinstallare il gioco",
                    "Errore", JOptionPane.INFORMATION_MESSAGE);                    
            closing();
            done=true;
        }

       return done;
    }
    
    //Chiusura del gioco
    public static void closing(){
        Global.livello=null;
        Global.sbarra=null;
        Global.palla=null;
        Global.finestra=null;
    }
}


