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
        logic.move(body.loc.x, body.loc.y);
        String b = "", s = "";
        if(p.elementAt(1)=="breeze") b = "breeze";
        if(p.elementAt(0)=="stench") s = "stench";
        logic.percept(s, b);
        Location wumpusLocation = logic.findWumpus();

        if (p.elementAt(4) =="sound") wumpusAlive=false;
        if (p.elementAt(2) =="glitter"){
            action = "grab";
            hasGold = true;
            plan = logic.pathTo(home,body.heading);
        }
        else {
            if (wumpusLocation!=null){
                System.out.println("Found wumpus at " + wumpusLocation);
                plan = logic.pathTo(wumpusLocation,body.heading);
                plan.removeElementAt(plan.size()-1);
                plan.addElement("shoot");
            }
            if (!plan.isEmpty()) {
                action = (String) plan.elementAt(0);
                plan.removeElementAt(0);
            } else {
                update(p);
                Location move = logic.nextMove();
                plan = logic.pathTo(move, body.heading);
                if (!plan.isEmpty()) {
                    action = (String) plan.elementAt(0);
                    plan.removeElementAt(0);
                }
            }
            if (logic.agentloc.equals(home)) {
                if (hasGold || logic.okayMoves().isEmpty()) {
                    action = "climb";
                }
            }
        }
        System.out.println(plan);
        System.out.println(logic.agentloc);
        System.out.println(action);
    }

    private void update(Vector p) {
        String b = "", s = "";
        if(p.elementAt(1)=="breeze") b = "breeze";
        if(p.elementAt(0)=="stench") s = "stench";
        logic.percept(s, b);
        logic.getStatus();
    }
}
