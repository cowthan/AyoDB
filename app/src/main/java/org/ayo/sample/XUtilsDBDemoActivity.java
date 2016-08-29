package org.ayo.sample;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.ayo.Ayo;
import org.ayo.activity.AyoActivityAttacher;
import org.ayo.db.sample.R;
import org.ayo.jlog.JLog;
import org.ayo.lang.Lang;
import org.xutils.common.util.DbException;
import org.xutils.db.DbManager;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.db.table.TableEntity;
import org.xutils.db.x;

import java.io.File;
import java.util.List;

/**
 * Created by Administrator on 2016/1/26.
 */
public class XUtilsDBDemoActivity extends AyoActivityAttacher{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_db_xutils);

        x.Ext.init(getActivity().getApplication());

        testDB_query();

        findViewById(R.id.btn_reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    getDB().dropTable(Emp.class);
                } catch (DbException e) {
                    e.printStackTrace();
                }

                testDB_query();
            }
        });

        findViewById(R.id.btn_insert).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testDB_add();
            }
        });

        findViewById(R.id.btn_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testDB_update();
            }
        });

        findViewById(R.id.btn_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testDB_delete();
            }
        });



    }

    private DbManager getDB(){

        DbManager.DaoConfig dbConfig = new DbManager.DaoConfig();
        dbConfig.setAllowTransaction(true);
        dbConfig.setDbDir(new File(Ayo.ROOT));
        dbConfig.setDbName("a2016.db");
        dbConfig.setDbVersion(1);
        dbConfig.setTableCreateListener(new DbManager.TableCreateListener() {
            @Override
            public void onTableCreated(DbManager db, TableEntity<?> table) {
                JLog.i("数据库创建完事了");
            }
        });
        dbConfig.setDbUpgradeListener(new DbManager.DbUpgradeListener() {
            @Override
            public void onUpgrade(DbManager db, int oldVersion, int newVersion) {

            }
        });

        DbManager db = x.getDb(dbConfig);
        return db;
    }

    private void testDB_add(){
        DbManager db = getDB();
        Emp emp = new Emp();
        try {
            db.save(emp);
            JLog.i("插入数据完事了");
        } catch (DbException e) {
            e.printStackTrace();
        }

        testDB_query();
    }

    private void testDB_update(){
        DbManager db = getDB();
        Emp emp; // = new Emp();
        try {
            emp = db.selector(Emp.class).findFirst();
            emp.name = "name new";
            db.saveOrUpdate(emp);
            JLog.i("更新数据完事了");

        } catch (DbException e) {
            e.printStackTrace();
        }

        testDB_query();
    }

    private void testDB_delete(){
        DbManager db = getDB();
        Emp emp; // = new Emp();
        try {
            emp = db.selector(Emp.class).findFirst();
            db.delete(Emp.class, WhereBuilder.b("id", "=", emp.id));
        } catch (DbException e) {
            e.printStackTrace();
        }

        testDB_query();
    }

    private void testDB_query(){
        DbManager db = getDB();
        try {
            List<Emp> list = db.selector(Emp.class).findAll();
            if(Lang.isEmpty(list)){
                Toast.makeText(getActivity(), "没数据", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getActivity(), list.size() + "条数据", Toast.LENGTH_SHORT).show();
                TextView tv_info = findViewById(R.id.tv_info);
                StringBuilder sb = new StringBuilder();

                for(Emp e: list){
                    sb.append(e.id + "----" + e.name + "----" + e.sex + "\n\n");
                }

                tv_info.setText(sb.toString());
            }

        } catch (DbException e) {
            e.printStackTrace();
        }
    }

}
