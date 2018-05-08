import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
//import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class JNotePad extends JFrame {
	
	private JTextPane _textPane;
	private ActionMap _actionMap = new ActionMap();
	private boolean   _isSaved = true;
	private JFileChooser _fc = new JFileChooser("."); // ������ ���ų� ������ �� �ߴ� ���� ���� â.
	private File _file = null;
	
	private boolean confirmSave() {
		if(_isSaved) 
			return true;
		int ret = JOptionPane.showConfirmDialog(
				this,
				"Content has been modified. Save changes?",
				"JNotePad",
				JOptionPane.YES_NO_CANCEL_OPTION);
		switch(ret) {
		case JOptionPane.YES_OPTION:
			save();
			return true;
		case JOptionPane.NO_OPTION:
			return true;
		default:
			return false;
		}
	}
	private boolean open() {
		if (_fc.showOpenDialog(this)==JFileChooser.APPROVE_OPTION) {
			File file = _fc.getSelectedFile();
			try {
				open(file);
				_file = file;
				setTitle(file.getName() + " - JNotePad");
				return true;
			} catch (IOException e){
				JOptionPane.showMessageDialog(this, "Can't open file " + file, "JNotePad", JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}
		return false;
	}
	
	private void open(File file) throws IOException{
		BufferedReader r = new BufferedReader(new FileReader(file));
		StringBuffer sbuf = new StringBuffer();
		char[] buf = new char[1024];
		int nread;
		
		while((nread = r.read(buf)) != -1) { // file r ���� buf(���ڹ迭) ��ŭ read
			sbuf.append(buf, 0, nread); // buf �� sbuf(��Ʈ��)�� append
		}
		r.close();
		_textPane.setText(sbuf.toString());
	}
	
	private boolean save() {
		if(_file == null)
			return saveAs();
		else
			try {
				save(_file);
				return true;
			} catch (IOException e) {
				showSaveErrorMessage();
			}
		return false;
	}
	
	private void showSaveErrorMessage() {
		String[] mesg = {
				"Could not save file : " + _file,
				"Access denied"
		};
		JOptionPane.showMessageDialog(this, mesg, "JNotePad", JOptionPane.ERROR_MESSAGE);
	}
	
	private void save(File file) throws IOException {
		BufferedWriter w = new BufferedWriter(new FileWriter(file));
		w.write(_textPane.getText());
		w.close();
	}
	
	public boolean saveAs() {
		if (_fc.showSaveDialog(this)==JFileChooser.APPROVE_OPTION) {
			File file = _fc.getSelectedFile();
			try {
				save(file);
				_file = file;
				setTitle(_file.getName() + " - JNotePad");
				return true;
			} catch (IOException e) {
				showSaveErrorMessage();
				return false;
			}
		}
		return false;  // ��Ҹ� ���� ���.
	}

	// ������
	public JNotePad() {
		super("JNotePad");
		_textPane = new JTextPane();
		_textPane.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				_isSaved = false;
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				_isSaved = false;			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				_isSaved = false;			}
		});
		JScrollPane p = new JScrollPane(_textPane); // ���� ��ũ�� �� �߰�
		add(p);
		_actionMap = createActionMap();
		setJMenuBar(createMenuBar());
		add(createToolBar(), BorderLayout.NORTH);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // ������ â�� ���� ��, ���α׷��� ����. ����Ʈ�� �� ������ �ʰ� ��.
	}
	
	private JToolBar createToolBar() {
		// TODO Auto-generated method stub
		// ���� -> ���پ����� ������ ����.
		JToolBar toolbar = new JToolBar();
//		toolbar.add(new JButton("New"));
//		toolbar.add(new JButton("Open"));
//		toolbar.add(new JButton("Save"));
//		toolbar.add(new JButton("Save As"));
		toolbar.add(new JButton(_actionMap.get("new")));
		toolbar.add(new JButton(_actionMap.get("open")));
		toolbar.add(new JButton(_actionMap.get("save")));
		toolbar.add(new JButton(_actionMap.get("saveas")));
		toolbar.addSeparator();
		
//		toolbar.add(new JButton("Cut"));
//		toolbar.add(new JButton("Copy"));
//		toolbar.add(new JButton("Paste"));
		toolbar.add(new JButton(_actionMap.get("cut")));
		toolbar.add(new JButton(_actionMap.get("copy")));
		toolbar.add(new JButton(_actionMap.get("paste")));
		toolbar.addSeparator();
		
//	2	toolbar.add(new JButton("Help"));
		toolbar.add(new JButton(_actionMap.get("help")));
//	1	toolbar.add(new JButton("About"));
//	2	JButton b;
//	2	b = new JButton("About");
//	2	b.addActionListener(new AboutAction());
//	2	toolbar.add(b);
		toolbar.add(new JButton(_actionMap.get("about")));
		
		Component[] comps = toolbar.getComponents();
		for (Component c : comps) {
			if(c instanceof JButton)
				c.setFocusable(false);
		}
		return toolbar;
	}

	private JMenuBar createMenuBar() {
		// TODO Auto-generated method stub
		// �޴��� -> �޴�(��) -> �޴������� �� ���ʷ� ����.
		// �޴��ٸ� return
		JMenuBar menubar = new JMenuBar(); // �޴��ٸ� ����
		
		// File
		JMenu m = new JMenu("File"); // Jmenu�� ������ m <- File �� �����Ͽ� �Ҵ�.
//		m.add(new JMenuItem("New")); // File <- New �߰�
//		m.add(new JMenuItem("Open..."));
//		m.add(new JMenuItem("Save"));
//		m.add(new JMenuItem("Save As..."));
		m.add(new JMenuItem(_actionMap.get("new")));
		m.add(new JMenuItem(_actionMap.get("open")));
		m.add(new JMenuItem(_actionMap.get("save")));
		m.add(new JMenuItem(_actionMap.get("saveas")));
		m.addSeparator();
//		m.add(new JMenuItem("Exit"));
		m.add(new JMenuItem(_actionMap.get("exit")));
		menubar.add(m);  // �޴��� <- m �� �߰�

		// Edit
		m = new JMenu("Edit"); // �޴� m <- Edit �� �����Ͽ� �Ҵ�.
//		m.add(new JMenuItem("Cut")); // Edit <- New �߰�
//		m.add(new JMenuItem("Copy"));
//		m.add(new JMenuItem("Paste"));
		m.add(new JMenuItem(_actionMap.get("cut")));
		m.add(new JMenuItem(_actionMap.get("copy")));
		m.add(new JMenuItem(_actionMap.get("paste")));
		menubar.add(m);  // �޴��� <- m �� �߰�

		// Help
		m = new JMenu("Help"); // �޴� m <- Help �� �����Ͽ� �Ҵ�.
//		m.add(new JMenuItem("Help")); // Help <- Help �߰�
		m.add(new JMenuItem(_actionMap.get("help")));
//	1	m.add(new JMenuItem("About"));
//	3	JMenuItem mi = new JMenuItem(_actionMap.get("about"));
//	2	mi.addActionListener(new AboutAction());
//	3	m.add(mi);
		m.add(new JMenuItem(_actionMap.get("about")));
		menubar.add(m);  // �޴��� <- m �� �߰�

		return menubar; // ������ menubar �� return.
	}

	private class NewAction extends AbstractAction {
		public NewAction() {
			super("New");
	//		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl N"));
	//		putValue(Action.MNEMONIC_KEY, KeyEvent.VK_N);
	//		putValue(Action.SMALL_ICON,	new ImageIcon("./new.jpg"));  // ���ٿ� ������ �ֱ�
		}
		public void actionPerformed(ActionEvent e) {
			System.out.println(getValue(Action.NAME));
			if(!confirmSave())
				return;
			_textPane.setText("");
			_isSaved = true;
		}
	}
	
	private class OpenAction extends AbstractAction {
		public OpenAction() {
			super("Open");
	//		putValue(Action.ACCELERATOR_KEY,KeyStroke.getKeyStroke("ctrl O"));
	//		putValue(Action.MNEMONIC_KEY,KeyEvent.VK_O);
		}
		public void actionPerformed(ActionEvent e) {
			System.out.println(getValue(Action.NAME));
			if(!confirmSave())
				return;
			_isSaved = open();
		}
	}
	
	private class SaveAction extends AbstractAction {
		public SaveAction() {
			super("Save");
	//		putValue(Action.ACCELERATOR_KEY,KeyStroke.getKeyStroke("ctrl S"));
	//		putValue(Action.MNEMONIC_KEY,KeyEvent.VK_S);
			//
		}
		public void actionPerformed(ActionEvent e) {
			System.out.println(getValue(Action.NAME));
			_isSaved = save();
		}
	}
	
	private class SaveAsAction extends AbstractAction {
		public SaveAsAction() {
			super("SaveAs");
		}
		public void actionPerformed(ActionEvent e) {
			System.out.println(getValue(Action.NAME));
			_isSaved = saveAs();
		}
	}
		
	private class ExitAction extends AbstractAction {
		public ExitAction() {
			super("Exit");
		}
		public void actionPerformed(ActionEvent e) {
			System.out.println(getValue(Action.NAME));
			if(_isSaved) 
				System.exit(0);
			else
				_isSaved = save();
				if(_isSaved)
					System.exit(0); // exit �Լ��� ���� ����.. save �ǰų�, save_no�� ������ ��츸. exit �ǵ��� ������ ��.
		}
	}
	private class CutAction extends AbstractAction {
		public CutAction() {
			super("Cut");
			putValue(Action.ACCELERATOR_KEY,
					KeyStroke.getKeyStroke("ctrl X"));
			putValue(Action.MNEMONIC_KEY,KeyEvent.VK_X);
		}
		public void actionPerformed(ActionEvent e) {
			System.out.println(getValue(Action.NAME));
			_textPane.cut();
		}
	}

	private class CopyAction extends AbstractAction {
		public CopyAction() {
			super("Copy");
			putValue(Action.ACCELERATOR_KEY,
					KeyStroke.getKeyStroke("ctrl C"));
			putValue(Action.MNEMONIC_KEY,KeyEvent.VK_C);
		}
		public void actionPerformed(ActionEvent e) {
			System.out.println(getValue(Action.NAME));
			_textPane.copy(); // ���� Ŭ���� ��Ŀ���� ���ٷ�...
			//_textPane.requestFocus(); // ��Ŀ���� �ٽ� ����Ʈ â���� ...
		}
	}

	private class PasteAction extends AbstractAction {
		public PasteAction() {
			super("Paste");
			putValue(Action.ACCELERATOR_KEY,
					KeyStroke.getKeyStroke("ctrl V"));
			putValue(Action.MNEMONIC_KEY,KeyEvent.VK_V);
		}
		public void actionPerformed(ActionEvent e) {
			System.out.println(getValue(Action.NAME));
			_textPane.paste();
		}
	}

	// Action Listener -> Action Map ���� ����.
	private class AboutAction extends AbstractAction {
		public AboutAction() {
			super("About");
		}
		public void actionPerformed(ActionEvent e) {
			String[] mesg = {
					"JNotePad v 0.1",
					"Authur : K.J.H",
			};
			JOptionPane.showMessageDialog(
					JNotePad.this,
					mesg,
					"About JNotePad",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

	
	private class HelpAction extends AbstractAction {
		public HelpAction() {
			super("Help");
		}
		public void actionPerformed(ActionEvent e) {
			String[] mesg = {
					"Sorry.",
					"Help contents are not supported yet.",
			};
			JOptionPane.showMessageDialog(
					JNotePad.this,
					mesg,
					"JNotePad",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	// Action Map
	private ActionMap createActionMap() {
		ActionMap am = new ActionMap();
		am.put("new", new NewAction());
		am.put("open", new OpenAction());
		am.put("save", new SaveAction());
		am.put("saveas", new SaveAsAction());
		am.put("exit", new ExitAction());
		am.put("cut", new CutAction());
		am.put("copy", new CopyAction());
		am.put("paste",new PasteAction());
		am.put("help", new HelpAction());
		am.put("about", new AboutAction());
		return am;
	}
	// �⺻ ������ ũ��, ��ġ, ���̱� ����
	private void start() {
		setSize(600,400);
		setLocation(100,100);
		setVisible(true);
		// Swing�� ����Ʈ ����� ���� Java �����찡 dispaly
		// program or JVM ���� ���� ����.
	}
	
	public static void main(String[] args) {
		
		
		// TODO Auto-generated method stub
		new JNotePad().start();
		
	}

}
