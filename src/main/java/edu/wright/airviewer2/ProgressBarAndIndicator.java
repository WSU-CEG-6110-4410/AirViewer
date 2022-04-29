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


import com.google.java.contract.Invariant;
import com.google.java.contract.Requires;
import com.google.java.contract.Ensures;

import javafx.beans.property.DoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Pagination;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

@Invariant("index != null")
public class ProgressBarAndIndicator  {
	
	private Integer index;
	private int totalPages;
	
	/**
	  * [Issue] (https://github.com/WSU-CEG-6110-4410/AirViewer/issues/17)
	  * [Issue] (https://github.com/WSU-CEG-6110-4410/AirViewer/issues/18)
	  * [Pull request] (https://github.com/WSU-CEG-6110-4410/AirViewer/pull/40)
	  * 
	  * \brief     Pdf progress bar percentage method
	  * 
	  * \details   when user click pdf pages,using handler to pickup clicked page number 
	  *            and pass to this method. Once get the page number,each time have to 
	  *            increment page number with one, get the total number of pages from the 
	  *            wrapped model.divide current page with total number of pages,
	  *            should get double value,this value uses as parameter 
	  *            for progress bar and progress indicator
	  *          
	  * \note         
	  *          
	  * \param[in]  index           get the pdf current page count
	  * 
	  * \param[in]  model           get the wrapped model with model view
	  * 
	  * \param[out] double          return double value this one used as percentage
	  * 
	  * \retval     return percentage value to controller for progress bar and progress indicator  
	  *                     
	  * 
	  */
	
	@Requires("index != null && totalPages >= index")
	@Ensures("percentage != null")
	public double getPercentage(Integer index,AIRViewerModel model) {
		this.index= index;
		this.totalPages = model.numPages();
		double  percentage = (double)(this.index+1)/this.totalPages;
		return percentage;
	}
}
