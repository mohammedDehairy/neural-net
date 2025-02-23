package neuralnet;

import java.util.ArrayList;
import java.util.List;

public final class MultiLayerPerceptron {
    private Layer[] layers;
    // private List<Value> cachedParameters;

    public MultiLayerPerceptron(int inputs, int[] layerDistribution) {
        this.layers = new Layer[layerDistribution.length];
        layers[0] = new Layer(0, layerDistribution[0], inputs);

        for(int i = 1; i < layerDistribution.length; i++) {
            this.layers[i] = new Layer(i+1, layerDistribution[i], layerDistribution[i - 1]);
        }
    }

    public Value[] activate(Value[] x) {
        for (Layer layer: layers) {
            x = layer.activate(x);
        }
        return x;
    }

    public List<Value> parameters() {
        List<Value> result = new ArrayList<>();
        for (Layer layer: layers) {
            result.addAll(layer.parameters());
        }
        return result;
    }
}
