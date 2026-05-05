<template>
  <div class="home">
    <!-- ====== HERO ====== -->
    <section class="hero">
      <div class="hero-bg">
        <div class="hero-grain"></div>
        <div class="hero-shape hero-shape-1"></div>
        <div class="hero-shape hero-shape-2"></div>
        <div class="hero-shape hero-shape-3"></div>
      </div>

      <div class="hero-content">
        <div class="hero-badge">
          <svg width="14" height="14" viewBox="0 0 14 14" fill="none">
            <path d="M7 1l1.8 3.6L13 5.3l-3 2.9.7 4.1L7 10.5 3.3 12.3l.7-4.1-3-2.9L5.2 4.6z" fill="var(--c-gold)" />
          </svg>
          <span>文创定制 · 独一无二</span>
        </div>

        <h1 class="hero-title">
          <span class="hero-title-line">把<span class="text-accent">回忆</span></span>
          <span class="hero-title-line">做成<span class="text-accent">实物</span></span>
        </h1>

        <p class="hero-desc">
          演唱会票根、纪念立牌、个性钥匙扣……<br>
          上传你的故事，我们帮你变成可以触摸的纪念品。
        </p>

        <div class="hero-actions">
          <button class="btn-hero" @click="$router.push('/templates')">
            <span>开始创作</span>
            <svg width="18" height="18" viewBox="0 0 18 18" fill="none">
              <path d="M4 9h10M10 5l4 4-4 4" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" />
            </svg>
          </button>
          <button class="btn-hero-outline" @click="$router.push('/templates')">
            浏览模板
          </button>
        </div>

        <div class="hero-stats">
          <div class="stat-item">
            <span class="stat-num">200+</span>
            <span class="stat-label">设计模板</span>
          </div>
          <div class="stat-divider"></div>
          <div class="stat-item">
            <span class="stat-num">5000+</span>
            <span class="stat-label">定制作品</span>
          </div>
          <div class="stat-divider"></div>
          <div class="stat-item">
            <span class="stat-num">98%</span>
            <span class="stat-label">好评率</span>
          </div>
        </div>
      </div>

      <div class="hero-visual">
        <div class="ticket-showcase">
          <div class="ticket ticket-1">
            <div class="ticket-top">
              <span class="ticket-tag">CONCERT</span>
              <span class="ticket-date">2026.05.20</span>
            </div>
            <div class="ticket-body">
              <div class="ticket-title">周杰伦</div>
              <div class="ticket-sub">嘉年华世界巡回演唱会</div>
            </div>
            <div class="ticket-tear"></div>
            <div class="ticket-bottom">
              <span class="ticket-seat">A区 12排 08座</span>
              <span class="ticket-no">#002586</span>
            </div>
          </div>

          <div class="ticket ticket-2">
            <div class="ticket-top">
              <span class="ticket-tag">FESTIVAL</span>
              <span class="ticket-date">2026.06.01</span>
            </div>
            <div class="ticket-body">
              <div class="ticket-title">草莓音乐节</div>
              <div class="ticket-sub">北京 · 世园公园</div>
            </div>
            <div class="ticket-tear"></div>
            <div class="ticket-bottom">
              <span class="ticket-seat">两日通票</span>
              <span class="ticket-no">#001337</span>
            </div>
          </div>

          <div class="ticket ticket-3">
            <div class="ticket-top">
              <span class="ticket-tag">MATCH</span>
              <span class="ticket-date">2026.04.15</span>
            </div>
            <div class="ticket-body">
              <div class="ticket-title">欧冠决赛</div>
              <div class="ticket-sub">皇家马德里 vs 利物浦</div>
            </div>
            <div class="ticket-tear"></div>
            <div class="ticket-bottom">
              <span class="ticket-seat">B区 22排 15座</span>
              <span class="ticket-no">#008899</span>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- ====== 品类 ====== -->
    <section class="categories">
      <div class="page-container">
        <div class="section-header">
          <span class="section-tag">选择品类</span>
          <h2 class="section-title">你想做什么？</h2>
          <p class="section-desc">四种品类，无限可能</p>
        </div>

        <div class="category-grid">
          <div
            v-for="(cat, i) in categories"
            :key="cat.value"
            class="category-card"
            :style="{ animationDelay: `${i * 0.1}s` }"
            @click="$router.push(`/templates?category=${cat.value}`)"
          >
            <div class="category-icon-wrap">
              <div class="category-icon" v-html="cat.svg"></div>
            </div>
            <h3>{{ cat.label }}</h3>
            <p>{{ cat.desc }}</p>
            <div class="category-arrow">
              <svg width="20" height="20" viewBox="0 0 20 20" fill="none">
                <path d="M5 10h10M11 6l4 4-4 4" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
              </svg>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- ====== 热门模板 ====== -->
    <section class="hot-templates">
      <div class="page-container">
        <div class="section-header">
          <span class="section-tag">热门推荐</span>
          <h2 class="section-title">大家都在做</h2>
          <p class="section-desc">精选高人气定制模板</p>
        </div>

        <div class="template-grid">
          <div
            v-for="(item, i) in hotTemplates"
            :key="item.id"
            class="template-card"
            :style="{ animationDelay: `${i * 0.08}s` }"
            @click="$router.push(`/template/${item.id}`)"
          >
            <div class="template-preview" :style="{ background: item.bg }">
              <div class="template-preview-inner" v-html="item.previewHtml"></div>
              <div class="template-tag">{{ item.tag }}</div>
            </div>
            <div class="template-info">
              <div>
                <h4>{{ item.name }}</h4>
                <span class="template-cat">{{ item.catLabel }}</span>
              </div>
              <span class="price">¥{{ item.price }}</span>
            </div>
          </div>
        </div>

        <div class="templates-more">
          <button class="btn-outline" @click="$router.push('/templates')">
            查看全部模板
            <svg width="16" height="16" viewBox="0 0 16 16" fill="none">
              <path d="M4 8h8M9 5l3 3-3 3" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
            </svg>
          </button>
        </div>
      </div>
    </section>

    <!-- ====== 流程 ====== -->
    <section class="process">
      <div class="page-container">
        <div class="section-header">
          <span class="section-tag">定制流程</span>
          <h2 class="section-title">三步搞定</h2>
        </div>

        <div class="process-grid">
          <div class="process-step">
            <div class="step-num">01</div>
            <div class="step-icon">
              <svg width="32" height="32" viewBox="0 0 32 32" fill="none">
                <rect x="4" y="6" width="24" height="20" rx="3" stroke="var(--c-orange)" stroke-width="2" />
                <path d="M4 12h24" stroke="var(--c-orange)" stroke-width="2" />
                <circle cx="9" cy="9" r="1.5" fill="var(--c-orange)" />
                <circle cx="14" cy="9" r="1.5" fill="var(--c-orange-light)" />
                <rect x="8" y="16" width="8" height="6" rx="1" fill="var(--c-orange-light)" opacity="0.5" />
                <rect x="18" y="16" width="6" height="6" rx="1" fill="var(--c-sage-light)" opacity="0.5" />
              </svg>
            </div>
            <h3>选模板</h3>
            <p>浏览 200+ 精选模板，找到你喜欢的风格</p>
          </div>

          <div class="process-connector">
            <svg width="40" height="2" viewBox="0 0 40 2">
              <line x1="0" y1="1" x2="40" y2="1" stroke="var(--c-border)" stroke-width="2" stroke-dasharray="4 4" />
            </svg>
          </div>

          <div class="process-step">
            <div class="step-num">02</div>
            <div class="step-icon">
              <svg width="32" height="32" viewBox="0 0 32 32" fill="none">
                <path d="M6 26l6-6 4 4 6-8 4 4" stroke="var(--c-orange)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" />
                <rect x="4" y="4" width="24" height="24" rx="3" stroke="var(--c-orange)" stroke-width="2" />
                <circle cx="22" cy="10" r="3" fill="var(--c-gold)" />
              </svg>
            </div>
            <h3>做定制</h3>
            <p>上传照片、编辑文字，实时预览定制效果</p>
          </div>

          <div class="process-connector">
            <svg width="40" height="2" viewBox="0 0 40 2">
              <line x1="0" y1="1" x2="40" y2="1" stroke="var(--c-border)" stroke-width="2" stroke-dasharray="4 4" />
            </svg>
          </div>

          <div class="process-step">
            <div class="step-num">03</div>
            <div class="step-icon">
              <svg width="32" height="32" viewBox="0 0 32 32" fill="none">
                <rect x="4" y="8" width="24" height="16" rx="3" stroke="var(--c-orange)" stroke-width="2" />
                <path d="M4 14h24" stroke="var(--c-orange)" stroke-width="2" />
                <path d="M12 20h8" stroke="var(--c-orange)" stroke-width="2" stroke-linecap="round" />
                <circle cx="16" cy="5" r="3" fill="var(--c-gold)" />
              </svg>
            </div>
            <h3>收好物</h3>
            <p>下单后精心制作，快递送到你手中</p>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getTemplateList } from '../api/modules/template'

