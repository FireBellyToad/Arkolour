/*
 * Arkolour
 * Classe della palla
 */
package arkolour;

import java.awt.Graphics;
import java.awt.Color;
/**
 *
 * Jacopo "Faust" Buttiglieri
 */
public class Ball {
    
    private int x=250;
    private int y=490;
    private int radius=Global.ballRadius;
    private int horSpeed=3;
    private int verSpeed=-3;
    private int isSpeedMod=0;    
    public int time=0;
    private Color color=Color.RED;
    
    
    //Procedura per gestire la logica della palla
    //(Collisioni, movimento...)
    public void logic(Bat sbarra,Bricks mattoni[],Boss boss){
        
        //Movimento normale, quanod la palla è dentro i limiti del livello
        if((x>28)&&(x+(radius*2)<523)){
            x+=horSpeed;
            y+=verSpeed;
        }else         
        {
            if(x<=28)
                x=29;
            if(x+(radius*2)>=523)
                x=522-radius*2;
            horSpeed=-horSpeed;
        }
        
        //Se la palla tocca il lato superiore della schermata, rimbalza verso
        //il basso
        if((y<=Global.screenStart)){
            y=Global.screenStart+1;
            verSpeed=-verSpeed;            
        }
        
        /*Se la palla finisce al di sotto della schermata, le vite vengono
         * decrementate di uno, il gioco si ferma per un secondo e la posizione
         * e la velocità della palla vengono resettate
        */
        if(y>=Global.screenEnd){
            restart();            
            Global.lives--;
        }
        //Controllo collisioni con sbarra
        collideWithBat(sbarra);
        //Controllo delle collisioni con il boss
        if(collideWithBoss(boss))
        {
            if(boss.health>1)
            Global.livello.createParticles(1, x,y,Color.black , 25);
        }
        //Controllo collisioni con un mattone 
        boolean end=false;
        for(int i=0;(i<mattoni.length)&&(!end);i++){
            if(collideWithBrick(mattoni[i])){
                //Se la durezza è 1 o la palla è ingrandita, viene distrutto
                if((mattoni[i].hardness==1)||(radius>Global.ballRadius)){
                    //Procedura per l'eventuale creazione del potere                    
                    mattoni[i].createPower();                    
                    Global.livello.createParticles(1, mattoni[i].getX(), mattoni[i].getY(),color , -1);
                    mattoni[i]=null;
                    end=true;                    
                    Global.change(Global.livello.getID()+1,false);
                }
                else
                    //altrimenti viene decrementata la durezza
                    mattoni[i].hardness--;
            }
        }
        
        //Se il tempo del potere finisce, ne vengono eliminati gli effetti,
        //altrimenti viene diminuito il tempo  a disposizione
        if(time==0){
            time=0;
            gotPower(10);
        }
        else
            if(time>0)
                time--;
            
    }
    
    //Procedura per ottenere il colore attuale della palla
    public Color getColor(){
        return color;
    }
    
    //Procedura per assegnare il giusto potere alla palla. Ogni volta che viene
    //chiamata, si annulla il potere posseduto precedentemente
    public void gotPower(int power){
        
       Global.finestra.activePower=power;
       
       switch(power){
            default:{
                //Nessun potere ottenuto relativo alla palla 
                isSpeedMod=0;
                radius=Global.ballRadius;                
                time=-1;
                //Controllo per evitare che in caso di un potere valido ma non 
                //appartenente alla sbarra venga sovrascritta la variabile
                if((power<0)||(power>6))
                    Global.finestra.activePower=-1;
                break;
            }
            case 1:{
               //Palla ingrandita
                radius=Global.ballRadius*2;
                isSpeedMod=0;                
                time=-1;                
                Global.livello.createParticles((int) (Math.random()*3)+10, x+radius, y+radius,color , 25);
                break;
            }
            case 2:{
                //Palla rallentata                
                isSpeedMod=1;
                radius=Global.ballRadius;  
                time=600;                
                Global.livello.createParticles((int) (Math.random()*3)+10, x+radius, y+radius,color , 25);
                break;
            }
            case 5:{
                //Palla velocizzata
                isSpeedMod=2;
                radius=Global.ballRadius;                
                time=600;                
                Global.livello.createParticles((int) (Math.random()*3)+10, x+radius, y+radius,Color.black , 25);
                break;
            }
        }
       
        //Se isSpeedMod è uguale ad 1, la velocità è dimezzata, se 
        //invece è uguale a 2 è raddoppiata
        speedChange(isSpeedMod);
    }
    
