package sdevo.bdmm;

import org.junit.Test;

import bdmmprime.mapping.TypeMappedTree;
import bdmmprime.parameterization.CanonicalParameterization;
import bdmmprime.parameterization.Parameterization;
import bdmmprime.parameterization.SkylineMatrixParameter;
import bdmmprime.parameterization.SkylineVectorParameter;
import bdmmprime.parameterization.TypeSet;
import beast.core.parameter.RealParameter;
import beast.evolution.tree.Node;
import beast.util.TreeParser;
import cern.colt.Arrays;
import junit.framework.Assert;

public class TestMapping {
	
    
    @Test
    public void testDensity() {
    	
        String newick = "((t1[&state=0] : 1.5, t2[&state=1] : 0.5):0.5, t3[&state=1]:2);";
        

		RealParameter originParam = new RealParameter("3.75");

		Parameterization parameterization = new CanonicalParameterization();
		parameterization.initByName(
		        "typeSet", new TypeSet(2),
                "origin", originParam,
                "birthRate", new SkylineVectorParameter(
                        null,
                        new RealParameter("0.1 1"), 2),
                "deathRate", new SkylineVectorParameter(
                        null,
                        new RealParameter("1.0"), 2),
                "birthRateAmongDemes", new SkylineMatrixParameter(
                        null,
                        new RealParameter("0.0"), 2),
                "migrationRate", new SkylineMatrixParameter(
                        null,
                        new RealParameter("0.1 0.1"), 2),
                "samplingRate", new SkylineVectorParameter(
                        null,
                        new RealParameter("0.1 0.5"), 2),
                "removalProb", new SkylineVectorParameter(
                        null,
                        new RealParameter("1.0"), 2));
    	    	
		double diff = getDiffMapping(parameterization, newick, 4);
		
		
        Assert.assertEquals(diff, 0, 0.1);		
		
		
        String newick2 = "((t1[&state=0] : 1.5, t2[&state=1] : 0.5):0.5, t3[&state=1]:2);";
        

		RealParameter originParam2 = new RealParameter("4.5");

		Parameterization parameterization2 = new CanonicalParameterization();
		parameterization2.initByName(
		        "typeSet", new TypeSet(2),
                "origin", originParam2,
                "birthRate", new SkylineVectorParameter(
                        null,
                        new RealParameter("0.1 0.1"), 2),
                "deathRate", new SkylineVectorParameter(
                        null,
                        new RealParameter("1.0"), 2),
                "birthRateAmongDemes", new SkylineMatrixParameter(
                        null,
                        new RealParameter("0.0"), 2),
                "migrationRate", new SkylineMatrixParameter(
                        null,
                        new RealParameter("0.1 0.50"), 2),
                "samplingRate", new SkylineVectorParameter(
                        null,
                        new RealParameter("0.1 0.1"), 2),
                "removalProb", new SkylineVectorParameter(
                        null,
                        new RealParameter("1.0"), 2));
		
		double diff2 = getDiffMapping(parameterization2, newick2, 4);
		
        Assert.assertEquals(diff2, 0, 0.1);
        
        
        
        String newick3 = "((t1[&state=1] : 1.5, t2[&state=1] : 0.5):0.5, t3[&state=1]:2);";
        

		RealParameter originParam3 = new RealParameter("3.5");

		Parameterization parameterization3 = new CanonicalParameterization();
		parameterization3.initByName(
		        "typeSet", new TypeSet(2),
                "origin", originParam3,
                "birthRate", new SkylineVectorParameter(
                        null,
                        new RealParameter("0.1 0.1"), 2),
                "deathRate", new SkylineVectorParameter(
                        null,
                        new RealParameter("1.0"), 2),
                "birthRateAmongDemes", new SkylineMatrixParameter(
                        null,
                        new RealParameter("0.0"), 2),
                "migrationRate", new SkylineMatrixParameter(
                        null,
                        new RealParameter("0.1 0.50"), 2),
                "samplingRate", new SkylineVectorParameter(
                        null,
                        new RealParameter("0.1 0.1"), 2),
                "removalProb", new SkylineVectorParameter(
                        null,
                        new RealParameter("1.0"), 2));
		
		double diff3 = getDiffMapping(parameterization3, newick3, 4);
		
		
        Assert.assertEquals(diff3, 0, 0.1);
        
        
        
        String newick4 = "((t1[&state=1] : 1.5, t2[&state=1] : 0.5):0.5, (t3[&state=1]:2, t4[&state=0]:0.5):1);";
        

		RealParameter originParam4 = new RealParameter("5.5");

		Parameterization parameterization4 = new CanonicalParameterization();
		parameterization4.initByName(
		        "typeSet", new TypeSet(2),
                "origin", originParam4,
                "birthRate", new SkylineVectorParameter(
                        null,
                        new RealParameter("0.1 1"), 2),
                "deathRate", new SkylineVectorParameter(
                        null,
                        new RealParameter("1.0"), 2),
                "birthRateAmongDemes", new SkylineMatrixParameter(
                        null,
                        new RealParameter("0.0"), 2),
                "migrationRate", new SkylineMatrixParameter(
                        null,
                        new RealParameter("0.1 0.50"), 2),
                "samplingRate", new SkylineVectorParameter(
                        null,
                        new RealParameter("0.1 0.1"), 2),
                "removalProb", new SkylineVectorParameter(
                        null,
                        new RealParameter("1.0"), 2));
		
		double diff4 = getDiffMapping(parameterization4, newick4, 6);
		
        Assert.assertEquals(diff4, 0, 0.1);
        
        Assert.assertEquals(getDiffMapping(parameterization3, newick4, 6), 0.,0.1);
        Assert.assertEquals(getDiffMapping(parameterization2, newick4, 6), 0.,0.1);
        Assert.assertEquals(getDiffMapping(parameterization, newick4, 6), 0.,0.1);

    }
    
    
    private double getDiffMapping(Parameterization parameterization, String newick, int nrnodes) {
    	FlatTypeMappedTree avgTypedTree = new FlatTypeMappedTree();
    	
    	avgTypedTree.initByName(
		        "parameterization", parameterization,
                "frequencies", new RealParameter("0.5 0.5"),
                "untypedTree", new TreeParser(newick,
                        false, false,
                        true,0),
                "typeLabel", "state");
    	    	

    	
    	avgTypedTree.doStochasticMapping();
    	
    	int states=2;
    	int reps=100000;
    	double[] totalLength = new double[states];
    	
        TypeMappedTree typeMappedTree = new TypeMappedTree();

    	
    	for (int i = 0; i < reps; i++) {
            typeMappedTree.initByName(
    		        "parameterization", parameterization,
                    "frequencies", new RealParameter("0.5 0.5"),
                    "untypedTree", new TreeParser(newick,
                            false, false,
                            true,0),
                    "typeLabel", "state");
            
            double[] newLength = getStateLength(typeMappedTree.getRoot(),states);
    		
    		
    		double[] newLength2 = getRootLength(typeMappedTree.getRoot(),states);
    		
    		for (int j = 0; j < states; j++) {
    			totalLength[j] += newLength[j];
				totalLength[j] -= newLength2[j];
    		}  
    		
    	}
    	
    	double diff = 0;
		for (int j = 0; j < states; j++) {
			double nodediff = totalLength[j]/reps;
			for (int i= 0; i < nrnodes; i++) {
				nodediff-= avgTypedTree.getNodeTime(i, j);
			}
			
			diff += Math.abs(nodediff);
			
		}
		System.out.println(diff);
		return diff;
    }

    
    