const categories = [
  {
    value: 'ticket',
    label: '票根定制',
    desc: '演唱会、比赛门票，留住现场的感动',
    svg: '<svg width="40" height="40" viewBox="0 0 40 40" fill="none"><rect x="4" y="8" width="32" height="24" rx="3" stroke="currentColor" stroke-width="2"/><path d="M4 16h32" stroke="currentColor" stroke-width="2" stroke-dasharray="3 3"/><path d="M12 22h16M12 27h10" stroke="currentColor" stroke-width="2" stroke-linecap="round" opacity="0.5"/><circle cx="30" cy="27" r="3" fill="currentColor" opacity="0.3"/></svg>'
  },
  {
    value: 'acrylic',
    label: '亚克力立牌',
    desc: '桌面摆件、应援周边，专属你的小世界',
    svg: '<svg width="40" height="40" viewBox="0 0 40 40" fill="none"><rect x="10" y="4" width="20" height="28" rx="2" stroke="currentColor" stroke-width="2"/><path d="M14 32h12v4H14z" fill="currentColor" opacity="0.15" stroke="currentColor" stroke-width="2"/><circle cx="20" cy="16" r="5" stroke="currentColor" stroke-width="2" opacity="0.5"/><path d="M15 24h10" stroke="currentColor" stroke-width="2" stroke-linecap="round" opacity="0.5"/></svg>'
  },
  {
    value: 'keychain',
    label: '钥匙扣',
    desc: '随身携带的小确幸，每天都能看到',
    svg: '<svg width="40" height="40" viewBox="0 0 40 40" fill="none"><circle cx="20" cy="14" r="8" stroke="currentColor" stroke-width="2"/><circle cx="20" cy="14" r="3" stroke="currentColor" stroke-width="2" opacity="0.5"/><path d="M20 22v10" stroke="currentColor" stroke-width="2"/><circle cx="20" cy="35" r="2" fill="currentColor" opacity="0.3"/><rect x="16" y="26" width="8" height="6" rx="1" stroke="currentColor" stroke-width="2" opacity="0.5"/></svg>'
  },
  {
    value: 'badge',
    label: '纪念徽章',
    desc: '胸针、徽章，别在包上的个性宣言',
    svg: '<svg width="40" height="40" viewBox="0 0 40 40" fill="none"><path d="M20 4l4 8 9 1-6.5 6.3L28 29l-8-4-8 4 1.5-9.7L7 13l9-1z" stroke="currentColor" stroke-width="2" stroke-linejoin="round"/><circle cx="20" cy="17" r="4" stroke="currentColor" stroke-width="2" opacity="0.5"/></svg>'
  }
]

