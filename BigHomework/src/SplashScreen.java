import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// 独立的启动动画界面类
public class SplashScreen extends JFrame {
    private JPanel mainPanel;
    private JLabel nameLabel;
    private Timer fadeTimer;
    private float opacity = 0.0f;
    private boolean isFirstPage = true;

    // ====================== 你只需要改这里的图片路径 ======================
    private final String FIRST_BG = "image/splash.png";   // 第一页背景
    private final String SECOND_BG = "image/menu.png";  // 第二页背景

    public SplashScreen() {
        // 窗口基础设置
        setTitle("启动界面");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setUndecorated(true); // 无边框

        // 初始化第一页
        initFirstPage();

        // 小组名淡入动画
        startFadeAnimation();
    }

    // 第一页：背景 + 淡入小组名 groupIron
    private void initFirstPage() {
        mainPanel = new BackgroundPanel(FIRST_BG);
        mainPanel.setLayout(new BorderLayout());

        // 小组名字
        nameLabel = new JLabel("groupIron", SwingConstants.CENTER);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 50));
        nameLabel.setForeground(new Color(1, 1, 1, 0)); // 初始透明
        mainPanel.add(nameLabel, BorderLayout.CENTER);

        // 点击任意位置切换到第二页
        mainPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (isFirstPage) {
                    switchToSecondPage();
                }
            }
        });

        setContentPane(mainPanel);
    }

    // 文字淡入动画
    private void startFadeAnimation() {
        fadeTimer = new Timer(20, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                opacity += 0.03f;
                if (opacity >= 1.0f) {
                    opacity = 1.0f;
                    fadeTimer.stop();
                }
                nameLabel.setForeground(new Color(255, 255, 255, (int) (opacity * 255)));
            }
        });
        fadeTimer.start();
    }

    // 切换到第二页：背景 + PLAY + READ 按钮
    private void switchToSecondPage() {
        isFirstPage = false;

        // 更换背景
        mainPanel = new BackgroundPanel(SECOND_BG);
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 30, 20, 30);

        // ====================== PLAY 按钮（黑色）======================
        JButton playBtn = new JButton("PLAY");
        playBtn.setFont(new Font("Arial", Font.BOLD, 22));
        playBtn.setForeground(Color.BLACK);
        playBtn.setFocusPainted(false);
        playBtn.setContentAreaFilled(false);
        playBtn.setBorderPainted(false);

        // 点击 PLAY → 打开主界面
        playBtn.addActionListener(e -> {
            dispose(); // 关闭启动页
            new MainFrame().setVisible(true); // 打开你的主界面
        });

        // ====================== READ 按钮（黑色）======================
        JButton readBtn = new JButton("READ");
        readBtn.setFont(new Font("Arial", Font.BOLD, 22));
        readBtn.setForeground(Color.BLACK);
        readBtn.setFocusPainted(false);
        readBtn.setContentAreaFilled(false);
        readBtn.setBorderPainted(false);

        // 布局按钮
        gbc.gridx = 0;
        mainPanel.add(playBtn, gbc);
        gbc.gridx = 1;
        mainPanel.add(readBtn, gbc);

        setContentPane(mainPanel);
        revalidate();
        repaint();
    }

    // 背景图片面板（你项目里有 BackgroundPanel 就用你自己的）
    class BackgroundPanel extends JPanel {
        private Image image;

        public BackgroundPanel(String imagePath) {
            try {
                image = new ImageIcon(imagePath).getImage();
            } catch (Exception e) {
                System.out.println("背景图加载失败：" + imagePath);
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (image != null) {
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }
}
