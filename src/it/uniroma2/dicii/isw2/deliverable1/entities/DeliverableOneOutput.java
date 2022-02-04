package it.uniroma2.dicii.isw2.deliverable1.entities;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * A {fixMontYear, occurencies} couple, to be used as main deliverable I output.
 */
public class DeliverableOneOutput extends ExportableAsDatasetRecord {
    private Date date;
    private int occurrencies;

    public DeliverableOneOutput(Date date, int occurrencies) {
        this.date = date;
        this.occurrencies = occurrencies;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getOccurrencies() {
        return occurrencies;
    }

    public void setOccurrencies(int occurrencies) {
        this.occurrencies = occurrencies;
    }

    @Override
    public List<List<String>> getDatasetAttributes() {
        this.setDatasetAttributes("Date", "Occurrences");
        return this.datasetAttributes;
    }

    @Override
    public List<List<String>> getDatasetRecord() {
        Format f = new SimpleDateFormat("MMM yyyy");
        this.setDatasetRecord(f.format(this.date), this.occurrencies);
        return this.datasetRecord;
    }
}
