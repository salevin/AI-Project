package ai.worlds.wumpus;

import ai.logic.Logic;
import ai.worlds.Location;

import java.util.Vector;

/**
 * Created by sam on 10/13/15.
 */
public class SamsWumpusAgent extends WumpusAgent {

    /**
     * A list of planned moves
     */
    Vector plan = new Vector();
    /**
     * A flag indicating if the wumpus is still alive
     */
    boolean wumpusAlive = true;
    boolean hasGold = false;
    Location home = new Location(1,1);

    public SamsWumpusAgent() {
        super();
        logic = new SamsWumpusLogic(4, 4); //Logic
    }


    @Override
    public void determineAction() {

        Vector p = (Vector) percept;


        if (p.elementAt(4) =="sound") wumpusAlive=false;
        if (p.elementAt(2) =="glitter"){
            action = "grab";
            hasGold = true;
            plan = logic.pathTo(home,body.heading);
        }
        else if (!plan.isEmpty()){
            action = (String)plan.elementAt(0);
            plan.removeElementAt(0);
        }
        else if (p.elementAt(1)=="breeze" && wumpusAlive && p.elementAt(0)=="stench"){
            update(p);
            Location move = logic.nextMove();
            plan = logic.pathTo(move,body.heading);
        }

        else if (p.elementAt(1)=="breeze") {
            update(p);
            Location move = logic.nextMove();
            plan = logic.pathTo(move,body.heading);
        }
        else if (wumpusAlive && p.elementAt(0)=="stench") {
            update(p);
            Location move = logic.nextMove();
            plan = logic.pathTo(move,body.heading);
            }

        else {
            update(p);
            Location move = logic.nextMove();
            plan = logic.pathTo(move,body.heading);
        }
        if (logic.agentloc.equals(home)){
            System.out.println("hey");
            update(p);
            if (hasGold || logic.okayMoves().isEmpty() ){
                action = "climb";
            }
        }



    }

    private void update(Vector p) {
        String b = "", s = "";
        if(p.elementAt(1)=="breeze") b = "breeze";
        if(p.elementAt(0)=="stench") s = "stench";
        logic.percept(s, b);
        logic.getStatus();
    }
}
