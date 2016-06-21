package com.example.dhruv.test;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

/**
 * Created by noriq on 21/06/2016.
 */
public class ImgProc {
    public double Thresholding (Mat m) {
        Mat gray = new Mat();
        double ret;

        Imgproc.cvtColor(m, gray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.GaussianBlur(gray, gray, new Size(3, 3), 0);
        ret = Imgproc.threshold(gray, gray, 0, 255, Imgproc.THRESH_BINARY+Imgproc.THRESH_OTSU);

        return ret;
    }
}