const categoryLabelMap = {
  ticket: '票根',
  acrylic: '亚克力',
  keychain: '钥匙扣',
  badge: '徽章'
}

const categoryBgMap = {
  ticket: 'linear-gradient(135deg, #1a1a2e 0%, #16213e 50%, #0f3460 100%)',
  acrylic: 'linear-gradient(135deg, #ffecd2 0%, #fcb69f 100%)',
  keychain: 'linear-gradient(135deg, #a18cd1 0%, #fbc2eb 100%)',
  badge: 'linear-gradient(135deg, #2d3436 0%, #636e72 100%)'
}

const categoryColorMap = {
  ticket: '#e94560',
  acrylic: '#c0392b',
  keychain: '#6c3483',
  badge: '#f5c542'
}

const hotTemplates = ref([])

async function loadHotTemplates() {
  try {
    const res = await getTemplateList({ current: 1, size: 4 })
    hotTemplates.value = res.data.records.map(item => ({
      ...item,
      catLabel: categoryLabelMap[item.category] || item.category,
      tag: item.sortOrder <= 3 ? '热门' : '新品',
      bg: categoryBgMap[item.category] || categoryBgMap.ticket,
      previewHtml: `<div style="color:${categoryColorMap[item.category] || '#333'};font-family:serif;font-size:16px;font-weight:700;text-align:center;line-height:1.4">${item.name.split(/[（(]/)[0]}<br><span style="font-size:10px;font-weight:400;color:rgba(255,255,255,0.7);letter-spacing:2px">${categoryLabelMap[item.category]?.toUpperCase() || ''}</span></div>`
    }))
  } catch (e) {
    // fallback
  }
}

