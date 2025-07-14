package org.example.bean;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.example.enums.Role;
import org.example.models.entities.Staff;
import org.example.services.StaffService;
import org.primefaces.event.RowEditEvent;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Named("staffTableBean")
@ViewScoped
public class StaffTableBean implements Serializable {

    private List<Staff> staffList;
    private Staff selectedStaff;
    private Role selectedRole;
    private String searchKeyword;
    private List<Staff> filteredStaffs;
    private Staff staff;
    private List<Staff> staffs;

    private List <Staff> receptionists;
    private List<Staff> nurses;

    @Inject
    private StaffService staffService;

    @PostConstruct
    public void init() {
        refreshStaff();

    }




    public void filterByRole() {
        if (selectedRole != null) {
            staffList = staffService.getStaffByRole(selectedRole);
        } else {
            refreshStaff();
        }
    }

    public void deleteSelectedStaff() {
        if (selectedStaff != null && selectedStaff.getStaffID() != null) {
            staffService.deleteStaffById(selectedStaff.getStaffID());
            refreshStaff();
            selectedStaff = null;
        }
    }

    public void searchByKeyword() {
        System.out.println("=== SEARCH DEBUG ===");
        System.out.println("Search called with keyword: '" + searchKeyword + "'");
        System.out.println("Total staffs available: " + (staffs != null ? staffs.size() : 0));

        if (searchKeyword == null || searchKeyword.trim().isEmpty()) {
            System.out.println("Empty search - showing all staffs");
            assert staffs != null;
            filteredStaffs = new ArrayList<>(staffs);
        } else {
            String keyword = searchKeyword.toLowerCase().trim();
            System.out.println("Searching for keyword: '" + keyword + "'");

            filteredStaffs = staffs.stream()
                    .filter(p -> {
                        String firstName = p.getFirstName() != null ? p.getFirstName().toLowerCase() : "";
                        String lastName = p.getLastName() != null ? p.getLastName().toLowerCase() : "";
                        String email = p.getEmail() != null ? p.getEmail().toLowerCase() : "";

                        boolean matches = firstName.contains(keyword) ||
                                lastName.contains(keyword) ||
                                email.contains(keyword);

                        if (matches) {
                            System.out.println("Match found: " + firstName + " " + lastName + " (" + email + ")");
                        }

                        return matches;
                    })
                    .collect(Collectors.toList());
        }

        System.out.println("Filtered results: " + filteredStaffs.size() + " staffs");
        System.out.println("=== END SEARCH DEBUG ===");
    }

    public void resetForm() {

        staff = new Staff();

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Info",
                        "Form has been reset."));
    }



    public String cancelForm() {
        staff = new Staff();

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Info",
                        "Form has been cancelled."));

        return "/pages/staffs/list.xhtml?faces-redirect=true";
    }

    public void resetFilters() {
        System.out.println("Resetting filters...");
        searchKeyword = "";
        filteredStaffs = new ArrayList<>(staffs);
        System.out.println("Reset complete. Showing " + filteredStaffs.size() + " staffs");
    }

    public void onRowEdit(RowEditEvent event) {
        System.out.println("=== RowEdit Triggered ===");
        Staff staff = (Staff) event.getObject();
        try {
            // Update the staff in the database
            staffService.updateStaff(staff);

            // Show success message
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("Staff Updated", "Staff " + staff.getFirstName() + " " + staff.getLastName() + " updated successfully"));

            // Refresh the list
            refreshStaff();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to update staff: " + e.getMessage()));
        }
    }

    public void onRowCancel(RowEditEvent event) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage("Edit Cancelled", "Edit cancelled"));
    }

    private void sortStaffsLatestFirst() {

        staffs.sort((p1, p2) -> {
            if (p1.getStaffID() == null || p2.getStaffID() == null) {
                return 0; // Keep order if IDs missing
            }
            return p2.getStaffID().compareTo(p1.getStaffID()); // Descending order
        });


    }

    public void refreshStaff() {
        staffs = staffService.getAllStaff();
        sortStaffsLatestFirst();
        filterStaffs(staffs);

    }

    public void delete(Long staffId) {
        try {
            staffService.deleteStaffById(staffId);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("Patient Deleted", "Patient deleted successfully"));
            refreshStaff();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to delete staff: " + e.getMessage()));
        }
    }

    public void filterStaffs(List<Staff> staffList) {
        nurses = staffList.stream()
                .filter(p -> p.getRole() == Role.NURSE)
                .sorted(Comparator.comparing(Staff::getStaffID).reversed()) // Latest first
                .toList();

        receptionists = staffList.stream()
                .filter(p -> p.getRole() == Role.RECEPTION)
                .sorted(Comparator.comparing(Staff::getStaffID).reversed()) // Latest first
                .toList();
    }



    // Getters and setters

    public List<Staff> getStaffList() {
        return staffList;
    }

    public void setStaffList(List<Staff> staffList) {
        this.staffList = staffList;
    }

    public Staff getSelectedStaff() {
        return selectedStaff;
    }

    public void setSelectedStaff(Staff selectedStaff) {
        this.selectedStaff = selectedStaff;
    }

    public Role getSelectedRole() {
        return selectedRole;
    }

    public void setSelectedRole(Role selectedRole) {
        this.selectedRole = selectedRole;
    }

    public Role[] getRoles() {
        return Role.values();
    }

    public String getSearchKeyword() {
        return searchKeyword;
    }

    public void setSearchKeyword(String searchKeyword) {
        this.searchKeyword = searchKeyword;
    }

    public List<Staff> getFilteredStaffs() {
        return filteredStaffs;
    }

    public void setFilteredStaffs(List<Staff> filteredStaffs) {
        this.filteredStaffs = filteredStaffs;
    }

    public void setNurses(List<Staff> nurses){
        this.nurses = nurses;
    }

    public List<Staff> getNurses(){
        return this.nurses;
    }

    public void setReceptionists(List<Staff> receptionists){
        this.receptionists = receptionists;
    }

    public List<Staff> getReceptionists(){
        return receptionists;
    }



}
