package tendering.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class LspResponseViewDTO {
    private String lspCompanyName;
    private BigDecimal bidPrice;
    private LocalDate estimatedArrivalDate;
    private String lspMessage;

    public LspResponseViewDTO() {
    }

    public LspResponseViewDTO(String lspCompanyName, BigDecimal bidPrice, LocalDate estimatedArrivalDate, String lspMessage) {
        this.lspCompanyName = lspCompanyName;
        this.bidPrice = bidPrice;
        this.estimatedArrivalDate = estimatedArrivalDate;
        this.lspMessage = lspMessage;
    }

    public String getLspCompanyName() {
        return lspCompanyName;
    }

    public void setLspCompanyName(String lspCompanyName) {
        this.lspCompanyName = lspCompanyName;
    }

    public BigDecimal getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(BigDecimal bidPrice) {
        this.bidPrice = bidPrice;
    }

    public LocalDate getEstimatedArrivalDate() {
        return estimatedArrivalDate;
    }

    public void setEstimatedArrivalDate(LocalDate estimatedArrivalDate) {
        this.estimatedArrivalDate = estimatedArrivalDate;
    }

    public String getLspMessage() {
        return lspMessage;
    }

    public void setLspMessage(String lspMessage) {
        this.lspMessage = lspMessage;
    }

    @Override
    public String toString() {
        return "LspResponseViewDTO{" +
                "lspCompanyName='" + lspCompanyName + '\'' +
                ", bidPrice=" + bidPrice +
                ", estimatedArrivalDate=" + estimatedArrivalDate +
                ", lspMessage='" + lspMessage + '\'' +
                '}';
    }
}
