/*
 * Arkolour
 * Classe degli stati macchina del gioco
 */
package arkolour;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.Image;
import java.io.*;
import java.awt.Toolkit;
import java.awt.Color;
/**
 *
 * Jacopo "Faust" Buttiglieri
 */
public class State{
    
    private int levelID;
    public static Bricks mattoni[]=new Bricks[100];
    public Particle part[]=new Particle[500];
    private Image back;
    private Boss boss;
    public Menu menu;
    //Flag che indica se i suoni sono attivi
    public static boolean sound=true;
    //Flag che indica se le musiche sono attive
    public static boolean music=true;
    //Flag che indica se le particelle sono attive
    public static  boolean particles=true;    
    
    //Flag di salvataggio
    public boolean saveGame=false;
    //Flag di salvataggio
    public boolean gameSaved=false;

    
    public void logic(){
        if((levelID>=1)&&(levelID<=5)){
            if(!Global.pause){
                //Logica della sbarra
                Global.sbarra.logic();
                //Logica della palla
                Global.palla.logic(Global.sbarra,mattoni,boss);
                //Logica del potere
                if(Global.potere!=null){
                    Global.potere.logic(Global.sbarra,Global.palla);
                    if((Global.potere.isCollided)||(Global.potere.isOutside()))
                       Global.potere=null;  
                }
                for(int i=0;i<500;i++){
                   if(part[i]!=null){
                      part[i].logic(); 
                      if(part[i].isDead()||part[i].isOutside())
                        part[i]=null;
                      }
                   }
                if(boss!=null){
                    boss.logic();
                }  
                if(Global.highscore<Global.score)
                    Global.highscore=Global.score;
            }
            else{
                
                if(saveGame){
                    //Scrittura su file
                    DataOutputStream out;
                    try{
                        out=new  DataOutputStream(new FileOutputStream("savegame.sav"));
                        for(int i=0;i<150;i++){
                            out.writeInt(Global.lives);
                            out.writeBytes("\t");
                            out.writeInt(Global.score);
                            out.writeBytes("\t");                    
                            out.writeInt(levelID);
                            out.writeBytes("\t");                                              
                            out.writeInt(Global.highscore);
                            out.writeBytes("\t");
                        }
                        out.close();
                        saveGame=false;
                        gameSaved=true;
                    }
                    catch(Exception e){}
                }
            }
        }
        else
            if(levelID==6){
                
                
                    
            }
            else
            menu.logic();

        
        //Se le vite sono meno di uno, il gioco termina
        if(Global.lives<1)
            System.exit(0);
    }
    
    public void events(KeyEvent e,boolean keyIsPressed){
        if((e.getKeyCode()==KeyEvent.VK_S)&&(keyIsPressed)){
            saveGame=true;
        }
        
        if((e.getKeyCode()==KeyEvent.VK_ENTER)&&(keyIsPressed)&&(levelID==6)){
           Global.change(0,true);
        }
    }

    //Funzione del Rendering
    public void render(Graphics2D g) {
        
        //Disegno sfondo del livello 
        g.drawImage(back, 25, 50,null);
        
        //Disegno dei vari oggetti del livello, se sono livelli di gioco
        if((levelID>=1)&&(levelID<=5)){
            for(int i=0;i<500;i++){
               if(part[i]!=null)
                   if(part[i].getLife()==-1)
                        part[i].drawPart(g);
           }
            //Disegno della palla e della sbarra
            Global.palla.drawBall(g);
            Global.sbarra.drawBat(g);            

            if(Global.potere!=null)
                Global.potere.drawPower(g);

            if(boss!=null){
                boss.drawBoss(g);    
            } 
                        
           for(int i=0;i<mattoni.length;i++){
               if(mattoni[i]!=null)
                    mattoni[i].drawBrick(g);
           }
           
           for(int i=0;i<500;i++){
               if(part[i]!=null)
                   if(part[i].getLife()!=-1)
                        part[i].drawPart(g);
           }
        }
        else{
            if(levelID==6)
            {                
                //Disegna la fine del gioco                
                g.setColor(Color.ORANGE);
                g.fillRect(180, 210, 230, 120);
                g.setColor(Color.black);
                g.drawRect(180, 210, 230, 120);
                g.drawString("Record: " + Global.highscore, 220, 250);
                g.drawString("Punti: " + Global.score, 220, 275);                
                g.drawString("PREMI ENTER PER USCIRE", 190, 300);
            }
            else                
            if(menu!=null){
                menu.drawMenu(g);
            }
       }
   }
    
    
    
