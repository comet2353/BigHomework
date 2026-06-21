import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Area;

//新手指引部分的画布设置，包括特定区域挖空，绘制半透明黑色幕布等
public class GlassPane extends JComponent {
    private Rectangle cutout;//挖空区域
    private boolean flag = false;//是否展示幕布效果，初始为false

    //指引步骤控制
    private int guideStep = 0; // 0=未激活, 1=输入阶段, 2=点击按钮阶段
    private String guideText = ""; // 当前提示文字
    private JTextField targetTextField; // 目标输入框引用
    private JButton targetButton; // 目标按钮引用
    private Runnable onComplete; // 完成回调
    // 用于移除临时监听器
    private ActionListener tempButtonListener;

    public GlassPane() {
        // 关键：不获取焦点，让焦点能到下层组件
        setFocusable(false);
        // 关键：背景透明
        setOpaque(false);
    }

    ///changeflag
    public void changeFlag(){
        this.flag = !flag;
    }

    //获取cutout
    public Rectangle getCutout(){
        return this.cutout;
    }

    public void setCutout(Rectangle cutout) {
        this.cutout = cutout;
        repaint();
    }//setCutout

    // 启动新手指引
    public void startGuide(JTextField searchField, JButton searchBtn, Runnable completeCallback) {
        this.targetTextField = searchField;
        this.targetButton = searchBtn;
        this.onComplete = completeCallback;
        this.guideStep = 1;
        this.guideText = "请输入：末日的仓鼠之王";

        System.out.println("[GlassPane] 开始指引，第一步");
        // 设置第一步的挖空区域为搜索框
        updateCutoutForCurrentStep();

        // 显示幕布
        setVisible(true);

        // 清空搜索框
        if (targetTextField != null) {
            targetTextField.setText("");
        }
        setupInputListener();
        // 确保搜索框能获得焦点
        SwingUtilities.invokeLater(() -> {
            targetTextField.requestFocusInWindow();
        });
    }

