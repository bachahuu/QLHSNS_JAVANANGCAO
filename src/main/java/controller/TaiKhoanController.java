/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.TaiKhoanModel;
import model.Connect;
import model.NhanVienDTO;
import java.sql.*;
import java.util.Base64;
/**
 *
 * @author Admin
 */
public class TaiKhoanController {
    public List<TaiKhoanModel> getAll(){
        List<TaiKhoanModel> list = new ArrayList<>();
        String query = "SELECT tai_khoan.ma_tai_khoan, tai_khoan.ma_nhan_vien, tai_khoan.ten_dang_nhap, " +
                      "tai_khoan.vai_tro, tai_khoan.trang_thai, nhan_vien.ho_ten " +
                      "FROM tai_khoan LEFT JOIN nhan_vien ON tai_khoan.ma_nhan_vien = nhan_vien.ma_nhan_vien";

        try (Connection cn = new Connect().getConnection();
             Statement stmt = cn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (cn == null) {
                System.out.println("Không thể kết nối tới cơ sở dữ liệu");
            }

            while (rs.next()) {
                int maTaiKhoan = rs.getInt("ma_tai_khoan");
                int maNhanVien = rs.getInt("ma_nhan_vien");
                String tenDangNhap = rs.getString("ten_dang_nhap");
                String vaiTro = rs.getString("vai_tro");
                String trangThai = rs.getString("trang_thai");
                String tenNhanVien = rs.getString("ho_ten"); // Có thể null nếu LEFT JOIN

                // Nếu bạn thêm tenNhanVien vào TaiKhoanModel, sử dụng constructor phù hợp
                TaiKhoanModel taiKhoan = new TaiKhoanModel();
                taiKhoan.setMaTaiKhoan(maTaiKhoan);
                taiKhoan.setMaNhanVien(maNhanVien);
                taiKhoan.setTenDangNhap(tenDangNhap);
                taiKhoan.setVaiTro(vaiTro);
                taiKhoan.setTrangThai(trangThai);
                // Nếu TaiKhoanModel có thuộc tính tenNhanVien
                taiKhoan.setTenNhanVien(tenNhanVien != null ? tenNhanVien : "Không có");

                list.add(taiKhoan);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Có thể ném lại ngoại lệ hoặc ghi log tùy theo yêu cầu
            System.err.println("Lỗi khi lấy danh sách tài khoản: " + e.getMessage());
        }

        return list;
    }
    
    // Phương thức mã hóa SHA-256 với salt
    public String hashPassword(String password) throws NoSuchAlgorithmException {
        // Tạo salt ngẫu nhiên (16 byte)
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        // Kết hợp password với salt
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(salt);
        byte[] hashedBytes = digest.digest(password.getBytes());

        // Kết hợp salt và hash thành một chuỗi duy nhất (dùng Base64 để mã hóa)
        byte[] saltAndHash = new byte[salt.length + hashedBytes.length];
        System.arraycopy(salt, 0, saltAndHash, 0, salt.length);
        System.arraycopy(hashedBytes, 0, saltAndHash, salt.length, hashedBytes.length);

        return Base64.getEncoder().encodeToString(saltAndHash);
    }

    public boolean insertTaiKhoan(TaiKhoanModel taiKhoan) {
        if (taiKhoan == null || taiKhoan.getTenDangNhap() == null || taiKhoan.getMatKhau() == null ||
            taiKhoan.getVaiTro() == null || taiKhoan.getTrangThai() == null) {
            System.err.println("Dữ liệu tài khoản không hợp lệ");
            return false;
        }

        String sqlCheck = "SELECT COUNT(*) FROM nhan_vien WHERE ma_nhan_vien = ?";
        String sqlInsert = "INSERT INTO tai_khoan (ma_nhan_vien, ten_dang_nhap, mat_khau, vai_tro, trang_thai) VALUES (?, ?, ?, ?, ?)";

        try {
            // Mã hóa mật khẩu
            String hashedPassword = hashPassword(taiKhoan.getMatKhau());

            try (Connection conn = new Connect().getConnection();
                 PreparedStatement pstmtCheck = conn.prepareStatement(sqlCheck);
                 PreparedStatement pstmtInsert = conn.prepareStatement(sqlInsert)) {

                if (conn == null) {
                    System.err.println("Kết nối không thành công");
                    return false;
                }

                // Kiểm tra xem ma_nhan_vien có tồn tại trong nhan_vien không
                pstmtCheck.setInt(1, taiKhoan.getMaNhanVien());
                ResultSet rs = pstmtCheck.executeQuery();
                if (rs.next() && rs.getInt(1) == 0) {
                    System.err.println("Mã nhân viên " + taiKhoan.getMaNhanVien() + " không tồn tại trong bảng nhan_vien");
                    return false;
                }

                // Thực thi lệnh INSERT với mật khẩu đã mã hóa
                pstmtInsert.setInt(1, taiKhoan.getMaNhanVien());
                pstmtInsert.setString(2, taiKhoan.getTenDangNhap());
                pstmtInsert.setString(3, hashedPassword); // Lưu mật khẩu đã mã hóa
                pstmtInsert.setString(4, taiKhoan.getVaiTro());
                pstmtInsert.setString(5, taiKhoan.getTrangThai());

                int rowInsert = pstmtInsert.executeUpdate();
                return rowInsert > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Lỗi khi thêm tài khoản: " + e.getMessage());
            return false;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            System.err.println("Lỗi khi mã hóa mật khẩu: " + e.getMessage());
            return false;
        }
    }
    
    public boolean updateTaiKhoan(TaiKhoanModel taiKhoan) {
        if (taiKhoan == null || taiKhoan.getTenDangNhap() == null || taiKhoan.getMatKhau() == null ||
            taiKhoan.getVaiTro() == null || taiKhoan.getTrangThai() == null) {
            System.err.println("Dữ liệu tài khoản không hợp lệ");
            return false;
        }

        String sqlCheck = "SELECT COUNT(*) FROM nhan_vien WHERE ma_nhan_vien = ?";
        String sqlUpdate = "UPDATE tai_khoan SET ma_nhan_vien = ?, ten_dang_nhap = ?, mat_khau = ?, vai_tro = ?, trang_thai = ? WHERE ma_tai_khoan = ?";

        try (Connection conn = new Connect().getConnection();
             PreparedStatement pstmtCheck = conn.prepareStatement(sqlCheck);
             PreparedStatement pstmtUpdate = conn.prepareStatement(sqlUpdate)) {

            if (conn == null) {
                System.err.println("Kết nối không thành công");
                return false;
            }

            // Kiểm tra xem ma_nhan_vien có tồn tại trong nhan_vien không
            pstmtCheck.setInt(1, taiKhoan.getMaNhanVien());
            ResultSet rs = pstmtCheck.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                System.err.println("Mã nhân viên " + taiKhoan.getMaNhanVien() + " không tồn tại trong bảng nhan_vien");
                return false;
            }

            // Mã hóa mật khẩu nếu cần (giả định matKhau là mật khẩu mới)
            String hashedPassword = taiKhoan.getMatKhau(); // Giữ nguyên nếu không thay đổi
            if (taiKhoan.getMatKhau() != null && !taiKhoan.getMatKhau().isEmpty()) {
                hashedPassword = hashPassword(taiKhoan.getMatKhau());
            }

            // Thực thi lệnh UPDATE
            pstmtUpdate.setInt(1, taiKhoan.getMaNhanVien());
            pstmtUpdate.setString(2, taiKhoan.getTenDangNhap());
            pstmtUpdate.setString(3, hashedPassword);
            pstmtUpdate.setString(4, taiKhoan.getVaiTro());
            pstmtUpdate.setString(5, taiKhoan.getTrangThai());
            pstmtUpdate.setInt(6, taiKhoan.getMaTaiKhoan()); // Điều kiện WHERE

            int rowUpdated = pstmtUpdate.executeUpdate();
            return rowUpdated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Lỗi khi cập nhật tài khoản: " + e.getMessage());
            return false;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            System.err.println("Lỗi khi mã hóa mật khẩu: " + e.getMessage());
            return false;
        }
    }
    
    public List<NhanVienDTO> getNhanVienList() {
        List<NhanVienDTO> nhanVienList = new ArrayList<>();
        String sql = "SELECT ma_nhan_vien, ho_ten FROM nhan_vien";

        try (Connection conn = new Connect().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (conn == null) {
                System.err.println("Kết nối không thành công");
                return nhanVienList;
            }

            while (rs.next()) {
                int maNhanVien = rs.getInt("ma_nhan_vien");
                String hoTen = rs.getString("ho_ten");
                if (hoTen != null) {
                    nhanVienList.add(new NhanVienDTO(maNhanVien, hoTen));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Lỗi khi lấy danh sách nhân viên: " + e.getMessage());
        }

        return nhanVienList;
    }
    
    public void delete(int maTaiKhoan) {
        try {
           Connect mc = new Connect();
           Connection conn = mc.getConnection();
           Statement stmt = conn.createStatement();
             String sql = "DELETE FROM tai_khoan WHERE ma_tai_khoan = " + maTaiKhoan;
             int result = stmt.executeUpdate(sql);
             if (result > 0) {
                    System.out.println("Xóa thành công user có id = " + maTaiKhoan);
                } else {
                    System.out.println("Không tìm thấy user để xóa.");
                }
             conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
    
    public TaiKhoanModel getById(int maTaiKhoan) {
        TaiKhoanModel taiKhoan = null;
        String sql = "SELECT tk.ma_tai_khoan, tk.ma_nhan_vien, tk.ten_dang_nhap, tk.mat_khau, tk.vai_tro, tk.trang_thai, nv.ho_ten " +
                     "FROM tai_khoan tk LEFT JOIN nhan_vien nv ON tk.ma_nhan_vien = nv.ma_nhan_vien " +
                     "WHERE tk.ma_tai_khoan = ?";

        try (Connection conn = new Connect().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (conn == null) {
                System.err.println("Kết nối không thành công");
                return null;
            }

            pstmt.setInt(1, maTaiKhoan);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                taiKhoan = new TaiKhoanModel();
                taiKhoan.setMaTaiKhoan(rs.getInt("ma_tai_khoan"));
                taiKhoan.setMaNhanVien(rs.getInt("ma_nhan_vien"));
                taiKhoan.setTenDangNhap(rs.getString("ten_dang_nhap"));
                taiKhoan.setMatKhau(rs.getString("mat_khau")); // Lấy mật khẩu đã mã hóa
                taiKhoan.setVaiTro(rs.getString("vai_tro"));
                taiKhoan.setTrangThai(rs.getString("trang_thai"));
                taiKhoan.setTenNhanVien(rs.getString("ho_ten") != null ? rs.getString("ho_ten") : "Không có");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Lỗi khi lấy tài khoản theo ID: " + e.getMessage());
        }

        return taiKhoan;
    }
    
    public List<TaiKhoanModel> getByVaiTro(String selectedRole) {
        List<TaiKhoanModel> list = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            Connect mc = new Connect();
            conn = mc.getConnection();
            if (conn != null) {
                stmt = conn.createStatement();
                String query;
                // Kiểm tra null hoặc chuỗi rỗng cho selectedRole
                if (selectedRole == null || selectedRole.trim().isEmpty() || selectedRole.equals("Tất cả vai trò")) {
                    query = "SELECT tk.ma_tai_khoan, tk.ma_nhan_vien, tk.ten_dang_nhap, tk.mat_khau, tk.vai_tro, tk.trang_thai, nv.ho_ten " +
                            "FROM tai_khoan tk LEFT JOIN nhan_vien nv ON tk.ma_nhan_vien = nv.ma_nhan_vien";
                } else {
                    query = "SELECT tk.ma_tai_khoan, tk.ma_nhan_vien, tk.ten_dang_nhap, tk.mat_khau, tk.vai_tro, tk.trang_thai, nv.ho_ten " +
                            "FROM tai_khoan tk LEFT JOIN nhan_vien nv ON tk.ma_nhan_vien = nv.ma_nhan_vien " +
                            "WHERE tk.vai_tro = '" + selectedRole + "'";
                }
                rs = stmt.executeQuery(query);

                while (rs.next()) {
                    TaiKhoanModel taiKhoan = new TaiKhoanModel();
                    taiKhoan.setMaTaiKhoan(rs.getInt("ma_tai_khoan"));
                    taiKhoan.setMaNhanVien(rs.getInt("ma_nhan_vien"));
                    taiKhoan.setTenDangNhap(rs.getString("ten_dang_nhap"));
                    taiKhoan.setMatKhau(rs.getString("mat_khau")); // Lấy mật khẩu đã mã hóa
                    taiKhoan.setVaiTro(rs.getString("vai_tro"));
                    taiKhoan.setTrangThai(rs.getString("trang_thai"));
                    taiKhoan.setTenNhanVien(rs.getString("ho_ten") != null ? rs.getString("ho_ten") : "Không có");
                    list.add(taiKhoan);
                }
            } else {
                System.out.println("Kết nối cơ sở dữ liệu thất bại");
            }
        } catch (Exception e) {
            e.printStackTrace();
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
    
    public List<TaiKhoanModel> getByTrangThai(String selectedStatus) {
        List<TaiKhoanModel> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            Connect mc = new Connect();
            conn = mc.getConnection();
            if (conn != null) {
                String query;
                String trangThai = null;

                // Chuẩn hóa selectedStatus
                if (selectedStatus != null) {
                    selectedStatus = selectedStatus.trim();
                }

                // Kiểm tra trạng thái
                if (selectedStatus == null || selectedStatus.isEmpty() || selectedStatus.equalsIgnoreCase("Tất cả trạng thái")) {
                    query = "SELECT tk.ma_tai_khoan, tk.ma_nhan_vien, tk.ten_dang_nhap, tk.mat_khau, tk.vai_tro, tk.trang_thai, nv.ho_ten " +
                            "FROM tai_khoan tk LEFT JOIN nhan_vien nv ON tk.ma_nhan_vien = nv.ma_nhan_vien";
                    pstmt = conn.prepareStatement(query);
                } else {
                    // Ánh xạ trực tiếp giá trị từ UI
                    if (selectedStatus.equalsIgnoreCase("Hoat_dong") || selectedStatus.equalsIgnoreCase("Hoạt động")) {
                        trangThai = "Hoat_dong";
                    } else if (selectedStatus.equalsIgnoreCase("Bi_khoa") || selectedStatus.equalsIgnoreCase("Bị khóa")) {
                        trangThai = "Bi_khoa";
                    }

                    if (trangThai == null) {
                        query = "SELECT tk.ma_tai_khoan, tk.ma_nhan_vien, tk.ten_dang_nhap, tk.mat_khau, tk.vai_tro, tk.trang_thai, nv.ho_ten " +
                                "FROM tai_khoan tk LEFT JOIN nhan_vien nv ON tk.ma_nhan_vien = nv.ma_nhan_vien";
                        pstmt = conn.prepareStatement(query);
                    } else {
                        query = "SELECT tk.ma_tai_khoan, tk.ma_nhan_vien, tk.ten_dang_nhap, tk.mat_khau, tk.vai_tro, tk.trang_thai, nv.ho_ten " +
                                "FROM tai_khoan tk LEFT JOIN nhan_vien nv ON tk.ma_nhan_vien = nv.ma_nhan_vien " +
                                "WHERE tk.trang_thai = ?";
                        pstmt = conn.prepareStatement(query);
                        pstmt.setString(1, trangThai);
                    }
                }

                System.out.println("Debug - Selected Status: " + selectedStatus + ", TrangThai: " + trangThai + ", Query: " + query);
                rs = pstmt.executeQuery();

                while (rs.next()) {
                    TaiKhoanModel taiKhoan = new TaiKhoanModel();
                    taiKhoan.setMaTaiKhoan(rs.getInt("ma_tai_khoan"));
                    taiKhoan.setMaNhanVien(rs.getInt("ma_nhan_vien"));
                    taiKhoan.setTenDangNhap(rs.getString("ten_dang_nhap"));
                    taiKhoan.setMatKhau(rs.getString("mat_khau"));
                    taiKhoan.setVaiTro(rs.getString("vai_tro"));
                    taiKhoan.setTrangThai(rs.getString("trang_thai"));
                    taiKhoan.setTenNhanVien(rs.getString("ho_ten") != null ? rs.getString("ho_ten") : "Không có");
                    list.add(taiKhoan);
                }
            } else {
                System.out.println("Kết nối cơ sở dữ liệu thất bại");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
    
    public TaiKhoanModel getByTenDangNhap(String tenDangNhap) {
        TaiKhoanModel taiKhoan = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            Connect mc = new Connect();
            conn = mc.getConnection();
            if (conn != null) {
                String query = "SELECT tk.ma_tai_khoan, tk.ma_nhan_vien, tk.ten_dang_nhap, tk.mat_khau, tk.vai_tro, tk.trang_thai, nv.ho_ten " +
                              "FROM tai_khoan tk LEFT JOIN nhan_vien nv ON tk.ma_nhan_vien = nv.ma_nhan_vien " +
                              "WHERE tk.ten_dang_nhap = ?";
                pstmt = conn.prepareStatement(query);
                pstmt.setString(1, tenDangNhap != null ? tenDangNhap.trim() : "");
                rs = pstmt.executeQuery();

                if (rs.next()) {
                    taiKhoan = new TaiKhoanModel();
                    taiKhoan.setMaTaiKhoan(rs.getInt("ma_tai_khoan"));
                    taiKhoan.setMaNhanVien(rs.getInt("ma_nhan_vien"));
                    taiKhoan.setTenDangNhap(rs.getString("ten_dang_nhap"));
                    taiKhoan.setMatKhau(rs.getString("mat_khau"));
                    taiKhoan.setVaiTro(rs.getString("vai_tro"));
                    taiKhoan.setTrangThai(rs.getString("trang_thai"));
                    taiKhoan.setTenNhanVien(rs.getString("ho_ten") != null ? rs.getString("ho_ten") : "Không có");
                }
            } else {
                System.out.println("Kết nối cơ sở dữ liệu thất bại");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return taiKhoan;
    }
    
    public List<TaiKhoanModel> searchByTenDangNhapOrTenNhanVien(String searchTerm) {
        List<TaiKhoanModel> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            Connect mc = new Connect();
            conn = mc.getConnection();
            if (conn != null) {
                String query = "SELECT tk.ma_tai_khoan, tk.ma_nhan_vien, tk.ten_dang_nhap, tk.mat_khau, tk.vai_tro, tk.trang_thai, nv.ho_ten " +
                              "FROM tai_khoan tk LEFT JOIN nhan_vien nv ON tk.ma_nhan_vien = nv.ma_nhan_vien " +
                              "WHERE tk.ten_dang_nhap LIKE ? OR nv.ho_ten LIKE ?";
                pstmt = conn.prepareStatement(query);
                pstmt.setString(1, "%" + searchTerm + "%");
                pstmt.setString(2, "%" + searchTerm + "%");
                rs = pstmt.executeQuery();

                while (rs.next()) {
                    TaiKhoanModel taiKhoan = new TaiKhoanModel();
                    taiKhoan.setMaTaiKhoan(rs.getInt("ma_tai_khoan"));
                    taiKhoan.setMaNhanVien(rs.getInt("ma_nhan_vien"));
                    taiKhoan.setTenDangNhap(rs.getString("ten_dang_nhap"));
                    taiKhoan.setMatKhau(rs.getString("mat_khau")); // Lấy mật khẩu đã mã hóa
                    taiKhoan.setVaiTro(rs.getString("vai_tro"));
                    taiKhoan.setTrangThai(rs.getString("trang_thai"));
                    taiKhoan.setTenNhanVien(rs.getString("ho_ten") != null ? rs.getString("ho_ten") : "Không có");
                    list.add(taiKhoan);
                }
            } else {
                System.out.println("Kết nối cơ sở dữ liệu thất bại");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}
