package tendering.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "lsp_responses")
public class LspResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tenderNo;
    private String lspCompanyName;

    // New field: The company name who created the tender (3PL)
    private String createdByCompanyName;

    private String sourceLocation;
    private String destinationLocation;

    private LocalDate pickupDate;
    private LocalDate dropDate;

    private BigDecimal weight;

    @Column(columnDefinition = "TEXT")
    private String specialInstructions;

    private BigDecimal tenderPrice;

    private LocalDate estimatedArrivalDate;
    private BigDecimal bidPrice;

    @Column(columnDefinition = "TEXT")
    private String lspMessage;
    @Column(columnDefinition = "TEXT")
    private String selectionMessage;  
    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    @Enumerated(EnumType.STRING)
    @Column(name = "selection_status")
    private SelectionStatus selectionStatus = SelectionStatus.PENDING;

    private LocalDateTime createdAt;

    public enum Status {
        ACTIVE,
        INPROCESS,
        COMPLETED,
        PENDING
    }

    public enum SelectionStatus {
        PENDING,
        CONFIRMED,
        REJECTED
    }

    // === Constructors ===

    public LspResponse() {
        this.createdAt = LocalDateTime.now();
        this.selectionStatus = SelectionStatus.PENDING;
    }

    public LspResponse(Long id, String tenderNo, String lspCompanyName, String sourceLocation,
                       String destinationLocation, LocalDate pickupDate, LocalDate dropDate,
                       BigDecimal weight, String specialInstructions, BigDecimal tenderPrice,
                       LocalDate estimatedArrivalDate, BigDecimal bidPrice, String lspMessage,
                       Status status, LocalDateTime createdAt, String createdByCompanyName,String selectionMessage) {
        this.id = id;
        this.tenderNo = tenderNo;
        this.lspCompanyName = lspCompanyName;
        this.sourceLocation = sourceLocation;
        this.destinationLocation = destinationLocation;
        this.pickupDate = pickupDate;
        this.dropDate = dropDate;
        this.weight = weight;
        this.specialInstructions = specialInstructions;
        this.tenderPrice = tenderPrice;
        this.estimatedArrivalDate = estimatedArrivalDate;
        this.bidPrice = bidPrice;
        this.lspMessage = lspMessage;
        this.status = status != null ? status : Status.ACTIVE;
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
        this.createdByCompanyName = createdByCompanyName;
        this.selectionStatus = SelectionStatus.PENDING;
        this.selectionMessage = selectionMessage;
    }

    // === Getters and Setters ===

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenderNo() {
        return tenderNo;
    }

    public void setTenderNo(String tenderNo) {
        this.tenderNo = tenderNo;
    }

    public String getLspCompanyName() {
        return lspCompanyName;
    }

    public void setLspCompanyName(String lspCompanyName) {
        this.lspCompanyName = lspCompanyName;
    }

    public String getCreatedByCompanyName() {
        return createdByCompanyName;
    }
    public String getSelectionMessage() { return selectionMessage; }
    public void setSelectionMessage(String selectionMessage) { this.selectionMessage = selectionMessage; }
    public void setCreatedByCompanyName(String createdByCompanyName) {
        this.createdByCompanyName = createdByCompanyName;
    }

    public String getSourceLocation() {
        return sourceLocation;
    }

    public void setSourceLocation(String sourceLocation) {
        this.sourceLocation = sourceLocation;
    }

    public String getDestinationLocation() {
        return destinationLocation;
    }

    public void setDestinationLocation(String destinationLocation) {
        this.destinationLocation = destinationLocation;
    }

    public LocalDate getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(LocalDate pickupDate) {
        this.pickupDate = pickupDate;
    }

    public LocalDate getDropDate() {
        return dropDate;
    }

    public void setDropDate(LocalDate dropDate) {
        this.dropDate = dropDate;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public String getSpecialInstructions() {
        return specialInstructions;
    }

    public void setSpecialInstructions(String specialInstructions) {
        this.specialInstructions = specialInstructions;
    }

    public BigDecimal getTenderPrice() {
        return tenderPrice;
    }

    public void setTenderPrice(BigDecimal tenderPrice) {
        this.tenderPrice = tenderPrice;
    }

    public LocalDate getEstimatedArrivalDate() {
        return estimatedArrivalDate;
    }

    public void setEstimatedArrivalDate(LocalDate estimatedArrivalDate) {
        this.estimatedArrivalDate = estimatedArrivalDate;
    }

    public BigDecimal getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(BigDecimal bidPrice) {
        this.bidPrice = bidPrice;
    }

    public String getLspMessage() {
        return lspMessage;
    }

    public void setLspMessage(String lspMessage) {
        this.lspMessage = lspMessage;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public SelectionStatus getSelectionStatus() {
        return selectionStatus;
    }

    public void setSelectionStatus(SelectionStatus selectionStatus) {
        this.selectionStatus = selectionStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // === Builder ===

    public static class Builder {
        private Long id;
        private String tenderNo;
        private String lspCompanyName;
        private String sourceLocation;
        private String destinationLocation;
        private LocalDate pickupDate;
        private LocalDate dropDate;
        private BigDecimal weight;
        private String specialInstructions;
        private BigDecimal tenderPrice;
        private LocalDate estimatedArrivalDate;
        private BigDecimal bidPrice;
        private String lspMessage;
        private String selectionMessage;
        private Status status = Status.ACTIVE;
        private LocalDateTime createdAt = LocalDateTime.now();
        private String createdByCompanyName;
        private SelectionStatus selectionStatus = SelectionStatus.PENDING;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder tenderNo(String tenderNo) {
            this.tenderNo = tenderNo;
            return this;
        }

        public Builder lspCompanyName(String lspCompanyName) {
            this.lspCompanyName = lspCompanyName;
            return this;
        }

        public Builder sourceLocation(String sourceLocation) {
            this.sourceLocation = sourceLocation;
            return this;
        }

        public Builder destinationLocation(String destinationLocation) {
            this.destinationLocation = destinationLocation;
            return this;
        }

        public Builder pickupDate(LocalDate pickupDate) {
            this.pickupDate = pickupDate;
            return this;
        }

        public Builder dropDate(LocalDate dropDate) {
            this.dropDate = dropDate;
            return this;
        }

        public Builder weight(BigDecimal weight) {
            this.weight = weight;
            return this;
        }

        public Builder specialInstructions(String specialInstructions) {
            this.specialInstructions = specialInstructions;
            return this;
        }

        public Builder tenderPrice(BigDecimal tenderPrice) {
            this.tenderPrice = tenderPrice;
            return this;
        }

        public Builder estimatedArrivalDate(LocalDate estimatedArrivalDate) {
            this.estimatedArrivalDate = estimatedArrivalDate;
            return this;
        }

        public Builder bidPrice(BigDecimal bidPrice) {
            this.bidPrice = bidPrice;
            return this;
        }

        public Builder lspMessage(String lspMessage) {
            this.lspMessage = lspMessage;
            return this;
        }
        public Builder selectionMessage(String selectionMessage) {  // ✅ New setter
            this.selectionMessage = selectionMessage;
            return this;
        }
        public Builder status(Status status) {
            this.status = status;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder createdByCompanyName(String createdByCompanyName) {
            this.createdByCompanyName = createdByCompanyName;
            return this;
        }

        public Builder selectionStatus(SelectionStatus selectionStatus) {
            this.selectionStatus = selectionStatus;
            return this;
        }

        public LspResponse build() {
            return new LspResponse(id, tenderNo, lspCompanyName, sourceLocation, destinationLocation,
                    pickupDate, dropDate, weight, specialInstructions, tenderPrice,
                    estimatedArrivalDate, bidPrice, lspMessage, status, createdAt, createdByCompanyName,selectionMessage);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
