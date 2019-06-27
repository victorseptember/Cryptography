import java.awt.*;
import java.awt.RenderingHints.Key;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
//import java.util.Arrays;

import javax.swing.*;
import javax.swing.border.TitledBorder;

public class Caesar_xyc extends JFrame { 
	 
	private JTextArea mingArea = new JTextArea(7,35);
	private JTextArea miArea = new JTextArea(7,35);
	private JTextArea stringArea = new JTextArea(5,35);
	private JCheckBox[] checkBox = {new JCheckBox("加密"),new JCheckBox("解密")};
	private JButton clean = new JButton("清空");
	private JButton button = new JButton("加/解密");
	private JTextField textField = new JTextField(20); 
	private JLabel label_1 = new JLabel("明文");
	private JLabel label_2 = new JLabel("密文");
	private JLabel label_3 = new JLabel("密钥");
	private static int flag = -1;
	private String plaintext;
	private String ciphertext;
	private int key;
	
	public Caesar_xyc() {
		setLayout(new BorderLayout());
		mingArea.setFont(new Font("黑体", Font.PLAIN, 25));
		miArea.setFont(new Font("黑体", Font.PLAIN, 25));
		stringArea.setFont(new Font("黑体", Font.PLAIN, 25));
		textField.setFont(new Font("黑体", Font.PLAIN, 20));
		
		textField.setToolTipText("请输入整数");
		
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
					Caesar_xyc.flag = 0;   //若为加密则flag为0
				}
			}
		});  
		
		checkBox[1].addItemListener(new ItemListener() {     
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				JCheckBox check = (JCheckBox)e.getSource();
				if(check.isSelected()) {
					Caesar_xyc.flag = 1;   //若为解密flag为1
				}
			}
		});
		
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BorderLayout());
		centerPanel.add(label_1,BorderLayout.WEST);
		centerPanel.add(new JScrollPane(mingArea),BorderLayout.SOUTH);
		//centerPanel.setBorder(new TitledBorder(""));
		
		JPanel southPanel = new JPanel();
		southPanel.setLayout(new FlowLayout());
		southPanel.add(label_3);
		southPanel.add(textField);
		southPanel.add(clean);
		southPanel.add(button);
		southPanel.add(checkBox[0]);
		southPanel.add(checkBox[1]);
		
		clean.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				stringArea.setText(null);
				mingArea.setText(null);
				miArea.setText(null);
				textField.setText(null);
			}
		});
		
		button.addActionListener(new ActionListener() {     //
			
			@Override
			public void actionPerformed(ActionEvent e) {
				key = Integer.parseInt(textField.getText());
				//key = calculateKey(key);
				if(flag==0) {
					miArea.setText(new String(encrypt_xyc()));
				}
				else if(flag==1) {
					mingArea.setText(new String(decrypt_xyc()));
				}
				else {
					System.out.println("请选择加密或解密");
				}
			}
		});
		
		add(northPanel,BorderLayout.NORTH);
		add(centerPanel, BorderLayout.CENTER);
		add(southPanel, BorderLayout.SOUTH);
	}
	
	
	public char[] encrypt_xyc() {   //加密函数
 	 	plaintext = stringArea.getText();
 	 	
		char[] chars = plaintext.toCharArray();
		for(int i=0;i<plaintext.length();i++) {
			if (chars[i]>='a'&&chars[i]<='z') {    //若待加密字符为小写字母
				int a = (chars[i]-'a'+key%26+26)%26;
				chars[i] = (char)('a'+a);
			}
			if(chars[i]>='A'&&chars[i]<='Z') {
				int b = (chars[i]-'A'+key%26+26)%26;
				chars[i] = (char)('A'+b);
			}
			if(chars[i]>='0'&&chars[i]<='9') {    //若为数字
				int c = (chars[i]-'0'+key%10+10)%10;
				chars[i] = (char)('0'+c);
			}
			if(chars[i]>=' '&&chars[i]<='/') {    //若为标点符号
				int d = (chars[i]-' '+key%16+16)%16;
				chars[i] = (char)(' '+d);
			}
			if (chars[i]>=':'&&chars[i]<='@') {
				int e = (chars[i]-':'+key%7+7)%7;
				chars[i] = (char)(':'+e);
			}
			if (chars[i]>='['&&chars[i]<='`') {
				int f = (chars[i]-'['+key%6+6)%6;
				chars[i] = (char)('['+f);
			}
			if (chars[i]>='{'&&chars[i]<='~') {
				int g = (chars[i]-'{'+key%4+4)%4;
				chars[i] = (char)('{'+g);
			}
		}
		
		return chars;
		
	}
	
	public char[] decrypt_xyc() {       //解密函数
		ciphertext = miArea.getText();
		
		char[] chars = ciphertext.toCharArray();
		for(int i=0;i<ciphertext.length();i++) {
			if (chars[i]>='a'&&chars[i]<='z') {     //对小写字母解密
				int a = (chars[i]-'a'-key%26+26)%26;
				chars[i] = (char)('a'+a);
			}
			if(chars[i]>='A'&&chars[i]<='Z') {
				int b = (chars[i]-'A'-key%26+26)%26;
				chars[i] = (char)('A'+b);
			}
			if(chars[i]>='0'&&chars[i]<='9') {    //对数字解密
				int c = (chars[i]-'0'-key%10+10)%10;
				chars[i] = (char)('0'+c);
			}
			if(chars[i]>=' '&&chars[i]<='/') {    //对标点符号解密
				int d = (chars[i]-' '-key%16+16)%16;
				chars[i] = (char)(' '+d);
			}
			if (chars[i]>=':'&&chars[i]<='@') {
				int e = (chars[i]-':'-key%7+7)%7;
				chars[i] = (char)(':'+e);
			}
			if (chars[i]>='['&&chars[i]<='`') {
				int f = (chars[i]-'['-key%6+6)%6;
				chars[i] = (char)('['+f);
			}
			if (chars[i]>='{'&&chars[i]<='~') {
				int g = (chars[i]-'{'-key%4+4)%4;
				chars[i] = (char)('{'+g);
			}
		}
		
		return chars;
	}
	
	public static void main(String[] args) {
		Caesar_xyc frame = new Caesar_xyc();
		frame.setTitle("凯撒密码");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		//frame.setSize(600,800);
		frame.setVisible(true);
	}
}
