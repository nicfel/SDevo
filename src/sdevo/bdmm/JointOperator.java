package sdevo.bdmm;

import java.util.ArrayList;
import java.util.List;

import beast.core.Input;
import beast.core.Operator;
import beast.core.Input.Validate;
import beast.evolution.tree.Tree;

public class JointOperator extends Operator {
	
//	Input<List<Double>> doubleInput = new Input<>("doubleInput", "a list of operator, each called in sequence", new ArrayList<>(), Validate.REQUIRED);
    public final Input<Tree> treeInput = new Input<>("tree", "if specified, all beast.tree divergence times are scaled");

	final public Input<List<Operator>> operatorInput = new Input<>("operator", "a list of operator, each called in sequence", new ArrayList<>(), Validate.REQUIRED);

	@Override
	public void initAndValidate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double proposal() {
		double HR = 0.0;
		for (Operator ops : operatorInput.get()) {
			HR += ops.proposal();
		}				
		return HR;
	}

}
