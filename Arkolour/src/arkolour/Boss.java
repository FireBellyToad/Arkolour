/*
 * Arkolour
 * Classe del boss finale
 */
package arkolour;


import java.awt.Graphics;
import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
/**
 *
 * Jacopo "Faust" Buttiglieri
 */
public class Boss {
    
    private int x=250;
    private int y=150;
    private int radius=80;
    private int horSpeed=-1;
    private int verSpeed=-1;
    private Image boss[]=new Image[2];
    public int health=51;
    private Powers drops[]=new Powers[6];
    private int time=0;    
    private int freq;
    private int imageIndex;
    private int timeBeforeDying=600;
    public boolean stopHit=false;
    
    Boss(){
        boss[0]= Toolkit.getDefaultToolkit().getImage("img/boss.gif");        
        boss[1]= Toolkit.getDefaultToolkit().getImage("img/boss2.gif");
        imageIndex=0;
    }
    
    public void logic()
    {
        
        freq=((int) Math.floor(health/10)+1)*10;
        
        
        //Movimento normale, quando il Boss è dentro i limiti del livello
        if((x>28)&&(x+(radius*2)<523)){
            x+=horSpeed;
            y+=verSpeed;
        }
        else{
            x-=horSpeed;
            horSpeed=-horSpeed;
        }
        
        //Se il Boss tocca il lato superiore della schermata, rimbalza verso
        //il basso
        if(((y<Global.screenStart)||(y+(radius*2)>Global.screenEnd-100)))
            verSpeed=-verSpeed;
        
        if(health>1)
        {
            for(int i=0;i<drops.length;i++){
                if(drops[i]!=null){
                     drops[i].logic(Global.sbarra, Global.palla);
                     if((drops[i].isCollided)||(drops[i].isOutside())){
                         drops[i]=null;
                     }
                }
            }
            //Sei time è uguale a zero, la ripone unguale a freq e crea una goccia.
            //Altrimenti ne decrementa il valore        
            if(time==0){
                createDrop(0);
                time=freq;
            }else
                time--;
        }
        else
        {   
            for(int i=0;i<drops.length;i++){
                drops[i]=null;                
                
            if(timeBeforeDying==600)
                Global.livello.createParticles(50, x+radius,y+radius,Color.black , 10);
                
            if(timeBeforeDying>0)
                timeBeforeDying--;
            else
            {   
                health=0;
                Global.change(6,false);
            }
            
            imageIndex=1;
            }
        }
        
    }
    
    //Procedura di creazione della goccia
    public void createDrop(int i){

        if(drops[i]==null){
            drops[i]=new Powers(4,x+70,y+120);
        }
        else
            if(i<4)
                createDrop(i+1);
    }
    
    //Procedura del disegno a schermo del boss
    public void drawBoss(Graphics g){
        for(int i=0;i<drops.length;i++){
            if(drops[i]!=null)
                drops[i].drawPower(g);
        }                
        g.drawImage(boss[imageIndex], x, y, null);
        if(health>1)
        {
            g.setColor(Color.white);
            g.drawString(String.valueOf(health-1), x+radius-10, y+radius+2);
        }
    }
    
    public boolean isDead(){
        return (health<1);
    }
    
   public int getX(){
        return x;
    }
    
    public int getRadiusX2(){
        return radius*2;
    }
        
    public int getY(){
        return y;
    }
    
    public Color getBallColor(){
        switch((int) (Math.random()*3)+1){
            case 1:{return Color.red;}
            case 2:{return Color.blue;}
            case 3:{return Color.yellow;}
        }
        return(Color.red);
    }
}
