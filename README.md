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

Available from two registries — pick one:

- **[JitPack](https://jitpack.io)** — no credentials needed; builds the jar on demand from this repo's tags/releases.
- **[GitHub Packages](https://github.com/CodeYogiCo/micronaut-httpclient2curl/packages)** — prebuilt artifacts published on each release; requires a GitHub personal access token with `read:packages` scope (GitHub Packages does not allow anonymous downloads).

### Option 1: JitPack

**Kotlin DSL (`build.gradle.kts`):**

```kotlin
repositories {
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation("com.github.codeyogico:micronaut-httpclient2curl:<tag>")
}
```

**Groovy DSL (`build.gradle`):**

```groovy
repositories {
    maven { url "https://jitpack.io" }
}

dependencies {
    implementation "com.github.codeyogico:micronaut-httpclient2curl:<tag>"
}
```

`<tag>` is any git tag/release (e.g. `v1.0.0`), a commit SHA, or `main-SNAPSHOT` for the latest commit on `main`. The first resolve after a new tag takes a little longer while JitPack builds it; after that it's cached.

### Option 2: GitHub Packages

Put your GitHub username and a token with `read:packages` scope in `~/.gradle/gradle.properties`:

```properties
gpr.user=YOUR_GITHUB_USERNAME
gpr.key=YOUR_GITHUB_TOKEN
```

**Kotlin DSL (`build.gradle.kts`):**

```kotlin
repositories {
    maven {
        url = uri("https://maven.pkg.github.com/CodeYogiCo/micronaut-httpclient2curl")
        credentials {
            username = findProperty("gpr.user") as String? ?: System.getenv("GITHUB_ACTOR")
            password = findProperty("gpr.key") as String? ?: System.getenv("GITHUB_TOKEN")
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
        url "https://maven.pkg.github.com/CodeYogiCo/micronaut-httpclient2curl"
        credentials {
            username = findProperty("gpr.user") ?: System.getenv("GITHUB_ACTOR")
            password = findProperty("gpr.key") ?: System.getenv("GITHUB_TOKEN")
        }
    }
}

dependencies {
    implementation "com.codeyogico:micronaut-httpclient2curl:<version>"
}
```

`<version>` is the release tag without the leading `v` (a `v1.0.0` release publishes as version `1.0.0`).

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

Cut a GitHub Release:

- **JitPack** builds that version on demand the first time someone requests it — no publish step.
- **GitHub Packages** — the `Publish to GitHub Packages` workflow runs automatically on release and publishes the artifacts, using the release tag (minus any leading `v`) as the version. It can also be run manually from the Actions tab, which publishes the current `main` as `0.1.0-SNAPSHOT`.