    //Procedura per il disegno della palla su schermo
    public void drawBall(Graphics g)
    {
        
        //Disegno sfondo della palla 
        g.setColor(getColor());
        g.fillOval(x,y,radius*2,radius*2);
        g.setColor(Color.black);
        g.drawOval(x,y,radius*2,radius*2);
        
    }
    
    /*
     * Procedura per le collisioni contro i mattoni. Questa procedura cicla
     * l'array dei mattoni,fino a che viene trovato il mattone con cui
     * la palla collide. Il controllo consiste nel confrontare le coordinate 
     * di entrambi. Se il controllo ha successo, il rimbalzo è calcolato
     * controllando se gli estremi orizzontali della palla toccano gli estremi 
     * orrizzontali opposti del mattone. In tal caso si ha un rimbalzo
     * La stessa cosa si fa con gli estremi verticali di entrambi, ma il 
     * rimbalzo sarà verticale. La palla ottiene lo stesso colore del mattoncino
     * colpito e si guadagnano 250 punti
     */
     public boolean collideWithBrick(Bricks mattone){
        
        if(mattone!=null)
            if((y+radius*2>=mattone.getY())&&(y<=mattone.getY()+mattone.getH()))
                if((x+radius>=mattone.getX())&&(x+radius<=mattone.getX()+mattone.getW())){               
                    
                    if((x < mattone.getX()+mattone.getW()+radius*2 && x > mattone.getX()+mattone.getW()-radius*2)
                            ^ (x+radius*2 < mattone.getX()+radius*2 && x+radius*2 > mattone.getX()-radius*2))
                        horSpeed=-horSpeed;
                    else
                    if((y < mattone.getY()+mattone.getH()+radius*2 && y > mattone.getY()+mattone.getH()-radius*2) 
                       ^ (y+radius*2 < mattone.getY()+radius*2 && y+radius*2 > mattone.getY()-radius*2))
                        verSpeed=-verSpeed;
                        
                            
                    
                    color=mattone.getBrickColor();   
                    Global.livello.createParticles(radius+1, mattone.getX()+12, mattone.getY()+6,color , 25);
                    Global.score+=250;
                    Global.audio.playSound(4);
                    return true;
                }
        return false;
  
     }
     public boolean collideWithBoss(Boss boss){
         
        if(boss!=null){
            if((y+radius*2>=boss.getY())&&(y<=boss.getY()+boss.getRadiusX2())){
                if((x+radius>=boss.getX())&&(x+radius<=boss.getX()+boss.getRadiusX2())){               
                    
                                     
                    if(!boss.stopHit){
                        if(boss.health>1)
                            boss.health--;
                        boss.stopHit=true;
                    }
                    color=boss.getBallColor();
                    Global.score+=10;
                    return true;
                }
                else
                    boss.stopHit=false;
            }            
            else
                boss.stopHit=false;
        }
        return false;
  
     }
    
