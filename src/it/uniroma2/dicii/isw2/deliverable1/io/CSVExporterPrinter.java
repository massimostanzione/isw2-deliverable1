package it.uniroma2.dicii.isw2.deliverable1.io;

import it.uniroma2.dicii.isw2.deliverable1.entities.ExportableAsDatasetRecord;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A CSV exporter tool, used to export dataset in a standardized way.
 */
public class CSVExporterPrinter extends ExporterPrinter {
    private static CSVExporterPrinter instance;
    private CSVExporterPrinter() {
        super();
    }

    public static CSVExporterPrinter getSingletonInstance() {
        if (instance == null)
            instance = new CSVExporterPrinter();

        return instance;
    }

    /**
     * Export dataset to a CSV file
     *
     * @param dataset dataset to be exported
     * @param outname output file
     */
    public void export(List<List<String>> dataset, String outname) {
        outname = System.getProperty("user.dir") + outname;
        printLog(outname);
        try {
            File file = new File(outname);
            file.getParentFile().mkdirs();
            boolean alreadyExisted = file.createNewFile();
            if (alreadyExisted)
                log.finer("CSV target fil already existed.");
            fileWriter = new FileWriter(file);
            Integer i = -1;
            Integer j = -1;
            if (!dataset.isEmpty()) {
                Integer recordDim = dataset.get(0).size();
                for (List<String> datasetRecord : dataset) {
                    i = i + 1;
                    j = -1;
                    for (String value : datasetRecord) {
                        j++;
                        fileWriter.append(value);
                        fileWriter.append(j + 1 < recordDim ? "," : "\n");
                    }
                }
            }
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adapt dataset to a standardized dataset that can be arranged with comma separators.
     *
     * @param objList dataset to be adapted. Must inherit <code>ExportableAsDatasetRecord</code>
     * @return adapted dataset
     * @see ExportableAsDatasetRecord
     */
    public List<List<String>> convertToCSVExportable(List<?> objList) {
        List<List<String>> ret = new ArrayList<>();
        if (!objList.isEmpty()) {
            ret = Stream
                    .concat(ret.stream(), ((ExportableAsDatasetRecord) objList.get(0)).getDatasetAttributes().stream())
                    .collect(Collectors.toList());
            for (Object obj : objList) {
                ret = Stream.concat(ret.stream(), ((ExportableAsDatasetRecord) obj).getDatasetRecord().stream())
                        .collect(Collectors.toList());
            }
        }
        return ret;
    }

    /**
     * Adapt a dataset and export it to a CSV in a standardized way.
     *
     * @param objList dataset to be adapted and exported
     * @param path    output file name
     */
    public void convertAndExport(List<?> objList, String path) {
        export(convertToCSVExportable(objList), path);
    }
}
