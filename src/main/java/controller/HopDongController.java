/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
import java.sql.Date; // Sử dụng java.sql.Date cho các thao tác với PreparedStatement

/**
 *
 * @author LAPTOP
 */
public class HopDongController {
    private HopDongView view;

    public void setView(HopDongView view) {
        this.view = view;
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
                stmt = conn.createStatement();

                String sqlQuery = "SELECT hd.ma_hop_dong, hd.ma_nhan_vien, nv.ho_ten, " +
                                  "hd.loai_hop_dong, hd.ngay_bat_dau, hd.ngay_ket_thuc, hd.ngay_ky, hd.trang_thai " +
                                  "FROM hop_dong hd JOIN nhan_vien nv ON hd.ma_nhan_vien = nv.ma_nhan_vien";
                System.out.println("Thực thi truy vấn: " + sqlQuery);
                rs = stmt.executeQuery(sqlQuery);

                if (!rs.isBeforeFirst()) {
                    System.out.println("ResultSet rỗng, không có dữ liệu trả về từ database.");
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
                        HopDongModel.TrangThaiHopDong.valueOf(rs.getString("trang_thai"))
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

    public void loadAllHopDong() {
        List<HopDongModel> danhSachHopDong = getAll();
        if (view != null) {
            view.hienThiDanhSachHopDong(danhSachHopDong);
        }
    }

    public void timKiemHopDong(String tuKhoa) {
        if (tuKhoa.isEmpty()) {
            if (view != null) {
                view.hienThiThongBao("Vui lòng nhập từ khóa tìm kiếm!");
                loadAllHopDong();
            }
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
                             "hd.loai_hop_dong, hd.ngay_bat_dau, hd.ngay_ket_thuc, hd.ngay_ky, hd.trang_thai " +
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
                        HopDongModel.TrangThaiHopDong.valueOf(rs.getString("trang_thai"))
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
            if (ketQua.isEmpty() && !tuKhoa.isEmpty()) {
                view.hienThiThongBao("Không tìm thấy hợp đồng nào phù hợp với từ khóa: " + tuKhoa);
            }
            view.hienThiDanhSachHopDong(ketQua);
        }
    }

    public void themHopDong() {
        if (view != null) {
            view.openAddHopDongDialog(); // Yêu cầu View mở dialog thêm
        }
    }

    public void xuatFile() {
        if (view != null) {
            view.hienThiThongBao("Thực hiện xuất file dữ liệu hợp đồng.");
            // TODO: Implement logic xuất dữ liệu ra file (Excel, PDF, CSV, v.v.)
        }
    }

    public void xemChiTietHopDong(int maHopDong, String hoten) {
        if (view != null) {
            HopDongModel hd = getHopDongById(maHopDong);
            if (hd != null) {
                view.openViewHopDongDialog(hd); // Yêu cầu View mở dialog xem chi tiết
            } else {
                view.hienThiLoi("Không tìm thấy hợp đồng có mã: " + maHopDong);
            }
        }
    }

    public void suaHopDong(int maHopDong, String hoten) {
        if (view != null) {
            HopDongModel hd = getHopDongById(maHopDong);
            if (hd != null) {
                view.openEditHopDongDialog(hd); // Yêu cầu View mở dialog sửa
            } else {
                view.hienThiLoi("Không tìm thấy hợp đồng có mã: " + maHopDong);
            }
        }
    }

    public void xoaHopDong(int maHopDong, String hoten) {
        if (view != null) {
            int confirm = view.hienThiXacNhan("Bạn có chắc chắn muốn xóa hợp đồng: Mã HD=" + maHopDong + " - Tên NV=" + hoten + "?");
            if (confirm == JOptionPane.YES_OPTION) {
                boolean success = deleteHopDongFromDB(maHopDong);
                if (success) {
                    view.hienThiThongBao("Đã xóa hợp đồng: " + maHopDong + " - " + hoten);
                    loadAllHopDong();
                } else {
                    view.hienThiLoi("Xóa hợp đồng thất bại. Vui lòng thử lại.");
                }
            }
        }
    }

    public boolean deleteHopDongFromDB(int maHopDong) {
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
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            if (view != null) view.hienThiLoi("Lỗi SQL khi xóa hợp đồng: " + e.getMessage());
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
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
                             "hd.loai_hop_dong, hd.ngay_bat_dau, hd.ngay_ket_thuc, hd.ngay_ky, hd.trang_thai " +
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
                        HopDongModel.TrangThaiHopDong.valueOf(rs.getString("trang_thai"))
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

    // Phương thức thêm hợp đồng vào database
    public void addHopDong(HopDongModel newHopDong) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            Connect mc = new Connect();
            conn = mc.getConnection();
            if (conn != null) {
                // hoten không có trong bảng hop_dong, chỉ có ma_nhan_vien
                String sql = "INSERT INTO hop_dong (ma_nhan_vien, loai_hop_dong, ngay_bat_dau, ngay_ket_thuc, ngay_ky, trang_thai) VALUES (?, ?, ?, ?, ?, ?)";
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, newHopDong.getMaNhanVien());
                pstmt.setString(2, newHopDong.getLoaiHopDong().name());
                pstmt.setDate(3, new Date(newHopDong.getNgayBatDau().getTime())); // Chuyển đổi util.Date sang sql.Date
                pstmt.setDate(4, new Date(newHopDong.getNgayKetThuc().getTime()));
                pstmt.setDate(5, new Date(newHopDong.getNgayKy().getTime()));
                pstmt.setString(6, newHopDong.getTrangThai().name());
                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0 && view != null) {
                    view.hienThiThongBao("Thêm hợp đồng thành công!");
                    loadAllHopDong(); // Cập nhật lại bảng
                } else {
                     view.hienThiLoi("Thêm hợp đồng thất bại. Có thể Mã nhân viên không tồn tại hoặc lỗi khác.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            view.hienThiLoi("Lỗi SQL khi thêm hợp đồng: " + e.getMessage());
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Phương thức cập nhật hợp đồng vào database
    public void updateHopDong(HopDongModel updatedHopDong) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            Connect mc = new Connect();
            conn = mc.getConnection();
            if (conn != null) {
                // hoten không có trong bảng hop_dong, chỉ có ma_nhan_vien
                String sql = "UPDATE hop_dong SET ma_nhan_vien = ?, loai_hop_dong = ?, ngay_bat_dau = ?, ngay_ket_thuc = ?, ngay_ky = ?, trang_thai = ? WHERE ma_hop_dong = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, updatedHopDong.getMaNhanVien());
                pstmt.setString(2, updatedHopDong.getLoaiHopDong().name());
                pstmt.setDate(3, (updatedHopDong.getNgayBatDau() != null) ? new java.sql.Date(updatedHopDong.getNgayBatDau().getTime()) : null);
                pstmt.setDate(4, (updatedHopDong.getNgayKetThuc() != null) ? new java.sql.Date(updatedHopDong.getNgayKetThuc().getTime()) : null);
                pstmt.setDate(5, (updatedHopDong.getNgayKy() != null) ? new java.sql.Date(updatedHopDong.getNgayKy().getTime()) : null);
                pstmt.setString(6, updatedHopDong.getTrangThai().name());
                pstmt.setInt(7, updatedHopDong.getMaHopDong());
                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0 && view != null) {
                    view.hienThiThongBao("Cập nhật hợp đồng thành công!");
                    loadAllHopDong(); // Cập nhật lại bảng
                } else {
                     view.hienThiLoi("Cập nhật hợp đồng thất bại. Có thể Mã nhân viên không tồn tại hoặc lỗi khác.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            view.hienThiLoi("Lỗi SQL khi cập nhật hợp đồng: " + e.getMessage());
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