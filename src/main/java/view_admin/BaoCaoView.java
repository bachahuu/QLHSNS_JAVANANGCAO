/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view_admin;

/**
 *
 * @author Admin
 */
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import controller.BaoCaoLuongController;
import controller.nhanSuController;
import controller.TaiKhoanController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import model.NhanSuModel;
import model.LuongModel;
import model.TaiKhoanModel;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class BaoCaoView extends JPanel {
    private JTabbedPane tabbedPane;
    private JTextField searchTaiKhoanField, searchNhanSuField, searchLuongField;
    private JComboBox<String> roleFilter, statusFilter, monthFilter, yearFilter;
    private JComboBox<String> nhanSuFilter;
    private JTable taiKhoanTable, nhanSuTable, luongTable;
    private DefaultTableModel taiKhoanTableModel, nhanSuTableModel, luongTableModel;
    private TaiKhoanController taiKhoanController;
    private nhanSuController nhanSuController;
    private BaoCaoLuongController baoCaoLuongController;

    public BaoCaoView() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Khởi tạo controller với connection
        this.taiKhoanController = new TaiKhoanController();
        this.nhanSuController = new nhanSuController();
        this.baoCaoLuongController = new BaoCaoLuongController();

        // Tạo JTabbedPane
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.PLAIN, 14));

        // Tab 1: Danh sách tài khoản
        JPanel taiKhoanPanel = createTaiKhoanPanel();
        tabbedPane.addTab("Danh sách tài khoản", taiKhoanPanel);

        // Tab 2: Danh sách nhân viên
        JPanel nhanSuPanel = createNhanSuPanel();
        tabbedPane.addTab("Danh sách nhân viên", nhanSuPanel);

        // Tab 3: Danh sách lương
        JPanel luongPanel = createLuongPanel();
        tabbedPane.addTab("Danh sách lương", luongPanel);

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createTaiKhoanPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        // Top panel for search and filters
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.setBackground(Color.WHITE);

        // Search bar
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBackground(new Color(240, 240, 240));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        searchTaiKhoanField = new JTextField("Tìm kiếm tài khoản...");
        searchTaiKhoanField.setForeground(Color.GRAY);
        searchTaiKhoanField.setPreferredSize(new Dimension(200, 30));
        searchTaiKhoanField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchTaiKhoanField.getText().equals("Tìm kiếm tài khoản...")) {
                    searchTaiKhoanField.setText("");
                    searchTaiKhoanField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (searchTaiKhoanField.getText().isEmpty()) {
                    searchTaiKhoanField.setForeground(Color.GRAY);
                    searchTaiKhoanField.setText("Tìm kiếm tài khoản...");
                }
            }
        });

        URL searchUrl = getClass().getResource("/images/search.png");
        ImageIcon searchIcon = (searchUrl != null) ? resizeIcon(new ImageIcon(searchUrl), 25, 25) : new ImageIcon();
        JLabel searchLabel = new JLabel(searchIcon);
        searchLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String searchText = searchTaiKhoanField.getText().trim();
                if (searchText.equals("Tìm kiếm tài khoản...")) {
                    searchText = "";
                }
                searchTaiKhoan(searchText);
            }
        });

        searchPanel.add(searchLabel, BorderLayout.WEST);
        searchPanel.add(searchTaiKhoanField, BorderLayout.CENTER);
        topPanel.add(searchPanel, BorderLayout.WEST);

        // Filter and export button panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        filterPanel.setBackground(Color.WHITE);

        roleFilter = new JComboBox<>(new String[]{"Tất cả vai trò", "Quản trị", "Nhân viên"});
        statusFilter = new JComboBox<>(new String[]{"Tất cả trạng thái", "Hoạt động", "Bị khóa"});
        filterPanel.add(new JLabel("Vai trò:"));
        filterPanel.add(roleFilter);
        filterPanel.add(new JLabel("Trạng thái:"));
        filterPanel.add(statusFilter);

        JButton exportExcelButton = new JButton("Xuất Excel");
        exportExcelButton.setBackground(new Color(46, 204, 113));
        exportExcelButton.setForeground(Color.WHITE);
        exportExcelButton.addActionListener(e -> exportTaiKhoanToExcel());
        filterPanel.add(exportExcelButton);

        topPanel.add(filterPanel, BorderLayout.EAST);
        panel.add(topPanel, BorderLayout.NORTH);

        // Table for account data
        String[] columnNames = {"Mã TK", "Tên NV", "Tên đăng nhập", "Vai trò", "Trạng thái"};
        taiKhoanTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        taiKhoanTable = new JTable(taiKhoanTableModel);
        taiKhoanTable.setRowHeight(40);
        JScrollPane tableScrollPane = new JScrollPane(taiKhoanTable);
        panel.add(tableScrollPane, BorderLayout.CENTER);

        // Lọc dữ liệu
        roleFilter.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String selectedRole = roleFilter.getSelectedItem().toString();
                String mappedRole = mapRoleToDbValue(selectedRole);
                filterTaiKhoanByRole(mappedRole);
            }
        });
        statusFilter.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String selectedStatus = statusFilter.getSelectedItem().toString();
                String mappedStatus = mapStatusToDbValue(selectedStatus);
                filterTaiKhoanByStatus(mappedStatus);
            }
        });

        // Load dữ liệu
        loadTaiKhoanData();
        return panel;
    }

    private JPanel createNhanSuPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        // Top panel for search and filters
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.setBackground(Color.WHITE);

        // Search bar
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBackground(new Color(240, 240, 240));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        searchNhanSuField = new JTextField("Tìm kiếm nhân viên...");
        searchNhanSuField.setForeground(Color.GRAY);
        searchNhanSuField.setPreferredSize(new Dimension(200, 30));
        searchNhanSuField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchNhanSuField.getText().equals("Tìm kiếm nhân viên...")) {
                    searchNhanSuField.setText("");
                    searchNhanSuField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (searchNhanSuField.getText().isEmpty()) {
                    searchNhanSuField.setForeground(Color.GRAY);
                    searchNhanSuField.setText("Tìm kiếm nhân viên...");
                }
            }
        });

        URL searchUrl = getClass().getResource("/images/search.png");
        ImageIcon searchIcon = (searchUrl != null) ? resizeIcon(new ImageIcon(searchUrl), 25, 25) : new ImageIcon();
        JLabel searchLabel = new JLabel(searchIcon);
        searchLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String searchText = searchNhanSuField.getText().trim();
                if (searchText.equals("Tìm kiếm nhân viên...")) {
                    searchText = "";
                }
                searchNhanSu(searchText);
            }
        });

        searchPanel.add(searchLabel, BorderLayout.WEST);
        searchPanel.add(searchNhanSuField, BorderLayout.CENTER);
        topPanel.add(searchPanel, BorderLayout.WEST);

        // Filter and export button panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        filterPanel.setBackground(Color.WHITE);

        nhanSuFilter = new JComboBox<>(new String[]{"Tất cả nhân viên", "Nhân viên mới (6 tháng)"});
        filterPanel.add(new JLabel("Bộ lọc:"));
        filterPanel.add(nhanSuFilter);

        JButton exportExcelButton = new JButton("Xuất Excel");
        exportExcelButton.setBackground(new Color(46, 204, 113));
        exportExcelButton.setForeground(Color.WHITE);
        exportExcelButton.addActionListener(e -> exportNhanSuToExcel());
        filterPanel.add(exportExcelButton);

        topPanel.add(filterPanel, BorderLayout.EAST);
        panel.add(topPanel, BorderLayout.NORTH);

        // Table for employee data
        String[] columnNames = {"STT", "Mã NV", "Họ tên", "Phòng ban", "Ngày vào làm", "Tình trạng"};
        nhanSuTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        nhanSuTable = new JTable(nhanSuTableModel);
        nhanSuTable.setRowHeight(40);
        JScrollPane tableScrollPane = new JScrollPane(nhanSuTable);
        panel.add(tableScrollPane, BorderLayout.CENTER);

        // Lọc dữ liệu
        nhanSuFilter.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String selectedFilter = nhanSuFilter.getSelectedItem().toString();
                filterNhanSu(selectedFilter);
            }
        });

        // Load dữ liệu
        loadNhanSuData();
        return panel;
    }

    private JPanel createLuongPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        // Top panel for search and filters
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.setBackground(Color.WHITE);

        // Search bar
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBackground(new Color(240, 240, 240));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        searchLuongField = new JTextField("Tìm kiếm lương...");
        searchLuongField.setForeground(Color.GRAY);
        searchLuongField.setPreferredSize(new Dimension(200, 30));
        searchLuongField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchLuongField.getText().equals("Tìm kiếm lương...")) {
                    searchLuongField.setText("");
                    searchLuongField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (searchLuongField.getText().isEmpty()) {
                    searchLuongField.setForeground(Color.GRAY);
                    searchLuongField.setText("Tìm kiếm lương...");
                }
            }
        });

        URL searchUrl = getClass().getResource("/images/search.png");
        ImageIcon searchIcon = (searchUrl != null) ? resizeIcon(new ImageIcon(searchUrl), 25, 25) : new ImageIcon();
        JLabel searchLabel = new JLabel(searchIcon);
        searchLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String searchText = searchLuongField.getText().trim();
                if (searchText.equals("Tìm kiếm lương...")) {
                    searchText = "";
                }
                searchLuong(searchText);
            }
        });

        searchPanel.add(searchLabel, BorderLayout.WEST);
        searchPanel.add(searchLuongField, BorderLayout.CENTER);
        topPanel.add(searchPanel, BorderLayout.WEST);

        // Filter and export button panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        filterPanel.setBackground(Color.WHITE);

        monthFilter = new JComboBox<>();
        yearFilter = new JComboBox<>();
        populateMonthFilter();
        populateYearFilter();
        filterPanel.add(new JLabel("Tháng:"));
        filterPanel.add(monthFilter);
        filterPanel.add(new JLabel("Năm:"));
        filterPanel.add(yearFilter);

        JButton exportExcelButton = new JButton("Xuất Excel");
        exportExcelButton.setBackground(new Color(46, 204, 113));
        exportExcelButton.setForeground(Color.WHITE);
        exportExcelButton.addActionListener(e -> exportLuongToExcel());
        filterPanel.add(exportExcelButton);

        topPanel.add(filterPanel, BorderLayout.EAST);
        panel.add(topPanel, BorderLayout.NORTH);

        // Table for salary data
        String[] columnNames = {"Mã NV", "Họ tên", "Ngày tính lương", "Số ngày công", "Số giờ tăng ca", "Tiền thưởng", "Tổng phụ cấp", "Tổng khấu trừ", "Lương thực nhận"};
        luongTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        luongTable = new JTable(luongTableModel);
        luongTable.setRowHeight(40);
        JScrollPane tableScrollPane = new JScrollPane(luongTable);
        panel.add(tableScrollPane, BorderLayout.CENTER);

        // Lọc dữ liệu theo tháng và năm
        monthFilter.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String selectedMonth = monthFilter.getSelectedItem().toString();
                String selectedYear = yearFilter.getSelectedItem().toString();
                filterLuongByMonthYear(selectedMonth, selectedYear);
            }
        });
        yearFilter.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String selectedMonth = monthFilter.getSelectedItem().toString();
                String selectedYear = yearFilter.getSelectedItem().toString();
                filterLuongByMonthYear(selectedMonth, selectedYear);
            }
        });

        // Load dữ liệu mặc định
        loadLuongData();
        return panel;
    }

    private void populateMonthFilter() {
        monthFilter.addItem("Tất cả");
        for (int i = 1; i <= 12; i++) {
            monthFilter.addItem(String.format("%02d", i));
        }
    }

    private void populateYearFilter() {
        yearFilter.addItem("Tất cả");
        int currentYear = new Date().getYear() + 1900;
        for (int i = currentYear - 5; i <= currentYear + 5; i++) {
            yearFilter.addItem(String.valueOf(i));
        }
        yearFilter.setSelectedItem(String.valueOf(currentYear));
    }

    private void loadTaiKhoanData() {
        taiKhoanController = new TaiKhoanController();
        List<TaiKhoanModel> taiKhoanList = taiKhoanController.getAll();
        updateTaiKhoanTable(taiKhoanList);
    }

    private void loadNhanSuData() {
        nhanSuController = new nhanSuController();
        List<NhanSuModel> nhanSuList = nhanSuController.getAllBaoCao();
        updateNhanSuTable(nhanSuList);
    }

    private void loadLuongData() {
        List<LuongModel> luongList = baoCaoLuongController.getAll();
        updateLuongTable(luongList);
    }

    private void searchTaiKhoan(String searchText) {
        taiKhoanController = new TaiKhoanController();
        List<TaiKhoanModel> filteredList = taiKhoanController.searchByTenDangNhapOrTenNhanVien(searchText);
        updateTaiKhoanTable(filteredList);
    }

    private void searchNhanSu(String searchText) {
        nhanSuController = new nhanSuController();
        List<NhanSuModel> filteredList = nhanSuController.searchByHoTen(searchText);
        updateNhanSuTable(filteredList);
    }

    private void searchLuong(String searchText) {
        List<LuongModel> filteredList = baoCaoLuongController.searchByMaNhanVien(searchText);
        updateLuongTable(filteredList);
    }

    private void filterTaiKhoanByRole(String selectedRole) {
        taiKhoanController = new TaiKhoanController();
        List<TaiKhoanModel> filteredList = taiKhoanController.getByVaiTro(selectedRole);
        updateTaiKhoanTable(filteredList);
    }

    private void filterTaiKhoanByStatus(String selectedStatus) {
        taiKhoanController = new TaiKhoanController();
        List<TaiKhoanModel> filteredList = taiKhoanController.getByTrangThai(selectedStatus);
        updateTaiKhoanTable(filteredList);
    }

    private void filterNhanSu(String selectedFilter) {
        nhanSuController = new nhanSuController();
        List<NhanSuModel> filteredList;
        if ("Nhân viên mới (6 tháng)".equals(selectedFilter)) {
            filteredList = nhanSuController.getNhanVienMoi6Thang();
        } else {
            filteredList = nhanSuController.getAll();
        }
        updateNhanSuTable(filteredList);
    }

    private void filterLuongByMonthYear(String selectedMonth, String selectedYear) {
        int month = "Tất cả".equals(selectedMonth) ? -1 : Integer.parseInt(selectedMonth);
        int year = "Tất cả".equals(selectedYear) ? -1 : Integer.parseInt(selectedYear);
        List<LuongModel> filteredList = baoCaoLuongController.getByMonthYear(month, year);
        updateLuongTable(filteredList);
    }

    private void updateTaiKhoanTable(List<TaiKhoanModel> taiKhoanList) {
        taiKhoanTableModel.setRowCount(0);
        for (TaiKhoanModel taiKhoan : taiKhoanList) {
            Object[] row = {
                taiKhoan.getMaTaiKhoan(),
                taiKhoan.getTenNhanVien(),
                taiKhoan.getTenDangNhap(),
                mapRoleToDisplayName(taiKhoan.getVaiTro()),
                mapStatusToDisplayName(taiKhoan.getTrangThai())
            };
            taiKhoanTableModel.addRow(row);
        }
    }

    private void updateNhanSuTable(List<NhanSuModel> nhanSuList) {
        nhanSuTableModel.setRowCount(0);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        for (int i = 0; i < nhanSuList.size(); i++) {
            NhanSuModel nhanSu = nhanSuList.get(i);
            Object[] row = {
                i + 1,
                nhanSu.getMaNhanVien(),
                nhanSu.getHoTen(),
                nhanSu.getTenPhongBan() != null ? nhanSu.getTenPhongBan() : "N/A",
                nhanSu.getNgayVaoLam() != null ? dateFormat.format(nhanSu.getNgayVaoLam()) : "N/A",
                mapTinhTrangToDisplayName(nhanSu.getTinhTrang())
            };
            nhanSuTableModel.addRow(row);
        }
    }

    private void updateLuongTable(List<LuongModel> luongList) {
        luongTableModel.setRowCount(0);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        for (LuongModel luong : luongList) {
            Object[] row = {
                luong.getMaNhanVien(),
                luong.getHoTen(),
                luong.getNgayTinhLuong() != null ? dateFormat.format(luong.getNgayTinhLuong()) : "N/A",
                luong.getSoNgayCong(),
                luong.getSoGioTangCa(),
                luong.getTienThuong() != null ? luong.getTienThuong() : BigDecimal.ZERO,
                luong.getTongPhuCap() != null ? luong.getTongPhuCap() : BigDecimal.ZERO,
                luong.getTongKhauTru() != null ? luong.getTongKhauTru() : BigDecimal.ZERO,
                luong.getLuongThucNhan() != null ? luong.getLuongThucNhan() : BigDecimal.ZERO
            };
            luongTableModel.addRow(row);
        }
    }

    private String mapRoleToDbValue(String displayRole) {
        if ("Quản trị".equalsIgnoreCase(displayRole)) return "Quan_tri";
        if ("Nhân viên".equalsIgnoreCase(displayRole)) return "Nhan_vien";
        return null; // Trả về null cho "Tất cả vai trò"
    }

    private String mapStatusToDbValue(String displayStatus) {
        if ("Hoạt động".equalsIgnoreCase(displayStatus)) return "Hoat_dong";
        if ("Bị khóa".equalsIgnoreCase(displayStatus)) return "Bi_khoa";
        return null; // Trả về null cho "Tất cả trạng thái"
    }

    private String mapRoleToDisplayName(String dbRole) {
        if ("Quan_tri".equalsIgnoreCase(dbRole)) return "Quản trị";
        if ("Nhan_vien".equalsIgnoreCase(dbRole)) return "Nhân viên";
        return dbRole;
    }

    private String mapStatusToDisplayName(String dbStatus) {
        if ("Hoat_dong".equalsIgnoreCase(dbStatus)) return "Hoạt động";
        if ("Bi_khoa".equalsIgnoreCase(dbStatus)) return "Bị khóa";
        return dbStatus;
    }

    private String mapTinhTrangToDisplayName(String dbTinhTrang) {
        if ("Dang_lam".equalsIgnoreCase(dbTinhTrang)) return "Đang làm";
        if ("Da_nghi".equalsIgnoreCase(dbTinhTrang)) return "Đã nghỉ";
        return dbTinhTrang != null ? dbTinhTrang : "N/A";
    }

    private void exportTaiKhoanToExcel() {
        try {
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("DanhSachTaiKhoan");
            XSSFRow row = null;
            Cell cell = null;

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String currentDate = dateFormat.format(new Date());

            CellStyle centerStyle = workbook.createCellStyle();
            centerStyle.setAlignment(HorizontalAlignment.CENTER);

            // Tiêu đề
            row = sheet.createRow(0);
            cell = row.createCell(0, CellType.STRING);
            cell.setCellValue("DANH SÁCH TÀI KHOẢN");
            cell.setCellStyle(centerStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));

            // Thời gian xuất
            row = sheet.createRow(1);
            cell = row.createCell(0, CellType.STRING);
            cell.setCellValue("Thời gian xuất: " + currentDate);
            cell.setCellStyle(centerStyle);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 4));

            // Tiêu đề cột
            row = sheet.createRow(3);
            String[] headers = {"Mã TK", "Tên NV", "Tên đăng nhập", "Vai trò", "Trạng thái"};
            for (int i = 0; i < headers.length; i++) {
                cell = row.createCell(i, CellType.STRING);
                cell.setCellValue(headers[i]);
            }

            // Lấy dữ liệu từ bảng hiển thị
            int rowCount = taiKhoanTableModel.getRowCount();
            for (int i = 0; i < rowCount; i++) {
                row = sheet.createRow(4 + i);

                // Mã TK
                cell = row.createCell(0, CellType.NUMERIC);
                cell.setCellValue(((Number) taiKhoanTableModel.getValueAt(i, 0)).intValue());

                // Tên NV
                cell = row.createCell(1, CellType.STRING);
                cell.setCellValue(String.valueOf(taiKhoanTableModel.getValueAt(i, 1)));

                // Tên đăng nhập
                cell = row.createCell(2, CellType.STRING);
                cell.setCellValue(String.valueOf(taiKhoanTableModel.getValueAt(i, 2)));

                // Vai trò
                cell = row.createCell(3, CellType.STRING);
                cell.setCellValue(String.valueOf(taiKhoanTableModel.getValueAt(i, 3)));

                // Trạng thái
                cell = row.createCell(4, CellType.STRING);
                cell.setCellValue(String.valueOf(taiKhoanTableModel.getValueAt(i, 4)));
            }

            // Tự động điều chỉnh kích thước cột
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            saveExcelFile(workbook, "DanhSachTaiKhoan.xlsx");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi xuất Excel: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exportNhanSuToExcel() {
        try {
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("DanhSachNhanSu");
            XSSFRow row = null;
            Cell cell = null;

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String currentDate = dateFormat.format(new Date());

            CellStyle centerStyle = workbook.createCellStyle();
            centerStyle.setAlignment(HorizontalAlignment.CENTER);

            // Tiêu đề
            row = sheet.createRow(0);
            cell = row.createCell(0, CellType.STRING);
            cell.setCellValue("DANH SÁCH NHÂN VIÊN");
            cell.setCellStyle(centerStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));

            // Thời gian xuất
            row = sheet.createRow(1);
            cell = row.createCell(0, CellType.STRING);
            cell.setCellValue("Thời gian xuất: " + currentDate);
            cell.setCellStyle(centerStyle);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 5));

            // Tiêu đề cột
            row = sheet.createRow(3);
            String[] headers = {"STT", "Mã NV", "Họ tên", "Phòng ban", "Ngày vào làm", "Tình trạng"};
            for (int i = 0; i < headers.length; i++) {
                cell = row.createCell(i, CellType.STRING);
                cell.setCellValue(headers[i]);
            }

            // Lấy dữ liệu từ bảng hiển thị
            int rowCount = nhanSuTableModel.getRowCount();
            for (int i = 0; i < rowCount; i++) {
                row = sheet.createRow(4 + i);

                // STT
                cell = row.createCell(0, CellType.NUMERIC);
                cell.setCellValue(((Number) nhanSuTableModel.getValueAt(i, 0)).intValue());

                // Mã NV
                cell = row.createCell(1, CellType.NUMERIC);
                cell.setCellValue(((Number) nhanSuTableModel.getValueAt(i, 1)).intValue());

                // Họ tên
                cell = row.createCell(2, CellType.STRING);
                cell.setCellValue(String.valueOf(nhanSuTableModel.getValueAt(i, 2)));

                // Phòng ban
                cell = row.createCell(3, CellType.STRING);
                cell.setCellValue(String.valueOf(nhanSuTableModel.getValueAt(i, 3)));

                // Ngày vào làm
                cell = row.createCell(4, CellType.STRING);
                cell.setCellValue(String.valueOf(nhanSuTableModel.getValueAt(i, 4)));

                // Tình trạng
                cell = row.createCell(5, CellType.STRING);
                cell.setCellValue(String.valueOf(nhanSuTableModel.getValueAt(i, 5)));
            }

            // Tự động điều chỉnh kích thước cột
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            saveExcelFile(workbook, "DanhSachNhanSu.xlsx");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi xuất Excel: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exportLuongToExcel() {
        try {
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("DanhSachLuong");
            XSSFRow row = null;
            Cell cell = null;

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String currentDate = dateFormat.format(new Date());

            CellStyle centerStyle = workbook.createCellStyle();
            centerStyle.setAlignment(HorizontalAlignment.CENTER);

            // Tiêu đề
            row = sheet.createRow(0);
            cell = row.createCell(0, CellType.STRING);
            cell.setCellValue("DANH SÁCH LƯƠNG");
            cell.setCellStyle(centerStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 8));

            // Thời gian xuất
            row = sheet.createRow(1);
            cell = row.createCell(0, CellType.STRING);
            cell.setCellValue("Thời gian xuất: " + currentDate);
            cell.setCellStyle(centerStyle);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 8));

            // Tiêu đề cột
            row = sheet.createRow(3);
            String[] headers = {"Mã NV", "Họ tên", "Ngày tính lương", "Số ngày công", "Số giờ tăng ca", "Tiền thưởng", "Tổng phụ cấp", "Tổng khấu trừ", "Lương thực nhận"};
            for (int i = 0; i < headers.length; i++) {
                cell = row.createCell(i, CellType.STRING);
                cell.setCellValue(headers[i]);
            }

            // Lấy dữ liệu từ bảng hiển thị
            int rowCount = luongTableModel.getRowCount();
            for (int i = 0; i < rowCount; i++) {
                row = sheet.createRow(4 + i);

                // Mã NV
                cell = row.createCell(0, CellType.NUMERIC);
                cell.setCellValue(((Number) luongTableModel.getValueAt(i, 0)).intValue());

                // Họ tên
                cell = row.createCell(1, CellType.STRING);
                cell.setCellValue(String.valueOf(luongTableModel.getValueAt(i, 1)));

                // Ngày tính lương
                cell = row.createCell(2, CellType.STRING);
                cell.setCellValue(String.valueOf(luongTableModel.getValueAt(i, 2)));

                // Số ngày công
                cell = row.createCell(3, CellType.NUMERIC);
                cell.setCellValue(((Number) luongTableModel.getValueAt(i, 3)).intValue());

                // Số giờ tăng ca
                cell = row.createCell(4, CellType.NUMERIC);
                cell.setCellValue(((Number) luongTableModel.getValueAt(i, 4)).intValue());

                // Tiền thưởng
                cell = row.createCell(5, CellType.NUMERIC);
                cell.setCellValue(((Number) luongTableModel.getValueAt(i, 5)).doubleValue());

                // Tổng phụ cấp
                cell = row.createCell(6, CellType.NUMERIC);
                cell.setCellValue(((Number) luongTableModel.getValueAt(i, 6)).doubleValue());

                // Tổng khấu trừ
                cell = row.createCell(7, CellType.NUMERIC);
                cell.setCellValue(((Number) luongTableModel.getValueAt(i, 7)).doubleValue());

                // Lương thực nhận
                cell = row.createCell(8, CellType.NUMERIC);
                cell.setCellValue(((Number) luongTableModel.getValueAt(i, 8)).doubleValue());
            }

            // Tự động điều chỉnh kích thước cột
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            saveExcelFile(workbook, "DanhSachLuong.xlsx");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi xuất Excel: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveExcelFile(XSSFWorkbook workbook, String defaultFileName) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File(defaultFileName));
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".xlsx");
            }

            @Override
            public String getDescription() {
                return "Excel Files (*.xlsx)";
            }
        });

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (!file.getName().toLowerCase().endsWith(".xlsx")) {
                file = new File(file.getAbsolutePath() + ".xlsx");
            }
            try (FileOutputStream fos = new FileOutputStream(file)) {
                workbook.write(fos);
                JOptionPane.showMessageDialog(this, "Xuất Excel thành công tại " + file.getAbsolutePath(), "Thành công", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi lưu file Excel: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }

        try {
            workbook.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private ImageIcon resizeIcon(ImageIcon icon, int width, int height) {
        if (icon != null) {
            Image img = icon.getImage();
            BufferedImage resizedImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = resizedImg.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.drawImage(img, 0, 0, width, height, null);
            g2d.dispose();
            return new ImageIcon(resizedImg);
        }
        return icon;
    }
}