// INTRO:
// This program demonstrates the implementation of a Bayesian Belief Network (BBN) and provides 
// functionalities for performing probabilistic inference within the network.

// FUNCTIONS FOR INFERENCE:
//      calculatePosteriorProbab: Calculates the posterior probability of a target node given evidence.
//      calculateNormFactor: Calculates the normalization factor for a target node.
//      calculateNormalisedProbab: Calculates the normalized probability of a target node given evidence.
//      predictOutcome: Predicts the outcome of an event (denoted as target) based on evidence.

// USAGE:
//      Define the BBN structure by creating nodes and specifying their parents.
//          - Under Define all Nodes section: Keep each Node's variable name and constructor input "name" same.
//          - Under Define Network Structure: Use [currentNode].addParent([parentNode]) to define network relations. {Order is important}
//      Set conditional probability distributions for each node.
//          - Under Specify CPD Values: Use [currentNode].probabilities.addProbability([key],[probability])
//          - Keep [key] as a string with value "T" or "F" only.
//          - For nodes with multiple parents, key written as "TTFF..." with the order of T and F 
//            corresponding to order in which parents were added.
//          - Keep [probability] as a double corresponding to the key.
//      Perform probabilistic inference by providing evidence and a target node to calculate probabilities or predict outcomes.
//          - Before calling any inference function, enter EVIDENCE for each node using evidence.put([key],[value]).
//            Here, [key] should be the exact name of the node.
//                  [value] should be "T" or "F".
//          - Set TARGET node for the function using target.put([key],[value]). {Similar to EVIDENCE}.
//          - When calling predictOutcome, [value] for TARGET can be any String. {Default shown in example is "?"}.



package Java.BayesianBeliefNetworks;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

// Example network shown:
//     Burglary Network + CPDs
//         Burglary -> True = 0.001       
//         Earthquake -> True = 0.002
//         Alarm (B,e) -> B=T,E=T = 0.95
//                        B=T,E=F = 0.94
//                        B=F,E=T = 0.29
//                        B=F,E=F = 0.001
//         P1Calls (A) -> A=T = 0.9
//                        A=F = 0.05
//         P2Calls (A) -> A=T = 0.7
//                        A=F = 0.01


public class BBN {
    public static void main(String[] args) {

        // Define all nodes
        Node burglary = new Node("burglary");
        Node earthquake = new Node("earthquake");
        Node alarm = new Node("alarm");
        Node p1Calls = new Node("p1Calls");
        Node p2Calls = new Node("p2Calls");

        // Define network structure
        alarm.addParent(burglary);
        alarm.addParent(earthquake);
        p1Calls.addParent(alarm);
        p2Calls.addParent(alarm);

        // Specify CPD values for each node
        burglary.probabilities.addProbability("T", 0.001);
        burglary.probabilities.addProbability("F", 0.999);

        earthquake.probabilities.addProbability("T", 0.002);
        earthquake.probabilities.addProbability("F", 0.998);

        alarm.probabilities.addProbability("TTT", 0.95);
        alarm.probabilities.addProbability("TTF", 0.94);
        alarm.probabilities.addProbability("TFT", 0.29);
        alarm.probabilities.addProbability("TFF", 0.001);
        alarm.probabilities.addProbability("FTT", 0.05);
        alarm.probabilities.addProbability("FTF", 0.06);
        alarm.probabilities.addProbability("FFT", 0.71);
        alarm.probabilities.addProbability("FFF", 0.999);

        p1Calls.probabilities.addProbability("TT", 0.9);
        p1Calls.probabilities.addProbability("TF", 0.05);
        p1Calls.probabilities.addProbability("FT", 0.1);
        p1Calls.probabilities.addProbability("FF", 0.95);

        p2Calls.probabilities.addProbability("TT", 0.7);
        p2Calls.probabilities.addProbability("TF", 0.01);
        p2Calls.probabilities.addProbability("FT", 0.3);
        p2Calls.probabilities.addProbability("FF", 0.99);




        // Inference on data 
        List<Node> allNodes = List.of(burglary, earthquake, alarm, p1Calls, p2Calls);
        Map<String, String> evidence = new HashMap<>();
        Map<String, String> target = new HashMap<>(); 

        // Calculating probability of a specific node, given values of all existing nodes
        evidence.clear();
        evidence.put("burglary","T");
        evidence.put("alarm","T");
        evidence.put("p1Calls","T");
        evidence.put("p2Calls","F");
        
        target.clear();
        target.put("earthquake", "T");
        
        double result = Functions.calculateNormalisedProbab(allNodes, target, evidence);
        System.out.println("Probability = " + result +".\n");

        
        // Predicting outcome of an event, given values of the other nodes
        evidence.clear();
        evidence.put("earthquake","T");
        evidence.put("burglary","F");
        evidence.put("p1Calls","T");
        evidence.put("p2Calls","F");

        target.clear();
        target.put("alarm", "?");

        Functions.predictOutcome(allNodes, target, evidence);

    }

}



