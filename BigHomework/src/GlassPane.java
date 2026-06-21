import javax.swing.*;
import java.awt.*;

//新手指引部分的画布设置，包括特定区域挖空，绘制半透明黑色幕布等
public class GlassPane extends JComponent {
    private Rectangle cutout;//挖空区域

    public void setCutout(Rectangle cutout) {
        this.cutout = cutout;
        repaint();
    }//setCutout

    @Override
    protected void paintComponent(Graphics g) {
        //复制一份原始画笔的副本，传给g2
        Graphics2D g2 = (Graphics2D) g.create();
        //半透明黑色背景
        g2.setColor(new Color(0,0,0,150));
        g2.fillRect(0, 0, getWidth(), getHeight());

        //挖掉搜索框区域
        if(cutout != null) {
            g2.setComposite(AlphaComposite.Clear);
            g2.fill(cutout);
        }
        //释放 Graphics2D 对象占用的系统资源
        g2.dispose();

    }//paintComponent

    @Override
    //点击挖空区域时，事件穿透到下层组件
    //若若位于指定区域内，则返回false，则鼠标事件会穿透玻璃，落到搜索框上
    //否则调用父类的判断，正常拦截非指定区域的鼠标，不让他们点到下面的按钮。
    public boolean contains(int x, int y){

        if(cutout != null && cutout.contains(x,y)){
            //判断鼠标点击位置是否在cutout的区域内部
            return false;
        }
        return super.contains(x,y);
    }//contains
}
