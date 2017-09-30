/*
 * Arkolour
 * Classe dell'audio
 */
package arkolour;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

import  java.io.*;
 /*
 * Jacopo "Faust" Buttiglieri
 */
public class Audio {
    
    private Clip sounds[]=new Clip[5];
    private Clip music;
    private boolean isPlaying=false;
    public void loadSounds() throws Exception{
        
        AudioInputStream sound;


        //Vengono caricati i suoni e la musica
        for(int i =1;i<6;i++)
        {
            sound=AudioSystem.getAudioInputStream(new File("snd/sound"+i+".wav"));
            sounds[i-1] = (Clip) AudioSystem.getLine( new DataLine.Info(Clip.class, sound.getFormat()));
            sounds[i-1].open(sound);     
        }
        
        
        sound= AudioSystem.getAudioInputStream(new File("snd/music.wav"));
        music= (Clip) AudioSystem.getLine( new DataLine.Info(Clip.class, sound.getFormat()));
        music.open(sound);
        

    }
    
    //Un suono con un determinato ID viene riprodotto
    public void playSound(int streamID)
    {
        if(Global.soundsOn)
        {
            // play the sound clip
            if(sounds[streamID].isRunning())
            {
               sounds[streamID].stop();
            }

            sounds[streamID].setFramePosition(0);        
            sounds[streamID].start();
        }
    }
    
    //La musica viene riprodotta in loop
    public void playMusic()
    {
        if(Global.musicOn)
        {
            if(!music.isRunning())
                music.loop(Clip.LOOP_CONTINUOUSLY);
        }
        else
        {
            music.stop();
            music.setFramePosition(0);  
        }
    }
    
    
}
