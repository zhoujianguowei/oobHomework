package com.jianguo.adriftbookclient.customviews;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
public class MainActivity extends AppCompatActivity
{

    ListView lv;
    String subjectsArray[] = new String[]{"语文", "数学", "英语", "政治思想", "哲学", "医疗卫生",
            "物理", "化学", "社会",
            "面向对象分析与设计"};
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv = (ListView) findViewById(R.id.lv);
        lv.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1,
                subjectsArray));
    }
}
