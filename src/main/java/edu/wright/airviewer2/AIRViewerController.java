/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wright.airviewer2;

import edu.wright.airviewer2.AIRViewerModel;
import edu.wright.airviewer2.AIRViewer;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.net.URL;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import java.awt.image.BufferedImage;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import java.util.ResourceBundle;
import javafx.scene.layout.VBox;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Pagination;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.util.Calendar;
import javafx.stage.WindowEvent;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.TextPosition;
import javax.swing.JOptionPane;

/**
 *
 * @author erik
 */
public class AIRViewerController implements Initializable {

	static final String DEFAULT_PATH = "sample.pdf";

	@FXML
	private Pagination pagination;

	@FXML
	private MenuItem openMenuItem;

	@FXML
	private MenuItem saveMenuItem; // Saves the open PDF File

	@FXML
	private PDDocumentInformation docInfo;

	@FXML
	private MenuItem saveAsMenuItem;

	@FXML
	private MenuItem closeMenuItem;

	@FXML
	private MenuItem extractTextMenuItem;

	@FXML
	private MenuItem undoMenuItem;

	@FXML
	private MenuItem redoMenuItem;

	@FXML
	private MenuItem addBoxAnnotationMenuItem;

	@FXML
	private MenuItem addEllipseAnnotationMenuItem;

	@FXML
	private MenuItem addTextAnnotationMenuItem;

	@FXML
	private MenuItem deleteAnnotationMenuItem;
	
	@FXML
	private MenuItem mergeFileMenuItem;

	@FXML
	private MenuItem signMenuItem; // Allows the user to sign a document

	// Opens a modal to display information about the app
	@FXML
	private MenuItem aboutMenuItem;

	private AIRViewerModel model;

	private ImageView currentPageImageView;
	
	@FXML
	VBox rightControls; // Controls on the Rights side of the Scene

	@FXML
	private TextField navigateInput; // Input a page to Navigate to

	@FXML
	private Button navigateButton; // Perform page indexed navigation actions

	@FXML
	Label navigateWarning; // Display a warning concerning invalid Navigation input

	private Group pageImageGroup;
	
	private PageDimensions currentPageDimensions ;

	@FXML
	private ScrollPane scrollPane = new ScrollPane();
	private DoubleProperty zoom = new SimpleDoubleProperty(1.1);

	String cssLayout = "-fx-border-color: red;\n" +
			 "-fx-border-insets: 5;\n" +
			 "-fx-border-width: 3;\n" +
			 "-fx-border-style: dashed;\n";

	String scrollCssLayout= "-fx-border-color: green;\n" +
			 "-fx-border-insets: 5;\n" +
			 "-fx-border-width: 3;\n" +
			 "-fx-border-style: dashed;\n"+
			 //Ne pas afficher le petit trait gris
			 "-fx-background-color:transparent";
	private ImageView imageView = new ImageView();


	private String path;

	private AIRViewerModel promptLoadModel(String startPath) {

		AIRViewerModel loadedModel = null;
		try {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Open PDF File");
			fileChooser.setInitialFileName(startPath);
			Stage stage = null;
			if(pagination.getScene() != null ) {
			      stage = (Stage) pagination.getScene().getWindow();
			}
			File file = fileChooser.showOpenDialog(stage);
			// modified the code to open only files with .pdf extension
			if (null != file) {
				path = file.getCanonicalPath();
				while (!path.endsWith(".pdf")) {
					System.out.println("select only pdf format files");
					file = fileChooser.showOpenDialog(stage);
					path = file.getCanonicalPath();
				}
				loadedModel = new AIRViewerModel(Paths.get(path));

			}
		} catch (IOException ex) {
//            Logger.getLogger(AIRViewerController.class.getName()).log(
//                    Level.INFO,
//                    "Unable to open <" + ex.getLocalizedMessage() + ">",
//                    "");
			loadedModel = null;
		}

		return loadedModel;
	}

