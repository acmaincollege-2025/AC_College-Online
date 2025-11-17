package bean;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean
@RequestScoped
public class ModuleBean {

    public static class Module {

        private String name;
        private String description;
        private String icon;
        private String page;
        private String color;       // hex or class color
        private String colorClass;  // CSS color class (for index.xhtml)
        private List<String> roles;

        public Module(String name, String description, String icon, String page, String colorClass, List<String> roles) {
            this.name = name;
            this.description = description;
            this.icon = icon;
            this.page = page;
            this.colorClass = colorClass;
            this.roles = roles;
        }

        public Module(String name, String description, String color, List<String> roles) {
            this.name = name;
            this.description = description;
            this.color = color;
            this.roles = roles;
        }

        // ✅ Getters and Setters
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getPage() {
            return page;
        }

        public void setPage(String page) {
            this.page = page;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getColorClass() {
            return colorClass;
        }

        public void setColorClass(String colorClass) {
            this.colorClass = colorClass;
        }

        public List<String> getRoles() {
            return roles;
        }

        public void setRoles(List<String> roles) {
            this.roles = roles;
        }
    }

    public ModuleBean() {
    }

    // ✅ Updated module list with colorClass values matching CSS in index.xhtml
    public List<Module> getModuleList() {
        return Arrays.asList(
                new Module("📝 Admission", "Student applications and requirements.", "#03A9F4", Arrays.asList("Admission", "admission")),
                new Module("📋 Registrar", "Manage student records and courses.", "#1976D2", Arrays.asList("Registrar", "registrar")),
                new Module("📊 Dean", "Monitor program statistics and reports.", "#388E3C", Arrays.asList("Dean", "dean")),
                new Module("💰 Cashier", "Process fees and generate receipts.", "#FFA000", Arrays.asList("Cashier", "cashier")),
                new Module("🎓 Student", "View assessments and enroll.", "#607D8B", Arrays.asList("Student", "student")),
                new Module("⚙️ Admin", "System administration and configuration.", "#8E24AA", Arrays.asList("Admin", "admin"))
        );
    }

    public List<Module> getModulesByRole(String role) {
        if ("Admin".equalsIgnoreCase(role)) {
            return getModuleList();
        }
        return getModuleList().stream()
                .filter(m -> m.getRoles().contains(role))
                .collect(Collectors.toList());
    }
}
