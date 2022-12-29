package sdevo.util;

import beast.app.beauti.BeautiDoc;
import beast.core.*;
import beast.core.parameter.RealParameter;
import beast.core.util.CompoundDistribution;
import beast.evolution.likelihood.GenericTreeLikelihood;
import beast.evolution.operators.UpDownOperator;
import beast.evolution.tree.RandomTree;
import beast.evolution.tree.TraitSet;
import beast.evolution.tree.Tree;
import sdevo.bdmm.BdmmStateClock;
import sdevo.bdmm.FlatTypeMappedTree;

import java.util.*;

import bdmmprime.distribution.BirthDeathMigrationDistribution;
import bdmmprime.parameterization.CanonicalParameterization;
import bdmmprime.parameterization.EpiParameterization;
import bdmmprime.parameterization.FBDParameterization;

/**
 * Class containing a static method used in the BEAUti template to tidy
 * up some loose ends.
 */
public class BEAUtiConnector {

    public static boolean customConnector(BeautiDoc doc) {


        for (BEASTInterface p : doc.getPartitions("tree")) {
            String pId = BeautiDoc.parsePartition(p.getID());
            MCMC mcmc = (MCMC) doc.mcmc.get();


            BirthDeathMigrationDistribution dummy = (BirthDeathMigrationDistribution) doc.pluginmap.get("BDMMPrime.t:" + pId);

            
    		System.out.println("dsadsadasdasdsa");
    		System.out.println("dsadsadasdasdsa");
    		System.out.println("dsadsadasdasdsa");
    		System.out.println("dsadsadasdasdsa");

            
            if (dummy == null || !dummy.getOutputs().contains(doc.pluginmap.get("prior")))
                continue;

            
            
            FlatTypeMappedTree ft = new FlatTypeMappedTree();
            
            ft.initByName("parameterization", dummy.parameterizationInput.get(),
            		"frequencies", dummy.frequenciesInput.get(),
            		"untypedTree", dummy.treeInput.get());
            

            

            for (Distribution distr : ((CompoundDistribution) mcmc.posteriorInput.get()).pDistributions.get()) {
	        	if (distr.getID().contentEquals("likelihood")) {
	        		int i = 0;
	            	for (Distribution likelihood : ((CompoundDistribution) distr).pDistributions.get()) {
	            		i++;
	            		if (!likelihood.getID().contentEquals("treeLikelihood." + pId))
	            			continue;
	            		
	            		
					    if (((GenericTreeLikelihood) likelihood).branchRateModelInput.get() instanceof BdmmStateClock && 
					    		((BdmmStateClock) ((GenericTreeLikelihood) likelihood).branchRateModelInput.get()).typedTreeInput.get()!=null) {
			             	continue;
					    }
					    
			            BdmmStateClock bdmmclock = (BdmmStateClock) ((GenericTreeLikelihood) likelihood).branchRateModelInput.get();
			            
			            if (dummy.parameterizationInput.get() instanceof CanonicalParameterization)      
			            	try{bdmmclock.initByName("clock.rate",((GenericTreeLikelihood) likelihood).branchRateModelInput.get().meanRateInput.get(),
			            			"typedTree", ft,
			            			"relativeClockRates", (RealParameter) ((CanonicalParameterization) dummy.parameterizationInput.get()).birthRateInput.get().skylineValuesInput.get());
			            	}catch(Exception e) {System.out.println(e);}
			            
			            if (dummy.parameterizationInput.get() instanceof EpiParameterization)      
			            	bdmmclock.initByName("clock.rate",((GenericTreeLikelihood) likelihood).branchRateModelInput.get().meanRateInput.get(),
			            			"typedTree", ft,
			            			"relativeClockRates", (RealParameter) ((EpiParameterization) dummy.parameterizationInput.get()).R0Input.get().skylineValuesInput.get());

			            if (dummy.parameterizationInput.get() instanceof FBDParameterization)
			            	throw new IllegalArgumentException("not implemented for FBD");

			            
			            System.out.println(i);
//			            
//			            ((GenericTreeLikelihood) likelihood).branchRateModelInput.set(bdmmclock);
			            
//			            ((CompoundDistribution) distr).pDistributions.get().add(nlk);
//			            ((CompoundDistribution) distr).pDistributions.get().remove(i-1);
	
			   			 
			   		 }
	            }
            }
            
            
        }
        
        return false;
    }
}
