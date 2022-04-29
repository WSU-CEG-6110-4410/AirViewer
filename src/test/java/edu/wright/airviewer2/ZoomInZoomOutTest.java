package edu.wright.airviewer2;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.Test;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.Slider;

public class ZoomInZoomOutTest {
	
	/**
	  * [issue] (https://github.com/WSU-CEG-6110-4410/AirViewer/issues/10)
	  * These are the test cases for ZoomIn functionality
	  */

	@Test
	public void testZoomIn() {
		DoubleProperty doubleProperty = new SimpleDoubleProperty(3);
		//doubleProperty.bind(slider.valueProperty());
		new ZoomInZoomOut().zoomIn(doubleProperty) ;
	}
	
	/**
	  * [issue] (https://github.com/WSU-CEG-6110-4410/AirViewer/issues/10)
	  * These are the test cases for ZoomOut functionality
	  */

	@Test
	public void testZoomOut() {
		DoubleProperty doubleProperty = new SimpleDoubleProperty(7);
		//doubleProperty.bind(slider.valueProperty());
	    new ZoomInZoomOut().zoomOut(doubleProperty) ;
		
	}
	
	

}
