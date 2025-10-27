package tendering.dto;

import java.util.Objects;

public class LspResponseFilterRequest {
    private String companyName;
    private String tender_no;

    public LspResponseFilterRequest() {
    }

    public LspResponseFilterRequest(String companyName, String tender_no) {
        this.companyName = companyName;
        this.tender_no = tender_no;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getTender_no() {
        return tender_no;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setTender_no(String tender_no) {
        this.tender_no = tender_no;
    }

    @Override
    public String toString() {
        return "LspResponseFilterRequest{" +
                "companyName='" + companyName + '\'' +
                ", tender_no='" + tender_no + '\'' +
                '}';
    }
}
