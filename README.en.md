# Daidaimi Japan Shopping · Cross-border Mini Program

[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)

A complete **Japanese cross-border shopping / proxy buying** frontend solution for Chinese users, supporting Yahoo Auctions and Mercari. This repository includes the **user-facing WeChat mini program**, **admin dashboard**, and a warehouse app scaffold.

> This is a **frontend monorepo** without backend source code. A self-hosted or third-party backend API is required for full operation.

---

## Overview

**Daidaimi Japan Shopping** is a mature proxy-buying system covering the full flow: product browsing, payment, Japan warehouse inbound, consolidated shipping, and domestic delivery.

**Business model:**

- Users search and browse products from Japanese e-commerce platforms
- The platform places orders or bids on behalf of users; goods go to a Japan warehouse first
- Users can merge multiple orders, choose shipping routes, and pay international shipping before delivery to China
- Revenue from exchange rates, service fees, auction deposits, storage/packing/international shipping fees
- Referral code and distributor commission system supported

**Supported platforms:**

| Platform | Mode | Description |
|----------|------|-------------|
| **Yahoo Auctions** | Auction proxy | Scheduled/instant bids, buy-it-now; deposit required |
| **Mercari** | Fixed-price | Shopping cart and batch checkout |

---

## Repository Structure

```
ddmGit/
├── 前台小程序/              # User WeChat mini program (core)
├── 后台管理/                # Admin web dashboard
├── 代代米日淘出入库APP/      # Warehouse app scaffold (WIP)
├── docs/
│   └── wechat-qrcode.png    # Author WeChat QR code
├── README.md
└── README.en.md
```

---

## Tech Stack

| Sub-project | Framework | UI |
|-------------|-----------|-----|
| Mini program | uni-app + Vue 2 | uni-ui |
| Admin | Vue 2 + Vue CLI 5 | Element UI 2 |
| Warehouse app | uni-app + Vue 2 | uni-ui (WIP) |

---

## Quick Start

### Mini program

1. Open `前台小程序/` in HBuilderX
2. Update WeChat AppID in `manifest.json`
3. Update API base URL in `utils/url.js`
4. Run to WeChat DevTools

### Admin dashboard

```bash
cd 后台管理
yarn install
yarn serve
yarn build
```

---

## Backend API

No backend is included. You need REST APIs for auth, product proxy/scraping, orders, WeChat Pay, uploads, etc. See:

- `前台小程序/utils/api.js`
- `后台管理/src/http/api.js`

---

## Contact · Custom Development / Business

This project is open source. For **custom development, private deployment, technical consulting, or business cooperation**:

| Contact | Info |
|---------|------|
| **Phone** | +86 185151626295 |
| **WeChat** | Scan QR code below |

<p align="center">
  <img src="docs/wechat-qrcode.png" alt="WeChat QR Code" width="240">
</p>

Services: deployment guidance, backend integration, feature customization, new platform integration (Rakuten, Amazon, etc.), warehouse app development.

---

## License

[MIT License](LICENSE)
