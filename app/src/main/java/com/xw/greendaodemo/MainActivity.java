package com.xw.greendaodemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.xw.greendaodemo.bean.UserBean;
import com.xw.greendaodemo.dao.UserBeanDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.Iterator;
import java.util.List;

/**
 * greendao使用步骤：
 * 1、在build.gradle中加入apply plugin: 'org.greenrobot.greendao'
 * 2、在build.gradle中加入
          buildscript {
            repositories {
               mavenCentral()
            }
            dependencies {
               classpath 'org.greenrobot:greendao-gradle-plugin:3.2.2'
            }
         }
 * 3、在build.gradle中加入
       compile 'org.greenrobot:greendao:3.2.2'
       compile 'org.greenrobot:greendao-generator:3.2.2'
 * 4、在build.gradle中加入
            greendao {
                  schemaVersion 1
                  daoPackage 'com.xw.greendaodemo.dao'
                  targetGenDir 'src/main/java'
            }
 * 5、在项目中新建一个实体类(如UserBean.java)，并加入如下代码：
 *          @Entity
            public class UserBean {
                private String name;
                private int age;
            }
 * 6、点击bulid下的make project，生成文件------大功告成！！！
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText id, name, age, sex;
    private Button add, delete, update, quire_all, equal, like, between, dayu, xiaoyu, not_equal,
            is_null, is_not_null, paixu, dayu_dengyu, xiaoyu_dengyu;

    private TextView tv;

    private UserBeanDao userBeanDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userBeanDao = GreenDaoManager.getInstance().getSession().getUserBeanDao();

        //添加这两段代码可在控制台查看sql语句
        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;

        init();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add:
                add();
                break;

            case R.id.delete:
                delete();
                break;

            case R.id.update:
                update();
                break;

            case R.id.quire_all:
                quireAll();
                break;

            case R.id.equal:
                queryEq();
                break;

            case R.id.like:
                queryLike();
                break;

            case R.id.between:
                queryBetween();
                break;

            case R.id.dayu:
                queryDayu();
                break;

            case R.id.xiaoyu:
                queryXiaoyu();
                break;

            case R.id.not_equal:
                queryNotEq();
                break;

            case R.id.is_null:
                queryIsNull();
                break;

            case R.id.is_not_null:
                queryIsNotNull();
                break;

            case R.id.paixu:
                queryPaixu();
                break;

            case R.id.dayu_dengyu:
                queryDayuDengyu();
                break;

            case R.id.xiaoyu_dengyu:
                queryXiaoyuDengyu();
                break;
        }
    }

    //增  list集合就循环增加
    private void add() {
        UserBean user = new UserBean();
        user.setId(Long.valueOf(id.getText().toString()));
        user.setName(name.getText().toString());
        user.setAge(Integer.valueOf(age.getText().toString()));
        user.setSex(sex.getText().toString());
        userBeanDao.insert(user);
        clearText();
    }

    //删除name=xx3   list集合就循环删除
    private void delete() {
        UserBean ub = userBeanDao.queryBuilder()
                .where(UserBeanDao.Properties.Name.eq("xx3")).unique();//unique()返回一条记录
        if (ub!=null){
            userBeanDao.deleteByKey(Long.valueOf(ub.getId()));//删除
        }
    }

    //更新name=xx3的age为50    list集合就循环更新
    private void update() {
        UserBean ub = userBeanDao.queryBuilder()
                .where(UserBeanDao.Properties.Name.eq("xx3")).unique();//unique()返回一条记录
        if (ub!=null){
            ub.setAge(50);
            userBeanDao.update(ub);
        }
    }


    //查询所有
    private void quireAll() {
        //第一种
//        List<UserBean> list=userBeanDao.queryBuilder().list();
//        for (UserBean user:list) {
//            Log.e("daodao","quireAll()"+user.toString());
//        }

        //第二种---懒加载，可提高性能
//        LazyList<UserBean> list =userBeanDao.queryBuilder().listLazy();
//        list.close();//必须执行此方法---关闭数据库的游标

        //第三种---也是懒加载，但是没放到内存中,无法服用
//        LazyList<UserBean> list =userBeanDao.queryBuilder().listLazyUncached();
//        list.close();

        //第四种---可以控制迭代
        Iterator it = userBeanDao.queryBuilder().listIterator();
        while (it.hasNext()) {
            UserBean userbean = (UserBean) it.next();
            Log.e("daodao", "quireAll()" + userbean.toString());
        }

    }

    //name=xx3
    private void queryEq() {
        tv.setText("");
        UserBean ub = userBeanDao.queryBuilder()
                .where(UserBeanDao.Properties.Name.eq("xx3")).unique();//unique()返回一条记录
        tv.setText(ub.toString());
    }

    //name包含xx     要加%
    private void queryLike() {
        tv.setText("");
        List<UserBean> list = userBeanDao.queryBuilder().where(UserBeanDao.Properties.Name.like("xx%")).list();
        tv.setText(list.toString());
    }

    //age在30---50之间
    private void queryBetween() {
        tv.setText("");
        List<UserBean> list = userBeanDao.queryBuilder().where(UserBeanDao.Properties.Age.between(30, 50)).list();
        tv.setText(list.toString());
    }

    //age>30
    private void queryDayu() {
        tv.setText("");
        List<UserBean> list = userBeanDao.queryBuilder().where(UserBeanDao.Properties.Age.gt(30)).list();
        tv.setText(list.toString());
    }

    //age<30
    private void queryXiaoyu() {
        tv.setText("");
        List<UserBean> list = userBeanDao.queryBuilder().where(UserBeanDao.Properties.Age.lt(30)).list();
        tv.setText(list.toString());
    }

    //sex!="nan"
    private void queryNotEq() {
        tv.setText("");
        List<UserBean> list = userBeanDao.queryBuilder().where(UserBeanDao.Properties.Sex.notEq("nan")).list();
        tv.setText(list.toString());
    }

    //sex=null
    private void queryIsNull() {
        tv.setText("");
        List<UserBean> list = userBeanDao.queryBuilder().where(UserBeanDao.Properties.Sex.isNull()).list();
        tv.setText(list.toString());
    }

    //sex!=null
    private void queryIsNotNull() {
        tv.setText("");
        List<UserBean> list = userBeanDao.queryBuilder().where(UserBeanDao.Properties.Sex.isNotNull()).list();
        tv.setText(list.toString());
    }

    //name中包含xx的age的升序排列
    private void queryPaixu() {
        tv.setText("");
        List<UserBean> list = userBeanDao.queryBuilder().where(UserBeanDao.Properties.Name.like("xx%"))
                .orderAsc(UserBeanDao.Properties.Age).list();//升序
//        List<UserBean> list = userBeanDao.queryBuilder().where(UserBeanDao.Properties.Name.like("xx%"))
//                .orderDesc(UserBeanDao.Properties.Age).list();//降序
        tv.setText(list.toString());
    }

    //age>=32
    private void queryDayuDengyu() {
        tv.setText("");
        List<UserBean> list = userBeanDao.queryBuilder().where(UserBeanDao.Properties.Age.ge(32)).list();
        tv.setText(list.toString());
    }

    //age<=19
    private void queryXiaoyuDengyu() {
        tv.setText("");
        List<UserBean> list = userBeanDao.queryBuilder().where(UserBeanDao.Properties.Age.le(19)).list();
        tv.setText(list.toString());
    }


    private void clearText() {
        id.setText("");
        name.setText("");
        age.setText("");
        sex.setText("");
    }


    private void init() {
        id = (EditText) findViewById(R.id.userid);
        name = (EditText) findViewById(R.id.name);
        age = (EditText) findViewById(R.id.age);
        sex = (EditText) findViewById(R.id.sex);
        add = (Button) findViewById(R.id.add);
        delete = (Button) findViewById(R.id.delete);
        update = (Button) findViewById(R.id.update);
        quire_all = (Button) findViewById(R.id.quire_all);

        equal = (Button) findViewById(R.id.equal);
        like = (Button) findViewById(R.id.like);
        between = (Button) findViewById(R.id.between);
        dayu = (Button) findViewById(R.id.dayu);
        xiaoyu = (Button) findViewById(R.id.xiaoyu);
        not_equal = (Button) findViewById(R.id.not_equal);
        is_null = (Button) findViewById(R.id.is_null);
        is_not_null = (Button) findViewById(R.id.is_not_null);
        paixu = (Button) findViewById(R.id.paixu);
        dayu_dengyu = (Button) findViewById(R.id.dayu_dengyu);
        xiaoyu_dengyu = (Button) findViewById(R.id.xiaoyu_dengyu);

        tv = (TextView) findViewById(R.id.tv);

        add.setOnClickListener(this);
        delete.setOnClickListener(this);
        update.setOnClickListener(this);
        quire_all.setOnClickListener(this);

        equal.setOnClickListener(this);
        like.setOnClickListener(this);
        between.setOnClickListener(this);
        dayu.setOnClickListener(this);
        xiaoyu.setOnClickListener(this);
        not_equal.setOnClickListener(this);
        is_null.setOnClickListener(this);
        is_not_null.setOnClickListener(this);
        paixu.setOnClickListener(this);
        dayu_dengyu.setOnClickListener(this);
        xiaoyu_dengyu.setOnClickListener(this);
    }


    /**
     * 数据表中的测试数据
     UserBean{id=0, name='xx1', age=15, sex='nan'}
     UserBean{id=12312, name='zz3', age=36, sex=''}
     UserBean{id=46513, name='zz1', age=24, sex='nan'}
     UserBean{id=78646, name='xx5', age=32, sex='nv'}
     UserBean{id=999999, name='xx2', age=19, sex='nv'}
     UserBean{id=74645632, name='zz2', age=49, sex='nv'}
     UserBean{id=78646542, name='xx4', age=43, sex='nan'}
     UserBean{id=78646545, name='xx3', age=56, sex='nv'}
     */


}
