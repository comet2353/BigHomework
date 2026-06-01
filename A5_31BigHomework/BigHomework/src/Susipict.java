public class Susipict {

        private String Id;
        private String name;
        private String gender;
        private int age;
        private String major;
        private String skill;
        private String post;

        // 空构造方法
        public Susipict() {}

        // 全参构造方法（可选）
        public Susipict(String Id, String name, String gender, int age, String major) {
            this.Id = Id;
            this.name = name;
            this.gender = gender;
            this.age = age;
            this.major = major;
        }

        // Getter和Setter方法
        public String getStudentId() { return Id; }
        public void setStudentId(String Id) { this.Id = Id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getGender() { return gender; }
        public void setGender(String gender) { this.gender = gender; }
        public int getAge() { return age; }
        public void setAge(int age) { this.age = age; }
        public String getMajor() { return major; }
        public void setMajor(String major) { this.major = major; }

}
