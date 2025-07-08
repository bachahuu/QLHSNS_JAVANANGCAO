package view_nhanvien;

import controller.LuongController;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.MediaTracker;
import model.LuongModel;
import org.jdatepicker.impl.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Windows
 */
public class ProfileLuongView extends JPanel {
    private int maNhanVien;
    private UtilDateModel dateModel;
    private JDatePickerImpl datePicker;
    private JButton btnSearch;
    private JTable table;
    private DefaultTableModel tableModel;
    private LuongController luongController;

    public ProfileLuongView(int maNhanVien) {
        this.maNhanVien = maNhanVien;
        this.luongController = new LuongController();
        setLayout(new BorderLayout());
        initTopPanel();
        initTable();
        loadLuongData(null); // Load dữ liệu ban đầu không lọc
    }

    private void initTopPanel() {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        dateModel = new UtilDateModel();
        dateModel.setSelected(true);
        Properties p = new Properties();
        p.put("text.today", "Hôm nay");
        p.put("text.month", "Tháng");
        p.put("text.year", "Năm");
        JDatePanelImpl datePanel = new JDatePanelImpl(dateModel, p);
        datePicker = new JDatePickerImpl(datePanel, new DateComponentFormatter());
        datePicker.getJFormattedTextField().setFont(new Font("Segoe UI", Font.PLAIN, 14));
        topPanel.add(new JLabel("Ngày tính lương:"));
        topPanel.add(datePicker);
        btnSearch = new JButton("Tìm kiếm");
        ImageIcon icon = new ImageIcon(getClass().getResource("/images/search.png"));
        if (icon.getImageLoadStatus() == MediaTracker.COMPLETE) {
            btnSearch.setIcon(new ImageIcon(icon.getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH)));
        }
        btnSearch.addActionListener(e -> {
            Date selectedDate = (Date) datePicker.getModel().getValue();
            loadLuongData(selectedDate);
        });
        topPanel.add(btnSearch);
        add(topPanel, BorderLayout.NORTH);
    }

    private void initTable() {
        String[] columns = {
            "Mã Lương", "Ngày Tính Lương", "Số Ngày Công", "Số Giờ Tăng Ca",
            "Tiền Thưởng", "Tổng Phụ Cấp", "Tổng Khấu Trừ", "Lương Thực Nhận"
        };
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }

    // Hàm mới: Lấy toàn bộ dữ liệu lương từ cơ sở dữ liệu
    private List<LuongModel> fetchLuongDataFromDatabase() {
        List<LuongModel> list = luongController.getAllByNhanVien(maNhanVien);
        if (list.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không có dữ liệu lương hoặc lỗi kết nối cơ sở dữ liệu", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
        return list;
    }

    // Hàm đã chỉnh sửa: Lọc và hiển thị dữ liệu lên bảng
    private void loadLuongData(Date filterDate) {
        tableModel.setRowCount(0); // Xóa bảng trước khi hiển thị dữ liệu mới
        List<LuongModel> list = fetchLuongDataFromDatabase(); // Lấy dữ liệu từ database
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        for (LuongModel l : list) {
            if (filterDate != null) {
                Calendar cal1 = Calendar.getInstance();
                cal1.setTime(filterDate);
                Calendar cal2 = Calendar.getInstance();
                cal2.setTime(l.getNgayTinhLuong());
                if (cal1.get(Calendar.YEAR) != cal2.get(Calendar.YEAR) ||
                    cal1.get(Calendar.MONTH) != cal2.get(Calendar.MONTH) ||
                    cal1.get(Calendar.DAY_OF_MONTH) != cal2.get(Calendar.DAY_OF_MONTH)) {
                    continue; // Bỏ qua nếu ngày không khớp
                }
            }
            tableModel.addRow(new Object[]{
                l.getMaLuong(),
                sdf.format(l.getNgayTinhLuong()),
                l.getSoNgayCong(),
                l.getSoGioTangCa(),
                l.getTienThuong(),
                l.getTongPhuCap(),
                l.getTongKhauTru(),
                l.getLuongThucNhan()
            });
        }
    }
}