#!/bin/bash
# =============================================================================
# Enable Vertex AI Model Garden LLMs
# =============================================================================
# This script enables the required APIs and model endpoints for Cortex Agent
# to use Vertex AI Model Garden LLMs (Claude, Gemini).
#
# Prerequisites:
#   - gcloud CLI installed and authenticated
#   - A Google Cloud project with billing enabled
#
# Usage:
#   ./enable-vertex-models.sh [PROJECT_ID]
#
# If PROJECT_ID is not provided, it will use the current gcloud project.
# =============================================================================

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

print_header() {
    echo ""
    echo -e "${BLUE}================================================${NC}"
    echo -e "${BLUE}  $1${NC}"
    echo -e "${BLUE}================================================${NC}"
    echo ""
}

print_step() {
    echo -e "${GREEN}[✓]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[!]${NC} $1"
}

print_error() {
    echo -e "${RED}[✗]${NC} $1"
}

# Get project ID
if [ -n "$1" ]; then
    PROJECT_ID="$1"
else
    PROJECT_ID=$(gcloud config get-value project 2>/dev/null)
fi

if [ -z "$PROJECT_ID" ]; then
    print_error "No project ID specified and no default project set."
    echo "Usage: $0 [PROJECT_ID]"
    exit 1
fi

print_header "Enabling Vertex AI Model Garden for: $PROJECT_ID"

# Check if gcloud is authenticated
if ! gcloud auth list --filter=status:ACTIVE --format="value(account)" 2>/dev/null | head -n1 > /dev/null; then
    print_error "gcloud is not authenticated. Run: gcloud auth login"
    exit 1
fi

print_step "Authenticated with gcloud"

# Enable required APIs
print_header "Enabling Required APIs"

APIS=(
    "aiplatform.googleapis.com"
    "iam.googleapis.com"
    "cloudresourcemanager.googleapis.com"
)

for api in "${APIS[@]}"; do
    echo -n "Enabling $api... "
    if gcloud services enable "$api" --project="$PROJECT_ID" 2>/dev/null; then
        echo -e "${GREEN}done${NC}"
    else
        echo -e "${YELLOW}already enabled or failed${NC}"
    fi
done

# List of Vertex AI Model Garden publisher models used by Cortex Agent
print_header "Enabling Model Garden Publishers"

echo "The following models will be accessible via Vertex AI:"
echo ""
echo "  Anthropic Claude Models (via Model Garden):"
echo "    - claude-haiku-4-5@20251001"
echo "    - claude-sonnet-4-5@20250929"
echo "    - claude-opus-4-5@20251101"
echo ""
echo "  Google Gemini Models (native Vertex AI):"
echo "    - gemini-2.5-pro"
echo "    - gemini-2.5-flash"
echo "    - gemini-3-pro-preview"
echo "    - gemini-3-flash-preview"
echo ""

# Check Model Garden access
print_header "Verifying Model Garden Access"

# Test listing available publishers
echo "Checking available publishers in Model Garden..."
if gcloud ai models list --region=us-central1 --project="$PROJECT_ID" --limit=1 2>/dev/null > /dev/null; then
    print_step "Model Garden access verified"
else
    print_warning "Could not verify Model Garden access. This may be normal for new projects."
fi

# Anthropic models require agreement to terms
print_header "Anthropic Model Garden Setup"

echo "To use Claude models via Vertex AI Model Garden, you must:"
echo ""
echo "  1. Visit the Google Cloud Console Model Garden:"
echo -e "     ${BLUE}https://console.cloud.google.com/vertex-ai/publishers/anthropic/model-garden${NC}"
echo ""
echo "  2. Select each Claude model you want to use:"
echo "     - Claude 4.5 Haiku"
echo "     - Claude 4.5 Sonnet" 
echo "     - Claude 4.5 Opus"
echo ""
echo "  3. Click 'Enable' and accept the terms of service"
echo ""
print_warning "Anthropic models require manual enablement in the Console"

# Create service account if needed
print_header "Service Account Setup"

SA_NAME="cortex-agent-vertex"
SA_EMAIL="${SA_NAME}@${PROJECT_ID}.iam.gserviceaccount.com"

echo "Checking for service account: $SA_EMAIL"

if gcloud iam service-accounts describe "$SA_EMAIL" --project="$PROJECT_ID" 2>/dev/null > /dev/null; then
    print_step "Service account already exists"
else
    echo "Creating service account..."
    if gcloud iam service-accounts create "$SA_NAME" \
        --display-name="Cortex Agent Vertex AI" \
        --description="Service account for Cortex Agent to access Vertex AI" \
        --project="$PROJECT_ID" 2>/dev/null; then
        print_step "Service account created"
    else
        print_warning "Could not create service account (may already exist)"
    fi
fi

# Grant necessary roles
print_header "Granting IAM Roles"

ROLES=(
    "roles/aiplatform.user"
    "roles/aiplatform.serviceAgent"
)

for role in "${ROLES[@]}"; do
    echo -n "Granting $role... "
    if gcloud projects add-iam-policy-binding "$PROJECT_ID" \
        --member="serviceAccount:$SA_EMAIL" \
        --role="$role" \
        --condition=None \
        --quiet 2>/dev/null > /dev/null; then
        echo -e "${GREEN}done${NC}"
    else
        echo -e "${YELLOW}may already be granted${NC}"
    fi
done

# Generate key if requested
print_header "Service Account Key"

KEY_PATH="$HOME/.cortex-agent/vertex-key.json"

if [ -f "$KEY_PATH" ]; then
    print_step "Key already exists at: $KEY_PATH"
else
    echo "To generate a service account key for Cortex Agent, run:"
    echo ""
    echo -e "  ${BLUE}gcloud iam service-accounts keys create $KEY_PATH \\${NC}"
    echo -e "  ${BLUE}  --iam-account=$SA_EMAIL \\${NC}"
    echo -e "  ${BLUE}  --project=$PROJECT_ID${NC}"
    echo ""
    
    read -p "Generate key now? (y/N) " -n 1 -r
    echo
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        mkdir -p "$HOME/.cortex-agent"
        if gcloud iam service-accounts keys create "$KEY_PATH" \
            --iam-account="$SA_EMAIL" \
            --project="$PROJECT_ID" 2>/dev/null; then
            chmod 600 "$KEY_PATH"
            print_step "Key saved to: $KEY_PATH"
        else
            print_error "Failed to create key"
        fi
    fi
fi

# Summary
print_header "Setup Complete!"

echo "Summary:"
echo "  Project:         $PROJECT_ID"
echo "  Service Account: $SA_EMAIL"
echo "  Key Location:    $KEY_PATH"
echo ""
echo "Next steps:"
echo "  1. Enable Anthropic models in Model Garden (see URLs above)"
echo "  2. Ensure the key file exists at: $KEY_PATH"
echo "  3. Launch Cortex Agent and select 'Vertex AI' endpoint"
echo ""
echo "Console links:"
echo -e "  Model Garden:  ${BLUE}https://console.cloud.google.com/vertex-ai/model-garden?project=$PROJECT_ID${NC}"
echo -e "  Anthropic:     ${BLUE}https://console.cloud.google.com/vertex-ai/publishers/anthropic?project=$PROJECT_ID${NC}"
echo -e "  IAM:           ${BLUE}https://console.cloud.google.com/iam-admin/iam?project=$PROJECT_ID${NC}"
echo ""
