const SECTIONS = [
  {
    id: 'mockups',
    label: 'UI Mockups',
    groups: [
      {
        label: 'Main Frames',
        items: [
          { slug: '01-login',     num: '01', title: 'Login',     file: 'screens/app/login.html' },
          { slug: '02-dashboard', num: '02', title: 'Dashboard', file: 'screens/app/dashboard.html' },
        ],
      },
      {
        label: 'Master Data',
        items: [
          { slug: '03-items',      num: '03', title: 'Items',      file: 'screens/app/master-data/items.html' },
          { slug: '04-categories', num: '04', title: 'Categories', file: 'screens/app/master-data/categories.html' },
          { slug: '05-outlets',    num: '05', title: 'Outlets',    file: 'screens/app/master-data/outlets.html' },
          { slug: '06-vendors',    num: '06', title: 'Vendors',    file: 'screens/app/master-data/vendors.html' },
          { slug: '07-users',      num: '07', title: 'Users',      file: 'screens/app/master-data/users.html' },
        ],
      },
      {
        label: 'Transactions',
        items: [
          { slug: '08-receiving', num: '08', title: 'Receiving', file: 'screens/app/transactions/receiving.html' },
          { slug: '09-sales',     num: '09', title: 'Sales',     file: 'screens/app/transactions/sales.html' },
          { slug: '10-movement',  num: '10', title: 'Movement',  file: 'screens/app/transactions/movement.html' },
          { slug: '11-recipes',   num: '11', title: 'Recipes',   file: 'screens/app/transactions/recipes.html' },
        ],
      },
      {
        label: 'Stock Operations',
        items: [
          { slug: '12-stock-on-hand', num: '12', title: 'Stock On Hand', file: 'screens/app/stock-operations/stock-on-hand.html' },
          { slug: '13-audit',         num: '13', title: 'Audit',         file: 'screens/app/stock-operations/audit.html' },
          { slug: '14-end-balance',   num: '14', title: 'End Balance',   file: 'screens/app/stock-operations/end-balance.html' },
        ],
      },
      {
        label: 'Reports & Charts',
        items: [
          { slug: '15-reports', num: '15', title: 'Reports', file: 'screens/app/reports/reports.html' },
          { slug: '16-charts',  num: '16', title: 'Charts',  file: 'screens/app/reports/charts.html' },
        ],
      },
      {
        label: 'Dialogs',
        items: [
          { slug: '17-about', num: '17', title: 'About', file: 'screens/app/dialogs/about.html' },
        ],
      },
    ],
  },
  {
    id: 'overview',
    label: 'Project Overview',
    items: [
      { slug: 'project-home',      title: 'Project Home',      file: 'screens/overview/title.html' },
      { slug: 'what-it-does',      title: 'What It Does',      file: 'screens/overview/what-it-does.html' },
      { slug: 'feature-guide',     title: 'Feature Guide',     file: 'screens/overview/feature-guide.html' },
      { slug: 'role-based-access', title: 'Role-Based Access', file: 'screens/overview/role-based-access.html' },
      { slug: 'data-flow',         title: 'Data Flow',         file: 'screens/overview/data-flow.html' },
    ],
  },
  {
    id: 'specs',
    label: 'Design Specs',
    items: [
      { slug: 'application-flow',       title: 'Application Flow',       file: 'screens/specs/application-flow.html' },
      { slug: 'architecture',           title: 'Architecture',           file: 'screens/specs/architecture.html' },
      { slug: 'database-schema',        title: 'Database Schema',        file: 'screens/specs/schema.html' },
      { slug: 'build-phases',           title: 'Build Phases',           file: 'screens/specs/build-phases.html' },
      { slug: 'technical-requirements', title: 'Technical Requirements', file: 'screens/specs/technical-requirements.html' },
      { slug: 'future-work',            title: 'Future Work',            file: 'screens/specs/future-work.html' },
    ],
  },
  {
    id: 'dev',
    label: 'Developer Reference',
    items: [
      { slug: 'orchestrator-cheat-sheet', title: 'Orchestrator Cheat Sheet', file: 'screens/reference/developer-handoff.html' },
      { slug: 'quick-start-demo',         title: 'Quick Start Demo',         file: 'screens/reference/quick-start.html' },
      { slug: 'project-credits',          title: 'Project Credits',          file: 'screens/reference/credits.html' },
    ],
  },
];

function sectionItems(section) {
  return section.items
    ? section.items
    : section.groups.flatMap(g => g.items);
}

const FLAT = SECTIONS.flatMap(s => sectionItems(s).map(i => ({ ...i, section: s.id })));
const BY_SLUG = Object.fromEntries(FLAT.map(i => [i.slug, i]));

function makeNavItem(item, grouped = false) {
  const a = document.createElement('a');
  a.className = grouped ? 'nav-item nav-item-grouped' : 'nav-item';
  a.href = `#/${item.slug}`;
  a.dataset.slug = item.slug;
  if (item.num) {
    const n = document.createElement('span');
    n.className = 'nav-num';
    n.textContent = item.num;
    a.appendChild(n);
  }
  a.appendChild(document.createTextNode(item.title));
  return a;
}

function buildSidebar() {
  const nav = document.getElementById('nav');
  for (const section of SECTIONS) {
    const sec = document.createElement('div');
    sec.className = 'sidebar-section';
    const label = document.createElement('div');
    label.className = 'sidebar-section-label';
    label.textContent = section.label;
    sec.appendChild(label);

    if (section.items) {
      for (const item of section.items) sec.appendChild(makeNavItem(item));
    } else {
      for (const group of section.groups) {
        if (group.label) {
          const gl = document.createElement('div');
          gl.className = 'sidebar-subgroup-label';
          gl.textContent = group.label;
          sec.appendChild(gl);
        }
        for (const item of group.items) sec.appendChild(makeNavItem(item, !!group.label));
      }
    }
    nav.appendChild(sec);
  }
}

