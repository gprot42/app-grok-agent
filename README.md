# Cortex Agent v0.0.1

A modern desktop AI assistant for interacting with Large Language Models on
**Google Cloud Vertex AI** and **AI Studio**, built with Tauri, React, and Rust.

## Tech Stack

- **Frontend**: React 18, TypeScript, Vite, Tailwind CSS
- **Backend**: Tauri 2.0 (Rust)
- **Package Manager**: bun (recommended), pnpm, or npm

## Features

### AI Models
- **Claude 4.5**: Haiku, Sonnet, Opus with memory support
- **Gemini 2.5/3**: Pro and Flash variants with grounding
- **Gemini Deep Research**: Multi-step web research agent
- **Nano Banana Pro**: AI image generation and editing

### Capabilities
- **Dual Endpoints**: Vertex AI, AI Studio, or Custom API
- **1M Context Window**: Extended context for Claude and Gemini
- **Memory Tool**: Claude models remember across conversations
- **Deep Thinking**: Extended reasoning for complex problems
- **Grounding**: Web search for up-to-date information
- **File Attachments**: Text, images, and PDFs

### Interface
- **Multiple Sessions**: Tabbed prompt sessions
- **Project Management**: Organize outputs into folders
- **Token Tracking**: Real-time usage with cost estimation
- **Three Themes**: Light, Tokyo Night, Dark
- **Customizable Fonts**: Multiple font options

## Token Approximation

- 1 token ≈ 4 characters (English)
- 1 token ≈ 0.75 words
- 1,000 tokens ≈ 750 words (1-2 pages)
- 1M tokens ≈ 750,000 words

## Quick Start

```bash
# Run development server
./run.sh

# Or manually:
bun install
bun run tauri:dev

# Build DMG
./build-dmg.sh

# Build and create GitHub release
./build-dmg.sh --release
```

## Requirements

- Node.js 18+ (or bun)
- Rust 1.70+
- bun, pnpm, or npm
- Google Cloud credentials (for Vertex AI)
- API key (for AI Studio)

## Secure Storage

API keys are encrypted using AES-256-GCM and stored at `~/.cortex-agent/`. The encryption key is derived from your machine's hardware UUID, making stored keys non-transferable.

## Keyboard Shortcuts

| Shortcut | Action |
|----------|--------|
| Ctrl/Cmd + Enter | Send message |
| Ctrl/Cmd + T | New prompt session |
| Ctrl/Cmd + W | Close current session |

## Standalone App

The built DMG contains a self-contained `.app` bundle with all dependencies embedded. No Homebrew or external runtime required.

## Disclaimer

This is not an official Google product. All pricing shown is for estimation only.

---

**Version 0.0.1** - Built with Tauri, React, and Rust
