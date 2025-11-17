/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;

/**
 *
 * @author hrkas
 */
@ManagedBean
@ViewScoped
public class CarouselUploadBean implements Serializable {

    private UploadedFile uploadedFile;
    private List<String> imageList;
    private boolean randomMode = true;
    private final String imageDirectory;

    public CarouselUploadBean() {
        imageDirectory = FacesContext.getCurrentInstance().getExternalContext()
                .getRealPath("/resources/images/");
    }

    @PostConstruct
    public void init() {
        loadImages();
    }

    public void upload() {
        if (uploadedFile != null && uploadedFile.getSize() > 0) {
            String extension = getFileExtension(uploadedFile.getFileName());
            String uniqueFileName = UUID.randomUUID() + extension;
            Path targetPath = Paths.get(imageDirectory, uniqueFileName);
            try {
                Files.write(targetPath, uploadedFile.getContent());
            } catch (IOException ex) {
                Logger.getLogger(CarouselUploadBean.class.getName()).log(Level.SEVERE, null, ex);
            }
            imageList.add(uniqueFileName);
            FacesMessage msg = new FacesMessage("Upload Successful", uniqueFileName + " has been uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public void handleFileUpload(FileUploadEvent event) {
        setUploadedFile(event.getFile());
        upload();
    }

    public void loadImages() {
        imageList = new ArrayList<>();
        File folder = new File(imageDirectory);
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles((dir, name) -> isImage(name));
            if (files != null) {
                for (File file : files) {
                    imageList.add(file.getName());
                }
            }
        }
    }

    private boolean isImage(String fileName) {
        return fileName.toLowerCase().matches(".*\\.(jpg|jpeg|png|gif)$");
    }

    private String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf('.'));
    }

    // Getters and Setters
    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public List<String> getImageList() {
        return imageList;
    }

    public boolean isRandomMode() {
        return randomMode;
    }

    public void setRandomMode(boolean randomMode) {
        this.randomMode = randomMode;
    }
}
