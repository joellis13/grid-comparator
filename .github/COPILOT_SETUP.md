# Setting Up GitHub Copilot for Pull Requests

## Overview

This project has two Copilot instruction files:
- **`.github/copilot-instructions.md`** - For code completion/generation in your IDE
- **`.github/copilot-review-instructions.md`** - For PR reviews on GitHub

## How to Configure Copilot PR Reviews

### Option 1: GitHub Repository Settings (Recommended)

1. Go to your GitHub repository
2. Click **Settings** → **Copilot** (in the left sidebar)
3. Scroll to **Code review** section
4. Enable **Copilot code review**
5. In the **Custom instructions** text area, either:
   - Copy/paste the contents of `.github/copilot-review-instructions.md`, OR
   - Reference it: "Follow the instructions in `.github/copilot-review-instructions.md`"

### Option 2: Use the File Directly

GitHub Copilot *may* automatically detect `.github/copilot-review-instructions.md` in your repository. While this isn't officially documented, some teams report success with this approach.

## What Gets Used Where

| Feature | Uses This File | Where It Works |
|---------|---------------|----------------|
| Code completion in IDE | `.github/copilot-instructions.md` | VS Code, JetBrains IDEs, etc. |
| Copilot Chat in IDE | `.github/copilot-instructions.md` | VS Code, JetBrains IDEs, etc. |
| PR reviews on GitHub | Repository Settings → Code review OR `.github/copilot-review-instructions.md` | GitHub.com |
| PR summaries | Repository Settings → Code review | GitHub.com |

## Testing Your Configuration

### For IDE Instructions
1. Open any Java file in your IDE
2. Start typing a new test method
3. Copilot should suggest code following your patterns (3-layer architecture, AssertJ assertions, etc.)

### For PR Review Instructions
1. Create a test PR with intentional issues (e.g., step definition calling page object directly)
2. Wait for Copilot to review the PR (usually takes a few minutes)
3. Check if Copilot flags the architectural violation
4. Verify the feedback matches your review guidelines

## Tips

- **Keep instructions synchronized**: If you update patterns in `copilot-instructions.md`, update `copilot-review-instructions.md` too
- **Be specific**: The more specific your instructions, the better Copilot performs
- **Iterate**: Review Copilot's suggestions and refine your instructions over time
- **Team alignment**: Share these files with your team so everyone knows what Copilot is trained to do

## Updating Instructions

Both files are version-controlled in this repository. To update them:

1. Edit the file in your IDE or GitHub
2. Commit and push changes
3. For PR reviews, also update the GitHub repository settings if you copied the content there

## Access Requirements

- **Code completion/chat**: Anyone with Copilot access can use the IDE instructions
- **PR reviews**: Requires GitHub Copilot Enterprise or GitHub Copilot Business (with PR review feature enabled)

## Further Reading

- [GitHub Copilot Instructions Documentation](https://docs.github.com/en/copilot/customizing-copilot/adding-custom-instructions-for-github-copilot)
- [Copilot PR Reviews](https://github.blog/changelog/2023-11-08-github-copilot-code-review-in-pull-requests/)

