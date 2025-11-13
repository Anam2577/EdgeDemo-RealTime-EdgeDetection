const imgEl = document.getElementById('img') as HTMLImageElement
const fpsEl = document.getElementById('fps') as HTMLSpanElement

const sampleBase64 = 'data:image/png;base64,REPLACE_WITH_BASE64_IMAGE'
imgEl.src = sampleBase64

let last = performance.now()
setInterval(() => {
  const now = performance.now()
  const fps = Math.round(1000 / (now - last))
  fpsEl.textContent = fps.toString()
  last = now
}, 1000)
