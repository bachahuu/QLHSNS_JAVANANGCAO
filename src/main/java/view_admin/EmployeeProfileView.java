/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view_admin;

import controller.ChucVuController;
import controller.PhongBanController;
import controller.nhanSuController;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import org.jdatepicker.impl.*; 
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import model.ChucVuModel;
import model.ContractModel;
import model.NhanSuModel;
import model.PhongBanModel;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.SqlDateModel;

/**
 * @author Windows
 */
public class EmployeeProfileView extends JPanel {
    private JTextField searchField;
    private JComboBox<String> departmentFilter;
    private JComboBox<String> positionFilter;
    private JComboBox<String> statusFilter;
    private JTable employeeTable;
    private DefaultTableModel tableModel;
    private JButton addButton;
    private nhanSuController nhansu;
    private NhanSuModel selectedNhanSu;

    public EmployeeProfileView() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Top panel for search and filters
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.setBackground(Color.WHITE);

        // Search bar
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBackground(new Color(240, 240, 240));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Tạo JTextField với placeholder
        searchField = new JTextField();
        searchField.setForeground(Color.GRAY);
        searchField.setText("Tìm kiếm nhân sự..."); // Đặt placeholder làm giá trị mặc định
        searchField.setPreferredSize(new Dimension(200, 30));
        // Xử lý placeholder cho searchField
        searchField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals("Tìm kiếm nhân sự...")) {
                    searchField.setText(""); // Xóa placeholder khi focus
                    searchField.setForeground(Color.BLACK); // Đổi màu chữ thành đen khi nhập
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setForeground(Color.GRAY); // Đổi màu chữ thành xám khi rỗng
                    searchField.setText("Tìm kiếm nhân sự..."); // Khôi phục placeholder
                }
            }
        });
        // Load search icon
        URL searchUrl = getClass().getResource("/images/search.png");
        if (searchUrl == null) {
            System.err.println("Không tìm thấy icon: /images/search.png");
        }
        ImageIcon searchIcon = (searchUrl != null) ? resizeIcon(new ImageIcon(searchUrl), 25, 25) : new ImageIcon();
        JLabel searchLabel = new JLabel(searchIcon);
        
        searchLabel.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                String searchText = searchField.getText().trim();
                if (searchText.equals("Tìm kiếm nhân sự...")) {
                    searchText = ""; // Xóa placeholder nếu người dùng nhấn tìm mà không nhập
                }
                searchNhanSu(searchText);
            }
        });

        // Thêm biểu tượng vào bên trái của JTextField
        searchField.setBorder(BorderFactory.createCompoundBorder(
            searchField.getBorder(),
            BorderFactory.createEmptyBorder(0, 25, 0, 0) // Tạo khoảng cách bên trái để chứa icon
        ));
        searchPanel.add(searchLabel, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);

        topPanel.add(searchPanel, BorderLayout.WEST);

        // Filter and add button panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        filterPanel.setBackground(Color.WHITE);

//        departmentFilter = new JComboBox<>(new String[]{"Tất cả phòng ban", "Ban Lãnh đạo", "Phòng Kinh doanh", "Phòng Nhân sự"});
//        positionFilter = new JComboBox<>(new String[]{"Tất cả chức vụ", "Giám đốc", "Nhân viên", "Trưởng phòng"});
        statusFilter = new JComboBox<>(new String[]{"Tất cả trạng thái", "Đang làm việc", "Đã nghỉ việc"});

//        filterPanel.add(departmentFilter);
//        filterPanel.add(positionFilter);
        filterPanel.add(statusFilter);

        addButton = new JButton("Thêm nhân sự");
        addButton.setBackground(new Color(103, 65, 217));
        addButton.setForeground(Color.WHITE);
        filterPanel.add(addButton);
        
        JButton exportExcelButton = new JButton("Xuất Excel");
        exportExcelButton.setBackground(new Color(46, 204, 113)); // Màu xanh lá cho nổi bật
        exportExcelButton.setForeground(Color.WHITE);
        // Thêm biểu tượng
        URL excelUrl = getClass().getResource("/images/export.png");
        ImageIcon excelIcon = (excelUrl != null) ? resizeIcon(new ImageIcon(excelUrl), 20, 20) : new ImageIcon();
        exportExcelButton.setIcon(excelIcon);
        exportExcelButton.addActionListener(e -> {
            exportExcelButton.setEnabled(false); // Vô hiệu hóa nút
            try {
                exportToExcel();
            } finally {
                exportExcelButton.setEnabled(true); // Kích hoạt lại nút
            }
        });
        filterPanel.add(exportExcelButton);

        topPanel.add(filterPanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // Table for employee data
        String[] columnNames = {"Mã số NV", "Họ và tên","Giới Tính", "Ngày Sinh", "Địa Chỉ", "Thông tin liên hệ", "Trạng thái", "Thao tác"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7; // Chỉ cột "Thao tác" có thể chỉnh sửa (các nút)
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 7) return JPanel.class; // For action buttons
                return String.class;
            }
            @Override
            public void setValueAt(Object aValue, int row, int column) {
                if (column != 7 && row < getRowCount()) { // Bỏ qua cột "Thao tác" và kiểm tra hàng hợp lệ
                    super.setValueAt(aValue, row, column);
                }
            }
        };
        
        //lọc dữ liệu theo trạng thái
        statusFilter.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    String selectedStatus = statusFilter.getSelectedItem().toString();
                    filterNhanSuByStatus(selectedStatus);
                }
            }            
        });
        //fill dữ liệu từ database
        loadDataFromDatabase();
       
        // Sample data (replace with actual data in a real application)
        setupAddButtonListener();
        
  
        
     

        employeeTable = new JTable(tableModel);
        employeeTable.setRowHeight(50);
        employeeTable.getColumnModel().getColumn(1).setPreferredWidth(50); // Avatar column width
        employeeTable.getColumnModel().getColumn(7).setPreferredWidth(120); // Actions column width

        // Áp dụng renderer và editor cho cột "Thao tác"
        employeeTable.getColumnModel().getColumn(7).setCellRenderer(new ButtonRenderer());
        employeeTable.getColumnModel().getColumn(7).setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane tableScrollPane = new JScrollPane(employeeTable);
        add(tableScrollPane, BorderLayout.CENTER);

        
    }
    
    private void loadDataFromDatabase(){
       nhansu = new nhanSuController();
       List<NhanSuModel> nhanSuList = nhansu.getAll();
         for (NhanSuModel nhanSu : nhanSuList) {
            Object[] row = {
                nhanSu.getMaSo(),
                nhanSu.getHoTen(),
                nhanSu.getGioiTinh(),
                nhanSu.getNgaySinh() != null ? nhanSu.getNgaySinh().toString() : "",
                nhanSu.getDiaChi(),
                "SDT: " + nhanSu.getSoDienThoai() + "\nEmail: " + nhanSu.getEmail(),
                nhanSu.getTinhTrang(),
                null
            };
            tableModel.addRow(row);
        }
    }
    
    // Method để refresh lại dữ liệu bảng
    private void refreshTableData() {
        // Xóa tất cả dữ liệu hiện tại
        tableModel.setRowCount(0);
        
        // Load lại dữ liệu từ database
        loadDataFromDatabase();
    }
    
    private void searchNhanSu(String searchText) {
        nhansu = new nhanSuController();
        List<NhanSuModel> filteredList = nhansu.searchByHoTen(searchText);

        // Cập nhật bảng với danh sách đã lọc
        tableModel.setRowCount(0);
        for (NhanSuModel nhanSu : filteredList) {
            Object[] row = {
                nhanSu.getMaSo(),
                nhanSu.getHoTen(),
                nhanSu.getGioiTinh(),
                nhanSu.getNgaySinh() != null ? nhanSu.getNgaySinh().toString() : "",
                nhanSu.getDiaChi(),
                "SDT: " + nhanSu.getSoDienThoai() + "\nEmail: " + nhanSu.getEmail(),
                nhanSu.getTinhTrang(),
                createButtonPanel(false, null, null, null)
            };
            tableModel.addRow(row);
        }
    }
    
    private void filterNhanSuByStatus(String selectedStatus){
        nhansu = new nhanSuController();
        List<NhanSuModel> filteredList = nhansu.getByTinhTrang(selectedStatus);
        // Cập nhật bảng với danh sách đã lọc
        tableModel.setRowCount(0);
        for (NhanSuModel nhanSu : filteredList) {
            Object[] row = {
                nhanSu.getMaSo(),
                nhanSu.getHoTen(),
                nhanSu.getGioiTinh(),
                nhanSu.getNgaySinh() != null ? nhanSu.getNgaySinh().toString() : "",
                nhanSu.getDiaChi(),
                "SDT: " + nhanSu.getSoDienThoai() + "\nEmail: " + nhanSu.getEmail(),
                nhanSu.getTinhTrang(),
                createButtonPanel(false, null, null, null) // Thêm panel nút cho cột "Thao tác"
            };
            tableModel.addRow(row);
        }
    }
    private void setupAddButtonListener() {
        addButton.addActionListener(e -> {
            insertDetailDialog();
        });
    }
    
    // Phương thức chung để tạo panel chứa các nút
    private JPanel createButtonPanel(boolean withListeners, ActionListener viewListener, ActionListener editListener, ActionListener deleteListener) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        panel.setOpaque(true);

