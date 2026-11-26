import { defineConfig } from "@playwright/test";

const baseURL = process.env.ODONCLICK_BASE_URL;
if (!baseURL) throw new Error("Ejecutá la suite desde ./gradlew browserTest (ver README).");

export default defineConfig({
  testDir: "./tests",
  workers: 1,
  reporter: "list",
  use: {
    baseURL,
    browserName: "chromium",
    locale: "es-AR",
    viewport: { width: 1440, height: 900 },
  },
});
