document.addEventListener("DOMContentLoaded", () => {
  const targetClass = document.body.dataset.scrollAnimateClass || "fade-in-up";
  const activeClass = `${targetClass}-active`;

  const observer = new IntersectionObserver(
    (entries) => {
      entries.forEach((entry) => {
        if (entry.isIntersecting) {
          entry.target.classList.add(activeClass);
          observer.unobserve(entry.target);
        }
      });
    },
    { threshold: 0.1 }
  );

  document.querySelectorAll(`.${targetClass}`).forEach((el) => {
    observer.observe(el);
  });
});
