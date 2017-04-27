 package edu.georgiasouthern.cr04956.architecturelab10;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.androidplot.ui.TextOrientation;
import com.androidplot.util.PlotStatistics;
import com.androidplot.xy.BarFormatter;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PointLabelFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;

import java.util.Arrays;

 public class MainActivity extends AppCompatActivity implements SensorEventListener {

    final static int HISTORY_SIZE = 30;
     private SensorManager sensorManager;
     private Sensor sensor;

    private XYPlot plot;
    private XYPlot historyPlot;

     private SimpleXYSeries series;
//     private SimpleXYSeries aSeries;
//    private SimpleXYSeries pSeries;
//     private SimpleXYSeries rSeries;

     private SimpleXYSeries aHistorySeries;
     private SimpleXYSeries pHistorySeries;
     private SimpleXYSeries rHistorySeries;


     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        plot = (XYPlot) findViewById(R.id.myPlot);

        series = new SimpleXYSeries("APR Levels");
        series.useImplicitXVals();

         plot.addSeries(series, new BarFormatter(Color.argb(100,400,0,0), Color.rgb(0, 80, 0)));
         plot.setDomainStepValue(3);
//         plot.setLinesPerRangeLabel(3);
         plot.setRangeBoundaries(-180, 359, BoundaryMode.FIXED);


         plot.setDomainLabel("Axis");
         plot.getDomainTitle().pack();
         plot.setRangeLabel("Angle (degs)");
         plot.getRangeTitle().pack();

//         plot.setPlotPadding(0, 0, 0, 0);

         //set up history plot
         historyPlot = (XYPlot) findViewById(R.id.historyPlot);

         aHistorySeries = new SimpleXYSeries("Azimuth");
         aHistorySeries.useImplicitXVals();
         pHistorySeries = new SimpleXYSeries("Pitch");
         pHistorySeries.useImplicitXVals();
         rHistorySeries = new SimpleXYSeries("Roll");
         rHistorySeries.useImplicitXVals();

         historyPlot.setRangeBoundaries(-180, 359, BoundaryMode.FIXED);
         historyPlot.setDomainBoundaries(0, 30, BoundaryMode.FIXED);

         LineAndPointFormatter lpf1 = new LineAndPointFormatter(Color.rgb(400,0,0), null, null, null);
         LineAndPointFormatter lpf2 = new LineAndPointFormatter(Color.rgb(0,400,0), null, null, null);
         LineAndPointFormatter lpf3 = new LineAndPointFormatter(Color.rgb(0,0,400), null, null, null);

         historyPlot.addSeries(aHistorySeries, lpf1);
         historyPlot.addSeries(pHistorySeries, lpf2);
         historyPlot.addSeries(rHistorySeries, lpf3);

         historyPlot.setDomainStepValue(5);
//         historyPlot.setLinesPerRangeLabel(3);
         historyPlot.setDomainLabel("Sample Index");
         historyPlot.getDomainTitle().pack();
         historyPlot.setRangeLabel("Angle (Degs)");
         historyPlot.getRangeTitle().pack();

         final PlotStatistics levelStats = new PlotStatistics(1000, false);
         final PlotStatistics histStats = new PlotStatistics(1000, false);

         plot.addListener(levelStats);
         historyPlot.addListener(histStats);

         sensorManager = (SensorManager) getApplicationContext().getSystemService(Context.SENSOR_SERVICE);
         for(Sensor s: sensorManager.getSensorList(Sensor.TYPE_ORIENTATION)) {
             if(s.getType() == Sensor.TYPE_ORIENTATION) {
                 sensor = s;
             }
         }

         if(sensor == null) {
             System.out.println("Failed to attach to sensor");

             return;
         }

         sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
         //checkboxes?
//        Number[] series1Numbers = {1, 8, 5, 2, 7, 4};
//        Number[] series2Numbers = {4, 6, 3, 8, 2, 10};
//
//        XYSeries series1 = new SimpleXYSeries(Arrays.asList(series1Numbers), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Series 1");
//        XYSeries series2 = new SimpleXYSeries(Arrays.asList(series2Numbers), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Series 2");
//
//        LineAndPointFormatter series1Format = new LineAndPointFormatter();
//        series1Format.setPointLabelFormatter(new PointLabelFormatter());
//        series1Format.configure(getApplicationContext(), R.xml.line_point_formatter_with_plf1);
//
//        plot.addSeries(series1, series1Format);
//
//        LineAndPointFormatter series2Format = new LineAndPointFormatter();
//        series2Format.setPointLabelFormatter(new PointLabelFormatter());
//        series2Format.configure(getApplicationContext(), R.xml.line_point_formatter_with_plf2);
//
//        plot.addSeries(series2, series2Format);
//
//
//        plot.setLinesPerRangeLabel(3);
//        plot.getDomainTitle().setOrientation(TextOrientation.HORIZONTAL);
    }

     @Override
     public synchronized void onSensorChanged(SensorEvent event) {
         Number[] series1Numbers = {event.values[0], event.values[1], event.values[2]};
         series.setModel(Arrays.asList(series1Numbers), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY);

         if(rHistorySeries.size() >= HISTORY_SIZE) {
             rHistorySeries.removeFirst();
             pHistorySeries.removeFirst();
             aHistorySeries.removeFirst();
         }

         aHistorySeries.addLast(null, event.values[0]);
         pHistorySeries.addLast(null, event.values[1]);
         rHistorySeries.addLast(null, event.values[2]);

         plot.redraw();
         historyPlot.redraw();
     }

     @Override
     public void onAccuracyChanged(Sensor sensor, int accuracy) {

     }
 }
