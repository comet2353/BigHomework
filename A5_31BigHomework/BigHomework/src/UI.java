import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class UI {
    public static void main(String[] args) {
        // 确保UI在EDT线程运行
        SwingUtilities.invokeLater(MainFrame::new);
    }
}

class MainFrame {
    JFrame frame = new JFrame();
    DefaultTableModel tableModel;
    JTable dataTable;
    // 声明输入框引用（用于获取输入值）
    JTextField nameField, ageField, sexField, heightField;
    DAO dao = new DAO(); // 实例化DAO

    public MainFrame() {
        frame.setTitle("█ █ █ █");

        // 背景面板（补充实现）
        BackgroundPanel backgroundPanel = new BackgroundPanel("image/bg.jpg");
        frame.setContentPane(backgroundPanel);

        // 初始化面板
        JPanel westPanel = new JPanel();
        JPanel centerPanel = new JPanel();
        JPanel northPanel = new JPanel();
        westPanel.setOpaque(false);
        centerPanel.setOpaque(false);
        northPanel.setOpaque(false);

        westPanel.setPreferredSize(new Dimension(352, 572));
        northPanel.setPreferredSize(new Dimension(800, 28));
        frame.setLayout(new BorderLayout());
        backgroundPanel.add(westPanel, BorderLayout.WEST);
        backgroundPanel.add(centerPanel, BorderLayout.CENTER);
        backgroundPanel.add(northPanel, BorderLayout.NORTH);

        // 左侧面板：人物+文本
        JPanel chaPanel = new JPanel();
        JPanel contPanel = new JPanel();
        westPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        chaPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        contPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        westPanel.setLayout(new GridLayout(2, 1, 10, 10));
        BackgroundPanel textBG = new BackgroundPanel("image/textBG.png");
        BackgroundPanel peopleBG = new BackgroundPanel("image/peopleBG.png");
        chaPanel.add(peopleBG, BorderLayout.CENTER);

        westPanel.add(chaPanel);
        westPanel.add(contPanel);

        // 右侧面板：修改+按钮+数据库+答案
        JPanel modifyPanel = new JPanel();
        JPanel buttonPanel = new JPanel();
        JPanel DBPanel = new JPanel();
        JPanel ansPanel = new JPanel();
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 15));
        modifyPanel.setPreferredSize(new Dimension(433, 125));
        buttonPanel.setPreferredSize(new Dimension(433, 50));
        DBPanel.setPreferredSize(new Dimension(433, 197));
        ansPanel.setPreferredSize(new Dimension(433, 100));
        ansPanel.setBackground(Color.BLUE);
        centerPanel.setLayout(new FlowLayout());
        centerPanel.add(modifyPanel);
        centerPanel.add(buttonPanel);
        centerPanel.add(DBPanel);
        centerPanel.add(ansPanel);

        // 修改面板：标题+输入框
        JLabel infoMidifyLabel = new JLabel("学生信息添加/修改");
        modifyPanel.setLayout(new BorderLayout());
        infoMidifyLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 0));
        infoMidifyLabel.setFont(new Font("微软雅黑", Font.BOLD, 15));
        modifyPanel.add(infoMidifyLabel, BorderLayout.NORTH);

        // 输入框面板（修复：保存输入框引用）
        JPanel modifyArea = new JPanel();
        InputPanel nameInputPanel = new InputPanel("姓名：");
        InputPanel ageInputPanel = new InputPanel("年龄：");
        InputPanel sexInputPanel = new InputPanel("性别：");
        InputPanel heightInputPanel = new InputPanel("身高：");
        // 获取输入框引用
        nameField = nameInputPanel.getTextField();
        ageField = ageInputPanel.getTextField();
        sexField = sexInputPanel.getTextField();
        heightField = heightInputPanel.getTextField();

        modifyArea.setLayout(new GridLayout(2, 2, 5, 10));
        modifyArea.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 0));
        modifyArea.add(nameInputPanel);
        modifyArea.add(ageInputPanel);
        modifyArea.add(sexInputPanel);
        modifyArea.add(heightInputPanel);
        modifyPanel.add(modifyArea, BorderLayout.CENTER);

        // 按钮面板
        Btn addBtn = new Btn("添加学生");
        Btn modBtn = new Btn("修改学生");
        Btn delBtn = new Btn("删除学生");
        Btn queryBtn = new Btn("查询全部");
        buttonPanel.setOpaque(false);
        buttonPanel.add(addBtn);
        buttonPanel.add(modBtn);
        buttonPanel.add(delBtn);
        buttonPanel.add(queryBtn);

        // 数据库面板：搜索+表格
        DBPanel.setLayout(new BorderLayout());
        JPanel searchPanel = new JPanel();
        JLabel searchLabel = new JLabel("按姓名查询：");
        JTextField searchPart = new JTextField();
        JButton searchBtn = new JButton("查询");
        searchPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 0));
        searchBtn.setFont(new Font("Courier", Font.PLAIN, 13));
        searchBtn.setBackground(Color.WHITE);
        searchBtn.setPreferredSize(new Dimension(55, 20));
        searchBtn.setBorder(BorderFactory.createLineBorder(Color.gray, 1, true));
        searchPart.setPreferredSize(new Dimension(175, 25));
        DBPanel.add(searchPanel, BorderLayout.NORTH);
        // 修复：重复添加searchLabel问题
        searchPanel.add(searchLabel);
        searchPanel.add(searchPart);
        searchPanel.add(searchBtn);

        // 表格面板
        JPanel disPanel = new JPanel();
        String[] columnNames = {"编号", "姓名", "身高（m）", "性别", "年龄"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        dataTable = new JTable(tableModel);
        dataTable.setRowHeight(20);
        dataTable.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        dataTable.getTableHeader().setFont(new Font("微软雅黑", Font.BOLD, 13));
        dataTable.setPreferredScrollableViewportSize(new Dimension(400, 110));
        JScrollPane scrollPane = new JScrollPane(dataTable);
        Border titleBorder = BorderFactory.createTitledBorder("学生信息列表");
        Border emptyBorder = BorderFactory.createEmptyBorder(0, 10, 0, 10);
        Border compoundBorder = BorderFactory.createCompoundBorder(emptyBorder, titleBorder);
        disPanel.setBorder(compoundBorder);
        disPanel.add(scrollPane);
        DBPanel.add(disPanel, BorderLayout.CENTER);

        // 答案面板（核对姓名）
        ansPanel.setLayout(new FlowLayout());
        JLabel ansLabel = new JLabel("请输入目标姓名：");
        JTextField ansInput = new JTextField(20);
        JButton checkBtn = new JButton("确认核对");
        ansPanel.add(ansLabel);
        ansPanel.add(ansInput);
        ansPanel.add(checkBtn);

        // ===================== 事件绑定 =====================
        // 1. 查询全部
        queryBtn.addActionListener(e -> loadAllStudents());

        // 2. 按姓名查询
        searchBtn.addActionListener(e -> {
            String searchName = searchPart.getText().trim();
            if (searchName.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "请输入要查询的姓名。");
                return;
            }
            loadStudentByName(searchName);
        });

        // 3. 添加学生
        addBtn.addActionListener(e -> {
            try {
                String name = nameField.getText().trim();
                String ageStr = ageField.getText().trim();
                String sex = sexField.getText().trim();
                String heightStr = heightField.getText().trim();

                // 非空校验
                if (name.isEmpty() || ageStr.isEmpty() || sex.isEmpty() || heightStr.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "请输入完整的学生信息！");
                    return;
                }

                // 类型转换
                int age = Integer.parseInt(ageStr);
                double height = Double.parseDouble(heightStr);

                // 调用DAO添加
                boolean success = dao.addStudent(name, age, sex, height);
                if (success) {
                    JOptionPane.showMessageDialog(frame, "添加成功！");
                    loadAllStudents(); // 刷新表格
                    clearInputFields(); // 清空输入框
                } else {
                    JOptionPane.showMessageDialog(frame, "添加失败！");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "年龄请输入整数，身高请输入数字！");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "添加异常！");
            }
        });

        // 4. 修改学生
        modBtn.addActionListener(e -> {
            try {
                String name = nameField.getText().trim();
                String ageStr = ageField.getText().trim();
                String sex = sexField.getText().trim();
                String heightStr = heightField.getText().trim();

                if (name.isEmpty() || ageStr.isEmpty() || sex.isEmpty() || heightStr.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "请输入完整的学生信息！");
                    return;
                }

                int age = Integer.parseInt(ageStr);
                double height = Double.parseDouble(heightStr);

                boolean success = dao.updateStudent(name, age, sex, height);
                if (success) {
                    JOptionPane.showMessageDialog(frame, "修改成功！");
                    loadAllStudents();
                    clearInputFields();
                } else {
                    JOptionPane.showMessageDialog(frame, "修改失败（姓名不存在？）！");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "年龄请输入整数，身高请输入数字！");
            }
        });

        // 5. 删除学生
        delBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "请输入要删除的学生姓名！");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(frame, "确认删除【" + name + "】吗？", "删除确认", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                boolean success = dao.deleteStudent(name);
                if (success) {
                    JOptionPane.showMessageDialog(frame, "删除成功！");
                    loadAllStudents();
                    clearInputFields();
                } else {
                    JOptionPane.showMessageDialog(frame, "删除失败（姓名不存在？）！");
                }
            }
        });

        // 6. 姓名核对
        checkBtn.addActionListener(e -> {
            String inputName = ansInput.getText().trim();
            if (inputName.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "请先输入姓名！", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 中文校验
            if (!inputName.matches("^[\\u4e00-\\u9fa5]+$")) {
                JOptionPane.showMessageDialog(frame, "只能输入中文姓名！", "校验失败", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 数据库校验
            boolean exists = dao.checkNameExist(inputName);
            if (exists) {
                JOptionPane.showMessageDialog(frame, "核对正确！", "提示", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame, "该人员不在档案库中！", "核对失败", JOptionPane.ERROR_MESSAGE);
            }
        });

        // 窗口设置
        frame.setBounds(500, 100, 800, 600);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // 加载所有学生到表格
    private void loadAllStudents() {
        tableModel.setRowCount(0); // 清空表格
        List<Object[]> students = dao.getAllStudents();
        for (Object[] row : students) {
            tableModel.addRow(row);
        }
    }

    // 按姓名加载学生到表格
    private void loadStudentByName(String name) {
        tableModel.setRowCount(0);
        List<Object[]> students = dao.searchStudent(name);
        if (students.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "未找到【" + name + "】的信息！");
            return;
        }
        for (Object[] row : students) {
            tableModel.addRow(row);
        }
    }

    // 清空输入框
    private void clearInputFields() {
        nameField.setText("");
        ageField.setText("");
        sexField.setText("");
        heightField.setText("");
    }
}


// 输入面板（修改：暴露输入框）
class InputPanel extends JPanel {
    private JTextField textField;

    InputPanel(String name) {
        setLayout(new BorderLayout());
        add(new JLabel(name, JLabel.LEFT), BorderLayout.WEST);
        textField = new JTextField("");
        textField.setEditable(true);
        add(textField, BorderLayout.CENTER);
    }

    public JTextField getTextField() {
        return textField;
    }
}

// 自定义按钮
class Btn extends JButton {
    Btn(String name) {
        setText(name);
        setFont(new Font("Courier", Font.PLAIN, 13));
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(75, 25));
        setBorder(BorderFactory.createLineBorder(Color.gray, 1, true));
    }
}

// 背景面板（补充缺失类）
class BackgroundPanel extends JPanel {
    private Image backgroundImage;

    public BackgroundPanel(String imagePath) {
        try {
            // 加载图片（支持绝对/相对路径）
            backgroundImage = Toolkit.getDefaultToolkit().getImage(imagePath);
        } catch (Exception e) {
            // 图片加载失败时不报错，仅打印日志
            System.out.println("背景图片加载失败：" + imagePath);
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            // 绘制背景图（适配面板大小）
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}