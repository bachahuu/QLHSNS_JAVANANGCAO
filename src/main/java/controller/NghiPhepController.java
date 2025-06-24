package controller;

import java.io.File; // Thêm import này
import model.Connect;
import model.NghiPhepModel;
import view_admin.NghiPhepView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap; // Để duy trì thứ tự chèn
import java.util.Map;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel; // Đã có, đảm bảo nó vẫn ở đây


public class NghiPhepController {
    private NghiPhepView view;

    public void setView(NghiPhepView view) {
        this.view = view;
    }

    // Phương thức tải tất cả dữ liệu nghỉ phép từ DB và hiển thị lên View
    public void loadAllNghiPhep() {
        List<NghiPhepModel> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            Connect mc = new Connect();
            conn = mc.getConnection();

            if (conn != null) {
                System.out.println("Kết nối thành công đến database.");
                String sqlQuery = "SELECT np.ma_nghi_phep, nv.ho_ten, np.ma_nhan_vien, np.loai_nghi, np.ngay_bat_dau, np.ngay_ket_thuc, np.ly_do, np.trang_thai " +
                                  "FROM nghi_phep np JOIN nhan_vien nv ON np.ma_nhan_vien = nv.ma_nhan_vien ORDER BY np.ma_nghi_phep DESC";
                pstmt = conn.prepareStatement(sqlQuery);
                rs = pstmt.executeQuery();

                while (rs.next()) {
                    NghiPhepModel nghiPhep = new NghiPhepModel(
                        rs.getInt("ma_nghi_phep"),
                        rs.getInt("ma_nhan_vien"),
                        rs.getString("loai_nghi"),
                        rs.getDate("ngay_bat_dau"),
                        rs.getDate("ngay_ket_thuc"),
                        rs.getString("ly_do"),
                        rs.getString("trang_thai"),
                        rs.getString("ho_ten") // Lấy họ tên từ bảng nhan_vien
                    );
                    list.add(nghiPhep);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            if (view != null) {
                view.hienThiLoi("Lỗi SQL khi tải dữ liệu nghỉ phép: " + e.getMessage());
            }
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
            view.hienThiDanhSachNghiPhep(list);
        }
    }

    // Phương thức thêm mới một đơn nghỉ phép
    public void addNghiPhep(NghiPhepModel nghiPhep) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            Connect mc = new Connect();
            conn = mc.getConnection();

            if (conn != null) {
                String sql = "INSERT INTO nghi_phep (ma_nhan_vien, loai_nghi, ngay_bat_dau, ngay_ket_thuc, ly_do, trang_thai) VALUES (?, ?, ?, ?, ?, ?)";
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, nghiPhep.getMaNhanVien());
                pstmt.setString(2, nghiPhep.getLoaiNghi());
                pstmt.setDate(3, nghiPhep.getNgayBatDau());
                pstmt.setDate(4, nghiPhep.getNgayKetThuc());
                pstmt.setString(5, nghiPhep.getLyDo());
                pstmt.setString(6, nghiPhep.getTrangThai());

                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0 && view != null) {
                    view.hienThiThongBao("Thêm đơn nghỉ phép thành công!");
                    loadAllNghiPhep(); // Cập nhật lại bảng
                } else if (view != null) {
                    view.hienThiLoi("Thêm đơn nghỉ phép thất bại. Vui lòng kiểm tra Mã nhân viên có tồn tại.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            if (view != null) {
                if (e.getMessage().contains("Cannot add or update a child row: a foreign key constraint fails")) {
                    view.hienThiLoi("Lỗi: Mã nhân viên không tồn tại. Vui lòng chọn một nhân viên hợp lệ.");
                } else {
                    view.hienThiLoi("Lỗi SQL khi thêm nghỉ phép: " + e.getMessage());
                }
            }
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Phương thức cập nhật thông tin đơn nghỉ phép
    public void updateNghiPhep(NghiPhepModel nghiPhep) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            Connect mc = new Connect();
            conn = mc.getConnection();

            if (conn != null) {
                String sql = "UPDATE nghi_phep SET ma_nhan_vien = ?, loai_nghi = ?, ngay_bat_dau = ?, ngay_ket_thuc = ?, ly_do = ?, trang_thai = ? WHERE ma_nghi_phep = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, nghiPhep.getMaNhanVien());
                pstmt.setString(2, nghiPhep.getLoaiNghi());
                pstmt.setDate(3, nghiPhep.getNgayBatDau());
                pstmt.setDate(4, nghiPhep.getNgayKetThuc());
                pstmt.setString(5, nghiPhep.getLyDo());
                pstmt.setString(6, nghiPhep.getTrangThai());
                pstmt.setInt(7, nghiPhep.getMaNghiPhep());

                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0 && view != null) {
                    view.hienThiThongBao("Cập nhật đơn nghỉ phép thành công!");
                    loadAllNghiPhep(); // Cập nhật lại bảng
                } else if (view != null) {
                    view.hienThiLoi("Cập nhật đơn nghỉ phép thất bại. Có thể Mã nghỉ phép không tồn tại hoặc lỗi khác.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            if (view != null) {
                if (e.getMessage().contains("Cannot add or update a child row: a foreign key constraint fails")) {
                    view.hienThiLoi("Lỗi: Mã nhân viên không tồn tại. Vui lòng chọn một nhân viên hợp lệ.");
                } else {
                    view.hienThiLoi("Lỗi SQL khi cập nhật nghỉ phép: " + e.getMessage());
                }
            }
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Phương thức xóa một đơn nghỉ phép
    public void deleteNghiPhep(int maNghiPhep, String hoten) { // Giữ hoten để hiển thị thông báo, maNghiPhep để xóa
        if (view != null && view.hienThiXacNhan("Bạn có chắc chắn muốn xóa đơn nghỉ phép của " + hoten + " có mã " + maNghiPhep + " này không?") == JOptionPane.YES_OPTION) {
            Connection conn = null;
            PreparedStatement pstmt = null;

            try {
                Connect mc = new Connect();
                conn = mc.getConnection();

                if (conn != null) {
                    String sql = "DELETE FROM nghi_phep WHERE ma_nghi_phep = ?";
                    pstmt = conn.prepareStatement(sql);
                    pstmt.setInt(1, maNghiPhep);

                    int rowsAffected = pstmt.executeUpdate();
                    if (rowsAffected > 0 && view != null) {
                        view.hienThiThongBao("Xóa đơn nghỉ phép thành công!");
                        loadAllNghiPhep(); // Cập nhật lại bảng
                    } else if (view != null) {
                        view.hienThiLoi("Xóa đơn nghỉ phép thất bại. Không tìm thấy đơn nghỉ phép với mã " + maNghiPhep + ".");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                if (view != null) {
                    view.hienThiLoi("Lỗi SQL khi xóa nghỉ phép: " + e.getMessage());
                }
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

    // Phương thức tìm kiếm nghỉ phép theo họ tên nhân viên
    public void timKiemNghiPhep(String tuKhoa) {
        List<NghiPhepModel> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            Connect mc = new Connect();
            conn = mc.getConnection();

            if (conn != null) {
                String sqlQuery = "SELECT np.ma_nghi_phep, nv.ho_ten, np.ma_nhan_vien, np.loai_nghi, np.ngay_bat_dau, np.ngay_ket_thuc, np.ly_do, np.trang_thai " +
                                  "FROM nghi_phep np JOIN nhan_vien nv ON np.ma_nhan_vien = nv.ma_nhan_vien " +
                                  "WHERE nv.ho_ten LIKE ? ORDER BY np.ma_nghi_phep DESC"; // DÒNG NÀY ĐANG CHỈ TÌM THEO ho_ten
                pstmt = conn.prepareStatement(sqlQuery);
                pstmt.setString(1, "%" + tuKhoa + "%");
                rs = pstmt.executeQuery();

                while (rs.next()) {
                    NghiPhepModel nghiPhep = new NghiPhepModel(
                        rs.getInt("ma_nghi_phep"),
                        rs.getInt("ma_nhan_vien"),
                        rs.getString("loai_nghi"),
                        rs.getDate("ngay_bat_dau"),
                        rs.getDate("ngay_ket_thuc"),
                        rs.getString("ly_do"),
                        rs.getString("trang_thai"),
                        rs.getString("ho_ten")
                    );
                    list.add(nghiPhep);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            if (view != null) {
                view.hienThiLoi("Lỗi SQL khi tìm kiếm nghỉ phép: " + e.getMessage());
            }
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
            view.hienThiDanhSachNghiPhep(list);
        }
    }

    // Phương thức xem chi tiết nghỉ phép (dựa vào mã nghỉ phép)
    public void xemChiTietNghiPhep(int maNghiPhep, String hoTenNhanVien) {
        NghiPhepModel nghiPhep = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            Connect mc = new Connect();
            conn = mc.getConnection();

            if (conn != null) {
                String sqlQuery = "SELECT np.ma_nghi_phep, np.ma_nhan_vien, np.loai_nghi, np.ngay_bat_dau, np.ngay_ket_thuc, np.ly_do, np.trang_thai " +
                                  "FROM nghi_phep np WHERE np.ma_nghi_phep = ?";
                pstmt = conn.prepareStatement(sqlQuery);
                pstmt.setInt(1, maNghiPhep);
                rs = pstmt.executeQuery();

                if (rs.next()) {
                    nghiPhep = new NghiPhepModel(
                        rs.getInt("ma_nghi_phep"),
                        rs.getInt("ma_nhan_vien"),
                        rs.getString("loai_nghi"),
                        rs.getDate("ngay_bat_dau"),
                        rs.getDate("ngay_ket_thuc"),
                        rs.getString("ly_do"),
                        rs.getString("trang_thai"),
                        hoTenNhanVien // Sử dụng họ tên đã truyền vào
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            if (view != null) {
                view.hienThiLoi("Lỗi SQL khi lấy thông tin nghỉ phép: " + e.getMessage());
            }
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
            if (nghiPhep != null) {
                view.openViewNghiPhepDialog(nghiPhep);
            } else {
                view.hienThiLoi("Không tìm thấy thông tin chi tiết cho mã nghỉ phép: " + maNghiPhep);
            }
        }
    }

    // Phương thức mở dialog thêm nghỉ phép và cung cấp danh sách nhân viên
    public void themNghiPhep() {
        Map<Integer, String> danhSachNhanVien = getDanhSachNhanVien();
        if (danhSachNhanVien != null && !danhSachNhanVien.isEmpty()) {
            view.openAddNghiPhepDialog(danhSachNhanVien);
        } else {
            view.hienThiLoi("Không thể tải danh sách nhân viên. Vui lòng kiểm tra kết nối cơ sở dữ liệu và dữ liệu nhân viên.");
        }
    }

    // Phương thức mở dialog sửa nghỉ phép và cung cấp danh sách nhân viên
    public void suaNghiPhep(int maNghiPhep, String hoTenNhanVien) {
        NghiPhepModel nghiPhepToEdit = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            Connect mc = new Connect();
            conn = mc.getConnection();

            if (conn != null) {
                String sqlQuery = "SELECT np.ma_nghi_phep, np.ma_nhan_vien, np.loai_nghi, np.ngay_bat_dau, np.ngay_ket_thuc, np.ly_do, np.trang_thai " +
                                  "FROM nghi_phep np WHERE np.ma_nghi_phep = ?";
                pstmt = conn.prepareStatement(sqlQuery);
                pstmt.setInt(1, maNghiPhep);
                rs = pstmt.executeQuery();

                if (rs.next()) {
                    nghiPhepToEdit = new NghiPhepModel(
                        rs.getInt("ma_nghi_phep"),
                        rs.getInt("ma_nhan_vien"),
                        rs.getString("loai_nghi"),
                        rs.getDate("ngay_bat_dau"),
                        rs.getDate("ngay_ket_thuc"),
                        rs.getString("ly_do"),
                        rs.getString("trang_thai"),
                        hoTenNhanVien // Sử dụng họ tên đã truyền vào
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            if (view != null) {
                view.hienThiLoi("Lỗi SQL khi lấy thông tin nghỉ phép để sửa: " + e.getMessage());
            }
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
            if (nghiPhepToEdit != null) {
                Map<Integer, String> danhSachNhanVien = getDanhSachNhanVien();
                if (danhSachNhanVien != null && !danhSachNhanVien.isEmpty()) {
                    view.openEditNghiPhepDialog(nghiPhepToEdit, danhSachNhanVien);
                } else {
                    view.hienThiLoi("Không thể tải danh sách nhân viên. Vui lòng kiểm tra kết nối cơ sở dữ liệu và dữ liệu nhân viên.");
                }
            } else {
                view.hienThiLoi("Không tìm thấy đơn nghỉ phép để sửa với mã: " + maNghiPhep);
            }
        }
    }


    // Phương thức lấy danh sách mã nhân viên và họ tên từ bảng nhan_vien
    public Map<Integer, String> getDanhSachNhanVien() {
        Map<Integer, String> danhSachNhanVien = new LinkedHashMap<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            Connect mc = new Connect();
            conn = mc.getConnection();

            if (conn != null) {
                stmt = conn.createStatement();
                String sql = "SELECT ma_nhan_vien, ho_ten FROM nhan_vien ORDER BY ho_ten";
                rs = stmt.executeQuery(sql);

                while (rs.next()) {
                    danhSachNhanVien.put(rs.getInt("ma_nhan_vien"), rs.getString("ho_ten"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            if (view != null) {
                view.hienThiLoi("Lỗi SQL khi lấy danh sách nhân viên: " + e.getMessage());
            }
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return danhSachNhanVien;
    }

    // Export data to CSV
    public void xuatFile(DefaultTableModel tableModel) { // Cập nhật chữ ký phương thức
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Lưu file Excel");
        fileChooser.setFileFilter(new FileNameExtensionFilter("CSV (Comma Separated Values)", "csv"));
        fileChooser.setSelectedFile(new File("danh_sach_nghi_phep.csv")); // Default filename

        int userSelection = fileChooser.showSaveDialog(view);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            // Ensure the file has a .csv extension
            String filePath = fileToSave.getAbsolutePath();
            if (!filePath.toLowerCase().endsWith(".csv")) {
                fileToSave = new File(filePath + ".csv");
            }

            try (FileWriter writer = new FileWriter(fileToSave)) {
                // Write header
                for (int i = 0; i < tableModel.getColumnCount() - 1; i++) { // Exclude "Thao tác" column
                    writer.append(tableModel.getColumnName(i));
                    if (i < tableModel.getColumnCount() - 2) {
                        writer.append(",");
                    }
                }
                writer.append("\n");

                // Write data rows
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    for (int j = 0; j < tableModel.getColumnCount() - 1; j++) { // Exclude "Thao tác" column
                        writer.append(String.valueOf(tableModel.getValueAt(i, j)));
                        if (j < tableModel.getColumnCount() - 2) {
                            writer.append(",");
                        }
                    }
                    writer.append("\n");
                }
                view.hienThiThongBao("Dữ liệu đã được xuất ra file CSV thành công tại: " + fileToSave.getAbsolutePath());
            } catch (IOException ex) {
                view.hienThiLoi("Lỗi khi xuất file: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }
}