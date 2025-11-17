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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
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

    /**
     * Creates a new instance of CarouselUploadBean
     */
    private UploadedFile uploadedFile;
    private List<UploadedFile> uploadedFiles = new ArrayList<>();
    private List<String> imageList;
    private static final String IMAGE_DIR = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/images/");
    private boolean randomMode = true;

    public CarouselUploadBean() {
    }

    /**
     * @return the file
     */
    public UploadedFile getFile() {
        return uploadedFile;
    }

    /**
     * @param file the file to set
     */
    public void setFile(UploadedFile file) {
        this.uploadedFile = file;
    }

    /**
     * @return the imageList
     */
    public List<String> getImageList() {
        return imageList;
    }

    /**
     * @param imageList the imageList to set
     */
    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
    }

    @PostConstruct
    public void init() {
        loadImages();
    }

    public void upload() {
        if (getFile() != null && getFile().getSize() > 0) {
            try {
                String fileName = getFile().getFileName();
                String path = FacesContext.getCurrentInstance().getExternalContext()
                        .getRealPath("/resources/images/" + fileName);

                File targetFile = new File(path);
                try (FileOutputStream out = new FileOutputStream(targetFile)) {
                    out.write(getFile().getContent());
                }

                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Upload successful", fileName + " uploaded."));

            } catch (IOException e) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Upload failed", e.getMessage()));
            }
        }
    }

    public void handleFileUpload(FileUploadEvent event) {
        UploadedFile file = event.getFile();

        if (file != null) {
            try {
                String extension = file.getFileName().substring(file.getFileName().lastIndexOf('.'));
                String uniqueFileName = UUID.randomUUID().toString() + extension;
                String path = FacesContext.getCurrentInstance().getExternalContext()
                        .getRealPath("/resources/images/" + uniqueFileName);

                File targetFile = new File(path);
                try (InputStream input = file.getInputStream();
                        OutputStream output = new FileOutputStream(targetFile)) {

                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = input.read(buffer)) != -1) {
                        output.write(buffer, 0, bytesRead);
                    }
                }

                uploadedFiles.add(file); // Store in the list
                FacesMessage msg = new FacesMessage("Success", file.getFileName() + " is uploaded.");
                FacesContext.getCurrentInstance().addMessage(null, msg);

            } catch (IOException e) {
                FacesMessage error = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Upload Failed", file.getFileName());
                FacesContext.getCurrentInstance().addMessage(null, error);
            }
        }
    }

    public void loadImages() {
        setImageList(new ArrayList<>());
        try {
            String realPath = FacesContext.getCurrentInstance().getExternalContext()
                    .getRealPath("/resources/images/");
            File dir = new File(realPath);
            if (dir.exists() && dir.isDirectory()) {
                for (File file : dir.listFiles()) {
                    if (file.isFile() && isImage(file.getName())) {
                        getImageList().add(file.getName());
                    }
                }
            }
        } catch (Exception ex) {
            ex.getMessage();
        }
    }

    private boolean isImage(String fileName) {
        return fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")
                || fileName.endsWith(".png") || fileName.endsWith(".gif");
    }
}
