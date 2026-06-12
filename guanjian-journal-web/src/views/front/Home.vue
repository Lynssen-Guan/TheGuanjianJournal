<template>
  <div class="site-shell">
    <div class="page">
      <header class="masthead">
        <div class="masthead__eyebrow">The Guanjian Journal</div>
        <h1>管见录</h1>
        <p>A modest record of code, books, and thoughts.</p>
      </header>

      <nav class="site-nav" aria-label="主导航">
        <span>首页</span>
        <span>全部文章</span>
        <span>Java 后端</span>
        <span>读书笔记</span>
        <span>关于</span>
      </nav>

      <section class="lead">
        <div class="lead__main">
          <div class="lead__label">Editor's Opening</div>
          <h2>在代码与书页之间，整理自己的理解。</h2>
          <p>
            这里收录 Java 后端学习笔记、数据库与中间件总结、读书摘记和项目复盘。
            每一篇文章都尽量保存当时的思考路径，而不只是最终结论。
          </p>
        </div>

        <div class="quote-box">
          <div class="quote-box__label">EDITOR'S NOTE</div>
          <blockquote>管中窥见，亦可成录；日积月累，方成系统。</blockquote>
        </div>
      </section>

      <main class="content">
        <section class="article-section">
          <div class="section-head">
            <h3>最新刊载</h3>
            <span>全部文章 →</span>
          </div>

          <article
            v-for="(article, index) in latestArticles"
            :key="article.id"
            class="article-card"
          >
            <div class="article-card__num">{{ formatIndex(index) }}</div>

            <div class="article-card__body">
              <div class="article-card__category">
                {{ article.categoryName }}
              </div>

              <h4>{{ article.title }}</h4>

              <p>{{ article.summary }}</p>

              <div class="article-card__meta">
                {{ article.publishDate }} · {{ article.categoryName }}
              </div>

              <div class="tag-row">
                <span
                  v-for="tag in article.tags"
                  :key="tag"
                  class="tag"
                >
                  {{ tag }}
                </span>
              </div>
            </div>
          </article>
        </section>

        <aside class="sidebar">
          <section class="side-block">
            <h4>栏目</h4>
            <ul>
              <li v-for="category in categories" :key="category.id">
                <span>{{ category.name }}</span>
                <em>{{ category.articleCount }} 篇</em>
              </li>
            </ul>
          </section>

          <section class="side-block">
            <h4>最近阅读</h4>
            <ul>
              <li v-for="book in recentBooks" :key="book.id">
                <span>{{ book.name }}</span>
              </li>
            </ul>
          </section>

          <section class="side-block">
            <h4>近期关注</h4>
            <ul>
              <li v-for="topic in focusTopics" :key="topic">
                <span>{{ topic }}</span>
              </li>
            </ul>
          </section>

          <section class="dark-box">
            <strong>关于这个博客</strong>
            <p>
              不做信息搬运，主要记录自己真正理解过、使用过、复盘过的内容。
            </p>
          </section>
        </aside>
      </main>

      <footer class="site-footer">
        © 2026 管见录 · Built by Guan Shengling.
      </footer>
    </div>
  </div>
</template>

<script setup lang="ts">
interface Article {
  id: number
  slug: string
  title: string
  summary: string
  categoryName: string
  publishDate: string
  tags: string[]
}

interface Category {
  id: number
  name: string
  slug: string
  articleCount: number
}

interface Book {
  id: number
  name: string
}

const latestArticles: Article[] = [
  {
    id: 1,
    slug: 'spring-boot-auto-config',
    title: 'Spring Boot 自动配置到底自动在哪里？',
    summary:
      '从 starter、条件注解、配置属性到实际生效过程，梳理 Spring Boot 自动配置的核心链路。',
    categoryName: 'Java 后端',
    publishDate: '2026.06.03',
    tags: ['Java', 'Spring Boot', '自动配置']
  },
  {
    id: 2,
    slug: 'mysql-index-leftmost-prefix',
    title: '联合索引的最左前缀原则，怎么才算真正理解？',
    summary:
      '结合查询条件和执行计划，整理几个非常容易混淆的联合索引判断场景。',
    categoryName: '数据库',
    publishDate: '2026.05.29',
    tags: ['MySQL', '索引', 'SQL']
  },
  {
    id: 3,
    slug: 'effective-java-static-factory',
    title: '《Effective Java》读书笔记：用静态工厂方法代替构造器',
    summary:
      '整理静态工厂方法相比构造器的几个优势，以及在实际项目中的使用边界。',
    categoryName: '读书笔记',
    publishDate: '2026.05.22',
    tags: ['Effective Java', '读书', 'Java']
  },
  {
    id: 4,
    slug: 'redis-cache-problems',
    title: 'Redis 缓存穿透、击穿和雪崩的一次性整理',
    summary:
      '用更接近业务场景的方式理解三类缓存问题，并整理常见解决策略。',
    categoryName: '中间件',
    publishDate: '2026.05.16',
    tags: ['Redis', '缓存', '后端']
  }
]

