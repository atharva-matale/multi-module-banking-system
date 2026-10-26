# Multi-Module Banking System (Gradle)

Requires Java 17+ and Gradle 8+ installed.

Run from this folder:

```powershell
gradle clean build
gradle :web:run
```

Test in browser: http://localhost:8080/balance

In a second PowerShell window:

```powershell
Invoke-RestMethod -Method Post 'http://localhost:8080/deposit?amount=500'
Invoke-RestMethod -Method Post 'http://localhost:8080/withdraw?amount=200'
```

Build artifact: `web/build/libs/web-1.0.0.jar`.

Run the deployable artifact: `java -jar web/build/libs/web-1.0.0.jar`.

Demo only: in-memory single account; no database, authentication, or persistence. Do not use for real banking.
