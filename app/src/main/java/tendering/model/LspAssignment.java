package tendering.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "lsp_assignments")
public class LspAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lsp_response_id", nullable = false, unique = true)
    private Long lspResponseId;

    @Column(name = "tender_no", nullable = false)
    private String tenderNo;

    @Column(name = "assigned_by_3pl", nullable = false)
    private String assignedBy3pl;

    @Column(name = "lsp_company_name", nullable = false)
    private String lspCompanyName;

    @Column(name = "vehicle_number", nullable = false)
    private String vehicleNumber;

    @Column(name = "driver_name", nullable = false)
    private String driverName;

    @Column(name = "driver_contact", nullable = false)
    private String driverContact;

    @Column(name = "dl_number", nullable = false)  
    private String dlNumber;

//    @Column(name = "selection_message")  
//    private String selectionMessage;

    @Column(name = "assigned_at", nullable = false)
    private LocalDateTime assignedAt = LocalDateTime.now();

    // Default constructor
    public LspAssignment() {}

    // All-args constructor
    public LspAssignment(Long lspResponseId, String tenderNo, String assignedBy3pl,
                         String lspCompanyName, String vehicleNumber, String driverName, 
                         String driverContact, String dlNumber) {
        this.lspResponseId = lspResponseId;
        this.tenderNo = tenderNo;
        this.assignedBy3pl = assignedBy3pl;
        this.lspCompanyName = lspCompanyName;
        this.vehicleNumber = vehicleNumber;
        this.driverName = driverName;
        this.driverContact = driverContact;
        this.dlNumber = dlNumber; 
        this.assignedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLspResponseId() {
        return lspResponseId;
    }

    public void setLspResponseId(Long lspResponseId) {
        this.lspResponseId = lspResponseId;
    }

    public String getTenderNo() {
        return tenderNo;
    }

    public void setTenderNo(String tenderNo) {
        this.tenderNo = tenderNo;
    }

    public String getAssignedBy3pl() {
        return assignedBy3pl;
    }

    public void setAssignedBy3pl(String assignedBy3pl) {
        this.assignedBy3pl = assignedBy3pl;
    }

    public String getLspCompanyName() {
        return lspCompanyName;
    }

    public void setLspCompanyName(String lspCompanyName) {
        this.lspCompanyName = lspCompanyName;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverContact() {
        return driverContact;
    }

    public void setDriverContact(String driverContact) {
        this.driverContact = driverContact;
    }

    public String getDlNumber() {
        return dlNumber;
    }

    public void setDlNumber(String dlNumber) {
        this.dlNumber = dlNumber;
    }

//    public String getSelectionMessage() {
//        return selectionMessage;
//    }
//
//    public void setSelectionMessage(String selectionMessage) {
//        this.selectionMessage = selectionMessage;
//    }

    public LocalDateTime getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(LocalDateTime assignedAt) {
        this.assignedAt = assignedAt;
    }

    @Override
    public String toString() {
        return "LspAssignment{" +
                "id=" + id +
                ", lspResponseId=" + lspResponseId +
                ", tenderNo='" + tenderNo + '\'' +
                ", assignedBy3pl='" + assignedBy3pl + '\'' +
                ", lspCompanyName='" + lspCompanyName + '\'' +
                ", vehicleNumber='" + vehicleNumber + '\'' +
                ", driverName='" + driverName + '\'' +
                ", driverContact='" + driverContact + '\'' +
                ", dlNumber='" + dlNumber + '\'' +  
//                ", selectionMessage='" + selectionMessage + '\'' +
                ", assignedAt=" + assignedAt +
                '}';
    }
}
