package tendering.dto;

import jakarta.validation.constraints.NotBlank;

public class LspConfirmationRequest {

    @NotBlank(message = "Tender number is required")
    private String tenderNo;

    @NotBlank(message = "LSP company name is required")
    private String lspCompanyName;

    private String selectionMessage;  // Optional field

    // Default constructor
    public LspConfirmationRequest() {}

    // All-args constructor
    public LspConfirmationRequest(String tenderNo, String lspCompanyName, String selectionMessage) {
        this.tenderNo = tenderNo;
        this.lspCompanyName = lspCompanyName;
        this.selectionMessage = selectionMessage;
    }

    // Getters and Setters
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

    public String getSelectionMessage() {
        return selectionMessage;
    }

    public void setSelectionMessage(String selectionMessage) {
        this.selectionMessage = selectionMessage;
    }

    @Override
    public String toString() {
        return "LspConfirmationRequest{" +
                "tenderNo='" + tenderNo + '\'' +
                ", lspCompanyName='" + lspCompanyName + '\'' +
                ", selectionMessage='" + selectionMessage + '\'' +
                '}';
    }
}
