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
package solver.recorders.fine;

import solver.ICause;
import solver.Solver;
import solver.constraints.propagators.Propagator;
import solver.exception.ContradictionException;
import solver.exception.SolverException;
import solver.propagation.IPropagationEngine;
import solver.variables.EventType;
import solver.variables.Variable;
import solver.variables.delta.IDeltaMonitor;

/**
 * <br/>
 *
 * @author Charles Prud'homme
 * @since 02/02/12
 */
public class ArcEventRecorder<V extends Variable> extends AbstractFineEventRecorder<V> {

    protected int idxV; // index of this within the variable structure -- mutable

    public ArcEventRecorder(V variable, Propagator<V> propagator, Solver solver,IPropagationEngine engine) {
        super(solver, engine);
        // BEWARE : required by AbstractFineEventRecorder
        this.variables = (V[]) new Variable[]{variable};
        this.propagators = new Propagator[]{propagator};
    }

    @Override
    public boolean execute() throws ContradictionException {
        throw new SolverException("PropEventRecorder#execute() is empty and should not be called (nor scheduled)!");
    }

    @Override
    public void afterUpdate(V var, EventType evt, ICause cause) {
        // Only notify constraints that filter on the specific event received
        assert cause != null : "should be Cause.Null instead";
        if (cause != propagators[PINDEX]) { // due to idempotency of propagator, it should not schedule itself
            // 1. if instantiation, then decrement arity of the propagator
            if (EventType.anInstantiationEvent(evt.mask)) {
                propagators[PINDEX].decArity();
            }
            // 2. schedule the coarse event recorder associated to thos
            propagators[PINDEX].forcePropagate(EventType.FULL_PROPAGATION);
        }
    }

    @Override
    public int getIdx(V variable) {
        return idxV;
    }

    @Override
    public void setIdx(V variable, int idx) {
        this.idxV = idx;
    }

    @Override
    public void flush() {
        // can be void
    }

    @Override
    public IDeltaMonitor getDeltaMonitor(Propagator propagator, V variable) {
        return IDeltaMonitor.Default.NONE;
    }

    @Override
    public void virtuallyExecuted(Propagator propagator) {
    }

    @Override
    public String toString() {
        return "<< " + variables[VINDEX].toString() + "::" + propagators[PINDEX].toString() + " >>";
    }

    @Override
    public void enqueue() {
        enqueued = true;
        propagators[PINDEX].incNbRecorderEnqued();
    }

    @Override
    public void deque() {
        enqueued = false;
        propagators[PINDEX].decNbRecrodersEnqued();
    }

}
