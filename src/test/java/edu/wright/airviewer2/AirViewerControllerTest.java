package edu.wright.airviewer2;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.testfx.framework.junit.ApplicationTest;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.control.Pagination;
import javafx.scene.image.ImageView;

public class AirViewerControllerTest extends ApplicationTest {
	
	@Test
	public void promptLoadModelTest() throws IOException {
		

		File f = new File("");
		Platform.startup(() ->
		{
		    // This block will be executed on JavaFX Thread
		});
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					new AIRViewerController().promptLoadModel("");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
	
	@Test
	public void initializeTest() throws IOException {
		File f = new File("");
		URL url = new URL("https://myDevServer.com/dev/api/gate");
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					new AIRViewerController().initialize(url, null);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

}
