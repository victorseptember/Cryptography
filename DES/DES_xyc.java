package DES;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;

import javax.swing.*;
import javax.swing.border.TitledBorder;


public class DES_xyc extends JFrame {
	private JTextArea mingArea = new JTextArea(7,35);
	private JTextArea miArea = new JTextArea(7,35);
	private JTextArea stringArea = new JTextArea(5,35);
	private JCheckBox[] checkBox = {new JCheckBox("加密"),new JCheckBox("解密")};
	private JButton clean = new JButton("清空");
	private JButton button = new JButton("加/解密");
	private JButton File_Button = new JButton("文件加/解密");
	//private JButton otherFile_Button = new JButton("其他文件加/解密");
	private JTextField textField = new JTextField(50); 
	//private JLabel label_1 = new JLabel("明文");
	private JLabel label_2 = new JLabel("密文");
	private JLabel label_3 = new JLabel("密钥");
	
	private static int flag = -1; 
	
	public String plaintext;
	public String ciphertext;
	public String key;
	 
	public DES_xyc() { 
		setLayout(new BorderLayout());
		mingArea.setFont(new Font("黑体", Font.PLAIN, 30));
		miArea.setFont(new Font("黑体", Font.PLAIN, 30));
		stringArea.setFont(new Font("黑体", Font.PLAIN, 30));
		textField.setFont(new Font("宋体", Font.PLAIN, 25));
		
		textField.setToolTipText("请输入至少八个字符");
		
		JPanel northPanel = new JPanel();
		northPanel.setLayout(new BorderLayout());
		northPanel.add(label_2, BorderLayout.WEST);
		northPanel.add(new JScrollPane(miArea),BorderLayout.SOUTH);
		northPanel.add(new JScrollPane(stringArea),BorderLayout.NORTH);
		northPanel.setBorder(new TitledBorder("字符串"));
		
		checkBox[0].addItemListener(new ItemListener() {     
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				JCheckBox check = (JCheckBox)e.getSource();
				if(check.isSelected()) {
					DES_xyc.flag = 0;   //若为加密则设flag为0
				}
			}
		});  //注册监听
		
		checkBox[1].addItemListener(new ItemListener() {     
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				JCheckBox check = (JCheckBox)e.getSource();
				if(check.isSelected()) {
					DES_xyc.flag = 1;   //若为解密则设flag为1
				}
			}
		});
		
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BorderLayout());
		//centerPanel.add(label_1,BorderLayout.WEST);
		centerPanel.add(new JScrollPane(mingArea),BorderLayout.NORTH);
		centerPanel.add(label_3,BorderLayout.WEST);
		centerPanel.add(textField,BorderLayout.SOUTH);
		centerPanel.setBorder(new TitledBorder("明文"));
		//centerPanel.setBorder(new TitledBorder("字符串"));
		
		JPanel southPanel = new JPanel();
		southPanel.setLayout(new FlowLayout());
		//southPanel.add(label_3);
		//southPanel.add(textField);
		southPanel.add(clean);
		southPanel.add(button);
		southPanel.add(checkBox[0]);
		southPanel.add(checkBox[1]);
		southPanel.add(File_Button);
		//southPanel.add(otherFile_Button);
		
		clean.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				stringArea.setText(null);
				mingArea.setText(null);
				miArea.setText(null);
				textField.setText(null);
			} 
		});
		
		button.addActionListener(new ActionListener() {     
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				key = textField.getText();
				if(flag==0) {
					plaintext = stringArea.getText();
					miArea.setText(new Core_xyc().DES(plaintext, flag, key, 0));
				}
				else if(flag==1) {
					ciphertext = miArea.getText();
					mingArea.setText(new Core_xyc().DES(ciphertext, flag, key, 0));
				}
				else {
					System.out.println("请选择加密或解密");
				}
			}
		});
		
		File_Button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				key = textField.getText();
				String string1 = new String();
				String string2 = new String();
				
				string1 = new File_xyc().fileChooser();
				string2 = new File_xyc().fileSave();
				new File_xyc().File_DES(string1, string2, key, flag);
				
			}
		});
		

		add(northPanel,BorderLayout.NORTH);
		add(centerPanel, BorderLayout.CENTER);
		add(southPanel, BorderLayout.SOUTH);
		
	}
	
	public static void main(String[] args) {
		DES_xyc frame = new DES_xyc();
		frame.setTitle("DES");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//frame.setSize(600,800);
		frame.pack();
		frame.setVisible(true);
	}
}