    private double[] getStateLength(Node node, int states) {
    	double[] length = new double[states];
    	if (node.getChildCount()==1) {
    		if (node.getParent()!=null)
    			length[(int) node.getMetaData("state")] += node.getParent().getHeight()-node.getHeight();
    		

    		double[] newLength = getStateLength(node.getChild(0),states);

    		for (int i = 0;i<states;i++)
    			length[i]+=newLength[i];
    		
    		
    				
    	}else if (node.getChildCount()==2) {
    		if (node.getParent()!=null)
    			length[(int) node.getMetaData("state")] += node.getParent().getHeight()-node.getHeight();

    		double[] newLength = getStateLength(node.getChild(0),states);

    		for (int i = 0;i<states;i++)
    			length[i]+=newLength[i];

    		double[] newLength2 = getStateLength(node.getChild(1),states);

    		for (int i = 0;i<states;i++)
    			length[i]+=newLength2[i];
    	}else {
			length[(int) node.getMetaData("state")] += node.getParent().getHeight()-node.getHeight();    		
    	}
    	
    	return length;
    			
    	
    }
    
    private double[] getRootLength(Node node, int states) {
    	double[] length = new double[states];
    	if (node.getChildCount()==1) {
    		if (node.getParent()!=null)
    			length[(int) node.getMetaData("state")] += node.getParent().getHeight()-node.getHeight();
    		

    		double[] newLength = getRootLength(node.getChild(0),states);

    		for (int i = 0;i<states;i++)
    			length[i]+=newLength[i];
    		
    		
    				
    	}else if (node.getChildCount()==2) {
    		if (node.getParent()!=null)
    			length[(int) node.getMetaData("state")] += node.getParent().getHeight()-node.getHeight();

    		return length;
    	}
    	
    	return length;
    			
    	
    }
    
    
}
