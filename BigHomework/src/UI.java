import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class UI {
    public static void main(String[] args) {
        // 确保UI在EDT线程运行
        //任务提交到 事件分发线程（ EDT） 的队列中
        //将UI的创建/更改集中在单一线程中
        //防止因对同一个组件的多次更改集中在同一时间时导致的崩溃
        //MainFrame::new
        //等价于 () -> new MainFrame()
        SwingUtilities.invokeLater(MainFrame::new);
    }
}

class MainFrame{
    JFrame frame = new JFrame();
    // 表格模型（用于动态更新数据）
    DefaultTableModel tableModel;
    JTable dataTable;
    // 声明输入框引用（用于获取输入值）
    JTextField nameField, ageField, sexField, heightField;
    DAO dao = new DAO(); // 实例化DAO
    
    // 翻页相关组件
    PageManager pageManager;
    JTextArea textArea;
    JLabel imageLabel;
    JLabel pageLabel;
    Btn prevBtn;
    Btn nextBtn;

    public MainFrame() {
        frame.setTitle("█ █ █ █");


        //窗口背景设置
        BackgroundPanel backgroundPanel = new BackgroundPanel("image/bg.jpg");
        frame.setContentPane(backgroundPanel);


        //设置底层panel
        JPanel westPanel = new JPanel();//左panel
        JPanel centerPanel = new JPanel();//右panel
        JPanel northPanel = new JPanel();//上panel
        //设置透明度
        westPanel.setOpaque(false);
        centerPanel.setOpaque(false);
        northPanel.setOpaque(false);

        westPanel.setPreferredSize(new Dimension(352,572));//尺寸刚好贴合右侧显示区
        //center部分不做绝对尺寸限制(448,572)
        northPanel.setPreferredSize(new Dimension(800,28));//尺寸刚好贴合顶部黑色菜单
        frame.setLayout(new BorderLayout());//边界布局
        backgroundPanel.add(westPanel,BorderLayout.WEST);
        backgroundPanel.add(centerPanel,BorderLayout.CENTER);
        backgroundPanel.add(northPanel,BorderLayout.NORTH);

        // 初始化页面管理器
        pageManager = new PageManager();

        //设置左部分panel：人物显示区域panel + 文本显示panel
        BackgroundPanel chaPanel = new BackgroundPanel("image/peopleBG.jpg");
        BackgroundPanel contPanel = new BackgroundPanel("image/textBG.png");
        //设置westpanel的空边距，
        westPanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        //panel颜色设置
        chaPanel.setBackground(Color.WHITE);
        contPanel.setBackground(Color.WHITE);
        //设置chapanel和contpanel的边界颜色
        chaPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK,2));//边框颜色，像素
        contPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK,2));//边框颜色，像素
        westPanel.setLayout(new GridLayout(2,1,10,10));//网格布局(行数，列数，水平间距，垂直间距)

        // 图片显示区域（在文本框上方）
        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(312, 150));
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        
        // 加载第一张图片
        updateImageDisplay();

        chaPanel.setLayout(new BorderLayout());
        chaPanel.add(imageLabel, BorderLayout.NORTH);

        // 文本显示区域（使用JTextArea支持多行文本）
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBackground(new Color(255, 255, 255, 200));
        textArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // 设置初始文本
        updateTextDisplay();

        JScrollPane textScrollPane = new JScrollPane(textArea);
        textScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        contPanel.setLayout(new BorderLayout());
        contPanel.add(textScrollPane, BorderLayout.CENTER);

        // 页码显示
        pageLabel = new JLabel(pageManager.getPageDisplay(), SwingConstants.CENTER);
        pageLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        pageLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        contPanel.add(pageLabel, BorderLayout.SOUTH);

        // 翻页按钮面板@@@@@@@@@@
        JPanel flipButtonPanel = new JPanel();
        flipButtonPanel.setOpaque(false);
        flipButtonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
        
        prevBtn = new Btn("上一页");
        nextBtn = new Btn("下一页");
        prevBtn.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        nextBtn.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        prevBtn.setPreferredSize(new Dimension(80, 30));
        nextBtn.setPreferredSize(new Dimension(80, 30));
        
        flipButtonPanel.add(prevBtn);
        
        // 页码显示（在两个按钮中间）
        pageLabel = new JLabel(pageManager.getPageDisplay(), SwingConstants.CENTER);
        pageLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        pageLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        flipButtonPanel.add(pageLabel);
        
        flipButtonPanel.add(nextBtn);
        
        contPanel.add(flipButtonPanel, BorderLayout.SOUTH);

        // 为文本区域添加鼠标点击事件
        textArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // 如果当前页小于总页数（如1/2, 2/4），先翻到下一页
                if (pageManager.getCurrentPageNumber() < pageManager.getTotalPages()) {
                    pageManager.nextPage();
                    updateTextDisplay();
                    updateImageDisplay();
                    updatePageLabel();
                    updateButtonStates();
                } 
                // 如果当前页等于总页数，尝试扩展总页数
                else if (pageManager.expandTotalPages()) {
                    updateTextDisplay();
                    updateImageDisplay();
                    updatePageLabel();
                    updateButtonStates();
                } else {
                    // 如果已经是最后一页，提示用户
                    JOptionPane.showMessageDialog(frame, "已经是最后一页了！");
                }
            }
        });

        // 上一页按钮事件
        prevBtn.addActionListener(e -> {
            if (pageManager.previousPage()) {
                updateTextDisplay();
                updateImageDisplay();
                updatePageLabel();
                updateButtonStates();
            }
        });

        // 下一页按钮事件
        nextBtn.addActionListener(e -> {
            if (pageManager.nextPage()) {
                updateTextDisplay();
                updateImageDisplay();
                updatePageLabel();
                updateButtonStates();
            }
        });

        westPanel.add(chaPanel);
        westPanel.add(contPanel);


        //右部分panel：个人信息修改panel + 按钮panel + 数据库信息显示panel + 答案填写panel
        //整体布局：边界布局
        JPanel modifyPanel = new JPanel();//个人信息修改panel
        JPanel buttonPanel = new JPanel();//按钮panel
        JPanel DBPanel = new JPanel();//数据库信息显示panel
        JPanel ansPanel = new JPanel();//答案填写panel
        //设置centerPanel的边界
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20,0,20,15));

        //设置各区块的绝对尺寸
        modifyPanel.setPreferredSize(new Dimension(433,125));
        buttonPanel.setPreferredSize(new Dimension(433,50));
        DBPanel.setPreferredSize(new Dimension(433,197));
        ansPanel.setPreferredSize(new Dimension(433,100));

        //ansPanel.setBackground(Color.BLUE);

        centerPanel.setLayout(new FlowLayout());
        centerPanel.add(modifyPanel);
        centerPanel.add(buttonPanel);
        centerPanel.add(DBPanel);
        centerPanel.add(ansPanel);



        //modifyPanel个人信息修改panel：标题 + 4种信息的修改框（2*2）
        //标题区域
        JLabel infoMidifyLabel = new JLabel("学生信息添加/修改");

        modifyPanel.setLayout(new BorderLayout());//设置整个修改框的布局
        //标题的边距
        infoMidifyLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 0));
        //字体设置
        infoMidifyLabel.setFont(new Font("微软雅黑",Font.BOLD,15));
        modifyPanel.add(infoMidifyLabel,BorderLayout.NORTH);

        //信息添加/修稿区：含有4个大小相同的部分，用网格布局2*2
        JPanel modifyArea = new JPanel();//外部框
        //输入框
        InputPanel nameInputPanel = new InputPanel("姓名：");//姓名框
        InputPanel ageInputPanel = new InputPanel("年龄：");//年龄框
        InputPanel sexInputPanel = new InputPanel("性别：");//性别框
        InputPanel heightInputPanel = new InputPanel("身高：");//身高框

        // 获取输入框中内容
        nameField = nameInputPanel.getTextField();
        ageField = ageInputPanel.getTextField();
        sexField = sexInputPanel.getTextField();
        heightField = heightInputPanel.getTextField();

        //设置modifyArea布局：网格布局
        modifyArea.setLayout(new GridLayout(2,2,5,10));
        modifyArea.setBorder(BorderFactory.createEmptyBorder(5,10,5,0));

        modifyArea.add(nameInputPanel);
        modifyArea.add(ageInputPanel);
        modifyArea.add(sexInputPanel);
        modifyArea.add(heightInputPanel);
        modifyPanel.add(modifyArea,BorderLayout.CENTER);//将modifyArea加入到modifyPanel中



        //按钮区域（buttonPanel）（透明底）:4个按钮，浮动布局
        Btn addBtn = new Btn("添加学生");
        Btn modBtn = new Btn("修改学生");
        Btn delBtn = new Btn("删除学生");
        Btn queryBtn = new Btn("查询全部");
        //将buttonPanel区域背景透明
        buttonPanel.setOpaque(false);
        buttonPanel.add(addBtn);
        buttonPanel.add(modBtn);
        buttonPanel.add(delBtn);
        buttonPanel.add(queryBtn);



        //DBPanel：数据库信息显示区域（使用JTable显示数据）：搜索框 + 数据显示区域
        DBPanel.setLayout(new BorderLayout());
        //搜索区域
        JPanel searchPanel = new JPanel();
        JLabel searchLabel = new JLabel("按姓名查询：");
        JTextField searchPart = new JTextField();
        JButton searchBtn = new JButton("查询");

        //searchPanel布局为流式布局，左对齐，垂直间距5，水平间距5
        searchPanel.setLayout(new FlowLayout(FlowLayout.LEFT,5,5));
        //searchPanel的边距
        searchPanel.setBorder(BorderFactory.createEmptyBorder(0,3,0,0));

        //查询按钮设置
        searchBtn.setFont(new java.awt.Font("Courier",Font.PLAIN,13));//设置Jbutton字体大小 以及风格
        searchBtn.setBackground(Color.WHITE);
        searchBtn.setPreferredSize(new Dimension(55,20));
        searchBtn.setBorder(BorderFactory.createLineBorder(Color.gray,1,true));

        //搜索框设置
        searchPart.setPreferredSize(new Dimension(175,25));

        DBPanel.add(searchPanel,BorderLayout.NORTH);
        searchPanel.add(searchLabel);
        searchPanel.add(searchLabel);
        searchPanel.add(searchPart);
        searchPanel.add(searchBtn);



        //数据库显示区域
        JPanel disPanel = new JPanel();

        // 创建表格模型
        String[] columnNames = {"编号", "姓名", "身高（m）","性别", "年龄"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 设置表格不可编辑
            }
        };

        dataTable = new JTable(tableModel);
        dataTable.setRowHeight(20);
        dataTable.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        dataTable.getTableHeader().setFont(new Font("微软雅黑", Font.BOLD, 13));
        // 设置表格的首选大小
        dataTable.setPreferredScrollableViewportSize(new Dimension(400, 110));
        //滚动设置
        JScrollPane scrollPane = new JScrollPane(dataTable);
        // 创建复合边界：内层是标题边界，外层是空边界
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
        Btn checkBtn = new Btn("确认核对");
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


        frame.setBounds(500, 100, 800, 600);//窗口大小
        frame.setLocationRelativeTo(null);//窗口居中
        frame.setResizable(false);//用户不可调整窗口大小
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // 更新文本显示
    private void updateTextDisplay() {
        textArea.setText(pageManager.getCurrentText());
    }

    // 更新图片显示
    private void updateImageDisplay() {
        String imagePath = pageManager.getCurrentImagePath();
        if (imagePath != null) {
            ImageIcon icon = new ImageIcon(imagePath);
            if (icon.getIconWidth() > 0) { // 图片加载成功
                // 缩放图片以适应标签大小
                Image scaledImage = icon.getImage().getScaledInstance(
                    300, 140, Image.SCALE_SMOOTH
                );
                imageLabel.setIcon(new ImageIcon(scaledImage));
            } else {
                imageLabel.setIcon(null);
                imageLabel.setText("图片加载中...");
            }
        } else {
            imageLabel.setIcon(null);
            imageLabel.setText("暂无图片");
        }
    }

    // 更新页码显示
    private void updatePageLabel() {
        pageLabel.setText(pageManager.getPageDisplay());
    }

    // 更新按钮状态
    private void updateButtonStates() {
        prevBtn.setEnabled(pageManager.hasPrevious());
        nextBtn.setEnabled(pageManager.hasNext());
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


//信息添加/修改部分1/4尺寸设置
class InputPanel extends JPanel{
    private JTextField textField;

    InputPanel(String name){
        setLayout(new BorderLayout());
        add(new JLabel(name,JLabel.LEFT),BorderLayout.WEST);
        textField = new JTextField("");
        textField.setEditable(true);
        add(textField,BorderLayout.CENTER);

    }
    //获取输入框内容
    public JTextField getTextField() {
        return textField;
    }
}

//自定义按钮尺寸设置
class Btn extends JButton{

    Btn(String name){
        //按钮细节设置
        setText(name);
        setFont(new java.awt.Font("Courier",Font.PLAIN,13));//设置Jbutton字体大小 以及风格
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(75,25));
        setBorder(BorderFactory.createLineBorder(Color.gray,1,true));
    }


}