class Node {
    String name;
    List<Node> parents;
    CPD probabilities;

    public Node(String name) {
        this.name = name;
        this.parents = new ArrayList<>();
        this.probabilities = new CPD();
    }

    public void addParent(Node parent) {
        parents.add(parent);
    }
}


class CPD {
    Map<String, Double> probabilities;

    public CPD() {
        this.probabilities = new HashMap<>();
    }

    public void addProbability(String key, double probability) {
        probabilities.put(key, probability);
    }

    public double getProbability(String key) {
        return probabilities.get(key);
    }
}

class Functions {

    public static double calculatePosteriorProbab(List<Node> nodes, Map<String, String> target, Map<String, String> evidence) {
        double probab = 1.0;
        String targetName = "null";

        for (String key : target.keySet()) {
            targetName = key;
        }

        for (Node node : nodes) {
            if (node.name.equals(targetName)) {
                String key = target.get(node.name);
                if (!node.parents.isEmpty()) {
                    for (Node parent : node.parents) {
                        key = key+evidence.get(parent.name);
                    }
                }
                probab *= node.probabilities.getProbability(key);
            }
            else {
                String key = evidence.get(node.name);
                if (!node.parents.isEmpty()) {
                    for (Node parent : node.parents) {
                        if (parent.name.equals(targetName)) {
                            key = key+target.get(parent.name);
                        }
                        else {
                            key = key+evidence.get(parent.name);
                        }
                    }
                }
                probab *= node.probabilities.getProbability(key);
            }
        }

        return probab;
    }

    public static double calculateNormFactor(List<Node> nodes, Map<String, String> target, Map<String, String> evidence) {
        double result = 0.0;

        String key = "null";
        for (String item : target.keySet()) {
            key = item;
        }
        
        target.put(key, "T");
        double trueProbability = calculatePosteriorProbab(nodes, target, evidence);
        target.put(key, "F");
        double falseProbability = calculatePosteriorProbab(nodes, target, evidence);

        result = trueProbability+falseProbability;

        return result;
    }

    public static double calculateNormalisedProbab(List<Node> nodes, Map<String, String> target, Map<String, String> evidence) {
        double numerator = calculatePosteriorProbab(nodes, target, evidence);
        double denominator = calculateNormFactor(nodes, target, evidence);
        return numerator/denominator; 
    }
    
    public static void predictOutcome(List<Node> nodes, Map<String, String> target, Map<String, String> evidence) {
    
        String key = "null";
        for (String item : target.keySet()) {
            key = item;
        }
        
        target.put(key, "T");
        double trueProbability = calculatePosteriorProbab(nodes, target, evidence);
        target.put(key, "F");
        double falseProbability = calculatePosteriorProbab(nodes, target, evidence);
        String outcome = trueProbability > falseProbability ? "true" : "false";
        System.out.println("The outcome is likely to be: "+outcome);

        return;
    }
    
}
