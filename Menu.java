package GameData;

import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Menu {
	
	public static void Pause(){
		Stage stage = new Stage();
    	Pane root = new Pane();
    	Scene scene = new Scene(root, 640, 400);
    	stage.setResizable(false);
    	
    	stage.initModality(Modality.APPLICATION_MODAL);
    	stage.setScene(scene);
        stage.show();
        //array for keyboard input
	    ArrayList<String> input = new ArrayList<String>();
	    
	    //keyboard input
	    
 	    scene.setOnKeyTyped(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent e) {
                String code = e.getCode().toString();

                // only add once... prevents duplicates
                if ( !input.contains(code) ){
                    input.add( code );
                
                }
            }
        });

  	    scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent e) {
                String code = e.getCode().toString();
                input.remove( code );
                
            }
        });
  	    
  	  new AnimationTimer(){
	    	public void handle(long NanoSeconds){
  	    
		  	    if (input.contains("ESCAPE")){
		  	    	//System.out.println("quit");
		  	    	
		  	    	stage.close();
		  	    	Game.Pause = false;
		  	    }
	    	}
  	  }.start();
  	  
	}
}
