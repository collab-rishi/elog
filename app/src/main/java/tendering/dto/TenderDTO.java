package tendering.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class TenderDTO {

	@NotNull(message = "Source location is required")
	@Size(min = 2, max = 100, message = "Source location must be between 2 and 100 characters")
	@Pattern(regexp = "^[A-Za-z\\s]+$", message = "Source location must contain only alphabets and spaces")
	private String sourceLocation;

	@NotNull(message = "Destination location is required")
	@Size(min = 2, max = 100, message = "Destination location must be between 2 and 100 characters")
	@Pattern(regexp = "^[A-Za-z\\s]+$", message = "Destination location must contain only alphabets and spaces")
	private String destinationLocation;

	@NotNull(message = "Pickup date is required")
	@FutureOrPresent(message = "Pickup date must be today or in the future")
	private LocalDate pickupDate;

	@NotNull(message = "Drop date is required")
	@Future(message = "Drop date must be in the future")
	private LocalDate dropDate;

	@NotNull(message = "Weight is required")
	@DecimalMin(value = "1.0", message = "Weight must be greater than 0")
	@DecimalMax(value = "5000.0", message = "Weight must be less than or equal to 5000 kg")
	private BigDecimal weight;

	@Size(max = 500, message = "Special instructions cannot exceed 500 characters")
	private String specialInstructions;

	@NotNull(message = "Tender price is required")
	@DecimalMin(value = "100.0", message = "Tender price must be greater than 100")
	@DecimalMax(value = "100000.0", message = "Tender price must be less than or equal to 100000")
	private BigDecimal tenderPrice;

	// Additional output-only fields
	private String tenderNo;
	private String status;
	private String createdBy;
	private LocalDateTime createdAt;

	@AssertTrue(message = "Drop date must be after the pickup date")
	public boolean isDropDateAfterPickupDate() {
		return dropDate.isAfter(pickupDate);
	}

	// Getters and Setters

	@Override
	public String toString() {
		return "TenderDTO{" + "sourceLocation='" + sourceLocation + '\'' + ", destinationLocation='"
				+ destinationLocation + '\'' + ", pickupDate=" + pickupDate + ", dropDate=" + dropDate + ", weight="
				+ weight + ", specialInstructions='" + specialInstructions + '\'' + ", tenderPrice=" + tenderPrice
				+ ", tenderNo='" + tenderNo + '\'' + ", status='" + status + '\'' + ", createdBy='" + createdBy + '\''
				+ ", createdAt=" + createdAt + '}';
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

	public String getTenderNo() {
		return tenderNo;
	}

	public void setTenderNo(String tenderNo) {
		this.tenderNo = tenderNo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
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
}
