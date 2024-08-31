package texteditor;

import javax.swing.*;
import java.awt.event.*;
import java.io.*;

public class SimpleTextEditor {

    private JFrame frame;
    private JTextArea textArea;
    private JFileChooser fileChooser;
    private File currentFile;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SimpleTextEditor::new);
    }

    public SimpleTextEditor() {
        frame = new JFrame("Simple Text Editor");
        textArea = new JTextArea(20, 60);
        fileChooser = new JFileChooser();
        currentFile = null;

        setupMenu();
        setupFrame();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void setupMenu() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem openItem = new JMenuItem("Open");
        JMenuItem saveItem = new JMenuItem("Save");
        JMenuItem printItem = new JMenuItem("Print");
        JMenuItem exitItem = new JMenuItem("Exit");

        openItem.addActionListener(e -> openFile());
        saveItem.addActionListener(e -> saveFile());
        printItem.addActionListener(e -> printFile());
        exitItem.addActionListener(e -> exitApplication());

        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(printItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        JMenu editMenu = new JMenu("Edit");
        JMenuItem cutItem = new JMenuItem("Cut");
        JMenuItem copyItem = new JMenuItem("Copy");
        JMenuItem pasteItem = new JMenuItem("Paste");

        cutItem.addActionListener(e -> textArea.cut());
        copyItem.addActionListener(e -> textArea.copy());
        pasteItem.addActionListener(e -> textArea.paste());

        editMenu.add(cutItem);
        editMenu.add(copyItem);
        editMenu.add(pasteItem);

        menuBar.add(fileMenu);
        menuBar.add(editMenu);

        frame.setJMenuBar(menuBar);
    }

    private void setupFrame() {
        frame.getContentPane().add(new JScrollPane(textArea));
    }

    private void openFile() {
        int returnValue = fileChooser.showOpenDialog(frame);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            currentFile = fileChooser.getSelectedFile();
            try (BufferedReader reader = new BufferedReader(new FileReader(currentFile))) {
                textArea.read(reader, null);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "File could not be opened.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void saveFile() {
        if (currentFile == null) {
            int returnValue = fileChooser.showSaveDialog(frame);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                currentFile = fileChooser.getSelectedFile();
            } else {
                return;
            }
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(currentFile))) {
            textArea.write(writer);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(frame, "File could not be saved.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void printFile() {
        try {
            textArea.print();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Print failed.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exitApplication() {
        int response = JOptionPane.showConfirmDialog(frame, "Do you want to save changes before exiting?", "Confirm Exit", JOptionPane.YES_NO_CANCEL_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            saveFile();
            System.exit(0);
        } else if (response == JOptionPane.NO_OPTION) {
            System.exit(0);
        }
    }
}