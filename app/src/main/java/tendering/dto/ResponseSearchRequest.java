// --- DTO: ResponseSearchRequest.java ---
package tendering.dto;

import jakarta.validation.constraints.NotBlank;

public class ResponseSearchRequest {

    @NotBlank(message = "Company name must not be blank")
    private String companyName;

    @NotBlank(message = "Status must not be blank")
    private String status;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}