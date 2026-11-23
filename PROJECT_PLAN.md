# ServiceWatch - Shared Project Plan

**Status on 2026-11-23:** Ali estimates that about 25% of the whole project is complete. Phase 1 is in progress. This estimate reflects the work Ali has reported. The central system still needs its end-to-end verification.

## Project overview

ServiceWatch monitors two independent Java desktop applications, Sales and Stock. Each demo app uses the same SDK JAR to send logs to ServiceWatch and answer health checks. ServiceWatch stores the results in MySQL and displays them in its own desktop window. Only ServiceWatch connects to MySQL.

## Team and status key

| Owner | What they build |
|---|---|
| Ali - Phase 1 | The MySQL database and the entire central ServiceWatch application, including its desktop UI, log collector, health checks, and temporary verification programs. |
| Mohammad - Phase 2 | One reusable SDK, the Sales desktop app and its UI, and the Stock desktop app and its UI. |
| Ali + Mohammad - Phase 3 | Integration, final demonstration, runnable builds, README, and technical report. |

**DONE** means the listed code is reported present. **PARTIAL** means some work exists and the checkbox identifies what remains. **TODO** means work is pending. **WAITING** names the dependency that must be ready before implementation starts.

When using Claude Code: Work on the requesting teammate's items. Check the existing repository before creating or replacing code. Record verification separately from implementation. When another teammate owns a dependency, describe the handoff they need to provide.

## Phase 1 - Finish ServiceWatch

**Owner: Ali.** The three-day plan covers the remaining Phase 1 work.

### 1. Day 1 - Finish the database and log collector

- [x] **DONE - Ali - Protocol and models.** JSON conversion and message types exist for LOG, ACK, ERROR, PING, and PONG. The project has application, log, severity, and health-check models.
- [x] **DONE - Ali - Local MySQL setup.** Ali created the local database, seeded data, and added the JDBC connection class.
- [ ] **PARTIAL - Database setup for submission.** Verify the tables for applications, logs, and health-check history, their primary and foreign keys, and the SQL script that sets them up on another machine.
- [ ] **PARTIAL - Repositories and listener.** JDBC repository methods and a TCP listener with background workers exist. Resume here: confirm `findByServiceId()` is in `ApplicationRepository` and that `LogCollector` uses its actual return type, `MoniterApplication`.
- [ ] **TODO - Validate and save one log.** Check that the message type is LOG, the `serviceId` is registered, the timestamp parses, the severity is DEBUG, INFO, WARN, or ERROR, and the message text is non-empty. Save the application's database ID, event time, receipt time, severity, and text through `LogRepository`.
- [ ] **TODO - Reply to the sender.** Send `{"type":"ACK"}` only after the MySQL insert commits. Send `{"type":"ERROR","message":"reason"}` for invalid input, an unknown app, or a database failure. Write one UTF-8 JSON line, add a newline, flush, and close that client socket. Keep the listener available for the next connection.

### 2. Day 2 - Build the central ServiceWatch UI

- [ ] **TODO - Application registration.** Register and edit an app's name, host, and health port. Require a unique `serviceId`, keep it fixed after registration, validate inputs, and allow monitoring to be enabled or disabled.
- [ ] **TODO - Logs screen.** Show stored logs in a table and let the user open one log's details.
- [ ] **TODO - Filters and counts.** Combine application, severity, time range, and message-text filters. Show the matching total and counts by severity using those same filters.
- [ ] **TODO - Refresh.** Add manual and automatic refresh and display the last refresh time. Run database queries in background workers and update the screen on the UI thread.

### 3. Day 3 - Add health checks

- [ ] **TODO - Check each enabled app.** A background worker connects to the app's health port, sends PING, and expects PONG with that app's `serviceId`. Make the interval and connection/read timeouts configurable.
- [ ] **TODO - Store and display results.** Show UNKNOWN before the first check, UP for a valid reply, and DOWN for a failed connection, timeout, or invalid reply. Save each check's time, outcome, elapsed time, and failure reason when applicable. Show current health and previous checks in the monitor UI.
- [ ] **TODO - Clean shutdown.** Stop scheduled checks, close sockets, and release workers when ServiceWatch exits.

### 4. Day 3 - Pass the central-system checkpoint

Ali must complete this checkpoint before Mohammad starts SDK development. Ali can verify the monitor independently using two temporary Java programs.

