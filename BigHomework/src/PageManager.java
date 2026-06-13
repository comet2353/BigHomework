import java.util.ArrayList;
import java.util.List;

public class PageManager {
    private List<String> textPages; // 存储所有文本页
    private List<String> imagePaths; // 存储所有图片路径
    private int currentPageIndex; // 当前页索引（从0开始）
    private int maxPageIndex; // 最大可访问页索引（控制可见页数）

    public PageManager() {
        textPages = new ArrayList<>();
        imagePaths = new ArrayList<>();
        currentPageIndex = 0;
        maxPageIndex = 0; // 初始时只有1页可见

        // 初始化一些示例数据
        initializePages();
    }
//只需要再增加pages.add()就能很方便地实现增加页数
    private void initializePages() {
        // 添加文本页
        textPages.add("**请点击屏幕");
        textPages.add("a：\n您好，档案管理员。");
        textPages.add("a：\n我是FSD派来的，想拜托你帮忙追查一名隐藏的嫌疑人。");
        textPages.add("a：\nFSD那边几经考量，全系统没人知道的比你更多，我们需要你的帮助。");
        textPages.add("a：\n委托方是末日的仓鼠之王，你一定知道祂吧\n。"+"*这是谁？在后台偷偷查一下应该没事");
        textPages.add("【管理员操作查询后】\nA：你知道祂呀，那你应该清楚这次案件的轻重了。");
        textPages.add("a：\n目前锁定关键线索：嫌疑人隶属于614部门，部门内部出现内鬼。");
        textPages.add("a：\n还有一条目击证词，嫌疑人有着一头黄色头发，这是外貌关键特征。");
        textPages.add("【管理员翻阅档案间隙】\nA：听说春团早前已经从614调去Behold分部了，这条情报你档案里有记录吧？");
        textPages.add("a：\n我们只有这点信息，你得查个两三天吧");
        textPages.add("\n*【管理员操作中】\n……");
        textPages.add("a：\n查到了？……这么快，也是，小瞧你了");
        textPages.add("a：\n我有点期待下次的合作了，也许你还能再见到我。");

        // 添加图片路径
        imagePaths.add("image/a_005.png");
        imagePaths.add("image/a_007.png");
        imagePaths.add("image/a_005.png");
        imagePaths.add("image/a_008.png");
        imagePaths.add("image/a_005.png");
        imagePaths.add("image/a_006.png");
        imagePaths.add("image/a_005.png");
        imagePaths.add("image/a_010.png");
        imagePaths.add("image/a_005.png");
        imagePaths.add("image/a_004.png");
        imagePaths.add("image/a_005.png");
        imagePaths.add("image/a_005.png");
        imagePaths.add("image/a_007.png");
    }

    // 获取当前文本内容
    public String getCurrentText() {
        if (textPages.isEmpty()) {
            return "暂无内容";
        }
        return textPages.get(currentPageIndex);
    }

    // 获取当前图片路径
    public String getCurrentImagePath() {
        if (imagePaths.isEmpty()) {
            return null;
        }
        return imagePaths.get(currentPageIndex);
    }
//**********************重要
    // 点击文本框：增加总页数并跳转到新页（解锁新一页）
    public boolean expandTotalPages() {
        // 如果当前已经在可见的最后一页，则可以扩展总页数
        if (currentPageIndex == maxPageIndex && maxPageIndex < textPages.size() - 1) {
            maxPageIndex++;
            currentPageIndex = maxPageIndex; // 自动跳转到新页面
            return true;
        }
        // 如果当前页不在可见的最后一页，说明还有未浏览的页面，不扩展但返回true表示可以翻页
        if (currentPageIndex < maxPageIndex) {
            return true; // 表示可以翻到下一页
        }
        // 如果已经是全部页面的最后一页，返回false表示无法再扩展
        return false;
    }

    // 翻到下一页（通过按钮）
    public boolean nextPage() {
        if (currentPageIndex < maxPageIndex) {
            currentPageIndex++;
            return true;
        }
        return false;
    }

    // 翻到上一页
    public boolean previousPage() {
        if (currentPageIndex > 0) {
            currentPageIndex--;
            return true;
        }
        return false;
    }

    // 添加新页面（文本+图片）
    public void addNewPage(String text, String imagePath) {
        textPages.add(text);
        imagePaths.add(imagePath);
        // 注意：不改变maxPageIndex，需要用户点击文本框才能解锁
    }

    // 获取当前页码（从1开始）
    public int getCurrentPageNumber() {
        return currentPageIndex + 1;
    }

    // 获取总页数（可见的页数）
    public int getTotalPages() {
        return maxPageIndex + 1;
    }

    // 获取页码显示字符串（格式：当前页/总页数）
    public String getPageDisplay() {
        return getCurrentPageNumber() + "/" + getTotalPages();
    }

    // 判断是否有上一页
    public boolean hasPrevious() {
        return currentPageIndex > 0;
    }

    // 判断是否有下一页（基于可见页数）
    public boolean hasNext() {
        return currentPageIndex < maxPageIndex;
    }

    // 判断是否是真正的最后一页（所有预加载的页面）
    public boolean isAtRealLastPage() {
        return currentPageIndex >= textPages.size() - 1;
    }

    // 跳转到指定页（从1开始）
    public boolean goToPage(int pageNumber) {
        if (pageNumber >= 1 && pageNumber <= getTotalPages()) {
            currentPageIndex = pageNumber - 1;
            return true;
        }
        return false;
    }
}
