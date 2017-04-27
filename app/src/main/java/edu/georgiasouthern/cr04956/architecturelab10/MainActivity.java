 package edu.georgiasouthern.cr04956.architecturelab10;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.androidplot.xy.XYPlot;

 public class MainActivity extends AppCompatActivity {

    private XYPlot plot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        plot = (XYPlot) findViewById(R.id.myPlot);



    }
}
