# Breakpoint Tracker Plugin for JetBrains IDEs

## Overview
The **Breakpoint Tracker Plugin** is a JetBrains IDE plugin that provides a real-time count of active breakpoints in a project. It displays this information in a dedicated **tool window**, ensuring that developers can easily track and manage their breakpoints.

Key features include:
- **Real-time breakpoint tracking**: Updates automatically when breakpoints are added or removed.
- **File-specific details**: Displays the file locations of all active breakpoints.
- **JCEF-based UI**: Uses **JCEF (JetBrains Chromium Embedded Framework)** for an enhanced user interface.
- **Standalone frontend server** (Optional): Implements a separate frontend server module for better modularity and performance.

---
## Installation
### Prerequisites
- IntelliJ IDEA **2023.3+** (or any compatible JetBrains IDE)
- Java **17+**
- Node.js **18+** (for frontend build)
- Gradle **8.0+**

### Installing from Source
1. Clone the repository:
   ```sh
   git clone <repository-url>
   cd BreakpointTracker
   ```
2. Build and install the plugin:
   ```sh
   ./gradlew buildPlugin
   ```
3. Locate the generated `.zip` file in `build/distributions/`.
4. Open JetBrains IDE, go to **Settings > Plugins > Install Plugin from Disk**.
5. Select the `.zip` file and install the plugin.

### Running in Development Mode
To test the plugin within IntelliJ:
```sh
./gradlew runIde
```
This will start a sandboxed IntelliJ instance with the plugin loaded.

---
## Usage
### Opening the Tool Window
1. Open **View > Tool Windows > Breakpoint Tracker**.
2. The tool window displays:
   - The total number of breakpoints in the project.
   - A list of files containing breakpoints with their respective count.

### Live Updates
- Adding or removing a breakpoint updates the displayed count instantly.
- The UI automatically refreshes when a breakpoint is modified.

---
## Technical Details
### Architecture
1. **Backend (Kotlin + IntelliJ API)**
   - Listens for breakpoint changes using IntelliJ’s debugging API.
   - Starts a lightweight embedded HTTP server using **NanoHTTPD** (if using frontend server module).
   - Communicates with the frontend via JSON messages.

2. **Frontend (Vue.js + JCEF)**
   - A Vue.js app renders the breakpoint data.
   - Served via **NanoHTTPD** at `http://localhost:5173/index.html`.
   - Uses WebSockets or polling to receive updates from the backend.

### Key Components
- `BreakpointListenerService`: Monitors breakpoints and sends updates.
- `MyToolWindowFactory`: Initializes and manages the tool window.
- `FrontendServer`: Serves the Vue-based UI if the separate frontend server is enabled.

---
## Development & Contribution
### Building the Frontend Automatically
To ensure the frontend is built before `buildPlugin`, Gradle is configured to:
1. Run `npm install` in the `frontend/` directory.
2. Run `npm run build`.
3. Copy the generated `dist/` folder into `src/main/resources/frontend/`.

This process runs automatically when executing:
```sh
./gradlew buildPlugin
```

### Code Structure
```
BreakpointTracker/
├── frontend/           # Vue.js frontend
│   ├── src/
│   ├── dist/           # Built frontend (copied to resources)
│   ├── package.json
│   ├── vite.config.js
│   └── ...
├── src/main/kotlin/
│   ├── com/github/aleksaqm/breakpointtracker/
│   │   ├── BreakpointListenerService.kt
│   │   ├── MyToolWindowFactory.kt
│   │   ├── FrontendServer.kt
│   │   └── ...
├── build.gradle.kts    # Gradle build script
├── settings.gradle.kts
└── plugin.xml          # Plugin metadata
```

---
## Troubleshooting
### Plugin Does Not Appear in Tool Windows
- Ensure the plugin is installed and enabled in **Settings > Plugins**.
- Restart the IDE and check **View > Tool Windows > Breakpoint Tracker**.

### Frontend Does Not Load
- Run `./gradlew buildFrontend` to manually build the frontend.
- Ensure `NanoHTTPD` is not blocked by a firewall.
- Check logs for any errors: `idea.log` (Help > Show Log in Explorer/Finder).

---
## Contact
For any issues or feature requests, please open an issue in the repository or reach out via email.

**Repository:** [GitHub Link]
**Author:** AleksaQM

