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

    private void initializePages() {
        // 添加文本页
        textPages.add("a：贵安，档案管理员。今天的新闻看了吗，fsd让我来找你，查查那个藏起来的凶手是谁。");
        textPages.add("a：为什么找你？整个fsd大概没人比你知道的更多吧。");
        textPages.add("a：谁让我来的……？末日的仓鼠之王，祂的信息你肯定查得到。（暗示user输入末日的仓鼠之王进行查找）" +
                "\n" +
                "*仓鼠之王，编号0505，溶酶体复兴者，工作单位：溶酶体。爱好思考人生。备注：难以捉摸的神秘领导者，最好不要惹祂，可能会带来末日。");
        textPages.add("a：你知道祂？那就知道这件事情的严重性吧。目击者说，这个人的头发是黄色的。" +
                "\n" +
                "*五Java，编号1205，洪秀全的父亲，工作单位：614。爱好卖烧烤。备注：曾经染了黄头发，现在是布丁头。（F）\n" +
                "*小斗斗，编号0512，心健部毁灭战士，工作单位：614。爱好歌唱。备注：被无期吉驰和侧颜杀染了黄头发。（F）\n" +
                "*额每里卡，编号0001，美国人，工作单位：美国。爱好美国，备注：黄头发。（F）\n" +
                "*……省略几个人\n" +
                "（菜单栏有一个random，鼠标悬浮在上面显示，没思路吗？随便选个人看看吧。点了之后会从数据库里面抽取一个人的信息，显示在一个弹窗，只是有排版的信息罢了，没有增加多少）");
        textPages.add("a：还有一个可以确定的，这个人绝对来自614，614部门出内鬼了。（严肃脸）\n" +
                "（数据库里只有三个人来自614。我们可以把这三个人的信息添加到新表。）");
        textPages.add("a：还没查好吗，你的信息不会从08年开始就没有更新了吧，你知道春团已经从614调到behold了吗。");
        textPages.add("a：……好吧，你先记下吧。");
        textPages.add("a：我们只有这点信息，你得查个两三天吧（这时候这人还在微笑，但是看起来有点得意）");
        textPages.add("a：查到了？……这么快，也是，小瞧你了");
        textPages.add("a：我有点期待下次的合作了，也许你还能再见到我。");

        // 添加图片路径
        imagePaths.add("image/page1.jpg");
        imagePaths.add("image/page2.jpg");
        imagePaths.add("image/page3.jpg");
        imagePaths.add("image/page4.jpg");
        imagePaths.add("image/page5.jpg");
        imagePaths.add("image/page6.jpg");
        imagePaths.add("image/page7.jpg");
        imagePaths.add("image/page8.jpg");
        imagePaths.add("image/page9.jpg");
        imagePaths.add("image/page10.jpg");
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

    // 点击文本框：增加总页数并跳转到新页（解锁新一页）
    public boolean expandTotalPages() {
        // 如果当前已经在可见的最后一页，则可以扩展总页数
        if (currentPageIndex == maxPageIndex && maxPageIndex < textPages.size() - 1) {
            maxPageIndex++;
            currentPageIndex = maxPageIndex; // 自动跳转到新页面
            return true;
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
