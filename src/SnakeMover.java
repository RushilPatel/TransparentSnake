/*
 * Author: Rushil Patel, rushil2011@my.fit.edu
 * Fall 2012 Project: snake
 */
import java.util.ArrayList;
import javax.swing.JOptionPane;

public final class SnakeMover {


    private static final int ONE = 1;
    private static final int HUNDRED = 100;
    private static final int TWO = 2;
    private static final int FIVE = 5;
    private static int length; // Length of the game screen
    private static int height; // Height of the game screen
    private static int xScale; // sets screen scale for x-axis
    private static int yScale; // sets screen scale for y-axis
    private final int foodRadius = 20; // radius of the food (5)
    private final int snakeRadius = 20; // radius of the snake (5)
    private final int move = snakeRadius ; // number points to skip for the
                                                // next move
    // Total number of spots the snake can fill up on the screen
    private final int totalPositions = ( length / move) * ( height / move);
    private final FoodGenerator food; // Generates food at random places
    private int xFood_one; // xCoordinate of first food
    private int yFood_one; // yCoordinate of first food
    private int xSnake = 0; // xCoordinate of the snake's head
    private int ySnake = 0; // yCoordinate of the snake's head
    private int snakeLength = FIVE; // Tracks the length of the snake
    private final int snakeSpeed = HUNDRED; // Speed at which the snake will
                                            // travel
    // Store the previous xPosition of the snake
    private final ArrayList<Integer> xPoints = new ArrayList<> ();
    // Store the previous yPosition of the snake
    private final ArrayList<Integer> yPoints = new ArrayList<> ();
    private final ScoreTracker score = new ScoreTracker (); // handles the score
    private int noOfmoves = 0; // tracks the total number of moves

    public SnakeMover (final int xWidth, final int yWidth) {
        length = xWidth;
        height = yWidth;
        xScale = length / TWO;
        yScale = height / TWO;
        // Creates a new window for the game
        MyStdDraw.setOpacity (1);
        MyStdDraw.setCanvasSize (length, height);
        MyStdDraw.setXscale (-xScale, xScale);
        MyStdDraw.setYscale (-yScale, yScale);
        food = new FoodGenerator (xScale - foodRadius, yScale - foodRadius,
                foodRadius);

        // sets up the initial length and position of the snake
        xSnake = -( TWO * move);
        xPoints.add (xSnake); // Sets the xHead
        yPoints.add (0);
        xPoints.add (-move);
        yPoints.add (0);
        xPoints.add (0);
        yPoints.add (0);
        xPoints.add (move);
        yPoints.add (0);
        xPoints.add ( ( TWO * move));
        yPoints.add (0);
        generateFood (ONE); // generates first food
        generateFood (TWO); // generates second food
        
    }

    // moves the snake
    public boolean draw (final boolean playing) {

        MyStdDraw.show (snakeSpeed); // sets the snake speed
        MyStdDraw.clear (); // sets the game background
        MyStdDraw.setPenColor (MyStdDraw.RED); // sets the color of first food
        // draw the first food on the screen
        MyStdDraw.filledSquare (xFood_one, yFood_one, foodRadius);

        // if the snake is moving then move the body
        // of the snake along with its head
        if (playing) {
            noOfmoves++;
            for (int i = snakeLength - ONE; i > 0; i--) {
                xPoints.set (i, xPoints.get (i - ONE));
                yPoints.set (i, yPoints.get (i - ONE));
            }
            xPoints.set (0, xSnake);
            yPoints.set (0, ySnake);
        }

        // moves the snake on the screen
        for (int i = 0; i < snakeLength; i++) {

                MyStdDraw.setPenColor (MyStdDraw.BLUE);
                MyStdDraw.filledCircle (xPoints.get (i), yPoints.get (i),
                        snakeRadius);

        }
        // Sets the color of the wall
        MyStdDraw.setPenColor (MyStdDraw.BLUE);
        // Draw the walls
        MyStdDraw.line (-xScale, -yScale, xScale, -yScale);
        MyStdDraw.line (-xScale, -yScale, -xScale, yScale);
        MyStdDraw.line (-xScale, yScale, xScale, yScale);
        MyStdDraw.line (xScale, -yScale, xScale, yScale);
        return ( checkPoints ());

    }

    // sets the direction the snake
    public void setDirection (final String key) {
        // moves snake upwards on the screen
        if (key.equals ("up")) {
            ySnake += move;
            // moves snake downwards on the screen
        } else if (key.equals ("down")) {
            ySnake -= move;
            // moves snake towards left on the screen
        } else if (key.equals ("left")) {
            xSnake -= move;
            // moves snake towards right on the screen
        } else if (key.equals ("right")) {
            xSnake += move;
        }
    }

    // Checks the position of the snake
    private boolean checkPoints () {
        // if snake hits the wall or eats itself then end the game and
        // display the score
        if ( ( ( Math.abs (xSnake) + snakeRadius) > xScale)
                || ( ( Math.abs (ySnake) + snakeRadius) > yScale)
                || checkCollision ()) {
            final String message = String.format (
                    "You Lost !!!\nYour score: %d", score.getScore ());
            JOptionPane.showMessageDialog (null, message, "Game Over !!!", TWO);
            return true;

        } else if ( ( Math.abs(xSnake - xFood_one) < snakeRadius && ( Math.abs( ySnake - yFood_one) < snakeRadius))){
            ateFood (ONE);
            return false;
            
        } else if (snakeLength == ( totalPositions - TWO)) {
            final String message = String.format (
                    "You Win !!!\nYour score: %d", score.getScore ());
            JOptionPane.showMessageDialog (null, message, "Game Over!", TWO);
            return true;
        } else {
            return false;
        }
    }

    // This checks whether that snake has eaten itself or not
    private boolean checkCollision () {
        boolean status = false;
        final int xHead = xPoints.get (0);
        final int yHead = yPoints.get (0);
        if (noOfmoves > ONE) {
            // see if the head has collided with its body
            for (int i = ONE; i < snakeLength; i++) {

                if ( ( xHead == xPoints.get (i)) && ( yHead == yPoints.get (i))) {
                    System.out.println (i + " " + xPoints.get (i) + " "
                            + yPoints.get (i));
                    status = true;
                }
            }
        }
        return status;
    }

    // generates a new food when the snake eats the old one
    // and increases the size of the snake
    private void ateFood (final int foodNo) {
        generateFood (foodNo);
        score.addScore ();
        snakeLength++;
        xPoints.add (xSnake);
        yPoints.add (ySnake);
    }

    // generates food
    private void generateFood (final int foodNo) {
        // generate the first food upon request
        if (foodNo == ONE) {
            xFood_one = food.newX ();
            yFood_one = food.newY ();
                for (int i = 0; i < snakeLength; i++) {
                    if ( ( ( xFood_one == xPoints.get (i)) && ( yFood_one == yPoints
                            .get (i)))) {
                        generateFood (ONE);
                        return;
                    }
            }

        }
    }
    public void decompose(){
        xPoints.clear ();
        yPoints.clear ();
    }
}
