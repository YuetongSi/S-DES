package function;

import function.SDES_Decrypt;
import function.SDES_Encrypt;
import function.encryptASCII;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class ui extends JFrame{
    JFrame f;
    JPanel jp;
    JLabel jlab1, jlab2,jlab3,jlab4;
    JTextField keytext,text,output;
//    JTextArea output;
    JButton btn1,btn2,btn3;
    JRadioButton bitButton, asciiButton;

    public ui() {
        f = new JFrame("SDES加密解密");
        jp = new JPanel();
        jp.setBackground(new Color(235, 245, 241));

        jlab1 = new JLabel("请输入加解密使用的10位二进制密钥");
        jlab1.setFont(new Font("楷体", 10, 18));
        keytext = new JTextField(15);
        keytext.setPreferredSize(new Dimension (100,35));
        text = new JTextField(20);
        text.setPreferredSize(new Dimension (100,35));

        jlab2 = new JLabel("请输入加解密对应的原文或密文");
        jlab2.setFont(new Font("楷体", 20, 18));

        jlab3 = new JLabel("请选择编码类型");
        jlab3.setFont(new Font("楷体", 10, 18));

        ButtonGroup group = new ButtonGroup();

        bitButton = new JRadioButton("Bit", false);
        bitButton.setBackground(new Color(168, 203, 195));
        asciiButton = new JRadioButton("ASCII", false);
        asciiButton.setBackground(new Color(168, 203, 195));
        group.add(bitButton);
        group.add(asciiButton);
        JPanel northPanel = new JPanel();
        northPanel.add(bitButton);
        northPanel.add(asciiButton);

        add(northPanel, BorderLayout.NORTH);

        btn1 = new JButton("解密");
        btn1.setFont(new java.awt.Font("楷体", 1, 18));
        btn1.setBackground(new Color(168, 203, 195));
        btn1.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn2 = new JButton("加密");
        btn2.setFont(new java.awt.Font("楷体", 1, 18));
        btn2.setBackground(new Color(168, 203, 195));
        btn2.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn3 = new JButton("全部重置");
        btn3.setFont(new java.awt.Font("楷体", 1, 18));
        btn3.setBackground(new Color(168, 203, 195));
        btn3.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));


        jlab4 = new JLabel("结果");
        jlab4.setFont(new Font("楷体", 10, 19));
//        output = new JTextArea(5, 7);
        output=new JTextField();
        output.setPreferredSize(new Dimension (100,80));

    }

    public void displayWindow(){

        Box vBox = Box.createVerticalBox();
        vBox.add(Box.createVerticalStrut(30));
        vBox.add(jlab1);
        vBox.add(Box.createVerticalStrut(7));
        vBox.add(keytext);
        vBox.add(Box.createVerticalStrut(25));
        vBox.add(jlab2);
        vBox.add(Box.createVerticalStrut(7));
        vBox.add(text);
        vBox.add(Box.createVerticalStrut(23));
        //请选择编码类型
        vBox.add(jlab3);

        vBox.add(Box.createVerticalStrut(9));
        vBox.add(bitButton);
        vBox.add(Box.createVerticalStrut(10));
        vBox.add(asciiButton);


        vBox.add(Box.createVerticalStrut(20));
        vBox.add(jlab4);
        vBox.add(Box.createVerticalStrut(7));
        vBox.add(output);
        vBox.add(Box.createVerticalStrut(60));

        Box hBox = Box.createHorizontalBox();
        hBox.add(btn1);
        hBox.add(Box.createHorizontalStrut(20));
        hBox.add(btn2);
        hBox.add(Box.createHorizontalStrut(20));
        hBox.add(btn3);


        jp.add(vBox);
        jp.add(hBox);

        f.setContentPane(jp);
        f.setSize(530, 630);

        // 使用 ImageIcon 加载图片
//        ImageIcon icon = new ImageIcon("E:/acipher/exp1/SDES/src/bg.jpg");
//        JLabel imageLabel = new JLabel(icon);
//        imageLabel.setBounds(0, 0, 435, 202);  // 设置图片的位置和大小
//        add(imageLabel);
        f.setResizable(false);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);

        //解密按钮监听器
        btn1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = text.getText();
                String key = keytext.getText();

                if (key.length() != 10) {
                    JOptionPane.showMessageDialog(null, "请输入10bits密钥！", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (input.length() != 8) {
                    JOptionPane.showMessageDialog(null, "请输入正确的明文或密文！", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                StringBuilder plaintext = new StringBuilder();
                boolean isASCII = asciiButton.isSelected();
                if (isASCII) {
                    for (int i = 0; i < input.length(); i++) {
                        char c = input.charAt(i);
                        String ciphertext = String.valueOf(c);
                        String decrypted = SDES_Decrypt.decryptASCII(ciphertext, key);
                        plaintext.append(decrypted);
                        output.setText("ASCII类型解密输出结果: " + plaintext);
                    }
                } else {
                    int[] plainBinary=StringToBinary(input);
                    int[] Bplaintext = SDES_Decrypt.decrypt(plainBinary, key);
                    output.setText("数组类型解密输出结果: " + Arrays.toString(Bplaintext) + "\n");
                }

            }
        });

        btn2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = text.getText();
                String key = keytext.getText();

                if (key.length() != 10) {
                    JOptionPane.showMessageDialog(null, "请输入正确的密钥！", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (input.length() != 8) {
                    JOptionPane.showMessageDialog(null, "请输入正确的明文或密文！", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                StringBuilder ciphertext = new StringBuilder();
                boolean isASCII = asciiButton.isSelected();
                if (isASCII) {
                    for (int i = 0; i < input.length(); i++) {
                        char c = input.charAt(i);
                        String plaintext = String.valueOf(c);
                        String encrypted = encryptASCII.EncryptASCIIfunction(plaintext, key);
                        ciphertext.append(encrypted);
                        output.setText("ASCII类型加密输出结果: " + ciphertext);
                    }
                } else {
                    int[] plainBinary=StringToBinary(input);
                    int[] Bciphertext = SDES_Encrypt.encrypt(plainBinary, key);
                    output.setText("数组类型加密输出结果: " + Arrays.toString(Bciphertext) + "\n");

                }


            }
        });
        btn3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                keytext.setText("");
                text.setText("");
//                .setText("");
                output.setText("");


            }
        });

    }
    public static int[] StringToBinary(String str) {
        int[] binary = new int[8];
        for (int i = 0; i < 8; i++) {
            Character ch = str.charAt(i);
            binary[i] = Integer.parseInt(ch.toString());
        }

        return binary;
    }


    public static void main(String []args)
    {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        }catch(Exception e) {
            System.out.println(e);
        }
        ui win=new ui();
        win.displayWindow();

    }

}

