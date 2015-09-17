/**
 * Created by sam on 9/1/15.
 */
package ai.worlds.vacuum;
import java.util.*;


// My Vacuum Agent

public class SamVacuumAgent extends VacuumAgent
{

    // declare any state variables here
    String lastMove = "none";
    Integer horizontalDistance = null;
    Integer currentHoriz = 1;
    Integer currentVert = 1;
    Integer moveNumber = 0;
    Boolean inLeftTurn = false;
    Boolean inRightTurn = false;
    Boolean movingLeft = false;
    Boolean goingToHome = false;
    Boolean turnAround = false;
    Boolean goForward = false;
    Boolean inALine =false;

    public void determineAction()
    {

        Vector p = (Vector) percept;
        if (turnAround) {
        lastMove = action = "turn right";
        turnAround = false;
    }
        else if (goForward) {
            lastMove = action = "forward";
            goForward = false;
        }
        else if (p.elementAt(1) == "dirt") action = "suck";
        else if (p.elementAt(2) == "home") {
            if (lastMove == "none") {
                lastMove = action = "forward";
                moveNumber++;
            }
            else if (horizontalDistance == null){
                inALine =true;
                lastMove = action = "turn left";
                moveNumber++;
                goForward = true;
                horizontalDistance = 1;
            }
            else {
                lastMove = action = "shut-off";
            }
        }
        else if (inALine) {
            if (p.elementAt(0) == "bump") {
                lastMove = action = "turn right";
                moveNumber++;
                turnAround = true;
            }
            else {
                lastMove = action = "forward";
                moveNumber++;
            }
        }
        else if (goingToHome) {
            if (currentVert == 1 && !movingLeft){
                lastMove = action = "turn right";
                movingLeft = true;
                moveNumber++;
            }
            else {
                lastMove = action = "forward";
                moveNumber++;
                currentVert--;
            }
        }
        else if (p.elementAt(0) == "bump") {
            if (inLeftTurn||inRightTurn){
                horizontalDistance = currentHoriz;
                goingToHome = true;
                lastMove = action = "turn right";
                moveNumber++;
                turnAround = true;
            }
            else {
                horizontalDistance = currentHoriz;
                if (movingLeft) {
                    lastMove = action = "turn right";
                    moveNumber++;
                    inRightTurn = true;
                }
                else {
                    lastMove = action = "turn left";
                    moveNumber++;
                    inLeftTurn = true;
                }
            }

        }
        else if (inLeftTurn) {

            if (lastMove=="forward"){
                lastMove = action = "turn left";
                moveNumber++;
                inLeftTurn = false;
                currentVert++;
                movingLeft = true;
            }
            else {
                lastMove = action = "forward";
                moveNumber++;
                currentHoriz=1;
                System.out.println(currentVert);
            }
        }
        else if (inRightTurn) {

            if (lastMove=="forward"){
                lastMove = action = "turn right";
                moveNumber++;
                inRightTurn = false;
                currentVert++;
                movingLeft = false;
            }
            else {
                lastMove = action = "forward";
                moveNumber++;
                currentHoriz=1;
                System.out.println(currentVert);
            }
        }
        else if (currentHoriz==horizontalDistance && !goingToHome) {
            if (movingLeft) {
                lastMove = action = "turn right";
                moveNumber++;
                inRightTurn = true;
            }
            else {
                lastMove = action = "turn left";
                moveNumber++;
                inLeftTurn = true;
            }
        }
        else {
            lastMove = action = "forward";
            moveNumber++;
            currentHoriz++;
        }
    }
}