	private void synchronizeSelectionKnobs() {
		if (null != model && null != currentPageImageView && null != pageImageGroup) {
			List<java.awt.Rectangle> selectedAreas = model.getSelectedAreas();
			ArrayList<Node> victims = new ArrayList<>(pageImageGroup.getChildren());

			// Delete everything in teh group that isn't currentPageImageView
			victims.stream().filter((n) -> (n != currentPageImageView)).forEach((n) -> {
				pageImageGroup.getChildren().remove(n);
			});

			// Add knobs to thegroup to indicate selection
			for (java.awt.Rectangle r : selectedAreas) {
				Circle knobA = new Circle(r.getX(), (int) pageImageGroup.prefHeight(0) - r.getY(), 4);
				knobA.setStroke(Color.YELLOW);
				knobA.setStrokeWidth(2);
				pageImageGroup.getChildren().add(knobA);
				Circle knobB = new Circle(r.getX() + r.getWidth(), (int) pageImageGroup.prefHeight(0) - r.getY(), 4);
				knobB.setStroke(Color.YELLOW);
				knobB.setStrokeWidth(2);
				pageImageGroup.getChildren().add(knobB);
				Circle knobC = new Circle(r.getX() + r.getWidth(),
						(int) pageImageGroup.prefHeight(0) - (r.getY() + r.getHeight()), 4);
				knobC.setStroke(Color.YELLOW);
				knobC.setStrokeWidth(2);
				pageImageGroup.getChildren().add(knobC);
				Circle knobD = new Circle(r.getX(), (int) pageImageGroup.prefHeight(0) - (r.getY() + r.getHeight()), 4);
				knobD.setStroke(Color.YELLOW);
				knobD.setStrokeWidth(2);
				pageImageGroup.getChildren().add(knobD);
			}
		}

	}

	private void refreshUserInterface() {
		assert pagination != null : "fx:id=\"pagination\" was not injected: check your FXML file 'simple.fxml'.";
		assert openMenuItem != null : "fx:id=\"openMenuItem\" was not injected: check your FXML file 'simple.fxml'.";
		assert saveAsMenuItem != null
				: "fx:id=\"saveAsMenuItem\" was not injected: check your FXML file 'simple.fxml'.";
		assert closeMenuItem != null : "fx:id=\"closeMenuItem\" was not injected: check your FXML file 'simple.fxml'.";

		assert extractTextMenuItem != null
				: "fx:id=\"extractTextMenuItem\" was not injected: check your FXML file 'simple.fxml'.";
		assert undoMenuItem != null : "fx:id=\"undoMenuItem\" was not injected: check your FXML file 'simple.fxml'.";
		assert redoMenuItem != null : "fx:id=\"redoMenuItem\" was not injected: check your FXML file 'simple.fxml'.";
		assert addBoxAnnotationMenuItem != null
				: "fx:id=\"addBoxAnnotationMenuItem\" was not injected: check your FXML file 'simple.fxml'.";
		assert addEllipseAnnotationMenuItem != null
				: "fx:id=\"addEllipseAnnotationMenuItem\" was not injected: check your FXML file 'simple.fxml'.";
		assert addTextAnnotationMenuItem != null
				: "fx:id=\"addTextAnnotationMenuItem\" was not injected: check your FXML file 'simple.fxml'.";
		assert deleteAnnotationMenuItem != null
				: "fx:id=\"deleteAnnotationMenuItem\" was not injected: check your FXML file 'simple.fxml'.";

		if (null != model) {
			pagination.setPageCount(model.numPages());
			pagination.setDisable(false);
			saveMenuItem.setDisable(false);
			saveAsMenuItem.setDisable(false);
			extractTextMenuItem.setDisable(false);
			undoMenuItem.setDisable(!model.getCanUndo());
			undoMenuItem.setText("Undo " + model.getSuggestedUndoTitle());
			redoMenuItem.setDisable(!model.getCanRedo());
			redoMenuItem.setText("Redo " + model.getSuggestedRedoTitle());
			addBoxAnnotationMenuItem.setDisable(false);
			addEllipseAnnotationMenuItem.setDisable(false);
			addTextAnnotationMenuItem.setDisable(false);
			deleteAnnotationMenuItem.setDisable(0 >= model.getSelectionSize());
			rightControls.setDisable(false);

			if (null != currentPageImageView) {
				int pageIndex = pagination.getCurrentPageIndex();
				currentPageImageView.setImage(model.getImage(pageIndex));

				currentPageImageView.setOnMousePressed(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent me) {
						float flippedY = (float) currentPageImageView.getBoundsInParent().getHeight()
								- (float) me.getY();
						System.out.println("Mouse pressed X: " + me.getX() + " Y: " + Float.toString(flippedY));

						float xInPage = (float) me.getX();
						float yInPage = flippedY;

						if (null != model) {
							int pageIndex = pagination.getCurrentPageIndex();
							if (!me.isMetaDown() && !me.isShiftDown()) {
								model.deselectAll();
							}
							model.extendSelectionOnPageAtPoint(pageIndex, xInPage, yInPage);
							refreshUserInterface();
						}
					}
				});
			}

