# micronaut-httpclient2curl

A small Micronaut library that logs HTTP requests as ready-to-run `curl` commands, so you can copy a failing request straight out of your logs and replay it from a terminal.

It ships two filters:

- **`CurlRequestFilter`** — an `HttpClientFilter` that logs *outgoing* requests made through declarative `@Client` interfaces.
- **`CurlServerRequestFilter`** — an `HttpServerFilter` that logs *inbound* requests hitting `@Controller` endpoints.

Both filters build the curl command from the same logic (method, headers, body, URL) and print it to stdout, wrapped in separator lines, e.g.:

```
==============================================================
curl -X POST -H "Content-Type:application/json" -d '{"name":"Ada"}' "http://localhost:8080/api/applications/1"
==============================================================
```

Both filters are currently scoped to requests matching `/api/applications/**`.

## Requirements

Compiled targeting Java 25 bytecode — consumers need JDK 25+ at runtime.

## Installation

Published to GitHub Packages. GitHub Packages requires authentication to download even from public repositories — you need a GitHub **username** and a **Personal Access Token (PAT)** with `read:packages` scope (your account password will not work). Create one under GitHub Settings → Developer settings → Personal access tokens, then export it rather than hardcoding it:

```bash
export GITHUB_ACTOR=<your-github-username>
export GITHUB_TOKEN=<your-personal-access-token>
```

Add the repository and dependency to your Gradle build.

**Kotlin DSL (`build.gradle.kts`):**

```kotlin
repositories {
    maven {
        url = uri("https://maven.pkg.github.com/codeyogico/micronaut-httpclient2curl")
        credentials {
            username = System.getenv("GITHUB_ACTOR")
            password = System.getenv("GITHUB_TOKEN") // needs read:packages scope
        }
    }
}

dependencies {
    implementation("com.codeyogico:micronaut-httpclient2curl:<version>")
}
```

**Groovy DSL (`build.gradle`):**

```groovy
repositories {
    maven {
        url = uri("https://maven.pkg.github.com/codeyogico/micronaut-httpclient2curl")
        credentials {
            username = System.getenv("GITHUB_ACTOR")
            password = System.getenv("GITHUB_TOKEN") // needs read:packages scope
        }
    }
}

dependencies {
    implementation "com.codeyogico:micronaut-httpclient2curl:<version>"
}
```

## Configuration

Both filters are enabled by default and can be turned off independently via `application.yml`:

```yaml
curl-logger:
  client:
    enabled: false # disable logging for outgoing @Client requests
  server:
    enabled: false # disable logging for inbound @Controller requests
```

## Releasing

Publishing runs via the `Publish` GitHub Actions workflow (`.github/workflows/publish.yml`), triggered when a GitHub Release is published. The release tag (e.g. `v1.2.0`) is used as the published artifact version (`1.2.0`).
