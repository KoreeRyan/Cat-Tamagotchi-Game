import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.geometry.Pos;

public class Main extends Application {

    private Cat cat;

    private Label fullnessLabel;
    private Label happinessLabel;
    private Label energyLabel;
    private Label cleanlinessLabel;
    private Label sleepLabel;
    private Label catArtLabel;
    private Label moodLabel;
    private Label warningLabel;
    private Button feedButton;
    private Button playButton;
    private Button cleanButton;
    private Button startOverButton;
    private Timeline gameLoop;

    @Override
    public void start(Stage primaryStage) {

        cat = new Cat();

        catArtLabel = new Label();
        catArtLabel.getStyleClass().add("cat-art");

        moodLabel = new Label();
        moodLabel.getStyleClass().add("mood-label");


        warningLabel = new Label();
        warningLabel.getStyleClass().add("warning-label");

        fullnessLabel = new Label();
        fullnessLabel.getStyleClass().add("stat-label");

        happinessLabel = new Label();
        happinessLabel.getStyleClass().add("stat-label");

        energyLabel = new Label();
        energyLabel.getStyleClass().add("stat-label");

        cleanlinessLabel = new Label();
        cleanlinessLabel.getStyleClass().add("stat-label");

        sleepLabel = new Label();
        sleepLabel.getStyleClass().add("stat-label");

        feedButton = new Button("Feed");
        playButton = new Button("Play");
        cleanButton = new Button("Clean");
        startOverButton = new Button("Start Over");

        feedButton.getStyleClass().add("cute-button");
        playButton.getStyleClass().add("cute-button");
        cleanButton.getStyleClass().add("cute-button");
        startOverButton.getStyleClass().add("startover-button");

        feedButton.setOnAction(event -> {
            cat.feed();
            updateLabels();
        });

        playButton.setOnAction(event -> {
            cat.play();
            updateLabels();
        });

        cleanButton.setOnAction(event -> {
            cat.clean();
            updateLabels();
        });

        startOverButton.setOnAction(event -> {
            resetGame();
        });

        VBox card = new VBox(10);
        card.getStyleClass().add("cat-card");
        card.setAlignment(Pos.CENTER);
        card.getChildren().addAll(
                catArtLabel,
                moodLabel,
                warningLabel,
                fullnessLabel,
                happinessLabel,
                energyLabel,
                cleanlinessLabel,
                sleepLabel,
                feedButton,
                playButton,
                cleanButton,
                startOverButton
        );

        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(card);

        updateLabels();

        gameLoop = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> {
                    cat.updateStatus();
                    updateLabels();
                    if (cat.isGameOver()) {
                        gameLoop.stop();
                    }
                })

        );
        gameLoop.setCycleCount(Timeline.INDEFINITE);
        gameLoop.play();

        Scene scene = new Scene(root, 320, 380);

        scene.getStylesheets().add(
                getClass().getResource("/style/style.css").toExternalForm()
        );

        primaryStage.setTitle("Cat Tamagotchi");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void updateLabels() {
        //Stats
        fullnessLabel.setText("Fullness: " + cat.getFullness());
        happinessLabel.setText("Happiness: " + cat.getHappiness());
        energyLabel.setText("Energy: " + cat.getEnergy());
        cleanlinessLabel.setText("Cleanliness: " + cat.getCleanliness());

        //Status + ASCII cat + mood + warnings
        if (cat.isGameOver()) {
            sleepLabel.setText("Status: Game Over");

            //Dead/tired cat
            String art = " /\\_/\\\n( x_x )\n > ^ <";
            catArtLabel.setText(art);

            moodLabel.setText("Game over: your cat has left. . .");
            warningLabel.setText("Fullness or happiness reached 0.");

            //Disable buttons so player can't keep interacting
            feedButton.setDisable(true);
            playButton.setDisable(true);
            cleanButton.setDisable(true);

        } else {

            sleepLabel.setText(cat.isSleeping() ? "Status: Sleeping" : "Status: Awake");

            // ASCII cat based on sleep + happiness
            String art;
            if (cat.isSleeping()) {
                art = " /\\_/\\\n( -.- ) zZ\n > ^ <";
                moodLabel.setText("Your cat is sleeping.");
            } else {
                int h = cat.getHappiness();
                if (h>= 70) {
                    art = " /\\_/\\ \n( ^.^ )\n > ^ <";
                    moodLabel.setText("Your cat is happy.");
                } else if (h >= 40) {
                    art = " /\\_/\\\n( -.- )\n > ^ <";
                    moodLabel.setText("Your cat feels okay.");
                } else {
                    art = " /\\_/\\\n( -.- )\n > ^ <";
                    moodLabel.setText("Your cat is unhappy.");
                }
            }

            catArtLabel.setText(art);

            if (cat.getFullness() <= 30 && cat.getHappiness() <= 30) {
                warningLabel.setText("Warning: Your cat is very hungry and very unhappy!");
            } else if (cat.getFullness() <= 30) {
                warningLabel.setText("Warning: Your cat is very hungry.");
            } else if (cat.getHappiness() <= 30) {
                warningLabel.setText("Warning: Your cat is very unhappy.");
            } else {
                warningLabel.setText("");
            }

            //Buttons enabled during play
            feedButton.setDisable(false);
            playButton.setDisable(false);
            cleanButton.setDisable(false);

        }
    }

    private void resetGame() {


        cat = new Cat();

        feedButton.setDisable(false);
        playButton.setDisable(false);
        cleanButton.setDisable(false);

        updateLabels();

        if (gameLoop != null) {
            gameLoop.playFromStart();
        }

    }

    public static void main(String[] args) {
        launch(args);
    }
}





