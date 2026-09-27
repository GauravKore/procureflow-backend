# ProcureFlow — Full-Stack Procurement Management System

ProcureFlow is an enterprise role-based procurement management platform featuring a **Spring Boot REST API** backend and a modern **React + TypeScript (Vite)** frontend.

---

## 🌟 Key Features

- **Role-Based Workflows**: Tailored user experiences & authorization for 4 distinct roles:
  - `EMPLOYEE`: Create, view, and track purchase requests; cancel pending requests.
  - `MANAGER`: Review team purchase requests, approve or reject with decision comments.
  - `PROCUREMENT`: Manage approved requests, assign vendors, create procurement orders, and perform Vendor CRUD.
  - `FINANCE`: Perform financial approvals, disburse payments, and mark transactions as paid.
- **JWT Authentication & Security**: Stateless JWT authentication, Spring Security filter chain, role-based `@PreAuthorize` authorization, and client-side route protection.
- **Visual Procurement Pipeline**: Dynamic step-by-step visual workflow timeline tracking requests from submission to payment execution.
- **Notification Center**: Real-time unread notification count badge and notifications list for user actions.

---

## 🛠 Tech Stack

### Backend
- **Framework**: Spring Boot 4.1.1 (Java 21 / Java 17+)
- **Security**: Spring Security + JWT (`jjwt 0.12.6`)
- **Database**: PostgreSQL (`procureflow` database)
- **OR/M**: Spring Data JPA / Hibernate
- **Server Port**: `http://localhost:8081`

### Frontend
- **Framework**: React 18 + TypeScript + Vite
- **Routing**: React Router DOM v6
- **HTTP Client**: Axios (Centralized client with JWT request interceptor & 401 session expiration handling)
- **Styling**: Tailwind CSS + Custom Enterprise Design System + Lucide Icons
- **Dev Server Port**: `http://localhost:5173`

---

## 🔑 Demo Accounts & Test Credentials

The backend automatically seeds demo accounts on startup via `DataInitializer.java`:

| Role | Email | Password | Allowed Dashboards & Capabilities |
| :--- | :--- | :--- | :--- |
| **EMPLOYEE** | `employee@gmail.com` | `employee123` | Create Requests, My Requests, Request Details, Cancel Request |
| **MANAGER** | `manager@gmail.com` | `manager123` | Manager Dashboard, Review Requests, Approve/Reject Requests, View POs & Vendors |
| **PROCUREMENT** | `procurement@gmail.com` | `procurement123` | Procurement Dashboard, Create Procurement Orders, Manage Vendors (CRUD) |
| **FINANCE** | `finance@gmail.com` | `finance123` | Finance Dashboard, Financial Approvals, Execute Payments, Mark Paid |

---

## 🔄 Procurement Workflow Lifecycle

```
[EMPLOYEE] Create Purchase Request
             │
             ▼
    PENDING_MANAGER_APPROVAL
             │
   ┌─────────┴─────────┐
   │ Manager Approves  │ Manager Rejects
   ▼                   ▼
APPROVED_BY_MANAGER   REJECTED_BY_MANAGER
   │
   │ [PROCUREMENT] Create PO & Assign Vendor
   ▼
VENDOR_ASSIGNED / PENDING_FINANCE_APPROVAL
   │
   ┌─────────┴─────────┐
   │ Finance Approves  │ Finance Rejects
   ▼                   ▼
APPROVED_BY_FINANCE   REJECTED_BY_FINANCE
   │
   │ [FINANCE] Disburse Payment (Mark Paid)
   ▼
COMPLETED / PAID
```

---

## 🚀 Quick Start Guide

### 1. Prerequisites
- **Java**: 17 or 21 installed (`java -version`)
- **Node.js**: v18.18+ or v20+ (`node -v`)
- **PostgreSQL**: Running locally on port `5432` with database `procureflow`
  - Default username: `postgres`
  - Default password: `ProcureFlow@123`

### 2. Backend Startup
From the project root directory:

```bash
# Using Maven Wrapper (Windows)
.\mvnw.cmd spring-boot:run

# Or using Maven Wrapper (Linux/Mac)
./mvnw spring-boot:run
```

The Spring Boot backend will start on **`http://localhost:8081`**.

### 3. Frontend Startup
Navigate to the `frontend` folder:

```bash
cd frontend

# Install dependencies
npm install

# Start Vite development server
npm run dev
```

The React frontend will be accessible at **`http://localhost:5173`**.

---

## ⚙️ Environment Variables

The frontend relies on `.env` (a `.env.example` is provided):

```env
VITE_API_BASE_URL=http://localhost:8081/api
```

---

## 🔒 Security & JWT Architecture

1. **Login Flow**:
   - `POST /api/auth/login` receives `{ email, password }`.
   - On success, the backend returns `{ token, email, name, role }`.
2. **Token Storage**:
   - The token is saved in `localStorage` and managed via React `AuthContext`.
3. **API Requests**:
   - The centralized Axios client in `src/api/axiosClient.ts` automatically attaches `Authorization: Bearer <token>` to every HTTP request.
4. **Session Expiration**:
   - If the backend returns `401 Unauthorized`, the client automatically clears stored auth state and redirects the user to `/login?expired=true`.
5. **Route Guards**:
   - `<ProtectedRoute />` verifies login status.
   - `<RoleRoute allowedRoles={[...]} />` enforces frontend role boundaries (e.g. Employee cannot access Finance pages).
