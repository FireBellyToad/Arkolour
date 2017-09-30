/*
 * Arkolour
 * Classe dei mattoni
 */
package arkolour;

import java.awt.Graphics;
import java.awt.Color;
/**
 *
 * Jacopo "Faust" Buttiglieri
 */
public class Bricks {
    
    private int x;
    private int y;
    private int w=50;
    private int h=25;
    private int type;
    public int hardness=0;
   
   //Costruttore
   Bricks(int x, int y, int type,int hardness){
       this.x=x;
       this.y=y;
       this.type=type;
       this.hardness=hardness;
   }
    
    //Procedura per il disegno del mattone -TEMP-
    public void drawBrick(Graphics g){
            g.drawImage(Global.bricks[type][hardness-1],x,y,null);
    }
    
    //Procedura che crea un potere casuale con una probabilità del 25%
    public void createPower(){
        int perc= (int) (Math.random()*100)+1;
        int ID= (int) (Math.random() *9) ;
        
        if(ID==8)
        {
           ID=8;
        }
        
        if((perc<=25)&&(Global.potere==null))
            Global.potere= new Powers(ID,x+w/2,y+h/2);
        
    }
    //Ottiene il colore del mattone in base al suo tipo. Se il tipo è misto,
    //Il colore è determinato casualmnente
    public Color getBrickColor(){
        
        switch(type){
            case 0:{
                return Color.red;
            }            
            case 1:{
                return Color.yellow;
            }            
            case 2:{
                return Color.blue;
            }
            case 3:{
                if((int) (Math.random()*2)+1 ==1)
                    return Color.red;
                else
                    return Color.blue;
            }
            case 4:{
                if((int) (Math.random()*2)+1 ==1)
                    return Color.red;
                else
                    return Color.yellow;
            }
            case 5:{
                if((int) (Math.random()*2)+1 ==1)
                    return Color.blue;
                else
                    return Color.yellow;
            }  
            case 6:{
                switch((int) (Math.random()*3)+1){
                    case 1:{return Color.red;}
                    case 2:{return Color.blue;}
                    case 3:{return Color.yellow;}
                }
            }
        }
        
        return Color.red;
    }
    
    //Procedure per ottenere le coordinate, la lunghezza e l'altezza   
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
}
