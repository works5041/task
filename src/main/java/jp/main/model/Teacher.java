package jp.main.model;

public class Teacher {
    private int id;
    private String name;
    private int age;
    private String sex;
    private String course;

    // 引数付きコンストラクタを追加
    public Teacher(int id, String name, int age, String sex, String course) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.course = course;
    }

    // デフォルトコンストラクタ
    public Teacher() {
    }

    // toString() メソッドをオーバーライド
    @Override
    public String toString() {
        return "Teacher{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", sex='" + sex + '\'' +
                ", course='" + course + '\'' +
                '}';
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }
}
