package app.stevenpuerto2012.com.pruebatoast;

import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

public class Activity_acelerometer extends AppCompatActivity implements SensorEventListener {

    /** Called when the activity is first created. */
    TextView x,y,z;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        x = (TextView)findViewById(R.id.xID);
        y = (TextView)findViewById(R.id.yID);
        z = (TextView)findViewById(R.id.zID); //asociacia boton
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //pantalla en orientacion vertical
    }

    protected void onResume() {
        super.onResume();

        SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE); //manager de sensores
        List<Sensor> sensors= sm.getSensorList(Sensor.TYPE_ACCELEROMETER); //lista de sensores del tipo especifico
        if(sensors.size() > 0){ //dispositivo android tiene acelerometro
            sm.registerListener(this, sensors.get(0),SensorManager.SENSOR_DELAY_GAME);//iniciar sensor
        }
    }
    protected void onPause() {
        SensorManager mSensorManager=(SensorManager) getSystemService(SENSOR_SERVICE); //manager de sensores
        mSensorManager.unregisterListener(this); //detener sensor
        super.onPause();
    }
    protected void onStop() {
        SensorManager mSensorManager=(SensorManager) getSystemService(SENSOR_SERVICE); //manager de sensores
        mSensorManager.unregisterListener(this); // detener sensor
        super.onStop();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        this.x.setText("X = "+event.values[0]);
        this.y.setText("Y = "+event.values[1]);
        this.z.setText("Z = "+event.values[2]);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
