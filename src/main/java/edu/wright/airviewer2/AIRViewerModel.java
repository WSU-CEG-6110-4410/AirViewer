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

    //private static final Logger logger = Logger.getLogger(AIRViewerModel.class.getName());

    private PDFRenderer renderer;
    
    private PDDocumentInformation docInfo; 

    AIRViewerModel(Path path) throws IOException {
        super(PDDocument.load(path.toFile()), "");
        renderer = new PDFRenderer(wrappedDocument);
        
    }

    int numPages() {
        return wrappedDocument.getPages().getCount();
    }
    
    /*Getting the Title of the docuemnt from "getDocumentInformation"*/
    String title() {
		return wrappedDocument.getDocumentInformation().getTitle();
    	
    }
    /*Getting the Creation Date of the docuemnt from "getDocumentInformation"*/
    Calendar creationDate() {
		return wrappedDocument.getDocumentInformation().getCreationDate();
    	
    }
    /*Getting the Modified Date of the docuemnt from "getDocumentInformation"*/
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

}
