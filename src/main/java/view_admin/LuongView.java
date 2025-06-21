package view_admin;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

public class LuongView extends JPanel {
    private JTextField tfMaNV, tfNgayLuong, tfNgayCong, tfTangCa, tfThuong, tfPhuCap, tfKhauTru, tfSearch;
    private JButton btnAdd, btnUpdate, btnDelete, btnSearch, btnRefresh;
    private JTable table;
    private DefaultTableModel model;

    public LuongView() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel lblTitle = new JLabel("HỆ THỐNG QUẢN LÝ NHÂN SỰ", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(new Color(33, 150, 243));
        lblTitle.setBorder(new EmptyBorder(10, 0, 20, 0));
        add(lblTitle, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));

        // Bảng thông tin lương
        JPanel formPanel = new JPanel(new GridLayout(4, 4, 12, 12));
        tfMaNV = new JTextField(); tfNgayLuong = new JTextField();
        tfNgayCong = new JTextField(); tfTangCa = new JTextField();
        tfThuong = new JTextField(); tfPhuCap = new JTextField();
        tfKhauTru = new JTextField(); tfSearch = new JTextField();

        formPanel.add(new JLabel("Mã Nhân Viên:")); formPanel.add(tfMaNV);
        formPanel.add(new JLabel("Ngày Tính Lương (yyyy-mm-dd):")); formPanel.add(tfNgayLuong);
        formPanel.add(new JLabel("Số Ngày Công:")); formPanel.add(tfNgayCong);
        formPanel.add(new JLabel("Số Giờ Tăng Ca:")); formPanel.add(tfTangCa);
        formPanel.add(new JLabel("Tiền Thưởng:")); formPanel.add(tfThuong);
        formPanel.add(new JLabel("Tổng Phụ Cấp:")); formPanel.add(tfPhuCap);
        formPanel.add(new JLabel("Tổng Khấu Trừ:")); formPanel.add(tfKhauTru);
        formPanel.add(new JLabel("Tìm theo tên nhân viên:")); formPanel.add(tfSearch);

        contentPanel.add(formPanel, BorderLayout.NORTH);

        // Nút chức năng
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        btnAdd = new JButton("Thêm"); btnUpdate = new JButton("Sửa");
        btnDelete = new JButton("Xoá"); btnSearch = new JButton("Tìm kiếm"); btnRefresh = new JButton("Làm mới");
        buttonPanel.add(btnAdd); buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete); buttonPanel.add(btnSearch); buttonPanel.add(btnRefresh);
        contentPanel.add(buttonPanel, BorderLayout.CENTER);

        add(contentPanel, BorderLayout.NORTH);

        // Bảng dữ liệu
        model = new DefaultTableModel();
        table = new JTable(model);
        table.setRowHeight(28);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 15));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.setDefaultRenderer(Object.class, centerRenderer);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Danh sách lương nhân viên"));
        add(scrollPane, BorderLayout.CENTER);
    }

    public JTable getTable() { return table; }
    public int getMaNhanVien() { return Integer.parseInt(tfMaNV.getText().trim()); }
    public Date getNgayTinhLuong() { return java.sql.Date.valueOf(tfNgayLuong.getText().trim()); }
    public int getSoNgayCong() { return Integer.parseInt(tfNgayCong.getText().trim()); }
    public int getSoGioTangCa() { return Integer.parseInt(tfTangCa.getText().trim()); }
    public BigDecimal getTienThuong() { return new BigDecimal(tfThuong.getText().trim()); }
    public BigDecimal getTongPhuCap() { return new BigDecimal(tfPhuCap.getText().trim()); }
    public BigDecimal getTongKhauTru() { return new BigDecimal(tfKhauTru.getText().trim()); }
    public String getSearchKeyword() { return tfSearch.getText().trim(); }

    public void setMaNVField(int maNV) { tfMaNV.setText(String.valueOf(maNV)); }
    public void setNgayLuongField(Date date) { tfNgayLuong.setText(date.toString()); }
    public void setNgayCongField(int val) { tfNgayCong.setText(String.valueOf(val)); }
    public void setGioTCField(int val) { tfTangCa.setText(String.valueOf(val)); }
    public void setThuongField(BigDecimal val) { tfThuong.setText(val.toString()); }
    public void setPhuCapField(BigDecimal val) { tfPhuCap.setText(val.toString()); }
    public void setKhauTruField(BigDecimal val) { tfKhauTru.setText(val.toString()); }

    public JButton getBtnAdd() { return btnAdd; }
    public JButton getBtnUpdate() { return btnUpdate; }
    public JButton getBtnDelete() { return btnDelete; }
    public JButton getBtnSearch() { return btnSearch; }
    public JButton getBtnRefresh() { return btnRefresh; }

    public void loadTable(Vector<Vector<Object>> data, Vector<String> column) {
        model.setDataVector(data, column);
    }

    public int getSelectedLuongId() {
        int row = table.getSelectedRow();
        if (row == -1) return -1;
        return (int) table.getValueAt(row, 0);
    }

}
