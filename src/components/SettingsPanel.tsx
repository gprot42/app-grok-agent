import { useState, useEffect } from "react";
import { invoke } from "@tauri-apps/api/core";
import { Button, Input, Select } from "./index";
import { AppSettings, FONT_OPTIONS } from "../types";

interface SettingsPanelProps {
  settings: AppSettings;
  onUpdateSettings: (updates: Partial<AppSettings>) => void;
  onSaveApiKey: (apiKey: string) => void;
  onClose: () => void;
}

export function SettingsPanel({
  settings,
  onUpdateSettings,
  onSaveApiKey,
  onClose,
}: SettingsPanelProps) {
  const [apiKey, setApiKey] = useState(settings.apiKey);
  const [projectId, setProjectId] = useState(settings.projectId);
  const [hasServiceAccount, setHasServiceAccount] = useState(false);
  const [setupProjectId, setSetupProjectId] = useState("");
  const [setupLoading, setSetupLoading] = useState(false);
  const [setupResult, setSetupResult] = useState<{ success: boolean; message: string } | null>(null);

  useEffect(() => {
    setApiKey(settings.apiKey);
    setProjectId(settings.projectId);
  }, [settings]);

  useEffect(() => {
    invoke<boolean>("has_service_account").then(setHasServiceAccount);
    invoke<string | null>("get_service_account_project_id").then((id) => {
      if (id) setSetupProjectId(id);
    });
  }, []);

  const handleSave = () => {
    onSaveApiKey(apiKey);
    onUpdateSettings({ projectId });
    onClose();
  };

  const handleVertexSetup = async (remove: boolean) => {
    if (!setupProjectId.trim()) {
      setSetupResult({ success: false, message: "Please enter a Project ID" });
      return;
    }

    setSetupLoading(true);
    setSetupResult(null);

    try {
      const result = await invoke<string>("run_vertex_setup", {
        projectId: setupProjectId,
        remove,
      });
      setSetupResult({ success: true, message: result });
      const updated = await invoke<boolean>("has_service_account");
      setHasServiceAccount(updated);
      if (!remove && updated) {
        onUpdateSettings({ projectId: setupProjectId });
        setProjectId(setupProjectId);
      }
    } catch (e) {
      setSetupResult({ success: false, message: String(e) });
    } finally {
      setSetupLoading(false);
    }
  };

  return (
    <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50">
      <div className="bg-white dark:bg-tokyo-surface rounded-xl shadow-2xl w-full max-w-2xl mx-4 overflow-hidden max-h-[90vh] flex flex-col">
        <div className="px-6 py-4 border-b border-gray-200 dark:border-tokyo-border flex-shrink-0">
          <h2 className="text-lg font-semibold text-gray-900 dark:text-tokyo-text">
            Settings
          </h2>
        </div>

        <div className="p-6 space-y-6 overflow-y-auto flex-1">
          <div className="space-y-4">
            <h3 className="text-sm font-medium text-gray-700 dark:text-tokyo-muted uppercase tracking-wider">
              Authentication
            </h3>

            <Input
              label="Google Cloud Project ID"
              value={projectId}
              onChange={(e) => setProjectId(e.target.value)}
              placeholder="my-project-id"
            />

            <Input
              label="API Key (for AI Studio)"
              type="password"
              value={apiKey}
              onChange={(e) => setApiKey(e.target.value)}
              placeholder="Enter your API key"
            />

            <p className="text-xs text-gray-500 dark:text-tokyo-muted">
              Your API key is encrypted and stored securely on this device.
            </p>
          </div>

          <div className="space-y-4">
            <h3 className="text-sm font-medium text-gray-700 dark:text-tokyo-muted uppercase tracking-wider">
              Vertex AI Setup
            </h3>

            <div className="bg-blue-50 dark:bg-blue-900/20 border border-blue-200 dark:border-blue-800 rounded-lg p-4">
              <div className="flex items-center gap-2 mb-3">
                <span className={`w-3 h-3 rounded-full ${hasServiceAccount ? "bg-green-500" : "bg-gray-400"}`} />
                <span className="text-sm font-medium text-blue-800 dark:text-blue-200">
                  {hasServiceAccount ? "Service Account Configured" : "Service Account Not Configured"}
                </span>
              </div>

              <Input
                label="GCP Project ID for Vertex AI"
                value={setupProjectId}
                onChange={(e) => setSetupProjectId(e.target.value)}
                placeholder="my-gcp-project"
              />

              <div className="flex gap-3 mt-3">
                <Button
                  variant="primary"
                  onClick={() => handleVertexSetup(false)}
                  disabled={setupLoading}
                >
                  {setupLoading ? "Running..." : "Setup Service Account"}
                </Button>
                {hasServiceAccount && (
                  <Button
                    onClick={() => handleVertexSetup(true)}
                    disabled={setupLoading}
                  >
                    Remove
                  </Button>
                )}
              </div>

              {setupResult && (
                <div className={`mt-3 p-3 rounded text-sm max-h-32 overflow-auto ${
                  setupResult.success
                    ? "bg-green-100 dark:bg-green-900/30 text-green-800 dark:text-green-200"
                    : "bg-red-100 dark:bg-red-900/30 text-red-800 dark:text-red-200"
                }`}>
                  <pre className="whitespace-pre-wrap font-mono text-xs">{setupResult.message}</pre>
                </div>
              )}
            </div>

            <div className="bg-purple-50 dark:bg-purple-900/20 border border-purple-200 dark:border-purple-800 rounded-lg p-4">
              <h4 className="font-medium text-purple-800 dark:text-purple-200 mb-2">
                Enable Anthropic Models
              </h4>
              <p className="text-sm text-purple-700 dark:text-purple-300 mb-2">
                After setup, enable Claude models in the Vertex AI Model Garden:
              </p>
              <ol className="text-sm text-purple-700 dark:text-purple-300 list-decimal list-inside space-y-1">
                <li>Run <code className="bg-purple-100 dark:bg-purple-800/50 px-1 rounded">scripts/02-enable-vertex-models.sh</code></li>
                <li>Or manually enable models in the <a href="https://console.cloud.google.com/vertex-ai/model-garden" target="_blank" rel="noopener" className="underline hover:text-purple-900 dark:hover:text-purple-100">Model Garden</a></li>
              </ol>
            </div>
          </div>

          <div className="space-y-4">
            <h3 className="text-sm font-medium text-gray-700 dark:text-tokyo-muted uppercase tracking-wider">
              Display
            </h3>

            <div className="flex items-center gap-4">
              <label className="text-sm text-gray-600 dark:text-tokyo-muted">
                Font Size
              </label>
              <input
                type="range"
                min="12"
                max="20"
                value={settings.fontSize}
                onChange={(e) =>
                  onUpdateSettings({ fontSize: parseInt(e.target.value) })
                }
                className="flex-1"
              />
              <span className="text-sm text-gray-700 dark:text-tokyo-text w-8">
                {settings.fontSize}
              </span>
            </div>

            <Select
              label="Font Family"
              value={settings.fontFamily || "system"}
              onChange={(e) => onUpdateSettings({ fontFamily: e.target.value })}
              options={FONT_OPTIONS}
            />
          </div>

          <div className="space-y-4">
            <h3 className="text-sm font-medium text-gray-700 dark:text-tokyo-muted uppercase tracking-wider">
              Custom Colors
            </h3>

            <div className="grid grid-cols-3 gap-4">
              <div className="flex flex-col gap-2">
                <label className="text-sm text-gray-600 dark:text-tokyo-muted">
                  Accent Color
                </label>
                <input
                  type="color"
                  value={settings.customColors?.accentColor || "#6366f1"}
                  onChange={(e) => onUpdateSettings({
                    customColors: { ...settings.customColors, accentColor: e.target.value }
                  })}
                  className="w-full h-10 rounded cursor-pointer border border-gray-300 dark:border-gray-600"
                />
              </div>

              <div className="flex flex-col gap-2">
                <label className="text-sm text-gray-600 dark:text-tokyo-muted">
                  Your Messages
                </label>
                <input
                  type="color"
                  value={settings.customColors?.userMessageBg || "#6366f1"}
                  onChange={(e) => onUpdateSettings({
                    customColors: { ...settings.customColors, userMessageBg: e.target.value }
                  })}
                  className="w-full h-10 rounded cursor-pointer border border-gray-300 dark:border-gray-600"
                />
              </div>

              <div className="flex flex-col gap-2">
                <label className="text-sm text-gray-600 dark:text-tokyo-muted">
                  AI Messages
                </label>
                <input
                  type="color"
                  value={settings.customColors?.assistantMessageBg || "#f3f4f6"}
                  onChange={(e) => onUpdateSettings({
                    customColors: { ...settings.customColors, assistantMessageBg: e.target.value }
                  })}
                  className="w-full h-10 rounded cursor-pointer border border-gray-300 dark:border-gray-600"
                />
              </div>
            </div>

            <button
              onClick={() => onUpdateSettings({ customColors: undefined })}
              className="text-sm text-gray-500 hover:text-gray-700 dark:text-gray-400 dark:hover:text-gray-200"
            >
              Reset to defaults
            </button>
          </div>

          <div className="bg-amber-50 dark:bg-amber-900/20 border border-amber-200 dark:border-amber-800 rounded-lg p-4">
            <h4 className="font-medium text-amber-800 dark:text-amber-200 mb-2">
              ⚠️ Disclaimer
            </h4>
            <ul className="text-sm text-amber-700 dark:text-amber-300 space-y-1">
              <li>• Use at your own risk</li>
              <li>• All pricing shown is fictional and for demonstration only</li>
              <li>• Actual costs may vary significantly</li>
              <li>• This is not an official Google product</li>
            </ul>
          </div>
        </div>

        <div className="px-6 py-4 border-t border-gray-200 dark:border-tokyo-border flex justify-end gap-3 flex-shrink-0">
          <Button onClick={onClose}>Cancel</Button>
          <Button variant="primary" onClick={handleSave}>
            Save Settings
          </Button>
        </div>
      </div>
    </div>
  );
}
