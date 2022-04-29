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

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;


import javafx.scene.control.Pagination;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class SplitAndMerge  {
    private String textFieldValue; 
    private boolean result;
	/**
	  * [issue] (https://github.com/WSU-CEG-6110-4410/AirViewer/issues/30)
	  * [Pull request] (https://github.com/WSU-CEG-6110-4410/AirViewer/pull/37) 
	  * \brief      Pdf Merge file method
	  * 
	  * \details    In menu bar there is option to click merge file.Once click merge file ,system prompt to 
	  *             choose pdf file,once choose,system again prompt user to choose folder to save merge pdf.
	  *             current existing pdf file can be merge with choosen file and combine as pdf.
	  *             Generated pdf name contain current date and time.Using date with format "ddmmyyyHHMMSS".
	  *             So the final generated pdf file contain name "merge-{ddmmyyyHHMMSS}.pdf".
	  *             Example pdf file name "merge-12042022021238.pdf".
	  *             This method used "PDFMergerUtility" to merge more than one pdf file.
	  *             This method used "SimpleDateFormat" class to get the current date and time for file name.
	  *          
	  * \note       system prompt to choose file to merge pdf.If user user did not choose file,
	  *             system not generate pdf.
	  *          
	  * \param[in]  Pagination  get the pdf with page count
	  * 
	  * \param[in]  model       get the wrapped model with model view
	  * 
	  * \param[out] void  
	  * 
	  * \retval     show success dialog box with pdf name contain data time format.
	  *             Format pdf :"merge-{ddmmyyyyHHMMSS}.pdf". 
	  *             Example pdf filem name "merge-12042022011236.pdf" 
	  *                     
	  * 
	  */
	 public void mergefile(Pagination pagination, AIRViewerModel model) throws Exception  {
         FileChooser fileChooser = new FileChooser();
         FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
         fileChooser.getExtensionFilters().add(extFilter);
         System.out.println(pagination.getScene()+"test");
         File newfile = null;
         if(pagination.getScene() != null) {
          newfile = fileChooser.showOpenDialog((Stage) pagination.getScene().getWindow());
         }else {
        	 Path currentDir = Paths.get(".");
     		String current = currentDir.toAbsolutePath() + "/src/test/resources/test.pdf";
        	 newfile = new File(current);
         }
         
         DirectoryChooser dirChooser = new DirectoryChooser();
         dirChooser.setTitle("Select a folder");
         File selectedDir = null;
         if(pagination.getScene() != null) {
          selectedDir = dirChooser.showDialog((Stage) pagination.getScene().getWindow());
         } else {
        	 Path currentDir = Paths.get(".");
      		String current = currentDir.toAbsolutePath() + "/src/test/resources/";
         	
        	 selectedDir = new File(current);
         }

          String selectedDirPath =selectedDir !=null?selectedDir.getAbsolutePath():"";
          SimpleDateFormat sf = new SimpleDateFormat("ddmmyyyHHMMSS");

          
          PDFMergerUtility PDFmerger = new PDFMergerUtility();
         // PDDocument document = PDDocument.load(new File(model.getPathName()));
          PDFmerger.addSource(new File(model.getPathName()));
          PDFmerger.addSource(newfile);
          String name = "merge-"+sf.format(new Date()) +".pdf";
          String path = selectedDirPath + "/" + name;
   		   PDFmerger.setDestinationFileName(path);
   		   PDFmerger.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());
   		   /**
			* [Issue41] (https://github.com/WSU-CEG-6110-4410/AirViewer/issues/41)
		    * Alert pops up when the merge is done successfully
			*/
   		   Alert alert = new Alert(AlertType.INFORMATION);
   		   alert.setTitle("Success");
   		   alert.setHeaderText("Results:");
   		   alert.setContentText("Files has been merged with name " + name);
   		   alert.showAndWait();
          
       }
    
	 /**
	  * [Issue] (https://github.com/WSU-CEG-6110-4410/AirViewer/issues/29)
	  * \brief     Pdf splitter method
	  * 
	  * \details   current existing pdf file pages can be split and combine as pdf.
	  *            In UI,can view text box to choose pages.Text field value should be comma separated
	  *            values.Let say user choose 1,2 pages and click download button,
	  *            then prompt user to select folder to save pdf.Generated pdf name contain 
	  *            current date and time.Using date with format "ddmmyyyHHMMSS".
	  *            So the final generated pdf file contain name "split-{ddmmyyyHHMMSS}.pdf".
	  *            Example pdf file name "split-12042022011236.pdf".
	  *            This method used "PDFMergerUtility" to merge more than one pdf file.
	  *            This method used "SimpleDateFormat" class to get the current date and time for file name.
	  *          
	  * \note      system prompt to choose folder to save split pdf.If  user did not choose folder
	  *            system not generate pdf
	  *          
	  * \param[in]  textFieldValue  get the pdf pages to split,values are camma separated
	  * 
	  * \param[in]  Pagination      get the pdf with page count
	  * 
	  * \param[in]  model           get the wrapped model with model view
	  * 
	  * \param[out] void  
	  * 
	  * \retval     show success dialog box with pdf name contain data time format.
	  *             Format Example:"split-{ddmmyyyyHHMMSS}.pdf".  
	  *                     
	  * 
	  */
	 
	 @Requires("textFieldValue != null")
	 @Ensures("result == true")
	 public void splitter(String textFieldValue, Pagination pagination, AIRViewerModel model) {
			try {
			this.textFieldValue= textFieldValue;	
			Splitter splitter = new Splitter();
		       DirectoryChooser dirChooser = new DirectoryChooser();

		       dirChooser.setTitle("Select a folder");

		       File selectedDir = null;
		       if(pagination.getScene() != null) {
		    	   selectedDir =  dirChooser.showDialog((Stage) pagination.getScene().getWindow());
		       } else {
		        	 Path currentDir = Paths.get(".");
		       		String current = currentDir.toAbsolutePath() + "/src/test/resources/";
		         	 selectedDir = new File(current);
		      }		    		  

		       String selectedDirPath =selectedDir.getAbsolutePath();
		       SimpleDateFormat sf = new SimpleDateFormat("ddmmyyyHHMMSS");

		       
		       PDFMergerUtility PDFmerger = new PDFMergerUtility();
		       PDDocument document = PDDocument.load(new File(model.getPathName()));
		       List<PDDocument> pages = splitter.split(document);
		       Iterator<PDDocument> iterator = pages.listIterator();

		       //Saving each page as an individual document
		      
		      
		       PDDocument document1 = new PDDocument();
		       OutputStream out = new ByteArrayOutputStream();
		       boolean flag = false;
		       String[] values = textFieldValue.split(",");
		       System.out.println("valueslength"+values.length);
		       int count =1;
		       for(int i=0;i<values.length;i++) {
		    	   System.out.println("i..."+values[i]);
		    	   for(int j=0;j<pages.size();j++) {
		    		   System.out.println("j..."+j);
		    		   if(values[i].equals((j+1)+"")) {
		    			   System.out.println("values.........."+values[i]);
		    	    		  
		    			   File tempfile = new File(selectedDirPath + "/" + "temp_"+values[i]+".pdf");
		    			   flag = true;  
		    			   PDDocument pd = pages.get(j);
		    			   pd.save(tempfile);
		    			   PDFmerger.addSource(tempfile);
		    			   
		    			   
		    		   }
		    	   }
		       }
		       
		       if(flag) {
		    	   this.result= flag;
		    	   String name = "split-"+sf.format(new Date()) +".pdf";
		    	   String path = selectedDirPath + "/" + name;
				   PDFmerger.setDestinationFileName(path);
				   PDFmerger.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());
				   /**
					* [Issue41] (https://github.com/WSU-CEG-6110-4410/AirViewer/issues/41)
				    * Alert pops up when the split is done successfully
					*/
				   Alert alert = new Alert(AlertType.INFORMATION);
				   alert.setTitle("Success");
				   alert.setHeaderText("Results:");
				   alert.setContentText("File has been split with name "+name);	
				   alert.showAndWait();
		       }
		       for(int i=0;i<values.length;i++) {
		    	   File tempfile = new File(selectedDirPath + "/" + "temp_"+values[i]+".pdf");
		    	   tempfile.delete();
		       }
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
}
