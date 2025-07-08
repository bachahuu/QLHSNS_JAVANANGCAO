package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import model.Connect;
import model.HopDongModel;
import view_admin.HopDongView;
import java.sql.Date;

/**
 *
 * @author LAPTOP
 */
public class HopDongController {
    private HopDongView view;

    public void setView(HopDongView view) {
        this.view = view;
    }

    public void loadAllHopDong() {
        List<HopDongModel> danhSach = getAll();
        if (view != null) {
            view.hienThiDanhSachHopDong(danhSach);
        }
    }

    public List<HopDongModel> getAll() {
        List<HopDongModel> list = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            Connect mc = new Connect();
            conn = mc.getConnection();

            if (conn != null) {
                System.out.println("Kết nối thành công");
                String sqlQuery = "SELECT hd.ma_hop_dong, hd.ma_nhan_vien, nv.ho_ten, " +
                                  "hd.loai_hop_dong, hd.ngay_bat_dau, hd.ngay_ket_thuc, hd.ngay_ky, hd.trang_thai, hd.luong_co_ban " +
                                  "FROM hop_dong hd JOIN nhan_vien nv ON hd.ma_nhan_vien = nv.ma_nhan_vien";
                System.out.println("Thực thi truy vấn: " + sqlQuery);
                stmt = conn.createStatement();
                rs = stmt.executeQuery(sqlQuery);

                if (!rs.isBeforeFirst()) {
                    System.out.println("Không có dữ liệu hợp đồng nào.");
                }

                while (rs.next()) {
                    HopDongModel hd = new HopDongModel(
                        rs.getInt("ma_hop_dong"),
                        rs.getInt("ma_nhan_vien"),
                        rs.getString("ho_ten"),
                        HopDongModel.LoaiHopDong.valueOf(rs.getString("loai_hop_dong")),
                        rs.getDate("ngay_bat_dau"),
                        rs.getDate("ngay_ket_thuc"),
                        rs.getDate("ngay_ky"),
                        HopDongModel.TrangThaiHopDong.valueOf(rs.getString("trang_thai")),
                        rs.getDouble("luong_co_ban")
                    );
                    list.add(hd);
                    System.out.println("Đã thêm hợp đồng vào danh sách: " + hd.getMaHopDong() + " - " + hd.getHoten());
                }
                System.out.println("Tổng số hợp đồng đã tải: " + list.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (view != null) view.hienThiLoi("Lỗi khi tải danh sách hợp đồng: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public void timKiemHopDong(String tuKhoa) {
        if (tuKhoa == null || tuKhoa.trim().isEmpty()) {
            if (view != null) view.hienThiThongBao("Vui lòng nhập từ khóa tìm kiếm.");
            loadAllHopDong();
            return;
        }

        List<HopDongModel> ketQua = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            Connect mc = new Connect();
            conn = mc.getConnection();

            if (conn != null) {
                String sql = "SELECT hd.ma_hop_dong, hd.ma_nhan_vien, nv.ho_ten, " +
                             "hd.loai_hop_dong, hd.ngay_bat_dau, hd.ngay_ket_thuc, hd.ngay_ky, hd.trang_thai, hd.luong_co_ban " +
                             "FROM hop_dong hd JOIN nhan_vien nv ON hd.ma_nhan_vien = nv.ma_nhan_vien " +
                             "WHERE nv.ho_ten LIKE ? OR CAST(hd.ma_hop_dong AS CHAR) LIKE ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, "%" + tuKhoa + "%");
                pstmt.setString(2, "%" + tuKhoa + "%");

                rs = pstmt.executeQuery();

                while (rs.next()) {
                    HopDongModel hd = new HopDongModel(
                        rs.getInt("ma_hop_dong"),
                        rs.getInt("ma_nhan_vien"),
                        rs.getString("ho_ten"),
                        HopDongModel.LoaiHopDong.valueOf(rs.getString("loai_hop_dong")),
                        rs.getDate("ngay_bat_dau"),
                        rs.getDate("ngay_ket_thuc"),
                        rs.getDate("ngay_ky"),
                        HopDongModel.TrangThaiHopDong.valueOf(rs.getString("trang_thai")),
                        rs.getDouble("luong_co_ban")
                    );
                    ketQua.add(hd);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (view != null) view.hienThiLoi("Lỗi khi tìm kiếm hợp đồng: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (view != null) {
            if (ketQua.isEmpty()) {
                view.hienThiThongBao("Không tìm thấy hợp đồng nào phù hợp.");
            }
            view.hienThiDanhSachHopDong(ketQua);
        }
    }

    public void themHopDong() {
        if (view != null) {
            view.openAddHopDongDialog();
        }
    }

    public void addHopDong(HopDongModel newHopDong) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null; // Added ResultSet for the new check
        try {
            Connect mc = new Connect();
            conn = mc.getConnection();
            if (conn != null) {
                // Check if ma_nhan_vien exists in nhan_vien table
                String checkNVSql = "SELECT COUNT(*) FROM nhan_vien WHERE ma_nhan_vien = ?";
                PreparedStatement checkNVPstmt = conn.prepareStatement(checkNVSql);
                checkNVPstmt.setInt(1, newHopDong.getMaNhanVien());
                rs = checkNVPstmt.executeQuery();
                rs.next();
                if (rs.getInt(1) == 0) {
                    view.hienThiLoi("Mã nhân viên không tồn tại trong hệ thống.");
                    return;
                }
                rs.close();
                checkNVPstmt.close();

                // NEW: Check for duplicate ma_nhan_vien for active contracts
                // This assumes an employee can only have one active contract at a time
                if (newHopDong.getTrangThai() == HopDongModel.TrangThaiHopDong.Con_hieu_luc) {
                    String checkDuplicateContractSql = "SELECT COUNT(*) FROM hop_dong WHERE ma_nhan_vien = ? AND trang_thai = 'Con_hieu_luc'";
                    PreparedStatement checkDuplicatePstmt = conn.prepareStatement(checkDuplicateContractSql);
                    checkDuplicatePstmt.setInt(1, newHopDong.getMaNhanVien());
                    rs = checkDuplicatePstmt.executeQuery();
                    rs.next();
                    if (rs.getInt(1) > 0) {
                        view.hienThiLoi("Nhân viên này đã có hợp đồng còn hiệu lực. Vui lòng kiểm tra lại.");
                        return;
                    }
                    rs.close();
                    checkDuplicatePstmt.close();
                }

                String sql = "INSERT INTO hop_dong (ma_nhan_vien, loai_hop_dong, ngay_bat_dau, ngay_ket_thuc, ngay_ky, trang_thai, luong_co_ban) VALUES (?, ?, ?, ?, ?, ?, ?)";
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, newHopDong.getMaNhanVien());
                pstmt.setString(2, newHopDong.getLoaiHopDong().name());
                pstmt.setDate(3, (newHopDong.getNgayBatDau() != null) ? new java.sql.Date(newHopDong.getNgayBatDau().getTime()) : null);
                pstmt.setDate(4, (newHopDong.getNgayKetThuc() != null) ? new java.sql.Date(newHopDong.getNgayKetThuc().getTime()) : null);
                pstmt.setDate(5, (newHopDong.getNgayKy() != null) ? new java.sql.Date(newHopDong.getNgayKy().getTime()) : null);
                pstmt.setString(6, newHopDong.getTrangThai().name());
                pstmt.setDouble(7, newHopDong.getLuongCoBan());
                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0 && view != null) {
                    view.hienThiThongBao("Thêm hợp đồng thành công!");
                    loadAllHopDong();
                } else {
                     view.hienThiLoi("Thêm hợp đồng thất bại. Có thể Mã nhân viên không tồn tại hoặc lỗi khác.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQLIntegrityConstraintViolationException for actual unique key violations
            if (e.getSQLState().startsWith("23")) { // SQLState for integrity constraint violation
                view.hienThiLoi("Lỗi: Không thể thêm hợp đồng. Có thể do ràng buộc duy nhất bị vi phạm (ví dụ: nhân viên đã có hợp đồng).");
            } else {
                view.hienThiLoi("Lỗi SQL khi thêm hợp đồng: " + e.getMessage());
            }
        } finally {
            try {
                if (rs != null) rs.close(); // Close ResultSet
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void xemChiTietHopDong(int maHopDong, String hoten) {
        HopDongModel hopDong = getHopDongById(maHopDong);
        if (hopDong != null) {
            if (view != null) {
                view.openViewHopDongDialog(hopDong);
            }
        } else {
            if (view != null) view.hienThiLoi("Không tìm thấy hợp đồng với mã: " + maHopDong);
        }
    }

    public HopDongModel getHopDongById(int maHopDong) {
        HopDongModel hd = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            Connect mc = new Connect();
            conn = mc.getConnection();

            if (conn != null) {
                String sql = "SELECT hd.ma_hop_dong, hd.ma_nhan_vien, nv.ho_ten, " +
                             "hd.loai_hop_dong, hd.ngay_bat_dau, hd.ngay_ket_thuc, hd.ngay_ky, hd.trang_thai, hd.luong_co_ban " +
                             "FROM hop_dong hd JOIN nhan_vien nv ON hd.ma_nhan_vien = nv.ma_nhan_vien " +
                             "WHERE hd.ma_hop_dong = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, maHopDong);

                rs = pstmt.executeQuery();

                if (rs.next()) {
                    hd = new HopDongModel(
                        rs.getInt("ma_hop_dong"),
                        rs.getInt("ma_nhan_vien"),
                        rs.getString("ho_ten"),
                        HopDongModel.LoaiHopDong.valueOf(rs.getString("loai_hop_dong")),
                        rs.getDate("ngay_bat_dau"),
                        rs.getDate("ngay_ket_thuc"),
                        rs.getDate("ngay_ky"),
                        HopDongModel.TrangThaiHopDong.valueOf(rs.getString("trang_thai")),
                        rs.getDouble("luong_co_ban")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (view != null) view.hienThiLoi("Lỗi khi lấy chi tiết hợp đồng: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return hd;
    }


    public void suaHopDong(int maHopDong, String hoten) {
        HopDongModel hopDongToEdit = getHopDongById(maHopDong);
        if (hopDongToEdit != null) {
            if (view != null) {
                view.openEditHopDongDialog(hopDongToEdit);
            }
        } else {
            if (view != null) view.hienThiLoi("Không tìm thấy hợp đồng để sửa với mã: " + maHopDong);
        }
    }

    public void updateHopDong(HopDongModel updatedHopDong) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null; // Added ResultSet for the new check
        try {
            Connect mc = new Connect();
            conn = mc.getConnection();
            if (conn != null) {
                // NEW: Check for duplicate ma_nhan_vien for active contracts, excluding the current contract being updated
                if (updatedHopDong.getTrangThai() == HopDongModel.TrangThaiHopDong.Con_hieu_luc) {
                    String checkDuplicateContractSql = "SELECT COUNT(*) FROM hop_dong WHERE ma_nhan_vien = ? AND trang_thai = 'Con_hieu_luc' AND ma_hop_dong != ?";
                    PreparedStatement checkDuplicatePstmt = conn.prepareStatement(checkDuplicateContractSql);
                    checkDuplicatePstmt.setInt(1, updatedHopDong.getMaNhanVien());
                    checkDuplicatePstmt.setInt(2, updatedHopDong.getMaHopDong());
                    rs = checkDuplicatePstmt.executeQuery();
                    rs.next();
                    if (rs.getInt(1) > 0) {
                        view.hienThiLoi("Nhân viên này đã có hợp đồng còn hiệu lực khác. Vui lòng kiểm tra lại.");
                        return;
                    }
                    rs.close();
                    checkDuplicatePstmt.close();
                }

                String sql = "UPDATE hop_dong SET ma_nhan_vien = ?, loai_hop_dong = ?, ngay_bat_dau = ?, ngay_ket_thuc = ?, ngay_ky = ?, trang_thai = ?, luong_co_ban = ? WHERE ma_hop_dong = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, updatedHopDong.getMaNhanVien());
                pstmt.setString(2, updatedHopDong.getLoaiHopDong().name());
                pstmt.setDate(3, (updatedHopDong.getNgayBatDau() != null) ? new java.sql.Date(updatedHopDong.getNgayBatDau().getTime()) : null);
                pstmt.setDate(4, (updatedHopDong.getNgayKetThuc() != null) ? new java.sql.Date(updatedHopDong.getNgayKetThuc().getTime()) : null);
                pstmt.setDate(5, (updatedHopDong.getNgayKy() != null) ? new java.sql.Date(updatedHopDong.getNgayKy().getTime()) : null);
                pstmt.setString(6, updatedHopDong.getTrangThai().name());
                pstmt.setDouble(7, updatedHopDong.getLuongCoBan());
                pstmt.setInt(8, updatedHopDong.getMaHopDong());
                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0 && view != null) {
                    view.hienThiThongBao("Cập nhật hợp đồng thành công!");
                    loadAllHopDong();
                } else {
                     view.hienThiLoi("Cập nhật hợp đồng thất bại. Có thể Mã nhân viên không tồn tại hoặc lỗi khác.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            view.hienThiLoi("Lỗi SQL khi cập nhật hợp đồng: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close(); // Close ResultSet
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void xoaHopDong(int maHopDong, String hoten) {
        if (view != null) {
            int confirm = view.hienThiXacNhan("Bạn có chắc chắn muốn xóa hợp đồng của " + hoten + " (Mã HD: " + maHopDong + ") không?");
            if (confirm == JOptionPane.YES_OPTION) {
                Connection conn = null;
                PreparedStatement pstmt = null;
                try {
                    Connect mc = new Connect();
                    conn = mc.getConnection();
                    if (conn != null) {
                        String sql = "DELETE FROM hop_dong WHERE ma_hop_dong = ?";
                        pstmt = conn.prepareStatement(sql);
                        pstmt.setInt(1, maHopDong);
                        int rowsAffected = pstmt.executeUpdate();
                        if (rowsAffected > 0 && view != null) {
                            view.hienThiThongBao("Xóa hợp đồng thành công!");
                            loadAllHopDong();
                        } else {
                            view.hienThiLoi("Không tìm thấy hợp đồng để xóa hoặc xóa thất bại.");
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    view.hienThiLoi("Lỗi SQL khi xóa hợp đồng: " + e.getMessage());
                } finally {
                    try {
                        if (pstmt != null) pstmt.close();
                        if (conn != null) conn.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    // Phương thức mới để lấy danh sách nhân viên cho ComboBox
    public List<NhanVienItem> getAllNhanVien() {
        List<NhanVienItem> nhanVienList = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            Connect mc = new Connect();
            conn = mc.getConnection();

            if (conn != null) {
                String sqlQuery = "SELECT ma_nhan_vien, ho_ten FROM nhan_vien ORDER BY ho_ten";
                stmt = conn.createStatement();
                rs = stmt.executeQuery(sqlQuery);

                while (rs.next()) {
                    nhanVienList.add(new NhanVienItem(rs.getInt("ma_nhan_vien"), rs.getString("ho_ten")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            if (view != null) view.hienThiLoi("Lỗi khi tải danh sách nhân viên: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return nhanVienList;
    }

    // Lớp nội bộ để biểu diễn cặp Mã nhân viên - Họ tên trong ComboBox
    public static class NhanVienItem {
        private int maNhanVien;
        private String hoTen;

        public NhanVienItem(int maNhanVien, String hoTen) {
            this.maNhanVien = maNhanVien;
            this.hoTen = hoTen;
        }

        public int getMaNhanVien() {
            return maNhanVien;
        }

        public String getHoTen() {
            return hoTen;
        }

        @Override
        public String toString() {
            return hoTen; // Chỉ hiển thị Họ tên
        }
    }
}