package com.rilin.lzy.mybase.my;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.rilin.lzy.mybase.R;
import com.rilin.lzy.mybase.greendao.DaoMaster;
import com.rilin.lzy.mybase.greendao.DaoSession;
import com.rilin.lzy.mybase.greendao.Note;
import com.rilin.lzy.mybase.greendao.NoteDao;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;

/**
 * http://www.open-open.com/lib/view/open1438065400878.html
 * greenDAO数据库框架使用
 */
public class GreenDaoActivity extends AppCompatActivity {

    private SQLiteDatabase db;
    private EditText editText;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    private Cursor cursor;
    public static final String TAG = "GreenDaoActivity";
    private ListView mListview;
    private SimpleCursorAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_green_dao);

        // 官方推荐将获取 DaoMaster 对象的方法放到 Application 层，这样将避免多次创建生成 Session 对象
        setupDatabase();
        // 获取 NoteDao 对象
        getNoteDao();

        String textColumn = NoteDao.Properties.Text.columnName;
        String orderBy = textColumn + " COLLATE LOCALIZED ASC";
        cursor = db.query(getNoteDao().getTablename(),getNoteDao().getAllColumns(),null,null,null,null,orderBy);
//        cursor = db.query(getNoteDao().getTablename(),getNoteDao().getAllColumns(),null,null,null,null,null);
        String[] from = {textColumn,NoteDao.Properties.Comment.columnName};
        int[] to = {android.R.id.text1,android.R.id.text2};

        mAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, cursor, from,
                to);
        mListview = (ListView)findViewById(R.id.greendao_list);
        mListview.setAdapter(mAdapter);

        editText = (EditText) findViewById(R.id.greendao_editTextNote);

        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //删除操作,可以通过id也可以一次性删除所有
                getNoteDao().deleteByKey(id);
                //        getNoteDao().deleteAll();
                Log.i(TAG, "Deleted note, ID: " + id);
                Toast.makeText(GreenDaoActivity.this, "任性的我直接删除了-id-->>" + id + "的数据", Toast.LENGTH_SHORT).show();
                cursor.requery();
            }
        });

    }

    private void setupDatabase() {
        // 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
        // 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO 已经帮你做了。
        // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
        // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this,"notes-db",null);
        db = helper.getWritableDatabase();
        //主要 该数据库连接属于DaoMaster 所以多个
        mDaoMaster = new DaoMaster(db);
        mDaoSession = mDaoMaster.newSession();
    }

    private NoteDao getNoteDao() {
        return mDaoSession.getNoteDao();
    }

    public void onClick(View v){
        mAdapter.notifyDataSetChanged();
        switch (v.getId()) {
            case R.id.greendao_btnAdd:
                addNote();
                break;
            case R.id.greendao_btnSearch:
                search();
                break;
            default:
                Log.i(TAG, "what has gone wrong ?");
                break;
        }
    }

    private void addNote() {
        String noteText = editText.getText().toString();
        editText.setText("");

        final DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
        String comment = "Added on " + df.format(new Date());

        // 插入操作，简单到只要你创建一个 Java 对象
        Note note = new Note(null, noteText, comment, new Date());
        getNoteDao().insert(note);
        Log.i(TAG, "Inserted new note, ID: " + note.getId());
        //GreenDaoActivity: Inserted new note, ID: 13
        cursor.requery();
    }

    private void search() {
        // Query 类代表了一个可以被重复执行的查询
        Query query = getNoteDao().queryBuilder()
                .where(NoteDao.Properties.Text.eq("qqq"))
                .orderAsc(NoteDao.Properties.Date)
                .build();

//      查询结果以 List 返回
      List notes = query.list();
        if (notes != null && notes.size() > 0) {
            for (int i = 0; i < notes.size(); i++) {
                Object o = notes.get(i);
                if(o instanceof Note){
                    Note note = (Note) o;
                    String text = note.getText();
                    String comment = note.getComment();
                    Log.i(TAG,"TEXT---->>" + text + "------comment----->>>" + comment);
                }
            }
        }else {
            Log.i(TAG,"sorry,I can't find anything~~~~~~~~");
        }
        // 在 QueryBuilder 类中内置两个 Flag 用于方便输出执行的 SQL 语句与传递参数的值
        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;
    }
    //TEXT---->>qqq------comment----->>>Added on 2016年9月27日 09:13:29
}
