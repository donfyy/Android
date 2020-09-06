#pragma once //只包含一次

//这里宏的作用 防止重复导入
//#ifndef C_PLUS_STUDY_MYTEACHER_H
//#define C_PLUS_STUDY_MYTEACHER_H


//类的声明
class MyTeacher {
private:
    int m_age;
    char m_name[32];

public:

    void setAge(int age);
    int getAge();
};


//#endif //C_PLUS_STUDY_MYTEACHER_H
