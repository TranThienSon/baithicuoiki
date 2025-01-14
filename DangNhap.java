package QuanLyKhachSan;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DangNhap extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTextField txtTenDangNhap;
    private JPasswordField txtMatKhau;
    public DangNhap() {
        setTitle("Đăng Nhập");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 250);
        setLocationRelativeTo(null); 
        setLayout(new BorderLayout());
        JPanel panelChinh = new JPanel();
        panelChinh.setLayout(new GridLayout(3, 2, 10, 10));
        panelChinh.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTenDangNhap = new JLabel("Tên đăng nhập:");
        txtTenDangNhap = new JTextField();
        JLabel lblMatKhau = new JLabel("Mật khẩu:");
        txtMatKhau = new JPasswordField();

        panelChinh.add(lblTenDangNhap);
        panelChinh.add(txtTenDangNhap);
        panelChinh.add(lblMatKhau);
        panelChinh.add(txtMatKhau);

        add(panelChinh, BorderLayout.CENTER);
        JButton btnDangNhap = new JButton("Đăng nhập");
        btnDangNhap.addActionListener(new ActionListener() {
      
            public void actionPerformed(ActionEvent e) {
                xuLyDangNhap();
            }
        });

        JPanel panelButton = new JPanel();
        panelButton.add(btnDangNhap);
        add(panelButton, BorderLayout.SOUTH);
    }
    private void xuLyDangNhap() {
        String tenDangNhap = txtTenDangNhap.getText();
        String matKhau = new String(txtMatKhau.getPassword());
        if (tenDangNhap.equals("detaiquanlykhachsan") && matKhau.equals("123456")) {
            JOptionPane.showMessageDialog(this, "Đăng nhập thành công!");
            dispose(); 
            SwingUtilities.invokeLater(() -> {
                KhachSan khachSan = new KhachSan();
                khachSan.setVisible(true);
            });
        } else {
            JOptionPane.showMessageDialog(this, "Tên đăng nhập hoặc mật khẩu không đúng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DangNhap dangNhapGUI = new DangNhap();
            dangNhapGUI.setVisible(true);
        });
    }
}
