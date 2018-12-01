package com.aplusstory.fixme;

import android.app.admin.SystemUpdatePolicy;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;

public class UserMovementRecognizer implements Recognizer, SensorEventListener{
    public static final long DELAY_ASEC = 1000;
    public static final long DELAY_FEWSEC = 3000;
    public static final long DELAY_SOMESEC = 10000;
    public static final long DELAY_AMIN = 60000;

    public static final String KEY_ACCELERATION_X = "x";
    public static final String KEY_ACCELERATION_Y = "y";
    public static final String KEY_ACCELERATION_Z = "z";
    public static final String KEY_DELTA_ACCELERATION_X = "dx";
    public static final String KEY_DELTA_ACCELERATION_Y = "dy";
    public static final String KEY_DELTA_ACCELERATION_Z = "dz";
    public static final String KEY_SPEED = "speed";
    public static final String KEY_PITCH = "p";
    public static final String KEY_ROLL = "r";
    public static final String KEY_YAW = "y";
    public static final String KEY_DELTA_PITCH = "dp";
    public static final String KEY_DELTA_ROLL = "dr";
    public static final String KEY_DELTA_YAW = "dy";

    private static final long GAP_OF_TIME = 100;
    private static final long SHAKE_THRESHOLD = 800;

    private static final int STEP_THRESHOLD = 15;
//    private static final int STEP_THRESHOLD = 3;

    private Context context;
    private boolean cond = true;
    private boolean isEnabled = false;
    private Thread thd = null;
    private SensorManager sm = null;
    private boolean[] senGot = {false, false, false, false};
    private static final int INDEX_ACCELEOMETER = 0;
    private static final int INDEX_GYROSCOPE = 1;
    private static final int INDEX_STEP_COUNTER = 2;
    private static final int INDEX_STEP_DETECTOR = 3;

    private long tStep = -1;
    private long dStep = DELAY_AMIN / 2;
//    private long dStep = DELAY_SOMESEC;
    private int sCurrent = 0;
    private int sBegin = -1;
    private boolean stepDetected = false;

    private long T = -1;
    private float[]  V = {0.0F, 0.0F, 0.0F};
    private float[] dV = {0.0F, 0.0F, 0.0F};
    private float A = 0.0F;

    private float[]  Th = {0.0F, 0.0F, 0.0F};
    private float[] dTh = {0.0F, 0.0F, 0.0F};

    public UserMovementRecognizer(Context context){
        this.initialize(context);
    }

    private void initialize(Context context){
        this.context = context;
        this.T = System.currentTimeMillis();
        if(this.thd == null || !this.thd.isAlive()){
            this.thd = new MovementRecognizingThread();
        }

        if(this.sm == null){
            if(!this.registerSensors()){
                Log.d(this.getClass().getName(), "Failed to get some sensors");
            }
        }

        this.enable();
    }
    private boolean registerSensors(){
        if(this.sm == null) {
            this.sm = (SensorManager) this.context.getSystemService(Context.SENSOR_SERVICE);
        }
        Sensor acc = this.sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Sensor gyro = this.sm.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        Sensor stpDtc = this.sm.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        Sensor stpCnt = this.sm.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        if(acc != null) {
            sm.registerListener(this, acc, SensorManager.SENSOR_DELAY_NORMAL);
            this.senGot[INDEX_ACCELEOMETER] = true;
        }

        if(gyro != null){
            sm.registerListener(this, gyro, SensorManager.SENSOR_DELAY_NORMAL);
            this.senGot[INDEX_GYROSCOPE] = true;
        }

        if(stpDtc != null){
            sm.registerListener(this, stpDtc, SensorManager.SENSOR_DELAY_NORMAL);
            this.senGot[INDEX_STEP_DETECTOR] = true;
        }

        if(stpCnt != null){
            sm.registerListener(this, stpCnt, SensorManager.SENSOR_DELAY_NORMAL);
            this.senGot[INDEX_STEP_COUNTER] = true;
        }

        return senGot[INDEX_ACCELEOMETER]
            && senGot[INDEX_GYROSCOPE]
            && senGot[INDEX_STEP_DETECTOR]
            && senGot[INDEX_STEP_COUNTER];
    }

