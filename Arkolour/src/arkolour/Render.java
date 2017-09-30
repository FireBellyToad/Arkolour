/*
 * Arkolour
 * Classe che si occupa del rendering
 */
package arkolour;


import java.awt.*;
import java.awt.event.*;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.image.BufferStrategy;

/**
 *
 * Jacopo "Faust" Buttiglieri
 */
public class Render extends Frame{

    private Finestra evt = new Finestra();  //Classe della finestra e relativi comandi
    private State livello;                  //Classe che gestisce i livelli
    private BufferStrategy bs;              //Buffer per il disegno della grafic
    
    private Image tube_hor=Toolkit.getDefaultToolkit().getImage("img/screen/border_h.gif");
    private Image tube_ver=Toolkit.getDefaultToolkit().getImage("img/screen/border_v.gif");
    public Image circle=Toolkit.getDefaultToolkit().getImage("img/screen/circle.gif");
    public int activePower=-1;
    public static Image scorePlate;
    public boolean antialias=true;

   //Costruttore della finestra. Aggiunge il listener e seleziona il livello da
   //renderizzare
   Render(State livello,Listener list){
        setBounds(0,0,550,600);
        setVisible(true);
        addWindowListener(evt);
        addKeyListener(list);
        setLevel(livello);
        createBufferStrategy(2);
        bs = getBufferStrategy();
        this.setBackground(Color.BLACK);
        setIgnoreRepaint(true);
        
    }
    
   //Classe che contiene le procedure dei comandi della finestra
    class Finestra extends WindowAdapter {
         //Metodo invocato quando la finestra è chiusa
        @Override public void windowClosed(WindowEvent evt){           
            Global.closing();
            System.exit(0);
        }

        //Metodo invocato quando è inoltrata una richiesta di chiusura
        @Override public void windowClosing(WindowEvent evt)
        {
            
            evt.getWindow().dispose();
            System.out.println(evt);
        }       
    }
    
    public void setLevel(State livello){
        this.livello=livello;
    }
    
    //Procedura per disegno di punteggio e vite
    public void screen(Graphics2D g){
                
        //Disegno del rettangolo che mostra il colore della palla 
        g.setColor(Global.palla.getColor());
        g.fillRect(560,435,130,125);
        
        //Disegno sfondo del tabellone delle statistiche
        g.drawImage(scorePlate,545,50,165,525,null);
        
        //Scrittura delle statistiche (Punteggi, livello attuale, vita, potere 
        //attivo e colore della palla)
        g.setColor(Color.white);
        g.drawString("Record: " + Global.highscore, 550, 100);
        g.drawString("Punti: " + Global.score, 550, 175);
        g.drawString("Livello: "+livello.getID(), 550, 230);
        g.drawString("Potere: ", 550, 300);
        if((activePower!=-1)&&(activePower!=4))
            g.drawImage(Global.powers[activePower],625,280,null);
        g.drawString("Vite: " + Global.lives, 550, 365);
        g.drawString("Colore: ", 550, 425);

        //Disegno del bordo intorno al livello
        //Disegno del bordo verticale
        for(int i =0;i<105;i++){
            g.drawImage(tube_ver,13,50+(i*5),null); 
            g.drawImage(tube_ver,525,50+(i*5),null); 
        }
        //Disegno del bordo orizzontale
        for(int i =0;i<100;i++){
            g.drawImage(tube_hor,25+(i*5),40,null); 
            g.drawImage(tube_hor,25+(i*5),575,null); 
        }
        //Disegno dei 4 angoli del bordo
        g.drawImage(circle,5,30,null);        
        g.drawImage(circle,515,30,null);        
        g.drawImage(circle,5,562,null);     
        g.drawImage(circle,515,562,null);        

    }
    
    //Procedura disegno grafica del gioco. Viene utilizzato un buffer 
    //per evitare lo sfarfallio dato dal repaint()
    public void render(){
        
        Graphics2D g=null;
   
        //Se il frame è visibile, instanzia il buffer
        if(this.isVisible()){
            //Buffer per il disegno di grafica 2D
            g= (Graphics2D) bs.getDrawGraphics();
            
        }
        
        if(g!=null){
            
                     
            g.setFont(Global.font);            
            if(antialias)
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            
                    
            g.setColor(Color.BLACK);
            g.fillRect(0,0,725,600);
        
            //Disegno del livello e degli oggetti nel buffer
            livello.render(g);                         
            //Disegno dei punteggi, 
            screen(g);

           //Se il gioco è in pausa, lo notifica con testo a schermo
           if(Global.pause){
                g.setColor(Color.ORANGE);
                g.fillRect(200, 275, 150, 50);
                g.setColor(Color.black);
                g.drawRect(200, 275, 150, 50);
                g.drawString("GIOCO IN PAUSA", 212, 305);
                g.drawString("Premi S per salvare il gioco", 180, 405);
                if(livello.gameSaved)
                    g.drawString("Gioco salvato!", 220, 455);
                
            }
           
           //Disegna a schermo il contenuto del buffer
           bs.show();
           g.dispose();
           g=null;
        }
    }
}

