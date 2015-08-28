
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class SelectServer extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JList servers;

    public SelectServer() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

// call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

// call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
// add your code here
        if(servers.getSelectedIndex() != -1){
            System.out.println("!");
            Server t = (Server)servers.getSelectedValue();
            launch(t);
        }

        dispose();
    }

    private void onCancel() {
// add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        SelectServer dialog = new SelectServer();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    private void createUIComponents() {
        System.out.println("Indexing servers...");
        servers = new JList<Server>(Server.getServers());
        servers.setLayoutOrientation(JList.VERTICAL);
        servers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        servers.setCellRenderer(new FileListCellRenderer());
        servers.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                JList list = (JList) evt.getSource();
                if (evt.getClickCount() >= 2) {
                    Server t = (Server) servers.getSelectedValue();
                    launch(t);
                }
            }
        });

    }

    void launch(Server t){
        if(t.local()){
            //TODO launch server
        }
        new USC_client2(t.getRoot(), t.ip()).start();
    }

    class FileListCellRenderer extends DefaultListCellRenderer {

        private static final long serialVersionUID = -7799441088157759804L;
        private FileSystemView fileSystemView;
        private JLabel label;
        private Color textSelectionColor = Color.BLACK;
        private Color backgroundSelectionColor = Color.LIGHT_GRAY;
        private Color textNonSelectionColor = Color.BLACK;
        private Color backgroundNonSelectionColor = Color.GRAY;

        FileListCellRenderer() {
            label = new JLabel();
            label.setOpaque(true);
            label.setBorder(BorderFactory.createEtchedBorder());
            fileSystemView = FileSystemView.getFileSystemView();
        }

        @Override
        public Component getListCellRendererComponent(
                JList list,
                Object value,
                int index,
                boolean selected,
                boolean expanded) {

            Server s = (Server)value;
            try{
                Icon icon = new ImageIcon(s.getRoot() + "/icon.png");
                label.setIcon(icon);
            }catch (Exception e){
                e.printStackTrace();
            }
            label.setText("<html><font size=+2>" + s.name() + "</font><br/><font color = white>" + s.description() + "</font></html>");
            label.setToolTipText(s.ip());

            if (selected) {
                label.setBackground(backgroundSelectionColor);
                label.setForeground(textSelectionColor);
            } else {
                label.setBackground(backgroundNonSelectionColor);
                label.setForeground(textNonSelectionColor);
            }


            return label;
        }
    }


}
