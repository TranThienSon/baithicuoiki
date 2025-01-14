package QuanLyKhachSan;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.*;

public class KhachSan extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTable bangPhong;
    private DefaultTableModel model;
    private DatPhongUI datPhongUI;

    public KhachSan() {
        QuanLyPhong quanLyPhong = new QuanLyPhong();
        datPhongUI = new DatPhongUI(quanLyPhong);
        setTitle("Quản Lý Khách Sạn");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 600, 450);
        getContentPane().setLayout(null);

        loadingKhachSan();

        JButton btnQuanLyPhong = new JButton("Quản lý phòng");
        btnQuanLyPhong.setBounds(10, 300, 150, 30);
        getContentPane().add(btnQuanLyPhong);

        JButton btnQuanLyKhach = new JButton("Quản lý khách hàng");
        btnQuanLyKhach.setBounds(180, 300, 150, 30);
        getContentPane().add(btnQuanLyKhach);

        JButton btnDatPhong = new JButton("Đặt phòng");
        btnDatPhong.setBounds(350, 300, 150, 30);
        getContentPane().add(btnDatPhong);

        JButton btnTraPhong = new JButton("Trả phòng");
        btnTraPhong.setBounds(180, 350, 150, 30);
        getContentPane().add(btnTraPhong);

        btnQuanLyPhong.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                quanLyPhong.setVisible(true);
            }
        });

        btnQuanLyKhach.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                QuanLyKhachHang quanLyKhachHang = new QuanLyKhachHang();
                quanLyKhachHang.setVisible(true);
            }
        });

        btnDatPhong.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                datPhongUI.setVisible(true);
            }
        });

        btnTraPhong.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = bangPhong.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn phòng để trả!");
                    return;
                }

                int soPhong = (int) model.getValueAt(selectedRow, 0); // Lấy số phòng được chọn
                String trangThai = (String) model.getValueAt(selectedRow, 2);

                // Kiểm tra trạng thái phòng
                if (trangThai.equals("Còn trống")) {
                    JOptionPane.showMessageDialog(null, "Phòng này đã trống, không cần trả!");
                    return;
                }

                int confirm = JOptionPane.showConfirmDialog(null, "Bạn có chắc chắn muốn trả phòng số: " + soPhong + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    try (Connection conn = ConnectSQL.getConnection()) {
                        // Cập nhật trạng thái phòng trong cơ sở dữ liệu
                        String sql = "UPDATE Phong SET TrangThai = 1 WHERE SoPhong = ?";
                        PreparedStatement stmt = conn.prepareStatement(sql);
                        stmt.setInt(1, soPhong);
                        int rowsUpdated = stmt.executeUpdate();

                        if (rowsUpdated > 0) {
                            JOptionPane.showMessageDialog(null, "Phòng số " + soPhong + " đã được trả thành công!");
                            loadingKhachSan(); // Tải lại bảng sau khi cập nhật
                        } else {
                            JOptionPane.showMessageDialog(null, "Không thể trả phòng. Vui lòng kiểm tra lại!");
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Lỗi khi trả phòng!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // Listener để cập nhật bảng khi quay lại từ giao diện đặt phòng
        datPhongUI.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                loadingKhachSan();
            }
        });
    }

    public void loadingKhachSan() {
        String[] columnNames = { "Số Phòng", "Loại Phòng", "Trạng Thái" };
        if (model == null) {
            model = new DefaultTableModel(columnNames, 0);
            bangPhong = new JTable(model);
            JScrollPane cuonBang = new JScrollPane(bangPhong);
            cuonBang.setBounds(10, 10, 560, 250);
            getContentPane().add(cuonBang);
        }

        try (Connection conn = ConnectSQL.getConnection()) {
            String sql = "SELECT SoPhong, LoaiPhong, TrangThai FROM Phong";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            model.setRowCount(0); // Xóa dữ liệu cũ trước khi thêm dữ liệu mới

            while (rs.next()) {
                int soPhong = rs.getInt("SoPhong");
                String loaiPhong = rs.getString("LoaiPhong");
                boolean trangThai = rs.getBoolean("TrangThai");
                model.addRow(new Object[] { soPhong, loaiPhong, trangThai ? "Còn trống" : "Đã thuê" });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi tải danh sách phòng!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            KhachSan frame = new KhachSan();
            frame.setVisible(true);
        });
    }
}
