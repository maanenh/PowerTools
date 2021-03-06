/*	Copyright 2012 by Martin Gijsen (www.DeAnalist.nl)
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

package org.powertools.engine.symbol;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;
import org.powertools.engine.ExecutionException;
import org.powertools.engine.Symbol;


public class ConstantTest {
	@Before
	public void setUp () throws Exception {
		mScope		= new Scope (null);
		mConstant	= new Constant (NAME, mScope, VALUE);
	}

	
	@Test
	public void testConstant () {
		assertFalse (mConstant == null);
	}

	
	@Test
	public void testGetValue () {
		assertEquals (VALUE, mConstant.getValue (NAME));
	}

	@Test (expected=ExecutionException.class)
	public void testGetValueWithInvalidName () {
		assertEquals (VALUE, mConstant.getValue (INVALID_NAME));
	}

	@Test (expected=ExecutionException.class)
	public void testSetValueStringString () {
		mConstant.setValue (NAME, "new value");
	}

	@Test (expected=ExecutionException.class)
	public void testSetValueString () {
		mConstant.setValue ("new value");
	}

	@Test (expected=ExecutionException.class)
	public void testClear () {
		mConstant.clear ("a.b".split (Symbol.PERIOD));
	}


	@Test
	public void testGetName () {
		assertEquals (NAME, mConstant.getName ());
	}

	
	// private members
	private static final String NAME			= "name";
	private static final String INVALID_NAME	= "name.something";
	private static final String VALUE			= "value";
	
	private Scope mScope;
	private Constant mConstant;
}