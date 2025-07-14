# 🏥 Hospital Management System

A comprehensive web-based Hospital Management System (HMS) built using Java (JSF + PrimeFaces) to streamline the daily operations of a healthcare facility. This system allows administrators and staff to manage patients, staff, medical records, and appointments efficiently in a secure and user-friendly interface.

---

## 🚀 Features

### 👨‍⚕️ Staff Management
- Add and manage hospital staff:
  - **Doctors**
  - **Nurses**
  - **Receptionists**
- Assign roles and responsibilities.

### 👥 Patient Management
- Register new patients.
- View and update patient details.
- Maintain contact and demographic information.

### 📋 Medical Records
- Create and manage **medical records** for each patient.
- Attach notes, diagnoses, and prescriptions to each visit.
- View full medical history of a patient.

### 📅 Appointment Management
- Book appointments with available doctors.
- Ensure time slot validation (e.g., working hours: 8:00 AM - 5:00 PM).
- View, update, or cancel scheduled appointments.
- Avoid double-booking with real-time availability checks.

### 🔐 Authentication & Authorization *(Planned/Optional)*
- Secure login system for staff.
- Role-based access control (e.g., only doctors can view/edit records).

---

## 💻 Technologies Used

- **Backend**: Java EE (Jakarta), JSF (Jakarta Faces), PrimeFaces
- **Frontend**: HTML, CSS, Tailwind CSS (for styling)
- **Database**: MySQL / PostgreSQL
- **Server**: Apache TomEE / WildFly / Payara
- **Others**: CDI, JPA, Facelets

---

## 🧰 How to Run

1. **Clone the Repository**
   ```bash
   git clone https://github.com/helviz/HospitalMangementSystem.git
   cd hospital-management-system
