package neuralnet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Value {
    private double value;
    private Value parentValue1;
    private Value parentValue2;
    private double gradient;
    private String operator;
    private String label;

    public Value(double value, String label) {
        this.value = value;
        this.label = label;
        this.parentValue1 = null;
        this.parentValue2 = null;
        this.gradient = 0;
        this.operator = "";
        this.label = label;
    }

    static double roundOff(double value) {
        return (double) Math.round(value * 10000d) / 10000d;
    }

    public Value add(Value otherValue, String lable) {
        Value newValue = new Value(this.getValue() + otherValue.getValue(), label);
        newValue.setParentValue1(this);
        newValue.setParentValue2(otherValue);
        newValue.setOperator("+");
        return newValue;
    }

    public Value subtract(Value otherValue, String lable) {
        Value newValue = new Value(this.getValue() - otherValue.getValue(), lable);
        newValue.setParentValue1(this);
        newValue.setParentValue2(otherValue);
        newValue.setOperator("-");
        return newValue;
    }

    public Value multiply(Value otherValue, String lable) {
        Value newValue = new Value(this.getValue() * otherValue.getValue(), lable);
        newValue.setParentValue1(this);
        newValue.setParentValue2(otherValue);
        newValue.setOperator("*");
        return newValue;
    }

    public Value tanh(String label) {
        double tanh = (Math.exp(2 * this.getValue()) - 1) / (Math.exp(2 * this.getValue()) + 1);
        Value newValue = new Value(tanh, "tanh(" + label + ")");
        newValue.setParentValue1(this);
        newValue.setOperator("tanh");
        return newValue;
    }

    public List<Value> getTopologicalOrder() {
        Set<Value> visited = new HashSet<>();
        List<Value> result = new ArrayList<>();
        recursiveTopologicalOrder(result, visited);
        return result;
    }

    private void recursiveTopologicalOrder(List<Value> result, Set<Value> visited) {
        if (visited.contains(this)) {
            return;
        }
        visited.add(this);
        if (this.parentValue1 != null) {
            this.parentValue1.recursiveTopologicalOrder(result, visited);
        }
        if (this.parentValue2 != null) {
            this.parentValue2.recursiveTopologicalOrder(result, visited);
        }
        result.add(this);
    }

    public void computeParentGradient() {
        if (this.parentValue1 == null) {
            return;
        }
        double parent1Gradient = this.parentValue1.gradient;
        double parent2Gradient = this.parentValue2 != null ? this.parentValue2.gradient : 0.0;
        if (this.operator.equals("+")) {
            parent1Gradient += this.gradient;
            parent2Gradient += this.gradient;
        } else if (this.operator.equals("-")) {
            parent1Gradient += this.gradient;
            parent2Gradient -= this.gradient;            
        } else if (this.operator.equals("*")) {
            parent1Gradient += parentValue2.value * this.gradient;
            parent2Gradient += parentValue1.value * this.gradient;
        } else if (this.operator.equals("tanh")) {
            parent1Gradient += this.gradient * (1.0 - Math.pow(this.value, 2.0));
        }
        this.parentValue1.setGradient(parent1Gradient);
        if (this.parentValue2 != null) {
            this.parentValue2.setGradient(parent2Gradient);
        }
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Value getParentValue1() {
        return this.parentValue1;
    }

    public void setParentValue1(Value parentValue1) {
        this.parentValue1 = parentValue1;
    }

    public Value getParentValue2() {
        return this.parentValue2;
    }

    public void setParentValue2(Value parentValue2) {
        this.parentValue2 = parentValue2;
    }

    public double getGradient() {
        return this.gradient;
    }

    public void setGradient(double gradient) {
        this.gradient = gradient;
    }

    public String getOperator() {
        return this.operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(value);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(gradient);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + ((operator == null) ? 0 : operator.hashCode());
        result = prime * result + ((label == null) ? 0 : label.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Value other = (Value) obj;
        if (Double.doubleToLongBits(value) != Double.doubleToLongBits(other.value))
            return false;
        if (parentValue1 == null) {
            if (other.parentValue1 != null)
                return false;
        } else if (!parentValue1.equals(other.parentValue1))
            return false;
        if (parentValue2 == null) {
            if (other.parentValue2 != null)
                return false;
        } else if (!parentValue2.equals(other.parentValue2))
            return false;
        if (Double.doubleToLongBits(gradient) != Double.doubleToLongBits(other.gradient))
            return false;
        if (operator == null) {
            if (other.operator != null)
                return false;
        } else if (!operator.equals(other.operator))
            return false;
        if (label == null) {
            if (other.label != null)
                return false;
        } else if (!label.equals(other.label))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "{" +
            " value='" + getValue() + "'" +
            ", parentValue1='" + getParentValue1() + "'" +
            ", parentValue2='" + getParentValue2() + "'" +
            ", gradient='" + getGradient() + "'" +
            ", operator='" + getOperator() + "'" +
            ", label='" + getLabel() + "'" +
            "}";
    }
}
