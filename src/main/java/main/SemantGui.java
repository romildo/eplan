package main;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;

import error.CompilerError;
import java_cup.runtime.Symbol;
import javaslang.render.dot.DotFile;
import parse.Lexer;
import parse.Parser;
import absyn.Exp;

@SuppressWarnings("serial")
public class SemantGui extends JFrame implements ActionListener {
   private GridBagLayout layout = new GridBagLayout();
   private GridBagConstraints constraints = new GridBagConstraints();

   private JButton openButton;
   private JTextField fileField;
   private JFileChooser chooser;
   private JTextArea programArea;
   private JTextArea logArea;
   private JButton saveButton;
   private JButton compileButton;
   private JButton asyButton;
   private JButton quitButton;
   private JTree tree;
   private JDialog dialog;
   private JButton dialogClose;

   private Exp exp;

   public SemantGui() {
      super("Panda Compiler");
      setLayout(new BorderLayout());

      JToolBar toolBar = new JToolBar();
      toolBar.setFloatable(false);
      toolBar.setRollover(true);
      add(toolBar, BorderLayout.PAGE_START);

      JPanel panel_ = new JPanel();
      panel_.setLayout(new BoxLayout(panel_, BoxLayout.PAGE_AXIS));
      add(panel_, BorderLayout.CENTER);

      JLabel label = new JLabel("Source File:");
      label.setDisplayedMnemonic('F');
      toolBar.add(label);

      fileField = new JTextField(25);
      label.setLabelFor(fileField);
      fileField.addActionListener(this);
      toolBar.add(fileField);

      openButton = new JButton("Browse");
      openButton.setMnemonic('B');
      openButton.addActionListener(this);
      toolBar.add(openButton);

      toolBar.addSeparator();

      saveButton = new JButton("Save");
      saveButton.setMnemonic('S');
      saveButton.addActionListener(this);
      toolBar.add(saveButton);

      compileButton = new JButton("Compile");
      compileButton.setMnemonic('C');
      compileButton.addActionListener(this);
      toolBar.add(compileButton);

      asyButton = new JButton("Show Tree");
      asyButton.setMnemonic('T');
      asyButton.addActionListener(this);
      toolBar.add(asyButton);

      toolBar.addSeparator();

      quitButton = new JButton("Quit");
      quitButton.setMnemonic('Q');
      quitButton.addActionListener(this);
      toolBar.add(quitButton);

      chooser = new JFileChooser();

      label = new JLabel("Program Source Code");
      label.setDisplayedMnemonic('P');
      panel_.add(label);
      programArea = new JTextArea(12, 80);
      programArea.setFont(new Font("Courier", Font.BOLD, 22));
      JScrollPane scrollPane = new JScrollPane(programArea);
      label.setLabelFor(scrollPane);
      panel_.add(scrollPane);

      label = new JLabel("Compiler Messages");
      label.setDisplayedMnemonicIndex(9);
      panel_.add(label);
      logArea = new JTextArea(5, 80);
      scrollPane = new JScrollPane(logArea);
      label.setLabelFor(scrollPane);
      logArea.setFont(new Font("Courier", Font.BOLD, 22));
      panel_.add(scrollPane);

      JPanel panel = new JPanel(new GridLayout(1, 4));
      panel_.add(panel);

      label = new JLabel("Abstract Syntax Tree");
      label.setDisplayedMnemonicIndex(0);
      panel_.add(label);
      tree = new JTree(new DefaultTreeModel(null));
      tree.setFont(new Font("Courier", Font.BOLD, 22));
      scrollPane = new JScrollPane(tree);
      label.setLabelFor(scrollPane);
      panel_.add(scrollPane);

      pack();
   }

   private void addComponent(Container container, Component component, int row, int column, int width, int height,
                             int fill) {
      constraints.gridx = column;
      constraints.gridy = row;
      constraints.gridwidth = width;
      constraints.gridheight = height;
      constraints.fill = fill;
      constraints.weightx = 1;
      constraints.weighty = 1;
      constraints.insets = new Insets(2, 2, 2, 2);
      layout.setConstraints(component, constraints);
      container.add(component);
   }

   public static void main(String args[]) throws Exception {
      SemantGui application = new SemantGui();
      application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
      application.setVisible(true);
   }

