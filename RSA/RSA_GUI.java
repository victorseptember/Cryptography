package RSA;

import com.sun.xml.internal.ws.wsdl.writer.document.soap.Body;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import sm3.*;

public class RSA_GUI extends JFrame {
    private JTextField p_area = new JTextField(30);
    private JTextField q_area = new JTextField(30);
    private JTextField N_area = new JTextField(60);
    private JTextField e_area = new JTextField(60);
    private JTextField d_area = new JTextField(60);
    private JTextField plaintext = new JTextField(60);
    private JTextField ciphertext = new JTextField(60);
    private JTextField string = new JTextField(60);
    private JTextField hash_area = new JTextField(30);
    private JTextField signature_area = new JTextField(30);

    private JLabel p_label = new JLabel("P");
    private JLabel q_label = new JLabel("Q");
    private JLabel N_label = new JLabel("N");
    private JLabel e_label = new JLabel("公钥");
    private JLabel d_label = new JLabel("私钥");
    private JLabel plaintext_label = new JLabel("明文");
    private JLabel ciphertext_label = new JLabel("密文");
    private JLabel string_label = new JLabel("解密文");
    private JLabel hash_label = new JLabel("杂凑值");
    private JLabel signature_label = new JLabel("数字签名");

    private JButton key = new JButton("随机产生密钥");
    private JButton encrypt = new JButton("加密");
    private JButton decrypt = new JButton("解密");
    private JButton file_choose = new JButton("文件选择");
    private  JButton signature = new JButton("数字签名");

    BigInt_xyc bigInt = new BigInt_xyc();
    short[] e1,d,N,mi;
    //String ming_text,mi_text;


    public RSA_GUI() {
        setLayout(new BorderLayout());

        p_area.setFont(new Font("黑体", Font.PLAIN, 20));
        q_area.setFont(new Font("黑体", Font.PLAIN, 20));
        N_area.setFont(new Font("黑体", Font.PLAIN, 20));
        e_area.setFont(new Font("黑体", Font.PLAIN, 20));
        d_area.setFont(new Font("黑体", Font.PLAIN, 20));
        plaintext.setFont(new Font("黑体", Font.PLAIN, 20));
        ciphertext.setFont(new Font("黑体", Font.PLAIN, 20));
        string.setFont(new Font("黑体", Font.PLAIN, 20));
        hash_area.setFont(new Font("黑体", Font.PLAIN, 20));
        signature_area.setFont(new Font("黑体",Font.PLAIN,20));

        JPanel panel1 = new JPanel();
        panel1.setLayout(new FlowLayout());
        panel1.add(p_label);
        panel1.add(new JScrollPane(p_area));
        panel1.add(q_label);
        panel1.add(new JScrollPane(q_area));

        JPanel panel2 = new JPanel();
        panel2.setLayout(new FlowLayout());
        panel2.add(N_label);
        panel2.add(new JScrollPane(N_area));

        JPanel panel3 = new JPanel();
        panel3.setLayout(new FlowLayout());
        panel3.add(e_label);
        panel3.add(new JScrollPane(e_area));

        JPanel northpanel = new JPanel();
        northpanel.setLayout(new BorderLayout());
        northpanel.add(panel1,BorderLayout.NORTH);
        northpanel.add(panel2,BorderLayout.CENTER);
        northpanel.add(panel3,BorderLayout.SOUTH);

        JPanel panel4 = new JPanel();
        panel4.setLayout(new FlowLayout());
        panel4.add(d_label);
        panel4.add(new JScrollPane(d_area));

        JPanel panel5 = new JPanel();
        panel5.setLayout(new FlowLayout());
        panel5.add(key);
        panel5.add(encrypt);
        panel5.add(decrypt);
        panel5.add(file_choose);
        panel5.add(signature);

        JPanel panel6 = new JPanel();
        panel6.setLayout(new FlowLayout());
        panel6.add(plaintext_label);
        panel6.add(new JScrollPane(plaintext));

        JPanel centerpanel = new JPanel();
        centerpanel.setLayout(new BorderLayout());
        centerpanel.add(panel4,BorderLayout.NORTH);
        centerpanel.add(panel5,BorderLayout.CENTER);
        centerpanel.add(panel6,BorderLayout.SOUTH);

        JPanel panel7 = new JPanel();
        panel7.setLayout(new FlowLayout());
        panel7.add(ciphertext_label);
        panel7.add(new JScrollPane(ciphertext));

        JPanel panel8 = new JPanel();
        panel8.setLayout(new FlowLayout());
        panel8.add(string_label);
        panel8.add(new JScrollPane(string));

        JPanel panel9 = new JPanel();
        panel9.setLayout(new FlowLayout());
        panel9.add(hash_label);
        panel9.add(new JScrollPane(hash_area));
        panel9.add(signature_label);
        panel9.add(new JScrollPane(signature_area));

        JPanel southpanel = new JPanel();
        southpanel.setLayout(new BorderLayout());
        southpanel.add(panel7,BorderLayout.NORTH);
        southpanel.add(panel8, BorderLayout.CENTER);
        southpanel.add(panel9,BorderLayout.SOUTH);

        add(northpanel,BorderLayout.NORTH);
        add(centerpanel,BorderLayout.CENTER);
        add(southpanel,BorderLayout.SOUTH);

        key.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                short[] p = bigInt.genPrime(BigInt_xyc.SIZE);  //获取密钥
                String p_hex = bigInt.output_hex(p);   //将密钥转换成十六进制字符串形式输出
                p_area.setText(p_hex);

                short[] q = bigInt.genPrime(BigInt_xyc.SIZE);
                String q_hex = bigInt.output_hex(q);
                q_area.setText(q_hex);

                N = bigInt.multi(p,q);
                p[0] = (short) (p[0]-1);
                q[0] = (short) (q[0]-1);
                short[] phiN = bigInt.multi(p,q);
                e1 = bigInt.genE(phiN);
                d = bigInt.genD(e1,phiN);
                String N_hex = bigInt.output_hex(N);
                String e1_hex = bigInt.output_hex(e1);
                String d_hex = bigInt.output_hex(d);
                N_area.setText(N_hex);
                e_area.setText(e1_hex);
                d_area.setText(d_hex);

            }
        });

        encrypt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String ming_text = plaintext.getText();
                short[] ming = bigInt.getMessage(ming_text);   //明文数组
                mi = bigInt.encrypt(ming,e1,N);
                String mi_text = bigInt.getString(mi);
                ciphertext.setText(mi_text);
            }
        });

        decrypt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //String mi_text = ciphertext.getText();
                //short[] mi = bigInt.getMessage(mi_text);
                short[] ming = bigInt.decrypt(mi,d,N);
                String ming_text = bigInt.getString(ming);
                string.setText(ming_text);
            }
        });

        file_choose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String filesource = new SM3_file().fileChooser();
                String hash = new SM3_file().file_sm3(filesource);
                hash_area.setText(hash);
                plaintext.setText(hash);
            }
        });

        signature.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String ming_text = plaintext.getText();
                short[] ming = bigInt.getMessage(ming_text);   //明文数组
                short[] signature = bigInt.encrypt(ming,d,N);
                String signature_text = bigInt.getString(signature);
                signature_area.setText(signature_text);
            }
        });

    }

    public static void main(String[] args) {
        RSA_GUI frame = new RSA_GUI();
        frame.setTitle("RSA");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

}
