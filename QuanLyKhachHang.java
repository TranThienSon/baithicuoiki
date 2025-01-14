package QuanLyKhachSan;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class QuanLyKhachHang extends JFrame {
    private DefaultTableModel model;
    private JTable table;

    public QuanLyKhachHang() {
        setTitle("Quản Lý Khách Hàng");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Tạo model cho bảng
        String[] columnNames = {"Mã KH", "Tên KH", "CMND", "SĐT"};
        model = new DefaultTableModel(columnNames, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        getContentPane().add(scrollPane, BorderLayout.CENTER);

        // Nút thêm khách hàng
        JButton btnThemKhach = new JButton("Thêm khách hàng");
        btnThemKhach.addActionListener(e -> {
            String tenKH = JOptionPane.showInputDialog("Nhập tên khách hàng:");
            String cmnd = JOptionPane.showInputDialog("Nhập CMND:");
            String sdt = JOptionPane.showInputDialog("Nhập số điện thoại:");

            if (tenKH == null || cmnd == null || sdt == null ||
                tenKH.trim().isEmpty() || cmnd.trim().isEmpty() || sdt.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ thông tin!");
                return;
            }

            try (Connection conn = ConnectSQL.getConnection()) {
                String sql = "INSERT INTO KhachHang (TenKH, CMND, SDT) VALUES (?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, tenKH);
                stmt.setString(2, cmnd);
                stmt.setString(3, sdt);
                stmt.executeUpdate();

                JOptionPane.showMessageDialog(null, "Thêm khách hàng thành công!");
                loadKhachHang(); // Tải lại dữ liệu sau khi thêm
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Lỗi khi thêm khách hàng!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Nút xóa khách hàng
        JButton btnXoaKhach = new JButton("Xóa khách hàng");
        btnXoaKhach.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(null, "Vui lòng chọn khách hàng để xóa!");
                return;
            }

            int maKH = (int) model.getValueAt(selectedRow, 0);

            int confirm = JOptionPane.showConfirmDialog(null,
                    "Bạn có chắc chắn muốn xóa khách hàng với Mã KH: " + maKH + "?",
                    "Xóa khách hàng",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                try (Connection conn = ConnectSQL.getConnection()) {
                    String sql = "DELETE FROM KhachHang WHERE MaKH = ?";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setInt(1, maKH);
                    int rowsDeleted = stmt.executeUpdate();

                    if (rowsDeleted > 0) {
                        JOptionPane.showMessageDialog(null, "Xóa khách hàng thành công!");
                        loadKhachHang(); // Tải lại dữ liệu sau khi xóa
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Lỗi khi xóa khách hàng!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Panel chứa các nút
        JPanel panelButton = new JPanel();
        panelButton.add(btnThemKhach);
        panelButton.add(btnXoaKhach);
        add(panelButton, BorderLayout.SOUTH);

        // Tải dữ liệu từ cơ sở dữ liệu khi khởi tạo
        loadKhachHang();
    }

    private void loadKhachHang() {
        model.setRowCount(0); // Xóa dữ liệu cũ
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM KhachHang");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int maKH = rs.getInt("MaKH");
                String tenKH = rs.getString("TenKH");
                String cmnd = rs.getString("CMND");
                String sdt = rs.getString("SDT");
                model.addRow(new Object[]{maKH, tenKH, cmnd, sdt});
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            QuanLyKhachHang frame = new QuanLyKhachHang();
            frame.setVisible(true);
        });
    }
}
