/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package arkolourleveleditor;

import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Color;
import java.io.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.Icon;
import javax.swing.ImageIcon;
/**
 *
 * @author user
 */
public class ArkolourLevelEditor extends JFrame {
    
    public static JButton mattoni[]=new JButton[150];
    public static String color[]=
                        {"Delete Brick","Red","Yellow","Blue","RedBlue","RedYellow","BluYellow","All"};
    public static JComboBox selectColor;
    public static String hardness[]={"1","2","3"};
    public static JComboBox selectHardness;
    public static JPanel brickPanel=new JPanel();
    public static JPanel editPanel=new JPanel();
    public static int selectedColor=-1;
    public static int selectedHard=1;
    public static Brick bricks[]=new Brick[100];
    public Icon colorIcons[]={
        new ImageIcon(getClass().getResource("red.jpg")),
        new ImageIcon(getClass().getResource("yellow.jpg")),
        new ImageIcon(getClass().getResource("blue.jpg")),
        new ImageIcon(getClass().getResource("redblue.jpg")),
        new ImageIcon(getClass().getResource("redyellow.jpg")),
        new ImageIcon(getClass().getResource("blueyellow.jpg")),
        new ImageIcon(getClass().getResource("all.jpg")),
        };

    
    public static void main(String[] args) {       
       
            ArkolourLevelEditor ist=new ArkolourLevelEditor();

       }
       
    ArkolourLevelEditor (){
        
        //Crea la finestra e setta l'azione di chiusura sul tasto X
        setBounds(0,0,1000,400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //Crea i layout per il pannello dei mattoni e quello dell'editor
        brickPanel.setLayout(new GridLayout(10,10));
        editPanel.setLayout(new GridLayout(3,1));
        
        //Instanzia le due combobox per il colore e la durezza
        selectColor=new JComboBox(color);
        selectHardness=new JComboBox(hardness);
        //Instanzia il buttonhandler per gli eventi dei bottoni
        ButtonHandler listener=new ButtonHandler();
        
        //Instanzia il bottone per salvare il livello e gli assegna il buttonhandler
        JButton saveMap=new JButton("Save the map");
        saveMap.addActionListener(listener);
        
        //Instanzia l'itemlistener per la combobox di scelta del colore
        selectColor.addItemListener(
                new ItemListener(){
                    public void itemStateChanged(ItemEvent event){
                        if(event.getStateChange()==ItemEvent.SELECTED)
                            //Se viene selezionato un campo, cambia il tipo 
                            //applicata ai mattoni
                            selectedColor=selectColor.getSelectedIndex()-1;
                    }
                });
        
        //Instanza l'itemlistener per la combobox di scelta della durezza
        selectHardness.addItemListener(
                new ItemListener(){
                    public void itemStateChanged(ItemEvent event){
                        //Se viene selezionato un campo, cambia la durezza 
                        //applicata ai mattoni
                        if(event.getStateChange()==ItemEvent.SELECTED)
                            selectedHard=selectHardness.getSelectedIndex()+1;
                    }
                });
        

        //Instanzia i bottoni e inizializza i blocchi, aggiungendoli poi all'
        //apposito pannello
        for(int i=0;i<100;i++){
            
            bricks[i]=new Brick();
            bricks[i].color=0;
            bricks[i].hardness=1;
            mattoni[i]=new JButton(Integer.toString(i),colorIcons[bricks[i].color]);
            switch(bricks[i].hardness){
                case 1:{ mattoni[i].setBackground(Color.white); break;}
                case 2:{ mattoni[i].setBackground(Color.gray); break;}
                case 3:{ mattoni[i].setBackground(Color.black); break;}
            }
            mattoni[i].addActionListener(listener);
            brickPanel.add(mattoni[i]);

        }
        //Aggiunge al pannello dell'editor le due combobox ed il tasto di salvataggio
        editPanel.add(selectColor);
        editPanel.add(selectHardness);
        editPanel.add(saveMap);
        
        //Aggiunge al frame i due pannelli
        add(brickPanel,BorderLayout.CENTER);
        add(editPanel,BorderLayout.EAST);
        setVisible(true);
    }       
    
    private class ButtonHandler implements ActionListener{
        
         @Override public void actionPerformed(ActionEvent event){
            buttonPressed(event.getActionCommand());
        }
    }
    
    //Procedura che cambia lo stato dei mattoni quando viene premuto un bottone.
    //In base al bottone selezionato, cambia il mattone che esso rappresentava
    public void buttonPressed(String butt){
        //Se il bottone premuto NON è quello di salvataggio
        if(!(butt.equals("Save the map"))){
            //Ottiene il numero del bottone premuto
            int pressed=Integer.valueOf(butt);
            //Assegna la durezza ed il tipo selezionati
            bricks[pressed].color=selectedColor;
            bricks[pressed].hardness=selectedHard;

            //Se è selezionato "Delete brick", viene cancellato il mattone,
            //altrimenti viene assegiata al bottone l'icona corrispondente al tipo
            //di mattone
            if(selectedColor==-1)
                mattoni[pressed].setIcon(null);
            else
                mattoni[pressed].setIcon(colorIcons[selectedColor]);

            //Viene colorato il bottone a seconda della durezza del bottone
            switch(selectedHard){
                case 1:{ mattoni[pressed].setBackground(Color.white); break;}
                case 2:{ mattoni[pressed].setBackground(Color.gray); break;}
                case 3:{ mattoni[pressed].setBackground(Color.black); break;}
            }
        }
        else{
            //Scrittura su file
            DataOutputStream out;
            try{
                out=new  DataOutputStream(new FileOutputStream("level.lev"));
                for(int i=0;i<150;i++){
                    out.writeInt(bricks[i].color);
                    out.writeBytes("\t");
                    out.writeInt(bricks[i].hardness);
                    out.writeBytes("\n");
                }
                out.close();
            }
            catch(Exception e){}
            
        }
    }

    //Classe del mattone
    public static class Brick{
        public int color;
        public int hardness;
    } 
}
