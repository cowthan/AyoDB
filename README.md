# AyoDB
项目里用到的DB模块，直接把XUtils的代码抠出来了
XUtils的DB模块一直用在项目里，但http等模块后来换掉了，inject和bitmap本来就没用，
所以直接把DB单独抠出来放到jcenter，去掉多余代码，也方便引入

------------------------

* 涉及到以下知识点：
    * Table的Entity定义
        * Table注解
        * Column注解
    * 数据库的配置在DbManager.DaoConfig里
    * 所有功能都在DbManager里，这是个接口
    * 没有个createTable方法，所有访问表的方法，都遵循无则自动创建的原则

贴代码：
```java

//需要全局初始化：
x.Ext.init(getActivity().getApplication());

private DbManager getDB(){
    DbManager.DaoConfig dbConfig = new DbManager.DaoConfig();
    dbConfig.setAllowTransaction(true);
    dbConfig.setDbDir(new File(Ayo.ROOT));
    dbConfig.setDbName("a2016.db");
    dbConfig.setDbVersion(1);
    dbConfig.setTableCreateListener(new DbManager.TableCreateListener() {
        @Override
        public void onTableCreated(DbManager db, TableEntity<?> table) {
            Logger.info("表创建完事了");
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

插入：
private void testDB_add(){
    DbManager db = getDB();
    Emp emp = new Emp();
    try {
        db.save(emp);
        Logger.info("插入数据完事了");
    } catch (DbException e) {
        e.printStackTrace();
    }

    testDB_query();
}

更新：
private void testDB_update(){
    DbManager db = getDB();
    Emp emp; // = new Emp();
    try {
        emp = db.selector(Emp.class).findFirst();
        emp.name = "name new";
        db.saveOrUpdate(emp);
        Logger.info("更新数据完事了");

    } catch (DbException e) {
        e.printStackTrace();
    }

    testDB_query();
}

删除：
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

查询：简单方法
emp = db.selector(Emp.class).findFirst();
List<Emp> list = db.selector(Emp.class).findAll();

WhereBuilder：拼where
db.delete(Emp.class, WhereBuilder.b("id", "=", emp.id));

Selector：拼查询条件
Parent test = db.selector(Parent.class).where("id", "in", new int[]{1, 3, 6}).findFirst();
long count = db.selector(Parent.class).where("name", "LIKE", "w%").and("age", ">", 32).count();
List<Parent> testList = db.selector(Parent.class).where("id", "between", new String[]{"1", "5"}).findAll();

List<Parent> list = db.selector(Parent.class)
    .where("id", "<", 54)
    .and("time", ">", calendar.getTime())
    .orderBy("id", true)
    .limit(10).findAll();

List<DbModel> dbModels = db.selector(Parent.class)
    .groupBy("name")
    .select("name", "count(name) as count").findAll();
```