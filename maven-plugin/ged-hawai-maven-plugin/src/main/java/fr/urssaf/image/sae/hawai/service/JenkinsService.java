package fr.urssaf.image.sae.hawai.service;


import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.apache.maven.plugin.MojoExecutionException;
import org.json.JSONObject;


public class JenkinsService {

    public String getJenkinsBuildUsername(String buildBaseUrl) throws MojoExecutionException {
        String errorMsg = "impossible de récupérer l'identifiant ANAIS à partir de l'url ";

        String result = null;
        Content c;
        String url = buildBaseUrl + "/api/json";
        try {
            c = Request.Get(url).execute().returnContent();
        } catch (ClientProtocolException e) {
            throw new MojoExecutionException(errorMsg + url + " : " + e.getMessage(), e);
        } catch (IOException e) {
            throw new MojoExecutionException(errorMsg + url + " : " + e.getMessage(), e);
        }

        if (!c.getType().getMimeType().contains("json")) {
            throw new MojoExecutionException(
                    errorMsg + " le mimetype " + c.getType().getMimeType() + " n'est pas celui attendu (json)");
        }

        JSONObject obj = new JSONObject(c.toString());

        String description = "";
        if (obj.has("actions")) {
            for (int i = 0; i < obj.getJSONArray("actions").length(); i++) {
                JSONObject jsonLvl1 = obj.getJSONArray("actions").getJSONObject(i);
                if (jsonLvl1.has("causes")) {
                    for (int a = 0; a < jsonLvl1.getJSONArray("causes").length(); a++) {
                        JSONObject jsonLvl2 = jsonLvl1.getJSONArray("causes").getJSONObject(a);
                        if (jsonLvl2.has("userId")) {
                            result = jsonLvl2.getString("userId");
                        }
                        if (jsonLvl2.has("shortDescription")) {
                            description = jsonLvl2.getString("shortDescription");
                        }
                    }
                }
            }
        }

        if (StringUtils.isBlank(result)) {
            throw new MojoExecutionException(
                    "Impossible de récupéréer l'identifiant ANAIS de l'utilisateur ayant lancé le job. Le job a-t-il été lancé manuellement ? (cause :"
                            + description + ")");
        }


        return result;
    }

}
