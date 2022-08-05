package sdevo.bdmm;

import java.lang.reflect.Array;

import beast.core.Input;
import beast.core.Input.Validate;
import beast.core.parameter.RealParameter;

public class ProductParameter extends RealParameter {
	
	public Input<RealParameter> scalerInput = new Input<>("scaler", "used to scale input", Validate.REQUIRED);	
	public Input<RealParameter> ratioInput = new Input<>("ratio", "used to scale input", Validate.REQUIRED);

    RealParameter scaler;
    RealParameter ratio;
    
    public ProductParameter() {
    	valuesInput.setRule(Validate.FORBIDDEN);
    }


    @Override
    public void initAndValidate() {
    	scaler = scalerInput.get();
    	ratio = ratioInput.get();
    }
    
    @Override
    public int getDimension() {
        return ratio.getDimension();
    }


    /**
     * RRB: we need this here, because the base implementation (public T getValue()) fails
     * for some reason. Why?
     */
    @Override
    public Double getValue() {
        return scaler.getValue()*Math.exp(ratio.getArrayValue(0));
    }

    @Override
    public double getArrayValue() {
        return scaler.getValue()*Math.exp(ratio.getArrayValue(0));
    }

    @Override
    public double getArrayValue(final int index) {
        return scaler.getValue()*Math.exp(ratio.getArrayValue(index));
    }
    
    public boolean isDirty(final int index) {
    	if (scaler.isDirty(0))
    		return true;
    	for (int i = 0; i < ratio.getDimension(); i++)
    		if (ratio.isDirty(i))
    			return true;
        return false;
    }

    
    @SuppressWarnings("unchecked")
    @Override
    protected void store() {
    }

    @Override
    public void restore() {
    }

	
}
