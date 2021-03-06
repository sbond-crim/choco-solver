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
package org.chocosolver.solver.constraints.set;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.SetVar;
import org.chocosolver.util.ESat;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * @author Alexandre LEBRUN
 */
public class IntChannelTest {

    @Test(groups = "1s", timeOut=60000)
    public void testNominal() {
        Model model = new Model();

        IntVar[] intVars = model.intVarArray(5, 1, 5);
        SetVar[] setVars = model.setVarArray(5, new int[]{}, new int[]{0, 1, 2, 3, 4});

        model.setsIntsChanneling(setVars, intVars).post();
        checkSolutions(model, setVars, intVars);
    }

    @Test(groups = "1s", timeOut=60000)
    public void testNominalMZN() {
        Model model = new Model();

        IntVar[] intVars = model.intVarArray(5, 2, 6);
        SetVar[] setVars = model.setVarArray(5, new int[]{}, new int[]{1, 2, 3, 4, 5});

        model.setsIntsChanneling(setVars, intVars, 1, 1).post();
        checkSolutions(model, setVars, intVars, 1);
    }

    @Test(groups = "1s", timeOut=60000)
    public void testNoChannelingPossible() {
        Model model = new Model();

        IntVar[] intVars = model.intVarArray(5, 0, 4);
        model.getEnvironment().worldPush();
        SetVar[] setVars = model.setVarArray(5, new int[]{5}, new int[]{5});

        model.setsIntsChanneling(setVars, intVars).post();

        assertEquals(model.getSolver().isSatisfied(), ESat.FALSE);
        assertFalse(model.getSolver().solve());

        model.getEnvironment().worldPop();
        setVars = model.setVarArray(5, new int[]{}, new int[]{5});
        model.setsIntsChanneling(setVars, intVars).post();
        assertFalse(model.getSolver().solve());
    }


    private void checkSolutions(Model model, SetVar[] setVars, IntVar[] intVars) {
        checkSolutions(model, setVars, intVars, 0);
    }

    private void checkSolutions(Model model, SetVar[] setVars, IntVar[] intVars, int offset) {
        boolean solutionFound = false;
        while(model.getSolver().solve()) {
            solutionFound = true;
            for (int i = 0; i < setVars.length; i++) {
                for (Integer value : setVars[i].getValue()) {
                    assertTrue(intVars[value - offset].getValue() - offset == i);
                }
            }
            for (int i = 0; i < intVars.length; i++) {
                assertTrue(setVars[intVars[i].getValue() - offset].getValue().contain(i + offset));
            }
        }
        assertTrue(solutionFound);
    }

}
