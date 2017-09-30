/*
 * Arkolour
 * Classe del menu principale
 */
package arkolour;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.*;
import java.awt.event.KeyEvent;

/**
 *
 * Jacopo "Faust" Buttiglieri
 */
public class Menu {
    
    private String commands[]={"Nuova partita",
                                "Continua partita",
                                "Opzioni",
                                "Esci"};
    
    private String options[]={  "Bordi sfumati",
                                "Effetti particellari",
                                "Suoni",
                                "Musica",
                                "Salva",
                                "Esci"};
    public int choice=0;
    private boolean stopPressing=false;
    private boolean isOption=false;
    private boolean startGame =false;    
    private boolean loadGame =false;
    private boolean optionsValues[]={true,true,true,true};
    public Image title=Toolkit.getDefaultToolkit().getImage("img/backgrounds/title.gif");
    
    
    public void events(KeyEvent e,boolean keyIsPressed){
        //Se un tasto è premuto, controlla che sia uno dei due tasti freccia
        //(destra o sinistra) per il movimento, od uno dei tasti A,S e D per
        //la scelta del colore della sbarra
        if(keyIsPressed)
        {
            switch(e.getKeyCode()){
                case KeyEvent.VK_DOWN:{
                    //Muove la freccia di selezione 
                    if(!stopPressing){
                        if(((choice<3)&&(!isOption))||((choice<5)&&(isOption)))
                            choice++;
                        else
                            choice=0;
                        stopPressing=true;
                    }
                    break;
                }
                case KeyEvent.VK_UP:{
                    //Muove la freccia di selezione  
                    if(!stopPressing){
                          if(choice>0)
                            choice--;
                        else
                          if(!isOption)
                            choice=3;
                          else
                            choice=5;   
                    }
                    break;
                }
                case KeyEvent.VK_ENTER:{
                    execChoice();
                    break;
                }
            }
        }
        else{
            stopPressing=false;
        }
    }
    
    public void execChoice(){
        
        if((isOption)&&(choice<4))
            optionsValues[choice]=!optionsValues[choice];
        
        switch(choice){
            case 0:{
                //Nuova partita
                if(!isOption){                    
                    startGame=true;
                }
                break;
            }
            case 1:{
                //Carica la partita
                if(!isOption){                    
                    loadGame=true;
                }
                break;
            }
            case 2:{
                if(!isOption){
                    //Menu opzioni
                    isOption=true;
                    choice=0;
                }                       
                break;
             }
            case 3:{
                if(!isOption){
                    //Uscita del gioco
                    Global.closing();
                    System.exit(0);
                } 
                break;
            }
            case 4:{
                //Salvataggio opzioni
                
                Global.finestra.antialias=optionsValues[0];
                State.particles=optionsValues[1];
                Global.soundsOn=optionsValues[2];                
                Global.musicOn=optionsValues[3];
                break;
             } 
            case 5:{
                //Uscita dal menu opzioni
                isOption=false;  
                choice=0;
                break;
             }  
        }
        
    }
    
    //Disegna a schermo il menu
    public void drawMenu(Graphics g){
                    
            g.drawImage(title,65,110,null);     
            g.drawString("di Jacopo Buttiglieri",180,180);
            
        
            if(!isOption)
                for(int i=0;i<commands.length;i++)
                    g.drawString(commands[i],220,250+(i*30));
            else
                for(int i=0;i<options.length;i++){
                    if(i<4){
                        if(optionsValues[i]==true)
                            g.drawString(options[i]+": Si",220,250+(i*30));
                        else
                            g.drawString(options[i]+": No",220,250+(i*30));                            
                    }
                    else{                        
                        g.drawString(options[i],220,250+(i*30));
                    }
                }
                 
            g.setColor(Color.white);
            g.drawString("Musica: Kevin MacLeod",40,570);      
            g.setColor(Color.red);
            g.fillOval(195,240+(choice*30),10,10);        
            g.setColor(Color.black);
            g.drawOval(195,240+(choice*30),10,10);  
    }
            
    
    //Procedura che gestisce la logica del menu
    public void logic(){
        
        Global.audio.playMusic();
        if(startGame)
            Global.change(1,true);
        if(loadGame)
            load();
    }
    
    //Procedura per caricare il gioco precedente
    public void load(){  
        int livello=0;
       //Lettura del file di livello
                DataInputStream in;
                try{
                    in=new  DataInputStream(new FileInputStream("savegame.sav"));
                    Global.lives=in.readInt();
                    in.read();
                    Global.score=in.readInt();
                    in.read();                    
                    livello=in.readInt();
                    in.read();
                    Global.highscore=in.readInt();
                    in.read();
                    in.close();
                }
                catch(Exception e){}
                
                Global.change(livello,true);
    }
}

