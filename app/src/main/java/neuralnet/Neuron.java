package neuralnet;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public final class Neuron {
    private Value[] weights;
    private Value bias;

    public Neuron(int neuronId, int layerId, int inputs) {
        this.weights = new Value[inputs];
        for (int i = 0; i < inputs; i++) {
            weights[i] = new Value(ThreadLocalRandom.current().nextDouble(-1, 1), "w" + Integer.toString(i) + " For Layer: " + layerId + " For Neuron: " + neuronId);
        }
        this.bias = new Value(ThreadLocalRandom.current().nextDouble(-1, 1), "b");
    }

    public Value activate(Value[] x) {
        if (x.length != weights.length) {
            throw new IllegalArgumentException("Input array length is not equal to weights array length");
        }

        Value activatedValue = new Value(0, "output");

        for (int i = 0; i < x.length; i++) {
            activatedValue = activatedValue.add(weights[i].multiply(x[i], "w" + Integer.toString(i) + "*" + "w" + Integer.toString(i)), "z");
        }
        activatedValue = activatedValue.add(bias, "z");
        return activatedValue.tanh("tanh(z)");
    }

    public List<Value> parameters() {
        List<Value> result = new ArrayList<>();

        for (int i = 0; i < weights.length; i++) {
            result.add(weights[i]);
        }
        result.add(bias);
        return result;
    }
}
