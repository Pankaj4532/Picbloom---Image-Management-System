# Picbloom

Picbloom is a modern, responsive, and user-friendly photo gallery and album management web application built with **React**, **Material UI**, and **Vite**. It allows users to register, login, create albums, upload photos, and manage them easily.

---

## Table of Contents

* [Features](#features)
* [Tech Stack](#tech-stack)
* [Getting Started](#getting-started)
* [Available Scripts](#available-scripts)
* [Folder Structure](#folder-structure)
* [Build for Production](#build-for-production)
* [License](#license)

---

## Features

* User registration and login
* Album creation, editing, and deletion
* Photo upload, edit, and delete
* Responsive Material UI design
* State management with React Context API
* Client-server communication using Axios

---

## Tech Stack

* **Frontend:** React, Material UI, Vite
* **State Management:** React Context API
* **Routing:** React Router DOM
* **HTTP Client:** Axios
* **Build Tool:** Vite

---

## Getting Started

### Prerequisites

* Node.js v18+
* npm or yarn
* Backend API running (if needed for full functionality)

### Installation

1. Clone the repository:

```bash
git clone <your-picbloom-repo-url>
cd picbloom/frontend
```

2. Install dependencies:

```bash
npm install --legacy-peer-deps
```

3. Start the development server:

```bash
npm run start
```

4. Open [http://localhost:3000](http://localhost:3000) in your browser.

---

## Available Scripts

In the project directory, you can run:

* `npm run start` – Runs the app in development mode
* `npm run build` – Builds the app for production in the `dist/` folder
* `npm run preview` – Preview the production build locally

---

## Folder Structure

```
frontend/
│
├── src/                   # Source code
│   ├── api/               # API client functions
│   ├── assets/            # Images, fonts, icons
│   ├── components/        # Reusable UI components
│   ├── contexts/          # React Context API for state management
│   ├── hooks/             # Custom React hooks
│   ├── layout/            # Layout components (Header, Footer, Sidebar)
│   ├── pages/             # Application pages
│   ├── routes/            # Route definitions
│   └── utils/             # Utility functions
│
├── public/                # Public assets
├── package.json
├── vite.config.mjs
└── README.md
```

---

## Build for Production

1. Build the project:

```bash
npm run build
```

2. Serve the production build locally for testing:

```bash
npm install -g serve
serve -s dist
```

3. Upload the contents of the `dist/` folder to your hosting platform (Netlify, Vercel, AWS S3, etc.)

---

## License

This project is licensed under the MIT License. See [LICENSE](./LICENSE) for details.

---

## Author

**Pankaj Sapkal**
