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

import javafx.scene.control.Pagination;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class SplitAndMerge  {

	
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
          
          
       }
    

	 public void splitter(String textFieldValue, Pagination pagination, AIRViewerModel model) {
			try {
			Splitter splitter = new Splitter();
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
		    	   String name = "split-"+sf.format(new Date()) +".pdf";
		    	   String path = selectedDirPath + "/" + name;
				   PDFmerger.setDestinationFileName(path);
				   PDFmerger.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());
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
