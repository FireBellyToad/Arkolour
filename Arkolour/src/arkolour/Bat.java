/*
 * Arkolour
 * Classe della sbarra
 */
package arkolour;


import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.Image;
import java.awt.Toolkit;

/*
 *
 * Jacopo "Faust" Buttiglieri
 */
public class Bat {
    
    private int x=250;
    private int y=550;
    private int w=50;
    private int h=15;
    private int speed=0;
    //Flag di controllo: se è vero, è attivo l'effetto del bonus M (non viene
    // controllato il colore della sbarra per il rimbalzo della palla)
    public boolean checkColor=true;
    private Color color=Color.RED;
    //Flag di controllo: se è vero, non si può mettere in pausa il gioco
    private boolean stopPause=false;
    private Image bat[]=new Image[2]; 
    //Flag di controllo: è vero se si è sotto l'effetto del bonus L
    private int isLarge=0;
    //Temporizzatore dell'animazione
    public int animTime=0;
    //Temporizzatore dei vari poteri(-1 se nulla è attivo)
    public int time=-1;  
    //Flag di controllo: è vero se si è sotto l'effetto del malus C
    public boolean scrambled=false;  
    //Frame dell'animazione
    public int frame=0;
    
    Bat(){
        bat[0]= Toolkit.getDefaultToolkit().getImage("img/bats/bat.gif");
        bat[1]= Toolkit.getDefaultToolkit().getImage("img/bats/batlarge.gif");
    }
    
    //Procedura per la gestione dell'input da tastiera
    public void events(KeyEvent e,boolean keyIsPressed){
        
        //Se un tasto è premuto, controlla che sia uno dei due tasti freccia
        //(destra o sinistra) per il movimento, od uno dei tasti A,S e D per
        //la scelta del colore della sbarra
        if(keyIsPressed)
        {
            switch(e.getKeyCode()){
                case KeyEvent.VK_RIGHT:{
                    //Muove a destra la sbarra
                    speed= 4;
                    break;
                }
                case KeyEvent.VK_LEFT:{
                    //Muove a sinistra la sbarra
                    speed =-4;
                    break;
                }
                 case KeyEvent.VK_A:{
                     if(!scrambled)
                        //Cambia colore della sbarra in rosso
                        setColor(Color.red);
                     else
                        //Cambia colore della sbarra in giallo
                        setColor(Color.yellow);
                    break;
                }
                 case KeyEvent.VK_S:{
                     if(!stopPause){
                         if(!scrambled)
                            //Cambia colore della sbarra in giallo
                            setColor(Color.yellow);
                         else
                            //Cambia colore della sbarra in blu
                            setColor(Color.blue);
                     }
                    break;
                 }
                 case KeyEvent.VK_D:{
                     if(!scrambled)
                        //Cambia colore della sbarra in blu
                        setColor(Color.blue);
                     else
                        //Cambia colore della sbarra in rosso
                        setColor(Color.red);
                    break;
                 }
                 case KeyEvent.VK_P:{
                     //Mette in pausa il gioco. il flag stopPause evita che 
                     //si esca e si entri ripetutamente dalla pausa
                     if(!stopPause){
                        Global.livello.gameSaved=false;
                        Global.pause=! Global.pause;
                        stopPause=true;
                     }
                    break;
                 }

            }
        }
        else
            //Caso in cui uno di questi tre tasti è rilasciato
            switch(e.getKeyCode()){
                case KeyEvent.VK_RIGHT:{
                    //Ferma il movimento della sbarra
                    speed= 0;
                    break;
                }
                case KeyEvent.VK_LEFT:{
                    //Ferma il movimento della sbarra
                    speed =0;
                    break;
                }
                case KeyEvent.VK_P:{
                    //Permette di togliere lo stato di pausa del gioco
                    stopPause=false;
                    break;
                }
            }
        
    }
    //Procedura per gestione della logica del gioco
    //(Collisioni,Movimento,Punteggio...)
    public void logic(){
        
        //Se la sbarra è all'interno della schermata del livello, si muove normalmente
        //Altrimenti viene bloccata dai bordi della schermata
        if((x>25)&&(x+w<525))
            x+=speed;
        else
        {
            if(x<=25)
                x=26;
            if(x+w>=525)
                x=524-w;
            speed=0;
        }
        
        if(time==0){
            time=-1;
            gotPower(10);
        }
        else
            if(time>0)
                time--;
                
    }
    
