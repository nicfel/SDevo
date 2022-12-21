package sdevo.bdmm;

import beast.core.Description;
import beast.evolution.operators.TreeOperator;
import beast.evolution.tree.Node;
import beast.evolution.tree.Tree;
import beast.util.Randomizer;


@Description("Randomly selects true internal tree node (i.e. not the root) and move node height uniformly in interval " +
        "restricted by the nodes parent and children.")
public class UniformSubtree extends TreeOperator {

    // empty constructor to facilitate construction by XML + initAndValidate
    public UniformSubtree() {
    }

    public UniformSubtree(Tree tree) {
        try {
            initByName(treeInput.getName(), tree);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            throw new RuntimeException("Failed to construct Uniform Tree Operator.");
        }
    }

    @Override
    public void initAndValidate() {
    }

    /**
     * change the parameter and return the hastings ratio.
     *
     * @return log of Hastings Ratio, or Double.NEGATIVE_INFINITY if proposal should not be accepted *
     */
    @Override
    public double proposal() {
        final Tree tree = treeInput.get(this);

        // randomly select internal node
        final int nodeCount = tree.getNodeCount();
        
        // Abort if no non-root internal nodes
        if (tree.getInternalNodeCount()==1)
            return Double.NEGATIVE_INFINITY;
        
        Node node;
        do {
            final int nodeNr = nodeCount / 2 + 1 + Randomizer.nextInt(nodeCount / 2);
            node = tree.getNode(nodeNr);
        } while (node.isRoot() || node.isLeaf());
        
        final double upper = node.getParent().getHeight();
        
        
        
        final double lower = getNearestSamplingTime(node);
        final double newValue = (Randomizer.nextDouble() * (upper - lower)) + lower;
        
        final double ratio = newValue/node.getHeight();
        
        scaleAllNodes(node, ratio);

        return 0.0;
    }
    
    
    private double getNearestSamplingTime(Node n) {
    	if (n.isLeaf()) {
    		return n.getHeight();
    	}else {
    		return Math.max(getNearestSamplingTime(n.getChild(0)), getNearestSamplingTime(n.getChild(0)));
    	}
    }
    
    private void scaleAllNodes(Node n, double ratio) {
    	if (n.isLeaf()) {
    		return;
    	}else {
    		n.setHeight(n.getHeight()*ratio);
    		scaleAllNodes(n.getChild(0), ratio);
    		scaleAllNodes(n.getChild(1), ratio);
    	}
    }


}
