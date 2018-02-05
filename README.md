# Sentiment analyzer

If not present create the necessary services
```
cf create-service google-ml-apis default gcp-ml
cf create-service p-service-registry standard registry
```
Upload the application with no-start flag
```
cf push -p target/analyzer-0.0.1-SNAPSHOT.jar --no-start
```
Bind the right services:
* Registry
```
cf bind-service sentiment-analyzer registry
```
* Bind the ML services
```
cf bind-service sentiment-analyzer gcp-ml -c '{"role": "ml.developer"}'
```

Start the application
```
cf start sentiment-analyzer
```