    //Procedure per ottenere il colore, le coordinate, la lunghezze e l'altezza
    public Color getColor(){
        return color;
    }
    
    public int getX(){
        return x;
    }
    
    public int getW(){
        return w;
    }
        
    public int getY(){
        return y;
    }
    
    public int getH(){
        return h;
    }
    
    //Procedura per settare il colore
    public void setColor(Color color){
        this.color=color;
    }
    
    
    //Procedura per assegnare il giusto potere alla sbarra. Ogni volta che viene
    //chiamata, si annulla il potere posseduto precedentemente
    public void gotPower(int power){
        
       Global.finestra.activePower=power;
       
       switch(power){
            default:{
                //Nessun potere ottenuto relativo alla sbarra 
                w=50;
                checkColor=true;                
                isLarge=0;                
                time=-1;
                scrambled=false;                  
                //Controllo per evitare che in caso di un potere valido ma non 
                //appartenente alla sbarra venga sovrascritta la variabile
                if((power<0)||(power>6))
                    Global.finestra.activePower=-1;
                break;
            }
            case 0:{
                //Allungamento della sbarra
                isLarge=1;
                if((x>26)&&(x+w<524)){
                    x-=25;
                    w=100;
                }
                else{
                    if(x+w==524)
                        x-=50;
                    w=100;        
                    isLarge=0;
                }
                                  
                checkColor=true;                
                time=-1;                
                Global.livello.createParticles((int) (Math.random()*3)+10, x+25, y+8,Color.BLUE , 25);
                Global.livello.createParticles((int) (Math.random()*3)+10, x+25, y+8,Color.YELLOW , 25);
                Global.livello.createParticles((int) (Math.random()*3)+10, x+25, y+8,Color.RED, 25);
                break;
            }
            case 3:{
                //Allungamento della sbarra
                w=50;
                checkColor=false;
                time=1200;
                isLarge=0;
                Global.livello.createParticles((int) (Math.random()*3)+10, x+25, y+8,Color.BLUE , 25);
                Global.livello.createParticles((int) (Math.random()*3)+10, x+25, y+8,Color.YELLOW , 25);
                Global.livello.createParticles((int) (Math.random()*3)+10, x+25, y+8,Color.RED, 25);
                break;                
            }
            case 4:{
                //Perdita di una vita
                w=50;
                Global.palla.restart();
                restart();              
                Global.lives--;
                isLarge=0;                  
                time=-1;
                checkColor=true;                
                break;
            }
            case 6:{
                //Tasti per colorare la sbarra scambiati
                scrambled=true;
                time=600;
                w=50;
                checkColor=true;
                isLarge=0;                
                Global.livello.createParticles((int) (Math.random()*3)+10, x+25, y+8,Color.black , 25);
                break;
            }
        }
        
        
    }
    
    //Resetta le coordinate, la velocità ed i poteri della palla
    public void restart(){
        x=250;
        y=550;
        speed=0;
        gotPower(100);
    }
    
    
    public void drawBat(Graphics2D g)
    {
        //Disegno della sbarra e del suo sfondo
        
        //Se ha il potere M (ID=3) che attiva il flag checkColor, cambia colore
        //ripetutamente
        if(!checkColor)
        {
           if(animTime==0){
               switch(frame){
                       case 0:{
                           color=Color.RED;
                           frame++;
                           break;
                       }   
                       case 1:{
                           color=Color.YELLOW;
                           frame++;
                           break;
                       } 
                       case 2:{
                           color=Color.BLUE;                           
                           frame=0;
                           break;
                       }
                   }
               animTime=2;
               
           }
           else
               animTime--;
        }
        g.setColor(color);
        g.fillRoundRect(x, y, w, h, 10, 10);   
        g.drawImage(bat[isLarge], x, y,null);
    }
}
