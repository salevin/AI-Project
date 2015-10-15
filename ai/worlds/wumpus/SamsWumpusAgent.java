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
            plan = logic.pathTo(logic.agentloc,home);
        }
        if (logic.agentloc == home){
            if (hasGold || logic.okayMoves().isEmpty() ){
                action = "climb";
            }
        }
        else if (p.elementAt(3) =="bump") {
            plan.removeAllElements();
            plan.addElement("turn right");
            plan.addElement("forward");
            action = "turn right";
        }
        else if (!plan.isEmpty()){
            action = (String)plan.elementAt(0);
            plan.removeElementAt(0);
        }
        else if (p.elementAt(1)=="breeze" && wumpusAlive && p.elementAt(0)=="stench"){
            this.update(p);
        }

        else if (p.elementAt(1)=="breeze") {
            logic.percept("","breeze");
            logic.getStatus();
        }
        else if (wumpusAlive && p.elementAt(0)=="stench") {
            logic.percept("stench","");

            logic.getStatus();
            }

        else {
            update(p);
            Location move = logic.nextMove();
            plan = logic.pathTo(logic.agentloc, move);
            System.out.println(plan);
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
