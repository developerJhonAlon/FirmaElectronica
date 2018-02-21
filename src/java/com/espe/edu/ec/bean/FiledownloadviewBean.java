/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.espe.edu.ec.bean;

/**
 *
 * @author Klever
 */
import java.io.InputStream;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
 
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
 
@ManagedBean
public class FiledownloadviewBean implements Serializable{
    
    private StreamedContent file;
     
    public FiledownloadviewBean() {        
        InputStream stream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("C:\\Users\\Klever\\Desktop\\RecordAcademico.pdf");
        file = new DefaultStreamedContent(stream, "file/pdf", "downloaded_RecordAcademicoFirmado.pdf");
    }
 
    public StreamedContent getFile() {
        
        
        return file;
        
    }
}