    /* 
     * Procedura per controllare le collsioni con la sbarra. 
     * Viene prima controllata se la parte sottostante della palla tocca la
     * parte soprastante della sbarra. Se ciò è vero, viene controllato se il 
     * centro della palla è compreso tra l'estremità destra e quella sinistra 
     * della sbarra. Infine si controlla se i colori di palla e sbarra sono 
     * uguali. Se tutti i controlli risultano veri, allora la palla rimbalzerà
     */
    public void collideWithBat(Bat sbarra){
        
        
        
        if((y+radius*2>=sbarra.getY())&&(y<=sbarra.getY()+sbarra.getH()))
            if((x+radius>=sbarra.getX())&&(x+radius<=sbarra.getX()+sbarra.getW()))
                if(((sbarra.checkColor)&&(color==sbarra.getColor()))||(!sbarra.checkColor)){
        
        /*
         * In base a su che parte della sbarra cadrà, la palla rimbalzerà in 6 
         * direzioni diverse. Più a sinistra cadrà, minore sarà l'angolo del
         * rimbalzo tra sbarra e palla. Più a destra cadrà, maggiore sarà 
         * l'angolo tra sbarra e palla
         */
           
                if(x+radius<(sbarra.getX()+(sbarra.getW()/6))){
                    horSpeed=-5;
                    verSpeed=-1;
                }
                else
                if(x+radius<(sbarra.getX()+(sbarra.getW()/6)*2)){
                    horSpeed=-4;
                    verSpeed=-2;
                }
                else
                if(x+radius<(sbarra.getX()+(sbarra.getW()/6)*3)){
                    horSpeed=-3;
                    verSpeed=-3;
                }
                else
                if(x+radius<(sbarra.getX()+(sbarra.getW()/6)*4)){
                    horSpeed=3;
                    verSpeed=-3;
                }
                else
                if(x+radius<(sbarra.getX()+(sbarra.getW()/6)*5)){
                    horSpeed=4;
                    verSpeed=-2;
                }
                else
                if(x+radius<(sbarra.getX()+sbarra.getW())){
                    horSpeed=5;
                    verSpeed=-1;
                }
                
                //Se la palla è troppo in basso rispetto alla sbarra, rimbalzerà
                //verso il basso. Evita "i salvataggi in corner"
                if(y+radius>(sbarra.getY()+(sbarra.getH()/4)*3))
                    if(verSpeed<0)
                        verSpeed=-verSpeed;
                
                //Se isSpeedMod è uguale ad 1, la velocità è dimezzata, se 
                //invece è uguale a 2 è raddoppiata
                speedChange(isSpeedMod);
                Global.audio.playSound(4);
            }
        }
    
    //Procedura che resetta le coordinate, le velocità e le statistiche della palla.
    //Diminuisce anche la vite totali
    public void restart(){
        x=250;
        y=490;
        verSpeed=-3;
        horSpeed=3;
        gotPower(100);        
        Global.sbarra.gotPower(100);
        try{
            Thread.sleep(2000);
        }
        catch(InterruptedException e){
        }
    } 
    
    //Procedura per il corretto cambio delle velocità in caso di potenziamenti
    public void speedChange(int isSpeedMod){
        
        if(isSpeedMod==1){
            switch(horSpeed){
                case-5:{horSpeed=-3;break;}                    
                case-4:{horSpeed=-2;break;}                    
                case-3:{horSpeed=-2;break;}
                case 5:{horSpeed=3;break;}                    
                case 4:{horSpeed=2;break;}                    
                case 3:{horSpeed=2;break;}
            }
            switch(verSpeed){
                case-1:{verSpeed=-1;break;}                    
                case-2:{verSpeed=-1;break;}                    
                case-3:{verSpeed=-2;break;}
                case 1:{verSpeed=1;break;}                    
                case 2:{verSpeed=1;break;}                    
                case 3:{verSpeed=2;break;}
            }

        }
        else
        if(isSpeedMod==2){
            switch(horSpeed){
                case-5:{horSpeed=-7;break;}                    
                case-4:{horSpeed=-6;break;}                    
                case-3:{horSpeed=-5;break;}
                case 5:{horSpeed=7;break;}                    
                case 4:{horSpeed=6;break;}                    
                case 3:{horSpeed=5;break;}
            }
            switch(verSpeed){
                case-1:{verSpeed=-2;break;}                    
                case-2:{verSpeed=-4;break;}                    
                case-3:{verSpeed=-6;break;}
                case 1:{verSpeed=2;break;}                    
                case 2:{verSpeed=4;break;}                    
                case 3:{verSpeed=6;break;}
            }  
        }
    }
}

