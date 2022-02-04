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
    /**
     * Export dataset to a CSV file
     *
     * @param dataset dataset to be exported
     * @param outname output file
     */
    public static void export(List<List<String>> dataset, String outname) {
        outname = System.getProperty("user.dir") + outname;
        printLog(outname);
        try {
            File file = new File(outname);
            file.getParentFile().mkdirs();
            file.createNewFile();
            fileWriter = new FileWriter(file);
            Integer i = -1, j = -1;
            if (dataset.size() > 0) {
                Integer recordDim = dataset.get(0).size();
                for (List<String> record : dataset) {
                    i++;
                    j = -1;
                    for (String value : record) {
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
    public static List<List<String>> convertToCSVExportable(List<?> objList) {
        List<List<String>> ret = new ArrayList<>();
        if (objList.size() > 0) {
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
    public static void convertAndExport(List<?> objList, String path) {
        export(convertToCSVExportable(objList), path);
    }
}
