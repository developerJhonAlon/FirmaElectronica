/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.espe.edu.ec.bean;

import com.espe.edu.ec.service.FirmarDocumentoService;
import java.io.File;
import static java.io.File.separatorChar;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.security.GeneralSecurityException;
import javax.ejb.EJB;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import org.primefaces.model.UploadedFile;

/**
 *
 * @author Klever
 */
@ManagedBean
@ViewScoped
public class FileuploadviewBean implements Serializable {

    private final FirmarDocumentoService firmarDocumento = new FirmarDocumentoService();
    private String realPath = "", passFileP12;
    private UploadedFile filePDF,fileP12;
    private StreamedContent file;
    private boolean btnDescagar = true;

    public FileuploadviewBean() {
    }

    public boolean isBtnDescagar() {
        return btnDescagar;
    }

    public void setBtnDescagar(boolean btnDescagar) {
        this.btnDescagar = btnDescagar;
    }

    
    
    
    public String getPassFileP12() {
        return passFileP12;
    }

    public void setPassFileP12(String passFileP12) {
        this.passFileP12 = passFileP12;
    }
    
    
    public UploadedFile getFilePDF() {
        return filePDF;
    }

    public void setFilePDF(UploadedFile filePDF) {
        this.filePDF = filePDF;
    }

    public UploadedFile getFileP12() {
        return fileP12;
    }

    public void setFileP12(UploadedFile fileP12) {
        this.fileP12 = fileP12;
    }
    
    public void upload() throws GeneralSecurityException, IOException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        System.out.println("path 2:" + externalContext.getRealPath("/upload/"));
        
        String sourceFilePDF, sourceFileP12;
        
        realPath = externalContext.getRealPath("/upload/") + File.separator +"Firmado.pdf";    
        //System.out.println("");
        
        sourceFilePDF = this.handleFileUpload(filePDF);
        sourceFileP12 = this.handleFileUpload(fileP12);
        
        System.out.println("sourceFilePDF:" +sourceFilePDF+ " sourceNewFile: "+realPath );
        System.out.println("sourceFileP12:" +sourceFileP12);        
        
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();         
        String path = servletContext.getRealPath("");
        String carpetaAdjuntos = "documentos_adjuntos" + separatorChar + "anexos";
        System.out.println("PARTH APLICACION"+ path + "SEPARADOR: "+carpetaAdjuntos);
        
        firmarDocumento.iniciar(sourceFilePDF,realPath , sourceFileP12 ,this.passFileP12);
        sourceFileP12 = null;
        sourceFilePDF = null;
        
        //this.btnDescagar = false;

    }
    
 

    

    //Jhonny: Carga de Documento
    public String handleFileUpload(UploadedFile file1 ) {

        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();

        System.out.println("path 1:" + externalContext.getRealPath("/upload/"));
       

        System.out.println("file solo:" + file1.getFileName());
      
//        File result1 = new File(file1.getFileName());
//        System.out.println("filee:" + result1.getAbsolutePath());
//           

        File result = new File(externalContext.getRealPath("/upload/") + File.separator + file1.getFileName());
        
        System.out.println("final file:" +result.getPath());

        //Here. Guardar el nombre del archivo en Modelo JPA.
        // this.publicacionModal.setPubPdf(this.file.getFileName());
        try {

            FileOutputStream fileOutputStream = new FileOutputStream(result);
            
            byte[] buffer = new byte[20000];

            int bulk;

            // Here you get uploaded picture bytes, while debugging you can see that 34818
            InputStream inputStream = file1.getInputstream();
            
            while (true) {

                bulk = inputStream.read(buffer);

                if (bulk < 0) {

                    break;

                } //end of if

                fileOutputStream.write(buffer, 0, bulk);
                fileOutputStream.flush();
               
            } //end fo while(true)

            
            fileOutputStream.close();
            inputStream.close();
          
            
            FacesMessage msg = new FacesMessage("Succesful", file1.getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            

        } catch (IOException e) {

            e.printStackTrace();

            FacesMessage error = new FacesMessage("The files were not uploaded!");
            FacesContext.getCurrentInstance().addMessage(null, error);

        }
        
        return result.getPath();
    }
    
    
    
    
//    
//    
//        public String handleFileUpload(UploadedFile file1 ) {
//        
//        
//
//        FacesContext facesContext = FacesContext.getCurrentInstance();
//        ExternalContext externalContext = facesContext.getExternalContext();
//
//        System.out.println("path 1:" + externalContext.getRealPath("/upload/"));
//
//        System.out.println("file solo:" + file1.getFileName());
//      
////        File result1 = new File(file1.getFileName());
////        System.out.println("filee:" + result1.getAbsolutePath());
//           
//
//        File resultE = new File(file1.getContentType());
//        File result = new File(externalContext.getRealPath("/upload/") + File.separator + file1.getFileName());
//       
//
//        //System.out.println("final file:" +result.getPath());
//
//        //Here. Guardar el nombre del archivo en Modelo JPA.
//        // this.publicacionModal.setPubPdf(this.file.getFileName());
//        try {
//            FileInputStream fi = new FileInputStream(resultE);
//            
//            FileOutputStream fo = new FileOutputStream(result);
//
//            byte[] b = new byte[(int)resultE.length()];
//            
//            fi.read(b);
//            fo.write(b);
//            
//            
//            FacesMessage msg = new FacesMessage("Succesful", file1.getFileName() + " is uploaded.");
//            FacesContext.getCurrentInstance().addMessage(null, msg);
//            
//
//        } catch (IOException e) {
//
//            e.printStackTrace();
//
//            FacesMessage error = new FacesMessage("The files were not uploaded!");
//            FacesContext.getCurrentInstance().addMessage(null, error);
//
//        }
//        
//        return result.getPath();
//    }
//    
//    
//    
//    
    
     public StreamedContent descargarPDF() throws FileNotFoundException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        System.out.println("path of files: " + externalContext.getRealPath("/upload/"));
        //input entra un archivo o objecto a java, out sale o write un archivo fuera de java.
        InputStream stream = new FileInputStream(externalContext.getRealPath("/upload/") + File.separator +"Firmado.pdf");
        this.file = new DefaultStreamedContent(stream, "application/pdf", "Firmado.pdf");
        
        File directorio = new File(externalContext.getRealPath("/upload/"));
        File[] ficheros = directorio.listFiles();
        for (File fichero : ficheros) {
             fichero.delete();
         } 
        
        return this.file;
    }

}