- [ ] **TODO - Log sender.** Send a valid log, confirm its row exists in MySQL, and confirm the sender receives ACK. Send invalid input and confirm the sender receives ERROR.
- [ ] **TODO - Health responder.** Answer PING with PONG. Stop the responder to show DOWN, then restart it to show recovery to UP.
- [ ] **TODO - UI and persistence.** Verify filters, details, and counts while the monitor is receiving logs. Restart ServiceWatch and confirm registered apps, logs, and health history are still available. Verify clean shutdown.
- [ ] **TODO - Handoff to Mohammad.** Save commands and results as README evidence. Share the finalized JSON examples, service IDs, hosts, ports, timeouts, and expected replies. Confirm that Phase 1 passed.

## Phase 2 - Build the shared SDK and both demo apps

**Owner: Mohammad. Status: WAITING for Ali's Phase 1 checkpoint and finalized protocol.** Until then, Mohammad can review the requirements and plan the demo screens. Once Ali confirms the checkpoint, Mohammad starts with the SDK.

### 1. Build one reusable SDK

- [ ] **TODO - Library and configuration.** Create a separate Java library project. Configure the collector host/port, the app's `serviceId`, its local health port, and socket timeouts.
- [ ] **TODO - Logging API and queue.** Provide methods such as `info()`, `warn()`, and `error()`. Each call creates a timestamped log, adds it to a bounded thread-safe queue, and lets the UI continue.
- [ ] **TODO - Background sender.** Take a log from the queue, send one UTF-8 JSON line, read ACK or ERROR, and close the socket. Report a full queue, connection failure, or timeout. The host app must keep operating if the monitor is unavailable.
- [ ] **TODO - Health listener and shutdown.** Answer PING with PONG and the configured `serviceId`. On shutdown, stop workers, close sockets, set a finite wait, and document what happens to unsent logs.
- [ ] **TODO - Package the SDK.** Build one JAR for use by both demo projects.

### 2. Build the Sales and Stock desktop apps

- [ ] **TODO - Sales app and UI.** Create a separate desktop project with controls that generate recognizable Sales logs, such as `Sale completed`. Its button handlers call the SDK.
- [ ] **TODO - Stock app and UI.** Create a second desktop project with controls that generate recognizable Stock logs, such as `Stock update failed`. Its button handlers use the same SDK JAR.
- [ ] **TODO - Configure both apps.** Run each app as its own process with its own SDK instance, service ID, and health port. Provide actions that demonstrate INFO, WARN, and ERROR logs. Match the settings Ali registered in ServiceWatch.

## Phase 3 - Integrate, demonstrate, and submit

**Owners: Ali + Mohammad.** Final integration waits for both phases to be complete.

- [ ] **TODO - Demonstrate normal operation.** Start all three programs. Generate logs from Sales and Stock while health checks and refreshes run. Combine filters, open log details, and verify the displayed counts.
- [ ] **TODO - Demonstrate failure and recovery.** Stop one app and show DOWN within the configured check interval plus timeouts, visible by the next UI refresh. Restart it and show UP. Stop the monitor, show both apps keep working, restore it, and show new logs arriving. Demonstrate an ERROR reply for an invalid log.
- [ ] **TODO - Demonstrate persistence.** Restart ServiceWatch and show that registrations, logs, and health history survive.
- [ ] **TODO - Package the submission.** Include runnable builds, complete source, SDK JAR, dependency configuration, and MySQL setup scripts.
- [ ] **TODO - Write the README and integration guide.** Ali documents MySQL setup, monitor configuration, and Phase 1 evidence. Mohammad documents SDK usage, demo configuration, and queue/failure/shutdown behavior. Together, include the Java version, build/run commands, and protocol examples.
- [ ] **TODO - Write the 2-3 page report.** Include architecture and key-class diagrams, database schema, thread responsibilities, demonstration results, known limitations, and each member's contribution.

## Handoff notes

| Connection | Who initiates it | Expected result |
|---|---|---|
| Send a log | Mohammad's SDK connects to Ali's collector, planned port 9000 | ServiceWatch saves the log and replies ACK, or replies ERROR with a reason. |
| Check Sales health | Ali's monitor connects to the Sales SDK listener, planned port 9001 | Sales replies PONG with `serviceId` set to `sales`. |
| Check Stock health | Ali's monitor connects to the Stock SDK listener, planned port 9002 | Stock replies PONG with `serviceId` set to `stock`. |

Each connection carries one request and one reply, each written as one UTF-8 JSON line. Ali registers the apps in the monitor. Mohammad uses the matching IDs and ports in the SDK configuration. The SDK sends the stable `serviceId`. ServiceWatch looks up the internal MySQL ID and records when it received the log.
