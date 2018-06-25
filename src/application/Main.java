package application;
	
import java.util.ArrayList;
import java.util.List;

import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


public class Main extends Application {
	
	private AnimationTimer timer;

    private Pane root;
    // List of cars
    private List<Node> cars = new ArrayList<>();
    // List of logs
    private List<Node> logs = new ArrayList<>();
    
    private Node frog;
    
    private int lives = 3;
    
    public Text livestxt;
    
    public int elapsedTime;   // Not Working Props
    
    // -Nodes
    // Node Frog
    private Node initFrog() {
        Rectangle rect = new Rectangle(38, 38, Color.GREEN);
        rect.setTranslateY(600 - 39);
        rect.setTranslateX(350);

        return rect;
    }
    // Node Logs
    private Node spawnLogs() {
        Rectangle rect = new Rectangle(60, 40, Color.DARKSLATEGRAY);
        // Car Spawn Area
        rect.setTranslateY((int)(Math.random() * 14) * 15);
        rect.setTranslateX(800);

        root.getChildren().add(rect);
        return rect;
    }
    // Node Cars
    private Node spawnCar() {
        Rectangle rect = new Rectangle(40, 40, Color.DARKRED);
        // Car Spawn Area
        rect.setTranslateY((int)(Math.random() * 14) * 15 + 300);

        root.getChildren().add(rect);
        return rect;
       
    }
    //--------------------------------
    
    private void onUpdate() {
    	// Spawn factory for cars 
        for (Node car : cars)
        	// Car Speed in X lane
            car.setTranslateX(car.getTranslateX() + 5);
        // Spawn factory for logs 
        for (Node log : logs)
        	// Log Speed in X lane
            log.setTranslateX(log.getTranslateX() - Math.random() * 5);
        
     // When a new Car should be added  - 75 or 55 medium - 14 to 45 easy - 90+ hard
        if (Math.random() < 0.035) {
            cars.add(spawnCar());
            logs.add(spawnLogs());
        }

        checkState();
        
    }
    
    private void checkState() {
    	// Testing, not working though
    	elapsedTime++;
    	
    	// Collision for cars and logs    	   	
        for (Node car : cars) {
            if (car.getBoundsInParent().intersects(frog.getBoundsInParent())) {
                frog.setTranslateY(600 - 39);
                lives--;
                livestxt.setText("Lives: " + lives);
                return;
            }
             
        }
        for (Node log : logs) {
            if (log.getBoundsInParent().intersects(frog.getBoundsInParent())) {
                frog.setTranslateY(600 - 39);
                lives--;
                livestxt.setText("Lives: " + lives);
                return;
            }
        }
        // Invisible Walls for frog
        if (frog.getTranslateX()< 0) {
        	frog.setTranslateX(frog.getTranslateX() + 40);
		}
        if(frog.getTranslateX() >= 800) {
        	frog.setTranslateX(frog.getTranslateX() - 40);
        }
        if(frog.getTranslateY() > 600) {
        	frog.setTranslateY(frog.getTranslateY() - 40);
        }
        
        // Condition - Collision to Win
        if (frog.getTranslateY() <= 0) {
        	
        	livestxt.setText("Time: " + elapsedTime);
        	timer.stop();
            String win = "YOU WIN!";

            HBox hBox = new HBox();
            hBox.setTranslateX(300);
            hBox.setTranslateY(250);
            root.getChildren().add(hBox);
            
            

            for (int i = 0; i < win.toCharArray().length; i++) {
                char letter = win.charAt(i);

                Text text = new Text(String.valueOf(letter));
                text.setFont(Font.font(48));
                text.setOpacity(0);

                hBox.getChildren().add(text);
                
                // Fade In and Out
                FadeTransition ft = new FadeTransition(Duration.seconds(0.66), text);
                ft.setToValue(1);
                ft.setDelay(Duration.seconds(i * 0.15));
                ft.play();
                
                FadeTransition ft2 = new FadeTransition(Duration.seconds(2), text);
                ft2.setFromValue(1);
                ft2.setToValue(0);
                ft2.setDelay(Duration.seconds(i * 1));
                ft2.play();
            }
        }
        
     // Condition to Lose
        if (lives == 0) {
			
       	 timer.stop();
            String lose = "YOU LOSE!";

            HBox hBox = new HBox();
            hBox.setTranslateX(300);
            hBox.setTranslateY(250);
            root.getChildren().add(hBox);

            for (int i = 0; i < lose.toCharArray().length; i++) {
                char letter = lose.charAt(i);

                Text text = new Text(String.valueOf(letter));
                text.setFont(Font.font(48));
                text.setOpacity(0);

                hBox.getChildren().add(text);
                
                // Fade In and Out
                FadeTransition ft = new FadeTransition(Duration.seconds(0.66), text);
                ft.setToValue(1);
                ft.setDelay(Duration.seconds(i * 0.15));
                ft.play();
                
                FadeTransition ft2 = new FadeTransition(Duration.seconds(2), text);
                ft2.setFromValue(1);
                ft2.setToValue(0);
                ft2.setDelay(Duration.seconds(i * 1));
                ft2.play();
       	 
            } 
       }
        
    }
  
	// Parent node for start Stage
    private Parent createContent() {
        root = new Pane();
        root.setPrefSize(800, 600);
        
        // Background -----
        Rectangle back1 = new Rectangle(800, 300, Color.BLUE);
        root.getChildren().add(back1); 
        
        Rectangle back2 = new Rectangle(800, 400, Color.GREY);
        back2.setTranslateY(300);
        root.getChildren().add(back2); 
        //------------------
        
        frog = initFrog();

        root.getChildren().add(frog);
        
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                onUpdate();
            }
        };
        //timer.start(); //autostart

        return root;
    }
	
	@Override
	public void start(Stage stage) throws Exception{
		try {
			stage.setScene(new Scene(createContent()));

	        stage.getScene().setOnKeyPressed(event -> {
	            switch (event.getCode()) {
	                case W:
	                    frog.setTranslateY(frog.getTranslateY() - 40);
	                    break;
	                case S:
	                    frog.setTranslateY(frog.getTranslateY() + 40);
	                    break;
	                case A:
	                    frog.setTranslateX(frog.getTranslateX() - 40);
	                    break;
	                case D:
	                    frog.setTranslateX(frog.getTranslateX() + 40);
	                    break;
	                default:
	                    break;
	            }
	        });
	        
	        // Text area for points
	        livestxt = new Text();
	        livestxt.setFont(Font.font(28));
	        livestxt.setTranslateX(680);
	        livestxt.setTranslateY(580);
	        livestxt.setText("Lives: " + lives);
	        root.getChildren().add(livestxt);  
	        
	        // new game Button - Not working well
	        Button btn = new Button("Start new game");
	        btn.setOnAction(event -> {
	        	timer.start();
	        	frog.setTranslateY(600 - 39);
	        	lives = 3;
	        	livestxt.setText("Lives: 3");
	        	
	        });
	        root.getChildren().add(btn);
	        // ---
	        
	        stage.show();
	        
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
		
		
	}
}
