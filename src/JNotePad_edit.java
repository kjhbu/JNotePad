import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JToolBar;

public class JNotePad extends JFrame {
	
	private JTextPane _textPane;
	private ActionMap _actionMap;
	
	// ������
	public JNotePad() {
		super("JNotePad");
		_textPane = new JTextPane();
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
		toolbar.add(new JButton("New"));
		toolbar.add(new JButton("Open"));
		toolbar.add(new JButton("Save"));
		toolbar.add(new JButton("Save As"));
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
		m.add(new JMenuItem("New")); // File <- New �߰�
		m.add(new JMenuItem("Open..."));
		m.add(new JMenuItem("Save"));
		m.add(new JMenuItem("Save As..."));
		m.addSeparator();
		m.add(new JMenuItem("Exit"));
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

	private class CutAction extends AbstractAction {
		public CutAction() {
			super("Cut");
		}
		public void actionPerformed(ActionEvent e) {
			System.out.println(getValue(Action.NAME));
			_textPane.cut();
		}
	}

	private class CopyAction extends AbstractAction {
		public CopyAction() {
			super("Copy");
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