onMounted(() => {
  loadHotTemplates()
})
</script>

<style scoped>
/* ========== HERO ========== */
.hero {
  position: relative;
  min-height: 600px;
  display: flex;
  align-items: center;
  overflow: hidden;
  padding: 60px 0;
}

.hero-bg {
  position: absolute;
  inset: 0;
  background: linear-gradient(160deg, var(--c-cream) 0%, #FFE8D6 40%, #FFD4B8 100%);
  z-index: 0;
}

.hero-grain {
  position: absolute;
  inset: 0;
  background-image: url("data:image/svg+xml,%3Csvg viewBox='0 0 256 256' xmlns='http://www.w3.org/2000/svg'%3E%3Cfilter id='n'%3E%3CfeTurbulence type='fractalNoise' baseFrequency='0.85' numOctaves='4' stitchTiles='stitch'/%3E%3C/filter%3E%3Crect width='100%25' height='100%25' filter='url(%23n)' opacity='0.06'/%3E%3C/svg%3E");
  opacity: 0.8;
}

.hero-shape {
  position: absolute;
  border-radius: 50%;
  filter: blur(80px);
}

.hero-shape-1 {
  width: 400px;
  height: 400px;
  background: rgba(232, 114, 42, 0.15);
  top: -100px;
  right: -50px;
}

.hero-shape-2 {
  width: 300px;
  height: 300px;
  background: rgba(245, 197, 66, 0.12);
  bottom: -80px;
  left: 10%;
}

.hero-shape-3 {
  width: 200px;
  height: 200px;
  background: rgba(139, 158, 126, 0.1);
  top: 30%;
  left: 40%;
}

.hero-content {
  position: relative;
  z-index: 1;
  flex: 1;
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 24px;
  display: flex;
  align-items: center;
  gap: 60px;
}

.hero-badge {
  position: absolute;
  top: -40px;
  left: 24px;
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 6px 14px;
  background: rgba(255, 255, 255, 0.7);
  backdrop-filter: blur(8px);
  border-radius: 20px;
  font-size: 12px;
  font-weight: 500;
  color: var(--c-orange);
  border: 1px solid rgba(232, 114, 42, 0.15);
  animation: fadeUp 0.6s var(--ease-smooth) both;
}

.hero-title {
  font-family: var(--f-display);
  font-size: 52px;
  color: var(--c-brown);
  line-height: 1.2;
  letter-spacing: 2px;
  animation: fadeUp 0.6s 0.1s var(--ease-smooth) both;
}

.hero-title-line {
  display: block;
}

.text-accent {
  color: var(--c-orange);
  position: relative;
}

.text-accent::after {
  content: '';
  position: absolute;
  bottom: 2px;
  left: 0;
  width: 100%;
  height: 8px;
  background: var(--c-gold);
  opacity: 0.3;
  border-radius: 4px;
  z-index: -1;
}

.hero-desc {
  font-size: 16px;
  color: var(--c-text-secondary);
  line-height: 1.8;
  margin-top: 16px;
  animation: fadeUp 0.6s 0.2s var(--ease-smooth) both;
}

.hero-actions {
  display: flex;
  gap: 16px;
  margin-top: 32px;
  animation: fadeUp 0.6s 0.3s var(--ease-smooth) both;
}

.btn-hero {
  display: inline-flex;
  align-items: center;
  gap: 10px;
  padding: 14px 32px;
  font-size: 16px;
  font-weight: 600;
  color: #fff;
  background: var(--c-orange);
  border: none;
  border-radius: var(--r-md);
  cursor: pointer;
  transition: all 0.3s var(--ease-bounce);
  font-family: var(--f-body);
  box-shadow: 0 4px 16px rgba(232, 114, 42, 0.3);
}

.btn-hero:hover {
  background: var(--c-orange-dark);
  transform: translateY(-2px);
  box-shadow: 0 8px 28px rgba(232, 114, 42, 0.4);
}

.btn-hero svg {
  transition: transform 0.3s;
}

.btn-hero:hover svg {
  transform: translateX(4px);
}

.btn-hero-outline {
  padding: 14px 28px;
  font-size: 16px;
  font-weight: 500;
  color: var(--c-brown);
  background: rgba(255, 255, 255, 0.6);
  border: 2px solid var(--c-border);
  border-radius: var(--r-md);
  cursor: pointer;
  transition: all 0.3s;
  font-family: var(--f-body);
  backdrop-filter: blur(4px);
}

.btn-hero-outline:hover {
  background: #fff;
  border-color: var(--c-orange);
  color: var(--c-orange);
}

.hero-stats {
  display: flex;
  align-items: center;
  gap: 24px;
  margin-top: 40px;
  padding-top: 28px;
  border-top: 1px solid rgba(45, 24, 16, 0.08);
  animation: fadeUp 0.6s 0.4s var(--ease-smooth) both;
}

.stat-num {
  display: block;
  font-family: var(--f-heading);
  font-size: 24px;
  font-weight: 700;
  color: var(--c-brown);
}

.stat-label {
  font-size: 13px;
  color: var(--c-text-muted);
}

.stat-divider {
  width: 1px;
  height: 32px;
  background: var(--c-border);
}

/* Ticket showcase */
.hero-visual {
  position: relative;
  z-index: 1;
  flex-shrink: 0;
  width: 380px;
  height: 440px;
  animation: fadeUp 0.8s 0.3s var(--ease-smooth) both;
}

.ticket-showcase {
  position: relative;
  width: 100%;
  height: 100%;
}

.ticket {
  position: absolute;
  width: 280px;
  background: #fff;
  border-radius: var(--r-md);
  overflow: hidden;
  box-shadow: 0 8px 32px rgba(45, 24, 16, 0.12);
  transition: all 0.4s var(--ease-bounce);
  cursor: default;
}

.ticket:hover {
  z-index: 10;
  transform: scale(1.05) !important;
  box-shadow: 0 16px 48px rgba(45, 24, 16, 0.18);
}

.ticket-1 {
  top: 0;
  left: 40px;
  transform: rotate(-3deg);
  z-index: 3;
  animation: float 6s ease-in-out infinite;
}

.ticket-2 {
  top: 80px;
  left: 80px;
  transform: rotate(2deg);
  z-index: 2;
  animation: float 6s 2s ease-in-out infinite;
}

.ticket-3 {
  top: 160px;
  left: 20px;
  transform: rotate(-1deg);
  z-index: 1;
  animation: float 6s 4s ease-in-out infinite;
}

.ticket-top {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px 8px;
}

.ticket-tag {
  font-size: 10px;
  font-weight: 700;
  letter-spacing: 2px;
  color: #fff;
  background: var(--c-orange);
  padding: 3px 10px;
  border-radius: 4px;
}

.ticket-date {
  font-size: 11px;
  color: var(--c-text-muted);
  font-family: var(--f-body);
}

.ticket-body {
  padding: 8px 16px 16px;
}

.ticket-title {
  font-family: var(--f-display);
  font-size: 22px;
  color: var(--c-brown);
  margin-bottom: 4px;
}

.ticket-sub {
  font-size: 12px;
  color: var(--c-text-secondary);
}

.ticket-tear {
  height: 1px;
  margin: 0 12px;
  background: repeating-linear-gradient(
    90deg,
    var(--c-border) 0,
    var(--c-border) 6px,
    transparent 6px,
    transparent 12px
  );
  position: relative;
}

.ticket-tear::before,
.ticket-tear::after {
  content: '';
  position: absolute;
  width: 16px;
  height: 16px;
  background: var(--c-cream);
  border-radius: 50%;
  top: -8px;
}

.ticket-tear::before { left: -20px; }
.ticket-tear::after { right: -20px; }

.ticket-bottom {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 16px;
}

.ticket-seat {
  font-size: 11px;
  color: var(--c-text-secondary);
}

.ticket-no {
  font-size: 11px;
  font-weight: 600;
  color: var(--c-text-muted);
  font-family: monospace;
}

/* ========== CATEGORIES ========== */
.categories {
  padding: 80px 0 60px;
}

.section-header {
  text-align: center;
  margin-bottom: 48px;
}

.section-tag {
  display: inline-block;
  font-size: 12px;
  font-weight: 600;
  letter-spacing: 3px;
  text-transform: uppercase;
  color: var(--c-orange);
  background: rgba(232, 114, 42, 0.08);
  padding: 6px 16px;
  border-radius: 20px;
  margin-bottom: 16px;
}

.section-title {
  font-family: var(--f-display);
  font-size: 36px;
  color: var(--c-brown);
  margin-bottom: 8px;
}

.section-desc {
  font-size: 15px;
  color: var(--c-text-muted);
}

.category-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}

