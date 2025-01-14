package QuanLyKhachSan;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class DatPhongUI extends JFrame {
	private JTextField txtPhong, txtKhachHang, txtNgayNhan, txtNgayTra, txtLoaiPhong;
	private QuanLyPhong quanLyPhong;
	
	public DatPhongUI(QuanLyPhong quanLyPhong) {
		this.quanLyPhong = quanLyPhong; // Nhận đối tượng QuanLyPhong từ constructor
		
		setTitle("Đặt Phòng");
		setSize(400, 350);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(new GridLayout(6, 2, 10, 10));

		add(new JLabel("Số Phòng:"));
		txtPhong = new JTextField();
		add(txtPhong);

		add(new JLabel("Tên Khách Hàng:"));
		txtKhachHang = new JTextField();
		add(txtKhachHang);

		add(new JLabel("Ngày Nhận (YYYY-MM-DD):"));
		txtNgayNhan = new JTextField();
		add(txtNgayNhan);

		add(new JLabel("Ngày Trả (YYYY-MM-DD):"));
		txtNgayTra = new JTextField();
		add(txtNgayTra);

		add(new JLabel("Loại Phòng:"));
		txtLoaiPhong = new JTextField();
		add(txtLoaiPhong);

		JButton btnDatPhong = new JButton("Đặt Phòng");
		btnDatPhong.addActionListener(e -> {
			String soPhong = txtPhong.getText();
			String tenKhach = txtKhachHang.getText();
			String ngayNhan = txtNgayNhan.getText();
			String ngayTra = txtNgayTra.getText();
			String loaiPhong = txtLoaiPhong.getText();

			// Kiểm tra thông tin nhập
			if (soPhong.trim().isEmpty() || tenKhach.trim().isEmpty() || ngayNhan.trim().isEmpty()
					|| ngayTra.trim().isEmpty() || loaiPhong.trim().isEmpty()) {
				JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ thông tin!");
				return;
			}

			// Hiển thị thông tin đặt phòng
			String message = String.format(
					"Thông tin đặt phòng:\nSố Phòng: %s\nTên Khách Hàng: %s\nNgày Nhận: %s\nNgày Trả: %s\nLoại Phòng: %s",
					soPhong, tenKhach, ngayNhan, ngayTra, loaiPhong);
			JOptionPane.showMessageDialog(null, message, "Chi Tiết Đặt Phòng", JOptionPane.INFORMATION_MESSAGE);

			try (Connection conn = ConnectSQL.getConnection()) {
				// Thêm dữ liệu vào bảng DatPhong
				String sqlDatPhong = "INSERT INTO DatPhong (SoPhong, TenKhach, NgayNhan, NgayTra, LoaiPhong) VALUES (?, ?, ?, ?, ?)";
				PreparedStatement stmtDatPhong = conn.prepareStatement(sqlDatPhong);
				stmtDatPhong.setInt(1, Integer.parseInt(soPhong)); // Sử dụng kiểu INT cho SoPhong
				stmtDatPhong.setString(2, tenKhach);
				stmtDatPhong.setString(3, ngayNhan);
				stmtDatPhong.setString(4, ngayTra);
				stmtDatPhong.setString(5, loaiPhong);
				stmtDatPhong.executeUpdate();
				// Cập nhật trạng thái phòng trong bảng Phong
				String sqlPhong = "UPDATE Phong SET TrangThai = 0 WHERE SoPhong = ?";
				PreparedStatement stmtPhong = conn.prepareStatement(sqlPhong);
				stmtPhong.setInt(1, Integer.parseInt(soPhong)); // Sử dụng kiểu INT cho SoPhong
				stmtPhong.executeUpdate();

				// Cập nhật lại dữ liệu trong bảng quản lý phòng
				if (quanLyPhong != null) {
					quanLyPhong.loadPhongData(); // Đảm bảo không phải null
				}
											
				JOptionPane.showMessageDialog(null, "Đặt phòng thành công!");

				dispose();
				System.out.println("exit2.");
			} catch (SQLException ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(null, "Lỗi khi đặt phòng!", "Error", JOptionPane.ERROR_MESSAGE);
			}
		});
		add(btnDatPhong);

		JButton btnHuy = new JButton("Hủy");
		btnHuy.addActionListener(e -> dispose());
		add(btnHuy);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			// Khởi tạo QuanLyPhong trước khi truyền vào DatPhongUI
			QuanLyPhong quanLyPhong = new QuanLyPhong(); // Khởi tạo QuanLyPhong
			quanLyPhong.setVisible(true); // Hiển thị QuanLyPhong

			// Khởi tạo và hiển thị cửa sổ DatPhongUI
			DatPhongUI frame = new DatPhongUI(quanLyPhong);
			frame.setVisible(true);
		});
	}
}
