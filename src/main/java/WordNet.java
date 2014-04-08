import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WordNet {
    private static final int UNKNOWN_ROOT = Integer.MIN_VALUE;

    private Map<String, List<Integer>> nounToVertex = new HashMap<>();
    private List<String> synsets = new ArrayList<>();
    private int root = UNKNOWN_ROOT;
    private Digraph digraph;

    public WordNet(String synFile, String hypFile) {
        processFile(synFile, new SynFileProcessor());
        processFile(hypFile, new HypFileProcessor());

        validatGraphIsRootedDAG();
    }

    private void processFile(String synFile, FileProcessor fileProcessor) {
        In in = null;
        try {
            in = new In(synFile);

            String strLine;
            int lineNumber = 0;
            while ((strLine = in.readLine()) != null) {
                fileProcessor.processLine(strLine, lineNumber);

                lineNumber++;
            }
            fileProcessor.postLineProcess(lineNumber);
        } finally {
            close(in);
        }
    }

    private void close(In closable) {
        if (closable != null) {
            closable.close();
        }
    }

    private interface FileProcessor {
        void processLine(String strLine, int lineNumber);

        void postLineProcess(int processedLines);
    }

    private class SynFileProcessor implements FileProcessor {

        @Override
        public void processLine(String strLine, int lineNumber) {
            String[] parts = strLine.split(",");
            validateParts(strLine, parts, 2);
            String idStr = parts[0];
            String nounsStr = parts[1];
            validate(idStr, strLine, lineNumber);
            synsets.add(nounsStr);

            addNouns(lineNumber, nounsStr);
        }

        @Override
        public void postLineProcess(int processedLines) {
            digraph = new Digraph(processedLines);
        }
    }

    private class HypFileProcessor implements FileProcessor {
        @Override
        public void processLine(String strLine, int lineNumber) {
            String[] arrayIds = strLine.split(",");
            validateParts(strLine, arrayIds, 1);
            int syn = Integer.parseInt(arrayIds[0]);

            for (int i = 1; i < arrayIds.length; i++) {
                int hyp = Integer.parseInt(arrayIds[i]);
                digraph.addEdge(syn, hyp);
            }
        }

        @Override
        public void postLineProcess(int processedLines) {
        }
    }

    private void validateParts(String strLine, String[] parts, int minSize) {
        if (parts == null || parts.length < minSize) {
            throw new IllegalArgumentException("Line " + strLine
                    + " does not have the expected format");
        }
    }

    private void validate(String idStr, String strLine, int vertexCounter) {
        if (!idStr.trim().equalsIgnoreCase(String.valueOf(vertexCounter))) {
            throw new IllegalArgumentException("Line " + strLine
                    + " does not have the expected format: expected id "
                    + vertexCounter + " but was: " + idStr);
        }
    }

    private void addNouns(int id, String nounsStr) {
        String[] nouns = nounsStr.trim().split(" ");

        for (String noun : nouns) {
            if (!nounToVertex.containsKey(noun)) {
                nounToVertex.put(noun, new ArrayList<Integer>());
            }
            nounToVertex.get(noun).add(id);
        }
    }

    private void validatGraphIsRootedDAG() {
        boolean[] visited = new boolean[digraph.V()];
        Set<Integer> inPath = new HashSet<>();
        for (int i = 0; i < digraph.V(); i++) {
            if (!visited[i]) {
                validationDfs(i, visited, inPath);
            }
        }
    }

    private void validationDfs(int visit, boolean[] visited, Set<Integer> inPath) {
        if (!visited[visit]) {
            checkRoot(visit);
            checkCycle(visit, inPath);

            visited[visit] = true;
            inPath.add(visit);

            Iterable<Integer> neighbors = digraph.adj(visit);
            if (neighbors.iterator().hasNext()) {
                validationDfs(visit, visited, inPath);
            }
            inPath.remove(visit);
        }
    }

    private void checkCycle(int visit, Set<Integer> inPath) {
        if (inPath.contains(visit)) {
            throw new IllegalArgumentException("The has cycle: " + inPath
                    + ", " + visit);
        }
    }

    private void checkRoot(int visit) {
        Iterable<Integer> neighbors = digraph.adj(visit);
        boolean isRoot = !neighbors.iterator().hasNext();
        if (isRoot) {
            if (root == UNKNOWN_ROOT) {
                root = visit;
            } else {
                throw new IllegalArgumentException(
                        "The graph is not rooted: there are two vertex with no connection: "
                                + root + " and " + visit);
            }
        }

    }

    public Iterable<String> nouns() {
        return nounToVertex.keySet();
    }

    public boolean isNoun(String string) {
        return nounToVertex.containsKey(string);
    }

    public String sap(String nounA, String nounB) {
        List<Integer> v = nounToVertex.get(nounA);
        List<Integer> w = nounToVertex.get(nounB);
        SAP sap = new SAP(digraph);

        int ancestor = sap.ancestor(v, w);

        return synsets.get(ancestor);
    }

    public int distance(String nounA, String nounB) {
        List<Integer> v = nounToVertex.get(nounA);
        List<Integer> w = nounToVertex.get(nounB);
        SAP sap = new SAP(digraph);

        return sap.length(v, w);
    }

}
