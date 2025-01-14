package QuanLyKhachSan;

public class DatPhong {
	    private Phong phong;
	    private KhachHang khachHang;
	    private String ngayNhan;
	    private String ngayTra;

	    public DatPhong(Phong phong, KhachHang khachHang, String ngayNhan, String ngayTra) {
	        this.phong = phong;
	        this.khachHang = khachHang;
	        this.ngayNhan = ngayNhan;
	        this.ngayTra = ngayTra;
	    }

	    public Phong getPhong() {
	        return phong;
	    }

	    public KhachHang getKhachHang() {
	        return khachHang;
	    }

	    public String getNgayNhan() {
	        return ngayNhan;
	    }

	    public String getNgayTra() {
	        return ngayTra;
	    }

	    @Override
	    public String toString() {
	        return "Đặt phòng: " + phong + " - Khách hàng: " + khachHang + " - Nhận: " + ngayNhan + " - Trả: " + ngayTra;
	    }
	}