// Nút View (xanh lam sáng)
        URL viewUrl = getClass().getResource("/images/eye.png");
        if (viewUrl == null) {
            System.err.println("Không tìm thấy icon: /images/eye.png");
        }
        ImageIcon viewIcon = (viewUrl != null) ? resizeIcon(new ImageIcon(viewUrl), 20, 20) : new ImageIcon();
        JButton viewButton = new JButton(viewIcon);
//        viewButton.setBackground(new Color(100, 181, 246)); // Xanh lam sáng
        viewButton.setForeground(Color.WHITE);
        viewButton.setPreferredSize(new Dimension(30, 30));

        // Nút Edit (xanh lá sáng)
        URL editUrl = getClass().getResource("/images/edit.png");
        if (editUrl == null) {
            System.err.println("Không tìm thấy icon: /images/edit.png");
        }
        ImageIcon editIcon = (editUrl != null) ? resizeIcon(new ImageIcon(editUrl), 20, 20) : new ImageIcon();
        JButton editButton = new JButton(editIcon);
//        editButton.setBackground(new Color(105, 240, 174)); // Xanh lá sáng
        editButton.setForeground(Color.WHITE);
        editButton.setPreferredSize(new Dimension(30, 30));

        // Nút Delete (đỏ nhạt sáng)
        URL deleteUrl = getClass().getResource("/images/delete.png");
        if (deleteUrl == null) {
            System.err.println("Không tìm thấy icon: /images/delete.png");
        }
        ImageIcon deleteIcon = (deleteUrl != null) ? resizeIcon(new ImageIcon(deleteUrl), 20, 20) : new ImageIcon();
        JButton deleteButton = new JButton(deleteIcon);
