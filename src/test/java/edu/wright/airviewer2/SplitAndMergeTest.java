package edu.wright.airviewer2;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Pagination;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

public class SplitAndMergeTest extends ApplicationTest {
	/**
	  * [issue] (https://github.com/WSU-CEG-6110-4410/AirViewer/issues/30)
	  * These are the test cases for merge functionality
	  */

	@Test
	public void mergefileTest() {
		try {
			Pagination pagination = new Pagination();
			Path currentDir = Paths.get(".");
			String current = currentDir.toAbsolutePath() + "/src/test/resources/test.pdf";
			Path exist = Paths.get(current);
			AIRViewerModel model = new AIRViewerModel(exist);
			pagination.setPageCount(model.numPages());
			pagination.setDisable(true);
			Group pageImageGroup = new Group();
			pagination.setPageFactory(index -> {
			ImageView currentPageImageView = new ImageView(model.getImage(0));
			pageImageGroup.getChildren().clear();
			pageImageGroup.getChildren().add(currentPageImageView);

			return pageImageGroup;
			});
			File f = new File(current);
			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						new SplitAndMerge().mergefile(pagination, model);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	  * [issue] (https://github.com/WSU-CEG-6110-4410/AirViewer/issues/29)
	  * These are the test cases for split functionality
	  */

	@Test
	public void splitterTest() {
		try {
			Pagination pagination = new Pagination();
			Path currentDir = Paths.get(".");
			String current = currentDir.toAbsolutePath() + "/src/test/resources/test.pdf";
			Path exist = Paths.get(current);
			AIRViewerModel model = new AIRViewerModel(exist);
			pagination.setPageCount(model.numPages());
			pagination.setDisable(true);
			Group pageImageGroup = new Group();
			pagination.setPageFactory(index -> {
			ImageView currentPageImageView = new ImageView(model.getImage(0));
			pageImageGroup.getChildren().clear();
			pageImageGroup.getChildren().add(currentPageImageView);

			return pageImageGroup;
			});
			File f = new File(current);
			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						new SplitAndMerge().splitter("1", pagination, model);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