			synchronizeSelectionKnobs();

		} else {
			pagination.setPageCount(0);
			pagination.setPageFactory(index -> {
				if (null == pageImageGroup) {
					pageImageGroup = new Group();
				}
				currentPageImageView = new ImageView();
				pageImageGroup.getChildren().clear();
				pageImageGroup.getChildren().add(currentPageImageView);
				return pageImageGroup;
			});
			pagination.setDisable(true);
			saveMenuItem.setDisable(true);
			saveAsMenuItem.setDisable(true);
			extractTextMenuItem.setDisable(true);
			undoMenuItem.setDisable(true);
			redoMenuItem.setDisable(true);
			addBoxAnnotationMenuItem.setDisable(true);
			addEllipseAnnotationMenuItem.setDisable(true);
			addTextAnnotationMenuItem.setDisable(true);
			deleteAnnotationMenuItem.setDisable(true);
			rightControls.setDisable(true);

		}
	}

	private AIRViewerModel reinitializeWithModel(AIRViewerModel aModel) {
		assert pagination != null : "fx:id=\"pagination\" was not injected: check your FXML file 'simple.fxml'.";
		assert openMenuItem != null : "fx:id=\"openMenuItem\" was not injected: check your FXML file 'simple.fxml'.";
		assert saveAsMenuItem != null
				: "fx:id=\"saveAsMenuItem\" was not injected: check your FXML file 'simple.fxml'.";
		assert closeMenuItem != null : "fx:id=\"closeMenuItem\" was not injected: check your FXML file 'simple.fxml'.";

		assert extractTextMenuItem != null
				: "fx:id=\"extractTextMenuItem\" was not injected: check your FXML file 'simple.fxml'.";
		assert undoMenuItem != null : "fx:id=\"undoMenuItem\" was not injected: check your FXML file 'simple.fxml'.";
		assert redoMenuItem != null : "fx:id=\"redoMenuItem\" was not injected: check your FXML file 'simple.fxml'.";
		assert addBoxAnnotationMenuItem != null
				: "fx:id=\"addBoxAnnotationMenuItem\" was not injected: check your FXML file 'simple.fxml'.";
		assert addEllipseAnnotationMenuItem != null
				: "fx:id=\"addEllipseAnnotationMenuItem\" was not injected: check your FXML file 'simple.fxml'.";
		assert addTextAnnotationMenuItem != null
				: "fx:id=\"addTextAnnotationMenuItem\" was not injected: check your FXML file 'simple.fxml'.";
		assert deleteAnnotationMenuItem != null
				: "fx:id=\"deleteAnnotationMenuItem\" was not injected: check your FXML file 'simple.fxml'.";
		assert mergeFileMenuItem != null 
			    : "fx:id=\"mergeFileMenuItem\" was not injected: check your FXML file 'simple.fxml'.";

		model = aModel;

		openMenuItem.setOnAction((ActionEvent e) -> {
			System.out.println("Open ...");
			reinitializeWithModel(promptLoadModel(AIRViewerController.DEFAULT_PATH));
		});
		openMenuItem.setDisable(false);
		closeMenuItem.setOnAction((ActionEvent e) -> {
			System.out.println("closeMenuItem ...");
			Platform.exit();
		});
		closeMenuItem.setDisable(false);

		if (null != model) {
			JOptionPane.showMessageDialog(null,
					"Title: " + model.title() + System.lineSeparator() + "Creation Date:"
							+ model.creationDate().getTime() + "\n" + "Author: " + model.Author()
							+ System.lineSeparator() + "Modified Date: " + model.modifiedDate().getTime()
							+ System.lineSeparator() + "Subject: " + model.Subject() + System.lineSeparator());
			Stage stage = AIRViewer.getPrimaryStage();
			assert null != stage;
			model.deselectAll();
			pagination.setPageCount(model.numPages());
			pagination.setPageFactory(index -> {
				if (null == pageImageGroup) {
					pageImageGroup = new Group();
				}
				currentPageImageView = new ImageView(model.getImage(index));
				pageImageGroup.getChildren().clear();
				zoomImage(currentPageImageView);
				pageImageGroup.getChildren().add(currentPageImageView);
				model.deselectAll();
				refreshUserInterface();
				return pageImageGroup;
			});

			saveMenuItem.setOnAction((ActionEvent event) -> {
				try {
					model.save(new File(path));
					MessageBox.show("Saved pdf at " + path, "Saved!");
				} catch (Exception e) {
					System.out.println(e);
					MessageBox.show(e.toString(), "Failed to save pdf file. Please try again later");
				}
			});
			saveAsMenuItem.setOnAction((ActionEvent event) -> {
				FileChooser fileChooser = new FileChooser();
				FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
				fileChooser.getExtensionFilters().add(extFilter);
				File file = fileChooser.showSaveDialog((Stage) pagination.getScene().getWindow());
				if (null != file) {
					model.save(file);
				}
			});
			extractTextMenuItem.setOnAction((ActionEvent e) -> {
				System.out.println("extractTextMenuItem ...");
			});
			undoMenuItem.setOnAction((ActionEvent e) -> {
				model.undo();
				refreshUserInterface();
			});
			redoMenuItem.setOnAction((ActionEvent e) -> {
				model.redo();
				refreshUserInterface();
			});
			addBoxAnnotationMenuItem.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					int pageIndex = pagination.getCurrentPageIndex();
					model.executeDocumentCommandWithNameAndArgs("AddBoxAnnotation",
							new String[] { Integer.toString(pageIndex), "36.0", "36.0", "72.0", "72.0" });
					refreshUserInterface();
				}
			});
			addEllipseAnnotationMenuItem.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					int pageIndex = pagination.getCurrentPageIndex();
					model.executeDocumentCommandWithNameAndArgs("AddCircleAnnotation", new String[] {
							Integer.toString(pageIndex), "288", "576", "144.0", "72.0", "Sample Text!" });
					refreshUserInterface();
				}
			});
			addTextAnnotationMenuItem.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					int pageIndex = pagination.getCurrentPageIndex();
					model.executeDocumentCommandWithNameAndArgs("AddTextAnnotation", new String[] {
							Integer.toString(pageIndex), "36", "576", "144.0", "19.0", "A Bit More Sample Text!" });
					refreshUserInterface();
				}
			});
			deleteAnnotationMenuItem.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					int pageIndex = pagination.getCurrentPageIndex();
					model.executeDocumentCommandWithNameAndArgs("DeleteSelectedAnnotation",
							new String[] { Integer.toString(pageIndex) });
					refreshUserInterface();
				}
			});
			
			mergeFileMenuItem.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					try {
						new SplitAndMerge().mergefile(pagination, model);
					} catch (Exception e1) {
						e1.printStackTrace();
					}

				}
			});
		}

		refreshUserInterface();
		return model;
	}

	//Initializing Sign menu item
	private void initSignMenu() {

        signMenuItem.setOnAction(e -> signDocument());
    }
	
	private void signDocument() {
        // Create a Page object
        PDPage pdPage = new PDPage();
        // Add the page to the document and save the document to a desired file.
        model.wrappedDocument.addPage(pdPage);

        try {

            PDSignature pdSignature = new PDSignature();
            pdSignature.setFilter(PDSignature.FILTER_VERISIGN_PPKVS);
            pdSignature.setSubFilter(PDSignature.SUBFILTER_ADBE_PKCS7_SHA1);

            pdSignature.setName("AirViewer Crew");
            pdSignature.setLocation("WFH");
            pdSignature.setReason("Signature Validation");
            pdSignature.setSignDate(Calendar.getInstance());
            model.wrappedDocument.addSignature(pdSignature, null);

            model.wrappedDocument.save(path);
            MessageBox.show("Added Signature successfully", "Alert");

        } catch (IOException ioe) {
            System.out.println("Error while saving pdf. Please try again later" + ioe.getMessage());
            MessageBox.show("Error while saving pdf. Please try again later"," Sorry for Causing incovinience!");
        }

    }


	/*
	 * Initializes about menu function
	 */
	private void aboutMenu() {

		String msg = "This is a small JavaFX application built using Apache PDFBox, "
				+ "maven, and NetBeans IDE to enable annotation of PDF documents "
				+ "and text extraction with unlimited undo and redo.";

		aboutMenuItem.setOnAction(e -> MessageBox.show(msg, "About AirViewer"));
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {

		assert pagination != null : "fx:id=\"pagination\" was not injected: check your FXML file 'simple.fxml'.";

		Stage stage = AIRViewer.getPrimaryStage();
		stage.addEventHandler(WindowEvent.WINDOW_SHOWING, (WindowEvent window) -> {
			reinitializeWithModel(promptLoadModel(DEFAULT_PATH));
		});

		// Initialize about menu control
		aboutMenu();
	}
	
	void zoomImage(ImageView imageView) {
		System.out.print("imageView.getFitHeight(): " + imageView.getImage().getHeight() + "\n");
		System.out.print("imageView.getFitWidth(): " + imageView.getImage().getWidth() + "\n");
		currentPageDimensions = new PageDimensions(imageView.getImage().getWidth(), imageView.getImage().getHeight());
		zoom.addListener(new InvalidationListener() {
			@Override
			public void invalidated(Observable arg0) {
				int width = (int) (imageView.getImage().getWidth() * zoom.get());
				int height = (int) (imageView.getImage().getHeight() * zoom.get());
				imageView.setFitWidth(width);
				System.out.print("width: " + (width) + "px\n");
				imageView.setFitHeight(height);
				System.out.print("height: " + height + "px\n");
				// ==================================================
			}
		});
		imageView.preserveRatioProperty().set(true);
		scrollPane.setPannable(true);
		scrollPane.setStyle(scrollCssLayout);
		scrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
		scrollPane.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
		scrollPane.setContent(imageView);

	}

	@FXML
	private void zoomIn() {
		//zoom.set(zoom.get() * 1.1);
		new ZoomInZoomOut().zoomIn(zoom);

	}

	@FXML
	private void zoomOut() {
		//zoom.set(zoom.get() / 1.1);
		new ZoomInZoomOut().zoomOut(zoom);

	}

	@FXML
	private void zoomFit() {
		double horizZoom = (scrollPane.getWidth() - 20) / currentPageDimensions.width;
		double verticalZoom = (scrollPane.getHeight() - 20) / currentPageDimensions.height;
		zoom.set(Math.min(horizZoom, verticalZoom));
	}

	@FXML
	private void zoomWidth() {
		zoom.set((scrollPane.getWidth() - 20) / currentPageDimensions.width);
	}

	private class PageDimensions {
		private double width;
		private double height;

		PageDimensions(double width, double height) {
			this.width = width;
			this.height = height;
		}

		@Override
		public String toString() {
			return String.format("[%.1f, %.1f]", width, height);
		}
	}

	@FXML
	private TextField textFieldValue;

	@SuppressWarnings("deprecation")
	@FXML
	private void download() throws IOException {
		System.out.println("textfieldvalue" + textFieldValue.getText());
		String value = textFieldValue.getText();
	    new SplitAndMerge().splitter(value,pagination,model); 
	}

}
