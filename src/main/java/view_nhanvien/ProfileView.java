/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view_nhanvien;

import controller.ChucVuController;
import controller.PhongBanController;
import javax.swing.JPanel;
import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import model.ContractModel;
import model.NhanSuModel;
import java.util.List;
/**
 *
 * @author Windows
 */
public class ProfileView extends JPanel {
    public ProfileView(NhanSuModel nhanVien, ContractModel hopDong) {
                // Tạo panel chính chia làm 2 cột
        setLayout(new BorderLayout());
        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        mainPanel.setBackground(Color.WHITE); // Màu nền trắng

        // Panel thông tin nhân viên (bên trái)
        JPanel employeePanel = new JPanel(new GridLayout(12, 2, 5, 5));
        employeePanel.setBorder(BorderFactory.createTitledBorder("Thông tin nhân viên"));
        employeePanel.setBackground(Color.WHITE);

        // Panel thông tin hợp đồng (bên phải)
        JPanel contractPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        contractPanel.setBorder(BorderFactory.createTitledBorder("Thông tin hợp đồng"));
        contractPanel.setBackground(Color.WHITE);

        // Font chữ
        Font labelFont = new Font("Arial", Font.PLAIN, 14);
        Font fieldFont = new Font("Arial", Font.PLAIN, 14);

        // ========== PHẦN THÔNG TIN NHÂN VIÊN ==========
        JLabel maNhanVienLabel = new JLabel("Mã nhân viên:");
        maNhanVienLabel.setFont(labelFont);
        employeePanel.add(maNhanVienLabel);
        JTextField maNhanVienField = new JTextField(nhanVien.getMaSo());
        maNhanVienField.setFont(fieldFont);
        maNhanVienField.setEditable(false);
        maNhanVienField.setHorizontalAlignment(JTextField.CENTER);
        maNhanVienField.setPreferredSize(new Dimension(150, 25));
        employeePanel.add(maNhanVienField);

        JLabel hoVaTenLabel = new JLabel("Họ và tên:");
        hoVaTenLabel.setFont(labelFont);
        employeePanel.add(hoVaTenLabel);
        JTextField hoVaTenField = new JTextField(nhanVien.getHoTen());
        hoVaTenField.setFont(fieldFont);
        hoVaTenField.setEditable(false);
        hoVaTenField.setHorizontalAlignment(JTextField.CENTER);
        hoVaTenField.setPreferredSize(new Dimension(150, 25));
        employeePanel.add(hoVaTenField);

        JLabel gioiTinhLabel = new JLabel("Giới tính:");
        gioiTinhLabel.setFont(labelFont);
        employeePanel.add(gioiTinhLabel);
        JTextField gioiTinhField = new JTextField(nhanVien.getGioiTinh());
        gioiTinhField.setFont(fieldFont);
        gioiTinhField.setEditable(false);
        gioiTinhField.setHorizontalAlignment(JTextField.CENTER);
        gioiTinhField.setPreferredSize(new Dimension(150, 25));
        employeePanel.add(gioiTinhField);

        JLabel ngaySinhLabel = new JLabel("Ngày sinh:");
        ngaySinhLabel.setFont(labelFont);
        employeePanel.add(ngaySinhLabel);
        String ngaySinhStr = formatDate(nhanVien.getNgaySinh());
        JTextField ngaySinhField = new JTextField(ngaySinhStr);
        ngaySinhField.setFont(fieldFont);
        ngaySinhField.setEditable(false);
        ngaySinhField.setHorizontalAlignment(JTextField.CENTER);
        ngaySinhField.setPreferredSize(new Dimension(150, 25));
        employeePanel.add(ngaySinhField);

        JLabel diaChiLabel = new JLabel("Địa chỉ:");
        diaChiLabel.setFont(labelFont);
        employeePanel.add(diaChiLabel);
        JTextField diaChiField = new JTextField(nhanVien.getDiaChi());
        diaChiField.setFont(fieldFont);
        diaChiField.setEditable(false);
        diaChiField.setHorizontalAlignment(JTextField.CENTER);
        diaChiField.setPreferredSize(new Dimension(150, 25));
        employeePanel.add(diaChiField);

        JLabel soDienThoaiLabel = new JLabel("Số điện thoại:");
        soDienThoaiLabel.setFont(labelFont);
        employeePanel.add(soDienThoaiLabel);
        JTextField soDienThoaiField = new JTextField(nhanVien.getSoDienThoai());
        soDienThoaiField.setFont(fieldFont);
        soDienThoaiField.setEditable(false);
        soDienThoaiField.setHorizontalAlignment(JTextField.CENTER);
        soDienThoaiField.setPreferredSize(new Dimension(150, 25));
        employeePanel.add(soDienThoaiField);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(labelFont);
        employeePanel.add(emailLabel);
        JTextField emailField = new JTextField(nhanVien.getEmail());
        emailField.setFont(fieldFont);
        emailField.setEditable(false);
        emailField.setHorizontalAlignment(JTextField.CENTER);
        emailField.setPreferredSize(new Dimension(150, 25));
        employeePanel.add(emailField);

        JLabel trinhDoHocVanLabel = new JLabel("Trình độ học vấn:");
        trinhDoHocVanLabel.setFont(labelFont);
        employeePanel.add(trinhDoHocVanLabel);
        JTextField trinhDoHocVanField = new JTextField(nhanVien.getTrinhDoHocVan());
        trinhDoHocVanField.setFont(fieldFont);
        trinhDoHocVanField.setEditable(false);
        trinhDoHocVanField.setHorizontalAlignment(JTextField.CENTER);
        trinhDoHocVanField.setPreferredSize(new Dimension(150, 25));
        employeePanel.add(trinhDoHocVanField);

        JLabel phongBanLabel = new JLabel("Phòng ban:");
        phongBanLabel.setFont(labelFont);
        employeePanel.add(phongBanLabel);
        PhongBanController phongBanController = new PhongBanController();
        List<String> phongbanlist = phongBanController.getPhongBanDisplayList();
        // Lấy mã phòng ban từ nhân sự
        int selectedMaPhongBan = nhanVien.getMaPhongBan();

        // Tìm chuỗi hiển thị tương ứng với mã phòng ban
        String phongBanDisplay = "";
        for (String item : phongbanlist) {
            if (item.startsWith(selectedMaPhongBan + " -")) {
                phongBanDisplay = item;
                break;
            }
        }
        JTextField phongBanField = new JTextField(phongBanDisplay);
        phongBanField.setFont(fieldFont);
        phongBanField.setEditable(false);
        phongBanField.setHorizontalAlignment(JTextField.CENTER);
        phongBanField.setPreferredSize(new Dimension(150, 25));
        employeePanel.add(phongBanField);

        JLabel chucVuLabel = new JLabel("Chức vụ:");
        chucVuLabel.setFont(labelFont);
        employeePanel.add(chucVuLabel);
        // load chuc vu tu database
        ChucVuController chucVuController = new ChucVuController();
        List<String> chucvulist = chucVuController.getChucVuDisplayList();
        // Lấy mã chức vụ từ nhân viên
        int selectedMaChucVu = nhanVien.getMaChucVu();

        // Tìm chuỗi hiển thị phù hợp từ danh sách
        String chucVuDisplay = "";
        for (String item : chucvulist) {
            if (item.startsWith(selectedMaChucVu + " -")) {
                chucVuDisplay = item;
                break;
            }
        }

        JTextField chucVuField = new JTextField(chucVuDisplay);
        chucVuField.setFont(fieldFont);
        chucVuField.setEditable(false);
        chucVuField.setHorizontalAlignment(JTextField.CENTER);
        chucVuField.setPreferredSize(new Dimension(150, 25));
        employeePanel.add(chucVuField);

        JLabel ngayVaoLamLabel = new JLabel("Ngày vào làm:");
        ngayVaoLamLabel.setFont(labelFont);
        employeePanel.add(ngayVaoLamLabel);
        String ngayVaoLamStr = formatDate(nhanVien.getNgayVaoLam());
        JTextField ngayVaoLamField = new JTextField(ngayVaoLamStr);
        ngayVaoLamField.setFont(fieldFont);
        ngayVaoLamField.setEditable(false);
        ngayVaoLamField.setHorizontalAlignment(JTextField.CENTER);
        ngayVaoLamField.setPreferredSize(new Dimension(150, 25));
        employeePanel.add(ngayVaoLamField);

        JLabel tinhTrangLabel = new JLabel("Tình trạng:");
        tinhTrangLabel.setFont(labelFont);
        employeePanel.add(tinhTrangLabel);
        JTextField tinhTrangField = new JTextField(nhanVien.getTinhTrang());
        tinhTrangField.setFont(fieldFont);
        tinhTrangField.setEditable(false);
        tinhTrangField.setHorizontalAlignment(JTextField.CENTER);
        tinhTrangField.setPreferredSize(new Dimension(150, 25));
        employeePanel.add(tinhTrangField);

        // ========== PHẦN THÔNG TIN HỢP ĐỒNG ==========
        JLabel loaiHopDongLabel = new JLabel("Loại hợp đồng:");
        loaiHopDongLabel.setFont(labelFont);
        contractPanel.add(loaiHopDongLabel);
        // Chuyển enum LoaiHopDong thành chuỗi
        String loaiHopDongStr = hopDong.getLoaiHopDong().toString();
        JTextField loaiHopDongField = new JTextField(loaiHopDongStr);
        loaiHopDongField.setFont(fieldFont);
        loaiHopDongField.setEditable(false);
        loaiHopDongField.setHorizontalAlignment(JTextField.CENTER);
        loaiHopDongField.setPreferredSize(new Dimension(150, 25));
        contractPanel.add(loaiHopDongField);

        JLabel ngayBatDauLabel = new JLabel("Ngày bắt đầu:");
        ngayBatDauLabel.setFont(labelFont);
        contractPanel.add(ngayBatDauLabel);
        String ngayBatDauStr = formatDate(hopDong.getNgayBatDau());
        JTextField ngayBatDauField = new JTextField(ngayBatDauStr);
        ngayBatDauField.setFont(fieldFont);
        ngayBatDauField.setEditable(false);
        ngayBatDauField.setHorizontalAlignment(JTextField.CENTER);
        ngayBatDauField.setPreferredSize(new Dimension(150, 25));
        contractPanel.add(ngayBatDauField);

        JLabel ngayKetThucLabel = new JLabel("Ngày kết thúc:");
        ngayKetThucLabel.setFont(labelFont);
        contractPanel.add(ngayKetThucLabel);
        String ngayKetThucStr = formatDate(hopDong.getNgayKetThuc());
        JTextField ngayKetThucField = new JTextField(ngayKetThucStr);
        ngayKetThucField.setFont(fieldFont);
        ngayKetThucField.setEditable(false);
        ngayKetThucField.setHorizontalAlignment(JTextField.CENTER);
        ngayKetThucField.setPreferredSize(new Dimension(150, 25));
        contractPanel.add(ngayKetThucField);

        JLabel ngayKyLabel = new JLabel("Ngày ký:");
        ngayKyLabel.setFont(labelFont);
        contractPanel.add(ngayKyLabel);
        String ngayKyStr = formatDate(hopDong.getNgayKy());
        JTextField ngayKyField = new JTextField(ngayKyStr);
        ngayKyField.setFont(fieldFont);
        ngayKyField.setEditable(false);
        ngayKyField.setHorizontalAlignment(JTextField.CENTER);
        ngayKyField.setPreferredSize(new Dimension(150, 25));
        contractPanel.add(ngayKyField);

        JLabel trangThaiHopDongLabel = new JLabel("Trạng thái hợp đồng:");
        trangThaiHopDongLabel.setFont(labelFont);
        contractPanel.add(trangThaiHopDongLabel);
        String trangThaiHopDong = hopDong.getTrangThai().toString();
        JTextField trangThaiHopDongField = new JTextField(trangThaiHopDong);
        trangThaiHopDongField.setFont(fieldFont);
        trangThaiHopDongField.setEditable(false);
        trangThaiHopDongField.setHorizontalAlignment(JTextField.CENTER);
        trangThaiHopDongField.setPreferredSize(new Dimension(150, 25));
        contractPanel.add(trangThaiHopDongField);

        JLabel luongCoBanLabel = new JLabel("Lương cơ bản:");
        luongCoBanLabel.setFont(labelFont);
        contractPanel.add(luongCoBanLabel);
        BigDecimal luongCoBan = hopDong.getLuongCoBan();
        String luongStr = (luongCoBan != null) ? luongCoBan.toString() : "";
        JTextField luongCoBanField = new JTextField(luongStr);
        luongCoBanField.setFont(fieldFont);
        luongCoBanField.setEditable(false);
        luongCoBanField.setHorizontalAlignment(JTextField.CENTER);
        luongCoBanField.setPreferredSize(new Dimension(150, 25));
        contractPanel.add(luongCoBanField);

        // Thêm 2 panel vào main panel
        mainPanel.add(employeePanel);
        mainPanel.add(contractPanel);

        // Thêm mainPanel vào giao diện chính
        add(mainPanel, BorderLayout.CENTER);
    }
    private String formatDate(Date date) {
        if (date == null) return "";
        return new SimpleDateFormat("dd/MM/yyyy").format(date);
    }


}
