

Demo AsyncAPI application.

### Usage 
1. run kafka: 
`podman-compose -f docker/kafka/docker-compose.yml up -d` (todo: move to pods)

2. run app and publish file to s3 https://play.min.io : 
`curl -v  -F file=@123 localhost:8080/storage`

3. check messages at http://linux.local:8000

### Configuration
S3 configuration: `resources/application.yaml`

api specification: `resources/api/`

### Linter
[spectral](https://github.com/stoplightio/spectral)

1. install `npm install -g @stoplight/spectral-cli`
2. lint
```bash 
cd src/main/resources/api
echo '{\n\t"extends": ["spectral:oas", "spectral:asyncapi"]\n}' > .spectral.json
spectral lint s3ChangelogAPI.yaml
```
### Generator
* [generator](https://github.com/asyncapi/generator)
* [java-spring-template](https://github.com/asyncapi/java-spring-template)

1. install
```bash
npm install -g @asyncapi/generator
npm install -g @asyncapi/java-spring-template
```
2. generate

`ag -o ./java s3ChangelogAPI.yaml @asyncapi/java-spring-template -p javaPackage=local.asyncapidemo.kafkaAdapter`