.category-card {
  background: var(--c-card);
  border-radius: var(--r-lg);
  padding: 36px 24px 28px;
  text-align: center;
  cursor: pointer;
  transition: all 0.35s var(--ease-bounce);
  border: 1px solid rgba(45, 24, 16, 0.04);
  animation: fadeUp 0.5s var(--ease-smooth) both;
  position: relative;
  overflow: hidden;
}

.category-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 3px;
  background: linear-gradient(90deg, var(--c-orange), var(--c-gold));
  transform: scaleX(0);
  transition: transform 0.3s var(--ease-smooth);
}

.category-card:hover {
  transform: translateY(-6px);
  box-shadow: var(--shadow-lg);
  border-color: transparent;
}

.category-card:hover::before {
  transform: scaleX(1);
}

.category-icon-wrap {
  width: 72px;
  height: 72px;
  margin: 0 auto 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(232, 114, 42, 0.06);
  border-radius: var(--r-lg);
  color: var(--c-orange);
  transition: all 0.3s var(--ease-bounce);
}

.category-card:hover .category-icon-wrap {
  background: var(--c-orange);
  color: #fff;
  transform: scale(1.08) rotate(-3deg);
}

.category-card h3 {
  font-family: var(--f-heading);
  font-size: 17px;
  color: var(--c-brown);
  margin-bottom: 8px;
}

