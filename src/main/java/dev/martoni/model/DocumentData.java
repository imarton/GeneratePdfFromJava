package dev.martoni.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DocumentData {
    private String id;
    private Date issueDate;
    private String issuedBy;
    private List<Asset> assets = new ArrayList<>();
    private String previousOwner;
    private String previousLocation;
    private String newOwner;
    private String newLocation;
    private String signer;

    public DocumentData() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Date getIssueDate() { return issueDate; }
    public void setIssueDate(Date issueDate) { this.issueDate = issueDate; }

    public String getIssuedBy() { return issuedBy; }
    public void setIssuedBy(String issuedBy) { this.issuedBy = issuedBy; }

    public List<Asset> getAssets() { return assets; }
    public void setAssets(List<Asset> assets) { this.assets = assets; }

    public String getPreviousOwner() { return previousOwner; }
    public void setPreviousOwner(String previousOwner) { this.previousOwner = previousOwner; }

    public String getPreviousLocation() { return previousLocation; }
    public void setPreviousLocation(String previousLocation) { this.previousLocation = previousLocation; }

    public String getNewOwner() { return newOwner; }
    public void setNewOwner(String newOwner) { this.newOwner = newOwner; }

    public String getNewLocation() { return newLocation; }
    public void setNewLocation(String newLocation) { this.newLocation = newLocation; }

    public String getSigner() { return signer; }
    public void setSigner(String signer) { this.signer = signer; }
}
