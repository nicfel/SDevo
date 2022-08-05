package sdevo.bdmm;

import bdmmprime.mapping.TypeMappedTree;
import bdmmprime.parameterization.TypeSet;
import beast.core.BEASTObject;
import beast.core.Input;
import beast.core.Loggable;
import beast.evolution.tree.Node;
import beast.evolution.tree.Tree;

import java.io.PrintStream;

/**
 * Logger for generating statistics from type mapped trees.
 */
public class OriginLogger extends BEASTObject implements Loggable {

    public Input<TypeMappedTree> typedTreeInput = new Input<>("typedTree",
            "Tree with type changes mapped.",
            Input.Validate.REQUIRED);

    public Input<String> typeLabelInput = new Input<>("typeLabel",
            "Type label used to store type information on tree.",
            Input.Validate.REQUIRED);

    int[][] countMatrix;

    TypeMappedTree tree;
    String typeLabel;

    @Override
    public void initAndValidate() {
        tree = typedTreeInput.get();
        typeLabel = typeLabelInput.get();

    }



    private int getType(Node node) {
        return (int)node.getMetaData(typeLabel);
    }



    @Override
    public void init(PrintStream out) {

        String prefix = tree.getID() != null
                ? tree.getID() + "."
                : "";

       out.print("origin\t");
    }

    @Override
    public void log(long sample, PrintStream out) {
        tree.remapForLog(sample);
        out.print(getType(tree.getRoot()) + "\t");
    }

    @Override
    public void close(PrintStream out) { }
}