   private void readSource() {
      try {
         FileReader file = new FileReader(fileField.getText());
         StringBuilder str = new StringBuilder();
         while (true) {
            int ch = file.read();
            if (ch == -1)
               break;
            str.append((char) ch);
         }
         file.close();
         programArea.setText(str.toString());
      }
      catch (FileNotFoundException e1) {
         JOptionPane.showMessageDialog(SemantGui.this, "File not found:\n" + fileField.getText(),
                                       "CompError reading file", JOptionPane.ERROR_MESSAGE);
      }
      catch (IOException e2) {
         JOptionPane.showMessageDialog(SemantGui.this, "CompError reading file:\n" + fileField.getText(),
                                       "CompError reading file", JOptionPane.ERROR_MESSAGE);
      }
   }

   public void actionPerformed(ActionEvent e) {
      if (e.getSource() == openButton) {
         int returnVal = chooser.showOpenDialog(SemantGui.this);
         if (returnVal == JFileChooser.APPROVE_OPTION)
            fileField.setText(chooser.getSelectedFile().getPath());
         readSource();
      }
      if (e.getSource() == saveButton) {
         String filePath = fileField.getText().trim();
         if (!filePath.isEmpty())
            chooser.setSelectedFile(new File(filePath));
         int returnVal = chooser.showSaveDialog(SemantGui.this);
         if (returnVal == JFileChooser.APPROVE_OPTION) {
            filePath = chooser.getSelectedFile().getPath();
            try {
               FileWriter file = new FileWriter(filePath);
               file.write(programArea.getText());
               file.close();
               fileField.setText(filePath);
            }
            catch (IOException e1) {
               JOptionPane.showMessageDialog(SemantGui.this,
                                             "CompError writing file:\n" + filePath,
                                             "CompError writing file",
                                             JOptionPane.ERROR_MESSAGE);
            }
         }
      }
      else if (e.getSource() == fileField)
         readSource();
      else if (e.getSource() == compileButton)
         compileProgram();
      else if (e.getSource() == asyButton)
         asyGraph();
      else if (e.getSource() == dialogClose)
         dialog.dispose();
      else if (e.getSource() == quitButton)
         dispose();
   }

   private void compileProgram() {
      StringReader file = new StringReader(programArea.getText());
      Lexer scanner = new Lexer(file, fileField.getText());
      Parser parser = new Parser(scanner, scanner.getSymbolFactory());
      try {
         logArea.setText("");
         Symbol prog = parser.parse();
         exp = (Exp) prog.value;
         DefaultTreeModel treeModel = new DefaultTreeModel(javaslang.render.swing.Tree.create(exp.toTree()));
         tree.setModel(treeModel);
         for (int row = 0; row < tree.getRowCount(); row++)
            tree.expandRow(row);
         types.Type et = exp.semantic(new env.Env());
         logArea.append("===> " + et.toString() + "\n");
      }
      catch (CompilerError e) {
         logArea.append(e.getMessage());
      }
      catch (Exception e) {
         JOptionPane.showMessageDialog(SemantGui.this,
                                       "Error compiling program\n",
                                       "Internal error compiling program\n" +
                                       e.getStackTrace(),
                                       JOptionPane.ERROR_MESSAGE);
      }

   }

   private void asyGraph() {
      if (exp != null)
         try {
            String imageType = "png";
            File tempfile = File.createTempFile("parsetree-", ".asy");
            tempfile.deleteOnExit();
            DotFile.write(exp.toTree(), tempfile);
            File tempfile2 = File.createTempFile("parsetree-", "." + imageType);
            tempfile2.deleteOnExit();
            String cmd[] = {"dot", "-Tpng", "-o", tempfile2.getPath(), tempfile.getPath()};
            Process proc = Runtime.getRuntime().exec(cmd, null, tempfile.getParentFile());
            proc.waitFor();
            dialog = new JDialog(SemantGui.this, "parse Tree");
            dialog.setLayout(layout);
            Icon icon = new ImageIcon(tempfile2.getPath());
            JLabel label = new JLabel(icon);
            JScrollPane scrollPane = new JScrollPane(label);
            addComponent(dialog, scrollPane, 0, 0, 1, 1, GridBagConstraints.BOTH);
            dialogClose = new JButton("Close");
            dialogClose.addActionListener(this);
            addComponent(dialog, dialogClose, 1, 0, 1, 1, GridBagConstraints.NONE);
            dialog.pack();
            dialog.setVisible(true);
         }
         catch (Exception e) {
            JOptionPane.showMessageDialog(SemantGui.this,
                                          "Error creating asy graph\n",
                                          "Cannot create asy graph\n" +
                                          e.getStackTrace(),
                                          JOptionPane.ERROR_MESSAGE);
         }
   }
}
