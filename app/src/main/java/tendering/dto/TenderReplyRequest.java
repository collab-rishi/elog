package tendering.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TenderReplyRequest {

    @NotNull(message = "Estimated arrival date is required")
    @FutureOrPresent(message = "Estimated arrival date must be today or in the future")
    private LocalDate estimatedArrivalDate;

    @NotNull(message = "Bid price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Bid price must be greater than 0")
    private BigDecimal bidPrice;

    @NotBlank(message = "LSP message is required")
    private String lspMessage;

    // Getters and Setters
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
}
