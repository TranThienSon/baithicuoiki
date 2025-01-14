package QuanLyKhachSan;

public class Phong {
    private int soPhong;
    private String loaiPhong;
    private boolean trangThai;

    public Phong(int soPhong, String loaiPhong, boolean trangThai) {
        this.soPhong = soPhong;
        this.loaiPhong = loaiPhong;
        this.trangThai = trangThai;
    }

    public int getSoPhong() {
        return soPhong;
    }

    public String getLoaiPhong() {
        return loaiPhong;
    }

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }

    @Override
    public String toString() {
        return "Phòng số " + soPhong + " - Loại: " + loaiPhong + " - " + (trangThai ? "Còn trống" : "Đã thuê");
    }
}
