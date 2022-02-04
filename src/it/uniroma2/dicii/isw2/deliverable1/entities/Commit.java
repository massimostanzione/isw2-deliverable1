package it.uniroma2.dicii.isw2.deliverable1.entities;

import org.eclipse.jgit.revwalk.RevCommit;

import java.util.Date;
import java.util.List;

/**
 * A single commit.
 * This is a separated entity with respect to <code>RevCommit</code> from Git,
 * free of all the unnecessary Git details and easier to manage.
 * An association with the corresponding <code>RevCommit</code> is always preserved.
 */
public class Commit extends ExportableAsDatasetRecord {
    private RevCommit referredRawCommit;
    private String commitID;
    private String commitMsg;
    private Date date;

    public Commit(RevCommit iteratedCommit) {
        this.referredRawCommit = iteratedCommit;
        this.commitID = this.referredRawCommit.getName();
        this.commitMsg = this.referredRawCommit.getFullMessage();
        this.date = this.referredRawCommit.getAuthorIdent().getWhen();
    }

    public String getCommitID() {
        return commitID;
    }

    public void setCommitID(String commitID) {
        this.commitID = commitID;
    }

    public String getCommitMsg() {
        return commitMsg;
    }

    public void setCommitMsg(String commitMsg) {
        this.commitMsg = commitMsg;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


    @Override
    public List<List<String>> getDatasetAttributes() {
        this.setDatasetAttributes("ID", "Date");
        return this.datasetAttributes;
    }

    @Override
    public List<List<String>> getDatasetRecord() {
        this.setDatasetRecord(this.commitID, this.date);
        return this.datasetRecord;
    }
}
