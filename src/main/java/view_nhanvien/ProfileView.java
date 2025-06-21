/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view_nhanvien;

import javax.swing.JPanel;
import javax.swing.*;
import java.awt.*;
/**
 *
 * @author Windows
 */
public class ProfileView extends JPanel {
    public ProfileView() {
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
        JTextField maNhanVienField = new JTextField("NV002");
        maNhanVienField.setFont(fieldFont);
        maNhanVienField.setEditable(false);
        maNhanVienField.setHorizontalAlignment(JTextField.CENTER);
        maNhanVienField.setPreferredSize(new Dimension(150, 25));
        employeePanel.add(maNhanVienField);

        JLabel hoVaTenLabel = new JLabel("Họ và tên:");
        hoVaTenLabel.setFont(labelFont);
        employeePanel.add(hoVaTenLabel);
        JTextField hoVaTenField = new JTextField("Hà Hữu Nam");
        hoVaTenField.setFont(fieldFont);
        hoVaTenField.setEditable(false);
        hoVaTenField.setHorizontalAlignment(JTextField.CENTER);
        hoVaTenField.setPreferredSize(new Dimension(150, 25));
        employeePanel.add(hoVaTenField);

        JLabel gioiTinhLabel = new JLabel("Giới tính:");
        gioiTinhLabel.setFont(labelFont);
        employeePanel.add(gioiTinhLabel);
        JTextField gioiTinhField = new JTextField("Nam");
        gioiTinhField.setFont(fieldFont);
        gioiTinhField.setEditable(false);
        gioiTinhField.setHorizontalAlignment(JTextField.CENTER);
        gioiTinhField.setPreferredSize(new Dimension(150, 25));
        employeePanel.add(gioiTinhField);

        JLabel ngaySinhLabel = new JLabel("Ngày sinh:");
        ngaySinhLabel.setFont(labelFont);
        employeePanel.add(ngaySinhLabel);
        JTextField ngaySinhField = new JTextField("Jun 14, 2025");
        ngaySinhField.setFont(fieldFont);
        ngaySinhField.setEditable(false);
        ngaySinhField.setHorizontalAlignment(JTextField.CENTER);
        ngaySinhField.setPreferredSize(new Dimension(150, 25));
        employeePanel.add(ngaySinhField);

        JLabel diaChiLabel = new JLabel("Địa chỉ:");
        diaChiLabel.setFont(labelFont);
        employeePanel.add(diaChiLabel);
        JTextField diaChiField = new JTextField("Thái Bình");
        diaChiField.setFont(fieldFont);
        diaChiField.setEditable(false);
        diaChiField.setHorizontalAlignment(JTextField.CENTER);
        diaChiField.setPreferredSize(new Dimension(150, 25));
        employeePanel.add(diaChiField);

        JLabel soDienThoaiLabel = new JLabel("Số điện thoại:");
        soDienThoaiLabel.setFont(labelFont);
        employeePanel.add(soDienThoaiLabel);
        JTextField soDienThoaiField = new JTextField("0964092003");
        soDienThoaiField.setFont(fieldFont);
        soDienThoaiField.setEditable(false);
        soDienThoaiField.setHorizontalAlignment(JTextField.CENTER);
        soDienThoaiField.setPreferredSize(new Dimension(150, 25));
        employeePanel.add(soDienThoaiField);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(labelFont);
        employeePanel.add(emailLabel);
        JTextField emailField = new JTextField("nam_dang_lam");
        emailField.setFont(fieldFont);
        emailField.setEditable(false);
        emailField.setHorizontalAlignment(JTextField.CENTER);
        emailField.setPreferredSize(new Dimension(150, 25));
        employeePanel.add(emailField);

        JLabel trinhDoHocVanLabel = new JLabel("Trình độ học vấn:");
        trinhDoHocVanLabel.setFont(labelFont);
        employeePanel.add(trinhDoHocVanLabel);
        JTextField trinhDoHocVanField = new JTextField("Cao đẳng");
        trinhDoHocVanField.setFont(fieldFont);
        trinhDoHocVanField.setEditable(false);
        trinhDoHocVanField.setHorizontalAlignment(JTextField.CENTER);
        trinhDoHocVanField.setPreferredSize(new Dimension(150, 25));
        employeePanel.add(trinhDoHocVanField);

        JLabel phongBanLabel = new JLabel("Phòng ban:");
        phongBanLabel.setFont(labelFont);
        employeePanel.add(phongBanLabel);
        JTextField phongBanField = new JTextField("1 - Chủ Tịch");
        phongBanField.setFont(fieldFont);
        phongBanField.setEditable(false);
        phongBanField.setHorizontalAlignment(JTextField.CENTER);
        phongBanField.setPreferredSize(new Dimension(150, 25));
        employeePanel.add(phongBanField);

        JLabel chucVuLabel = new JLabel("Chức vụ:");
        chucVuLabel.setFont(labelFont);
        employeePanel.add(chucVuLabel);
        JTextField chucVuField = new JTextField("1 - Chủ Tịch hội đồng quản trị");
        chucVuField.setFont(fieldFont);
        chucVuField.setEditable(false);
        chucVuField.setHorizontalAlignment(JTextField.CENTER);
        chucVuField.setPreferredSize(new Dimension(150, 25));
        employeePanel.add(chucVuField);

        JLabel ngayVaoLamLabel = new JLabel("Ngày vào làm:");
        ngayVaoLamLabel.setFont(labelFont);
        employeePanel.add(ngayVaoLamLabel);
        JTextField ngayVaoLamField = new JTextField("Jun 14, 2025");
        ngayVaoLamField.setFont(fieldFont);
        ngayVaoLamField.setEditable(false);
        ngayVaoLamField.setHorizontalAlignment(JTextField.CENTER);
        ngayVaoLamField.setPreferredSize(new Dimension(150, 25));
        employeePanel.add(ngayVaoLamField);

        JLabel tinhTrangLabel = new JLabel("Tình trạng:");
        tinhTrangLabel.setFont(labelFont);
        employeePanel.add(tinhTrangLabel);
        JTextField tinhTrangField = new JTextField("Đang làm");
        tinhTrangField.setFont(fieldFont);
        tinhTrangField.setEditable(false);
        tinhTrangField.setHorizontalAlignment(JTextField.CENTER);
        tinhTrangField.setPreferredSize(new Dimension(150, 25));
        employeePanel.add(tinhTrangField);

        // ========== PHẦN THÔNG TIN HỢP ĐỒNG ==========
        JLabel loaiHopDongLabel = new JLabel("Loại hợp đồng:");
        loaiHopDongLabel.setFont(labelFont);
        contractPanel.add(loaiHopDongLabel);
        JTextField loaiHopDongField = new JTextField("Thử việc");
        loaiHopDongField.setFont(fieldFont);
        loaiHopDongField.setEditable(false);
        loaiHopDongField.setHorizontalAlignment(JTextField.CENTER);
        loaiHopDongField.setPreferredSize(new Dimension(150, 25));
        contractPanel.add(loaiHopDongField);

        JLabel ngayBatDauLabel = new JLabel("Ngày bắt đầu:");
        ngayBatDauLabel.setFont(labelFont);
        contractPanel.add(ngayBatDauLabel);
        JTextField ngayBatDauField = new JTextField("Jun 14, 2025");
        ngayBatDauField.setFont(fieldFont);
        ngayBatDauField.setEditable(false);
        ngayBatDauField.setHorizontalAlignment(JTextField.CENTER);
        ngayBatDauField.setPreferredSize(new Dimension(150, 25));
        contractPanel.add(ngayBatDauField);

        JLabel ngayKetThucLabel = new JLabel("Ngày kết thúc:");
        ngayKetThucLabel.setFont(labelFont);
        contractPanel.add(ngayKetThucLabel);
        JTextField ngayKetThucField = new JTextField("Jun 14, 2025");
        ngayKetThucField.setFont(fieldFont);
        ngayKetThucField.setEditable(false);
        ngayKetThucField.setHorizontalAlignment(JTextField.CENTER);
        ngayKetThucField.setPreferredSize(new Dimension(150, 25));
        contractPanel.add(ngayKetThucField);

        JLabel ngayKyLabel = new JLabel("Ngày ký:");
        ngayKyLabel.setFont(labelFont);
        contractPanel.add(ngayKyLabel);
        JTextField ngayKyField = new JTextField("Jun 14, 2025");
        ngayKyField.setFont(fieldFont);
        ngayKyField.setEditable(false);
        ngayKyField.setHorizontalAlignment(JTextField.CENTER);
        ngayKyField.setPreferredSize(new Dimension(150, 25));
        contractPanel.add(ngayKyField);

        JLabel trangThaiHopDongLabel = new JLabel("Trạng thái hợp đồng:");
        trangThaiHopDongLabel.setFont(labelFont);
        contractPanel.add(trangThaiHopDongLabel);
        JTextField trangThaiHopDongField = new JTextField("Còn hiệu lực");
        trangThaiHopDongField.setFont(fieldFont);
        trangThaiHopDongField.setEditable(false);
        trangThaiHopDongField.setHorizontalAlignment(JTextField.CENTER);
        trangThaiHopDongField.setPreferredSize(new Dimension(150, 25));
        contractPanel.add(trangThaiHopDongField);

        JLabel luongCoBanLabel = new JLabel("Lương cơ bản:");
        luongCoBanLabel.setFont(labelFont);
        contractPanel.add(luongCoBanLabel);
        JTextField luongCoBanField = new JTextField("10,000,000 VNĐ");
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

}
