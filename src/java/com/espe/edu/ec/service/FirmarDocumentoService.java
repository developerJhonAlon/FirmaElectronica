/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.espe.edu.ec.service;

import com.espe.edu.ec.samples.SignatureTest;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.signatures.BouncyCastleDigest;
import com.itextpdf.signatures.DigestAlgorithms;
import com.itextpdf.signatures.IExternalDigest;
import com.itextpdf.signatures.IExternalSignature;
import com.itextpdf.signatures.PdfSignatureAppearance;
import com.itextpdf.signatures.PdfSigner;
import com.itextpdf.signatures.PrivateKeySignature;
import com.itextpdf.test.annotations.type.SampleTest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;
import org.junit.experimental.categories.Category;

import java.util.Properties;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.primefaces.context.RequestContext;


/**
 *
 * @author Klever
 */
@Category(SampleTest.class)
public class FirmarDocumentoService extends SignatureTest{
    public String SRC = "";
    public String DEST = "";
    public String IMG = "C:\\Users\\Jhonny\\Documents\\NetBeansProjects\\DISTRIBUIDAS\\DigitalSignMJ\\web\\resources\\images\\Logo_ESPE.png";

    public void sign(String src, String dest, int numberpage, float x, float y,
            Certificate[] chain,
            PrivateKey pk, String digestAlgorithm, String provider,
            PdfSigner.CryptoStandard subfilter,
            String reason, String location)
            throws GeneralSecurityException, IOException {

        // Creating the reader and the signer
        PdfReader reader = new PdfReader(src);
        PdfSigner signer = new PdfSigner(reader, new FileOutputStream(dest), false);
        // Creating the appearance
        PdfSignatureAppearance appearance = signer.getSignatureAppearance();
        appearance.setReason(reason);
        appearance.setLocation(location);
        appearance.setReuseAppearance(false);
        Rectangle rect = new Rectangle(x, y, 350, 100);
        appearance.setPageRect(rect);
        appearance.setPageNumber(numberpage);
        appearance.setImage(ImageDataFactory.create(IMG));
        appearance.setImageScale(-1);
        signer.setFieldName("sig");
        // Creating the signature
        IExternalSignature pks = new PrivateKeySignature(pk, digestAlgorithm, provider);
        IExternalDigest digest = new BouncyCastleDigest();
        signer.signDetached(digest, pks, chain, null, null, null, 0, subfilter);
        
        reader.close();
        
        
    }
    
    public void iniciar(String fnsrc,String fndest,String pathP12,String passP12) throws GeneralSecurityException, IOException{
         
        //Properties properties = new Properties();
        //properties.load(new FileInputStream("C:\\Users\\Jhonny\\Documents\\NetBeansProjects\\DISTRIBUIDAS\\DigitalSignMJ\\web\\resources\\encryption\\signkey.properties"));
        String path = pathP12;
        char[] pass = passP12.toCharArray();
        int temp;

        BouncyCastleProvider provider = new BouncyCastleProvider();
        Security.addProvider(provider);
        KeyStore ks = KeyStore.getInstance("pkcs12", provider.getName());
        
        
        ks.load(new FileInputStream(path), pass);
        String alias = ks.aliases().nextElement();
        PrivateKey pk = (PrivateKey) ks.getKey(alias, pass);
        Certificate[] chain = ks.getCertificateChain(alias);
        SRC=SRC+fnsrc;System.out.println("SRC: " + SRC);
        DEST=DEST+fndest;System.out.println("DEST: " + DEST);
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC));
        System.out.println("Number Pages: " + pdfDoc.getNumberOfPages());
        float x = 40;
        float y = pdfDoc.getPage(1).getPageSize().getBottom() + 15;
        System.out.println("x: " + x);
        System.out.println("y: " + y);
        
        temp = pdfDoc.getNumberOfPages();
        pdfDoc.close();
        
        
        sign(SRC, DEST, temp, x, y, chain, pk, DigestAlgorithms.SHA256, provider.getName(), PdfSigner.CryptoStandard.CMS, "Prueba Firma Klever", "Quito, Sangolqui");
     }
}
