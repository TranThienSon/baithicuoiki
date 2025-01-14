package QuanLyKhachSan;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class QuanLyPhong extends JFrame {
    private JTable table;
    private DefaultTableModel model;

    public QuanLyPhong() {
        setTitle("Quản Lý Phòng");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Chỉ hiển thị "Số Phòng" và "Loại Phòng"
        String[] columnNames = {"Số Phòng", "Loại Phòng"};
        model = new DefaultTableModel(columnNames, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        // Panel chứa các nút
        JPanel buttonPanel = new JPanel();
        JButton btnAdd = new JButton("Thêm Phòng");
        JButton btnDelete = new JButton("Xóa Phòng");
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnDelete);

        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        // Load dữ liệu từ bảng `Phong` khi khởi động
        loadPhongData();

        // Xử lý sự kiện Thêm Phòng
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                themPhong();
            }
        });

        // Xử lý sự kiện Xóa Phòng
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                xoaPhong();
            }
        });
    }

    // Phương thức tải dữ liệu từ bảng `Phong` vào JTable
    public void loadPhongData() {
        try (Connection conn = ConnectSQL.getConnection()) {
            String sql = "SELECT SoPhong, LoaiPhong FROM Phong"; // Chỉ chọn các cột cần thiết
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            // Xóa các dòng cũ trong bảng
            model.setRowCount(0);

            // Thêm dữ liệu vào bảng
            while (rs.next()) {
                int soPhong = rs.getInt("SoPhong");
                String loaiPhong = rs.getString("LoaiPhong");
                model.addRow(new Object[]{soPhong, loaiPhong});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Phương thức thêm phòng
    public void themPhong() {
        JTextField txtSoPhong = new JTextField();
        JTextField txtLoaiPhong = new JTextField();

        Object[] inputFields = {
                "Số Phòng:", txtSoPhong,
                "Loại Phòng:", txtLoaiPhong
        };

        int option = JOptionPane.showConfirmDialog(this, inputFields, "Thêm Phòng", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try (Connection conn = ConnectSQL.getConnection()) {
                String sql = "INSERT INTO Phong (SoPhong, LoaiPhong) VALUES (?, ?)"; // Bỏ cột `TrangThai`
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, Integer.parseInt(txtSoPhong.getText()));
                stmt.setString(2, txtLoaiPhong.getText());

                int rowsInserted = stmt.executeUpdate();
                if (rowsInserted > 0) {
                    JOptionPane.showMessageDialog(this, "Thêm phòng thành công!");
                    loadPhongData(); // Cập nhật lại JTable
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi thêm phòng!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Số phòng phải là số!");
            }
        }
    }

    // Phương thức xóa phòng
    public void xoaPhong() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phòng để xóa!");
            return;
        }

        int soPhong = (int) model.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa phòng " + soPhong + "?", "Xóa Phòng", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = ConnectSQL.getConnection()) {
                String sql = "DELETE FROM Phong WHERE SoPhong = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, soPhong);

                int rowsDeleted = stmt.executeUpdate();
                if (rowsDeleted > 0) {
                    JOptionPane.showMessageDialog(this, "Xóa phòng thành công!");
                    loadPhongData(); // Cập nhật lại JTable
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi xóa phòng!");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            QuanLyPhong quanLyPhong = new QuanLyPhong();
            quanLyPhong.setVisible(true);
        });
    }
}
