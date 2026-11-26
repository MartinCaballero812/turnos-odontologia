(() => {
  const BRAND_NAME = "Odonclick";

  for (const element of document.querySelectorAll("[data-brand-name]")) {
    element.textContent = BRAND_NAME;
  }
})();
