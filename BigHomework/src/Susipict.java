public class Susipict {

    private String number;    // 编号
    private String name;      // 姓名
    private String nickname;  // 称号
    private String workplace; // 工作单位
    private String hobby;     // 爱好
    private String remark;    // 备注

    // 空构造方法
    public Susipict() {
    }

    // 全参构造方法
    public Susipict(String number, String name, String nickname, String workplace, String hobby, String remark) {
        this.number = number;
        this.name = name;
        this.nickname = nickname;
        this.workplace = workplace;
        this.hobby = hobby;
        this.remark = remark;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getWorkplace() {
        return workplace;
    }

    public void setWorkplace(String workplace) {
        this.workplace = workplace;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

