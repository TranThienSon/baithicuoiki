
package QuanLyKhachSan;

public class KhachHang {
	    private String ten;
	    private String cmnd;
	    private String sdt;

	    public KhachHang(String ten, String cmnd, String sdt) {
	        this.ten = ten;
	        this.cmnd = cmnd;
	        this.sdt = sdt;
	    }

	    public String getTen() {
	        return ten;
	    }

	    public String getCmnd() {
	        return cmnd;
	    }

	    public String getSdt() {
	        return sdt;
	    }

	    @Override
	    public String toString() {
	        return "Khách hàng: " + ten + " - CMND: " + cmnd + " - SĐT: " + sdt;
	    }
	}




























