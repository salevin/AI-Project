package ai.worlds.wumpus;

import ai.logic.HornKnowledgeBase;
import ai.logic.Logic;
import ai.search.Problem;
import ai.worlds.Location;

import java.util.Vector;

/**
 * Created by sam on 9/29/15.
 */
public class SamsWumpusLogic extends WumpusLogic {

    public SamsWumpusLogic(int x, int y) { super(x, y); }
    /**
     * Make a knowledge base.
     * @param kb is the knowledge base that will be constructed.
     */
    public void makeKB(HornKnowledgeBase kb)
    // construct the wumpus knowledge base (this kb is incomplete)
    {
        for (int x=1; x<=size.x; x++)
            for (int y=1; y<=size.y; y++) {
                try{
                    if (x>1) kb.tell(Logic.parse("neighbor(" + x + "," + y + "," + (x - 1) + "," +
                            y + ")"));
                    if (x<size.x) kb.tell(Logic.parse("neighbor(" + x + "," + y + "," + (x+1) + "," +
                            y + ")"));
                    if (y>1) kb.tell(Logic.parse("neighbor(" + x + "," + y + "," + x + "," +
                            (y-1) + ")"));
                    if (y<size.y) kb.tell(Logic.parse("neighbor(" + x + "," + y + "," + x + "," +
                            (y+1) + ")"));
                }
                catch(Exception e) {}
            }
        try{
            kb.tell(Logic.parse("wOK(1,1)"));
            kb.tell(Logic.parse("pOK(1,1)"));
            kb.tell(Logic.parse("$x1=$x2 & $y1=$y2 -> same($x1,$y1,$x2,$y2)"));
            kb.tell(Logic.parse("neighbor($x1,$y1,$x3,$y3) & " +
                    "~same($x2,$y2,$x3,$y3) -> " +
                    "otherneighbor($x1,$y1,$x2,$y2,$x3,$y3)"));
            kb.tell(Logic.parse("otherneighbor($x1,$y1,$x2,$y2,$x3,$y3) & w?($x3,$y3) -> " +
                    "neighborwithw($x1,$y1,$x2,$y2)"));
            kb.tell(Logic.parse("~neighborwithw($x1,$y1,$x2,$y2) ->" +
                    "allothersWfree($x1,$y1,$x2,$y2)"));
            kb.tell(Logic.parse("otherneighbor($x1,$y1,$x2,$y2,$x3,$y3) & p?($x3,$y3) ->" +
                    "neighborwithp($x1,$y1,$x2,$y2)"));
            kb.tell(Logic.parse("~neighborwithp($x1,$y1,$x2,$y2) ->" +
                    "allothersPfree($x1,$y1,$x2,$y2)"));

            kb.tell(Logic.parse("nosmell($x1,$y1) & neighbor($x1,$y1,$x2,$y2) -> wOK($x2,$y2)"));
            kb.tell(Logic.parse("nobreeze($x1,$y1) & neighbor($x1,$y1,$x2,$y2) -> pOK($x2,$y2)"));
            kb.tell(Logic.parse("breeze($x1,$y1) & allothersPfree($x1,$y1,$x3,$y3) & neighbor($x1,$y1,$x3,$y3) & ~pOK($x3,$y3) -> p!($x3,$y3)"));
            kb.tell(Logic.parse("smell($x1,$y1) & allothersWfree($x1,$y1,$x3,$y3) & neighbor($x1,$y1,$x3,$y3) & ~wOK($x3,$y3) -> w!($x3,$y3)"));
            kb.tell(Logic.parse("breeze($x1,$y1) & neighbor($x1,$y1,$x3,$y3) & ~pOK($x3,$y3) -> p?($x3,$y3)"));
            kb.tell(Logic.parse("smell($x1,$y1) & neighbor($x1,$y1,$x3,$y3) & ~wOK($x3,$y3) -> w?($x3,$y3)"));
        }
        catch(Exception e) {System.out.println("Error in building kb");};
    }

    /**
     * Find the closest move from a list.
     * @param moves is a list of locations to which the agent can move.
     * @return the move that is closest to the agent's current location.
     */
    public Location closestMove(Vector moves) {
        Location closest = (Location) moves.get(0);
        for (int i = 1; i < moves.size(); i++){
            Location current = (Location) moves.elementAt(i);
            if (distance(agentloc, current) < distance(agentloc,closest) && current != closest){closest = current;}
        }
        return closest;
    }

    /**
     * Find the wumpus from a list.
     * @return the location of the wumpus
     */
    public Location findWumpus() {
        for (int x=1; x<=size.x; x++){
            for (int y=1; y<=size.y; y++){
                Vector v = (Vector) grid[x][y];
                String w = (String)v.elementAt(1);
                if (w=="w!"){
                    return new Location(x,y);
                }
            }
        }
        return null;
    }

    /**
     * Determine the next location the agent will visit.
     * @return the location that the agent will move to next.
     */
    public Location nextMove() {
        Vector moves = this.okayMoves();
        Location nextmove;
        if (moves.size() > 0){  nextmove = closestMove(moves); }
        else { nextmove = new Location(1,1); }
        return nextmove;
    }

    /**
     * Determine the sequence of actions which will take the agent to the given location.
     * @param loc is the location we wish to travel to.
     * @param heading is the agent's current heading.
     * @return the sequence of actions which will take the agent to the given location.
     */
    public Vector pathTo(Location loc, Location heading) {
        PathProblem path = new PathProblem(agentloc, heading, loc);
        return path.solve("No Duplicates Breadth First Search","",50).solutionActions();
    }

    public void removeW(){
        for (int x=1; x<=size.x; x++){
            for (int y=1; y<=size.y; y++){
                Vector v = (Vector) grid[x][y];
                v.set(1,"wOK");
            }
        }
    }
}