const categories: Category[] = [
  {
    id: 1,
    name: 'Java 后端',
    slug: 'java',
    articleCount: 12
  },
  {
    id: 2,
    name: '数据库',
    slug: 'database',
    articleCount: 8
  },
  {
    id: 3,
    name: '读书笔记',
    slug: 'reading',
    articleCount: 6
  },
  {
    id: 4,
    name: '项目复盘',
    slug: 'review',
    articleCount: 4
  }
]

const recentBooks: Book[] = [
  {
    id: 1,
    name: 'Effective Java'
  },
  {
    id: 2,
    name: '代码整洁之道'
  },
  {
    id: 3,
    name: '重构：改善既有代码的设计'
  },
  {
    id: 4,
    name: '凤凰架构'
  }
]

const focusTopics = [
  'Spring Boot 源码阅读',
  'JVM 内存模型',
  'MySQL 索引优化',
  '接口设计与异常处理'
]

function formatIndex(index: number) {
  return String(index + 1).padStart(2, '0')
}
</script>

<style scoped>
.site-shell {
  min-height: 100vh;
  padding: 0;
  background: #fbfbfb;
}

.page {
  width: 100%;
  min-height: 100vh;
  margin: 0;
  padding: 30px 96px 70px;
  background: #fbfbfb;
  color: #151515;
  box-shadow: none;
  font-family: "Times New Roman", "Noto Serif SC", "Songti SC", SimSun, serif;
}

.masthead {
  border-top: 4px solid #191919;
  border-bottom: 1px solid #191919;
  padding: 22px 0 18px;
  text-align: center;
}

.masthead__eyebrow {
  font-size: 12px;
  letter-spacing: 0.22em;
  color: #6c6c6c;
  text-transform: uppercase;
  margin-bottom: 8px;
}

.masthead h1 {
  margin: 0;
  font-size: 58px;
  letter-spacing: 0.16em;
  font-weight: 500;
}

.masthead p {
  margin: 10px 0 0;
  font-size: 14px;
  color: #6c6c6c;
  font-family:
    -apple-system,
    BlinkMacSystemFont,
    "Segoe UI",
    "PingFang SC",
    "Microsoft YaHei",
    sans-serif;
}

.site-nav {
  display: flex;
  justify-content: center;
  gap: 28px;
  padding: 13px 0;
  border-bottom: 3px double #191919;
  color: #6c6c6c;
  font-size: 14px;
  font-family:
    -apple-system,
    BlinkMacSystemFont,
    "Segoe UI",
    "PingFang SC",
    "Microsoft YaHei",
    sans-serif;
}

.site-nav span {
  cursor: default;
}

.lead {
  display: grid;
  grid-template-columns: 1.15fr 0.85fr;
  gap: 32px;
  padding: 34px 0;
  border-bottom: 1px solid #191919;
}

.lead__label {
  margin-bottom: 12px;
  color: #a33d2d;
  font-size: 13px;
  letter-spacing: 0.16em;
  text-transform: uppercase;
  font-family:
    -apple-system,
    BlinkMacSystemFont,
    "Segoe UI",
    "PingFang SC",
    "Microsoft YaHei",
    sans-serif;
}

.lead h2 {
  margin: 0;
  font-size: 48px;
  line-height: 1.12;
  font-weight: 500;
  letter-spacing: -0.04em;
}

.lead p {
  color: #6c6c6c;
  font-size: 17px;
  line-height: 2;
  margin: 20px 0 0;
  font-family:
    -apple-system,
    BlinkMacSystemFont,
    "Segoe UI",
    "PingFang SC",
    "Microsoft YaHei",
    sans-serif;
}

.quote-box {
  border-left: 3px solid #a33d2d;
  padding-left: 22px;
  align-self: center;
}

.quote-box__label {
  font-family:
    -apple-system,
    BlinkMacSystemFont,
    "Segoe UI",
    "PingFang SC",
    "Microsoft YaHei",
    sans-serif;
  color: #a33d2d;
  font-size: 13px;
  margin-bottom: 10px;
  letter-spacing: 0.08em;
}

.quote-box blockquote {
  margin: 0;
  font-size: 24px;
  line-height: 1.65;
}

.content {
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: 36px;
  padding-top: 30px;
}

.section-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  border-bottom: 1px solid #191919;
  margin-bottom: 18px;
  padding-bottom: 8px;
}

.section-head h3 {
  margin: 0;
  font-size: 18px;
  letter-spacing: 0.12em;
  font-weight: 500;
}

