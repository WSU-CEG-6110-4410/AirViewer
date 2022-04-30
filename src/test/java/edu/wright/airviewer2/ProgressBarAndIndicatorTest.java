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

public class ProgressBarAndIndicatorTest {

	/**
	  * [issue] (https://github.com/WSU-CEG-6110-4410/AirViewer/issues/17)
	  * [issue] (https://github.com/WSU-CEG-6110-4410/AirViewer/issues/18)
	  * These are the test cases for progress bar and progress indicator functionality
	  */
	
	@Test
	public void testPercentage() {
		try {
		Integer k = new Integer(1);
		Path currentDir = Paths.get(".");
		String current = currentDir.toAbsolutePath() + "/src/test/resources/test.pdf";
		Path choosen = Paths.get(current);
		System.out.println(currentDir.toAbsolutePath());
		AIRViewerModel airViewerModel = new AIRViewerModel(choosen);
	    new ProgressBarAndIndicator().getPercentage(k,airViewerModel) ;
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}

}
