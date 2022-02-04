package it.uniroma2.dicii.isw2.deliverable1.entities;

import java.util.Date;
import java.util.List;

/**
 * A single ticket.
 */
public class Ticket extends ExportableAsDatasetRecord {

    private String id;
    private Date creationTimestamp;
    private Date fixTimestamp;
    private List<Commit> commitList;

    public Ticket(String key) {
        this.id = key;
    }

    public String getId() {
        return id;
    }

    public void setId(String iD) {
        id = iD;
    }

    public Date getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(Date creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public Date getFixTimestamp() {
        return fixTimestamp;
    }

    public void setFixTimestamp(Date fixTimestamp) {
        this.fixTimestamp = fixTimestamp;
    }

    public List<Commit> getCommitList() {
        return commitList;
    }

    public void setCommitList(List<Commit> commitList) {
        this.commitList = commitList;
    }

    @Override
    public List<List<String>> getDatasetAttributes() {
        this.setDatasetAttributes("ID", "Fixed");
        return this.datasetAttributes;
    }

    @Override
    public List<List<String>> getDatasetRecord() {
        this.setDatasetRecord(this.id, this.fixTimestamp);
        return this.datasetRecord;
    }
}
