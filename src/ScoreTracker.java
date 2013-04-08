/*
 * Author: Rushil Patel, rushil2011@my.fit.edu
 * Fall 2012 Project: snake
 */
public final class ScoreTracker {

    // Tracks the game score
    private int score = 0;

    // Adds the score
    public void addScore () {
        score++;
    }

    // Return the score
    public int getScore () {
        return score;
    }
}
