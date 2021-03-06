/* Copyright 2013-2014 by Martin Gijsen (www.DeAnalist.nl)
 *
 * This file is part of the PowerTools engine.
 *
 * The PowerTools engine is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * The PowerTools engine is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with the PowerTools engine. If not, see <http://www.gnu.org/licenses/>.
 */

package org.powertools.engine.sources.model;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.powertools.engine.ExecutionException;
import org.powertools.engine.RunTime;


final class RandomEdgeSelector implements EdgeSelectionStrategy {
    static final String NAME = "random";
    
    private static final String DESCRIPTION = "select a random outgoing edge";

    private final DirectedGraph mMainGraph;
    private final RunTime mRunTime;
    private final RandomNumberGenerator mNumberGenerator;


    RandomEdgeSelector (DirectedGraph mainGraph, RunTime runTime, RandomNumberGenerator numberGenerator) {
        super ();
        mMainGraph       = mainGraph;
        mRunTime         = runTime;
        mNumberGenerator = numberGenerator;
    }

    @Override
    public String getDescription () {
        return NAME + " (" + DESCRIPTION + ")";
    }

    @Override
    public Edge selectEdge (DirectedGraph graph, Node currentNode) {
        boolean isMainGraph = (graph == mMainGraph);
        boolean atEndNode   = false;
        if (isMainGraph) {
            if (currentNode.mLabel.equalsIgnoreCase (Model.END_NODE_LABEL)) {
                atEndNode = true;
            }
        } else {
            if (graph.getEdges (currentNode).isEmpty ()) {
                return null;
            }
        }

        Set<Edge> remainingEdges = new HashSet<Edge> (graph.getEdges (currentNode));
        while (!remainingEdges.isEmpty ()) {
            Edge edge = removeRandomEdge (remainingEdges);
            if (conditionNoProblem (edge)) {
                return edge;
            }
        }

        if (atEndNode) {
            return graph.addEdge (currentNode, graph.getStartNode ());
        } else {
            throw new ExecutionException (String.format ("no edges out of end node '%s'", currentNode.getName ()));
        }
    }

    private boolean conditionNoProblem (Edge edge) {
        if ("".equals (edge.mCondition)) {
            return true;
        } else if (returnsTrue (edge.mCondition)) {
            return true;
        } else {
            return false;
        }
    }

    private Edge removeRandomEdge (Set<Edge> remainingEdges) {
        Edge edge           = null;
        Iterator<Edge> iter = remainingEdges.iterator ();
        int number          = mNumberGenerator.generate (remainingEdges.size ());
        for (int counter = 0; counter <= number; ++counter) {
            edge = iter.next ();
        }
        iter.remove ();
        return edge;
    }

    private boolean returnsTrue (String condition) {
        String value = mRunTime.evaluateExpression (condition);
        mRunTime.reportInfo (String.format ("condition '%s' is %s", condition, value));
        return "true".equals (value);
    }
}
