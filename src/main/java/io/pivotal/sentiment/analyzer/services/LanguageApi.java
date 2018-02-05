package io.pivotal.sentiment.analyzer.services;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.language.v1.AnalyzeSentimentResponse;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.LanguageServiceSettings;
import io.pivotal.sentiment.analyzer.domain.ScoreAndMagnitude;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

@Component
public class LanguageApi {

    private final static Logger logger = LoggerFactory.getLogger(LanguageApi.class);

    @Value("${vcap.services.gcp-ml.credentials.PrivateKeyData}")
    private String privateKeyData;

    private LanguageServiceSettings serviceSettings = null;

    public LanguageApi() {
    }

    private void init() {
        if ((serviceSettings == null) && (privateKeyData != null)) {
            ByteArrayInputStream stream = new ByteArrayInputStream(Base64Utils.decodeFromString(privateKeyData));
            try {
                ServiceAccountCredentials credentials = ServiceAccountCredentials.fromStream(stream);
                FixedCredentialsProvider provider = FixedCredentialsProvider.create(credentials);
                serviceSettings = LanguageServiceSettings.newBuilder().setCredentialsProvider(provider).build();
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    public ScoreAndMagnitude analyze(String content) {
        init();
        if(serviceSettings!=null) {
            try (LanguageServiceClient language = LanguageServiceClient.create(serviceSettings)) {
                Document doc = Document.newBuilder()
                        .setContent(content)
                        .setType(Document.Type.PLAIN_TEXT)
                        .build();
                AnalyzeSentimentResponse response = language.analyzeSentiment(doc);

                return Optional
                        .ofNullable(response)
                        .map(AnalyzeSentimentResponse::getDocumentSentiment)
                        .map(s -> new ScoreAndMagnitude(s.getScore(), s.getMagnitude()))
                        .orElseGet(ScoreAndMagnitude::new);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
        return new ScoreAndMagnitude();
    }
}
