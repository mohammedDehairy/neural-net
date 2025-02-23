package neuralnet;

import java.util.ArrayList;
import java.util.List;

public final class Layer {
    private Neuron[] neurons;
    private int neuronInputCount;

    public Layer(int layerId, int neuronCount, int neuronInputCount) {
        this.neurons = new Neuron[neuronCount];
        for (int i = 0; i < neuronCount; i++) {
            neurons[i] = new Neuron(i + 1, layerId, neuronInputCount);
        }
        this.neuronInputCount = neuronInputCount;
    }

    public Value[] activate(Value[] x) {
        if (x.length != neuronInputCount) {
            throw new IllegalArgumentException("Input count doesn't match the neuron input count");
        }
        Value[] result = new Value[neurons.length];
        for (int i = 0; i < neurons.length; i++) {
            result[i] = neurons[i].activate(x);
        }
        return result;
    }

    public List<Value> parameters() {
        List<Value> result = new ArrayList<>();

        for (Neuron neuron: neurons) {
            result.addAll(neuron.parameters());
        }
        return result;
    }
}
