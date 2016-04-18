package com.example.dhruv.test;


import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;

import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Toast;

public class MainActivity extends Activity implements CvCameraViewListener2,OnTouchListener{
    private static final String  TAG                 = "OCVSample::Activity";



    private CameraBridgeViewBase mOpenCvCameraView;

    private Mat                  mIntermediateMat;




    private BaseLoaderCallback  mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i(TAG, "OpenCV loaded successfully");
                    mOpenCvCameraView.setMaxFrameSize(720,480);
                    mOpenCvCameraView.enableFpsMeter();
                    mOpenCvCameraView.enableView();
                    mOpenCvCameraView.setOnTouchListener(MainActivity.this);

                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    public MainActivity() {
        Log.i(TAG, "Instantiated new " + this.getClass());
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "called onCreate");
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_main);

        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.image_manipulations_activity_surface_view);
        mOpenCvCameraView.setVisibility(CameraBridgeViewBase.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_1_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(TAG, "called onCreateOptionsMenu");

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(TAG, "called onOptionsItemSelected; selected item: " + item);

        return true;
    }

    public void onCameraViewStarted(int width, int height) {
        mIntermediateMat = new Mat();


    }

    public void onCameraViewStopped() {
        // Explicitly deallocate Mats
        if (mIntermediateMat != null)
            mIntermediateMat.release();

        mIntermediateMat = null;
    }

    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {


        Mat rgba = inputFrame.rgba();

        //did u change please change

            // generate gray scale and blur
            Mat gray = new Mat();
            Imgproc.cvtColor(rgba, gray, Imgproc.COLOR_BGR2GRAY);
            Imgproc.blur(gray, gray, new Size(3, 3));

            // detect the edges
            Mat edges = new Mat();

            Imgproc.Canny(gray, edges,40,200);
            gray.release();

            int threshold = 20;
            int minLineSize = 10;
            int lineGap = 20;
            Mat lines = new Mat();

            Imgproc.HoughLinesP(edges, lines, 1, Math.PI / 180, 80,10,50);
//        edges.release();

            for (int i = 0; i < lines.rows(); i++) {
                double[] val = lines.get(i, 0);
                Imgproc.line(rgba, new Point(val[0], val[1]), new Point(val[2], val[3]), new Scalar(255, 0, 0), 2);
            }
            lines.release();

            // Imgproc.line(rgba,new Point(0,0), new Point(sizeRgba.width,sizeRgba.height),new Scalar(255,0,0), 3);

        return rgba;

    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Toast toast = Toast.makeText(this, "TEST", Toast.LENGTH_SHORT);
        toast.show();
        return false;
    }
}

