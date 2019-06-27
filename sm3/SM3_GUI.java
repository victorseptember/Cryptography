package sm3;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SM3_GUI extends JFrame{
    private JTextArea mingArea = new JTextArea(7,35);
    private JTextArea miArea = new JTextArea(7,35);
    private JButton button1 = new JButton("START");
    private JButton button2 = new JButton("FILE");
    private JButton button3 = new JButton("CLEAN");

    private String plaintext;
    private String hashcode;

    public SM3_GUI(){
        setLayout(new BorderLayout());
        mingArea.setFont(new Font("黑体", Font.PLAIN, 30));
        miArea.setFont(new Font("黑体", Font.PLAIN, 30));

        JPanel northPanel = new JPanel();
        northPanel.setLayout(new BorderLayout());
        northPanel.add(new JScrollPane(mingArea));
        northPanel.setBorder(new TitledBorder("字符串"));

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        centerPanel.add(new JScrollPane(miArea));
        centerPanel.setBorder(new TitledBorder("散列值"));

        JPanel southPanel = new JPanel();
        southPanel.setLayout(new FlowLayout());
        southPanel.add(button1);
        southPanel.add(button2);
        southPanel.add(button3);

        add(northPanel,BorderLayout.NORTH);
        add(centerPanel,BorderLayout.CENTER);
        add(southPanel,BorderLayout.SOUTH);

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                plaintext = mingArea.getText();
                hashcode = new SM3_xyc().iteration_xyc(plaintext,0);
                miArea.setText(hashcode);
            }
        });

        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fileSource = new SM3_file().fileChooser();
                hashcode = new SM3_file().file_sm3(fileSource);
                miArea.setText(hashcode);
            }
        });

        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mingArea.setText(null);
                miArea.setText(null);
            }
        });

    }

    public static void main(String[] args){
        SM3_GUI sm3 = new SM3_GUI();
        sm3.setTitle("SM3");
        sm3.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        sm3.pack();
        sm3.setVisible(true);
    }
}
