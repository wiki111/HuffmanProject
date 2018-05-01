import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class GuiConf extends JFrame{

    private GroupLayout groupLayout;
    private TreePanel treePanel;
    private JTextField dataField;
    private DefaultListModel<String> output;
    private JLabel entropy;
    private JLabel averageCodeword;

    public GuiConf(){
        initUI();
    }

    private void initUI(){

        Container pane = getContentPane();
        groupLayout = new GroupLayout(pane);
        pane.setLayout(groupLayout);

        setBasics();
        //addActionButtons();
        addDataField();
    }

    private void setBasics(){
        setTitle("Dynamic Huffman Encoding - Vitter Algorithm");
        setSize(300, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void addDataField(){

        JLabel dataLabel = new JLabel("Enter text to encode : ");
        JTextField dataField = new JTextField(1000);
        this.dataField = dataField;

        JButton encodeBtn = new JButton("Encode Data");
        encodeBtn.addActionListener(new ClickAction());

        JLabel outputLabel = new JLabel("Output : ");
        DefaultListModel<String> outputModel = new DefaultListModel<>();
        JList outputList = new JList(outputModel);
        outputList.setMaximumSize(new Dimension(1000, 50));
        output = outputModel;

        JScrollPane outputScrollPane = new JScrollPane(outputList);
        outputScrollPane.setMaximumSize(new Dimension(10000, 50));

        JLabel treeLabel = new JLabel("Tree : ");
        JComponent tree = new TreePanel();
        this.treePanel = (TreePanel) tree;
        tree.setAutoscrolls(true);
        tree.setBackground(Color.WHITE);
        tree.setSize(250, 300);
        JScrollPane scrollPane = new JScrollPane(tree);
        scrollPane.setPreferredSize(new Dimension(100,100));

        JLabel entropyLabel = new JLabel("Entropy : ");
        JLabel entropyField = new JLabel(" xxxx ");
        this.entropy = entropyField;

        JLabel averageLabel = new JLabel("Average codeword length : ");
        JLabel averageField = new JLabel(" xxxx ");
        this.averageCodeword = averageField;

        groupLayout.setHorizontalGroup(
                groupLayout.createParallelGroup()
                    .addGroup( groupLayout.createSequentialGroup()
                                .addComponent(dataLabel)
                                .addComponent(dataField)
                                .addComponent(encodeBtn))
                    .addGroup( groupLayout.createParallelGroup()
                                .addComponent(outputLabel)
                                .addComponent(outputScrollPane))
                    .addGroup( groupLayout.createParallelGroup()
                                .addComponent(treeLabel)
                                .addComponent(scrollPane))
                    .addGroup( groupLayout.createSequentialGroup()
                                .addComponent(entropyLabel)
                                .addComponent(entropyField))
                    .addGroup( groupLayout.createSequentialGroup()
                                .addComponent(averageLabel)
                                .addComponent(averageField))
        );

        groupLayout.setVerticalGroup(
                groupLayout.createSequentialGroup()
                    .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(dataLabel)
                            .addComponent(dataField)
                            .addComponent(encodeBtn))
                    .addGroup( groupLayout.createSequentialGroup()
                            .addComponent(outputLabel)
                            .addComponent(outputScrollPane))
                    .addGroup( groupLayout.createSequentialGroup()
                            .addComponent(treeLabel)
                            .addComponent(scrollPane))
                    .addGroup( groupLayout.createParallelGroup()
                            .addComponent(entropyLabel)
                            .addComponent(entropyField))
                    .addGroup( groupLayout.createParallelGroup()
                            .addComponent(averageLabel)
                            .addComponent(averageField))
        );

        groupLayout.setAutoCreateContainerGaps(true);
        groupLayout.setAutoCreateGaps(true);

        pack();
    }


    private class ClickAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {

            VitterTree tree = generateTreeForText(dataField.getText());
            output.clear();
            output.addElement(tree.getOutput());

            treePanel.setTree(tree);
            treePanel.draw();
            treePanel.revalidate();

            entropy.setText(String.valueOf(tree.calculateEntropy()));
            averageCodeword.setText(String.valueOf(tree.calculateAverageCodewordLength()));

        }

        private VitterTree generateTreeForText(String text){

            VitterTree tree = new VitterTree();

            char[] characters = text.toCharArray();


            for(char c : characters){
                //tree.insertSymbol(c);
                tree.insert(c);
            }

            return tree;

        }
    }

}
