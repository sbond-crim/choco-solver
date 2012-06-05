/**
 *  Copyright (c) 1999-2011, Ecole des Mines de Nantes
 *  All rights reserved.
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *      * Redistributions of source code must retain the above copyright
 *        notice, this list of conditions and the following disclaimer.
 *      * Redistributions in binary form must reproduce the above copyright
 *        notice, this list of conditions and the following disclaimer in the
 *        documentation and/or other materials provided with the distribution.
 *      * Neither the name of the Ecole des Mines de Nantes nor the
 *        names of its contributors may be used to endorse or promote products
 *        derived from this software without specific prior written permission.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE REGENTS AND CONTRIBUTORS ``AS IS'' AND ANY
 *  EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED. IN NO EVENT SHALL THE REGENTS AND CONTRIBUTORS BE LIABLE FOR ANY
 *  DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package solver.propagation.generator;

import solver.Solver;
import solver.constraints.Constraint;
import solver.constraints.propagators.Propagator;
import solver.propagation.IPropagationEngine;
import solver.propagation.generator.predicate.Predicate;
import solver.recorders.coarse.AbstractCoarseEventRecorder;
import solver.recorders.coarse.CoarseEventRecorder;

import java.util.ArrayList;
import java.util.List;

import static solver.propagation.generator.PrimitiveTools.validate;

/**
 * <br/>
 *
 * @author Charles Prud'homme
 * @since 08/03/12
 */
public class PCoarse implements Generator<AbstractCoarseEventRecorder> {

    final List<AbstractCoarseEventRecorder> eventRecorders;

    public PCoarse(IPropagationEngine propagationEngine, Constraint... constraints) {
        this(propagationEngine, constraints, PArc.NOV);
    }

    public PCoarse(IPropagationEngine propagationEngine, Propagator[] propagators) {
        this(propagationEngine, propagators, PArc.NOV);
    }

    public PCoarse(IPropagationEngine propagationEngine, Constraint[] constraints, Predicate[] validations) {
        super();
        eventRecorders = new ArrayList<AbstractCoarseEventRecorder>();
        if (constraints.length > 0) {
            Solver solver = constraints[0].getSolver();
            propagationEngine.prepareWM(solver);
            for (int i = 0; i < constraints.length; i++) {
                Propagator[] propagators = constraints[i].propagators;
                for (int j = 0; j < propagators.length; j++) {
                    Propagator propagator = propagators[j];
                    if (validations.length == 0 || validate(propagator, validations)) {
                        int pidx = propagator.getId();
                        if (propagationEngine.isMarked(0, pidx, 0)) {
                            propagationEngine.clearWatermark(0, pidx, 0);
                            CoarseEventRecorder er = new CoarseEventRecorder(propagator, solver, propagationEngine);
                            eventRecorders.add(er);
                            propagationEngine.addEventRecorder(er);
                        }
                    }
                }
            }
        }
    }


    public PCoarse(IPropagationEngine propagationEngine, Propagator[] propagators, Predicate[] validations) {
        super();
        eventRecorders = new ArrayList<AbstractCoarseEventRecorder>();
        if (propagators.length > 0) {
            Solver solver = propagators[0].getSolver();
            propagationEngine.prepareWM(solver);
            for (int i = 0; i < propagators.length; i++) {
                Propagator propagator = propagators[i];
                if (validations.length == 0 || validate(propagator, validations)) {
                    int pidx = propagator.getId();
                    if (propagationEngine.isMarked(0, pidx, 0)) {
                        propagationEngine.clearWatermark(0, pidx, 0);
                        CoarseEventRecorder er = new CoarseEventRecorder(propagator, solver, propagationEngine);
                        eventRecorders.add(er);
                        propagationEngine.addEventRecorder(er);
                    }
                }
            }
        }
    }

    @Override
    public AbstractCoarseEventRecorder[] getElements() {
        return eventRecorders.toArray(new AbstractCoarseEventRecorder[eventRecorders.size()]);
    }
}