//        deleteButton.setBackground(new Color(255, 138, 128)); // Đỏ nhạt sáng
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setPreferredSize(new Dimension(30, 30));

        // Gắn sự kiện nếu cần (chỉ cho editor)
        if (withListeners) {
            viewButton.addActionListener(viewListener);
            editButton.addActionListener(editListener);
            deleteButton.addActionListener(deleteListener);
        }

        panel.add(viewButton);
        panel.add(editButton);
        panel.add(deleteButton);
        return panel;
    }
    // Phương thức để thay đổi kích thước icon
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
        return icon; // Trả về icon gốc nếu không thể resize
    }
   private void insertDetailDialog() {
        // Tạo dialog
        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(EmployeeProfileView.this), "Thêm nhân sự mới", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(1000, 700); // Tăng kích thước để chứa 2 cột
        dialog.setLocationRelativeTo(EmployeeProfileView.this);

        // Tạo panel chính chia làm 2 cột
        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Panel thông tin nhân viên (bên trái)
        JPanel employeePanel = new JPanel(new GridLayout(12, 2, 10, 10));
        employeePanel.setBorder(BorderFactory.createTitledBorder("Thông tin nhân viên"));

        // Panel thông tin hợp đồng (bên phải)
        JPanel contractPanel = new JPanel(new GridBagLayout());
        contractPanel.setBorder(BorderFactory.createTitledBorder("Thông tin hợp đồng"));

        // Font chữ
        Font labelFont = new Font("Arial", Font.PLAIN, 14);
        Font fieldFont = new Font("Arial", Font.PLAIN, 14);

       

        // ========== PHẦN THÔNG TIN NHÂN VIÊN ==========
        // Mã nhân viên
        JLabel maNhanVienLabel = new JLabel("Mã nhân viên:");
        maNhanVienLabel.setFont(labelFont);
        employeePanel.add(maNhanVienLabel);
        JTextField maNhanVienField = new JTextField();
        maNhanVienField.setFont(fieldFont);
        employeePanel.add(maNhanVienField);

        // Họ tên
        JLabel hoTenLabel = new JLabel("Họ và tên:");
        hoTenLabel.setFont(labelFont);
        employeePanel.add(hoTenLabel);
        JTextField hoTenField = new JTextField();
        hoTenField.setFont(fieldFont);
        employeePanel.add(hoTenField);

        // Giới tính
        JLabel gioiTinhLabel = new JLabel("Giới tính:");
        gioiTinhLabel.setFont(labelFont);
        employeePanel.add(gioiTinhLabel);
        String[] gioiTinhOptions = {"Nam", "Nu"};
        JComboBox<String> gioiTinhComboBox = new JComboBox<>(gioiTinhOptions);
        gioiTinhComboBox.setFont(fieldFont);
        employeePanel.add(gioiTinhComboBox);

        // Ngày sinh
        JLabel ngaySinhLabel = new JLabel("Ngày sinh:");
        ngaySinhLabel.setFont(labelFont);
        employeePanel.add(ngaySinhLabel);
         // Tạo model cho JDatePicker (mặc định là ngày hôm nay)
        UtilDateModel model = new UtilDateModel();
        model.setSelected(true);
        Properties p = new Properties();
        p.put("text.today", "Hôm nay");
        p.put("text.month", "Tháng");
        p.put("text.year", "Năm");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        JDatePickerImpl ngaySinhPicker = new JDatePickerImpl(datePanel, new DateComponentFormatter());
        ngaySinhPicker.getJFormattedTextField().setFont(fieldFont);
        employeePanel.add(ngaySinhPicker);

        // Địa chỉ
        JLabel diaChiLabel = new JLabel("Địa chỉ:");
        diaChiLabel.setFont(labelFont);
        employeePanel.add(diaChiLabel);
        JTextField diaChiField = new JTextField();
        diaChiField.setFont(fieldFont);
        employeePanel.add(diaChiField);

        // Số điện thoại
        JLabel soDienThoaiLabel = new JLabel("Số điện thoại:");
        soDienThoaiLabel.setFont(labelFont);
        employeePanel.add(soDienThoaiLabel);
        JTextField soDienThoaiField = new JTextField();
        soDienThoaiField.setFont(fieldFont);
        employeePanel.add(soDienThoaiField);

        // Email
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(labelFont);
        employeePanel.add(emailLabel);
        JTextField emailField = new JTextField();
        emailField.setFont(fieldFont);
        employeePanel.add(emailField);

        // Trình độ học vấn
        JLabel trinhDoHocVanLabel = new JLabel("Trình độ học vấn:");
        trinhDoHocVanLabel.setFont(labelFont);
        employeePanel.add(trinhDoHocVanLabel);
        String[] trinhDoOptions = {"Cao_dang", "Dai_hoc", "Tot_nghiep_cap_3"};
        JComboBox<String> trinhDoHocVanComboBox = new JComboBox<>(trinhDoOptions);
        trinhDoHocVanComboBox.setFont(fieldFont);
        employeePanel.add(trinhDoHocVanComboBox);

        // Phòng ban
        JLabel maPhongBanLabel = new JLabel("Phòng ban:");
        maPhongBanLabel.setFont(labelFont);
        employeePanel.add(maPhongBanLabel);
        JComboBox<String> maPhongBanComboBox = new JComboBox<>();
        maPhongBanComboBox.setFont(fieldFont);
        PhongBanController phongBanController = new PhongBanController();
        List<String> phongbanlist = phongBanController.getPhongBanDisplayList();
        if (phongbanlist.isEmpty()) {
            maPhongBanComboBox.addItem("Không có phòng ban");
            maPhongBanComboBox.setEnabled(false);
        } else {
            for (String string : phongbanlist) {
                maPhongBanComboBox.addItem(string);
            }
        }
        employeePanel.add(maPhongBanComboBox);

        // Chức vụ
        JLabel maChucVuLabel = new JLabel("Chức vụ:");
        maChucVuLabel.setFont(labelFont);
        employeePanel.add(maChucVuLabel);
        JComboBox<String> maChucVuComboBox = new JComboBox<>();
        maChucVuComboBox.setFont(fieldFont);
        ChucVuController chucVuController = new ChucVuController();
        List<String> chucvulist = chucVuController.getChucVuDisplayList();
        if (chucvulist.isEmpty()) {
            maChucVuComboBox.addItem("Không có chức vụ");
            maChucVuComboBox.setEnabled(false);
        } else {
            for (String string : chucvulist) {
                maChucVuComboBox.addItem(string);
            }
        }
        employeePanel.add(maChucVuComboBox);

        // Ngày vào làm
        JLabel ngayVaoLamLabel = new JLabel("Ngày vào làm:");
        ngayVaoLamLabel.setFont(labelFont);
        employeePanel.add(ngayVaoLamLabel);
        // Tạo model cho JDatePicker (mặc định là ngày hôm nay)
        UtilDateModel model_ngayVaoLam = new UtilDateModel();
        model_ngayVaoLam.setSelected(true);
        Properties p_ngayVaoLam = new Properties();
        p_ngayVaoLam.put("text.today", "Hôm nay");
        p_ngayVaoLam.put("text.month", "Tháng");
        p_ngayVaoLam.put("text.year", "Năm");
        JDatePanelImpl datePanel_ngayVaoLam = new JDatePanelImpl(model_ngayVaoLam, p_ngayVaoLam);
        JDatePickerImpl ngayVaoLamPicker = new JDatePickerImpl(datePanel_ngayVaoLam, new DateComponentFormatter());
        ngayVaoLamPicker.getJFormattedTextField().setFont(fieldFont);
        employeePanel.add(ngayVaoLamPicker);

        // Tình trạng
        JLabel tinhTrangLabel = new JLabel("Tình trạng:");
        tinhTrangLabel.setFont(labelFont);
        employeePanel.add(tinhTrangLabel);
        String[] tinhTrangOptions = {"Dang_lam", "Da_nghi"};
        JComboBox<String> tinhTrangComboBox = new JComboBox<>(tinhTrangOptions);
        tinhTrangComboBox.setFont(fieldFont);
        tinhTrangComboBox.setSelectedItem("Dang_lam");
        employeePanel.add(tinhTrangComboBox);

        // ========== PHẦN THÔNG TIN HỢP ĐỒNG ==========
        // Loại hợp đồng
       
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Padding giữa các thành phần
        gbc.anchor = GridBagConstraints.WEST;

        // Loại hợp đồng
        JLabel loaiHopDongLabel = new JLabel("Loại hợp đồng:");
        loaiHopDongLabel.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 0;
        contractPanel.add(loaiHopDongLabel, gbc);

        String[] loaiHopDongOptions = {"Thu_viec", "Chinh_thuc", "Thoi_vu"};
        JComboBox<String> loaiHopDongComboBox = new JComboBox<>(loaiHopDongOptions);
        loaiHopDongComboBox.setFont(fieldFont);
        loaiHopDongComboBox.setPreferredSize(new Dimension(150, 25)); // Giới hạn kích thước
        gbc.gridx = 1;
        contractPanel.add(loaiHopDongComboBox, gbc);

        // Ngày bắt đầu
        JLabel ngayBatDauLabel = new JLabel("Ngày bắt đầu:");
        ngayBatDauLabel.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 1;
        contractPanel.add(ngayBatDauLabel, gbc);
        // Tạo model cho JDatePicker (mặc định là ngày hôm nay)
        UtilDateModel model_ngayBatDau = new UtilDateModel();
        model_ngayBatDau.setSelected(true);
        Properties p_ngayBatDau = new Properties();
        p_ngayBatDau.put("text.today", "Hôm nay");
        p_ngayBatDau.put("text.month", "Tháng");
        p_ngayBatDau.put("text.year", "Năm");
        JDatePanelImpl datePanel_ngayBatDau = new JDatePanelImpl(model_ngayBatDau, p_ngayBatDau);
        JDatePickerImpl ngayBatDauPicker = new JDatePickerImpl(datePanel_ngayBatDau, new DateComponentFormatter());
        ngayBatDauPicker.getJFormattedTextField().setFont(fieldFont);
        ngayBatDauPicker.setPreferredSize(new Dimension(150, 25)); // Giới hạn kích thước
        gbc.gridx = 1;
        contractPanel.add(ngayBatDauPicker, gbc);

        // Ngày kết thúc
        JLabel ngayKetThucLabel = new JLabel("Ngày kết thúc:");
        ngayKetThucLabel.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 2;
        contractPanel.add(ngayKetThucLabel, gbc);
        
        // Tạo model cho JDatePicker (mặc định là ngày hôm nay)
        UtilDateModel model_ngayKetThuc = new UtilDateModel();
        model_ngayKetThuc.setSelected(true);
        Properties p_ngayKetThuc = new Properties();
        p_ngayKetThuc.put("text.today", "Hôm nay");
        p_ngayKetThuc.put("text.month", "Tháng");
        p_ngayKetThuc.put("text.year", "Năm");
        JDatePanelImpl datePanel_ngayKetThuc = new JDatePanelImpl(model_ngayKetThuc, p_ngayKetThuc);
        JDatePickerImpl ngayKetThucPicker = new JDatePickerImpl(datePanel_ngayKetThuc, new DateComponentFormatter());
        ngayKetThucPicker.getJFormattedTextField().setFont(fieldFont);
        ngayKetThucPicker.setPreferredSize(new Dimension(150, 25)); // Giới hạn kích thước
        gbc.gridx = 1;
        contractPanel.add(ngayKetThucPicker, gbc);

        // Ngày ký
        JLabel ngayKyLabel = new JLabel("Ngày ký:");
        ngayKyLabel.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 3;
        contractPanel.add(ngayKyLabel, gbc);
        
        // Tạo model cho JDatePicker (mặc định là ngày hôm nay)
        UtilDateModel model_ngayKy = new UtilDateModel();
        model_ngayKy.setSelected(true);
        Properties p_ngayKy = new Properties();
        p_ngayKy.put("text.today", "Hôm nay");
        p_ngayKy.put("text.month", "Tháng");
        p_ngayKy.put("text.year", "Năm");
        JDatePanelImpl datePanel_ngayKy = new JDatePanelImpl(model_ngayKy, p_ngayKy);
        JDatePickerImpl ngayKyPicker = new JDatePickerImpl(datePanel_ngayKy, new DateComponentFormatter());
        ngayKyPicker.getJFormattedTextField().setFont(fieldFont);
        ngayKyPicker.setPreferredSize(new Dimension(150, 25)); // Giới hạn kích thước
        gbc.gridx = 1;
        contractPanel.add(ngayKyPicker, gbc);

        // Trạng thái hợp đồng
        JLabel trangThaiHopDongLabel = new JLabel("Trạng thái hợp đồng:");
        trangThaiHopDongLabel.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 4;
        contractPanel.add(trangThaiHopDongLabel, gbc);

        String[] trangThaiHopDongOptions = {"Con_hieu_luc", "Het_hieu_luc"};
        JComboBox<String> trangThaiHopDongComboBox = new JComboBox<>(trangThaiHopDongOptions);
        trangThaiHopDongComboBox.setFont(fieldFont);
        trangThaiHopDongComboBox.setPreferredSize(new Dimension(150, 25)); // Giới hạn kích thước
        trangThaiHopDongComboBox.setSelectedItem("Con_hieu_luc");
        gbc.gridx = 1;
        contractPanel.add(trangThaiHopDongComboBox, gbc);

        // Lương cơ bản
        JLabel luongCoBanLabel = new JLabel("Lương cơ bản:");
        luongCoBanLabel.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 5;
        contractPanel.add(luongCoBanLabel, gbc);

        JTextField luongCoBanField = new JTextField();
        luongCoBanField.setFont(fieldFont);
        luongCoBanField.setPreferredSize(new Dimension(150, 25)); // Giới hạn kích thước
        gbc.gridx = 1;
        contractPanel.add(luongCoBanField, gbc);

        // Thêm khoảng trống để tránh tràn layout (nếu cần)
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weighty = 1.0; // Đẩy các thành phần lên trên, chừa khoảng trống phía dưới
        contractPanel.add(new JLabel(), gbc);

        // Thêm 2 panel vào main panel
        mainPanel.add(employeePanel);
        mainPanel.add(contractPanel);

        // Panel chứa các nút button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        // Nút Lưu
        JButton saveButton = new JButton("Lưu thay đổi");
        saveButton.setFont(new Font("Arial", Font.BOLD, 14));
        saveButton.setBackground(new Color(0, 153, 0));
        saveButton.setForeground(Color.WHITE);
        saveButton.addActionListener(e -> {
            // Xử lý lưu dữ liệu ở đây
            try {
                // Lấy dữ liệu từ các field
                String maSo = maNhanVienField.getText().trim();
                String hoTen = hoTenField.getText().trim();
                String gioiTinh = (String) gioiTinhComboBox.getSelectedItem();
                // Lấy ngày thành lập từ date picker
                Date selectedDate_ngaySinh = (Date) ngaySinhPicker.getModel().getValue();
                    if (selectedDate_ngaySinh == null) {
                        JOptionPane.showMessageDialog(dialog, "Vui lòng chọn ngày sinh!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                java.sql.Date ngaySinh = new java.sql.Date(selectedDate_ngaySinh.getTime());
                String diaChi = diaChiField.getText().trim();
                String soDienThoai = soDienThoaiField.getText().trim();
                String email = emailField.getText().trim();
                String trinhDoHocVan = (String) trinhDoHocVanComboBox.getSelectedItem();
                // Lấy ngày vào làm từ date picker
                Date selectedDate_ngayVaoLam = (Date) ngayVaoLamPicker.getModel().getValue();
                    if (ngayVaoLamPicker == null) {
                        JOptionPane.showMessageDialog(dialog, "Vui lòng chọn ngày thành lập!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                java.sql.Date ngayVaoLam = new java.sql.Date(selectedDate_ngayVaoLam.getTime());
                String tinhTrang = (String) tinhTrangComboBox.getSelectedItem();

                // Lấy mã phòng ban và chức vụ
                String selectedPhongBan = (String) maPhongBanComboBox.getSelectedItem();
                int maPhongBan = selectedPhongBan.equals("Không có phòng ban") ? 0 : 
                    Integer.parseInt(selectedPhongBan.split(" - ")[0]);

                String selectedChucVu = (String) maChucVuComboBox.getSelectedItem();
                int maChucVu = selectedChucVu.equals("Không có chức vụ") ? 0 : 
                    Integer.parseInt(selectedChucVu.split(" - ")[0]);

                // Validate dữ liệu
                if (hoTen.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Họ tên không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // Lấy dữ liệu hợp đồng
                String loaiHopDong = (String) loaiHopDongComboBox.getSelectedItem();
                Date selectedDate_ngayBatDau = (Date) ngayBatDauPicker.getModel().getValue();
                    if (ngayBatDauPicker == null) {
                        JOptionPane.showMessageDialog(dialog, "Vui lòng chọn ngày ngày bắt đầu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                java.sql.Date ngayBatDau = new java.sql.Date(selectedDate_ngayBatDau.getTime());
                // Lấy ngày kết thúc từ date picker
                Date selectedDate_ngayKetThuc = (Date) ngayKetThucPicker.getModel().getValue();
                    if (ngayKetThucPicker == null) {
                        JOptionPane.showMessageDialog(dialog, "Vui lòng chọn ngày kết thúc!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                java.sql.Date ngayKetThuc = new java.sql.Date(selectedDate_ngayKetThuc.getTime());
                // Lấy ngày ký từ date picker
                Date selectedDate_ngayKy = (Date) ngayKyPicker.getModel().getValue();
                    if (ngayKyPicker == null) {
                        JOptionPane.showMessageDialog(dialog, "Vui lòng chọn ngày ký!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                java.sql.Date ngayKy = new java.sql.Date(selectedDate_ngayKy.getTime());
                String trangThaiHopDong = (String) trangThaiHopDongComboBox.getSelectedItem();
                String luongCoBanStr = luongCoBanField.getText().trim();
                // Xác thực dữ liệu hợp đồng
                if (luongCoBanStr.isEmpty() || !luongCoBanStr.matches("\\d+(\\.\\d+)?")) {
                    JOptionPane.showMessageDialog(dialog, "Lương cơ bản phải là số hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Tạo đối tượng NhanSuModel và lưu vào database
                NhanSuModel newNhanSu = new NhanSuModel();
                newNhanSu.setMaSo(maSo);
                newNhanSu.setHoTen(hoTen);
                newNhanSu.setGioiTinh(gioiTinh);
                newNhanSu.setNgaySinh(ngaySinh);
                newNhanSu.setDiaChi(diaChi);
                newNhanSu.setSoDienThoai(soDienThoai);
                newNhanSu.setEmail(email);
                newNhanSu.setTrinhDoHocVan(trinhDoHocVan);
                newNhanSu.setMaPhongBan(maPhongBan);
                newNhanSu.setMaChucVu(maChucVu);
                newNhanSu.setNgayVaoLam(ngayVaoLam);
                newNhanSu.setTinhTrang(tinhTrang);
                
                // Tạo đối tượng ContractModel
                ContractModel newContract = new ContractModel();
                newContract.setLoaiHopDong(ContractModel.LoaiHopDong.valueOf(loaiHopDong));
                newContract.setNgayBatDau(ngayBatDau);
                newContract.setNgayKetThuc(ngayKetThuc);
                newContract.setNgayKy(ngayKy);
                newContract.setTrangThai(ContractModel.TrangThaiHopDong.valueOf(trangThaiHopDong));
                newContract.setLuongCoBan(new BigDecimal(luongCoBanStr));
                

                boolean success = nhansu.insertNhanVienAndContract(newNhanSu,newContract);

                if (success) {
                    JOptionPane.showMessageDialog(dialog, "Thêm nhân viên thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    refreshTableData();
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Thêm nhân viên thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(dialog, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Nút Hủy
        JButton cancelButton = new JButton("Hủy bỏ");
        cancelButton.setFont(new Font("Arial", Font.BOLD, 14));
        cancelButton.setBackground(new Color(204, 0, 0));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.addActionListener(e -> dialog.dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        // Thêm các panel vào dialog
        dialog.add(mainPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // Hiển thị dialog
        dialog.setVisible(true);
    }

    private void exportToExcel() {
        try {
            //tạo workbook và sheet
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("DanhSachNhanSu");
            XSSFRow row = null;
            Cell cell = null;
            
            // Định dạng ngày hiện tại
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String currentDate = dateFormat.format(new Date());
            // Tạo style để căn giữa
            CellStyle centerStyle = workbook.createCellStyle();
            centerStyle.setAlignment(org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER);
            // Tiêu đề chính
            row = sheet.createRow(0);
            cell = row.createCell(0, CellType.STRING);
            cell.setCellValue("DANH SÁCH NHÂN SỰ");
            cell.setCellStyle(centerStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 12)); // Gộp cột từ 0 đến 12
            
            // Dòng thông tin bổ sung
            row = sheet.createRow(1);
            cell = row.createCell(0, CellType.STRING);
            cell.setCellValue("Thời gian xuất: " + currentDate);
            cell.setCellStyle(centerStyle);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 12)); // Gộp cột từ 0 đến 12
            
            // Header của bảng
            row = sheet.createRow(3);
            String[] headers = {
                "STT", "Mã NV", "Họ và tên", "Giới tính", "Ngày sinh",
                "Địa chỉ", "Số điện thoại", "Email", "Trình độ học vấn",
                "Mã phòng ban", "Mã chức vụ", "Ngày vào làm", "Tình trạng"
            };
            for (int i = 0; i < headers.length; i++) {
                cell = row.createCell(i, CellType.STRING);
                cell.setCellValue(headers[i]);
            }
            // Lấy dữ liệu từ tableModel
            List<NhanSuModel> nhanSuList = new ArrayList<>();
            nhansu = new nhanSuController();
            List<NhanSuModel> allNhanSu = nhansu.getAll(); // Lấy toàn bộ danh sách để tìm kiếm
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String maSo = (String) tableModel.getValueAt(i, 0);
                // Tìm NhanSuModel tương ứng với maSo
                for (NhanSuModel nhanSu : allNhanSu) {
                    if (nhanSu.getMaSo().equals(maSo)) {
                        nhanSuList.add(nhanSu);
                        break;
                    }
                }
            }
            

            // Điền dữ liệu
            for (int i = 0; i < nhanSuList.size(); i++) {
                NhanSuModel nhanSu = nhanSuList.get(i);
                row = sheet.createRow(4 + i);

                // STT
                cell = row.createCell(0, CellType.NUMERIC);
                cell.setCellValue(i + 1);

                // Mã NV
                cell = row.createCell(1, CellType.STRING);
                cell.setCellValue(nhanSu.getMaSo() != null ? nhanSu.getMaSo() : "");

                // Họ và tên
                cell = row.createCell(2, CellType.STRING);
                cell.setCellValue(nhanSu.getHoTen() != null ? nhanSu.getHoTen() : "");

                // Giới tính
                cell = row.createCell(3, CellType.STRING);
                cell.setCellValue(nhanSu.getGioiTinh() != null ? nhanSu.getGioiTinh() : "");

                // Ngày sinh
                cell = row.createCell(4, CellType.STRING);
                cell.setCellValue(nhanSu.getNgaySinh() != null ? dateFormat.format(nhanSu.getNgaySinh()) : "");

                // Địa chỉ
                cell = row.createCell(5, CellType.STRING);
                cell.setCellValue(nhanSu.getDiaChi() != null ? nhanSu.getDiaChi() : "");

                // Số điện thoại
                cell = row.createCell(6, CellType.STRING);
                cell.setCellValue(nhanSu.getSoDienThoai() != null ? nhanSu.getSoDienThoai() : "");

                // Email
                cell = row.createCell(7, CellType.STRING);
                cell.setCellValue(nhanSu.getEmail() != null ? nhanSu.getEmail() : "");

                // Trình độ học vấn
                cell = row.createCell(8, CellType.STRING);
                cell.setCellValue(nhanSu.getTrinhDoHocVan() != null ? nhanSu.getTrinhDoHocVan() : "");

                // Mã phòng ban
                cell = row.createCell(9, CellType.NUMERIC);
                cell.setCellValue(nhanSu.getMaPhongBan());

                // Mã chức vụ
                cell = row.createCell(10, CellType.NUMERIC);
                cell.setCellValue(nhanSu.getMaChucVu());

                // Ngày vào làm
                cell = row.createCell(11, CellType.STRING);
                cell.setCellValue(nhanSu.getNgayVaoLam() != null ? dateFormat.format(nhanSu.getNgayVaoLam()) : "");

                // Tình trạng
                cell = row.createCell(12, CellType.STRING);
                cell.setCellValue(nhanSu.getTinhTrang() != null ? nhanSu.getTinhTrang() : "");
            }
            // Tự động điều chỉnh độ rộng cột
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Lưu file
            // Sử dụng JFileChooser để chọn đường dẫn
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setSelectedFile(new File("DanhSachNhanSu.xlsx")); // Đặt tên file mặc định
            
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
                // Đảm bảo file có đuôi .xlsx
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
            } else {
                // Người dùng hủy chọn file
                workbook.close();
                return;
            }

            try {
                workbook.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi xuất Excel", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

  // Renderer cho cột "Thao tác"
    class ButtonRenderer implements TableCellRenderer {
        private JPanel panel;

        public ButtonRenderer() {
            panel = createButtonPanel(false, null, null, null);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                panel.setBackground(table.getSelectionBackground());
            } else {
                panel.setBackground(table.getBackground());
            }
            return panel;
        }
    }

    // Editor cho cột "Thao tác"
    class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
        private JPanel panel;
        private int row;
        private JTable table;

        public ButtonEditor(JCheckBox checkBox) {
            panel = createButtonPanel(true,
                e -> {
                if (table != null && row < table.getRowCount()) { // Kiểm tra hàng hợp lệ
                    showDetailDialog(row);
                }
                fireEditingStopped();
                },
                e -> {
                    if (table != null && row < table.getRowCount()) { // Kiểm tra hàng hợp lệ
                        editDetailDialog(row);
                    }
                    fireEditingStopped();
                },
                e -> {
                        if (table == null || row >= table.getRowCount()) { // Kiểm tra hàng hợp lệ
                            fireEditingStopped();
                            return;
                        }
                        // Lấy mã số từ cột 0
                        String maSo = (String) tableModel.getValueAt(row, 0);

                        // Tìm NhanSuModel tương ứng
                        List<NhanSuModel> nhanSuList = nhansu.getAll();
                        NhanSuModel selectedNhanSu = null;
                        for (NhanSuModel nhanSu : nhanSuList) {
                            if (nhanSu.getMaSo().equals(maSo)) {
                                selectedNhanSu = nhanSu;
                                break;
                            }
                        }

                        if (selectedNhanSu == null) {
                            JOptionPane.showMessageDialog(null, "Không tìm thấy thông tin nhân viên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                            fireEditingStopped();
                            return;
                        }

                        // Xác nhận xóa
                        int confirm = JOptionPane.showConfirmDialog(
                            null,
                            "Bạn có chắc muốn xóa nhân viên: " + selectedNhanSu.getHoTen() + " (Mã: " + maSo + ")?",
                            "Xác nhận xóa",
                            JOptionPane.YES_NO_OPTION
                        );

                        if (confirm == JOptionPane.YES_OPTION) {
                            try {
                                // Gọi hàm delete
                                nhansu.delete(selectedNhanSu.getMaNhanVien());
                                JOptionPane.showMessageDialog(null, "Xóa nhân viên thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                                // Hoãn làm mới bảng để tránh xung đột
                                SwingUtilities.invokeLater(() -> {
                                    refreshTableData();
                                });
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    JOptionPane.showMessageDialog(null, "Xóa nhân viên thất bại! Vui lòng thử lại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                                }
                        }

                        fireEditingStopped();                   
                }
            );
        }
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.table = table; // Lưu tham chiếu đến bảng
            this.row = row;
            if (isSelected) {
                panel.setBackground(table.getSelectionBackground());
            } else {
                panel.setBackground(table.getBackground());
            }
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return panel;
        }
        private void showDetailDialog(int row){
            
             // Lấy mã nhân viên từ cột 0
            String maSo = (String) tableModel.getValueAt(row, 0);
            List<NhanSuModel> nhanSuList = nhansu.getAll();
            NhanSuModel selectedNhanSu = null;
            for (NhanSuModel nhanSu : nhanSuList) {
                if (nhanSu.getMaSo().equals(maSo)) {
                    selectedNhanSu = nhanSu;
                    break;
                }
            }

            if (selectedNhanSu == null) {
                JOptionPane.showMessageDialog(null, "Không tìm thấy thông tin nhân viên!");
                return;
            }

            // Tạo dialog
            JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(EmployeeProfileView.this), "Chi tiết nhân sự", true);
            dialog.setLayout(new GridLayout(13, 2, 15, 15)); // 13 hàng (12 trường + 1 nút Quay lại), 2 cột (label + textfield)
            dialog.setSize(600, 500);
            dialog.setLocationRelativeTo(EmployeeProfileView.this);
            

            // Font chữ lớn hơn
            Font labelFont = new Font("Arial", Font.PLAIN, 16);
            Font fieldFont = new Font("Arial", Font.PLAIN, 16);

            // Thêm các thành phần
            JLabel maNhanVienLabel = new JLabel("Mã nhân viên:");
            maNhanVienLabel.setFont(labelFont);
            dialog.add(maNhanVienLabel);
            JTextField maNhanVienField = new JTextField(selectedNhanSu.getMaSo());
            maNhanVienField.setFont(fieldFont);
            maNhanVienField.setEditable(false);
            dialog.add(maNhanVienField);

            JLabel hoTenLabel = new JLabel("Họ và tên:");
            hoTenLabel.setFont(labelFont);
            dialog.add(hoTenLabel);
            JTextField hoTenField = new JTextField(selectedNhanSu.getHoTen());
            hoTenField.setFont(fieldFont);
            hoTenField.setEditable(false);
            dialog.add(hoTenField);

            JLabel gioiTinhLabel = new JLabel("Giới tính:");
            gioiTinhLabel.setFont(labelFont);
            dialog.add(gioiTinhLabel);
            JTextField gioiTinhField = new JTextField(selectedNhanSu.getGioiTinh());
            gioiTinhField.setFont(fieldFont);
            gioiTinhField.setEditable(false);
            dialog.add(gioiTinhField);

            JLabel ngaySinhLabel = new JLabel("Ngày sinh:");
            ngaySinhLabel.setFont(labelFont);
            dialog.add(ngaySinhLabel);
            JTextField ngaySinhField = new JTextField(selectedNhanSu.getNgaySinh() != null ? selectedNhanSu.getNgaySinh().toString() : "");
            ngaySinhField.setFont(fieldFont);
            ngaySinhField.setEditable(false);
            dialog.add(ngaySinhField);

            JLabel diaChiLabel = new JLabel("Địa chỉ:");
            diaChiLabel.setFont(labelFont);
            dialog.add(diaChiLabel);
            JTextField diaChiField = new JTextField(selectedNhanSu.getDiaChi());
            diaChiField.setFont(fieldFont);
            diaChiField.setEditable(false);
            dialog.add(diaChiField);

            JLabel soDienThoaiLabel = new JLabel("Số điện thoại:");
            soDienThoaiLabel.setFont(labelFont);
            dialog.add(soDienThoaiLabel);
            JTextField soDienThoaiField = new JTextField(selectedNhanSu.getSoDienThoai());
            soDienThoaiField.setFont(fieldFont);
            soDienThoaiField.setEditable(false);
            dialog.add(soDienThoaiField);

            JLabel emailLabel = new JLabel("Email:");
            emailLabel.setFont(labelFont);
            dialog.add(emailLabel);
            JTextField emailField = new JTextField(selectedNhanSu.getEmail());
            emailField.setFont(fieldFont);
            emailField.setEditable(false);
            dialog.add(emailField);

            JLabel trinhDoHocVanLabel = new JLabel("Trình độ học vấn:");
            trinhDoHocVanLabel.setFont(labelFont);
            dialog.add(trinhDoHocVanLabel);
            JTextField trinhDoHocVanField = new JTextField(selectedNhanSu.getTrinhDoHocVan());
            trinhDoHocVanField.setFont(fieldFont);
            trinhDoHocVanField.setEditable(false);
            dialog.add(trinhDoHocVanField);

            JLabel maPhongBanLabel = new JLabel("Mã phòng ban:");
            maPhongBanLabel.setFont(labelFont);
            dialog.add(maPhongBanLabel);
            JTextField maPhongBanField = new JTextField();
            maPhongBanField.setFont(fieldFont);
            maPhongBanField.setEditable(false);
            maPhongBanField.setBackground(new Color(240, 240, 240));
            maPhongBanField.setForeground(Color.BLACK);
            
            PhongBanController phongbancontroller = new PhongBanController();
            String displayText_phongban = "N/A";
            if(selectedNhanSu != null){
                int maphongban = selectedNhanSu.getMaPhongBan();
                PhongBanModel phongban = phongbancontroller.findById(maphongban);
                if(phongban != null){
                    displayText_phongban = phongban.getMaPhongBan() + "-" + phongban.getTenPhongBan();
                }
            }
            maPhongBanField.setText(displayText_phongban);           
            dialog.add(maPhongBanField);

            JLabel maChucVuLabel = new JLabel("Mã chức vụ:");
            maChucVuLabel.setFont(labelFont);
            dialog.add(maChucVuLabel);

            JTextField maChucVuField = new JTextField();
            maChucVuField.setFont(fieldFont);
            maChucVuField.setEditable(false);
            maChucVuField.setBackground(new Color(240, 240, 240)); // Nền xám nhạt để biểu thị không chỉnh sửa
            maChucVuField.setForeground(Color.BLACK);

            // Lấy thông tin chức vụ từ ChucVuController
            ChucVuController chucVuController = new ChucVuController();
            String displayText = "N/A"; // Giá trị mặc định nếu không tìm thấy chức vụ
            if (selectedNhanSu != null) {
                int maChucVu = selectedNhanSu.getMaChucVu();
                ChucVuModel chucVu = chucVuController.findById(maChucVu);
                if (chucVu != null) {
                    displayText = chucVu.getMaChucVu() + " - " + chucVu.getTenChucVu();
                }
            }
            maChucVuField.setText(displayText);
            dialog.add(maChucVuField);

            JLabel ngayVaoLamLabel = new JLabel("Ngày vào làm:");
            ngayVaoLamLabel.setFont(labelFont);
            dialog.add(ngayVaoLamLabel);
            JTextField ngayVaoLamField = new JTextField(selectedNhanSu.getNgayVaoLam() != null ? selectedNhanSu.getNgayVaoLam().toString() : "");
            ngayVaoLamField.setFont(fieldFont);
            ngayVaoLamField.setEditable(false);
            dialog.add(ngayVaoLamField);

            JLabel tinhTrangLabel = new JLabel("Tình trạng:");
            tinhTrangLabel.setFont(labelFont);
            dialog.add(tinhTrangLabel);
            JTextField tinhTrangField = new JTextField(selectedNhanSu.getTinhTrang());
            tinhTrangField.setFont(fieldFont);
            tinhTrangField.setEditable(false);
            dialog.add(tinhTrangField);

            // Nút Quay trở lại
            JLabel placeholderLabel = new JLabel(""); // Placeholder để căn giữa nút
            dialog.add(placeholderLabel);
            JButton backButton = new JButton("Quay trở lại");
            backButton.setFont(new Font("Arial", Font.PLAIN, 16)); // Tăng font cho nút
            backButton.addActionListener(e -> dialog.dispose());
            dialog.add(backButton);

            // Hiển thị dialog
            dialog.setVisible(true);
        }
        
        //editdialog
        private void editDetailDialog(int row){
             // Lấy mã nhân viên từ cột 0
            String maSo = (String) tableModel.getValueAt(row, 0);
            List<NhanSuModel> nhanSuList = nhansu.getAll();
            selectedNhanSu = null;
            for (NhanSuModel nhanSu : nhanSuList) {
                if (nhanSu.getMaSo().equals(maSo)) {
                    selectedNhanSu = nhanSu;
                    break;
                }
            }

            if (selectedNhanSu == null) {
                JOptionPane.showMessageDialog(null, "Không tìm thấy thông tin nhân viên!");
                return;
            }

            // Tạo dialog
            JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(EmployeeProfileView.this), "Chi tiết nhân sự", true);
            dialog.setLayout(new GridLayout(13, 2, 15, 15)); // 13 hàng (12 trường + 1 nút Quay lại), 2 cột (label + textfield)
            dialog.setSize(600, 500);
            dialog.setLocationRelativeTo(EmployeeProfileView.this);
            

            // Font chữ lớn hơn
            Font labelFont = new Font("Arial", Font.PLAIN, 16);
            Font fieldFont = new Font("Arial", Font.PLAIN, 16);

            // Thêm các thành phần
            JLabel maNhanVienLabel = new JLabel("Mã nhân viên:");
            maNhanVienLabel.setFont(labelFont);
            dialog.add(maNhanVienLabel);
            JTextField maNhanVienField = new JTextField(selectedNhanSu.getMaSo());
            maNhanVienField.setFont(fieldFont);
            maNhanVienField.setEditable(false);
            dialog.add(maNhanVienField);

            JLabel hoTenLabel = new JLabel("Họ và tên:");
            hoTenLabel.setFont(labelFont);
            dialog.add(hoTenLabel);
            JTextField hoTenField = new JTextField(selectedNhanSu.getHoTen());
            hoTenField.setFont(fieldFont);
            hoTenField.setEditable(true);
            dialog.add(hoTenField);

            JLabel gioiTinhLabel = new JLabel("Giới tính:");
            gioiTinhLabel.setFont(labelFont);
            dialog.add(gioiTinhLabel);
             String[] gioiTinhOptions = {"Nam", "Nu"};
            JComboBox<String> gioiTinhComboBox = new JComboBox<>(gioiTinhOptions);
            gioiTinhComboBox.setFont(fieldFont);
            gioiTinhComboBox.setSelectedItem(selectedNhanSu.getGioiTinh());
            dialog.add(gioiTinhComboBox);

            JLabel ngaySinhLabel = new JLabel("Ngày sinh:");
            ngaySinhLabel.setFont(labelFont);
            dialog.add(ngaySinhLabel);
            JTextField ngaySinhField = new JTextField(selectedNhanSu.getNgaySinh() != null ? selectedNhanSu.getNgaySinh().toString() : "");
            ngaySinhField.setFont(fieldFont);
            ngaySinhField.setEditable(true);
            dialog.add(ngaySinhField);

            JLabel diaChiLabel = new JLabel("Địa chỉ:");
            diaChiLabel.setFont(labelFont);
            dialog.add(diaChiLabel);
            JTextField diaChiField = new JTextField(selectedNhanSu.getDiaChi());
            diaChiField.setFont(fieldFont);
            diaChiField.setEditable(true);
            dialog.add(diaChiField);

            JLabel soDienThoaiLabel = new JLabel("Số điện thoại:");
            soDienThoaiLabel.setFont(labelFont);
            dialog.add(soDienThoaiLabel);
            JTextField soDienThoaiField = new JTextField(selectedNhanSu.getSoDienThoai());
            soDienThoaiField.setFont(fieldFont);
            soDienThoaiField.setEditable(true);
            dialog.add(soDienThoaiField);

            JLabel emailLabel = new JLabel("Email:");
            emailLabel.setFont(labelFont);
            dialog.add(emailLabel);
            JTextField emailField = new JTextField(selectedNhanSu.getEmail());
            emailField.setFont(fieldFont);
            emailField.setEditable(true);
            dialog.add(emailField);

            JLabel trinhDoHocVanLabel = new JLabel("Trình độ học vấn:");
            trinhDoHocVanLabel.setFont(labelFont);
            dialog.add(trinhDoHocVanLabel);
            String[] trinhDoOptions = {"Dai_hoc", "Cao_dang", "Tot_nghiep_cap_3"};
            JComboBox<String> trinhDoHocVanComboBox = new JComboBox<>(trinhDoOptions);
            trinhDoHocVanComboBox.setFont(fieldFont);
            trinhDoHocVanComboBox.setSelectedItem(selectedNhanSu.getTrinhDoHocVan());
            dialog.add(trinhDoHocVanComboBox);

            JLabel maPhongBanLabel = new JLabel("Mã phòng ban:");
            maPhongBanLabel.setFont(labelFont);
            dialog.add(maPhongBanLabel);
            //  Load danh sách phòng ban từ database
            JComboBox<String> maPhongBanComboBox = new JComboBox<>();
            maPhongBanComboBox.setFont(fieldFont);
            
            PhongBanController phongBanController = new PhongBanController();
            List<String> phongbanlist = phongBanController.getPhongBanDisplayList();
            for (String string : phongbanlist) {
                maPhongBanComboBox.addItem(string);
            }
            int selectedMaPhongBan = selectedNhanSu.getMaPhongBan();
            for (int i = 0; i < maPhongBanComboBox.getItemCount(); i++) {
                String item = maPhongBanComboBox.getItemAt(i);
                if (item.startsWith(selectedMaPhongBan + " -")) {
                    maPhongBanComboBox.setSelectedIndex(i);
                    break;
                }
            }
            dialog.add(maPhongBanComboBox);

            JLabel maChucVuLabel = new JLabel("Mã chức vụ:");
            maChucVuLabel.setFont(labelFont);
            dialog.add(maChucVuLabel);
            // TODO: Load danh sách chức vụ từ database
            JComboBox<String> maChucVuComboBox = new JComboBox<>();
            maChucVuComboBox.setFont(fieldFont);
            // load chuc vu tu database
            ChucVuController chucVuController = new ChucVuController();
            List<String> chucvulist = chucVuController.getChucVuDisplayList();
            for (String string : chucvulist) {
                maChucVuComboBox.addItem(string);
            }
            int selectedMaChucVu = selectedNhanSu.getMaChucVu();
            for (int i = 0; i < maChucVuComboBox.getItemCount(); i++) {
                String item = maChucVuComboBox.getItemAt(i);
                if (item.startsWith(selectedMaChucVu + " -")) {
                    maChucVuComboBox.setSelectedIndex(i);
                    break;
                }
            }
            dialog.add(maChucVuComboBox);

            JLabel ngayVaoLamLabel = new JLabel("Ngày vào làm:");
            ngayVaoLamLabel.setFont(labelFont);
            dialog.add(ngayVaoLamLabel);
            JTextField ngayVaoLamField = new JTextField(selectedNhanSu.getNgayVaoLam() != null ? selectedNhanSu.getNgayVaoLam().toString() : "");
            ngayVaoLamField.setFont(fieldFont);
            ngayVaoLamField.setEditable(true);
            dialog.add(ngayVaoLamField);

            JLabel tinhTrangLabel = new JLabel("Tình trạng:");
            tinhTrangLabel.setFont(labelFont);
            dialog.add(tinhTrangLabel);
            String[] tinhTrangOptions = {"Dang_lam", "Da_nghi"};
            JComboBox<String> tinhTrangComboBox = new JComboBox<>(tinhTrangOptions);
            tinhTrangComboBox.setFont(fieldFont);
            tinhTrangComboBox.setSelectedItem(selectedNhanSu.getTinhTrang());
            dialog.add(tinhTrangComboBox);
            
            JButton saveButton = new JButton("Lưu thay đổi");
            saveButton.setFont(new Font("Arial", Font.PLAIN, 16));
            saveButton.addActionListener(e ->{
                 try {
                    // Lấy dữ liệu từ các component trong dialog
                    String hoTen = hoTenField.getText().trim();
                    String gioiTinh = (String) gioiTinhComboBox.getSelectedItem();
                    String ngaySinh = ngaySinhField.getText().trim();
                    String diaChi = diaChiField.getText().trim();
                    String soDienThoai = soDienThoaiField.getText().trim();
                    String email = emailField.getText().trim();
                    String trinhDoHocVan = (String) trinhDoHocVanComboBox.getSelectedItem();
                    String ngayVaoLam = ngayVaoLamField.getText().trim();
                    String tinhTrang = (String) tinhTrangComboBox.getSelectedItem();

                    // Lấy mã phòng ban từ ComboBox (format: "1 - Tên phòng ban")
                    String selectedPhongBan = (String) maPhongBanComboBox.getSelectedItem();
                    int maPhongBan = Integer.parseInt(selectedPhongBan.split(" - ")[0]);

                    // Lấy mã chức vụ từ ComboBox (format: "1 - Tên chức vụ")
                    String selectedChucVu = (String) maChucVuComboBox.getSelectedItem();
                    int maChucVu = Integer.parseInt(selectedChucVu.split(" - ")[0]);

                    // Validate dữ liệu
                    if (hoTen.isEmpty()) {
                        JOptionPane.showMessageDialog(dialog, "Họ tên không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (email.isEmpty() || !email.contains("@")) {
                        JOptionPane.showMessageDialog(dialog, "Email không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (soDienThoai.isEmpty() || !soDienThoai.matches("\\d{10,11}")) {
                        JOptionPane.showMessageDialog(dialog, "Số điện thoại phải có 10-11 chữ số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Tạo object NhanSuModel với dữ liệu mới
                    NhanSuModel updatedNhanSu = new NhanSuModel();
                    updatedNhanSu.setMaNhanVien(selectedNhanSu.getMaNhanVien()); // ID không đổi
                    updatedNhanSu.setMaSo(selectedNhanSu.getMaSo()); // Mã số không đổi
                    updatedNhanSu.setHoTen(hoTen);
                    updatedNhanSu.setGioiTinh(gioiTinh);
                    updatedNhanSu.setDiaChi(diaChi);
                    updatedNhanSu.setSoDienThoai(soDienThoai);
                    updatedNhanSu.setEmail(email);
                    updatedNhanSu.setTrinhDoHocVan(trinhDoHocVan);
                    updatedNhanSu.setMaPhongBan(maPhongBan);
                    updatedNhanSu.setMaChucVu(maChucVu);
                    updatedNhanSu.setTinhTrang(tinhTrang);
                    
                    // Parse và set ngày sinh
                    if (!ngaySinh.isEmpty()) {
                        try {
                            java.sql.Date parsedNgaySinh = java.sql.Date.valueOf(ngaySinh);
                            updatedNhanSu.setNgaySinh(parsedNgaySinh);
                        } catch (IllegalArgumentException ex) {
                            JOptionPane.showMessageDialog(dialog, "Ngày sinh không đúng định dạng (yyyy-MM-dd)!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }

                    // Parse và set ngày vào làm
                    if (!ngayVaoLam.isEmpty()) {
                        try {
                            java.sql.Date parsedNgayVaoLam = java.sql.Date.valueOf(ngayVaoLam);
                            updatedNhanSu.setNgayVaoLam(parsedNgayVaoLam);
                        } catch (IllegalArgumentException ex) {
                            JOptionPane.showMessageDialog(dialog, "Ngày vào làm không đúng định dạng (yyyy-MM-dd)!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }

                    // Gọi method update trong controller
                    boolean success = nhansu.updateNhanVien(updatedNhanSu);

                    if (success) {
                        JOptionPane.showMessageDialog(dialog, "Cập nhật thông tin nhân viên thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);

                        SwingUtilities.invokeLater(() -> refreshTableData()); // Hoãn làm mới bảng
                        // Đóng dialog
                        dialog.dispose();

                    } else {
                        JOptionPane.showMessageDialog(dialog, "Cập nhật thất bại! Vui lòng thử lại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(dialog, "Có lỗi xảy ra: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            });
            dialog.add(saveButton);

            // Nút Quay trở lại
            JButton backButton = new JButton("Quay trở lại");
            backButton.setFont(new Font("Arial", Font.PLAIN, 16));
            backButton.addActionListener(e -> dialog.dispose());
            dialog.add(backButton);

            // Hiển thị dialog
            dialog.setVisible(true);
        }
        
    }
}
 