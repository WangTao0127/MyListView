package mylistview.view.wt.mylistview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import wt.view.PullListView;

public class MainActivity extends AppCompatActivity {
    PullPersenter pullPersenter;
    PullListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv= (PullListView) findViewById(R.id.lv);
        pullPersenter=new PullPersenter(lv,MainActivity.this);
        List<Map<String,String>>list=new LinkedList<>();
        for(int i=0;i<100;i++){
            Map<String,String>map=new HashMap<>();
            map.put("name","i="+i);
            list.add(map);
        }
        SimpleAdapter adapter=new SimpleAdapter(MainActivity.this,list,R.layout.tv,new String[]{"name"},new int[]{R.id.textView});
        lv.setAdapter(adapter);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }
}
