/*
 * Arkolour
 * Classe degli effetti particellari
 */
package arkolour;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
/**
 *
 * Jacopo "Faust" Buttiglieri
 */
public class Particle {
    
    private int x=0;
    private int y=0;
    private int horSpeed=3;
    private int verSpeed=-3;    
    private Image sprite;
    private int life=0;
    private int type;
        
    Particle(String nameID, int x,int y,int speed,int life,int mult){
     
        this.life=life*mult;
        sprite= Toolkit.getDefaultToolkit().getImage("img/stars/"+nameID+".gif"); 
        type=speed;
                
        switch(speed)
        {
            case(-1):
            {
                this.x=x+((int) (Math.random()*(50*mult))-(25*mult));
                this.y=y-(int) (Math.random()*(10*mult));
                horSpeed=0;
                verSpeed=(int) (Math.random()*3)+1;   
                this.life+=(int) (Math.random()*25);
                break;
            }
            case(1):
            {
                this.x=x;
                this.y=y;
                horSpeed=0;
                verSpeed=0;   
                break;
            }
            default:
            {
                this.x=x;
                this.y=y;
                horSpeed=(int) (Math.random()*6)-3;
                verSpeed=(int) (Math.random()*6)-3;   
                break;
            }
        }
    }
    
    public void logic(){
        x+=horSpeed;
        y+=verSpeed;
        if(life>0)
            life--;
    }
        
    public boolean isOutside(){
        return (type<=0)&&((x<28)||(x>515))||(((y<=Global.screenStart))||(y>=Global.screenEnd));
    }    
    
    public boolean isDead(){
        return (type<=0)&&(life<=0);
    }
    
    //Disegna a schermo la particella
    public void drawPart(Graphics g)
    {
        //Disegno dell'immagine del potere
        g.drawImage(sprite, x, y, null);
    }
    
    public int getLife()
    {
        return life;
    }
}
