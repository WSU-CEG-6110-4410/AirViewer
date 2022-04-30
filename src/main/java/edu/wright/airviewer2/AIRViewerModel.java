/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wright.airviewer2;

//import java.awt.image.BufferedImage;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.nio.file.Path;
import java.util.Calendar;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.rendering.PDFRenderer;

/**
 *
 * @author erik
 */ 
public class AIRViewerModel extends DocumentCommandWrapper {

    private PDFRenderer renderer;

    private String filePath;
 
    AIRViewerModel(Path path) throws IOException {
        super(PDDocument.load(path.toFile()), "");
        filePath = path.normalize().toString()+"";
        System.out.println(filePath);
        renderer = new PDFRenderer(wrappedDocument);
        
    }

    int numPages() {
        return wrappedDocument.getPages().getCount();
    }
    
    /*Getting the Title of the docuemnt from "getDocumentInformation"*/
    String title() {
		return wrappedDocument.getDocumentInformation().getTitle();
    	
    }
    /**
	  * [Issue] (https://github.com/WSU-CEG-6110-4410/AirViewer/issues/20)
	  * [Pull request] (https://github.com/WSU-CEG-6110-4410/AirViewer/pull/34)
	  * 
	  * \brief     this method will help to return the creation time of a document
	  * 
	  *          
	  * 
	  * \param[out] Calendar          return document created time 
	  * 
	  * \retval     return   Calendar which can be used to display the document created time
	  *                     
	  * 
	  */
    Calendar creationDate() {
		return wrappedDocument.getDocumentInformation().getCreationDate();
    	
    }
    /**
  	  * [Issue] (https://github.com/WSU-CEG-6110-4410/AirViewer/issues/20)
  	  * [Pull request] (https://github.com/WSU-CEG-6110-4410/AirViewer/pull/34)
  	  * 
  	  * \brief     this method will help to return the modified time of a document
  	  * 
  	  *          
  	  * 
  	  * \param[out] Calendar          return document modified time 
  	  * 
  	  * \retval     return   Calendar which can be used to display the document modified time
  	  *                     
  	  * 
  	  */
    Calendar modifiedDate() {
		return wrappedDocument.getDocumentInformation().getModificationDate();
    	
    }
    /*Getting the Author of the docuemnt from "getDocumentInformation"*/
    String Author() {
		return wrappedDocument.getDocumentInformation().getAuthor();
    	
    }
    /*Getting the Subject of the docuemnt from "getDocumentInformation"*/
    String Subject() {
		return wrappedDocument.getDocumentInformation().getSubject();
    	
    }
    

    Image getImage(int pageNumber) {
        BufferedImage pageImage;
        try {
            pageImage = renderer.renderImage(pageNumber);
        } catch (IOException ex) {
            throw new UncheckedIOException("AIRViewer throws IOException", ex);
        }
        return SwingFXUtils.toFXImage(pageImage, null);
    }

    public void save(File file) {
        try {
            wrappedDocument.save(file);
            
        } catch (IOException ex) {
//            Logger.getLogger(AIRViewerModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public String getPathName() {
    	 return filePath;
    	 }

}
