package view;

import controller.ChucVuController;
import model.ChucVuModel;
import model.ChucVuModel.TrangThaiChucVu;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.util.List;

public class ChucVuView extends JPanel {
    private JTextField txtMaChucVu, txtTenChucVu, txtMoTa, txtCapBac, txtPhuCapMacDinh, txtQuyenHan;
    private JComboBox<TrangThaiChucVu> cboTrangThai;
    private JTextField txtTimKiem;

    private JButton btnThem, btnSua, btnXoa, btnLamMoi, btnTimKiem;
    private JTable table;
    private DefaultTableModel tableModel;
    private ChucVuController controller;

    public ChucVuView() {
        controller = new ChucVuController();
        setLayout(new BorderLayout());
        initComponents();
        controllerLoadData();
        addEventHandlers();
    }

    private void initComponents() {
        JPanel pnlForm = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblMa = new JLabel("Mã chức vụ:");
        txtMaChucVu = new JTextField(10);
        txtMaChucVu.setEnabled(false);

        JLabel lblTen = new JLabel("Tên chức vụ:");
        txtTenChucVu = new JTextField(20);

        JLabel lblMoTa = new JLabel("Mô tả:");
        txtMoTa = new JTextField(20);

        JLabel lblCapBac = new JLabel("Cấp bậc:");
        txtCapBac = new JTextField(10);

        JLabel lblPhuCap = new JLabel("Phụ cấp mặc định:");
        txtPhuCapMacDinh = new JTextField(15);

        JLabel lblQuyenHan = new JLabel("Quyền hạn:");
        txtQuyenHan = new JTextField(20);

        JLabel lblTrangThai = new JLabel("Trạng thái:");
        cboTrangThai = new JComboBox<>(TrangThaiChucVu.values());

        gbc.gridx = 0; gbc.gridy = 0; pnlForm.add(lblMa, gbc);
        gbc.gridx = 1; pnlForm.add(txtMaChucVu, gbc);
        gbc.gridx = 2; pnlForm.add(lblTen, gbc);
        gbc.gridx = 3; pnlForm.add(txtTenChucVu, gbc);

        gbc.gridx = 0; gbc.gridy = 1; pnlForm.add(lblMoTa, gbc);
        gbc.gridx = 1; pnlForm.add(txtMoTa, gbc);
        gbc.gridx = 2; pnlForm.add(lblCapBac, gbc);
        gbc.gridx = 3; pnlForm.add(txtCapBac, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0; pnlForm.add(lblPhuCap, gbc);
        gbc.gridx = 1; pnlForm.add(txtPhuCapMacDinh, gbc);

        gbc.gridx = 0; gbc.gridy = 3; pnlForm.add(lblQuyenHan, gbc);
        gbc.gridx = 1; pnlForm.add(txtQuyenHan, gbc);
        gbc.gridx = 2; pnlForm.add(lblTrangThai, gbc);
        gbc.gridx = 3; pnlForm.add(cboTrangThai, gbc);

        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        btnThem = new JButton("Thêm");
        btnSua = new JButton("Sửa");
        btnXoa = new JButton("Xóa");
        btnLamMoi = new JButton("Làm mới");
        pnlButtons.add(btnThem);
        pnlButtons.add(btnSua);
        pnlButtons.add(btnXoa);
        pnlButtons.add(btnLamMoi);

        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        txtTimKiem = new JTextField(20);
        btnTimKiem = new JButton("Tìm kiếm");
        pnlSearch.add(new JLabel("Tìm kiếm tên:"));
        pnlSearch.add(txtTimKiem);
        pnlSearch.add(btnTimKiem);

        String[] columns = {"Mã chức vụ", "Tên chức vụ", "Mô tả", "Cấp bậc",
                "Phụ cấp mặc định", "Quyền hạn", "Trạng thái", "Ngày tạo"};

        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(80);
        columnModel.getColumn(1).setPreferredWidth(150);
        columnModel.getColumn(2).setPreferredWidth(200);
        columnModel.getColumn(3).setPreferredWidth(60);
        columnModel.getColumn(4).setPreferredWidth(130);
        columnModel.getColumn(5).setPreferredWidth(180);
        columnModel.getColumn(6).setPreferredWidth(100);
        columnModel.getColumn(7).setPreferredWidth(140);

        JScrollPane scrollPane = new JScrollPane(table);

        add(pnlForm, BorderLayout.NORTH);
        add(pnlButtons, BorderLayout.CENTER);
        add(pnlSearch, BorderLayout.SOUTH);
        add(scrollPane, BorderLayout.AFTER_LAST_LINE);
    }

    private void addEventHandlers() {
        btnThem.addActionListener(e -> themChucVu());
        btnSua.addActionListener(e -> suaChucVu());
        btnXoa.addActionListener(e -> xoaChucVu());
        btnLamMoi.addActionListener(e -> lamMoiForm());
        btnTimKiem.addActionListener(e -> timKiem());

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                fillFormFromSelectedRow();
            }
        });
    }

    private void controllerLoadData() {
        List<ChucVuModel> list = controller.getAll();
        setTableData(list);
    }

    public void setTableData(List<ChucVuModel> list) {
        tableModel.setRowCount(0);
        for (ChucVuModel cv : list) {
            tableModel.addRow(new Object[]{
                    cv.getMaChucVu(),
                    cv.getTenChucVu(),
                    cv.getMoTa(),
                    cv.getCapBac(),
                    cv.getPhuCapMacDinh(),
                    cv.getQuyenHan(),
                    cv.getTrangThai(),
                    cv.getNgayTao()
            });
        }
    }

    private ChucVuModel getFormData() {
        try {
            String ten = txtTenChucVu.getText().trim();
            if (ten.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tên chức vụ không được để trống!");
                return null;
            }
            String moTa = txtMoTa.getText().trim();
            Integer capBac = txtCapBac.getText().trim().isEmpty() ? null : Integer.parseInt(txtCapBac.getText().trim());
            BigDecimal phuCapMacDinh = txtPhuCapMacDinh.getText().trim().isEmpty() ? BigDecimal.ZERO : new BigDecimal(txtPhuCapMacDinh.getText().trim());
            String quyenHan = txtQuyenHan.getText().trim();
            TrangThaiChucVu trangThai = (TrangThaiChucVu) cboTrangThai.getSelectedItem();

            ChucVuModel cv = new ChucVuModel();
            if (!txtMaChucVu.getText().trim().isEmpty()) {
                cv.setMaChucVu(Integer.parseInt(txtMaChucVu.getText().trim()));
            }
            cv.setTenChucVu(ten);
            cv.setMoTa(moTa);
            cv.setCapBac(capBac);
            cv.setPhuCapMacDinh(phuCapMacDinh);
            cv.setQuyenHan(quyenHan);
            cv.setTrangThai(trangThai);

            return cv;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Dữ liệu số không hợp lệ!");
            return null;
        }
    }

    private void themChucVu() {
        ChucVuModel cv = getFormData();
        if (cv == null || cv.getMaChucVu() > 0) return;
        if (controller.insert(cv)) {
            JOptionPane.showMessageDialog(this, "Thêm chức vụ thành công!");
            controllerLoadData();
            lamMoiForm();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm chức vụ thất bại!");
        }
    }

    private void suaChucVu() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn chức vụ để sửa!");
            return;
        }
        ChucVuModel cv = getFormData();
        if (cv == null || cv.getMaChucVu() <= 0) return;
        if (controller.update(cv)) {
            JOptionPane.showMessageDialog(this, "Cập nhật chức vụ thành công!");
            controllerLoadData();
            lamMoiForm();
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật chức vụ thất bại!");
        }
    }

    private void xoaChucVu() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn chức vụ để xóa!");
            return;
        }
        int maChucVu = (int) table.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa chức vụ này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (controller.delete(maChucVu)) {
                JOptionPane.showMessageDialog(this, "Xóa chức vụ thành công!");
                controllerLoadData();
                lamMoiForm();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa chức vụ thất bại!");
            }
        }
    }

    private void lamMoiForm() {
        txtMaChucVu.setText("");
        txtTenChucVu.setText("");
        txtMoTa.setText("");
        txtCapBac.setText("");
        txtPhuCapMacDinh.setText("");
        txtQuyenHan.setText("");
        cboTrangThai.setSelectedIndex(0);
        txtTimKiem.setText("");
        table.clearSelection();
    }

    private void timKiem() {
        String keyword = txtTimKiem.getText().trim();
        List<ChucVuModel> list = keyword.isEmpty() ? controller.getAll() : controller.searchByName(keyword);
        setTableData(list);
    }

    private void fillFormFromSelectedRow() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        int maChucVu = (int) table.getValueAt(row, 0);
        ChucVuModel cv = controller.findById(maChucVu);
        if (cv != null) {
            txtMaChucVu.setText(String.valueOf(cv.getMaChucVu()));
            txtTenChucVu.setText(cv.getTenChucVu());
            txtMoTa.setText(cv.getMoTa());
            txtCapBac.setText(cv.getCapBac() == null ? "" : cv.getCapBac().toString());
            txtPhuCapMacDinh.setText(cv.getPhuCapMacDinh() == null ? "" : cv.getPhuCapMacDinh().toString());
            txtQuyenHan.setText(cv.getQuyenHan());
            cboTrangThai.setSelectedItem(cv.getTrangThai());
        }
    }
}
