package tendering.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tenders")
public class Tender {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "tender_no", unique = true, nullable = false, length = 50)
	private String tenderNo;

	@Column(name = "source_location", nullable = false)
	private String sourceLocation;

	@Column(name = "destination_location", nullable = false)
	private String destinationLocation;

	@Column(name = "pickup_date", nullable = false)
	private LocalDate pickupDate;

	@Column(name = "drop_date", nullable = false)
	private LocalDate dropDate;

	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal weight;

	@Column(name = "special_instructions", columnDefinition = "TEXT")
	private String specialInstructions;

	
	@Enumerated(EnumType.STRING)
	@Column(name = "tender_status", nullable = false)
	private TenderStatus status = TenderStatus.ACTIVE;

	@Column(name = "created_by", nullable = false)
	private String createdBy;

	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt = LocalDateTime.now();

	@Column(name = "tender_price", nullable = false, precision = 10, scale = 2)
	private BigDecimal tenderPrice;

	public Tender() {
	}

	// Getters and Setters
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

	public TenderStatus getStatus() {
		return status;
	}

	public void setStatus(TenderStatus status) {
		this.status = status;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public BigDecimal getTenderPrice() {
		return tenderPrice;
	}

	public void setTenderPrice(BigDecimal tenderPrice) {
		this.tenderPrice = tenderPrice;
	}
}
