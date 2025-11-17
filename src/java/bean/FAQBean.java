/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import dao.FAQDAO;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import model.FAQ;

/**
 *
 * @author hrkas
 */
@ManagedBean
@ViewScoped
public class FAQBean implements Serializable {

    /**
     * Creates a new instance of FAQBean
     */
    private FAQ faq = new FAQ();
    private List<FAQ> faqList;
    private List<FAQ> filteredFaqList;
    private List<FAQ> categories;
    private FAQDAO dao = new FAQDAO();
    private String selectedCategory;

    public FAQBean() {
    }

    /**
     * @return the faq
     */
    public FAQ getFaq() {
        return faq;
    }

    /**
     * @param faq the faq to set
     */
    public void setFaq(FAQ faq) {
        this.faq = faq;
    }

    /**
     * @return the faqList
     */
    public List<FAQ> getFaqList() {
        return faqList;
    }

    /**
     * @param faqList the faqList to set
     */
    public void setFaqList(List<FAQ> faqList) {
        this.faqList = faqList;
    }

    /**
     * @return the categories
     */
    public List<FAQ> getCategories() {
        return categories;
    }

    /**
     * @param categories the categories to set
     */
    public void setCategories(List<FAQ> categories) {
        this.categories = categories;
    }

    /**
     * @return the dao
     */
    public FAQDAO getDao() {
        return dao;
    }

    /**
     * @param dao the dao to set
     */
    public void setDao(FAQDAO dao) {
        this.dao = dao;
    }

    /**
     * @return the filteredFaqList
     */
    public List<FAQ> getFilteredFaqList() {
        return filteredFaqList;
    }

    /**
     * @param filteredFaqList the filteredFaqList to set
     */
    public void setFilteredFaqList(List<FAQ> filteredFaqList) {
        this.filteredFaqList = filteredFaqList;
    }

    /**
     * @return the selectedCategory
     */
    public String getSelectedCategory() {
        return selectedCategory;
    }

    /**
     * @param selectedCategory the selectedCategory to set
     */
    public void setSelectedCategory(String selectedCategory) {
        this.selectedCategory = selectedCategory;
    }

    @PostConstruct
    public void init() {
        faqList = dao.getAllFaqs();
    }

    public void save() {
        if (faq.getFaqId() == 0) {
            dao.insertFAQ(faq);
        } else {
            dao.updateFAQ(faq);
        }
        clearForm();
        faqList = dao.getAllFaqs();
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "FAQ saved successfully", null));
    }

    public void clearForm() {
        faq = new FAQ();
    }

    public void edit(FAQ f) {
        if (f != null) {
            faq = f;
        }
    }

    public void delete(int id) {
        if (id > 0) {
            dao.deleteFAQ(id);
            faqList = dao.getAllFaqs();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "FAQ deleted successfully", null));
        }
    }

    public void filterFaqByCategory() {
        if (getSelectedCategory() == null || getSelectedCategory().isEmpty()) {
            faqList = dao.getAllFaqs();
        } else {
            faqList = dao.getFaqsByCategory(getSelectedCategory());
        }
    }
}
