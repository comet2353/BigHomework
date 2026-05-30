import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class UI {
    public static void main(String[] args) {
        MainFrame mainframe = new MainFrame();
    }
}

class MainFrame{
    JFrame frame = new JFrame();
    // 表格模型（用于动态更新数据）
    DefaultTableModel tableModel;
    JTable dataTable;

    public MainFrame() {
        frame.setTitle("█ █ █ █");


        //窗口背景设置
        BackgroundPanel backgroundPanel = new BackgroundPanel("image/background.jpg");
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


        //设置左部分panel：人物显示区域panel + 文本显示panel
        JPanel chaPanel = new JPanel();
        JPanel contPanel = new JPanel();
        //设置westpanel的空边距，
        westPanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        //panel颜色设置
        chaPanel.setBackground(Color.WHITE);
        contPanel.setBackground(Color.WHITE);
        //设置chapanel和contpanel的边界颜色
        chaPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK,2));//边框颜色，像素
        contPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK,2));//边框颜色，像素
        westPanel.setLayout(new GridLayout(2,1,10,10));//网格布局(行数，列数，水平间距，垂直间距)
        //文本显示panel背景图
        BackgroundPanel textBG = new BackgroundPanel("image/textBG.png");
        contPanel.setContentPane(textBG);


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

        ansPanel.setBackground(Color.BLUE);

        centerPanel.setLayout(new FlowLayout());
        centerPanel.add(modifyPanel);
        centerPanel.add(buttonPanel);
        centerPanel.add(DBPanel);
        centerPanel.add(ansPanel);//右下角面板
        // 1. 给右下角面板设置布局：流式布局，组件从左到右排列
        ansPanel.setLayout(new FlowLayout());

        // 2. 创建文字标签：提示用户输入姓名
        JLabel ansLabel = new JLabel("请输入目标姓名：");
        // 3. 创建输入框：用户在这里输入内容，宽度20个字符
        JTextField ansInput = new JTextField(20);
        // 4. 创建按钮：点击这个按钮，开始校验输入内容
        JButton checkBtn = new JButton("确认核对");

        // 5. 把 标签、输入框、按钮 依次添加到右下角面板上
        ansPanel.add(ansLabel);
        ansPanel.add(ansInput);
        ansPanel.add(checkBtn);

        // 6. 给【确认核对】按钮绑定点击事件：点击后执行校验逻辑
        checkBtn.addActionListener(e -> {
        // 第一步：获取输入框里的内容，trim() 去掉首尾多余空格
        String inputName = ansInput.getText().trim();

        //判断输入框是否为空
        if(inputName.isEmpty()){
        // 弹出提示窗口：提醒用户必须输入内容，JOptionPane是专门用来做弹窗的工具
        JOptionPane.showMessageDialog(frame, "请先输入姓名！", "提示", JOptionPane.WARNING_MESSAGE);
        return; // 终止当前代码，不再往下执行
            }

         // 校验2：格式校验，只允许纯中文，禁止数字、字母、符号
         if (!inputName.matches("^[\\u4e00-\\u9fa5]+$")) {
         // 格式不符合要求，弹出错误窗口
         JOptionPane.showMessageDialog(frame, "输入不正确！只能输入中文姓名，不能包含数字、字母和符号", "校验失败", JOptionPane.ERROR_MESSAGE // 弹窗显示红色错误图标);
         return;
            }

          // 校验3：查询数据库，判断输入的名字是否存在
          // 调用DAO类的方法，去数据库查询该姓名是否存在
          boolean nameIsExist = DAO.checkNameExist(inputName);
          if(!nameIsExist){
          JOptionPane.showMessageDialog(frame, "输入不正确！该人员不在档案库中", "核对失败", JOptionPane.ERROR_MESSAGE);
          return;
            }
          // 所有校验全部通过，输入内容完全正确
          JOptionPane.showMessageDialog(frame, "核对正确！", "提示", JOptionPane.INFORMATION_MESSAGE);});

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
        InputPanel namePanel = new InputPanel("姓名：");//姓名框
        InputPanel agePanel = new InputPanel("年龄：");//年龄框
        InputPanel sexPanel = new InputPanel("性别：");//性别框
        InputPanel heightPanel = new InputPanel("身高：");//身高框
        //设置modifyArea布局：网格布局
        modifyArea.setLayout(new GridLayout(2,2,5,10));
        modifyArea.setBorder(BorderFactory.createEmptyBorder(5,10,5,0));

        modifyArea.add(namePanel);
        modifyArea.add(agePanel);
        modifyArea.add(sexPanel);
        modifyArea.add(heightPanel);
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

        // 添加按钮事件监听器

        // 查询全部按钮
//        queryBtn.addActionListener(e -> {
//            loadAllStudents();
//        });
//        //按姓名查询
//        searchBtn.addActionListener(e->{
//            String searchName = searchPart.getText().trim();//获取用户输入的搜索信息
//            //若输入为空
//            if(searchName.isEmpty() ){
//                JOptionPane.showMessageDialog(frame,"请输入要查询的姓名。");
//                return;
//            }
//            searchStudentByName(searchName);//调用查询方法
//        });
//
//        //增加学生信息
//        addBtn.addActionListener(e->{
//            String name = nameField.getText().trim();
//            String heightText = heightField.getText().trim();
//            String ageText = ageField.getText().trim();
//            String sex = sexField.getText().trim();
//
//            if(name.isEmpty() || heightText.isEmpty() || ageText.isEmpty() || sex.isEmpty())
//                JOptionPane.showMessageDialog(frame,"请输入完整的学生信息。");
//            return;
//        }//赋值后，将原本数据类型与
//        try{
//
//        }
//        );


        frame.setBounds(500, 100, 800, 600);//窗口大小
        frame.setLocationRelativeTo(null);//窗口居中
        frame.setResizable(false);//用户不可调整窗口大小
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
//信息添加/修稿部分1/4尺寸设置
class InputPanel extends JPanel{

    InputPanel(String name){
        setLayout(new BorderLayout());
        add(new JLabel(name,JLabel.LEFT),BorderLayout.WEST);
        JTextField nameField = new JTextField("");
        nameField.setEditable(true);
        add(nameField,BorderLayout.CENTER);

    }
}

//按钮尺寸设置
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