function parseHash() {
  const h = location.hash || '';
  const m = h.match(/^#\/(.+)$/);
  return m ? m[1] : null;
}

function setActive(slug) {
  document.querySelectorAll('.nav-item').forEach(el => {
    el.classList.toggle('active', el.dataset.slug === slug);
  });
}

function render() {
  const slug = parseHash();
  const main = document.getElementById('main');
  closeSidebar();
  if (!slug) {
    setActive(null);
    main.innerHTML = renderLanding();
    bindLanding();
    return;
  }
  const item = BY_SLUG[slug];
  if (!item) {
    setActive(null);
    main.innerHTML = `<div class="landing"><div class="landing-card"><h1>Not found</h1><p class="landing-sub">Unknown page: ${slug}</p></div></div>`;
    return;
  }
  setActive(slug);
  main.innerHTML = `<div class="frame-wrap"><iframe src="${item.file}" title="${item.title}"></iframe></div>`;
  const active = document.querySelector('.nav-item.active');
  if (active) active.scrollIntoView({ block: 'nearest' });
}

function renderLanding() {
  const firstOf = id => sectionItems(SECTIONS.find(s => s.id === id))[0].slug;
  return `
    <div class="landing">
      <div class="landing-card">
        <h1>Inventory Management System Plan</h1>
        <div class="landing-sub">Final Project · Team of 8 · Java Swing application — design, specs, and build reference</div>
        <div class="tiles">
          <a class="tile" href="#/${firstOf('mockups')}">
            <div class="tile-label">Section 1</div>
            <div class="tile-title">UI Mockups</div>
            <div class="tile-meta">17 screens · CRUD, transactions, reports</div>
          </a>
          <a class="tile" href="#/${firstOf('overview')}">
            <div class="tile-label">Section 2</div>
            <div class="tile-title">Project Overview</div>
            <div class="tile-meta">What it does, features, roles, data flow</div>
          </a>
          <a class="tile" href="#/${firstOf('specs')}">
            <div class="tile-label">Section 3</div>
            <div class="tile-title">Design Specs</div>
            <div class="tile-meta">Architecture, schema, build phases</div>
          </a>
          <a class="tile" href="#/${firstOf('dev')}">
            <div class="tile-label">Section 4</div>
            <div class="tile-title">Developer Reference</div>
            <div class="tile-meta">Cheat sheet, quick-start demo, credits</div>
          </a>
        </div>
      </div>
    </div>
  `;
}

function bindLanding() { /* tiles use href, no extra binding needed */ }

function navigateRelative(delta) {
  const slug = parseHash();
  if (!slug) return;
  const current = BY_SLUG[slug];
  if (!current) return;
  const items = sectionItems(SECTIONS.find(s => s.id === current.section));
  const idx = items.findIndex(i => i.slug === slug);
  const next = items[idx + delta];
  if (next) location.hash = `#/${next.slug}`;
}

async function downloadCurrentSlide() {
  const btn = document.getElementById('download-png');
  const slug = parseHash();
  if (!slug) { alert('Open a slide first, then click Download PNG.'); return; }
  const item = BY_SLUG[slug];
  if (!item) return;
  const iframe = document.querySelector('#main iframe');
  if (!iframe) return;
  let target;
  try {
    const doc = iframe.contentDocument;
    if (!doc || !doc.body) throw new Error('iframe inaccessible');
    target = doc.body;
  } catch (e) {
    alert('Cannot read the slide. Try serving via http (e.g. python -m http.server) instead of opening file:// in Chrome.');
    return;
  }
  if (typeof htmlToImage === 'undefined') { alert('html-to-image not loaded.'); return; }
  btn.disabled = true;
  const original = btn.textContent;
  btn.textContent = 'Capturing…';
  try {
    const dataUrl = await htmlToImage.toPng(target, {
      backgroundColor: '#b8b8b8',
      pixelRatio: 2,
      cacheBust: true,
      width: target.scrollWidth,
      height: target.scrollHeight,
    });
    const a = document.createElement('a');
    a.href = dataUrl;
    a.download = `${slug}.png`;
    document.body.appendChild(a);
    a.click();
    a.remove();
  } catch (e) {
    alert('Capture failed: ' + e.message);
  } finally {
    btn.disabled = false;
    btn.textContent = original;
  }
}

function toggleSidebar() {
  document.querySelector('.sidebar').classList.toggle('open');
  document.querySelector('.scrim').classList.toggle('open');
}
function closeSidebar() {
  document.querySelector('.sidebar').classList.remove('open');
  document.querySelector('.scrim').classList.remove('open');
}

window.addEventListener('DOMContentLoaded', () => {
  buildSidebar();
  render();
  document.getElementById('home-btn').addEventListener('click', () => {
    if (location.hash) {
      history.pushState('', document.title, location.pathname + location.search);
    }
    render();
  });
  document.getElementById('hamburger').addEventListener('click', toggleSidebar);
  document.querySelector('.scrim').addEventListener('click', closeSidebar);
  document.getElementById('download-png').addEventListener('click', downloadCurrentSlide);
  document.addEventListener('keydown', e => {
    if (e.target.tagName === 'INPUT' || e.target.tagName === 'TEXTAREA') return;
    if (e.key === 'ArrowRight') navigateRelative(1);
    else if (e.key === 'ArrowLeft') navigateRelative(-1);
  });
});

window.addEventListener('hashchange', render);
