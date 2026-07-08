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

Published via [JitPack](https://jitpack.io) — no credentials needed, same as `mavenCentral()`. JitPack builds the jar on demand straight from this repo's GitHub tags/releases.

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

No publish step required — cut a GitHub Release (or just push a tag) and JitPack builds that version the first time someone requests it.
