package wood.game;

import wood.graphics.UserInterface;
import wood.replay.Replay;
import wood.replay.ReplayIO;
import wood.strategy.*;

import javax.swing.*;

public class WoodTheGathering {
    private static final int DEFAULT_BOARD_SIZE = 10;
    private static final boolean DEFAULT_GUI_ENABLED = false;
    private static final int PREFERRED_GUI_WIDTH = 750; // Bump this up or down according to your screen size

    public static void main(String[] args) {
        boolean guiEnabled = DEFAULT_GUI_ENABLED;
        String replayFilePath = null; // Use this if you want to replay a past match
        final GameEngine gameEngine;

        if(replayFilePath == null) {
            WoodPlayerStrategy redStrategy =  new TrainingStrategy();
            WoodPlayerStrategy blueStrategy = new RandomStrategy();
            gameEngine = new GameEngine(DEFAULT_BOARD_SIZE, redStrategy, blueStrategy);
            gameEngine.setGuiEnabled(guiEnabled);
        } else {
            gameEngine = ReplayIO.setupEngineForReplay(replayFilePath);
            guiEnabled = false;
        }

        if(gameEngine == null) {
            return;
        }

        //TODO reinstate if needed
        /*
        if(guiEnabled) {
            // Run the GUI code on a separate Thread (The event dispatch thread)
            SwingUtilities.invokeLater(() -> UserInterface.instantiateGUI(gameEngine, PREFERRED_GUI_WIDTH));
        }
        */

        long startTime = System.nanoTime();

        gameEngine.runGame();

        long endTime   = System.nanoTime();
        long totalTime = endTime - startTime;
        System.out.println(totalTime / 1000000000.0);

        // Record the replay if the output path isn't null and we aren't already watching a replay
        String replayOutputFilePath = null;
        if(replayFilePath == null && replayOutputFilePath != null) {
            Replay gameReplay = gameEngine.getReplay();
            ReplayIO.writeReplayToFile(gameReplay, replayOutputFilePath);
        }
    }
}
