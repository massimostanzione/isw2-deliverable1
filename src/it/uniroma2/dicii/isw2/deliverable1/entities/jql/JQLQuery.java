package it.uniroma2.dicii.isw2.deliverable1.entities.jql;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A JQL query, handling all the "low-level" JQL tags an details for a JIRA query.
 */
public class JQLQuery {
    // A collection of JQL tags
    private final static String JQL_PROJECT_NAME = "project";
    private final static String JQL_ISSUE_TYPE = "issueType";
    private final static String JQL_STATUS = "status";
    private final static String JQL_RESOLUTION = "resolution";

    public final static String JQL_FIELDS = "fields";
    public final static String JQL_ISSUE_TYPE_BUG = "Bug";
    public final static String JQL_STATUS_CLOSED = "closed";
    public final static String JQL_STATUS_RESOLVED = "resolved";
    public final static String JQL_STATUS_DONE = "done";
    public final static String JQL_RESOLUTION_FIXED = "fixed";
    public final static String JQL_RESOLUTION_DONE = "done";
    public final static String JQL_FIELD_KEY = "key";
    public final static String JQL_FIELD_RESOLUTIONDATE = "resolutiondate";
    public final static String JQL_FIELD_FIXVERSIONS = "fixVersions";
    public final static String JQL_FIELD_CREATED = "created";

    private Map<String, List<String>> JQLProperties = new HashMap<>();

    public Integer getJQLPropertiesCount() {
        return JQLProperties.size();
    }

    public void setProjectName(String projName) {
        this.JQLProperties.put(JQL_PROJECT_NAME, Arrays.asList(projName));
    }

    public void setIssueType(String... issueTypes) {
        this.JQLProperties.put(JQL_ISSUE_TYPE, Arrays.asList(issueTypes));
    }

    public void setStatus(String... statusList) {
        this.JQLProperties.put(JQL_STATUS, Arrays.asList(statusList));
    }

    public void setResolution(String... resolution) {
        this.JQLProperties.put(JQL_RESOLUTION, Arrays.asList(resolution));
    }

    /**
     * Compose a ready-to-use JQL query.
     *
     * @return a JQL query.
     */
    public String compose() {
        String ret = null;
        Integer i = -1;

        ret = "(";
        for (Map.Entry<String, List<String>> entry : this.JQLProperties.entrySet()) {
            i++;
            for (Integer j = 0; j < entry.getValue().size(); j++) {
                ret += "\"" + entry.getKey() + "\"=";
                ret += "\"" + entry.getValue().get(j) + "\"";
                if (j < entry.getValue().size() - 1) {
                    ret += "OR";
                }
            }
            ret += ")";
            if (i < this.JQLProperties.entrySet().size() - 1) {
                ret += "AND(";
            }
        }
        return ret;
    }
}
