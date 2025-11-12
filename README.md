# Grid Comparator POC

This repository contains a simple Serenity+Cucumber test and POC tooling to compare non-containerized Selenium Grid vs Moon (containerized via Docker) for remote browser execution.

What I added for the POC
- `PerfReporter` (writes browser navigation timing JSON + some fields to `build/test-results/perf-results.csv`)
- `PerfHooks` (Cucumber hooks that record scenario durations in the same CSV)
- `WebDriverModeConfigurator` (reads `-Dremote.execution` and sets Serenity system properties to use remote mode)
- Instructions and helper files to run Moon and Selenium Grid and to compute simple metrics

Quick checklist
- [x] Instrument tests to record timings
- [x] Ability to select remote execution mode at runtime via `-Dremote.execution`
- [x] Docker Compose for Moon (simple POC)
- [x] Scripts/instructions to start non-containerized Selenium Grid
- [x] Small parser to compute averages from the CSV

How it works
- By default tests run locally with the local Chrome driver (same as before).
- To run tests against a remote WebDriver, set `-Dremote.execution=grid` (Selenium Grid) or `-Dremote.execution=moon` (Moon) and pass `-Dremote.webdriver.url` with the remote URL (defaults to `http://localhost:4444/wd/hub` in `src/test/resources/serenity.properties`).
- Tests will append lines into `build/test-results/perf-results.csv`. Use the parser to compute average scenario duration and navigation timings across runs.

Start Moon (Docker)

This is a small, pragmatic docker-compose to run Moon as a single container that will orchestrate browser containers inside Docker. See Moon docs for production-ready options and recommended configuration: https://aerokube.com/moon/latest/

# Run with Docker Compose (Linux/macOS) or Git Bash on Windows
```bash
# Start Moon
docker compose -f docker-compose-moon.yml up -d

# (Optional) View logs
docker compose -f docker-compose-moon.yml logs -f

# Stop Moon
docker compose -f docker-compose-moon.yml down
```

Windows notes

If you're running Docker Desktop on Windows, the default `docker-compose-moon.yml` mounts `/var/run/docker.sock` which doesn't exist on Windows. That causes Moon to attempt in-cluster Kubernetes configuration and fail with messages like:

```
create incluster config: unable to load in-cluster configuration, KUBERNETES_SERVICE_HOST and KUBERNETES_SERVICE_PORT must be defined
```

To run Moon on Windows use one of the options below:

1) Named pipe mount (quick, but may require elevated privileges)
- Use `docker-compose-moon-windows.yml` which mounts the Windows named pipe `//./pipe/docker_engine` into the container as `/var/run/docker.sock`.
- This is convenient but sometimes blocked by Docker Desktop settings. If the container still fails and logs show the `in-cluster` error, try the TCP option below.

2) TCP daemon (recommended for local POC when named pipe fails)
- Enable Docker Desktop option: Settings -> General -> "Expose daemon on tcp://localhost:2375 without TLS" and apply.
- Then use `docker-compose-moon-windows-tcp.yml` which configures Moon with DOCKER_HOST pointing to `host.docker.internal:2375`.

# Run Moon on Windows (Docker Desktop) - named pipe
```bash
# Start Moon using the Windows compose (named pipe)
docker compose -f docker-compose-moon-windows.yml up -d

# View logs
docker compose -f docker-compose-moon-windows.yml logs -f moon

# Stop
docker compose -f docker-compose-moon-windows.yml down
```

# Run Moon on Windows (Docker Desktop) - TCP daemon
```bash
# After enabling TCP daemon expose in Docker Desktop:
docker compose -f docker-compose-moon-windows-tcp.yml up -d

docker compose -f docker-compose-moon-windows-tcp.yml logs -f moon

docker compose -f docker-compose-moon-windows-tcp.yml down
```

Troubleshooting
- If Moon logs keep showing `create incluster config` errors, ensure the container has access to the Docker engine (either the named pipe mount or the TCP daemon). Inspect the container mounts and env (`docker inspect moon`).
- Check for permission errors or Windows Defender blocking access to the named pipe.
- As a fallback, run Moon and browsers on a Linux VM (WSL2 or an EC2 instance) where `/var/run/docker.sock` works reliably.

Start a non-containerized Selenium Grid (Selenium Server Jar)

Selenium Grid can be started from the Selenium Server standalone JAR. Download a Selenium Server jar from the Selenium releases: https://www.selenium.dev/downloads/

Example (run on the machine that will host the Grid):

```bash
# Download (example URL may change; get the latest selenium-server jar from selenium.dev)
curl -LO https://github.com/SeleniumHQ/selenium/releases/download/selenium-4.9.0/selenium-server-4.9.0.jar

# Start the standalone Grid on port 4444 (this runs hub+node on the same process)
java -jar selenium-server-4.9.0.jar standalone --port 4444
```

Note: For a non-containerized node farm, install browser drivers on the host and configure nodes accordingly. The above `standalone` is the simplest way to get a single-process Grid.

Run the tests

Local execution (default):

```bash
./gradlew test
# Windows (cmd.exe)
gradlew.bat test
```

Selenium Grid (non-containerized)

```bash
./gradlew test -Dremote.execution=grid -Dremote.webdriver.url=http://<GRID_HOST>:4444/wd/hub
# Windows
gradlew.bat test -Dremote.execution=grid -Dremote.webdriver.url=http://<GRID_HOST>:4444/wd/hub
```

Moon (Docker)

By default `docker-compose-moon.yml` exposes Moon on `http://localhost:4444`. Run tests with:

```bash
./gradlew test -Dremote.execution=moon -Dremote.webdriver.url=http://localhost:4444/wd/hub
# Windows
gradlew.bat test -Dremote.execution=moon -Dremote.webdriver.url=http://localhost:4444/wd/hub
```

Analyze results

After test runs the CSV will be in `build/test-results/perf-results.csv`. Use the simple parser to compute averages:

```bash
python3 tools/parse_perf.py build/test-results/perf-results.csv
```

The parser prints averages for scenario durations and loadEventEnd if present. You can use these values to compare Moon vs Grid performance by running several runs for each mode and comparing the averages.

Next steps / caveats
- This POC focuses on functionality and simplicity. For production comparisons you should:
  - Run many iterations, control for cold-start vs warm containers, and measure container/host CPU and memory (e.g., `docker stats` for Moon and OS metrics for Grid machines).
  - Use Moon's built-in metrics or Prometheus integration (see Moon docs) and Selenium Grid's observability options.
  - Tune browser image selection, concurrency, and VM/container sizing to match realistic load.

If you want, I can: provide a sample script to repeatedly run the test suite and aggregate metrics; add Prometheus scrape configuration for Moon; or provide a PowerShell/Batch wrapper for Windows runs.
