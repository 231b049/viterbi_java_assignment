import java.util.*;

public class Viterbi {

    public static void main(String[] args) {
        String[] states = {"Rainy", "Sunny"};
        String[] observations = {"walk", "shop", "clean"};

        Map<String, Double> startProb = new HashMap<>();
        startProb.put("Rainy", 0.6);
        startProb.put("Sunny", 0.4);

        Map<String, Map<String, Double>> transProb = new HashMap<>();
        Map<String, Double> rainyTrans = new HashMap<>();
        rainyTrans.put("Rainy", 0.7);
        rainyTrans.put("Sunny", 0.3);

        Map<String, Double> sunnyTrans = new HashMap<>();
        sunnyTrans.put("Rainy", 0.4);
        sunnyTrans.put("Sunny", 0.6);

        transProb.put("Rainy", rainyTrans);
        transProb.put("Sunny", sunnyTrans);

        Map<String, Map<String, Double>> emitProb = new HashMap<>();
        Map<String, Double> rainyEmit = new HashMap<>();
        rainyEmit.put("walk", 0.1);
        rainyEmit.put("shop", 0.4);
        rainyEmit.put("clean", 0.5);

        Map<String, Double> sunnyEmit = new HashMap<>();
        sunnyEmit.put("walk", 0.6);
        sunnyEmit.put("shop", 0.3);
        sunnyEmit.put("clean", 0.1);

        emitProb.put("Rainy", rainyEmit);
        emitProb.put("Sunny", sunnyEmit);

        Result result = viterbi(observations, states, startProb, transProb, emitProb);

        System.out.println("Best Path: " + result.path);
        System.out.println("Probability: " + result.prob);
    }

    static class Result {
        List<String> path;
        double prob;

        Result(List<String> path, double prob) {
            this.path = path;
            this.prob = prob;
        }
    }

    public static Result viterbi(String[] obs, String[] states,
                                Map<String, Double> startProb,
                                Map<String, Map<String, Double>> transProb,
                                Map<String, Map<String, Double>> emitProb) {

        // TODO: Implement Viterbi Algorithm

        Map<String, Double> prev = new HashMap<>();
        Map<String, List<String>> path = new HashMap<>();

        // Initialization
        for (String s : states) {
            double prob = startProb.get(s) * emitProb.get(s).get(obs[0]);
            prev.put(s, prob);
            path.put(s, new ArrayList<>(Arrays.asList(s)));
        }

        // Recursion
        for (int t = 1; t < obs.length; t++) {
            Map<String, Double> curr = new HashMap<>();
            Map<String, List<String>> newPath = new HashMap<>();

            for (String currState : states) {
                double maxProb = Double.NEGATIVE_INFINITY;
                String bestPrev = null;

                for (String prevState : states) {
                    double prob = prev.get(prevState)
                            * transProb.get(prevState).get(currState)
                            * emitProb.get(currState).get(obs[t]);

                    if (prob > maxProb) {
                        maxProb = prob;
                        bestPrev = prevState;
                    }
                }

                curr.put(currState, maxProb);

                List<String> bestPath = new ArrayList<>(path.get(bestPrev));
                bestPath.add(currState);
                newPath.put(currState, bestPath);
            }

            prev = curr;
            path = newPath;
        }

        double maxProb = Double.NEGATIVE_INFINITY;
        String bestState = null;

        for (String s : states) {
            if (prev.get(s) > maxProb) {
                maxProb = prev.get(s);
                bestState = s;
            }
        }

        return new Result(path.get(bestState), maxProb);
    }
}