.category-card p {
  font-size: 13px;
  color: var(--c-text-muted);
  line-height: 1.5;
}

.category-arrow {
  margin-top: 16px;
  color: var(--c-text-muted);
  transition: all 0.3s;
}

.category-card:hover .category-arrow {
  color: var(--c-orange);
  transform: translateX(4px);
}

/* ========== HOT TEMPLATES ========== */
.hot-templates {
  padding: 40px 0 80px;
}

.template-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}

.template-card {
  background: var(--c-card);
  border-radius: var(--r-lg);
  overflow: hidden;
  cursor: pointer;
  transition: all 0.35s var(--ease-bounce);
  border: 1px solid rgba(45, 24, 16, 0.04);
  animation: fadeUp 0.5s var(--ease-smooth) both;
}

.template-card:hover {
  transform: translateY(-6px) rotate(-0.5deg);
  box-shadow: var(--shadow-lg);
  border-color: transparent;
}

.template-preview {
  height: 220px;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  overflow: hidden;
}

.template-preview::after {
  content: '';
  position: absolute;
  inset: 0;
  background: url("data:image/svg+xml,%3Csvg viewBox='0 0 256 256' xmlns='http://www.w3.org/2000/svg'%3E%3Cfilter id='n'%3E%3CfeTurbulence type='fractalNoise' baseFrequency='0.9' numOctaves='4' stitchTiles='stitch'/%3E%3C/filter%3E%3Crect width='100%25' height='100%25' filter='url(%23n)' opacity='0.08'/%3E%3C/svg%3E");
  pointer-events: none;
}

