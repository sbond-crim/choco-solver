/**
 * Copyright (c) 2016, Ecole des Mines de Nantes
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    This product includes software developed by the <organization>.
 * 4. Neither the name of the <organization> nor the
 *    names of its contributors may be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY <COPYRIGHT HOLDER> ''AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.chocosolver.util.objects.setDataStructures.swapList;

import org.chocosolver.memory.IEnvironment;
import org.chocosolver.memory.IStateInt;

/**
 * Set of integers based on BipartiteSet implementation
 * BEWARE : CANNOT BOTH ADD AND REMOVE ELEMENTS DURING SEARCH
 * (add only or remove only)
 *
 * add : O(1)
 * testPresence: O(1)
 * remove: O(1)
 * iteration : O(m)
 *
 * @author : Jean-Guillaume Fages
 */
public class Set_Std_Swap extends Set_Swap {

	//***********************************************************************************
	// VARIABLES
	//***********************************************************************************

    protected IStateInt size;

	//***********************************************************************************
	// CONSTRUCTOR
	//***********************************************************************************

	/**
	 * Creates an empty bipartite set having numbers greater or equal than <code>offSet</code> (possibly < 0)
	 * @param e backtracking environment
	 * @param offSet smallest allowed value in this set (possibly < 0)
	 */
	public Set_Std_Swap(IEnvironment e, int offSet){
		super(offSet);
		size = e.makeInt(0);
	}

	//***********************************************************************************
	// METHODS
	//***********************************************************************************

    @Override
    public int getSize() {
        return size.get();
    }

    protected void setSize(int s) {
        size.set(s);
    }

    protected void addSize(int delta) {
        size.add(delta);
    }
}
