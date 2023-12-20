package edu.ucr.droidCFG.flowdroid.problems;

import edu.ucr.droidCFG.flowdroid.analysis.MayModifyAnalysisObject;
import heros.DefaultSeeds;
import heros.FlowFunction;
import heros.FlowFunctions;
import heros.InterproceduralCFG;
import heros.flowfunc.Identity;
import soot.*;
import soot.jimple.AssignStmt;
import soot.jimple.DefinitionStmt;
import soot.jimple.Stmt;
import soot.jimple.toolkits.ide.DefaultJimpleIFDSTabulationProblem;
import soot.toDex.TemporaryRegisterLocal;

import java.util.*;

public class BackwardsMayModifyProblem extends DefaultJimpleIFDSTabulationProblem<MayModifyAnalysisObject, InterproceduralCFG<Unit, SootMethod>> {

    private InterproceduralCFG<Unit, SootMethod> icfg;

    public BackwardsMayModifyProblem(InterproceduralCFG<Unit, SootMethod> icfg) {
        super(icfg);
        this.icfg = icfg;
    }

    @Override
    public FlowFunctions<Unit, MayModifyAnalysisObject, SootMethod> createFlowFunctionsFactory() {
        return new FlowFunctions<Unit, MayModifyAnalysisObject, SootMethod>() {
            @Override
            public FlowFunction<MayModifyAnalysisObject> getNormalFlowFunction(Unit curr, Unit next) {
                if (curr instanceof AssignStmt) {
                    final AssignStmt assign = (AssignStmt) curr;
                    final Value leftOp = assign.getLeftOp();
                    final Value rightOp = assign.getRightOp();

                    return new FlowFunction<MayModifyAnalysisObject>() {
                        @Override
                        public Set<MayModifyAnalysisObject> computeTargets(MayModifyAnalysisObject source) {
                            HashSet<MayModifyAnalysisObject> hashSet = new HashSet<MayModifyAnalysisObject>();
                            hashSet.add(source);
                            hashSet.add(new MayModifyAnalysisObject(assign.toString()));
                            return hashSet;
                        }
                    };
                } else {
                    return new FlowFunction<MayModifyAnalysisObject>() {
                        @Override
                        public Set<MayModifyAnalysisObject> computeTargets(MayModifyAnalysisObject source) {
                            HashSet<MayModifyAnalysisObject> hashSet = new HashSet<MayModifyAnalysisObject>();
                            hashSet.add(source);
                            return hashSet;
                        }
                    };
                }
            }

            @Override
            public FlowFunction<MayModifyAnalysisObject> getCallFlowFunction(Unit callStmt, final SootMethod destinationMethod) {
            }

            @Override
            public FlowFunction<MayModifyAnalysisObject> getReturnFlowFunction(final Unit callSite, SootMethod
                    calleeMethod, final Unit exitStmt, Unit returnSite) {
            }

            @Override
            public FlowFunction<MayModifyAnalysisObject> getCallToReturnFlowFunction(Unit callSite, Unit
                    returnSite) {
            }
        };
    }

    @Override
    protected MayModifyAnalysisObject createZeroValue() {
        return new MayModifyAnalysisObject();
    }

    @Override
    public Map<Unit, Set<MayModifyAnalysisObject>> initialSeeds() {
        return DefaultSeeds.make(interproceduralCFG().getStartPointsOf(Scene.v().getMainMethod()), zeroValue());
    }


    public String getLable() {
        return "MayModifyProblem";
    }
}