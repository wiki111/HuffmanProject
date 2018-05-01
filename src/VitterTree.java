import java.util.*;

public class VitterTree {

    //Tree root
    Node root;

    //Current NYT node
    Node nyt;

    //Characters present in tree are stored in a list
    private ArrayList<Character> seen;

    private String output = "";
    private Map<Character, Float> probabilities;
    private int codedTextLength = 0;

    public VitterTree() {
        initializeRoot();
        seen = new ArrayList<>();
        probabilities = new HashMap<>();
    }

    private void initializeRoot(){
        this.root = new Node();
        root.number = 256;
        root.isNYT = true;
        root.character = 0;
        root.weight = 0;
        nyt = root;
    }

    public void insert(char character){

        codedTextLength++;

        if(!seen.contains(character)){

            seen.add(character);

            output += generateOutputForNewCharacter(character);

            Node parent = addNewSymbol(nyt, character);

            updateTree(parent);

        }else{

            Node node = getNodeFor(character);

            output += generateOutputForNode(node);

            swapWithHighest(node);

            node.weight++;

            updateTree(node.parent);

        }

    }

    private String generateOutputForNewCharacter(char character){
        return " "
                + new StringBuilder().append(getOutputFor(nyt, "")).reverse()
                + " "
                + Integer.toBinaryString(character);
    }

    private String getOutputFor(Node node, String codeword){
        if(node.parent != null){
            if(isLeftChild(node)){
                return "0" + getOutputFor(node.parent, codeword);
            }else{
                return "1" + getOutputFor(node.parent, codeword);
            }
        }else {
            return codeword;
        }
    }

    private Node addNewSymbol(Node parent, char character){
        Node left = new Node();
        left.isNYT = true;
        left.weight = 0;
        left.number = parent.number - 2;
        left.parent = parent;
        left.character = 0;

        nyt = left;

        Node right = new Node();
        right.isNYT = false;
        right.weight = 1;
        right.number = parent.number - 1;
        right.parent = parent;
        right.character = character;


        parent.isNYT = false;
        parent.leftChild = left;
        parent.rightChild = right;

        return parent;
    }

    private void updateTree(Node node){
        if(node != null){
            swapWithHighest(node);
            node.weight++;
            updateTree(node.parent);
        }
    }

    private void swapWithHighest(Node node){
        Node highest = getHighest(node.weight, node.number);
        swapNodes(node, highest);
    }

    private Node getHighest(int weight, int currentMax){

        //Initialize highest node
        Node highest = null;

        //Initialize queue - start with root node
        Queue<Node> nodes = new LinkedList<>();
        nodes.add(root);

        //At beggining max number is the number of node about to be incremented
        //passed in as function parameter.
        int maxNumber = currentMax;

        //Search tree in order from left to right beggining with root.
        while(!nodes.isEmpty()){
            Node node = nodes.remove();

            if(node.weight == weight && node.number > maxNumber && node != root){
                highest = node;
                maxNumber = highest.number;
            }

            if(node.leftChild != null){
                nodes.add(node.leftChild);
            }

            if(node.rightChild != null){
                nodes.add(node.rightChild);
            }

        }

        return highest;

    }

    private void swapNodes(Node nodeA, Node nodeB){

        //Save data of one of the nodes to be swapped.
        int number = nodeA.number;
        Node parent = nodeA.parent;

        //Neither of nodes about to be swapped can't be other ones parent.
        if(nodeB != null && nodeA.parent != nodeB && nodeB.parent != nodeA){

            //Remember relation of both nodes to their parents.
            boolean isNodeALeftChild = isLeftChild(nodeA);
            boolean isNodeBLeftChild = isLeftChild(nodeB);

            //Swap parents and reassign numbers

            //Node A gets data from Node B
            nodeA.parent = nodeB.parent;
            nodeA.number = nodeB.number;

            //Set correct relation of Node A to the parent
            if(isNodeBLeftChild){
                nodeA.parent.leftChild = nodeA;
            }else {
                nodeA.parent.rightChild = nodeA;
            }

            //Node B gets data from old Node A
            nodeB.parent = parent;
            nodeB.number = number;

            //Set correct relation of Node B to the parent
            if(isNodeALeftChild){
                nodeB.parent.leftChild = nodeB;
            }else {
                nodeB.parent.rightChild = nodeB;
            }
        }
    }

    private Node getNodeFor(char character){
        Node node = checkTreeForCharacter(root, character);
        return node;
    }

    private Node checkTreeForCharacter(Node node, char character){
        if(node.character == character){
            return node;
        }

        Node foundNode = null;

        if(node.leftChild != null){
            foundNode = checkTreeForCharacter(node.leftChild, character);
        }

        if(node.rightChild != null && foundNode == null){
            foundNode = checkTreeForCharacter(node.rightChild, character);
        }

        if(foundNode == null){
            return null;
        }else {
            return foundNode;
        }
    }

    private boolean isLeftChild(Node node){
        if(node.parent.leftChild == node){
            return true;
        }else {
            return false;
        }
    }

    private String generateOutputForNode(Node node){
        return (" " + new StringBuilder().append(getOutputFor(node, "")).reverse());
    }

    public String getOutput() {
        return output;
    }

    public int getHeight(Node node){
        if(node == null){
            return 0;
        }else {
            int leftSubtreeDepth = getHeight(node.leftChild);
            int rightSubtreeDepth = getHeight(node.rightChild);

            if(leftSubtreeDepth > rightSubtreeDepth){
                return leftSubtreeDepth + 1;
            }else{
                return rightSubtreeDepth + 1;
            }
        }
    }

    public int getNumberOfLeaves(Node node){
        if(!isInternal(node)){
            return 1;
        }else {
            int countLeft = getNumberOfLeaves(node.leftChild);
            int countRight = getNumberOfLeaves(node.rightChild);

            return countLeft + countRight;
        }
    }

    private void calculateProbabilities(){
        for(char c : seen){
            Node node = getNodeFor(c);
            probabilities.put(c, ((float)node.weight/(float)codedTextLength));
        }
    }

    public double calculateEntropy(){

        calculateProbabilities();

        double entropy = 0;

        for(Map.Entry<Character, Float> entry : probabilities.entrySet() ){
            entropy += (double)(entry.getValue() * log2(1 / ((double)entry.getValue())));
        }

        return entropy;
    }

    public double calculateAverageCodewordLength(){

        calculateProbabilities();

        double averageCodewordLength = 0;

        for(Map.Entry<Character, Float> entry : probabilities.entrySet() ){
            String codeword = getOutputFor(getNodeFor(entry.getKey()), "");
            int codewordLength = codeword.length();
            averageCodewordLength += ((double)(entry.getValue() * codewordLength));
        }

        return averageCodewordLength;
    }

    private double log2(double value){
        return Math.log(value)/Math.log(2);
    }

    private boolean isInternal(Node node){
        if(node.leftChild == null && node.rightChild == null){
            return false;
        }else{
            return true;
        }
    }

}
