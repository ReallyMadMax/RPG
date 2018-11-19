
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.Math;
import java.util.Random;


//so many imports /:
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Main extends Application {
	
	//dimensions of the game
	private final int width = 1280;
	private int sideBarWidth = 64;
	private final int height = 768;
	
	static ArrayList<Double> upgrades = Serializer.in("MaxSaveUpgrades");
	int[][] PixelArray = new int[width][height];
	private final int maxX = width - 1 - sideBarWidth;
	private final int maxY = height - 1;
	private final int fps = 45; // 55 is about the fastest atm //50 is the normal amount
	double prevTime = 0;
	int prefferedTime = (1000/fps);
	int sleepTime = 0;
	int spawnCap = 40;
	int aiAmount = 0;
	
	public double mouseX = 0;
	public double mouseY = 0;
	
	public double mouseXClicked;
	public double mouseYClicked;
	
	public boolean clicked = false; 
	public static String pressed = "Menu";
	public static boolean BlackJack = false;
	public Objects selected = null;
	
	public boolean hit = false;
	
	public int[] price = {0,50,100,200,500,1000,2000,3000, 5000};
	
	Random RandInt = new Random();
	int number = 0;
	double pressTimer = 0;
	
	int spawnTimer = 0;
	int spawnDelay = 100;
	int soundDelay = 10;
	int soundCounter = 0;
	
	
	
	static int wave = 1;
	static int totalSpawned = 0;
	
	static double damage;
	static double speed;
	static double shootDelay;
	static double health;
	static double moveSpeed;
	static double playerMoveSpeed;
	static double shield;
	
	static int shieldDelay = 400;
	static int shieldRespawn = 20;
	static int shieldCounter = 0;
	static int currentShield = 0;
	
	int SelectedCost;
	
	static double money = Serializer.NFromFile("MaxMoney");
	Text Money = new Text(Double.toString(money));
	Text WaveNumber = new Text("Wave: " + wave);
	Text Cost = new Text("Cost: " );
	Text instructions = new Text("");
	
	String aiImage = "player32x32" + wave + ".png";
	
	static boolean Pause = true;
	
	//int myRandomNumber = 0;
	//myRandomNumber = r.nextInt(maxValue-minValue+1)+minValue;
	String button = "";
	public static ArrayList<Objects> AiList = new ArrayList<Objects>();
	public static ArrayList<Objects> Remove = new ArrayList<Objects>();
	
	public static void sound(String music){
		//String musicFile = (music);     // For example

		Media sound = new Media(new File(music).toURI().toString());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
	}
	
	public void spawn(){
		
		
		if (aiAmount < spawnCap){
			int sides = RandInt.nextInt(6-1+1)+1;
			int amount = RandInt.nextInt(10-2+1)+2;
			wave = totalSpawned/100 + 1;
			
			if (wave <= 8)
				aiImage = "player32x32" + wave + ".png";
			
			if (sides == 1){
				for (int i = 0; i <amount; i++){
					AiList.add(new Objects(30 + 65*i, 10, 2, aiImage, 32, 32, "", 0, 0, wave));
				}
			} else if (sides == 2){
				for (int i = 0; i <amount; i++){
					AiList.add(new Objects(800 + 65*i, 10, 2, aiImage, 32, 32, "", 0, 0, wave));
				}
			} else if (sides == 3){
				for (int i = 0; i <amount; i++){
					AiList.add(new Objects(30 + 65*i, 700, 2, aiImage, 32, 32, "", 0, 0, wave));
				}
			} else if (sides == 4){
				for (int i = 0; i <amount; i++){
					AiList.add(new Objects(800 + 65*i, 700, 2, aiImage, 32, 32, "", 0, 0, wave));
				}
			} else if (sides == 5){
				for (int i = 0; i <amount; i++){
					AiList.add(new Objects(30 + 65*i, 10, 2, aiImage , 32, 32, "", 0, 0, wave));
				}
				
				for (int i = 0; i <amount; i++){
					AiList.add(new Objects(800 + 65*i, 700, 2, aiImage , 32, 32, "", 0, 0, wave));
				}
			} else if (sides == 6){
				for (int i = 0; i <amount; i++){
					AiList.add(new Objects(800 + 65*i, 10, 2, aiImage , 32, 32, "", 0, 0, wave));
				}
				for (int i = 0; i <amount; i++){
					AiList.add(new Objects(30 + 65*i, 700, 2, aiImage , 32, 32, "", 0, 0, wave));
				}
			}
			
			if (sides <= 4){
				aiAmount += amount;
				totalSpawned += amount;
			} else if (sides >= 5){
				aiAmount += amount*2;
				totalSpawned += amount*2;
			}
			
		}
	}
	
	
	
	public void restart(Objects player){
		
		AiList.clear();
		Objects.objectlist.clear();
		
		player.NewX(400);
		player.NewY(400);
		player.SetHp(4);
		Objects.objectlist.add(player);
		aiAmount = 0;
		totalSpawned = 0;
		wave = 1;
		
	}

	public void start(Stage Window) {
		boolean New = true;
		
		if (New){
		
			wave = 1;
			totalSpawned = 0;
			
			damage = 1;
			speed = 1;
			
			health = 1;
			moveSpeed = 1;
			playerMoveSpeed = 7;
			shield = 1;
			shootDelay = 2;
			
		} else if (!New){
			
			wave = 1;
			totalSpawned = 0;
			
			damage = upgrades.get(2);
			speed = upgrades.get(3);
			
			health = upgrades.get(4);
			moveSpeed = upgrades.get(5);
			playerMoveSpeed = upgrades.get(7);
			shield = upgrades.get(6);
			shootDelay = upgrades.get(8);

			
			
		}
		
		Objects player = new Objects(400, 400, (int)playerMoveSpeed, "PlayerRight64x64.png", 64, 64, "player", 0, 0, health + 3);
		
	    Window.setTitle("Game");
	    System.out.println("test");
	    
	    Objects hpBar = new Objects("Hp64.png");
	    Image Background = new Image("BackGround1216x768.png");
	    Image sideBar = new Image("sideBar.png");
	    Image Heart = new Image("heart64x64.png");
	    Image Shield = new Image("heart64x642.png");
	    
	    //upgrade objects
	    Objects bDamage = new Objects("bullet64x64.png", 64, 64, "Increases the number of enemies a bullet can go through.");
	    Objects bSpeed = new Objects("speed64x64.png", 64, 64, "Increases the rate of which you shoot and the speed of you bullets.");
	    Objects cHealth = new Objects("heart64x64.png", 64, 64, "Increases the players maximum health.");
	    Objects cSpeed = new Objects("shoe64x64.png", 64, 64, "Increases the players movement speed.");
	    Objects cShield = new Objects("heart64x642.png", 64, 64, "Gives you a shield that regenerates after not having taken damage for 5 seconds.");
	    
	    Objects Start = new Objects("StartScreen1280x800.png");
	    Start.NewY(0);
	    Start.NewX(0);
	    
	    Objects PauseScreen = new Objects("PauseScreen640x400.png");
	    PauseScreen.NewY(200);
	    PauseScreen.NewX(320);
	    
	    Objects StatsScreen = new Objects("StatsGUI800x500.png");
	    StatsScreen.NewY(150);
	    StatsScreen.NewX(240);
	    
	    Objects Shop = new Objects("ShopGUI800x500.png");
	    Shop.NewY(150);
	    Shop.NewX(240);
	    //Objects object1 = new Objects(800, 700, 4, "aiImage", 32);
	    //Objects object2 = new Objects(64, 400, 4, "aiImage", 32);
	    
	    Group root = new Group();
	    Scene theWindow = new Scene(root);
	    Window.setScene(theWindow);
	    Window.setResizable(false);
	    
	    Canvas canvas = new Canvas(width, height);
	    root.getChildren().add(canvas);
	   
	    root.getChildren().add(instructions);
	    instructions.setX(810);
	    instructions.setY(340);
	    instructions.setWrappingWidth(200);
	    root.getChildren().add(Cost);
	    Cost.setX(810);
	    Cost.setY(480);
	    root.getChildren().add(Money);
	    Money.setX(1220);
	    Money.setY(720);
	    root.getChildren().add(WaveNumber);
	    WaveNumber.setX(1220);
	    WaveNumber.setY(735);
	    GraphicsContext screen = canvas.getGraphicsContext2D();
	    
	    //array for keyboard input
	    ArrayList<String> input = new ArrayList<String>();
	    
	    
    //keyboard input
 	    theWindow.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent e) {
                String code = e.getCode().toString();
 
                    // only add once... prevents duplicates
                if ( !input.contains(code) ){
                    input.add( code );
                	button = code;
                }
            }});
 
  	    theWindow.setOnKeyReleased(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent e) {
                String code = e.getCode().toString();
                input.remove( code );
                button = "";
                if (code.equals("ESCAPE")){
                	pressed = "";
                	if (Pause)
                	Pause = false;
                	else if (!Pause)
                	Pause = true;
                	clicked = false;
                	
                }
        }});
  	    
	    
	    //mouse input
	    theWindow.setOnMouseMoved(new EventHandler<MouseEvent>(){
	    	public void handle(MouseEvent event){
	    		mouseX = event.getX();
	    		mouseY = event.getY();
    	}});
	    
	    theWindow.setOnMouseClicked(new EventHandler<MouseEvent>(){
	    	public void handle(MouseEvent event){
	    		mouseXClicked = event.getX();
	    		mouseYClicked = event.getY();
	    		System.out.println("X:" + mouseXClicked);
	    		System.out.println("Y:" + mouseYClicked);
	    	}});
    	    
	        
	        // main game loop
	    new AnimationTimer(){
	    	public void handle(long NanoSeconds){
	    		//double seconds = NanoSeconds / 1000000000;
	    		//System.out.println(time);
	    		
	    		//making the player move while within the window
	    		//left movement and collision detection
	    		if (input.contains("LEFT") || input.contains("A") && player.Left() > 0) {
	    			double moveX = Math.min(player.Left(), player.MoveSpeed());
	    			player.Move(player, -moveX, 0, player);
	    			player.Sprite(player,"PlayerLeft64x64.png");
	    		}
	    		//right movement and collision detection
	    		if (input.contains("RIGHT") || input.contains("D") && player.Right() < maxX) {
	    			double moveX = Math.min(maxX - player.Right(), player.MoveSpeed());
	    			player.Move(player, moveX, 0, player);
	    			player.Sprite(player,"PlayerRight64x64.png");
	    		}
	    		//up movement and collision detection
	    		if (input.contains("UP") || input.contains("W") && player.Top() > 0) {
	    			double moveY = Math.min(player.Top(), player.MoveSpeed());
	    			player.Move(player,0, -moveY, player);
	    		}
	    		//down movement and collision detection
	    		if (input.contains("DOWN") || input.contains("S") && player.Bottom() < maxY) {
	    			double moveY = Math.min(maxY - player.Bottom(), player.MoveSpeed());
	    			player.Move(player, 0, moveY, player);
	    			//System.out.println("DOWN");
	    		}
	    		
	    		if (input.contains("SPACE") && !Pause){
	    			if (pressTimer <= 0){
    					player.Shoot(player, mouseX, mouseY, 3.5, (int)damage);
    					pressTimer = shootDelay;
    				}
	    		}
	    		
	    		
	    		if (Pause && mouseXClicked >= 520 && mouseXClicked <= 760 && pressed == ""){
	    			if (mouseYClicked >= 265 && mouseYClicked <= 295 && !clicked){
	    				//Menu button
	    				System.out.println("Button 1 ");
	    				clicked = true;
	    				pressed = "Menu";
	    				mouseXClicked = 0;
    					mouseYClicked = 0;
    					sound("ClickSound.mp3");
	    			} else if (mouseYClicked >= 340 && mouseYClicked <= 375 && !clicked){
	    				//Shop button
	    				System.out.println("Button 2 ");
	    				pressed = "Shop";
	    				clicked = true;
	    				mouseXClicked = 0;
    					mouseYClicked = 0;
    					sound("ClickSound.mp3");
	    			} else if (mouseYClicked >= 420 && mouseYClicked <= 460 && !clicked){
	    				//Stats button
	    				System.out.println("Button 3 ");
	    				clicked = true;
	    				pressed = "Stats";
	    				mouseXClicked = 0;
    					mouseYClicked = 0;
    					sound("ClickSound.mp3");
	    			} else if (mouseYClicked >= 505 && mouseYClicked <= 540 && !clicked){
	    				//Quit button
	    				Window.close();
	    			}
	    		}else if (pressed == "Shop"){
	    			bDamage.NewX(200 + (64*damage));
	    			bDamage.NewY(238);
	    			
	    			bSpeed.NewX(200 + (64*speed));
	    			bSpeed.NewY(300);
	    			
	    			cHealth.NewX(203 + ((player.GetMaxHP() - 3)*64));
	    			cHealth.NewY(366);
	    			
	    			cShield.NewX(203 + (shield*64));
	    			cShield.NewY(430);
	    			
	    			cSpeed.NewX(204 + (moveSpeed)*64);
	    			cSpeed.NewY(494);
	    			
	    			
	    			
	    			if (mouseXClicked >= 645 && mouseXClicked <= 1010){
	    				if (mouseYClicked >= 535 && mouseYClicked <= 605){
	    					//back button
	    					clicked = false;
	    					pressed = "";
	    					mouseXClicked = 0;
	    					mouseYClicked = 0;
	    					sound("ClickSound.mp3");
	    				} else if (mouseYClicked >= 240 && mouseYClicked <= 300){
	    					if (selected != null){
	    						if (damage <= 7 && selected == bDamage){
	    							if (money >= price[(int) damage]){
	    								money -= price[(int) damage];
	    								damage ++;
	    								SelectedCost ++;
	    								mouseXClicked = 0;
	    		    					mouseYClicked = 0;
	    		    					sound("ClickSound.mp3");
	    							}
	    						} else if (speed <= 7 && selected == bSpeed){
	    							if (money >= price[(int) speed]){
	    								money -= price[(int) speed];
	    								speed ++;
	    								SelectedCost ++;
	    								shootDelay -= (shootDelay/5);
	    								mouseXClicked = 0;
	    		    					mouseYClicked = 0;
	    		    					sound("ClickSound.mp3");
	    							}
	    						} else if (health <= 7 && selected == cHealth){
	    							if (money >= price[(int) health]){
	    								money -= price[(int) health];
	    								health ++;
	    								SelectedCost ++;
	    								player.SetMaxHP(player.GetMaxHP() + 1);
	    								mouseXClicked = 0;
	    		    					mouseYClicked = 0;
	    		    					sound("ClickSound.mp3");
	    							}
	    						}else if (moveSpeed <= 7 && selected == cSpeed){
	    							if (money >= price[(int) moveSpeed]){
	    								money -= price[(int) moveSpeed];
	    								moveSpeed ++;
	    								SelectedCost ++;
	    								
	    								
	    								playerMoveSpeed = (moveSpeed*0.25) + player.StaticMoveSpeed();
	    								player.SetMoveSpeed(playerMoveSpeed);
	    								mouseXClicked = 0;
	    		    					mouseYClicked = 0;
	    		    					sound("ClickSound.mp3");
	    		    					
	    							}
	    						} else if (shield <= 7 && selected == cShield){
	    							if (money >= price[(int) shield]){
	    								money -= price[(int) shield];
	    								shield ++;
	    								SelectedCost ++;
	    								
	    								mouseXClicked = 0;
	    		    					mouseYClicked = 0;
	    		    					sound("ClickSound.mp3");
	    		    					
	    							}
	    						}
	    					}
	    				}
	    			} else if (mouseXClicked >= bDamage.Left() && mouseXClicked <= bDamage.Right()
	    					&& mouseYClicked >= bDamage.Top() && mouseYClicked <= bDamage.Bottom()){
	    				selected = bDamage;
	    				SelectedCost = (int) damage;
	    				//damage += 1;
	    			} else if (mouseXClicked >= bSpeed.Left() && mouseXClicked <= bSpeed.Right()
	    					&& mouseYClicked >= bSpeed.Top() && mouseYClicked <= bSpeed.Bottom()){
	    				selected = bSpeed;
	    				SelectedCost = (int) speed;
	    				//speed +=1;
	    				//shootDelay -= (shootDelay/5);
	    			} else if (mouseXClicked >= cHealth.Left() && mouseXClicked <= cHealth.Right()
	    					&& mouseYClicked >= cHealth.Top() && mouseYClicked <= cHealth.Bottom()){
	    				selected = cHealth;
	    				SelectedCost = (int) health;
	    				//health += 1;
	    				//player.SetMaxHP(player.GetMaxHP() + 1);
	    			} else if (mouseXClicked >= cSpeed.Left() && mouseXClicked <= cSpeed.Right()
	    					&& mouseYClicked >= cSpeed.Top() && mouseYClicked <= cSpeed.Bottom()){
	    				selected = cSpeed;
	    				SelectedCost = (int) moveSpeed;
	    				//health += 1;
	    				//player.SetMaxHP(player.GetMaxHP() + 1);
	    			} else if (mouseXClicked >= cShield.Left() && mouseXClicked <= cShield.Right()
	    					&& mouseYClicked >= cShield.Top() && mouseYClicked <= cShield.Bottom()){
	    				selected = cShield;
	    				SelectedCost = (int) shield;
	    				//health += 1;
	    				//player.SetMaxHP(player.GetMaxHP() + 1);
	    			}
	    			
	    		} else if (pressed == "Stats"){
	    			sound("ClickSound.mp3");
	    			
	    			mouseXClicked = 0;
					mouseYClicked = 0;
					BlackJack = true;
					
	    			
	    			
	    			
	    		} /*else if (pressed == "Start"){
	    			Pause = true;
	    			if (mouseXClicked >= 495 && mouseXClicked <= 825){
	    				if (mouseYClicked >= 190 && mouseYClicked <= 275){
	    					Pause = false;
	    					pressed = "";
	    				}else if (mouseYClicked >= 340 && mouseYClicked <= 415){
	    					Window.close();
	    				}
	    			}
	    		}*/ else if (pressed == "Menu"){
	    			Pause = true;
	    			if (mouseXClicked >= 495 && mouseXClicked <= 825){
	    				if (mouseYClicked >= 190 && mouseYClicked <= 275){
	    					restart(player);
	    					Pause = false;
	    					clicked = false;
	    					pressed = "";
	    					mouseXClicked = 0;
	    					mouseYClicked = 0;
	    					sound("ClickSound.mp3");
	    				}else if (mouseYClicked >= 340 && mouseYClicked <= 415){
	    					pressed = "";
	    					sound("ClickSound.mp3");
	    					
	    				}
	    			}
	    		}
	          	   //Pausing game movement
	     	   if (Pause){
	     		   
	     		   for(Objects stop : Objects.objectlist){
	     			   stop.SetMoveSpeed(0);
	     		  
	     		   }
	     		   //Pause = false;
	     	   }else if (!(Pause)){
	     		   
	     		    for(Objects go : Objects.objectlist){
	     			   go.OldMoveSpeed();
	     		   
	     		   }
	     		   //Pause = true;
			
	     		   
	     		   
	     	   }
	    		    
	    		
	    		
	    		//other buttons
	    		
	    		
	    		//redrawing the screen after player moves
	    		//screen.clearRect(0, 0, width, height);
	    		screen.drawImage(Background, 0, 0);

	    		// dealing with objects
	    		for (Objects blob : AiList){

	    			if (blob.Left() < 0 || blob.Bottom() < 0 || 
	    				blob.Right() > maxX || blob.Top() > maxY){
	    					Remove.add(blob);
	    					//System.out.println("removed");
	    			}
	    			
	    			if (blob.Touching() == true){
	    				switch (blob.Name()){
	    					case "":
	    						blob.SetHp(blob.CurrentHP() - 1);
	    						blob.isTouching(false);
	    						if (blob.CurrentHP() <= 0){
	    							Remove.add(blob);
	    							player.SetHp(player.CurrentHP() + 1);
	    							money += (1*wave);
	    							if (soundCounter >= soundDelay){
	    								sound("DeathSound.mp3");
	    								soundCounter = 0;
	    							}
	    							
	    							
	    						}
	    						break;
	    					case "bullet":
	    						blob.SetHp(blob.CurrentHP() - 1);
	    						blob.isTouching(false);
	    						if (blob.CurrentHP() <= 0){
	    							Remove.add(blob);
	    							
	    						}
	    						
	    						break;
	    					case "player":
	    						System.out.println("player");
	    				}
	    				
	    			} else if (blob.Name() == "") {
	    				blob.Follow(blob, player);
	    				//blob.Sprite(blob, "aiImage");
	    			} else if (blob.Name() == "bullet"){
	    				//System.out.println("test");
	    				blob.Move(blob, 0, blob.Yspeed(), player);
	    				blob.Move(blob, blob.Xspeed(), 0, player);
	    			}
	    			screen.drawImage(blob.sprite(), blob.Left(), blob.Top());
	    		}
	    		
	    		for (Objects Delete: Remove){
	    			AiList.remove(Delete);
	    			Objects.objectlist.remove(Delete);
	    			Delete = null;
					aiAmount --;
	    		}
	    		
	    		Remove.clear();
	    		
	    		
	    		
	    		
	    		
	    		hpBar.NewY(player.Top()+64);
	    		hpBar.NewX(player.Left());
	    		//hpBar.Sprite(hpBar, "Hp64.png");
	    		
	    		
	    		//sound("HitSound.mp3");
	    		
	    		
	    		screen.drawImage(sideBar, 1216, 0);
	    		
	    		for (int i = 0; i < player.CurrentHP(); i++){
	    			screen.drawImage(Heart, 1216, 0 + (64*i));
	    		}
	    		
	    		for (int i = 0; i < currentShield; i ++){
	    			screen.drawImage(Shield, 1216, 0 + (64*i));
	    		}
	    		
	    		screen.drawImage(player.sprite(), player.Left(), player.Top());
	    		screen.drawImage(hpBar.sprite(), hpBar.Left(), hpBar.Top());
	    		instructions.setText("");
	    		//System.out.println(player.Touching());
	    		if (pressed == "Start" || pressed == "Menu"){
	    			screen.drawImage(Start.sprite(), Start.Left(), Start.Top());
	    	 	}else if (Pause && !clicked){
	    			screen.drawImage(PauseScreen.sprite(), PauseScreen.Left(), PauseScreen.Top());
	    		} else if (pressed == "Shop"){
	    			if (selected != null){
	    				instructions.setText(selected.Description());
	    			}
	    			screen.drawImage(Shop.sprite(), Shop.Left(), Shop.Top());
	    			
	    			screen.drawImage(bDamage.sprite(), bDamage.Left(), bDamage.Top());
	    			
	    			screen.drawImage(bSpeed.sprite(), bSpeed.Left(), bSpeed.Top());
	    			screen.drawImage(cSpeed.sprite(), cSpeed.Left(), cSpeed.Top());
	    			
	    			screen.drawImage(cHealth.sprite(), cHealth.Left(), cHealth.Top());
	    			
	    			screen.drawImage(cShield.sprite(), cShield.Left(), cShield.Top());
	    		} else{
	    			if (spawnTimer <= 0 && aiAmount <= spawnCap ){
          			   spawn();
          			   spawnTimer = spawnDelay;
	    			}
	    			soundCounter ++;
	    			shieldCounter --;
	    			
	    			if (shieldCounter <= 0 && (currentShield <= (shield-2))){
	    				shieldCounter = shieldRespawn;
	    				currentShield ++;
	    			}
	    		}
	    		
	    		if (BlackJack){
	    			pressed = "blackJack";
	    			Window.close();
	    			
	    		}
	    			
	    		Money.setText(Double.toString(money));
	    		WaveNumber.setText("Wave: " + wave);
	    		if (pressed == "Menu" || pressed == "Start" && Money.isVisible()){
	    			Money.setVisible(false);
	    			WaveNumber.setVisible(false);
	    		} else if (pressed != "Menu" || pressed != "Start" && !Money.isVisible()){
	    			Money.setVisible(true);
	    			WaveNumber.setVisible(true);
	    		}
	    		
	    		if (pressed == "Shop"){
	    			Cost.setText("Cost: " + price[SelectedCost]);
	    			Cost.setVisible(true);
	    		} else{
	    			Cost.setVisible(false);
	    		}
	    		//System.out.println(money);
	    		
	    		try {
	    			double time = NanoSeconds / 1000000;
	    		    double timetaken =  time - prevTime;
	    		    prevTime = time;
	    		    sleepTime = prefferedTime - (int)timetaken;
	    		    pressTimer -= 0.1;
	    		    spawnTimer --;
	    	//to see the amount of time taken in each cycle
	    		    //System.out.println(sleepTime);
	    		    //System.out.println(prefferedTime);
	    		    //System.out.println(timetaken);
	    		    //System.out.println((int)timetaken+sleepTime);
	    		    if (!((int)timetaken > prefferedTime)){
	    		    	Thread.sleep(sleepTime);
	    		    	//System.out.print("true");
	    		    }
	    		    
	    		} catch(InterruptedException ex) {
	    			System.out.println("InterruptedException");
	    		}
	    		
	    		
	    		if (player.CurrentHP() <= 0){
	    			restart(player);
	    		}
	    		
	    	}
	    }.start();
	    
	    Window.show();
	}
	
	public static void run(String[] args){
		launch(args);
	}
	
	public static void main(String[] args) {
		 
        launch(args);
        	
        System.out.print(Objects.objectlist.size());
        
        upgrades.clear();
        
        upgrades.add((double) wave);
        upgrades.add((double) totalSpawned);
    	
        upgrades.add(damage);
        upgrades.add(speed);
        upgrades.add(health);
        upgrades.add(moveSpeed);
        upgrades.add(shield);
        upgrades.add(playerMoveSpeed);
        upgrades.add(shootDelay);
        
        Serializer.out("MaxSaveUpgrades", upgrades);
        
        Serializer.ToFile("MaxMoney", money);
        
        if (pressed == "blackJack"){
        	OpeningWindow.main(null);
        }
        
	}
	
}
