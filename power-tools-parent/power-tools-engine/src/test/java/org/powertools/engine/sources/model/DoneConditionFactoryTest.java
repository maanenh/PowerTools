/*	Copyright 2013 by Martin Gijsen (www.DeAnalist.nl)
 *
 *	This file is part of the PowerTools engine.
 *
 *	The PowerTools engine is free software: you can redistribute it and/or
 *	modify it under the terms of the GNU Affero General Public License as
 *	published by the Free Software Foundation, either version 3 of the License,
 *	or (at your option) any later version.
 *
 *	The PowerTools engine is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *	GNU Affero General Public License for more details.
 *
 *	You should have received a copy of the GNU Affero General Public License
 *	along with the PowerTools engine. If not, see <http://www.gnu.org/licenses/>.
 */


package org.powertools.engine.sources.model;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;
import org.powertools.engine.ExecutionException;


public class DoneConditionFactoryTest {
    @Test
    public void testCreate () {
        DoneConditionFactory factory = new DoneConditionFactory ();
        assertTrue (factory.create ("never") instanceof NeverDone);
        assertTrue (factory.create ("end node") instanceof DoneWhenInEndNode);
        assertTrue (factory.create ("all nodes") instanceof DoneWhenAllNodesSeen);
        assertTrue (factory.create ("all edges") instanceof DoneWhenAllEdgesSeen);
        try {
            factory.create ("unknown condition");
            fail ("no exception");
        } catch (ExecutionException ee) {
            // ok
        }
    }
}
