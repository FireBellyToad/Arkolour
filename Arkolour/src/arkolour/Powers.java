/*
 * Classe dei poteri
 */
package arkolour;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Image;
/**
 *
 * Jacopo "Faust" Buttiglieri
 */
public class Powers {
    
    private int x=0;
    private int y=0;
    private int w=25;
    private int h=25;
    private int ID;
    public boolean isCollided;
    private Image sprite;
    
    Powers(int nameID, int x,int y){
        this.x=x;
        this.y=y;
        sprite= Global.powers[nameID];
        ID=nameID;
    }
    
    //Procedura che ritorna il valore booleano "vero" se il potere è fuori dallo
    //schermo
    public boolean isOutside(){
        return y>=Global.screenEnd-5;
    }
    
    //Procedura per la gestione della logica del potere
    public void logic(Bat sbarra,Ball palla){
        y+=2;
        isCollided=collideWithBat(sbarra,palla);

    }
    
    //Procedura di collisione con la sbarra
    public boolean collideWithBat(Bat sbarra,Ball palla){    
                
        if((y+h>=sbarra.getY())&&(y<=sbarra.getY()+sbarra.getH()))
            if((x+w>=sbarra.getX())&&(x<=sbarra.getX()+sbarra.getW())){
                    if(ID<7){
                        sbarra.gotPower(ID);
                        palla.gotPower(ID);
                        if(ID!=4)
                        Global.audio.playSound(0);
                    }
                    else
                        if(ID==7)
                        {
                            Global.score+=1000;
                            Global.audio.playSound(1);
                        }
                        else
                        {
                            Global.lives+=1;
                            Global.audio.playSound(3);
                        }
                    
                    
                    return true;
                }
        return false;
    }
    
    //Disegna a schermo il potere
    public void drawPower(Graphics g)
    {
        //Disegno dell'immagine del potere
        g.drawImage(sprite, x, y, null);
    }
}
