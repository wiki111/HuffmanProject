import javax.swing.*;
import java.awt.*;

public class TreePanel extends JPanel {

    private Dimension currentDimension = new Dimension(100,100);
    private VitterTree tree;

    private int nodeWidth = 50;
    private int nodeHeight = 50;
    private int windowGap = 15;
    private int verticalLevelGap = 100;
    private int horizontalLevelGap = 100;

    private int componentWidth;

    private Font characterFont = new Font("Serif", Font.PLAIN, 18);
    private Font numberFont = new Font("Serif", Font.PLAIN, 10);
    private Font weightFont = new Font("Serif", Font.PLAIN, 12);

    private Graphics2D canvas;

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        drawTree(g);
    }

    @Override
    public Dimension getPreferredSize() {
        return currentDimension;
    }

    public void setTree(VitterTree tree){
        this.tree = tree;
    }

    public void draw(){
        repaint();
    }

    public void drawTree(Graphics graphics){

        canvas = (Graphics2D) graphics;
        canvas.setColor(Color.blue);

        if(tree == null){
            return;
        }

        setComponentDimensions();

        int nodeX = componentWidth/2;
        int nodeY = windowGap;

        drawNodes(tree.root, nodeX, nodeY);
    }

    private void setComponentDimensions(){

        int treeHeight = tree.getHeight(tree.root);
        int numberOfLeaves = tree.getNumberOfLeaves(tree.root);

        int height = (treeHeight) * nodeHeight + (treeHeight + 1) * verticalLevelGap;
        int width = (2*windowGap) + (numberOfLeaves * nodeWidth * 5) + ((numberOfLeaves - 1) * horizontalLevelGap);
        this.componentWidth = width;

        currentDimension = new Dimension(width, height);

    }

    private void drawNodes(Node node, int x, int y){

        drawNode(node.number, node.character, node.weight, x, y);

        if(node.leftChild != null && node.rightChild != null){

            int gapBetweenNodes = tree.getNumberOfLeaves(node) * (27);

            int leftChildX = x - gapBetweenNodes;
            int leftChildY = y + verticalLevelGap;

            int rightChildX = x + gapBetweenNodes;
            int rightChildY = y + verticalLevelGap;

            drawLineBetweenNodes(x, y, leftChildX, leftChildY);
            drawLineBetweenNodes(x, y, rightChildX, rightChildY);

            drawNodes(node.leftChild, leftChildX, leftChildY);
            drawNodes(node.rightChild, rightChildX, rightChildY);

        }
    }

    private void drawLineBetweenNodes(int x1, int y1, int x2, int y2){
        int startX = x1 + nodeWidth/2;
        int startY = y1 + nodeHeight;
        int endX = x2 + nodeWidth/2;
        int endY = y2;

        canvas.setColor(Color.black);
        canvas.drawLine(startX, startY, endX, endY);
    }

    private void drawNode(int number, char character, int weight, int x, int y){

        canvas.setColor(Color.blue);
        canvas.drawRect(x, y, nodeWidth, nodeHeight);

        canvas.setColor(Color.black);
        canvas.setFont(characterFont);
        canvas.drawString(String.valueOf(character), x + 20, y + 33);

        canvas.setColor(Color.blue);
        canvas.setFont(numberFont);
        canvas.drawString(String.valueOf(number), x + 5, y + 15);

        canvas.setColor(Color.red);
        canvas.setFont(weightFont);
        canvas.drawString(String.valueOf(weight), x + 5, y + 47);
    }
}
