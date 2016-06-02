package TextEditor;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.JOptionPane;

public class TextEditor extends JFrame{
	

	//Variables
	private JTextArea area = new JTextArea(40, 120);
	private JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));
	private String currentFile = "Untitled";
	private boolean change = false;
	private boolean italics = false;
	
//Constructor 
	public TextEditor(){
		area.setFont(new Font("Consolas", Font.PLAIN, 12));
		JScrollPane scroll = new JScrollPane(area, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		add(scroll, BorderLayout.CENTER);
//Setting up the menu
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		JMenu file = new JMenu("file");
		JMenu edit = new JMenu("edit");
		menuBar.add(file);
		menuBar.add(edit);
//The file element
		
		//file.add(New);
		file.add(Open);
		file.add(Rename);
		file.add(Save);
		file.add(SaveAs);
		file.add(Quit);
		file.addSeparator();
		
		for(int i = 0; i < 5; i++){
			file.getItem(i).setIcon(null);
		}
		
		edit.add(Cut); 
		edit.add(Copy);
		edit.add(Paste);
		
		edit.getItem(0).setText("Cut");
		edit.getItem(1).setText("Copy");
		edit.getItem(2).setText("Paste");
		
		JToolBar tool = new JToolBar();
		add(tool, BorderLayout.NORTH);
		tool.add(New);
		tool.add(Open);
		tool.add(Save);
		
		
		tool.addSeparator();
		
		JButton ital = tool.add(Italics), cut = tool.add(Cut), cop = tool.add(Copy), pas = tool.add(Paste);
		
		
		cut.setText(null);
		cut.setIcon(new ImageIcon("icons/cut.gif"));
		cop.setText(null);
		cop.setIcon(new ImageIcon("icons/copy.gif"));
		pas.setText(null);
		pas.setIcon(new ImageIcon("icons/paste.gif"));
		ital.setText(null);
		ital.setIcon(new ImageIcon("icons/italics.gif"));
		
		Save.setEnabled(false);
		SaveAs.setEnabled(true);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		area.addKeyListener(k1);
		setTitle(currentFile);
		setVisible(true);
		
	}
	
	private KeyListener k1 = new KeyAdapter() {
		public void keyPressed(KeyEvent e) {
			change = true;
			Save.setEnabled(true);
			SaveAs.setEnabled(true);
		}
	};
	
	Action Save = new AbstractAction("Save", new ImageIcon("icons/save.gif")){
		public void actionPerformed(ActionEvent e){
			commit(currentFile);
		}
	};
	Action New = new AbstractAction("New", new ImageIcon("icons/new.gif")){
			public void actionPerformed(ActionEvent e) {
				saveOld();
				area.setText("");
				currentFile = "Untitled";
				setTitle(currentFile);
				change = false;
				Save.setEnabled(false);
				SaveAs.setEnabled(false);
			}
		};
	
	Action Open = new AbstractAction("Open", new ImageIcon("icons/open.gif")) {
		public void actionPerformed(ActionEvent e) {
			saveOld();
			if(chooser.showOpenDialog(null)==JFileChooser.APPROVE_OPTION) {
				readInFile(chooser.getSelectedFile().getAbsolutePath());
			}
			SaveAs.setEnabled(true);
		}
	};
	
	Action Rename = new AbstractAction("Rename"){
		public void actionPerformed(ActionEvent e){
			rename(currentFile);
		}
	};
	
	Action SaveAs = new AbstractAction("Save as...") {
		public void actionPerformed(ActionEvent e) {
			saveFileAs();
		}
	};
	
	Action Quit = new AbstractAction("Quit") {
		public void actionPerformed(ActionEvent e) {
			saveOld();
			System.exit(0);
		}
	};
	
	Action Italics = new AbstractAction("Italics"){
		public void actionPerformed(ActionEvent e){
			italics();
		}
	};
	
	ActionMap m = area.getActionMap();
	Action Cut = m.get(DefaultEditorKit.cutAction);
	Action Copy = m.get(DefaultEditorKit.copyAction);
	Action Paste = m.get(DefaultEditorKit.pasteAction);
	
	
	private void saveFileAs() {
		if(chooser.showSaveDialog(null)==JFileChooser.APPROVE_OPTION)
			commit(chooser.getSelectedFile().getAbsolutePath());
		System.out.println("You have selected Save As");
	}
	
	private void saveOld(){
		if(change){
			if(JOptionPane.showConfirmDialog(this, "Would you like to save" + currentFile + " ?", "Save", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
				 commit(currentFile);
			}
		}
	}
	
	private void readInFile(String fileName){
		try{
			
			FileReader r = new FileReader(fileName);
			area.read(r,null);
			r.close();
			currentFile = fileName;
			setTitle(currentFile);
			change = false;
		}
		catch(IOException e){
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(this, "Editor can't find filename" + fileName);
		}
	}
	
	private void commit(String fileName){
		try{
			FileWriter w = new FileWriter(fileName);
			area.write(w);
			w.close();
			currentFile = fileName;
			setTitle(currentFile);
			change = false;
			Save.setEnabled(false);
			JOptionPane.showMessageDialog(area,
				    "The file has been saved.");
		}
		catch(IOException e){
			JOptionPane.showMessageDialog(area,
				    "This file could not be saved!", "Warning", JOptionPane.WARNING_MESSAGE);
		}
	}
	
	private void italics(){
		if(italics){
			area.setFont(new Font("Consolas", Font.PLAIN, 12));
			italics = false;
		}
		else{
		area.setFont(new Font("Consolas", Font.ITALIC, 12));
		italics = true;
		}
	}
	
	public void rename(String fileName){
		 fileName = JOptionPane.showInputDialog("Rename");
		JOptionPane.showMessageDialog(null ,"File renamed to: "+fileName);
		currentFile = fileName;
		setTitle(currentFile);
		
	}
}
