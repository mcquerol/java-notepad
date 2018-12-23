package com.mateo.notepad;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import javax.swing.ImageIcon;
import javax.swing.JColorChooser;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;
import say.swing.JFontChooser;

import java.awt.BorderLayout;
import javax.swing.JScrollBar;
import javax.swing.JToolBar;


@SuppressWarnings("serial")
public class NotePad extends JFrame {

	private JMenuBar menuBar;
	private JLabel WordCount;
	private JEditorPane Pane;
	private JFileChooser FILECHOOSER;

	static String FN = "Untitled*"; // default unsaved filename

	static int COUNTER = 0; // for save/open

	static int CNTR = 0; // counter for undo/redo

	static String INITIALFILEPATH = "user.home";
	static String FILEPATH = INITIALFILEPATH;

	static String[] WORDS; // this is to contain the words from the JEditorPane

	// Constructor
	public NotePad() {
		this.setIconImage(Toolkit.getDefaultToolkit().getImage("Icons/main.png"));
		this.setTitle("Mateo' s Notepad - " + FN);

		this.setSize(500, 400);
		// menu generate method
		generateMenu();
		this.setJMenuBar(menuBar);

		JMenu mnFormat = new JMenu("Format");
		menuBar.add(mnFormat);
		
		JMenuItem Color = new JMenuItem("Color...");
		Color.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {	
				Color c = JColorChooser.showDialog(null,"Choose a color", getBackground());
				Pane.setForeground(c);
			}
		});
		Color.setIcon(new ImageIcon("Icons/color.png"));
		mnFormat.add(Color);
		
		
		JMenuItem Font = new JMenuItem("Font...");
		Font.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				 JFontChooser fontChooser = new JFontChooser();
				   int result = fontChooser.showDialog(null);
				   if (result == JFontChooser.OK_OPTION) {
				   	java.awt.Font f = fontChooser.getSelectedFont(); 
				   	Pane.setFont(f);
				   }
				
			}
			
		});
		Font.setIcon(new ImageIcon("Icons/font.png"));
		mnFormat.add(Font);
		// pane with null layout
		JPanel contentPane = new JPanel(null);
		contentPane.setPreferredSize(new Dimension(500, 400));
		contentPane.setBackground(new Color(192, 192, 192));

		// adding panel to JFrame and seting of window position and close
		// operation
		getContentPane().add(contentPane);
		contentPane.setLayout(null);
		
		JToolBar toolBar = new JToolBar();
		toolBar.setBounds(0, 337, 265, 63);
		contentPane.add(toolBar);
		
		WordCount = new JLabel("Word Count: 0");
		toolBar.add(WordCount);

		Pane = new JEditorPane();
		Pane.setBounds(0, 0, 500, 400);
		Pane.setContentType("text/plain/html");
		Pane.setBackground(new Color(255, 255, 255));
		contentPane.add(Pane);
		Pane.addKeyListener(new KeyAdapter() {

			public void keyPressed(KeyEvent arg0) {

				WORDS = Pane.getText().split("\\s+");
				if (WORDS.length == 1 && Pane.getText().isEmpty()) { // because an array cannot contain less than 1
																		// element, the label will always show Word
																		// Count: 1. this condition solves the issue
					WordCount.setText("Word Count: 0");
				} else {
					WordCount.setText("Word Count: " + WORDS.length);
				}

			}

		});
		Pane.setEnabled(true);

		
		
		Pane.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.pack();
		this.setVisible(true);
		
	}

	// method for generate menu
	public void generateMenu() {
		menuBar = new JMenuBar();

		JMenu mnFile = new JMenu("File");
		JMenu mnEdit = new JMenu("Edit");
		
		

		JMenuItem open = new JMenuItem("Open...");
		open.setIcon(new ImageIcon("Icons/open.png"));
		open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, Event.CTRL_MASK));

		JMenuItem save = new JMenuItem("Save   ");
		save.setIcon(new ImageIcon("Icons/save.png"));
		save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK + Event.SHIFT_MASK));

		JMenuItem saveAs = new JMenuItem("Save As...");
		saveAs.setIcon(new ImageIcon("Icons/save.png"));
		saveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK));

		JMenuItem quit = new JMenuItem("Quit   ");
		quit.setIcon(new ImageIcon("Icons/quit.png"));
		quit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, Event.CTRL_MASK));

		JMenuItem undo = new JMenuItem("Undo   ");
		undo.setIcon(new ImageIcon("Icons/undo.png"));
		undo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, Event.CTRL_MASK));

		JMenuItem redo = new JMenuItem("Redo   ");
		redo.setIcon(new ImageIcon("Icons/redo.png"));
		redo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, Event.CTRL_MASK));
		
		

		// Setings action for menu item
		// Call defined method
		open.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				FILEPATH = INITIALFILEPATH;
				SelectFile('o');
				if (FILEPATH != INITIALFILEPATH) {
					Read();
				}
			}
		});

		// Setings action for menu item
		// Call defined method
		saveAs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				FILEPATH = INITIALFILEPATH;
				SelectFile('s');
				if (FILEPATH != INITIALFILEPATH) {
					Write();
				}
			}
		});

		// Setings action for menu item
		// Call defined method
		quit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				System.exit(0);
			}
		});

		////////////////
		undo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				Undo();
			}

		});

		redo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				Redo();
			}
		});

		mnFile.add(open);
		mnFile.add(save);
		mnFile.add(saveAs);
		mnFile.addSeparator();
		mnFile.add(quit);

		mnEdit.add(undo);
		mnEdit.add(redo);

		menuBar.add(mnFile);
		menuBar.add(mnEdit);
	}

	private void SelectFile(char MODE) {
		FILECHOOSER = new JFileChooser();
		FILECHOOSER.setCurrentDirectory(new File(System.getProperty("user.home")));

		
		String EXTENSION;

		
		EXTENSION = ".txt";

		FILECHOOSER.addChoosableFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
		FILECHOOSER.addChoosableFileFilter(new FileNameExtensionFilter("Java Files", "java"));
		FILECHOOSER.addChoosableFileFilter(new FileNameExtensionFilter("Word Documents", "docx"));
		FILECHOOSER.addChoosableFileFilter(new FileNameExtensionFilter("Excel Workbook", "xlsx"));
		FILECHOOSER.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));

		int RESULT;
		if (MODE == 's') {
			RESULT = FILECHOOSER.showSaveDialog(null);
		} else {
			RESULT = FILECHOOSER.showOpenDialog(null);
		}

		if (RESULT == JFileChooser.APPROVE_OPTION) {
			File SELECTEDFILE = FILECHOOSER.getSelectedFile();
			FILEPATH = SELECTEDFILE.getAbsolutePath();
			if (FILEPATH.substring(FILEPATH.length() - 4).compareTo(EXTENSION) != 0) {
				FILEPATH = FILEPATH + INITIALFILEPATH;
			}
		}
	}

	private void Read() {
		File F = FILECHOOSER.getSelectedFile();
		String FILENAME = F.getAbsolutePath();
		FN = FILECHOOSER.getSelectedFile().getName();
		this.setTitle("Mateo' s Notepad - " + FN);

		try {
			FileReader READER = new FileReader(FILENAME);
			BufferedReader BR = new BufferedReader(READER);
			Pane.read(BR, null);
			BR.close();
			Pane.requestFocus();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e);
		}
	}

	private void Write() {
		File F = FILECHOOSER.getSelectedFile();
		String FILENAME = F.getAbsolutePath();
		FN = FILECHOOSER.getSelectedFile().getName();
		this.setTitle("Mateo' s Notepad - " + FN);

		try {
			FileWriter FW = new FileWriter(FILENAME);
			Pane.write(FW);

		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		JOptionPane.showMessageDialog(this, "File Saved");
	}

	private void Undo() {
		/*
		 * What i need: WORDS array TEMP variable to store the word that is being undone
		 * STACK - implemented in the WORDS array, if not, another array reduce the size
		 * of the array by copying it into a smaller array of the same data type.
		 * 
		 */

		for (int i = 0; i < WORDS.length / 2; i++) {
			String TEMP = WORDS[i];
			WORDS[i] = WORDS[WORDS.length - 1 - i];
			WORDS[WORDS.length - 1 - i] = TEMP;
		}
		/*
		 * because a STACK is FILO, it makes it easier if the array is reversed
		 */

		// for (String W : WORDS) {
		// System.out.println(W); //////for debugging
		// }

		if (CNTR != WORDS.length) {
			String TMP = WORDS[CNTR];
			System.out.println(TMP); // for debugging
			Pane.setText(Pane.getText().replace(WORDS[CNTR], ""));
		}

		// TODO
		/*
		 * SO CLOSEEE edit: DONE, it works! except that the spaces do not get undone, so
		 * when words are undone the spaces in between remain.
		 */

	}

	private void Redo() {
		for (int i = 0; i < WORDS.length / 2; i++) {
			String TEMP = WORDS[i];
			WORDS[i] = WORDS[WORDS.length - i - 1];
			WORDS[WORDS.length - 1 - i] = TEMP;
		}

		if (CNTR != WORDS.length) {
			String TMP = WORDS[CNTR];
			System.out.println(TMP); // for debugging
			Pane.setText(Pane.getText() + WORDS[CNTR]);
		}

		/// not as easy as it looks, not exactly similar to the undo method either.

	}

	public static void main(String[] args) {
		System.setProperty("swing.defaultlaf", "com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new NotePad(); 
			}
		});
	}
}