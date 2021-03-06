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
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.variables.SetVar;
import org.chocosolver.util.ESat;
import org.chocosolver.util.objects.setDataStructures.ISet;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.testng.Assert.*;

/**
 * @author Alexandre LEBRUN
 */
public class AtMost1EmptyTest {


    @Test(groups = "1s", timeOut=60000)
    public void testNominal() {
        Model model = new Model();

        SetVar[] vars = model.setVarArray(5, new int[]{}, new int[]{1, 2, 3});
        model.post(atMost1EmptySet(vars));

        checkSolutions(model, vars);
    }

    @Test(groups = "1s", timeOut=60000)
    public void testNoEmptySet() {
        Model model = new Model();

        SetVar[] vars = model.setVarArray(5, new int[]{1}, new int[]{1, 2, 3, 4});
        model.post(atMost1EmptySet(vars));

        checkSolutions(model, vars);
    }


    @Test(groups = "1s", timeOut=60000)
    public void testOneEmptySet() {
        Model model = new Model();

        SetVar[] vars = new SetVar[5];
        vars[0] = model.setVar(new int[]{});
        for (int i = 0; i <= 4; i++) {
            vars[i] = model.setVar(new int[]{1}, new int[]{1, 2, 3, 4});
        }
        model.post(atMost1EmptySet(vars));

        checkSolutions(model, vars);
    }

    @Test(groups = "1s", timeOut=60000)
    public void twoEmptySets() {
        Model model = new Model();

        SetVar[] vars = new SetVar[5];
        vars[0] = model.setVar(new int[]{});
        vars[1] = model.setVar(new int[]{});
        for (int i = 2; i <= 4; i++) {
            vars[i] = model.setVar(new int[]{1}, new int[]{1, 2, 3, 4});
        }
        model.post(atMost1EmptySet(vars));

        assertEquals(model.getSolver().isSatisfied(), ESat.FALSE);
        assertFalse(model.getSolver().solve());
    }



    private void checkSolutions(Model model, SetVar[] vars) {
        int nbSol = 0;
        while (model.getSolver().solve()) {
            nbSol++;
            boolean atMostOne = Arrays.stream(vars)
                    .map(SetVar::getValue)
                    .filter(ISet::isEmpty)
                    .count() <= 1;
            assertTrue(atMostOne);
        }
        assertTrue(nbSol > 0);
    }

    private Constraint atMost1EmptySet(SetVar... vars) {
        return new Constraint("AtMost1Empty", new PropAtMost1Empty(vars));
    }
}