.template-preview-inner {
  position: relative;
  z-index: 1;
  padding: 20px;
  background: rgba(255, 255, 255, 0.15);
  backdrop-filter: blur(4px);
  border-radius: var(--r-md);
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.template-tag {
  position: absolute;
  top: 12px;
  right: 12px;
  font-size: 11px;
  font-weight: 600;
  color: #fff;
  background: var(--c-orange);
  padding: 4px 12px;
  border-radius: 20px;
  z-index: 2;
}

.template-info {
  padding: 16px 18px;
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
}

.template-info h4 {
  font-size: 14px;
  font-weight: 600;
  color: var(--c-brown);
  margin-bottom: 4px;
}

.template-cat {
  font-size: 12px;
  color: var(--c-text-muted);
}

.price {
  font-family: var(--f-heading);
  font-size: 18px;
  font-weight: 700;
  color: var(--c-orange);
}

.templates-more {
  text-align: center;
  margin-top: 40px;
}

.btn-outline {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 12px 28px;
  font-size: 14px;
  font-weight: 500;
  color: var(--c-text);
  background: none;
  border: 2px solid var(--c-border);
  border-radius: var(--r-md);
  cursor: pointer;
  transition: all 0.3s;
  font-family: var(--f-body);
}

.btn-outline:hover {
  border-color: var(--c-orange);
  color: var(--c-orange);
  background: rgba(232, 114, 42, 0.04);
}

.btn-outline:hover svg {
  transform: translateX(4px);
}

.btn-outline svg {
  transition: transform 0.3s;
}

/* ========== PROCESS ========== */
.process {
  padding: 80px 0;
  background: linear-gradient(180deg, var(--c-cream-dark) 0%, var(--c-cream) 100%);
  position: relative;
}

.process::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 1px;
  background: linear-gradient(90deg, transparent, var(--c-border), transparent);
}

.process-grid {
  display: flex;
  align-items: flex-start;
  justify-content: center;
  gap: 24px;
}

.process-step {
  flex: 1;
  max-width: 280px;
  text-align: center;
  padding: 32px 24px;
  background: var(--c-card);
  border-radius: var(--r-lg);
  border: 1px solid rgba(45, 24, 16, 0.04);
  transition: all 0.3s;
}

.process-step:hover {
  transform: translateY(-4px);
  box-shadow: var(--shadow-md);
}

.step-num {
  font-family: var(--f-display);
  font-size: 40px;
  color: var(--c-orange-light);
  opacity: 0.4;
  line-height: 1;
  margin-bottom: 16px;
}

.step-icon {
  margin-bottom: 16px;
}

.process-step h3 {
  font-family: var(--f-heading);
  font-size: 18px;
  color: var(--c-brown);
  margin-bottom: 8px;
}

.process-step p {
  font-size: 14px;
  color: var(--c-text-muted);
  line-height: 1.6;
}

.process-connector {
  display: flex;
  align-items: center;
  padding-top: 80px;
}
</style>
