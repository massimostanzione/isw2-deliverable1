package it.uniroma2.dicii.isw2.deliverable1.entities.jql;

import org.apache.http.client.utils.URIBuilder;
import org.eclipse.jgit.util.StringUtils;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A JIRA Query, as an intermediate layer for handling the "high-level" parameters
 * with respect to the "low-level" JQL syntax.
 */
public class JIRAQuery {
    private final static String JIRA_QUERY_PREFIX = "https://issues.apache.org/jira/rest/api/2/";
    private final static String JQL_QUERY_START = "jql";

    private final static String JIRA_PROJECT = "project";
    private final static String JIRA_FIELDS = "fields";
    private final static String JIRA_START_AT = "startAt";
    private final static String JIRA_MAX_RESULTS = "maxResults";

    private JIRAQueryType queryType;
    private JQLQuery JQLQuery = new JQLQuery();
    private Map<String, List<String>> URLParameters = new HashMap<>();
    private String projName;

    public JIRAQuery(JIRAQueryType queryType) {
        this.queryType = queryType;
    }

    public void setFields(String... fields) {
        this.URLParameters.put(JIRA_FIELDS, Arrays.asList(fields));
    }

    public void setStartAt(Integer startAt) {
        this.URLParameters.put(JIRA_START_AT, Arrays.asList(startAt.toString()));
    }

    public void setMaxResults(Integer maxResults) {
        this.URLParameters.put(JIRA_MAX_RESULTS, Arrays.asList(maxResults.toString()));
    }

    public JQLQuery getJQLQuery() {
        return JQLQuery;
    }

    /**
     * Compose the JQL Query and the final URL that will be used for the JIRA JQL query.
     *
     * @return the final URL for the JQL query
     */
    public URL compose() {
        URIBuilder b = null;
        URL url = null;
        try {
            if (this.queryType.equals(JIRAQueryType.JIRA_QUERY_TYPE_PROJECT)) {
                b = new URIBuilder(JIRA_QUERY_PREFIX + this.queryType.label + "/" + this.projName);
            } else {
                b = new URIBuilder(JIRA_QUERY_PREFIX + this.queryType.label + "?");
            }
            if (this.JQLQuery.getJQLPropertiesCount() > 0) {
                b.addParameter(JQL_QUERY_START, this.JQLQuery.compose());
            }
            for (Map.Entry<String, List<String>> entry : this.URLParameters.entrySet()) {
                if (!entry.getKey().equals(JIRA_PROJECT)) {
                    b.addParameter(entry.getKey(), StringUtils.join(entry.getValue(), ","));
                }
            }
            url = b.build().toURL();
        } catch (URISyntaxException | MalformedURLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return url;
    }

}