.section-head span {
  color: #a33d2d;
  font-size: 13px;
  font-family:
    -apple-system,
    BlinkMacSystemFont,
    "Segoe UI",
    "PingFang SC",
    "Microsoft YaHei",
    sans-serif;
}

.article-card {
  display: grid;
  grid-template-columns: 120px 1fr;
  gap: 22px;
  padding: 24px 0;
  border-bottom: 1px solid #dedede;
}

.article-card__num {
  font-size: 44px;
  color: #a33d2d;
  line-height: 1;
}

.article-card__category {
  margin-bottom: 8px;
  color: #a33d2d;
  font-size: 12px;
  letter-spacing: 0.14em;
  text-transform: uppercase;
  font-family:
    -apple-system,
    BlinkMacSystemFont,
    "Segoe UI",
    "PingFang SC",
    "Microsoft YaHei",
    sans-serif;
}

.article-card h4 {
  margin: 0 0 8px;
  font-size: 27px;
  line-height: 1.25;
  font-weight: 500;
}

.article-card p {
  margin: 0 0 12px;
  color: #6c6c6c;
  line-height: 1.85;
  font-size: 15px;
  font-family:
    -apple-system,
    BlinkMacSystemFont,
    "Segoe UI",
    "PingFang SC",
    "Microsoft YaHei",
    sans-serif;
}

.article-card__meta {
  color: #6c6c6c;
  font-size: 13px;
  font-family:
    -apple-system,
    BlinkMacSystemFont,
    "Segoe UI",
    "PingFang SC",
    "Microsoft YaHei",
    sans-serif;
}

.tag-row {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-top: 12px;
}

.tag {
  display: inline-flex;
  border: 1px solid #dedede;
  color: #6c6c6c;
  padding: 3px 9px;
  border-radius: 999px;
  font-size: 12px;
  font-family:
    -apple-system,
    BlinkMacSystemFont,
    "Segoe UI",
    "PingFang SC",
    "Microsoft YaHei",
    sans-serif;
}

.sidebar {
  border-left: 1px solid #dedede;
  padding-left: 26px;
}

.side-block {
  padding-bottom: 28px;
  margin-bottom: 28px;
  border-bottom: 1px solid #dedede;
}

.side-block h4 {
  margin: 0 0 12px;
  font-size: 18px;
  letter-spacing: 0.08em;
  font-weight: 500;
}

.side-block ul {
  margin: 0;
  padding: 0;
  list-style: none;
  font-family:
    -apple-system,
    BlinkMacSystemFont,
    "Segoe UI",
    "PingFang SC",
    "Microsoft YaHei",
    sans-serif;
}

.side-block li {
  display: flex;
  justify-content: space-between;
  gap: 12px;
  padding: 9px 0;
  color: #6c6c6c;
  border-top: 1px solid #dedede;
  font-size: 14px;
}

.side-block li:first-child {
  border-top: 0;
}

.side-block em {
  font-style: normal;
  color: #999;
  white-space: nowrap;
}

.dark-box {
  background: #151515;
  color: white;
  padding: 22px;
  border-radius: 2px;
  font-family:
    -apple-system,
    BlinkMacSystemFont,
    "Segoe UI",
    "PingFang SC",
    "Microsoft YaHei",
    sans-serif;
}

.dark-box p {
  color: rgba(255, 255, 255, 0.72);
  line-height: 1.8;
  margin: 8px 0 0;
  font-size: 14px;
}

.site-footer {
  border-top: 3px double #191919;
  margin-top: 44px;
  padding-top: 16px;
  color: #6c6c6c;
  text-align: center;
  font-size: 13px;
  font-family:
    -apple-system,
    BlinkMacSystemFont,
    "Segoe UI",
    "PingFang SC",
    "Microsoft YaHei",
    sans-serif;
}

@media (max-width: 860px) {
  .site-shell {
    padding: 0;
  }

  .page {
    padding: 24px 28px 56px;
    box-shadow: none;
  }

  .masthead h1 {
    font-size: 42px;
  }

  .site-nav {
    flex-wrap: wrap;
    gap: 16px;
  }

  .lead,
  .content,
  .article-card {
    grid-template-columns: 1fr;
  }

  .lead h2 {
    font-size: 36px;
  }

  .sidebar {
    border-left: 0;
    padding-left: 0;
  }
}

@media (max-width: 520px) {
  .page {
    padding: 22px 18px 52px;
  }

  .masthead h1 {
    font-size: 36px;
    letter-spacing: 0.12em;
  }

  .lead h2 {
    font-size: 30px;
  }

  .quote-box blockquote {
    font-size: 21px;
  }

  .article-card h4 {
    font-size: 23px;
  }
}
</style>
