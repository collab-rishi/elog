package tendering.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;

@Entity
@Table(name = "lsp_responses")
public class LspResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tenderNo;

    @Column(nullable = false)
    private String lspCompanyName;

    @Column(nullable = false)
    private String sourceLocation;

    @Column(nullable = false)
    private String destinationLocation;

    @Column(nullable = false)
    private LocalDate pickupDate;

    @Column(nullable = false)
    private LocalDate dropDate;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal weight;

    @Column(columnDefinition = "TEXT")
    private String specialInstructions;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal tenderPrice;

    private LocalDate estimatedArrivalDate;

    @Column(precision = 10, scale = 2)
    private BigDecimal bidPrice;

    @Column(columnDefinition = "TEXT")
    private String lspMessage;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TenderStatus status = TenderStatus.ACTIVE;

    @Column(nullable = false)
    private Timestamp createdAt = new Timestamp(System.currentTimeMillis());

    // === Constructors ===

    public LspResponse() {}

    public LspResponse(String tenderNo, String lspCompanyName, String sourceLocation, String destinationLocation,
                       LocalDate pickupDate, LocalDate dropDate, BigDecimal weight, String specialInstructions,
                       BigDecimal tenderPrice) {
        this.tenderNo = tenderNo;
        this.lspCompanyName = lspCompanyName;
        this.sourceLocation = sourceLocation;
        this.destinationLocation = destinationLocation;
        this.pickupDate = pickupDate;
        this.dropDate = dropDate;
        this.weight = weight;
        this.specialInstructions = specialInstructions;
        this.tenderPrice = tenderPrice;
        this.status = TenderStatus.ACTIVE;
        this.createdAt = new Timestamp(System.currentTimeMillis());
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

    public TenderStatus getStatus() {
        return status;
    }

    public void setStatus(TenderStatus status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