    /* Costruttore del livello. L'argomento "levelID" è l'identificatore del 
     * livello, mentre "menuFlag" indica se il livello è in realtà il menù 
     * principale. Viene letto il file di livello in base all'ID dello stesso 
     */
    State(int levelID){
        this.levelID=levelID;

        /*Se il levelID inserito è quello di un livello di gioco ( il livello 0 è
         * il menù, il livello 7 è la fine del gioco), carica il livello e lo 
         * sfondo da utilizzare
         */
        switch(levelID){
                default:{
                    
                back= Toolkit.getDefaultToolkit().getImage("img/backgrounds/back1.jpg");
                String filename= "level"+levelID+".lev";
                int xx=0,yy,tipo=0,hard=1;

                //Lettura del file di livello
                DataInputStream in;
                try{
                    in=new  DataInputStream(new FileInputStream(filename));
                    for(int i=0;i<mattoni.length;i++){
                        if(i%10!=0)
                            xx+=50;
                        else
                            xx=25;
                        yy=75+(25*(int) Math.floor(i/10));  
                        tipo=in.readInt();
                        in.read();
                        hard=in.readInt();
                        in.read();
                        if(tipo!=-1)
                            mattoni[i]=new Bricks(xx,yy,tipo,hard);
                        else
                            mattoni[i]=null;
                    }
                    in.close();
                }
                catch(Exception e){}
                break;
            }
            case 0:{
                
                back= Toolkit.getDefaultToolkit().getImage("img/backgrounds/menu.jpg");
                menu=new Menu();
                break;
            }
            case 5:{
                //Instanzia il boss
                
                back= Toolkit.getDefaultToolkit().getImage("img/backgrounds/back5.jpg");
                boss=new Boss();
                break;
            }
            case 6:{
                //Fine del gioco
                
                back= Toolkit.getDefaultToolkit().getImage("img/backgrounds/end.jpg");
                break;
            }
        }
    }
                
    //Procedura per ottenere l'ID di un livello
    public int getID(){
        return levelID;           
    }
    
    /*Procedura per il controllo della distruzione di tutti i mattonicini. Cicla
     * ricorsivamente tutti i mattoncini. Se alla fine del controllo non trova 
     * nessua istanza della classe Bricks (ovvero non ci sono più mattoni) viene
     * restituito il valore booleano "vero". Altrimenti il controllo fallisce 
     * e viene restituito il valore booleano "falso". Nel caso invece il livello
     * sia quello del boss, il controllo viene effettuato sulla vita dello stesso:
     * se è uguale a zero, il boss muore e si finisce il gioco
    */
    public boolean levelIsFinished(int i){
    
        if((levelID!=0)&&(levelID<5 )){
            if(i>=mattoni.length)
                return (true);
            if(mattoni[i]!=null)
                return (false);        
            
            return(true&&levelIsFinished(i+1));
        }
        else{
            if(boss!=null&&boss.isDead())
                return (true);
        } 
        return false;
    }
    
   /* Procedura che crea gli effetti particellari. Gli argomenti sono rispettiva
    * mente il numero delle particelle create, la posizione iniziale, l'id dell'
    * immagine e la vita delle particelle
    */
    public void createParticles(int number,int x,int y,Color nameID,int life){
        int created=0,i=0;
        while((created<number)&&(i<500)&&(particles)){
            if(part[i]==null){
                if(life>=0)
                {
                    if(nameID==Color.yellow)
                       part[i]=new Particle("yellowstar",x,y,0,life,1);
                    if(nameID==Color.red)
                       part[i]=new Particle("redstar",x,y,0,life,1);
                    if(nameID==Color.blue)
                       part[i]=new Particle("bluestar",x,y,0,life,1);
                    if(nameID==Color.black)
                        if(boss==null)
                            part[i]=new Particle("blackgoo",x,y,-1,life,1);
                        else                            
                            part[i]=new Particle("blackgoo",x,y,-1,life,3);                       
                }
                else
                {
                    if(nameID==Color.yellow)
                       part[i]=new Particle("yellowsplat",x,y,1,life,1);
                    if(nameID==Color.red)
                       part[i]=new Particle("redsplat",x,y,1,life,1);
                    if(nameID==Color.blue)
                       part[i]=new Particle("bluesplat",x,y,1,life,1);
                }    
                    
                created++;
                i++;
            }
            else{
                i++;                
            }                
        }
    }
}