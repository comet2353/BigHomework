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


    // ====================== 只需要改这里的图片路径 ======================
    //
    private final String FIRST_BG = "image/splash.png";   // 第一页背景
    private final String SECOND_BG = "image/menu.jpg";  // 第二页背景

    public SplashScreen() {
        // 窗口基础设置
        setTitle("启动界面");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setUndecorated(true); // 无边框

        // 初始化第一页
        initFirstPage();

    }

    // 第一页：背景 + 图片淡入淡出
    private void initFirstPage() {
        mainPanel = new BackgroundPanel(FIRST_BG);
        mainPanel.setLayout(new BorderLayout());

        // 改用自定义透明图片标签
        ImageIcon groupIcon = new ImageIcon("image/name.png");
        // 关键：使用 FadeImageLabel，不是原生 JLabel
        FadeImageLabel nameLabel = new FadeImageLabel(groupIcon);
        // 初始图片完全透明
        nameLabel.setAlpha(0.0f);

        mainPanel.add(nameLabel, BorderLayout.CENTER);

        // 先淡入，停留片刻再淡出切页
        javax.swing.Timer fadeInTimer = new javax.swing.Timer(20, e -> {
            float curAlpha = nameLabel.getAlpha() + 0.05f;
            if (curAlpha >= 1.0f) {
                curAlpha = 1.0f;
                ((javax.swing.Timer)e.getSource()).stop();
                // 淡入完成后，延时再执行淡出
                new javax.swing.Timer(2000, ev -> {
                    animateFadeOut(nameLabel, () -> switchToSecondPage());
                }).start();
            }
            nameLabel.setAlpha(curAlpha);
        });
        fadeInTimer.setRepeats(true);
        fadeInTimer.start();

        setContentPane(mainPanel);
    }

    /**
     * 支持图片淡入淡出的自定义标签
     * 专门用来显示图片，并可控制整体透明度
     */
    class FadeImageLabel extends JLabel {
        private float alpha = 1.0f; // 图片透明度 0~1  0完全透明，1完全不透明
        private Image image;

        public FadeImageLabel(ImageIcon icon) {
            super();
            this.image = icon.getImage();
            setHorizontalAlignment(SwingConstants.CENTER);
        }

        // 设置透明度 0.0 ~ 1.0
        public void setAlpha(float alpha) {
            this.alpha = Math.max(0.0f, Math.min(1.0f, alpha));
            repaint();
        }

        public float getAlpha() {
            return alpha;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            int x = (getWidth() - image.getWidth(null)) / 2;
            int y = (getHeight() - image.getHeight(null)) / 2;
            g2d.drawImage(image, x, y, null);
            g2d.dispose();
        }
    }

    // 针对 FadeImageLabel 的图片淡出动画
    private void animateFadeOut(FadeImageLabel comp, Runnable onFinish) {
        Timer timer = new Timer(20, null);
        timer.addActionListener(e -> {
            float curAlpha = comp.getAlpha() - 0.05f;
            if (curAlpha <= 0) {
                curAlpha = 0;
                timer.stop();
                onFinish.run();
            }
            comp.setAlpha(curAlpha);
        });
        timer.start();
    }

    // 切换到第二页：背景 + PLAY + READ 按钮
    private void switchToSecondPage() {
        isFirstPage = false;

        // 更换背景
        mainPanel = new BackgroundPanel(SECOND_BG);
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 30, 20, 30);
/*
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
    }*/
        // ====================== PLAY 按钮（图片版，150*50）======================
        JButton playBtn = new JButton();
        playBtn.setIcon(new ImageIcon("image/play.png"));
        playBtn.setPreferredSize(new Dimension(250, 100));
        playBtn.setFocusPainted(false);
        playBtn.setContentAreaFilled(false);
        playBtn.setBorderPainted(false);

        /*playBtn.addActionListener(e -> {
            dispose();
            new MainFrame().setVisible(true);
        });*///改成新的方法，先第三页，再主界面
        playBtn.addActionListener(e -> showThirdPageThenMain());

// ====================== READ 按钮（图片版，150*50）======================
        JButton readBtn = new JButton();
        readBtn.setIcon(new ImageIcon("image/read.png"));
        readBtn.setPreferredSize(new Dimension(250, 100));
        readBtn.setFocusPainted(false);
        readBtn.setContentAreaFilled(false);
        readBtn.setBorderPainted(false);

// ====================== 核心：强制右下角 ======================
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.SOUTHEAST;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

// --------- 这里改成 0，完全贴紧 ---------
        gbc.insets = new Insets(0, 0, 0, 30);

// 第一个按钮 PLAY（上面）
        mainPanel.add(playBtn, gbc);

// 第二个按钮 READ（下面）
        gbc.gridy = 2;
// 重点：给下面按钮也设置 0 间距
        gbc.insets = new Insets(0, 0, 5, 30);
        mainPanel.add(readBtn, gbc);

        setContentPane(mainPanel);
        revalidate();
        repaint();

        // 背景图片面板
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
    }//再增加一个第三页，主界面的显示就不会太突兀
    private void showThirdPageThenMain() {
        // 1. 创建第三页面板
        JPanel thirdPage = new JPanel();
        thirdPage.setBackground(Color.BLACK);
        thirdPage.setLayout(new GridBagLayout()); // 居中显示文字

        JLabel messageLabel = new JLabel("你得到了一份来自FSD的委托", JLabel.CENTER);
        messageLabel.setFont(new Font("微软雅黑", Font.BOLD, 28));
        messageLabel.setForeground(Color.WHITE);
        thirdPage.add(messageLabel);

        setContentPane(thirdPage);
        revalidate();
        repaint();

        // 2. 文字淡出动画（1秒后开始淡出，持续1秒）
        Timer fadeOutTimer = new Timer(30, null);
        final float[] alpha = {1.0f};
        fadeOutTimer.addActionListener(e -> {
            alpha[0] -= 0.03f;
            if (alpha[0] <= 0) {
                fadeOutTimer.stop();
                // 动画结束：关闭启动页，打开主界面
                dispose();
                new MainFrame().setVisible(true);
            } else {
                messageLabel.setForeground(new Color(255, 255, 255, (int)(alpha[0] * 255)));
                messageLabel.repaint();
            }
        });

        // 延迟1500毫秒后启动淡出动画（让用户看清文字）
        Timer startFadeTimer = new Timer(1500, e -> fadeOutTimer.start());
        startFadeTimer.setRepeats(false);
        startFadeTimer.start();

        fadeTimer = new Timer(20, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                opacity += 0.03f;
                if (opacity >= 1.0f) {
                    opacity = 1.0f;
                    fadeTimer.stop();
                }
                messageLabel.setForeground(new Color(255, 255, 255, (int) (opacity * 255)));
            }
        });
        fadeTimer.start();
    }


}
