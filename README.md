# Bayesian-Belief-Network

## Introduction
This repository contains a Java program demonstrating the implementation of a Bayesian Belief Network (BBN) and provides functionalities for performing probabilistic inference within the network.

## Functions for Inference
The program includes the following functions for probabilistic inference within the Bayesian Belief Network:

- calculatePosteriorProbab: Calculates the posterior probability of a target node given evidence.
- calculateNormFactor: Calculates the normalization factor for a target node.
- calculateNormalisedProbab: Calculates the normalized probability of a target node given evidence.
- predictOutcome: Predicts the outcome of an event (denoted as target) based on evidence.

## Usage
To use this program for probabilistic inference within a Bayesian Belief Network, follow these steps:

- Define the BBN Structure:

1. Create nodes and specify their parents.
2. In the "Define all Nodes" section, provide the node's name as constructor input "name". Additionally, set the variable name for the node, and keep it same as the input given to the constructor.
3. In the "Define Network Structure" section, use [currentNode].addParent([parentNode]) to define network relations. Order is important.

- Set Conditional Probability Distributions (CPD):

1. In the "Specify CPD Values" section, use [currentNode].probabilities.addProbability([key],[probability]) to set CPD values.
2. Keep [key] as a string with the value "T" or "F" only.
3. For nodes with multiple parents, use [key] written as "TTFF..." with the order of T and F corresponding to the order in which parents were added.
4. Keep [probability] as a double value corresponding to the key.

- Perform Probabilistic Inference:

1. Provide evidence and a target node to calculate probabilities or predict outcomes.
2. Before calling any inference function, enter evidence for each node using evidence.put([key],[value]).
3. Set the target node for the function using target.put([key],[value]). Similar to evidence.
4. When calling predictOutcome, [value] for the target can be any string. Default is set as "?" (No need to change).


# Example:

```
evidence.put("Rain", "T");
evidence.put("Traffic", "F");
target.put("ArriveOnTime", "?");
double probability = predictOutcome();
```

Feel free to explore and modify the code as needed for your specific Bayesian Belief Network. 
If you have any questions or suggestions, please open an issue or contribute to the repository.
