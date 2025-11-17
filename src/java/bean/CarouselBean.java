/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import model.ImageItem;
import org.primefaces.model.file.UploadedFile;

/**
 *
 * @author hrkas
 */
@ManagedBean
@SessionScoped
public class CarouselBean implements Serializable {

    /**
     * Creates a new instance of CarouselBean
     */
    private String currentImage = "resources/images/default.jpg";
    private List<ImageItem> imageList;
    private List<String> images;
    private UploadedFile file;
    private final String uploadDir = FacesContext.getCurrentInstance().getExternalContext()
            .getRealPath("/resources/images/carousel");

    public CarouselBean() {
    }

    /**
     * @return the currentImage
     */
    public String getCurrentImage() {
        return currentImage;
    }

    /**
     * @param currentImage the currentImage to set
     */
    public void setCurrentImage(String currentImage) {
        this.currentImage = currentImage;
    }

    /**
     * @return the imageList
     */
    public List<ImageItem> getImageList() {
        return imageList;
    }

    /**
     * @param imageList the imageList to set
     */
    public void setImageList(List<ImageItem> imageList) {
        this.imageList = imageList;
    }

    /**
     * @return the file
     */
    public UploadedFile getFile() {
        return file;
    }

    /**
     * @param file the file to set
     */
    public void setFile(UploadedFile file) {
        this.file = file;
    }

    @PostConstruct
    public void init() {
        images = new ArrayList<>();

        String imagesPath = "/resources/images"; // relative to webapp root
        File folder = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath(imagesPath));

        if (folder.exists() && folder.isDirectory()) {
            for (File file : folder.listFiles()) {
                if (file.getName().endsWith(".jpg") || file.getName().endsWith(".png")) {
                    images.add(file.getName());
                }
            }
            Collections.shuffle(images); // random order
        }
    }

    public void upload() {
        if (getFile() != null) {
            try {
                File dir = new File(uploadDir);
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                String filename = UUID.randomUUID() + "_" + getFile().getFileName();
                Path destination = Paths.get(uploadDir, filename);
                Files.copy(getFile().getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage("Uploaded successfully: " + getFile().getFileName()));
            } catch (IOException e) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Upload failed", e.getMessage()));
            }
        }
    }

    public void delete(String imagePath) {
        try {
            String realPath = uploadDir + File.separator + new File(imagePath).getName();
            File fileToDelete = new File(realPath);
            if (fileToDelete.exists() && fileToDelete.delete()) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage("Deleted: " + imagePath));
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Delete failed", e.getMessage()));
        }
    }

    public List<String> getUploadedImages() {
        File dir = new File(uploadDir);
        if (dir.exists() && dir.isDirectory()) {
            return Arrays.stream(Objects.requireNonNull(dir.listFiles()))
                    .filter(f -> !f.isDirectory())
                    .map(f -> "/resources/images/carousel/" + f.getName())
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}
