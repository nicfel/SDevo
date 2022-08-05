package sdevo.bdmm;

import java.io.PrintStream;
import java.util.Arrays;

import bdmmprime.mapping.TypeMappedTree;
import beast.core.CalculationNode;
import beast.core.Input;
import beast.core.Loggable;
import beast.core.parameter.RealParameter;
import beast.evolution.branchratemodel.BranchRateModel;
import beast.evolution.tree.Node;
import beast.evolution.tree.TraitSet;

public class BdmmStateClock extends BranchRateModel.Base implements Loggable {

	final public Input<RealParameter> relativeClockRatesInput = new Input<>("relativeClockRates",
			"the rate parameters associated with rates in particular states.", Input.Validate.REQUIRED);
	
	final public Input<RealParameter> relativeClockScalerInput = new Input<>("relativeClockScalerRates",
			"an additional scaler in log space for the relative clock rates that allows for additional uncertainty.");

	final public Input<Boolean> normalizeInput = new Input<>("normalize",
			"if true, the rates are normalized, such that the average clock rate is equal to the clock rate.", false);


	final public Input<FlatTypeMappedTree> typedTreeInput = new Input<>("typedTree", "bdmm typed mapped tre.",
			Input.Validate.REQUIRED);
	


	public int states;
	RealParameter relativeClockRates;
	FlatTypeMappedTree typedTree;
	double[] relTime;
	
	boolean isMapped = false;

	@Override
	public void initAndValidate() {
		typedTree = typedTreeInput.get();

		relativeClockRates = relativeClockRatesInput.get();

		// ensure that the rate parameters has the same dimension as there are states in
		// the stateClockInput
		states = typedTree.parameterizationInput.get().getNTypes();
		
		if (relativeClockRates.getDimension() != states)
			relativeClockRates.setDimension(states);
		
		relTime = new double[states];
	}	

	@Override
	public double getRateForBranch(Node node) {		
		
		if (node.isRoot())
			return 0.0;


		
		
		
		if (!isMapped) {
			if (!typedTree.doStochasticMapping()) {
				System.err.println("probabilities at origin were below 0, return negative infinity for this likelihood");
				return 0.0;
			}
			isMapped = true;
		}
		
		
		if (!typedTree.isWGS(node)) {
			return 1e-6;
		}
		

		
		double t = 0;
		double normFactor = 0;
		double length = 0.0;
		
		for (int i = 0; i < states; i++) {
			t += typedTree.getNodeTime(node, i);
			if (normalizeInput.get()) {
				if (relativeClockScalerInput.get()!=null)
					normFactor += relativeClockScalerInput.get().getArrayValue(i) * relativeClockRates.getArrayValue(i)*typedTree.nodeTimeSum[i];
				else
					normFactor += relativeClockRates.getArrayValue(i)*typedTree.nodeTimeSum[i];
				length+=typedTree.nodeTimeSum[i];
			}
		}
		if (normalizeInput.get()) {
			normFactor /= length;
		}else {
			normFactor=1;
		}
		
		// compute the mean rate
		double rate = 0.0;
		for (int i = 0; i < states; i++) {
			if (relativeClockScalerInput.get()!=null)
				rate += relativeClockScalerInput.get().getArrayValue(i) * typedTree.getNodeTime(node.getNr(), i) * relativeClockRates.getArrayValue(i)/normFactor ;
			else
				rate += typedTree.getNodeTime(node.getNr(), i) * relativeClockRates.getArrayValue(i)/normFactor ;
		}
		
		return rate * meanRateInput.get().getValue()/t;
	}


	@Override
	protected boolean requiresRecalculation() {
//		if (((CalculationNode) typedTree).isDirtyCalculation()) {
//			// this is only called if any of its inputs is dirty, hence we need to recompute
//			isMapped = false;
//			return true;
//		}
//				
//		if (meanRateInput.get().isDirty(0)) {
//			isMapped = false;
//			return true;
//		}
//		
//		for (int i = 0; i < relativeClockRates.getDimension(); i++) {
//			if (relativeClockRates.isDirty(i)) {
//				isMapped = false;
//				return true;
//			}
//		}
//		
//		if (super.requiresRecalculation()) {
//			isMapped = false;
//			return true;
//		}
			
		isMapped = false;

		return true;
	}

//	@Override
//	protected void store() {
//		super.store();
//	}
//
//	@Override
//	protected void restore() {
//		super.restore();
//	}

	@Override
	public void init(PrintStream out) {
		for (int i = 0; i < relativeClockRates.getDimension(); i++) {
			out.print(String.format("%s.%d\t", relativeClockRates.getID(), i));
		}
	}

	@Override
	public void log(long sample, PrintStream out) {
		for (int i = 0; i < relativeClockRates.getDimension(); i++) {
			out.print(relativeClockRates.getArrayValue(i) + "\t");
		}
	}

	@Override
	public void close(PrintStream out) {
		// TODO Auto-generated method stub

	}

}