    private class MovementRecognizingThread extends Thread{
        @Override
        public void run() {
            UserMovementRecognizer that = UserMovementRecognizer.this;
            long now;
            that.cond = false;
            while(that.isEnabled){
                try{
                    Thread.sleep(UserMovementRecognizer.DELAY_FEWSEC);
                } catch (InterruptedException e){
                    return;
                }

                if(that.sm != null){
                    now = System.currentTimeMillis();
                    if(that.senGot[INDEX_STEP_DETECTOR] && that.senGot[INDEX_STEP_COUNTER]){
                        synchronized (that) {
                            if (that.stepDetected) {
                                if (that.tStep > 0) {
                                    if (now - that.tStep > that.dStep) {
                                        if(sCurrent - sBegin > STEP_THRESHOLD){
                                            that.cond = true;
                                        } else {
                                            that.cond = false;
                                        }
                                        that.stepDetected = false;
                                    } else {
                                        Log.d(that.getClass().getName(), "current steps : " + Integer.toString(that.sCurrent));
                                    }
                                } else {
                                    Log.d(that.getClass().getName(), "user walking");
                                    that.tStep = now;
                                    that.sBegin = sCurrent;
                                }
                            } else{
                                that.cond = false;
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean checkCondition() {
        synchronized (this) {
            return cond;
        }
    }

    @Override
    public boolean isEnabled() {
        synchronized (this) {
            return this.isEnabled;
        }
    }

    @Override
    public Object getExtraData() {
        Bundle b = new Bundle();
        float[] V, dV, Th, dTh;
        float A;
        synchronized (this) {
            V = this.V.clone();
            dV = this.dV.clone();
            Th = this.Th.clone();
            dTh = this.dTh.clone();
            A = this.A;
        }

        b.putFloat(KEY_ACCELERATION_X, V[0]);
        b.putFloat(KEY_ACCELERATION_Y, V[1]);
        b.putFloat(KEY_ACCELERATION_Z, V[2]);
        b.putFloat(KEY_DELTA_ACCELERATION_X, dV[0]);
        b.putFloat(KEY_DELTA_ACCELERATION_Y, dV[1]);
        b.putFloat(KEY_DELTA_ACCELERATION_Z, dV[2]);
        b.putFloat(KEY_SPEED, A);

        b.putFloat(KEY_PITCH, Th[0]);
        b.putFloat(KEY_ROLL, Th[1]);
        b.putFloat(KEY_YAW, Th[2]);
        b.putFloat(KEY_DELTA_PITCH, dTh[0]);
        b.putFloat(KEY_DELTA_ROLL, dTh[1]);
        b.putFloat(KEY_DELTA_YAW, dTh[2]);

        return b;
    }

    @Override
    public Class getExtraDataType() {
        return Bundle.class;
    }

    @Override
    public void enable() {
        if(!this.isEnabled) {
            synchronized (this) {
                this.isEnabled = true;
                if(this.thd == null || !this.thd.isAlive()){
                    this.thd = new MovementRecognizingThread();
                    this.thd.start();
                }
                this.registerSensors();
            }
        }
    }

    @Override
    public void disable() {
        if(this.isEnabled) {
            synchronized (this) {
                this.isEnabled = false;
                this.sm.unregisterListener(this);
            }
        }
    }

    @Override
    public void destroy() {
        if(this.thd != null && this.thd.isAlive()) {
            this.thd.interrupt();
        }

        this.disable();
    }

    private float getDeltaV(){
        return (float)Math.sqrt(
                Math.pow((double)this.dV[0], 2.0)
            +   Math.pow((double)this.dV[1], 2.0)
            +   Math.pow((double)this.dV[2], 2.0)
            );
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float[] sv = event.values;
        float x,y,z;
        long now = System.currentTimeMillis();
        long dT = now - this.T;
        switch (event.sensor.getType()) {
            case Sensor.TYPE_STEP_DETECTOR:
                Log.d(this.getClass().getName(), "Step detected");
                synchronized (this){
                    this.stepDetected = true;
                }
                break;
            case Sensor.TYPE_STEP_COUNTER:
                Log.d(this.getClass().getName(), "Step counted. steps : " + Float.toString(sv[0]));
                synchronized (this){
                    this.stepDetected = true;
                    this.sCurrent = Math.max((int)sv[0], sCurrent);
                }
                break;
            case Sensor.TYPE_ACCELEROMETER:
                x = sv[0]; y = sv[1]; z = sv[2];
                if(dT > GAP_OF_TIME) {
                    synchronized (this) {
                        this.dV[0] = x - this.V[0];
                        this.dV[1] = y - this.V[1];
                        this.dV[2] = z - this.V[2];
                        this. V[0] = x;
                        this. V[1] = y;
                        this. V[2] = z;
                        this.A = this.getDeltaV()/dT * 10000;
                    }
//                    Log.d(this.getClass().getName(),
//                            "Accellometer value changed"
//                                    + " , dx : " + Float.toString(this.dV[0])
//                                    + " , dy : " + Float.toString(this.dV[1])
//                                    + " , dz : " + Float.toString(this.dV[2])
//                                    + " , speed : " + Float.toString(this.A)
//                    );
                }
                break;
            case Sensor.TYPE_GYROSCOPE:
                Log.d(this.getClass().getName(), "");
                synchronized (this){
                    x = sv[0]; y = sv[1]; z = sv[2];
                    if(dT > 100) {

                        synchronized (this) {
                            this.dTh[0] = x - this.V[0];
                            this.dTh[1] = y - this.V[1];
                            this.dTh[2] = z - this.V[2];
                            this. Th[0] = x;
                            this. Th[1] = y;
                            this. Th[2] = z;
//                            Log.d(this.getClass().getName(),
//                                    "gyroscope value changed"
//                                            + " , x : " + Float.toString(x)
//                                            + " , y : " + Float.toString(y)
//                                            + " , z : " + Float.toString(z)
//                            );
                        }
                    }
                }
                break;
        }
        synchronized (this) {
            this.T = now;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d(this.getClass().getName(), "Accuracy changed. accuracy : " + Integer.toString(accuracy));
    }
}
