package it.uniroma2.dicii.isw2.deliverable1;

import it.uniroma2.dicii.isw2.deliverable1.entities.Project;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class, as a "start point" for the program.
 */
public class Main {

    public static void main(String[] args) {
        Project p1 = new Project("S2GRAPH", "https://github.com/apache/incubator-s2graph", "");
        List<Project> beingAnalyzed = new ArrayList<>();
        beingAnalyzed.add(p1);
        for (Integer i = 0; i < beingAnalyzed.size(); i++) {
            try {
                new ProjectAnalyzer(beingAnalyzed.get(i)).run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
