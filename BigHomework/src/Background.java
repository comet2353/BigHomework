import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Background {

}

class BackgroundPanel extends JPanel {
    private Image backgroundImage;

    public BackgroundPanel(String ImagePath){
        //构造方法，接收图片路径参数
        try{
            backgroundImage = ImageIO.read(new File(ImagePath));
            //从文件读取图片，并保存到变量

        }catch(IOException e) {
            e.printStackTrace();//若读取失败，打印错误信息

        }
    }

    @Override
    //默认情况下：
    //JPanel 的 paintComponent：只会绘制背景色或者什么都不画（透明）
    //它不会自动知道你要显示图片
    //paintpanel只负责绘制背景，所以不会覆盖在别的panel上面
    protected void paintComponent(Graphics g){
        //重写绘制方法，每次面板刷新时自动调用
        super.paintComponent(g);
        //调用父类方法，（清理面板）
        if(backgroundImage != null){
            g.drawImage(backgroundImage,0,0,getWidth(),getHeight(),this);
            //图片，起始x坐标，起始y坐标，末位置x坐标（获取窗口宽度），末位置y坐标（获取窗口高度），绘制位置
        }
    }


}//BackgroundPanel结束
