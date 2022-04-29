package edu.wright.airviewer2;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;

import com.google.java.contract.Ensures;
import com.google.java.contract.Requires;

import javafx.beans.property.DoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Pagination;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ZoomInZoomOut  {
	
	private Integer index;
	private int totalPages;
	private double zoomValue;
	private double zoomOutValue;
	private double zoomInValue;
	private boolean result;

	/**
	  * [issue] (https://github.com/WSU-CEG-6110-4410/AirViewer/issues/10)
	  * [Pull request] (https://github.com/WSU-CEG-6110-4410/AirViewer/pull/36) 
	  * 
	  * \brief     Pdf Zoom In method
	  * 
	  * \details   UI shows plus symbol, when user click plus symbol,
	  *            user handler pick up the current zoom property and pass to this method.
	  *            In this method it gets the current zoom property and multiply with 1.1
	  *            to increase zoom value. That value have to set to Double property to show in UI
	  *            
	  *          
	  * \note         
	  *          
	  * \param[in]  index           get the pdf current page count
	  * 
	  * \param[in]  model           get the wrapped model with model view
	  * 
	  * \param[out] void      
	  * 
	  * 
	  */
	@Requires("zoomValue!=null && zoomValue>0")
	@Ensures("result == true")
	public void zoomIn(DoubleProperty zoom) {
		this.zoomValue = zoom.get();
		this.zoomInValue = this.zoomValue * 1.1;
		this.result = zoomValue < zoomInValue;
        zoom.set(this.zoomInValue);
        
    }
	
	
	/**
	  * [issue] (https://github.com/WSU-CEG-6110-4410/AirViewer/issues/10)
	  * [Pull request] (https://github.com/WSU-CEG-6110-4410/AirViewer/pull/36) 
	  * 
	  * \brief     Pdf Zoom Out method
	  * 
	  * \details   UI shows minus symbol, when user click minus symbol,
	  *            user handler pick up the current zoom property and pass to this method.
	  *            In this method it gets the current zoom property and divide with 1.1
	  *            to reduce zoom value. That value have to set to Double property to show in UI
	  *            
	  *          
	  * \note         
	  *          
	  * \param[in]  index           get the pdf current page count
	  * 
	  * \param[in]  model           get the wrapped model with model view
	  * 
	  * \param[out] void      
	  * 
	  * 
	  */
	@Requires("zoomValue!=null && zoomValue>0")
	@Ensures("result == true")
	public void zoomOut(DoubleProperty zoom) {
        this.zoomValue = zoom.get();
		this.zoomOutValue = this.zoomValue / 1.1;
		this.result = zoomValue > zoomOutValue;
        zoom.set(this.zoomOutValue);
       
    }
	
	
}