    // 新增：根据当前步骤更新挖空区域
    private void updateCutoutForCurrentStep() {
        if (guideStep == 1 && targetTextField != null) {
            Rectangle bounds = SwingUtilities.convertRectangle(
                    targetTextField.getParent(),
                    targetTextField.getBounds(),
                    this
            );
            setCutout(bounds);
            System.out.println("[GlassPane] 第一步挖空区域: " + bounds);

            // 第一步：确保搜索框可见且可操作
            // 不需要特殊处理，contains 返回 false 让事件穿透
        } else if (guideStep == 2 && targetButton != null) {
            Rectangle bounds = SwingUtilities.convertRectangle(
                    targetButton.getParent(),
                    targetButton.getBounds(),
                    this
            );
            setCutout(bounds);
            System.out.println("[GlassPane] 第二步挖空区域: " + bounds);

            // ===== 关键修改：直接在按钮上添加监听器 =====
            addButtonClickListener();

        }
    }
    // 直接在目标按钮上添加监听器
    private void addButtonClickListener() {
        if (targetButton == null) return;

        // 移除旧的监听器（如果有）
        if (tempButtonListener != null) {
            targetButton.removeActionListener(tempButtonListener);
        }

        tempButtonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("[GlassPane] >>> 按钮被点击，完成指引");
                // 移除自己
                targetButton.removeActionListener(this);
                tempButtonListener = null;
                // 完成指引
                finishGuide();
            }
        };
        // 在现有监听器之前添加（保证先执行我们的完成逻辑）
        ActionListener[] existingListeners = targetButton.getActionListeners();
        for (ActionListener listener : existingListeners) {
            targetButton.removeActionListener(listener);
        }

        // 先添加我们的监听器
        targetButton.addActionListener(tempButtonListener);

        // 再添加原有的监听器
        for (ActionListener listener : existingListeners) {
            targetButton.addActionListener(listener);
        }

        System.out.println("[GlassPane] 已添加按钮监听器");
    }


    private void setupInputListener() {
        if (targetTextField == null) return;

        // 使用 DocumentListener 实时监听输入
        targetTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                checkInput();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                checkInput();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                checkInput();
            }
        });

        System.out.println("[GlassPane] 已添加输入监听器");
    }

    // 统一的输入检查方法
    private void checkInput() {
        if (guideStep != 1 || targetTextField == null) return;

        String input = targetTextField.getText().trim();
        System.out.println("[GlassPane] 当前输入: '" + input + "'");

        if ("末日的仓鼠之王".equals(input)) {
            System.out.println("[GlassPane] >>> 输入正确，切换到第二步");
            guideStep = 2;
            guideText = "点击【查询】按钮";
            updateCutoutForCurrentStep();
            repaint();
        }
    }

    // 完成指引
    private void finishGuide() {
        System.out.println("[GlassPane] >>> finishGuide 被调用");

        // 清理临时监听器
        if (tempButtonListener != null && targetButton != null) {
            targetButton.removeActionListener(tempButtonListener);
            tempButtonListener = null;
        }

        guideStep = 0;
        guideText = "";
        cutout = null; // 清除挖空区域
        setVisible(false);
        setFocusable(false);
        repaint();

        if (onComplete != null) {
            onComplete.run();
        }
    }



    @Override
    protected void paintComponent(Graphics g) {
        if (!isVisible()) return;
        //复制一份原始画笔的副本，传给g2
        Graphics2D g2 = (Graphics2D) g.create();

        // 启用抗锯齿，让边框更平滑
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (cutout != null) {
            // ===== 方案一：使用 Area 减法（推荐，不会变黑） =====
            Shape fullArea = new Rectangle(0, 0, getWidth(), getHeight());
            Area maskArea = new Area(fullArea);
            maskArea.subtract(new Area(cutout));

            // 绘制遮罩区域
            g2.setColor(new Color(0, 0, 0, 150));
            g2.fill(maskArea);

            // 绘制黄色高亮边框
            g2.setColor(new Color(255, 255, 100, 220));
            g2.setStroke(new BasicStroke(3));
            g2.drawRect(cutout.x, cutout.y, cutout.width, cutout.height);
        } else {
            g2.setColor(new Color(0, 0, 0, 150));
            g2.fillRect(0, 0, getWidth(), getHeight());
        }


        // 绘制提示文字
        if (guideStep > 0 && !guideText.isEmpty() && cutout != null) {
            g2.setFont(new Font("微软雅黑", Font.BOLD, 18));

            // 计算文字位置（在挖空区域上方或下方）
            FontMetrics fm = g2.getFontMetrics();
            int textWidth = fm.stringWidth(guideText);
            int textHeight = fm.getHeight();

            int x = cutout.x + (cutout.width - textWidth) / 2;
            int y = cutout.y - 20; // 在挖空区域上方

            // 如果上方空间不够，放到下方
            if (y < textHeight + 10) {
                y = cutout.y + cutout.height + textHeight + 10;
            }

            // 绘制文字阴影
            g2.setColor(new Color(0, 0, 0, 180));
            g2.drawString(guideText, x + 2, y + 2);

            // 绘制文字
            g2.setColor(Color.WHITE);
            g2.drawString(guideText, x, y);
        }

        //释放 Graphics2D 对象占用的系统资源
        g2.dispose();

    }//paintComponent

    @Override
    //点击挖空区域时，事件穿透到下层组件
    //若若位于指定区域内，则返回false，则鼠标事件会穿透玻璃，落到搜索框上
    //否则调用父类的判断，正常拦截非指定区域的鼠标，不让他们点到下面的按钮。
    public boolean contains(int x, int y) {
        // ===== 第二步时，按钮区域不穿透 =====
        if (cutout != null && cutout.contains(x, y)) {
            // 始终返回 false，让事件穿透到下层组件
            // 我们通过其他方式（DocumentListener、直接给按钮加监听器）来处理交互
            return false;
        }
        return super.contains(x, y);
    }


//    // 处理鼠标点击事件（用于第二步点击按钮）
//    @Override
//    protected void processMouseEvent(MouseEvent e) {
//        // 调试：打印所有鼠标事件
//        System.out.println("GlassPane 收到鼠标事件: " + e.getID() +
//                " 位置: (" + e.getX() + "," + e.getY() + ")" +
//                " guideStep=" + guideStep +
//                " cutout=" + cutout);
//
//        if (guideStep == 2 && cutout != null && cutout.contains(e.getX(), e.getY())) {
//            System.out.println(">>> 进入第二步点击处理！");
//            if (e.getID() == MouseEvent.MOUSE_PRESSED) {
//                System.out.println(">>> 触发 finishGuide");
//                // 先完成指引（隐藏幕布）
//                finishGuide();
//
//                // 然后在EDT中执行按钮点击
//                // 使用 invokeLater 确保幕布先隐藏，再执行查询
//                SwingUtilities.invokeLater(() -> {
//                    System.out.println(">>> 触发 doClick");
//                    targetButton.doClick();
//                });
//            }
//        }
//        super.processMouseEvent(e);
//    }
}
