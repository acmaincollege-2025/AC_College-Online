/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import dao.DocumentDAO;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import model.Documents;
import org.primefaces.model.file.UploadedFile;

/**
 *
 * @author hrkas
 */
@ManagedBean
@ViewScoped
public class DocumentBean {

    /**
     * Creates a new instance of DocumentBean
     */
    private String selectedType;
//    private List<String> documentTypes = Arrays.asList("Form 138", "Birth Certificate", "Good Moral", "2x2 ID Picture");
    private UploadedFile uploadedFile;
    private LoginBean loginBean;

    @EJB
    private DocumentDAO documentDAO;

    public DocumentBean() {
    }

    /**
     * @return the selectedType
     */
    public String getSelectedType() {
        return selectedType;
    }

    /**
     * @param selectedType the selectedType to set
     */
    public void setSelectedType(String selectedType) {
        this.selectedType = selectedType;
    }

    /**
     * @return the uploadedFile
     */
    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    /**
     * @param uploadedFile the uploadedFile to set
     */
    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    /**
     * @return the loginBean
     */
    public LoginBean getLoginBean() {
        return loginBean;
    }

    /**
     * @param loginBean the loginBean to set
     */
    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }

    public void upload() {
        if (getUploadedFile() != null && getSelectedType() != null) {
            String fileName = getUploadedFile().getFileName();
            String path = "/resources/uploads/" + fileName;

            // Save file physically (you can customize path handling)
            try (InputStream input = getUploadedFile().getInputStream()) {
                Files.copy(input, Paths.get(path), StandardCopyOption.REPLACE_EXISTING);

                // Save metadata
                Documents doc = new Documents();
                doc.setStudentId(getLoginBean().getLoggedInStudent().getStudno()); // assuming loginBean accessible
                doc.setFileType(getSelectedType());
                doc.setFileName(fileName);
                doc.setFilePath(path);
                documentDAO.saveDocument(doc);

                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage("Document uploaded successfully!"));

            } catch (IOException e) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Upload failed.", e.getMessage()));
            }
        }
    }
}
